package aft.reports.charts;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;

public class BarChart extends ApplicationFrame {

    CategoryDataset categoryDataset = null;
    final DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();

    public BarChart(String applicationTitle, String featuresList, String jBehaveReportsPath) throws IOException {
        super(applicationTitle);

        String pass, total, fail;

            File dir = new File(jBehaveReportsPath);

            final String features[] = featuresList.split(",");

            for(int i=0; i<features.length; i++) {
                final int index = i;
                Double passCount=0.0, failCount=0.0,totalCount=0.0;
                File[] statsFiles = dir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
//                    return name.endsWith(".stats");
                        String pattern = "^[a-z]*\\." + features[index].replace(" ","").toLowerCase() + "\\.[a-z A-z]*\\.stats$";
                        return name.matches(pattern);
                    }
                });

                for (File statsFile : statsFiles) {
                    String statsFilePath = (statsFile.getPath());
                    File statFileNames = new File(statsFilePath);

                    //Creating Scanner instances to read File in Java
                    Scanner scanner = new Scanner(statFileNames);
                    String[] result = new String[28];

                    for (int j = 0; j < 27; j++) {
                        result[j] = scanner.next();
                    }
                    pass = result[13];
                    total = result[17];
                    fail = result[25];

                    failCount = failCount + Integer.parseInt(fail.split("=", fail.length())[1]);
                    passCount = passCount + Integer.parseInt(pass.split("=", pass.length())[1]);
                    totalCount = totalCount + Integer.parseInt(total.split("=", total.length())[1]);

                    if (Integer.parseInt(total.split("=", total.length())[1]) > 0) {
                        categoryDataset = createDataSet(features[index], totalCount, passCount, failCount);
                    }
                }
            }

            /* For displaying count on bars*/
            render3dBarChart(applicationTitle, jBehaveReportsPath + "view/FeatureChart.png");

        }

    private CategoryDataset createDataSet(String storyName, Double storyTotal, Double storyPass, Double storyFail) {
        final String total = "Total";
        final String pass = "Pass";
        final String fail = "Fail";
        defaultCategoryDataset.addValue(storyTotal, total, storyName);
        defaultCategoryDataset.addValue(storyPass, pass, storyName);
        defaultCategoryDataset.addValue(storyFail, fail, storyName);
        return defaultCategoryDataset;
    }

    private void render3dBarChart(String applicationTitle, String saveBarChartTpPath) throws IOException {
      /* For displaying count on bars*/
        BarRenderer3D renderer3D = new BarRenderer3D();
        renderer3D.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer3D.setBaseItemLabelsVisible(true);
        renderer3D.setSeriesPaint(0, new java.awt.Color(102,178,255));
        renderer3D.setSeriesPaint(1, new java.awt.Color(178,255,102));
        renderer3D.setSeriesPaint(2, new java.awt.Color(255,153,153));

        JFreeChart barChart = ChartFactory.createBarChart3D(
                applicationTitle,
                "Features",
                "Scenarios",
                categoryDataset,
                PlotOrientation.VERTICAL,
                true, true, true);

        barChart.setBackgroundPaint(java.awt.Color.white);
        barChart.getCategoryPlot().setRenderer(renderer3D);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1500, 700));
        setContentPane(chartPanel);
        File BarChart = new File(saveBarChartTpPath);
        ChartUtilities.saveChartAsPNG( BarChart , barChart , 1100, 500);
    }

    public static void createBarChart(String applicationTitle, String features, String jBehaveReportsPath) throws IOException {
        BarChart barChart = new BarChart(applicationTitle, features, jBehaveReportsPath);
        barChart.pack();
        RefineryUtilities.centerFrameOnScreen(barChart);
        barChart.setVisible(false);
    }
}

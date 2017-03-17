package aft.reports.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;

public class PerFeaturePieChart {

    public void createPieChart(final String chartTitle, String jBehaveReportsPath) throws IOException {

        String pass, total, fail, pending;
        Double passCount=0.0, failCount=0.0,totalCount=0.0,pendingCount=0.0;

            File dir = new File(jBehaveReportsPath);
            File[] statsFiles = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {

                    //return name.endsWith(".stats");
                    String pattern = "^[a-z]*\\." + chartTitle.replace(" ","").toLowerCase() + "\\.[a-z A-z]*\\.stats$";
                    return name.matches(pattern);
                }
            });

            for (File statsFile : statsFiles) {
                String statsFilePath = (statsFile.getPath());
                File statFileNames= new File(statsFilePath);

                //Creating Scanner instances to read File in Java
                Scanner scnr = new Scanner(statFileNames);
                String[] result = new String[28];

                for (int i = 0; i < 27; i++) {
                    result[i] = scnr.next();
                }
                pass = result[13];
                total = result[17];
                fail = result[25];
                pending = result[22];

                failCount = failCount + Integer.parseInt(fail.split("=", fail.length())[1]);
                passCount = passCount + Integer.parseInt(pass.split("=", pass.length())[1]);
                totalCount = totalCount + Integer.parseInt(total.split("=", total.length())[1]);
                pendingCount = pendingCount + Integer.parseInt(pending.split("=", pending.length())[1]);

                if (Integer.parseInt(total.split("=",total.length())[1])>0){
                    drawChart(failCount, pendingCount, passCount, totalCount, chartTitle + "FeaturePieChart", jBehaveReportsPath);
                }
            }
    }

    public static void drawChart(Double fail, Double pending, Double pass, Double total, String storyName, String jBehaveReportsPath) throws IOException {

        final DefaultPieDataset data = new DefaultPieDataset();
        Double failPercent, passPercent, pendingPercent;

        /* Calculating pending count as JBehave is calculating incorrectly */
        if(pass+fail==total){pending =0.0;}

        failPercent = Double.valueOf(Math.round((fail/total)*100));
        passPercent = Double.valueOf(Math.round((pass/total)*100));
        pendingPercent = Double.valueOf(Math.round((pending/total)*100));

        data.setValue("Fail = " + failPercent + "%", fail);
        data.setValue("Pending = " + pendingPercent +"%", pending);
        data.setValue("Pass = " + passPercent + "%", pass);

        JFreeChart chart;
        final boolean drillDown = true;

        if (drillDown) {

            final PiePlot plot = new PiePlot3D(data);
            Color green = new Color(178,255,102);
            Color yellow = new Color(255,255,153);
            Color red = new Color(255,153,153);

            plot.setSectionPaint("Fail = " + failPercent + "%", red);
            plot.setSectionPaint("Pending = " + pendingPercent + "%", yellow);
            plot.setSectionPaint("Pass = " + passPercent + "%", green);
            plot.setOutlineVisible(false);
            plot.setURLGenerator(new StandardPieURLGenerator("pie_chart_detail.jsp"));

            chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        } else {
            chart = ChartFactory.createPieChart3D(
                    "AFT Results",  // barchart title
                    data,               // data
                    true,               // include legend
                    true,
                    true
            );
        }
        chart.setBackgroundPaint(java.awt.Color.white);

            final ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            final File file1 = new File(jBehaveReportsPath + "view/" + storyName + ".png");
            ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);
    }
}

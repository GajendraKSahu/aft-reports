package aft.reports;


import aft.reports.charts.BarChart;
import aft.reports.charts.PerFeaturePieChart;
import aft.reports.charts.PieChart;
import aft.reports.jbehave.JBehaveReport;

import java.io.IOException;

public class GenerateReport {

    private PieChart pieChart = new PieChart();
    private PerFeaturePieChart perFeaturePieChart = new PerFeaturePieChart();
    private BarChart barChart;
    private JBehaveReport jBehaveReport;

    public void generateReport(String applicationTitle, String features, String jBehaveReportsPath) throws IOException {

        /* Creating pie chart for each story and the consolidated results */
        pieChart.createPieChart(jBehaveReportsPath);

        /* Creating feature pie chart*/
        //perFeaturePieChart.createPieChart("AFT Features Bar Chart", jBehaveReportsPath);

        /* Creating feature bar chart*/
        barChart.createBarChart(applicationTitle, features, jBehaveReportsPath);

        /*Updating the adoc href to point to correct adoc files*/
        jBehaveReport.updateJBehaveReports(jBehaveReportsPath);
    }
}

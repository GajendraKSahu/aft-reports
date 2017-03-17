package aft.reports.jbehave;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import aft.reports.utils.FilesUtils;

import java.io.File;
import java.io.IOException;

public class JBehaveReport {

    public void updateJBehaveReports(String jBehaveReportsPath){
        moveAdocFiles(jBehaveReportsPath);
        deleteAdocHtmlFiles(jBehaveReportsPath);
        updateAdocHref(jBehaveReportsPath);
        addConsolidatedPieChart(jBehaveReportsPath);
    }

    public void updateAdocHref(String jBehaveReportsPath){
        Document html = null;
        try {
            html = Jsoup.parse(new File(jBehaveReportsPath + "view/reports.html"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i=0; i<html.getElementsByAttributeValueContaining("href", "adoc").size(); i++) {
            html.
                    getElementsByAttributeValueContaining("href", "adoc")
                    .get(i)
                    .attr("href", html
                                    .getElementsByAttributeValueContaining("href", "adoc").get(i).attr("href").replace(".html",""));
        }
        final File f = new File(jBehaveReportsPath + "view/reports.html");
        try {
            FileUtils.writeStringToFile(f, html.outerHtml(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addConsolidatedPieChart(String jBehaveReportsPath){
        Document html = null;
        try {
            html = Jsoup.parse(new File(jBehaveReportsPath + "view/reports.html"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        html.getElementById("mainTable").append("" +
                "<div>" +
                "<IMG SRC=\"UBO Consolidated Stories Chart.png\" WIDTH=\"700\" HEIGHT=\"500\" BORDER=\"0\" USEMAP=\"#barchart\">" +
                "<IMG SRC=\"FeatureChart.png\" WIDTH=\"1100\" HEIGHT=\"500\" BORDER=\"0\" USEMAP=\"#barchart\">" +
                "</div>" +
                "<br>" +
                "<br>" +
                "<h2>UBO Aggregate Report</h2> "
        );
        final File f = new File(jBehaveReportsPath + "view/reports.html");
        try {
            FileUtils.writeStringToFile(f, html.outerHtml(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveAdocFiles(String jBehaveReportsPath) {
        File dir = new File(jBehaveReportsPath);
        FilesUtils.copyFiles(dir, jBehaveReportsPath + "view/", ".adoc");
    }
    private void deleteAdocHtmlFiles(String jBehaveReportsPath) {
        File dir = new File(jBehaveReportsPath  + "/view/");
        FilesUtils.deleteFiles(dir,".adoc.html");
    }
}

package com.twopi.tutorial.client.javafx;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.twopi.tutorial.servlet.TweetMoodResponse;
import com.twopi.tutorial.utils.Constants;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * Class for Visualizing Tweet Sentiment Analysis. It can display data in Pie Chart, Bar Chart, or Bubble Chart.
 * 
 * @author arshad01
 *
 */
public class VisualizeSentiments extends Application {

    private String _query;
    private String _chartType;
    private boolean _ignoreNeutral;

    @Override
    public void start(Stage stage) throws Exception {
        
        // Read properties from command line
        _query = System.getProperty("query", "$hpq"); 
        _chartType = System.getProperty("chart","pie");
        _ignoreNeutral = Boolean.valueOf(System.getProperty("ignoreNeutral","false")).booleanValue();
        
        String title = "Sentiment Analysis for \""+_query+"\"";

        TweetMoodClient client = new TweetMoodClient();
        
        TweetMoodResponse data = client.getTweets(_query);
        
        if (_chartType.toLowerCase().equals("pie")) {
            showChart(stage,preparePieChart(title,data));
        } else if (_chartType.toLowerCase().equals("bar")) {
            showChart(stage, prepareBarChart(title,data));
        } else if (_chartType.toLowerCase().equals("bubble")) {
            showChart(stage, prepareBubbleChart(title,data));
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    private void showChart(Stage stage, Chart chart) {
        Group group = new Group(chart);
        Scene scene = new Scene(group);
        stage.setWidth(500);
        stage.setHeight(500);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Displays BubbleChart. Note that for X-Axis Day numbers are used since Bubble Chart does not support
     * Categories on X-Axis.
     * @param title
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    private BubbleChart<Number,Number> prepareBubbleChart(String title, TweetMoodResponse data) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
 
        BubbleChart<Number,Number> bubble = new BubbleChart<Number,Number>(xAxis, yAxis);
        
        Map<String,String> dailyData = data.getStats().get(Constants.DB_DAILY_STATS);
        
        XYChart.Series<Number, Number> positiveSeries = new XYChart.Series<Number,Number>();
        XYChart.Series<Number, Number> negativeSeries = new XYChart.Series<Number,Number>();
        XYChart.Series<Number, Number> neutralSeries = new XYChart.Series<Number,Number>();
        
        positiveSeries.setName("positive");
        negativeSeries.setName("negative");
        neutralSeries.setName("neutral");
        
        xAxis.setLabel("Day No.");
        yAxis.setLabel("Count");
        
        Set<String> days = new HashSet<String>();
        
        if (dailyData != null) {
            for (String k : dailyData.keySet()) {
                String[] parts = k.split("_");
                
                days.add(parts[0]);
                Integer value = Integer.valueOf(dailyData.get(k));
                XYChart.Data<Number, Number> dData = new XYChart.Data<Number,Number>(days.size(),value);
                
                if (parts[1].equals("neutral")) {
                    neutralSeries.getData().add(dData);
                } else if (parts[1].equals("positive")) {
                    positiveSeries.getData().add(dData);
                } else {
                    negativeSeries.getData().add(dData);
                }
            }
            
            xAxis.setLowerBound(0);
            xAxis.setUpperBound(days.size());
            xAxis.setTickLength(1.0);
 
            if (_ignoreNeutral) {
                bubble.getData().addAll(positiveSeries,negativeSeries);
            } else { 
                bubble.getData().addAll(positiveSeries,negativeSeries,neutralSeries);
            }
        }
        
        bubble.setTitle(title);
        
        return bubble;
        
    }

    /**
     * Displays BarChart. Days are used on X Axis and bar height shows the count of respective sentiment on
     * that day
     * @param title
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    private BarChart<String,Number> prepareBarChart(String title, TweetMoodResponse data) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
 
        BarChart<String,Number> bar = new BarChart<String,Number>(xAxis, yAxis);
        
        Map<String,String> dailyData = data.getStats().get(Constants.DB_DAILY_STATS);
        
        XYChart.Series<String, Number> positiveSeries = new XYChart.Series<String,Number>();
        XYChart.Series<String, Number> negativeSeries = new XYChart.Series<String,Number>();
        XYChart.Series<String, Number> neutralSeries = new XYChart.Series<String,Number>();
        
        positiveSeries.setName("positive");
        negativeSeries.setName("negative");
        neutralSeries.setName("neutral");
        
        xAxis.setLabel("Day");
        yAxis.setLabel("Count");
        
        ObservableList<String> daysCat = FXCollections.observableArrayList();
        Set<String> days = new HashSet<String>();
        
        if (dailyData != null) {
            for (String k : dailyData.keySet()) {
                String[] parts = k.split("_");
                
                days.add(parts[0]);
                Integer value = Integer.valueOf(dailyData.get(k));
                XYChart.Data<String, Number> dData = new XYChart.Data<String,Number>(parts[0],value);
                
                if (parts[1].equals("neutral")) {
                    neutralSeries.getData().add(dData);
                } else if (parts[1].equals("positive")) {
                    positiveSeries.getData().add(dData);
                } else {
                    negativeSeries.getData().add(dData);
                }
            }
            
            daysCat.addAll(days);
            xAxis.setCategories(daysCat);
            
            if (_ignoreNeutral) {
                bar.getData().addAll(positiveSeries,negativeSeries);
            } else { 
                bar.getData().addAll(positiveSeries,negativeSeries,neutralSeries);
            }
        }
        
        bar.setTitle(title);
        
        return bar;
        
    }

    /**
     * Displays Pie Chart. Only uses total values for each sentiment
     * @param title
     * @param data
     * @return
     */
    private PieChart preparePieChart(String title, TweetMoodResponse data) {
        Map<String,Map<String,String>> allStats = data.getStats();
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        Map<String,String> stats = allStats.get(Constants.DB_TOTAL_STATS);
        
        if (stats != null) {
            for (String k : stats.keySet()) {
                
                long value = Long.valueOf(stats.get(k));
                
                if (!k.equals("neutral")) {
                    pieChartData.add(new PieChart.Data(k, value));
                } else if (!_ignoreNeutral) {
                    pieChartData.add(new PieChart.Data(k, value));
                }
            }
        }
        
         PieChart pieChart  = new PieChart(pieChartData);
         pieChart.setTitle(title);
         return pieChart;
    }
    
    

}

package com.ifmo.math4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private LineChart<Double, Double> plot;
    @FXML
    private TextField velocityTextView;
    @FXML
    private TextField kappaTextView;
    @FXML
    private TextField dtTextView;
    @FXML
    private TextField dxTextView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void addPlot(String name, double[] xAxis, double[] yAxis) {
        if (xAxis.length != yAxis.length) {
            throw new IllegalArgumentException("xAxis and yAxis length is different");
        }

        ObservableList<XYChart.Series<Double, Double>> lineChartData = plot.getData();
        LineChart.Series<Double, Double> series = new LineChart.Series<Double, Double>();
        series.setName(name);
        for (int i = 0; i < xAxis.length; i++) {
            series.getData().add(new XYChart.Data<Double, Double>(xAxis[i], yAxis[i]));
        }
        lineChartData.add(series);
    }

    private void clearPlot() {
        plot.setData(FXCollections.<XYChart.Series<Double, Double>>observableArrayList());
    }

}

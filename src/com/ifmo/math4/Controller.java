package com.ifmo.math4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

    @FXML
    private CheckBox explicitUpstream;
    @FXML
    private CheckBox explicitDownstream;
    @FXML
    private CheckBox implicitUpstream;
    @FXML
    private CheckBox implicitDownstream;
    @FXML
    private CheckBox staggeredGrid;

    private CheckBox[] methods;

    @FXML
    private Button run;
    @FXML
    private Button stop;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methods = new CheckBox[]{explicitUpstream, explicitDownstream, implicitUpstream, implicitDownstream, staggeredGrid};
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

    @FXML
    private void explicitUpstreamChanged(ActionEvent event) {
        changeCheckBoxesState(explicitUpstream);
    }

    @FXML
    private void explicitDownstreamChanged(ActionEvent event) {
        changeCheckBoxesState(explicitDownstream);
    }

    @FXML
    private void implicitUpstreamChanged(ActionEvent event) {
        changeCheckBoxesState(implicitUpstream);
    }

    @FXML
    private void implicitDownstreamChanged(ActionEvent event) {
        changeCheckBoxesState(implicitDownstream);
    }

    @FXML
    private void staggeredGridChanged(ActionEvent event) {
        changeCheckBoxesState(staggeredGrid);
    }

    private void changeCheckBoxesState(CheckBox checkBox) {
        int index = -1;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].equals(checkBox)) {
                index = i;
                break;
            }
        }

        if (methods[index].isSelected()) {
            for (int j = 0; j < methods.length; j++) {
                if (index != j) {
                    methods[j].setSelected(false);
                }
            }
        }
    }

}

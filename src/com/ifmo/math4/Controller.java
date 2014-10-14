package com.ifmo.math4;

import com.ifmo.math4.schemes.AbstractScheme;
import com.ifmo.math4.schemes.ExplicitDownstreamScheme;
import com.ifmo.math4.schemes.ExplicitUpstreamScheme;
import javafx.application.Platform;
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
import java.util.Timer;
import java.util.TimerTask;

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
    private TextField numberTextView;

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

    private double velocity;
    private double kappa;
    private double dx;
    private double dt;
    private int number;

    private Timer timer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methods = new CheckBox[]{explicitUpstream, explicitDownstream, implicitUpstream, implicitDownstream, staggeredGrid};
    }

    private void setPlot(String name, double[] xAxis, double[] yAxis) {
        if (xAxis.length != yAxis.length) {
            throw new IllegalArgumentException("xAxis and yAxis length is different");
        }

        ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();
        LineChart.Series<Double, Double> series = new LineChart.Series<Double, Double>();
        series.setName(name);
        for (int i = 0; i < xAxis.length; i++) {
            series.getData().add(new XYChart.Data<Double, Double>(xAxis[i], yAxis[i]));
        }
        lineChartData.add(series);
        plot.setData(lineChartData);
    }

    @FXML
    private void runClick(ActionEvent event) throws InterruptedException {
        int index = -1;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isSelected()) {
                index = i;
                break;
            }
        }

        initParams();
        double[] f = new double[number];
        double[] x = new double[number];
        for (int i = 0; i < number; i++) {
            x[i] = dx * i;
            if (i < number / 5) {
                f[i] = 1;
            }
        }

        AbstractScheme scheme = null;
        switch (index) {
            case 0:
                scheme = new ExplicitUpstreamScheme(velocity, kappa, dx, dt, f, a -> 1., b -> 0.);
                break;
            case 1:
                scheme = new ExplicitDownstreamScheme(velocity, kappa, dx, dt, f, a -> 1., b -> 0.);
                break;
            case 2:
//                scheme = new ImplicitUpstreamScheme();
//                break;
            case 3:
//                scheme = new ImplicitDownstreamScheme();
//                break;
            case 4:
//                scheme = new StaggeredGridScheme();
//                break;
            default:
                return;
        }

        setPlot("asd", x, f);
        timer = new Timer();
        timer.schedule(new Drawer(scheme, x, "asd"), 300, 300);
    }

    private class Drawer extends TimerTask {
        private AbstractScheme scheme;
        private double[] x;
        private String name;

        public Drawer(AbstractScheme scheme, double[] x, String name) {
            this.scheme = scheme;
            this.x = x;
            this.name = name;
        }

        @Override
        public void run() {
            Platform.runLater(() -> setPlot(name, x, scheme.nextTimeLayer()));
        }
    }

    private void initParams() {
        try {
            velocity = Double.parseDouble(velocityTextView.getText());
            kappa = Double.parseDouble(kappaTextView.getText());
            dx = Double.parseDouble(dxTextView.getText());
            dt = Double.parseDouble(dtTextView.getText());
            number = Integer.parseInt(numberTextView.getText());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }

    @FXML
    private void stopClick(ActionEvent event) {
        timer.cancel();
        System.out.println("STOP");
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

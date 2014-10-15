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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class Controller implements Initializable {

    private static final long DELAY = 300;

    private Random random = new Random();

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
    private ChoiceBox<String> initFunctions;
    @FXML
    private ChoiceBox<String> leftFunctions;
    @FXML
    private ChoiceBox<String> rightFunctions;

    @FXML
    private Button start;
    @FXML
    private Button pause;
    @FXML
    private Button resume;

    private double velocity;
    private double kappa;
    private double dx;
    private double dt;
    private int number;

    private Timer timer = new Timer();
    private AbstractScheme scheme;
    private double[] x;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methods = new CheckBox[]{explicitUpstream, explicitDownstream, implicitUpstream, implicitDownstream, staggeredGrid};
        plot.setCreateSymbols(false);
        plot.setLegendVisible(false);
        plot.getStyleClass().add("thick-chart");
    }

    private void setPlot(double[] xAxis, double[] yAxis) {
        if (xAxis.length != yAxis.length) {
            throw new IllegalArgumentException("xAxis and yAxis length is different");
        }

        ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();
        LineChart.Series<Double, Double> series = new LineChart.Series<>();
        for (int i = 0; i < xAxis.length; i++) {
            series.getData().add(new XYChart.Data<>(xAxis[i], yAxis[i]));
        }
        lineChartData.add(series);
        plot.setData(lineChartData);
    }

    @FXML
    private void startClick(ActionEvent event) throws InterruptedException {
        pauseClick(event);
        int index = -1;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isSelected()) {
                index = i;
                break;
            }
        }

        initParams();
        double[] f = initValues();

        switch (index) {
            case 0:
                scheme = new ExplicitUpstreamScheme(velocity, kappa, dx, dt, f, getBoundFunction(leftFunctions), getBoundFunction(rightFunctions));
                break;
            case 1:
                scheme = new ExplicitDownstreamScheme(velocity, kappa, dx, dt, f, getBoundFunction(leftFunctions), getBoundFunction(rightFunctions));
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

        setPlot(x, f);
        resumeClick(event);
    }

    private class Drawer extends TimerTask {
        private AbstractScheme scheme;
        private double[] x;

        public Drawer(AbstractScheme scheme, double[] x) {
            this.scheme = scheme;
            this.x = x;
        }

        @Override
        public void run() {
            Platform.runLater(() -> setPlot(x, scheme.nextTimeLayer()));
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

    private double[] initValues() {
        Function<Integer, Double> function;
        switch (initFunctions.getValue()) {
            case "x < n / 5 ? 1 : 0":
                function = i -> i < number / 5 ? 1. : 0.;
                break;
            case "Delta":
                function = i -> i == number / 2 ? 100. : 0.;
                break;
            case "Random":
                function = i -> random.nextDouble();
                break;
            default:
                return null;
        }

        x = new double[number];
        double[] f = new double[number];
        for (int i = 0; i < number; i++) {
            x[i] = dx * i;
            f[i] = function.apply(i);
        }
        return f;
    }

    private Function<Double, Double> getBoundFunction(ChoiceBox<String> choiceBox) {
        switch (choiceBox.getValue()) {
            case "0":
                return x -> 0.;
            case "1":
                return x -> 1.;
            default:
                return null;
        }
    }

    @FXML
    private void pauseClick(ActionEvent event) {
        timer.cancel();
    }

    @FXML
    private void resumeClick(ActionEvent event) {
        timer = new Timer();
        if (scheme != null) {
            timer.schedule(new Drawer(scheme, x), 0, DELAY);
        }
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

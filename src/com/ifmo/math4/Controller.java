package com.ifmo.math4;

import com.ifmo.math4.schemes.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class Controller implements Initializable {

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
    private TextField updateTextView;

    @FXML
    private Label sLabel;
    @FXML
    private Label rLabel;

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
    @FXML
    private CheckBox showPoints;

    private CheckBox[] methods;

    @FXML
    private ChoiceBox<String> initFunctions;

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

    private Timer timer;
    private AbstractScheme scheme;
    private double[] x;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methods = new CheckBox[]{explicitUpstream, explicitDownstream, implicitUpstream, implicitDownstream, staggeredGrid};
        plot.setCreateSymbols(false);
        plot.setLegendVisible(false);
        plot.getStyleClass().add("thick-chart");

        initParams();
        resume.setDisable(true);

        velocityTextView.textProperty().addListener((observable, oldValue, newValue) -> initParams());
        kappaTextView.textProperty().addListener((observable, oldValue, newValue) -> initParams());
        dxTextView.textProperty().addListener((observable, oldValue, newValue) -> initParams());
        dtTextView.textProperty().addListener((observable, oldValue, newValue) -> initParams());
        numberTextView.textProperty().addListener((observable, oldValue, newValue) -> initParams());
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
        resume.setDisable(true);
        int index = -1;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isSelected()) {
                index = i;
                break;
            }
        }

        double[] f = initValues();

        scheme = getScheme(index, f);
        if (scheme == null) {
            return;
        }

        setPlot(x, f);
        resumeClick(event);
    }

    private AbstractScheme getScheme(int i, double[] initValues) {
        switch (i) {
            case 0:
                return new ExplicitUpstreamScheme(velocity, kappa, dx, dt, initValues);
            case 1:
                return new ExplicitDownstreamScheme(velocity, kappa, dx, dt, initValues);
            case 2:
                return new ImplicitUpstreamScheme(velocity, kappa, dx, dt, initValues);
            case 3:
                return new ImplicitDownstreamScheme(velocity, kappa, dx, dt, initValues);
            case 4:
                AbstractScheme startingScheme = getScheme(0, initValues); // this index should be specified by user
                double[] secondLayer = startingScheme.nextTimeLayer();
                return new StaggeredGridScheme(velocity, kappa, dx, dt, initValues, secondLayer);
            default:
                return null;
        }
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
            double[] f = scheme.nextTimeLayer();
            for (double value : f) {
                if (value >= 10e10 || Double.isNaN(value) || Double.isInfinite(value)) {
                    pauseClick(null);
                    Platform.runLater(() -> {
                        Stage stage = new Stage();
                        stage.initModality(Modality.WINDOW_MODAL);
                        Text message = new Text("Too large values for display on graph");
                        message.setFont(new Font(20));
                        Scene scene = new Scene(VBoxBuilder.create().
                                children(message).
                                alignment(Pos.CENTER).padding(new Insets(5)).build());
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.setOpacity(.8);
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.show();
                        Timer closeTimer = new Timer();
                        closeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(stage::close);
                                closeTimer.cancel();
                            }
                        }, 2000);
                    });
                    return;
                }
            }
            Platform.runLater(() -> setPlot(x, f));
        }
    }

    private void initParams() {
        try {
            velocity = Double.parseDouble(velocityTextView.getText());
            kappa = Double.parseDouble(kappaTextView.getText());
            dx = Double.parseDouble(dxTextView.getText());
            dt = Double.parseDouble(dtTextView.getText());
            number = Integer.parseInt(numberTextView.getText());
            sLabel.setText(String.format("s = %.4f", velocity * dt / dx));
            rLabel.setText(String.format("r = %.4f", kappa * dt / (dx * dx)));
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

    @FXML
    private void pauseClick(ActionEvent event) {
        if (timer != null) {
            timer.cancel();
            resume.setDisable(false);
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @FXML
    private void resumeClick(ActionEvent event) {
        timer = new Timer();
        if (scheme != null) {
            timer.schedule(new Drawer(scheme, x), 0, Long.parseLong(updateTextView.getText()));
        }
        resume.setDisable(true);
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

    @FXML
    private void showPointsChanged(ActionEvent event) {
        plot.setCreateSymbols(showPoints.isSelected());
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

package sample;

import com.fazecast.jSerialComm.SerialPort;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import sample.CtrlEvent;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    public Button btnConnect;

    @FXML
    public ChoiceBox portsChoiceBox;

    @FXML
    public Button btnRefreshConnections;

    @FXML
    public TextField txtTimeout;

    @FXML
    public TableView<ReadEventSummaries> eventsTable;

    @FXML
    public TableColumn<ReadEventSummaries, String> summaryColumn;

    @FXML
    public Label lblTest;

    ar721h_controller ar721h;
    boolean connected = false;
    sample.CtrlEvent ctrlEvent;
    private Thread controllerThread;
    public ObservableList<ReadEventSummaries> RES = FXCollections.observableArrayList();

    public void connectButtonClicked() {
        if(!connected) {
            String devicePortName = (String)portsChoiceBox.getValue();
            try {
                this.ar721h = new ar721h_controller(devicePortName);
                this.connected = true;
                btnConnect.setText("Disconnect");
                workController();
            } catch(Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        } else {
            try {
                this.controllerThread.interrupt();
                System.out.println("thread.isInterrupted: " + controllerThread.isInterrupted());
//                this.RES.clear();
                this.ar721h.disconnectPort(this.ar721h.getPort());
                this.connected = false;
                btnConnect.setText("Connect");
            } catch(Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }

    public synchronized void addRES(CtrlEvent event) {
//        this.RES.clear();

        summaryColumn.setMinWidth(200);
        summaryColumn.setCellValueFactory(new PropertyValueFactory<>("summary"));

        lblTest.setText("" + RES.size());
        this.RES.add(new ReadEventSummaries(event.getSummary()));
        eventsTable.setItems(RES);

        for(int i = 0; i < this.RES.size(); i++) {
            System.out.println("i: " + i);
            System.out.println("this.RES(i).getSummary: " + this.RES.get(i).getSummary());
        }
    }

    public void workController() {
        int timeout = Integer.parseInt(txtTimeout.getText());
        Main.ReadEvents currentEvent = null;
        summaryColumn.setCellValueFactory(new PropertyValueFactory<>("summary"));

        this.addRES(new CtrlEvent("Connected to controller"));

        lblTest.setText("Connected");

        this.controllerThread = new Thread(new runController(ar721h, RES, eventsTable, timeout, lblTest, this));
        this.controllerThread.start();
    }

    public static void addToTestLabel(String s, Label lbl) {
        lbl.setText(lbl + "\n" + s);
    }

    public void refreshCOMPortsChoiceBox() {
        SerialPort[] ports = SerialPort.getCommPorts();
        List<String> portList = new LinkedList<String>();
        for(SerialPort p : ports)
            portsChoiceBox.getItems().add(p.getDescriptivePortName());

        if(!portsChoiceBox.getItems().isEmpty())
            portsChoiceBox.setValue(portsChoiceBox.getItems().get(0));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.refreshCOMPortsChoiceBox();
    }
}

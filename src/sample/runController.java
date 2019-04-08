package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class runController implements Runnable {

    Thread t;
    ar721h_controller ar721h;
    CtrlEvent ctrlEvent;
    Main.ReadEvents currentEvent;
    ObservableList<ReadEventSummaries> RES;
    TableView<ReadEventSummaries> eventsTable;
    int timeout = 1000;
    Label lblTest;
    Controller ctrl;

    public runController(ar721h_controller controller, ObservableList<ReadEventSummaries> RES, TableView<ReadEventSummaries> eventsTable,
                         int timeout, Label lblTest, Controller ctrl) {
        this.ar721h = controller;
        this.RES = RES;
        this.eventsTable = eventsTable;
        this.timeout = timeout;
        this.lblTest = lblTest;
        this.ctrl = ctrl;
    }

    @Override
    public void run() {
        while(true){
            try {
                ar721h.writeCheck();
                while(ar721h.getPort().bytesAvailable() < 1)
                    Thread.sleep(timeout);
//                controllerSleep(timeout);
                synchronized (this) {
                    Platform.runLater(() -> {
                        synchronized (this) {
                            this.ctrlEvent = ar721h.readEvent();
                            currentEvent = this.ctrlEvent.getEventType();
                            ctrl.addRES(ctrlEvent);
//                            RES.add(new ReadEventSummaries(ctrlEvent.getSummary()));
//                            ctrl.addRES(RES);
                        }
                    });
//                    for(int i = 0; i<RES.size(); i++) {
//                        System.out.println(RES.get(i).getSummary());
//                    }
                }

//                Controller.addToTestLabel(ctrlEvent.getSummary(), lblTest);
//                this.ctrl.addToTestLabel(ctrlEvent.getSummary());
//                this.lblTest.setText(this.ctrlEvent.getSummary());
//                RES.add(new ReadEventSummaries(ctrlEvent.getSummary()));
//                eventsTable.getItems().add(new ReadEventSummaries(ctrlEvent.getSummary()));
//                System.out.println(this.ctrlEvent.stringifyEvent());
            } catch(Exception e) {
                System.out.println("AR721H controller error");
                System.out.println(e.getMessage());
            }
        }
    }

//    public void start() {
//        if(t == null) {
//            t = new Thread(this, "controller");
//            t.start();
//        }
//    }
}

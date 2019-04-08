package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.fazecast.jSerialComm.*;

public class Main extends Application {

    public static String devicePortName = "COM3";
    public static SerialPort sPort = null;
    public static int PACKET_SIZE_IN_BYTES = 0;

    public enum Command {
        CHECK,
        DELETE
    }

    public enum ReadEvents {
        NOEVENT, //04
        INVALIDCARD, //03
        VALIDCARD, //0B
        DELETEEVENT
    }

    SerialPort[] ports = SerialPort.getCommPorts();
    SerialPort comPort;

//    byte[] check = new byte[] { 0x7E, 0x04, 0x01, 0x25, (byte) 0xDB, 0x01, 0x00};
//    byte[] deleteData = new byte[] { 0x7E, 0x07, 0x01, 0x37, (byte) 0x44, 0x45, 0x4c, (byte) 0x84, (byte) 0x91, 0x00};

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("AR721H");
//        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnHidden(e -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }


//    @Override
//    public void start(Stage primaryStage) throws Exception{
//
//
//        System.out.println(Command.DELETE);
//
//        FlowPane layout = new FlowPane();
//        layout.setVgap(10);
//        layout.setHgap(10);
//
//        SerialPort[] ports = SerialPort.getCommPorts();
//        for(SerialPort port: ports)
//        {
//            layout.getChildren().add(new Label(port.getDescriptivePortName()));
////            port.getDescriptivePortName();
//        }
//
//        SerialPortPacketListener listener = new SerialPortPacketListener() {
//            @Override
//            public int getPacketSize() {
//                System.out.println(PACKET_SIZE_IN_BYTES);
//                return PACKET_SIZE_IN_BYTES;
//            }
//
//            @Override
//            public int getListeningEvents() {
//                return 0;
//            }
//
//            @Override
//            public void serialEvent(SerialPortEvent serialPortEvent) {
//                System.out.println("serialEvent");
//                byte[] newData = serialPortEvent.getReceivedData();
//                String str = new String(newData).split("\n", 2)[0].replaceAll("\\s+", "");
//
//                int byteSize = 0;
//                try {
//                    byteSize = str.getBytes("UTF-8").length;
//                } catch(UnsupportedEncodingException ex) {
//                    System.out.println("ex: " + ex.getMessage());
//                    Logger.getLogger(SerialPortPacketListener.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                System.out.println("Recieved data: " + str);
//                if(byteSize == PACKET_SIZE_IN_BYTES) {
//                    System.out.println("Recieved data: " + str);
//                }
//            }
//        };
//
//        SerialPort soyalPort;// = SerialPort.getCommPort("COM3");
//
//        for(SerialPort port : ports){
//            if(port.getDescriptivePortName().contains(devicePortName)){
//                soyalPort = port;
//                if(soyalPort.openPort())
//                    System.out.println("Connected to " + soyalPort.getDescriptivePortName());
//                else
//                    throw new Exception("Port: " + soyalPort.getDescriptivePortName() + " cannot open");
//
//                soyalPort.addDataListener(listener);
//                break;
//            }
//        }
//
//
//        Scene scene = new Scene(layout);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
////        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
////        primaryStage.setTitle("Hello World");
////        primaryStage.setScene(new Scene(root, 300, 275));
////        primaryStage.show();
//    }

    public static void main(String[] args) {
        launch(args);



//        try {
//            ar721h_controller ar721h = new ar721h_controller(devicePortName);
//            int timeout = 1000;
//            SerialPort comPort = ar721h.getPort();
//            byte [] read = null;
//
//            while(true) {
//                System.out.println("\n-------------WRITING----------------");
//                if(read != null && read.length == 31) {
//                    System.out.println("DELETE");
//                    ar721h.setCommand(Command.DELETE);
//                }
//                else {
//                    System.out.println("CHECK");
//                    ar721h.setCommand(Command.CHECK);
//                }
//
//                ar721h.writeToPort();
//                System.out.println("Write: " + ar721h.byteArrToStringBuilder(ar721h.getCommand(), true));
//                System.out.println("byte[] length: " + ar721h.getCommand().length);
//                System.out.println("\n-------------!WRITING----------------");
//
//                while(comPort.bytesAvailable() < 1)
//                    Thread.sleep(timeout);
//
//                System.out.println("\n-------------READ----------------");
//                ar721h.readFromPort();
//                read = ar721h.popReads();
//                System.out.println("Read: " + ar721h.byteArrToStringBuilder(read, true));
////                System.out.println("Read: " + ar721h.byteArrToStringBuilder(read, true));
//                System.out.println("byte [] length: " + read.length);
//                System.out.println("\n-------------!READ----------------");
//            }
//
//        } catch(Exception e) {
//            System.out.println("AR721H controller error");
//            System.out.println(e.getMessage());
//        }


    }

    public static void runController(ar721h_controller ar721h) {

    }
}



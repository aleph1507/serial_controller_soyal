package sample;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ar721h_controller {

    public String devicePortName;
    public static SerialPort[] ports = SerialPort.getCommPorts();
    public SerialPort port;
    Map<Main.Command, byte[]> commands = new HashMap<Main.Command, byte[]>();
    Map<Main.ReadEvents, Byte> readEvents = new HashMap<Main.ReadEvents, Byte>();
    Main.Command command;
    byte [] read = null;
    Stack<byte []> reads = new Stack<byte[]>();
//    Stack<CtrlEvent> events = new Stack<CtrlEvent>();
    ArrayList<Main.Command> writes = new ArrayList<Main.Command>();
    ArrayList<CtrlEvent> events = new ArrayList<CtrlEvent>();
    public static byte[] startOfRead = new byte[] {0x7E, 0x1D, 0x00};

//    commands(Main.Command.CHECK, new byte[] { 0x7E, 0x04, 0x01, 0x25, (byte) 0xDB, 0x01, 0x00});
//    public byte[] check = new byte[] { 0x7E, 0x04, 0x01, 0x25, (byte) 0xDB, 0x01, 0x00};
//    public byte[] deleteData = new byte[] { 0x7E, 0x07, 0x01, 0x37, (byte) 0x44, 0x45, 0x4c, (byte) 0x84, (byte) 0x91, 0x00};

    public ar721h_controller(String devicePortName) throws Exception{
        this.devicePortName = devicePortName;
        this.port = setupPort();
        this.commands.put(Main.Command.CHECK, new byte [] { 0x7E, 0x04, 0x01, 0x25, (byte) 0xDB, 0x01, 0x00} );
        this.commands.put(Main.Command.DELETE, new byte [] { 0x7E, 0x07, 0x01, 0x37, (byte) 0x44, 0x45, 0x4c, (byte) 0x84, (byte) 0x91, 0x00} );
        this.readEvents.put(Main.ReadEvents.NOEVENT, (byte)0x04);
        this.readEvents.put(Main.ReadEvents.INVALIDCARD, (byte)0x03);
        this.readEvents.put(Main.ReadEvents.VALIDCARD, (byte)0x04);
    }

    public SerialPort getPort() {
        return this.port;
    }

    public SerialPort setupPort() throws Exception {
        SerialPort soyalPort = null;// = SerialPort.getCommPort("COM3");

        for(SerialPort port : ports){
            if(port.getDescriptivePortName().contains(devicePortName)){
                soyalPort = port;
                if(soyalPort.openPort())
                    System.out.println("Connected to " + soyalPort.getDescriptivePortName());
                else
                    throw new Exception("Port: " + soyalPort.getDescriptivePortName() + " cannot open");
                break;
            }
        }

        return soyalPort;
    }

    public boolean disconnectPort(SerialPort soyalPort) {


        return soyalPort.closePort();
    }

//    public int writeToPort(byte [] data) {
//        return this.port.writeBytes(data, (long) data.length);
//    }

    public void setCommand(Main.Command c) {
        this.command = c;
    }

    public byte[] getCommand() {
        return this.commands.get(this.command);
    }

    public Main.Command getCommandKey() {
        return this.command;
    }

    public int writeToPort() {
        this.writes.add(this.command);
        return this.port.writeBytes(this.commands.get(this.command), (long)this.commands.get(this.command).length);
    }

    public byte[] readFromPort() {
        byte[] readBuffer = new byte[this.port.bytesAvailable()];
        this.port.readBytes(readBuffer, readBuffer.length);
        reads.push(readBuffer);

        CtrlEvent ctrlEvent = new CtrlEvent(byteArrToStringBuilder(readBuffer, true));
        this.events.add(ctrlEvent);
        System.out.println(ctrlEvent.stringifyEvent());
        System.out.println("Event: " + ctrlEvent.getEventType());
//
//        parseRes(readBuffer);
        return readBuffer;
    }

    public StringBuilder byteArrToStringBuilder(byte[] data, boolean space) {
        StringBuilder sb = new StringBuilder();
        for(byte r : data) {
            sb.append(String.format("%02X", r));
            if(space) sb.append(" ");
        }
        return sb;
    }

    public byte[] popReads() {
        return this.reads.pop();
    }

    public void writeCheck() {
//        while (true) {
            System.out.println("\n-------------WRITING----------------");
            if (read != null && read.length == 31) {
                System.out.println("DELETE");
                this.setCommand(Main.Command.DELETE);
            } else {
                System.out.println("CHECK");
                this.setCommand(Main.Command.CHECK);
            }

            this.writeToPort();
            System.out.println("Write: " + this.byteArrToStringBuilder(this.getCommand(), true));
            System.out.println("byte[] length: " + this.getCommand().length);
            System.out.println("\n-------------!WRITING----------------");
//        }
    }

    public sample.CtrlEvent readEvent() {
        this.read = this.readFromPort();

        sample.CtrlEvent ctrlEvent = new CtrlEvent(byteArrToStringBuilder(this.read, true));
        this.events.add(ctrlEvent);

        return ctrlEvent;
    }

//    try {
//        ar721h_controller ar721h = new ar721h_controller(devicePortName);
//        int timeout = 1000;
//        SerialPort comPort = ar721h.getPort();
//        byte [] read = null;
//
//        while(true) {
//            System.out.println("\n-------------WRITING----------------");
//            if(read != null && read.length == 31) {
//                System.out.println("DELETE");
//                ar721h.setCommand(Main.Command.DELETE);
//            }
//            else {
//                System.out.println("CHECK");
//                ar721h.setCommand(Main.Command.CHECK);
//            }
//
//            ar721h.writeToPort();
//            System.out.println("Write: " + ar721h.byteArrToStringBuilder(ar721h.getCommand(), true));
//            System.out.println("byte[] length: " + ar721h.getCommand().length);
//            System.out.println("\n-------------!WRITING----------------");

//            while(comPort.bytesAvailable() < 1)
//                Thread.sleep(timeout);


//            System.out.println("\n-------------READ----------------");
//            ar721h.readFromPort();
//            read = ar721h.popReads();
//            System.out.println("Read: " + ar721h.byteArrToStringBuilder(read, true));
////                System.out.println("Read: " + ar721h.byteArrToStringBuilder(read, true));
//            System.out.println("byte [] length: " + read.length);
//            System.out.println("\n-------------!READ----------------");
//        }
//
//    } catch(Exception e) {
//        System.out.println("AR721H controller error");
//        System.out.println(e.getMessage());
//    }


//    public void parseRes(byte [] res) {
//        CtrlEvent ctrlEvent = new CtrlEvent(byteArrToStringBuilder(res, true));
//        this.events.add(ctrlEvent);
//        System.out.println(ctrlEvent.stringifyEvent());
//        System.out.println("Event: " + ctrlEvent.getEventType());
//    }


}

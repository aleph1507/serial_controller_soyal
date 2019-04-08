package sample;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CtrlEvent {
    private Main.ReadEvents eventType;
    private LinkedHashMap<String, String> response = new LinkedHashMap<String, String>();
    public static boolean deleteConfirmation = false;

    String[] bytes;

    CtrlEvent(String msg) {
        this.eventType = Main.ReadEvents.NOEVENT;
        response.put("Summary", msg);
    }

    CtrlEvent(StringBuilder response) {
        System.out.println("CtrlEvent response.length: " + response.length());
        System.out.println("CtrlEvent response: " + response);
        switch(response.length()) {
            case 21:
                this.eventType = parse21(response); //NOEVENT
                break;
            case 93:
                this.eventType = parse93(response); // CARD
                break;
            case 24:
                this.eventType = parse24(response); //DELETEEVENT
                break;
            default:
                this.eventType = null;
                System.out.println("CtrlEvent: Event " + response + " cannot be parsed");
                break;
        }
    }

    private Main.ReadEvents parse24(StringBuilder event) {
        response.put("Delete", event.toString());
        return Main.ReadEvents.DELETEEVENT;
    }

    private void parseHead(StringBuilder event) {
        bytes = event.toString().split(" ");
        response.put("Head", bytes[0]);
        response.put("Length", bytes[1]);
        response.put("Node", bytes[2]);
    }

    private Main.ReadEvents parse93(StringBuilder event) {
        this.parseHead(event);
        response.put("Event", bytes[3]);
        response.put("ReaderID", bytes[4]);
        response.put("Seconds", bytes[5]);
        response.put("Minutes", bytes[6]);
        response.put("Hours", bytes[7]);
        response.put("DayOfWeek", bytes[8]);
        response.put("Date", bytes[9]);
        response.put("Month", bytes[10]);
        response.put("Year", bytes[11]);
        response.put("MessageSourceID", bytes[12]);
        response.put("AddrHi", bytes[13]);
        response.put("AddrLo", bytes[14]);
        response.put("OnDuty", bytes[15]);
        response.put("ForcedOpenAlarm", bytes[16]);
        response.put("BitSelection", bytes[17]);
        response.put("WG", bytes[18]);
        response.put("SiteHi", bytes[19]);
        response.put("SiteLo", bytes[20]);
        response.put("DoorNo", bytes[21]);
        response.put("401RO16Parameter", bytes[22]);
        response.put("CardHi", bytes[23]);
        response.put("CardLo", bytes[24]);
        response.put("DeductedAmount1", bytes[25]);
        response.put("DeductedAmount2", bytes[26]);
        response.put("Balance1", bytes[27]);
        response.put("Balance2", bytes[28]);
        response.put("XOR", bytes[29]);
        response.put("SUM", bytes[30]);
        response.put("Site:Card", String.valueOf(Integer.parseInt(response.get("SiteHi") + response.get("SiteLo") + response.get("CardHi") + response.get("CardLo"), 16)));
//        response.put("Summary", "Card " + (response.get("SiteHi") + response.get("SiteLo")) + " decimal: " +
//                Integer.parseInt(response.get("SiteHi") + response.get("SiteLo") + response.get("CardHi") + response.get("CardLo"), 16) + " swiped");
        if( response.get("Event").equals("03")) {
            response.put("Summary", "Invalid Card " + response.get("Site:Card") + " swiped");
            return Main.ReadEvents.INVALIDCARD;
        } else {
            response.put("Summary", "Card " + response.get("Site:Card") + " swiped");
            return Main.ReadEvents.VALIDCARD;
        }
    }

    private Main.ReadEvents parse21(StringBuilder event) {
        this.parseHead(event);
        response.put("Function", bytes[3]);
        response.put("ReaderID", bytes[4]);
        response.put("XOR", bytes[5]);
        response.put("SUM", bytes[6]);
        if(response.get("Function").equals("04"))
            response.put("Summary", "(ACK) Command Acknowledged, no controller events at this time");
        else
            response.put("Summary", "(NACK) Command Not Acknowledged, no controller events at this time");
        response.put("Error", "No error");
        return Main.ReadEvents.NOEVENT;
    }

    public Main.ReadEvents getEventType() {
        return this.eventType;
    }

    public HashMap<String, String> getEvent() {
        return this.response;
    }

    String stringifyEvent() {
        StringBuilder s = new StringBuilder();
        Iterator it = this.response.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            s.append(pair.getKey()).append(" :  ").append(pair.getValue()).append("\n");
            it.remove(); // avoids a ConcurrentModificationException
        }
        return s.toString();
    }

    public String getSummary() {
        return this.response.get("Summary");
    }


}

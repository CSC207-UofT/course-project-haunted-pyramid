package gateways;

import entities.Event;
import usecases.events.EventManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

public class ICalendar {
    public static final String iCalSpacer = "\r\n";
    private final EventManager eventManager;
    private final String beginCal = "BEGIN:VCALENDAR" + iCalSpacer;
    private final String version = "VERSION:2.0" + iCalSpacer;
    private final String prodID = "PRODID:-//Haunted Pyramid//CSC207 v1.0//EN" + iCalSpacer;
    private final String beginEvent = "BEGIN:VEVENT" + iCalSpacer;
    private final String endEvent = "END:VEVENT" + iCalSpacer;
    private final String endCal = "END:VCALENDAR" + iCalSpacer;

    public ICalendar(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    private void create(String name) {
        try {
            File newFile = new File(name + ".ics");
            if (newFile.createNewFile()) {
                System.out.println("Creating " + name + ".ics...");
            }
            else {
                System.out.println("File already exists...");
                System.out.println("Overwriting the file...");
            }
            FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(beginCal);
            bw.write(version);
            bw.write(prodID);
            for (UUID key : eventManager.getEventMap().keySet()) {
                addEvent(bw,  key);
            }
            bw.write(endCal);
            bw.close();
            System.out.println("Task Completed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addEvent(BufferedWriter bw, UUID eventID) {
        LocalDate startDate = eventManager.getStartDate(eventID);
        LocalTime startTime = eventManager.getStartTime(eventID);
        LocalDate endDate = eventManager.getEndDate(eventID);
        LocalTime endTime = eventManager.getEndTime(eventID);
        StringBuilder startDateTime = getTzTimeStamp(startDate, startTime);
        StringBuilder endDateTime = getTzTimeStamp(endDate, endTime);
        String name = eventManager.getName(eventManager.get(eventID));
        String description = eventManager.getDescription(eventID);
        try {
            bw.write(beginEvent);
            bw.write("UID:" + eventID + iCalSpacer);
            bw.write("DTSTAMP:" + startDateTime + iCalSpacer);
            bw.write("DTSTART:" + startDateTime + iCalSpacer);
            bw.write("DTEND:" + endDateTime + iCalSpacer);
            bw.write("SUMMARY:" + name + iCalSpacer);
            bw.write("DESCRIPTION:" + description + iCalSpacer);
            bw.write(endEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder getTzTimeStamp(LocalDate startDate2, LocalTime startTime2) {
        String startDate = String.valueOf(startDate2);
        String startTime = String.valueOf(startTime2);
        StringBuilder startDateTime = new StringBuilder();
        for (String components : startDate.split("-")) {
            startDateTime.append(components);
        }
        startDateTime.append("T");
        for (String components : startTime.split(":")) {
            startDateTime.append(components);
        }
        startDateTime.append("00").append("Z");
        return startDateTime;
    }

    public static void main(String[] args) {
        EventManager em = new EventManager(new ArrayList<>());
        Event event1 = new Event(UUID.randomUUID(), "TEST", LocalDateTime.of(2021,11,30,15, 30),
                LocalDateTime.of(2021, 11, 30, 18, 30));
        Event event2 = new Event(UUID.randomUUID(), "TEST2", LocalDateTime.of(2021, 11, 30, 12,0),
                LocalDateTime.of(2021, 11,30, 13, 0));
        em.addEvent(event1);
        em.addEvent(event2);
        ICalendar iCal = new ICalendar(em);
        iCal.create("TEST");
    }
}

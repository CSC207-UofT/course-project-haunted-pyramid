package gateways;

import usecases.EventCalendarCollaborator;
import usecases.calendar.*;
import usecases.events.EventManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Export to iCal file format (only as VEvent)
 * @author Seo Won Yi
 *
 */

public class ICalendar {
    public static final String iCalSpacer = "\r\n";
    private final EventManager eventManager;
    private final CalendarManager calendarManager;
    private final String endCal = "END:VCALENDAR" + iCalSpacer;

    /**
     * Initialize ICalendar with the given EventManager
     * @param eventManager object where all the information of events are stored
     */
    public ICalendar(EventManager eventManager) {
        this.eventManager = eventManager;
        this.calendarManager = new CalendarManager();
        EventCalendarCollaborator collaborator = new EventCalendarCollaborator(this.eventManager, this.calendarManager);
        collaborator.addAllEvents();
    }

    /**
     * Depending on different choices, create appropriate ics file
     * @param name name of the file
     * @param option type of calendar to create
     * @param yearMonthDate list of year, month, date to consider
     */
    public void create(String name, String option, List<Integer> yearMonthDate) {
        try {
            File newFile = createFile(name);
            BufferedWriter bw = setUp(newFile);
            switch(option) {
                case "MONTHLY":
                    exportMonthlyCalendar(yearMonthDate, bw, newFile);
                    break;
                case "WEEKLY":
                    exportWeeklyCalendar(yearMonthDate, bw, newFile);
                    break;
                case "DAILY":
                    exportDailyCalendar(yearMonthDate, bw, newFile);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * create ics file for entire events
     * @param name name of the file
     */
    public void create(String name) {
        try {
            File newFile = createFile(name);
            BufferedWriter bw = setUp(newFile);
            exportEntireCalendar(bw, newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * helper method that creates a new file with the given name
     * @param name name of the file
     * @return File object of newly created file
     * @throws IOException when file name is not appropriate
     */
    private File createFile(String name) throws IOException {
        File newFile = new File(name + ".ics");
        if (newFile.createNewFile()) {
            System.out.println("Creating " + name + ".ics...");
        } else {
            System.out.println("File already exists...");
            System.out.println("Overwriting the file...");
        }
        return newFile;
    }

    /**
     * helper method that sets up BufferedWriter and write initial information
     * @param newFile file to be written on
     * @return BufferedWriter object with initial setup
     * @throws IOException when file name is not appropriate
     */
    private BufferedWriter setUp(File newFile) throws IOException {
        FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        String beginCal = "BEGIN:VCALENDAR" + iCalSpacer;
        bw.write(beginCal);
        String version = "VERSION:2.0" + iCalSpacer;
        bw.write(version);
        String prodID = "PRODID:-//Haunted Pyramid//CSC207 v1.0//EN" + iCalSpacer;
        bw.write(prodID);
        return bw;
    }

    /**
     * Write entire event information to the file
     * @param bw BufferedWriter Object that helps writing on the file
     * @param file file to be written on
     * @throws IOException when file name is not appropriate
     */
    private void exportEntireCalendar(BufferedWriter bw, File file) throws IOException {
        if (eventManager.getEventMap().isEmpty()) {
            System.out.println("There is no event to export");
            bw.close();
            file.deleteOnExit();
            return;
        }
        for (UUID key : eventManager.getEventMap().keySet()) {
            addEventToFile(bw,  key);
        }
        bw.write(endCal);
        bw.close();
        System.out.println("Task Completed");
    }

    /**
     * Write event information from the given month to the file
     * @param yearMonthDate list of year, month, date to consider
     * @param bw BufferedWriter Object that helps writing on the file
     * @param file file to be written on
     * @throws IOException when file name is not appropriate
     */
    private void exportMonthlyCalendar(List<Integer> yearMonthDate, BufferedWriter bw, File file) throws IOException {
        MonthlyCalendarByType monthlyCalendar = new MonthlyCalendarByType();
        Map<Integer, List<UUID>> calendarMap = monthlyCalendar.getCalendar(calendarManager, yearMonthDate.get(0),
                yearMonthDate.get(1));
        writeEventAfterCheck(bw, file, calendarMap);
    }

    /**
     * Write event information from the given week to the file (starting from the date)
     * @param yearMonthDate list of year, month, date to consider
     * @param bw BufferedWriter Object that helps writing on the file
     * @param file file to be written on
     * @throws IOException when file name is not appropriate
     */
    private void exportWeeklyCalendar(List<Integer> yearMonthDate, BufferedWriter bw, File file) throws IOException {
        WeeklyCalendarByType weeklyCalendar = new WeeklyCalendarByType();
        Map<Integer, List<UUID>> calendarMap = weeklyCalendar.getCalendar(calendarManager, yearMonthDate.get(0),
                yearMonthDate.get(1), yearMonthDate.get(2));
        writeEventAfterCheck(bw, file, calendarMap);
    }

    /**
     * Write event information from the given day to the file
     * @param yearMonthDate list of year, month, date to consider
     * @param bw BufferedWriter Object that helps writing on the file
     * @param file file to be written on
     * @throws IOException when file name is not appropriate
     */
    private void exportDailyCalendar(List<Integer> yearMonthDate, BufferedWriter bw, File file) throws IOException {
        DailyCalendarByType dailyCalendar = new DailyCalendarByType();
        Map<Integer, List<UUID>> calendarMap = dailyCalendar.getCalendar(calendarManager, yearMonthDate.get(0),
                yearMonthDate.get(1), yearMonthDate.get(2));
        writeEventAfterCheck(bw, file, calendarMap);
    }

    /**
     * check if the calendarMap is empty, if empty perform appropriate action
     * otherwise, write the events in the map to the file using BufferedWriter object
     * @param bw BufferedWriter Object that helps writing on the file
     * @param file file to be written on
     * @param calendarMap map of calendar to consider from
     * @throws IOException when file name is not appropriate
     */
    private void writeEventAfterCheck(BufferedWriter bw, File file, Map<Integer, List<UUID>> calendarMap)
            throws IOException {
        if (isMapEmpty(bw, file, calendarMap)) {
            return;
        }
        List<UUID> eventIDList = new ArrayList<>();
        for (int date : calendarMap.keySet()) {
            for (UUID eventID : calendarMap.get(date)) {
                if (!eventIDList.contains(eventID)) {
                    eventIDList.add(eventID);
                }
            }
        }
        for (UUID eventID : eventIDList) {
            addEventToFile(bw, eventID);
        }
        bw.write(endCal);
        bw.close();
        System.out.println("Task Completed");
    }

    /**
     * Check if the given map does not have any values
     * @param bw BufferedWriter Object that helps writing on the file
     * @param file file to be written on
     * @param calendarMap calendar map to consider from
     * @return true if the map is empty. False otherwise
     * @throws IOException when file name is not appropriate
     */
    private boolean isMapEmpty(BufferedWriter bw, File file, Map<Integer, List<UUID>> calendarMap) throws IOException {
        int count = 0;
        for (List<UUID> lst : calendarMap.values()) {
            if (lst.size() != 0) {
                count += 1;
            }
        }
        if (count == 0) {
            System.out.println("There is no event to export in this calendar");
            bw.close();
            file.deleteOnExit();
            return true;
        }
        return false;
    }

    /**
     * Add the events to the file in appropriate format for ics file
     * @param bw BufferedWriter Object that helps writing on the file
     * @param eventID ID of an event
     */
    private void addEventToFile(BufferedWriter bw, UUID eventID) {
        LocalDate startDate = eventManager.getStartDate(eventID);
        LocalTime startTime = eventManager.getStartTime(eventID);
        LocalDate endDate = eventManager.getEndDate(eventID);
        LocalTime endTime = eventManager.getEndTime(eventID);
        StringBuilder startDateTime = getTzTimeStamp(startDate, startTime);
        StringBuilder endDateTime = getTzTimeStamp(endDate, endTime);
        String name = eventManager.getName(eventManager.get(eventID));
        String description = eventManager.getDescription(eventID);
        try {
            String beginEvent = "BEGIN:VEVENT" + iCalSpacer;
            bw.write(beginEvent);
            bw.write("UID:" + eventID + iCalSpacer);
            bw.write("DTSTAMP:" + startDateTime + iCalSpacer);
            bw.write("DTSTART:" + startDateTime + iCalSpacer);
            bw.write("DTEND:" + endDateTime + iCalSpacer);
            bw.write("SUMMARY:" + name + iCalSpacer);
            bw.write("DESCRIPTION:" + description + iCalSpacer);
            String endEvent = "END:VEVENT" + iCalSpacer;
            bw.write(endEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert date and time information to appropriate format for ics file
     * @param dateInfo date information
     * @param timeInfo time inforamtion
     * @return combined date and time information in appropriate format for ics file writing
     */
    private StringBuilder getTzTimeStamp(LocalDate dateInfo, LocalTime timeInfo) {
        String date = String.valueOf(dateInfo);
        String time = String.valueOf(timeInfo);
        StringBuilder dateTime = new StringBuilder();
        for (String components : date.split("-")) {
            dateTime.append(components);
        }
        dateTime.append("T");
        for (String components : time.split(":")) {
            dateTime.append(components);
        }
        dateTime.append("00").append("J");
        return dateTime;
    }
}

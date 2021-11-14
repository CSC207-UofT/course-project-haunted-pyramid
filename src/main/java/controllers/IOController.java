package controllers;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import java.util.List;
import java.util.Scanner;

public class IOController {
        /**
         *
         */
        private Scanner scanner = new Scanner(System.in);
        //TODO - add exceptions and input error catching to this class

        /**
         *
         * @return the title of an event as input by user
         */
        public String getName(){
            System.out.println("Enter name for event: ");
            return scanner.nextLine();
        }

        /**
         *
         * @return a course title as input by User
         */
        public String getCourse(){
            System.out.println("Enter course name");
            return scanner.nextLine();
        }

        /**
         *
         * @return date in form of list of integers
         */
        public Integer[] getDate(String request){
            System.out.println(request + " (YYYY-MM-DD)");
            String date = scanner.nextLine();
            String[] dateParts = date.split("-");
            return new Integer[]{Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]),
                    Integer.parseInt(dateParts[2])};
        }

        /**
         *
         * @return time in [HH, MM]
         */
        public List<Integer> getTime(String request){
            System.out.println(request + " (HH:MM)");
            String time = scanner.nextLine();
            if (time.equalsIgnoreCase("Return")){
                return new ArrayList<>();
            }
            while (!validTimeInput().contains(time)){
                System.out.println("Please enter the time in the right format (HH:MM)");
                time = scanner.nextLine();
                if (time.equalsIgnoreCase("Return")){
                    return new ArrayList<>();
                }
            }
            String[] timeParts = time.split(":");
            return List.of(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
        }

        public LocalDate getDate1(String request){
            System.out.println(request);
            System.out.println("enter date in the form YYYY-MM-DD");
            String date = scanner.nextLine();
            try{
                return this.stringToDate(date);
            }catch (NumberFormatException numberFormatException){
                System.out.println("please check format and try again");
                return this.getDate1(request);
            }catch (DateTimeException dateTimeException){
                System.out.println(dateTimeException.getMessage());
                return this.getDate1(request);
            }
        }
        public LocalTime getTime1(String request){
            System.out.println(request);
            System.out.println("enter time in the form HH:MM");
            String time = scanner.nextLine();
            try{
                return this.stringToTime(time);
            }catch (NumberFormatException numberFormatException){
                System.out.println("please check format and try again");
                return this.getTime1(request);
            }catch (DateTimeException dateTimeException){
                System.out.println(dateTimeException.getMessage());
                return this.getTime1(request);
            }
        }

        public LocalDateTime getDateTime(String requestTime, String requestDate){
            LocalDate date = this.getDate1(requestDate);
            LocalTime time = this.getTime1(requestTime);
            return LocalDateTime.of(date, time);
        }

        public String getAnswer(String request) {
            System.out.println(request);
            return scanner.nextLine();
        }

        private List<String> validTimeInput(){
            List<String> hours = new ArrayList<>();
            List<String> minutes = new ArrayList<>();
            for (int i = 0; i <= 9; i++){
                hours.add(String.valueOf(i));
                hours.add("0" + i);
                minutes.add(String.valueOf(i));
                minutes.add("0" + i);
            }
            for (int j = 10; j <= 24; j++){
                hours.add(String.valueOf(j));
            }
            for (int k = 10; k <= 59; k++){
                minutes.add(String.valueOf(k));
            }
            List<String> result = new ArrayList<>();
            for (String hour : hours){
                for (String minute : minutes){
                    result.add(hour + ":" + minute);
                }
            }
            return result;
        }

    private LocalTime stringToTime(String time) throws NumberFormatException, DateTimeException{
            String[] times = time.split(":");
        if (times.length != 2){
            throw new NumberFormatException();
        }
        try {
            return LocalTime.of(Integer.parseInt(times[0]), Integer.parseInt(times[1]));
        }catch(NumberFormatException numberFormatException){
            throw new NumberFormatException();
        }catch(DateTimeException dateTimeException){
            throw new DateTimeException(dateTimeException.getMessage());
        }
    }

    private LocalDate stringToDate(String date) throws NumberFormatException, DateTimeException{
            String[] dates = date.split("-");
        if (dates.length != 3){
            throw new NumberFormatException();
        }
        try {
            return LocalDate.of(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
        }catch(NumberFormatException numberFormatException){
            throw new NumberFormatException();
        }catch(DateTimeException dateTimeException){
            throw new DateTimeException(dateTimeException.getMessage());
        }
    }
    }


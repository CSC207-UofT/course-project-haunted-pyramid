package controllers;

import java.util.List;
import java.util.Scanner;

import java.util.List;
import java.util.Scanner;

public class IOController {
        /**
         *
         */
        private static Scanner scanner = new Scanner(System.in);
        //TODO - add exceptions and input error catching to this class

        /**
         *
         * @return the title of an event as input by user
         */
        public static String getName(){
            System.out.println("enter name for event: ");
            return scanner.nextLine();
        }

        /**
         *
         * @return a course title as input by User
         */
        public static String getCourse(){
            System.out.println("enter course name");
            return scanner.nextLine();
        }

        /**
         *
         * @return date in form of list of integers
         */
        public static Integer[] getDate(String request){
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
        public static List<Integer> getTime(String request){
            System.out.println(request + " (HH-MM)");
            String time = scanner.nextLine();
            String[] timeParts = time.split("-");
            return List.of(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
        }

        public static String getAnswer(String request) {
            System.out.println(request);
            return scanner.nextLine();
        }
    }


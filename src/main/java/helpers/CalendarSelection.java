package helpers;

public class CalendarSelection {
    private int year;
    private int month;
    private int numberOfDays;

    /**
     * Used in CalendarController for deciding the year and month of the calendar to be used
     * based on dateInput (which decides the year and date of the calendar) assign year, month, number of days
     * @param dateInfo DateInfo object used for giving out proper output for year, month and number of days
     * @param dateInput the choice that was made by the user to determine which year and month to use.
     */
    public CalendarSelection(DateInfo dateInfo, String dateInput){
        year = dateInfo.getDateInfo(0).get(0);
        month = dateInfo.getDateInfo(0).get(1);
        numberOfDays = dateInfo.getDateInfo(0).get(2);
        switch (dateInput) {
            case "1":
                year = dateInfo.getDateInfo(3).get(0);
                month = dateInfo.getDateInfo(3).get(1);
                numberOfDays = dateInfo.getDateInfo(3).get(2);
                break;
            case "2":
                year = dateInfo.getDateInfo(2).get(0);
                month = dateInfo.getDateInfo(2).get(1);
                numberOfDays = dateInfo.getDateInfo(2).get(2);
                break;
            case "3":
                year = dateInfo.getDateInfo(1).get(0);
                month = dateInfo.getDateInfo(1).get(1);
                numberOfDays = dateInfo.getDateInfo(1).get(2);
                break;
            case "4":
                break;
            case "5":
                year = dateInfo.getDateInfo(-1).get(0);
                month = dateInfo.getDateInfo(-1).get(1);
                numberOfDays = dateInfo.getDateInfo(-1).get(2);
                break;
            case "6":
                year = dateInfo.getDateInfo(-2).get(0);
                month = dateInfo.getDateInfo(-2).get(1);
                numberOfDays = dateInfo.getDateInfo(-2).get(2);
                break;
            case "7":
                year = dateInfo.getDateInfo(-3).get(0);
                month = dateInfo.getDateInfo(-3).get(1);
                numberOfDays = dateInfo.getDateInfo(-3).get(2);
                break;
        }
    }
    public int getYear(){
        return this.year;
    }

    public int getMonth(){
        return this.month;
    }

    public int getNumberOfDays(){
        return this.numberOfDays;
    }
}

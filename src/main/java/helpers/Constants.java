package helpers;

import java.time.LocalTime;

public class Constants {
    // CALENDAR_SIZE CAN ONLY INCREASE BY MULTIPLES OF 14
    // CAL_ROW_SPACER AND WEEKLY_CAL_NAME_LIMIT CAN BE INCREASED IFF CALENDAR_SIZE INCREASES
    // WHEN CALENDAR_SIZE INCREASES, CAL_ROW_SPACER INCREASES BY THE AMOUNT DIVIDED BY 7
    // WEEKLY_CAL_NAME_LIMIT CAN ONLY BE AROUND ROUGHLY CAL_ROW_SPACER - 10
    public static final int CALENDAR_SIZE = 224;
    public static final int DAILY_CAL_SIZE = 60;
    public static final int CAL_ROW_SPACER = 24;
    public static final int WEEKLY_CAL_NAME_LIMIT = 16;
    public static final int MENU_DIVIDER = 35;
    public static final int TIMELINE_SPACER = -8; // Space occupied by timeline (" xx:xx |")
    public static final int MAXIMUM_SESSION_LENGTH = 10;
    public static final int MAXIMUM_WORK_SESSION_HOUR = 50;
    public static final LocalTime RETURN_NOTIFIER = LocalTime.of(4, 4, 4);
}

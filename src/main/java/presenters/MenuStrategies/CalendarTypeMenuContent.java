package presenters.MenuStrategies;

import helpers.Constants;

import java.util.ArrayList;
import java.util.List;

public class CalendarTypeMenuContent implements MenuContent {

    @Override
    public List<String> getContent() {
        return new ArrayList<>(){{
            add("1. View Monthly Calendar");
            add("2. View Weekly Calendar");
            add("3. View Daily Calendar");
        }};
    }

}

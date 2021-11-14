package presenters.MenuStrategies;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Seo Won Yi
 */
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

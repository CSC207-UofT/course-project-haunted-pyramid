package presenters.MenuStrategies;


import java.util.ArrayList;
import java.util.List;

/**
 * Menu Contents to be used for CalendarController
 * @author Seo Won Yi
 */
public class CalNextActionMenuContent implements MenuContent {

    @Override
    public List<String> getContent(){
        return new ArrayList<>(){{
            add("1. View different Type/Date of Calendar");
            add("2. View/Modify Event through Calendar");
            add("3. Add a new Event");
            add("4. Create Repetition of the Existing Events");
            add("5. Return to the Main Menu");
        }};
    }
}

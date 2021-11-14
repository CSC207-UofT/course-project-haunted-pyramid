package presenters.MenuStrategies;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Seo Won Yi
 */
public class CalNextActionMenuContent implements MenuContent {

    @Override
    public List<String> getContent(){
        return new ArrayList<>(){{
            add("1. View different Type/Date of Calendar");
            add("2. View/Modify Event through Calendar");
            add("3. Add a new Event");
            add("4. Return to the Main Menu");
        }};
    }
}

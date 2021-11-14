package presenters.MenuStrategies;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Seo Won Yi
 */
public class WorkSessionMenu implements MenuContent{
    @Override
    public List<String> getContent(){
        return new ArrayList<>(){{
            add("1. Mark the Past Work Session Complete/Incomplete");
            add("2. Create / Modify Length of Each Session");
            add("3. Create / Modify Total Hours of Work Session");
            add("4. Return to the previous menu");
        }};
    }
}

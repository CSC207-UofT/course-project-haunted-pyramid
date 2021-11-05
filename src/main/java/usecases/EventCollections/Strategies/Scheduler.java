package usecases.EventCollections.Strategies;

public interface Scheduler {
    Integer[] getInstanceSchedule(Integer[] initial, Integer[][] except, Integer[][] also, Integer count);
}

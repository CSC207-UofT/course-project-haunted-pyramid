package usecases.events.worksessions.strategies.TimeOrderer;

import usecases.events.EventManager;
import usecases.events.worksessions.strategies.TimeGetters.TimeGetter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EveningPerson implements TimeOrderer {
    @Override
    public void order(UUID deadline, EventManager eventManager, Long length, List<LocalDate> idealDates, List<LocalDateTime> times, TimeGetter timeGetter) {

    }
}

package interfaces;

import java.util.*;

/**
 *
 */
public interface Fluid {
    float getFluidHours();
    float getSessionsLength();
    float setSessionLength(float hours);
    float setFluidHours(float hours);
    ArrayList<AutoSchedule> getFluidSessions();
}

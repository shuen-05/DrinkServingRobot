import java.time.LocalTime;
import java.util.*;

public class RobotScheduler {
    private Map<String, List<LocalTime[]>> robotSchedules;
    
    public RobotScheduler() {
        robotSchedules = new HashMap<>();
    }
    
    public void addRobotShift(String robotId, LocalTime start, LocalTime end) {
        if (!robotSchedules.containsKey(robotId)) {
            robotSchedules.put(robotId, new ArrayList<>());
        }
        robotSchedules.get(robotId).add(new LocalTime[]{start, end});
        SystemLogger.log("Scheduled robot " + robotId + " from " + start + " to " + end);
    }
    
    public boolean isRobotAvailable(String robotId, LocalTime checkTime) {
        if (!robotSchedules.containsKey(robotId)) return true;
        
        for (LocalTime[] shift : robotSchedules.get(robotId)) {
            if (!checkTime.isBefore(shift[0]) && !checkTime.isAfter(shift[1])) {
                return false;
            }
        }
        return true;
    }
    
    public void displayRobotSchedule(String robotId) {
        if (!robotSchedules.containsKey(robotId) || robotSchedules.get(robotId).isEmpty()) {
            System.out.println("No schedule found for robot " + robotId);
            return;
        }
        
        System.out.println("\nSchedule for Robot " + robotId + ":");
        System.out.println("--------------------------------");
        int shiftNumber = 1;
        for (LocalTime[] shift : robotSchedules.get(robotId)) {
            System.out.printf("Shift %d: %s - %s%n", 
                shiftNumber++, 
                shift[0].format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
                shift[1].format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        }
        System.out.println("--------------------------------");
    }
} 
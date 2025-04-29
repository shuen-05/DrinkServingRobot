import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DynamicDrinkServing {
    private RestrictedSpots currentSpot;
    private Scanner scanner;
    private Robot robot;
    
    public DynamicDrinkServing(RestrictedSpots spot, Robot robot) {
        this.currentSpot = spot;
        this.scanner = new Scanner(System.in);
        this.robot = robot;
    }
    
    public void performDistanceCheck() {
        SystemLogger.log("Dynamic distancing check started for robot " + robot.getRobotId() + " in " + currentSpot.getSpotName());
        System.out.println("\n=== Dynamic Distancing Check ===");
        System.out.println("Please enter distances from surrounding people (in meters):");
        
        double left = InputValidator.getValidDouble("Distance from Left: ", 0);
        double right = InputValidator.getValidDouble("Distance from Right: ", 0);
        double front = InputValidator.getValidDouble("Distance from Front: ", 0);
        double back = InputValidator.getValidDouble("Distance from Back: ", 0);
        
        boolean safe = true;
        StringBuilder directionsToMove = new StringBuilder();
        
        if (left < 1.0) {
            safe = false;
            directionsToMove.append("Left (").append(String.format("%.2f", 1.0 - left)).append("m needed), ");
        }
        if (right < 1.0) {
            safe = false;
            directionsToMove.append("Right (").append(String.format("%.2f", 1.0 - right)).append("m needed), ");
        }
        if (front < 1.0) {
            safe = false;
            directionsToMove.append("Front (").append(String.format("%.2f", 1.0 - front)).append("m needed), ");
        }
        if (back < 1.0) {
            safe = false;
            directionsToMove.append("Back (").append(String.format("%.2f", 1.0 - back)).append("m needed), ");
        }
        
        if (safe) {
            System.out.println("\nYou are safe in dynamic distancing!");
            SystemLogger.log("Robot " + robot.getRobotId() + " passed distancing check in " + currentSpot.getSpotName());
        } else {
            String directions = directionsToMove.substring(0, directionsToMove.length() - 2);
            System.out.println("\nWarning: Move away from " + directions);
            System.out.println("Alert: Robot is in close contact in this spot.");
            
            // Display contact information
            System.out.println("\n=== Contact Information ===");
            System.out.println("Robot's ID: " + robot.getRobotId());
            System.out.println("Robot's Full Name: " + robot.getRobotName());
            System.out.println("Robot Type: " + robot.getRobotType());
            System.out.println("Selected Spot ID: " + currentSpot.getSpotID());
            System.out.println("Spot Name: " + currentSpot.getSpotName());
            System.out.println("Date: " + LocalDate.now());
            System.out.println("Time: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            System.out.println("Contact Status: Casual Contact");
            
            // Log the violation
            SystemLogger.log(String.format("Distancing violation for robot %s (%s) in %s: %s", 
                robot.getRobotId(), robot.getRobotName(), currentSpot.getSpotName(), directions));
        }
    }
} 
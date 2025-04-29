import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StaticDrinkServing {
    private RestrictedSpots[] spots;
    private Scanner scanner;
    private RobotScheduler scheduler;
    private Map<String, DrinkServingRobot> robots;
    
    public StaticDrinkServing() {
        // Initialize restaurant spots
        spots = new RestrictedSpots[5];
        spots[0] = new RestrictedSpots("DF", "Dining Foyer", 25.0, 10);
        spots[1] = new RestrictedSpots("MDH", "Main Dining Hall", 75.0, 20);
        spots[2] = new RestrictedSpots("DR1", "Dining Room One", 20.0, 8);
        spots[3] = new RestrictedSpots("DR2", "Dining Room Two", 20.0, 8);
        spots[4] = new RestrictedSpots("FDR", "Family Dining Room", 30.0, 12);
        
        scanner = new Scanner(System.in);
        scheduler = new RobotScheduler();
        robots = new HashMap<>();
        
        // Initialize robots
        initializeRobots();
        
        SystemLogger.log("System initialized with spots and robots");
    }
    
    private void initializeRobots() {
        // Create and store robots
        DrinkServingRobot robot1 = new DrinkServingRobot("20612945", "Tan Shuen Xian", 2.5);
        DrinkServingRobot robot2 = new DrinkServingRobot("20611251", "Adrian Lee Joon Yin", 3.0);
        DrinkServingRobot robot3 = new DrinkServingRobot("20722250", "Adrian Lee Zhen Hom", 2.8);
        
        robots.put(robot1.getRobotId(), robot1);
        robots.put(robot2.getRobotId(), robot2);
        robots.put(robot3.getRobotId(), robot3);
        
        SystemLogger.log("Initialized three robots");
    }
    
    public void startSystem() {
        SystemLogger.log("System started");
        System.out.println("=== Drink Serving Robotic System ===");
        System.out.println("Welcome to the restaurant management system");
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = InputValidator.getValidInt("Enter your choice: ", 1, 3);
            
            switch (choice) {
                case 1:
                    checkSpotCapacity();
                    break;
                case 2:
                    manageRobotSchedule();
                    break;
                case 3:
                    running = false;
                    System.out.println("Exiting system...");
                    SystemLogger.log("System shutdown");
                    break;
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Check spot capacity and enter");
        System.out.println("2. Manage robot schedule");
        System.out.println("3. Exit system");
    }
    
    private void displayRobotInfo() {
        System.out.println("\nAvailable Robots:");
        for (DrinkServingRobot robot : robots.values()) {
            System.out.printf("%s (ID: %s) - Type: %s, Average Serve Time: %.1f minutes%n",
                robot.getRobotName(), robot.getRobotId(), robot.getRobotType(),
                robot.getAverageServeTime());
        }
    }
    
    private void checkSpotCapacity() {
        System.out.println("\nAvailable Dining Areas:");
        for (int i = 0; i < spots.length; i++) {
            System.out.printf("%d. %s (ID: %s) - Capacity: %d%n", 
                i+1, spots[i].getSpotName(), spots[i].getSpotID(),
                spots[i].getMaxCapacity());
        }
        
        int areaChoice = InputValidator.getValidInt("Select a dining area (1-5): ", 1, 5) - 1;
        RestrictedSpots selectedSpot = spots[areaChoice];
        int currentCapacity = selectedSpot.getMaxCapacity();
        
        // Display only robots with active shifts and not busy
        System.out.println("\nAvailable Robots (with active shifts and not busy):");
        boolean hasAvailableRobots = false;
        LocalTime currentTime = LocalTime.now();
        
        for (DrinkServingRobot robot : robots.values()) {
            if (!scheduler.isRobotAvailable(robot.getRobotId(), currentTime) && !robot.isBusy()) {
                System.out.printf("%s (ID: %s) - Type: %s, Average Serve Time: %.1f minutes%n",
                    robot.getRobotName(), robot.getRobotId(), robot.getRobotType(),
                    robot.getAverageServeTime());
                hasAvailableRobots = true;
            } else if (robot.isBusy()) {
                System.out.printf("%s (ID: %s) - Currently busy until %s%n",
                    robot.getRobotName(), robot.getRobotId(),
                    robot.getBusyUntil().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        }
        
        if (!hasAvailableRobots) {
            System.out.println("No robots are currently available to work.");
            System.out.println("Please wait for a robot to become available or schedule a shift for another robot.");
            return;
        }
        
        String robotId = InputValidator.getValidRobotId("Enter Robot ID: ");
        DrinkServingRobot selectedRobot = robots.get(robotId);
        
        if (selectedRobot == null) {
            System.out.println("Error: Invalid robot ID");
            return;
        }
        
        // Verify the selected robot has an active shift
        if (scheduler.isRobotAvailable(robotId, currentTime)) {
            System.out.println("Error: This robot is not scheduled to work at this time (" + currentTime + ")");
            System.out.println("Please schedule a shift for this robot first.");
            return;
        }
        
        // Check if the robot is busy
        if (selectedRobot.isBusy()) {
            System.out.println("Error: This robot is currently busy serving drinks.");
            System.out.println("Robot will be available at: " + 
                selectedRobot.getBusyUntil().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
            return;
        }
        
        int currentPeople = (int) (Math.random() * (currentCapacity + 1));
        SystemLogger.log(String.format("Capacity check for %s: %d/%d people", 
            selectedSpot.getSpotName(), currentPeople, currentCapacity));
        
        // Use the robot's capacity check method
        String capacityResult = selectedRobot.checkCapacity(currentPeople, currentCapacity, selectedSpot.getSpotName());
        System.out.println("\n" + capacityResult);
        
        System.out.printf("%nCurrent people in %s: %d/%d%n", 
            selectedSpot.getSpotName(), currentPeople, currentCapacity);
        
        if (currentPeople < currentCapacity) {
            System.out.println("Entrance permitted. Proceeding to dynamic distancing check...");
            SystemLogger.log("Robot " + robotId + " entered " + selectedSpot.getSpotName());
            DynamicDrinkServing dynamicCheck = new DynamicDrinkServing(selectedSpot, selectedRobot);
            dynamicCheck.performDistanceCheck();
            
            // Robot performs its task
            selectedRobot.performTask();
        } else {
            System.out.println("Capacity reached. Entrance not permitted.");
            System.out.printf("Average waiting time: %d minutes%n", selectedSpot.getAvgTimePerRobot());
            SystemLogger.log("Robot " + robotId + " denied entry to " + selectedSpot.getSpotName() + " - capacity full");
            
            String waitChoice = InputValidator.getValidYesNo("Would you like to wait? (yes/no): ");
            
            if (waitChoice.equals("yes")) {
                System.out.println("Robot is now waiting. After waiting period, entrance is permitted.");
                System.out.println("Proceeding to dynamic distancing check...");
                SystemLogger.log("Robot " + robotId + " waited and entered " + selectedSpot.getSpotName());
                DynamicDrinkServing dynamicCheck = new DynamicDrinkServing(selectedSpot, selectedRobot);
                dynamicCheck.performDistanceCheck();
                
                // Robot performs its task
                selectedRobot.performTask();
            } else {
                String anotherChoice = InputValidator.getValidYesNo("Would you like to visit another dining area? (yes/no): ");
                if (anotherChoice.equals("yes")) {
                    checkSpotCapacity();
                }
            }
        }
    }
    
    private void manageRobotSchedule() {
        System.out.println("\n=== Robot Scheduling ===");
        System.out.println("1. Add new robot shift");
        System.out.println("2. Check robot availability");
        System.out.println("3. Return to main menu");
        
        int choice = InputValidator.getValidInt("Enter choice: ", 1, 3);
        
        switch (choice) {
            case 1:
                displayRobotInfo();
                String robotId = InputValidator.getValidRobotId("Enter Robot ID: ");
                LocalTime start = InputValidator.getValidTime("Enter start time (HH:MM): ");
                LocalTime end = InputValidator.getValidEndTime("Enter end time (HH:MM): ", start);
                
                scheduler.addRobotShift(robotId, start, end);
                System.out.println("Shift added successfully.");
                break;
                
            case 2:
                displayRobotInfo();
                String checkRobotId = InputValidator.getValidRobotId("Enter Robot ID: ");
                
                // Display the robot's schedule
                scheduler.displayRobotSchedule(checkRobotId);
                
                // Check availability at current time
                LocalTime currentTime = LocalTime.now();
                boolean available = scheduler.isRobotAvailable(checkRobotId, currentTime);
                System.out.println("\nCurrent Time: " + currentTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                System.out.println("Robot " + checkRobotId + " is " + (available ? "available" : "not available") + 
                                  " at the current time.");
                break;
                
            case 3:
                return;
        }
    }
} 
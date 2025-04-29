public class Robot {
    protected String robotId;
    protected String robotName;
    protected String robotType;
    
    public Robot(String id, String name, String type) {
        this.robotId = id;
        this.robotName = name;
        this.robotType = type;
    }
    
    // Common methods for all robots
    public String getRobotId() { return robotId; }
    public String getRobotName() { return robotName; }
    public String getRobotType() { return robotType; }
    
    // Method to be overridden by subclasses
    public void performTask() {
        System.out.println("Robot " + robotName + " is performing a generic task");
    }
    
    // Overloaded methods for different types of capacity checks
    public boolean checkCapacity(int current, int max) {
        return current < max;
    }
    
    public boolean checkCapacity(double current, double max) {
        return current < max;
    }
    
    public String checkCapacity(int current, int max, String location) {
        if (current < max) {
            return "Capacity check passed for " + location;
        } else {
            return "Capacity exceeded for " + location;
        }
    }
} 
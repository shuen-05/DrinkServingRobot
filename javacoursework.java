// Main class for the Drink Serving Robotic System
public class javacoursework {
    public static void main(String[] args) {
        // Initialize the system with logging
        SystemLogger.initializeLog();
        StaticDrinkServing system = new StaticDrinkServing();
        system.startSystem();
    }
}
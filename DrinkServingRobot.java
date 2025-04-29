import java.time.LocalTime;

public class DrinkServingRobot extends Robot {
    private int drinksServed;
    private double averageServeTime;
    private boolean isBusy;
    private LocalTime busyUntil;
    
    public DrinkServingRobot(String id, String name, double serveTime) {
        super(id, name, "Drink Serving");
        this.drinksServed = 0;
        this.averageServeTime = serveTime;
        this.isBusy = false;
        this.busyUntil = null;
    }
    
    @Override
    public void performTask() {
        if (!isBusy) {
            System.out.println("Robot " + robotName + " is serving drinks");
            drinksServed++;
            isBusy = true;
            busyUntil = LocalTime.now().plusMinutes((long) averageServeTime);
            System.out.println("Robot will be busy until: " + busyUntil.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }
    
    // Overloaded methods specific to drink serving
    public void performTask(String drinkType) {
        if (!isBusy) {
            System.out.println("Robot " + robotName + " is serving " + drinkType);
            drinksServed++;
            isBusy = true;
            busyUntil = LocalTime.now().plusMinutes((long) averageServeTime);
            System.out.println("Robot will be busy until: " + busyUntil.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }
    
    public void performTask(String drinkType, int quantity) {
        if (!isBusy) {
            System.out.println("Robot " + robotName + " is serving " + quantity + " " + drinkType + "(s)");
            drinksServed += quantity;
            isBusy = true;
            busyUntil = LocalTime.now().plusMinutes((long) (averageServeTime * quantity));
            System.out.println("Robot will be busy until: " + busyUntil.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }
    
    // Additional methods specific to drink serving
    public int getDrinksServed() { return drinksServed; }
    public double getAverageServeTime() { return averageServeTime; }
    
    public boolean isBusy() {
        if (isBusy && busyUntil != null) {
            if (LocalTime.now().isAfter(busyUntil)) {
                isBusy = false;
                busyUntil = null;
            }
        }
        return isBusy;
    }
    
    public LocalTime getBusyUntil() {
        return busyUntil;
    }
    
    // Override capacity check with drink-serving specific logic
    @Override
    public String checkCapacity(int current, int max, String location) {
        String baseResult = super.checkCapacity(current, max, location);
        if (current >= max) {
            return baseResult + " - No more drinks can be served";
        }
        return baseResult + " - Ready to serve drinks";
    }
} 
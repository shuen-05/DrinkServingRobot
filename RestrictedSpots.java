public class RestrictedSpots {
    private String spotID;
    private String spotName;
    private double spotArea;
    private int avgTimePerRobot;
    private int maxCapacity;
    
    public RestrictedSpots(String id, String name, double area, int time) {
        this.spotID = id;
        this.spotName = name;
        this.spotArea = area;
        this.avgTimePerRobot = time;
        this.maxCapacity = calculateMaxCapacity(area);
    }
    
    private int calculateMaxCapacity(double area) {
        return (int) Math.floor(area / 1.0);
    }
    
    // Getters and setters
    public String getSpotID() { return spotID; }
    public String getSpotName() { return spotName; }
    public double getSpotArea() { return spotArea; }
    public int getAvgTimePerRobot() { return avgTimePerRobot; }
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int capacity) { this.maxCapacity = capacity; }
} 
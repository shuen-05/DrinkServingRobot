import java.io.*;
import java.time.LocalDateTime;

public class SystemLogger {
    private static final String LOG_FILE = "system_log.txt";
    
    public static void initializeLog() {
        try {
            // Clear previous log or create new file
            PrintWriter writer = new PrintWriter(LOG_FILE);
            writer.println("=== System Log ===");
            writer.println("Initialized: " + LocalDateTime.now());
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error initializing log file: " + e.getMessage());
        }
    }
    
    public static void log(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(LocalDateTime.now() + " - " + message);
        } catch (IOException e) {
            System.err.println("Error writing to log: " + e.getMessage());
        }
    }
} 
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputValidator {
    private static Scanner scanner = new Scanner(System.in);

    public static int getValidInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.println("Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static double getValidDouble(String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            try {
                double input = Double.parseDouble(scanner.nextLine());
                if (input >= min) {
                    return input;
                }
                System.out.println("Please enter a number greater than or equal to " + min);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static String getValidRobotId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equals("20612945") || input.equals("20611251") || input.equals("20722250")) {
                return input;
            }
            System.out.println("Invalid Robot ID. Please enter one of: 20612945, 20611251, 20722250");
        }
    }

    public static LocalTime getValidTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("now")) {
                return LocalTime.now();
            }
            try {
                return LocalTime.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please enter time in HH:MM format or 'now'");
            }
        }
    }

    public static LocalTime getValidEndTime(String prompt, LocalTime startTime) {
        while (true) {
            LocalTime endTime = getValidTime(prompt);
            if (endTime.isAfter(startTime)) {
                return endTime;
            }
            System.out.println("End time must be after start time (" + 
                startTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) + ")");
        }
    }

    public static String getValidYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("no")) {
                return input;
            }
            System.out.println("Please enter 'yes' or 'no'");
        }
    }
} 
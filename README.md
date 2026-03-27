# DrinkServingRobot

## Compilation Instructions

This project is compiled using **IntelliJ IDEA** (or any compatible IDE) with **Java**. It has no external dependencies — only the Java Standard Library is required.

### Prerequisites
- IntelliJ IDEA (or compatible IDE / command line `javac`)
- Java JDK 11 or higher (uses `java.time` API)

### Step-by-Step Compilation Guide

1. **Open the Project**
   - Open IntelliJ IDEA
   - Select "Open" and navigate to the `DrinkServingRobot-main` project directory
   - All source `.java` files are in the root directory of the project

2. **Configure Java Version**
   - Go to `File` → `Project Structure` → `Project`
   - Set `Project SDK` to Java 11 or higher
   - Set `Project language level` accordingly

3. **Compile the Project**
   - In IntelliJ, use `Build` → `Build Project` to compile all classes
   - Alternatively, from the command line in the project root:
     ```
     javac *.java
     ```

4. **Run the Application**
   - Run the `javacoursework` class as the entry point
   - In IntelliJ, right-click `javacoursework.java` and select `Run 'javacoursework.main()'`
   - From the command line:
     ```
     java javacoursework
     ```

### Dependencies
This project uses only Java Standard Library packages:
- `java.time` (LocalTime, LocalDate, LocalDateTime, DateTimeFormatter)
- `java.io` (FileWriter, BufferedWriter, PrintWriter)
- `java.util` (HashMap, Map, List, ArrayList, Scanner)

### Special Settings
- Main class: `javacoursework`
- Log file output: `system_log.txt` (created/overwritten in the working directory on each run)
- All source files are in the default (unnamed) package at the project root

---

## Implemented and Working Properly

### System Logging
- **Persistent log file**: `system_log.txt` is created fresh on every system start via `SystemLogger.initializeLog()`
- All significant system events are timestamped and appended using `LocalDateTime.now()`
- Logged events include: system initialisation, robot scheduling, capacity checks, spot entry, distancing violations, and system shutdown
- Log entries use append mode (`FileWriter` with `true`) so no events are lost mid-session
- Errors during log writes are gracefully reported to `System.err` without crashing the application

### Input Validation
- **Integer input**: `getValidInt()` loops until a valid integer within a specified min–max range is entered; catches `NumberFormatException`
- **Double input**: `getValidDouble()` loops until a valid decimal at or above a minimum value is entered
- **Robot ID input**: `getValidRobotId()` only accepts the three known robot IDs (`20612945`, `20611251`, `20722250`); rejects all other values with an error message
- **Time input**: `getValidTime()` parses `HH:MM` format using `LocalTime.parse()`; also accepts the keyword `"now"` to use the current system time; catches `DateTimeParseException`
- **End time validation**: `getValidEndTime()` ensures the entered end time is strictly after the provided start time
- **Yes/No input**: `getValidYesNo()` normalises input to lowercase and only accepts `"yes"` or `"no"`

### Robot Scheduling System
- Robots are assigned shifts via `RobotScheduler.addRobotShift()`, storing `LocalTime` start and end pairs per robot ID in a `HashMap<String, List<LocalTime[]>>`
- `isRobotAvailable()` checks whether a given time falls within any of a robot's scheduled shifts and returns `false` (not available / currently scheduled) if so
- `displayRobotSchedule()` prints all scheduled shifts for a robot in numbered order with formatted times
- Multiple shifts per robot are supported through the `List<LocalTime[]>` structure
- All shift additions are logged to `system_log.txt`

### Spot Capacity Management
- Five dining areas are pre-configured at system start, each with a unique ID, name, floor area (in m²), and average robot time (minutes):
  - **DF** — Dining Foyer (25.0 m², 10 min avg)
  - **MDH** — Main Dining Hall (75.0 m², 20 min avg)
  - **DR1** — Dining Room One (20.0 m², 8 min avg)
  - **DR2** — Dining Room Two (20.0 m², 8 min avg)
  - **FDR** — Family Dining Room (30.0 m², 12 min avg)
- Maximum capacity is calculated automatically from floor area using `Math.floor(area / 1.0)` (one person per square metre)
- Current occupancy is simulated with `Math.random()` bounded by max capacity
- The capacity check calls `DrinkServingRobot.checkCapacity()` which extends the base `Robot` method with drink-serving-specific messaging
- If capacity is full, the user is offered the option to wait (and proceed anyway) or visit a different dining area

### Dynamic Distancing Check
- Triggered after a robot successfully enters a dining area
- Prompts the user to input distances (in metres) from people on all four sides: Left, Right, Front, Back
- Any direction measuring less than 1.0 metre is flagged as unsafe, with the exact shortfall displayed (e.g. `Left (0.35m needed)`)
- If all four distances pass, a safe confirmation is shown
- If any direction fails, a full contact information report is printed including robot ID, name, type, spot ID, spot name, current date, and current time
- All distancing results — pass or violation — are logged to `system_log.txt`

### Robot Busy State Tracking
- `DrinkServingRobot` tracks whether it is currently busy via an `isBusy` flag and a `busyUntil` `LocalTime`
- When `performTask()` is called, the robot is marked busy until `LocalTime.now()` plus its average serve time in minutes
- `isBusy()` auto-clears the busy state if the current time has passed `busyUntil`, avoiding stale locks
- Overloaded `performTask(String drinkType)` and `performTask(String drinkType, int quantity)` extend the base task with drink-specific output; the quantity overload scales both the drinks-served count and busy duration proportionally
- Busy robots are shown in the available robots list with their expected free time displayed

### OOP Design — Inheritance and Overloading
- `Robot` is the base class holding `robotId`, `robotName`, `robotType`, and three overloaded `checkCapacity()` methods (int/int, double/double, int/int/String)
- `DrinkServingRobot` extends `Robot`, overrides `performTask()` and `checkCapacity(int, int, String)`, and adds drink-serving-specific state and three overloaded `performTask()` variants
- `DynamicDrinkServing` accepts a `Robot` reference (not `DrinkServingRobot`), demonstrating polymorphic usage of the base type

---

## New Java Classes

1. **`javacoursework.java`**
   - **Purpose**: Entry point of the application. Calls `SystemLogger.initializeLog()` to set up the log file, then constructs and starts a `StaticDrinkServing` session.

2. **`Robot.java`**
   - **Purpose**: Base class for all robot types. Stores the robot's ID, name, and type. Defines `performTask()` (overridable) and three overloaded `checkCapacity()` signatures to demonstrate method overloading.

3. **`DrinkServingRobot.java`**
   - **Purpose**: Extends `Robot` for drink-serving duties. Adds busy-state tracking with `LocalTime`-based expiry, three overloaded `performTask()` methods, a drinks-served counter, and an overridden `checkCapacity()` that appends drink-serving context to the base result.

4. **`StaticDrinkServing.java`**
   - **Purpose**: Core system controller. Initialises the five `RestrictedSpots`, the three `DrinkServingRobot` instances, and the `RobotScheduler`. Drives the main menu loop, handles spot selection, enforces shift and busy-state checks before allowing robot entry, and delegates to `DynamicDrinkServing` for distancing checks.

5. **`DynamicDrinkServing.java`**
   - **Purpose**: Handles the real-time distancing check once a robot has entered a dining area. Collects four directional distance readings, evaluates each against the 1.0 m threshold, reports directions requiring adjustment with their exact shortfall, and logs all outcomes.

6. **`RobotScheduler.java`**
   - **Purpose**: Manages robot shift schedules using a `HashMap<String, List<LocalTime[]>>`. Supports adding multiple shifts per robot, checking availability at a given time, and displaying a formatted schedule list.

7. **`RestrictedSpots.java`**
   - **Purpose**: Represents a dining area with an ID, name, floor area, average robot time, and an auto-calculated maximum capacity (`floor(area / 1.0)`). Provides getters and a capacity setter.

8. **`InputValidator.java`**
   - **Purpose**: Centralised input validation utility with static methods for reading integers, doubles, robot IDs, times (`HH:MM` or `"now"`), end times (must be after start), and yes/no responses — all with looping retry and descriptive error messages.

9. **`SystemLogger.java`**
   - **Purpose**: File-based logging utility. `initializeLog()` creates or clears `system_log.txt` and writes an initialisation header. `log(String)` appends a timestamped entry using `LocalDateTime.now()` with safe resource handling via try-with-resources.

---

## Unexpected Problems

### Robot Availability Logic Inversion
- **Problem**: `RobotScheduler.isRobotAvailable()` returns `false` when a robot *has* an active shift (i.e. is scheduled and therefore available to work), and `true` when it has no shift. The naming is counterintuitive — "available" in the scheduler context actually means "not scheduled" (i.e. off duty).
- **Impact**: In `StaticDrinkServing.checkSpotCapacity()`, the condition to show available robots and gate entry had to be written as `!scheduler.isRobotAvailable(...)`, which reads confusingly and required extra care to get the logic correct.
- **Solution**: Accepted the naming as-is and consistently applied the negation (`!`) at every call site. A cleaner fix would be to rename the method to `isUnscheduled()` or invert its return value and rename to `hasActiveShift()`.

### Busy State Not Checked Before Entry
- **Problem**: When a robot attempts to enter a dining area, both the shift check and the busy check need to pass. If these were evaluated in the wrong order or inconsistently, the user could receive conflicting messages or be blocked for the wrong reason.
- **Impact**: A robot could theoretically be shown as available (shift active, not busy) but be rejected at the gate due to check-ordering inconsistencies.
- **Solution**: Structured the checks sequentially in `checkSpotCapacity()` — shift validation first, then busy-state check — with distinct error messages for each failure path, ensuring the user always receives a clear and accurate reason for rejection.

### Scanner Conflicts Between Classes
- **Problem**: Both `StaticDrinkServing` and `DynamicDrinkServing` instantiate their own `Scanner` on `System.in`, while `InputValidator` also maintains a static `Scanner` on the same stream. Multiple `Scanner` instances on one input stream can cause buffering issues where one scanner consumes a newline intended for another.
- **Impact**: When transitioning between the static menu and the dynamic distancing check, a newline left in the buffer could cause a prompt to be skipped or an unexpected empty string to be read.
- **Solution**: Ensured that `InputValidator`'s static scanner handles all menu interactions and that distance readings in `DynamicDrinkServing` go through `InputValidator.getValidDouble()`, minimising the number of active scanners on `System.in` at any given time.

### Log File Path Dependency on Working Directory
- **Problem**: `SystemLogger` writes `system_log.txt` using a relative path, meaning the file is created in whichever directory the JVM is launched from — not necessarily the project root.
- **Impact**: When running from IntelliJ with a non-standard working directory setting, the log file would appear in an unexpected location (or fail silently if the directory was read-only), making it difficult to locate after a session.
- **Solution**: Verified that IntelliJ's default working directory is the project root for standard run configurations. The `system_log.txt` file is reliably produced there under normal usage. A more robust fix would use `System.getProperty("user.dir")` explicitly or accept the log path as a configurable parameter.

---

*This README documents the Drink Serving Robotic System built in Java.*

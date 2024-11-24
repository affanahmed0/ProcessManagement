import java.util.*;
import java.io.*;

public class Simulator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scheduler scheduler = new Scheduler();
        ProcessManager processManager = new ProcessManager();
        List<Process> processes = new ArrayList<>();
        List<Process> readyQueue = new ArrayList<>(); // Ready Queue for processes
        Statistics stats = null;    

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Add Process");
            System.out.println("2. Load Processes from File");
            System.out.println("3. Save Processes to File");
            System.out.println("4. Generate Random Processes");
            System.out.println("5. Run Scheduler");
            System.out.println("6. View Statistics");
            System.out.println("7. Exit");

            int choice = getValidInput(scanner);

            switch (choice) {
                case 1:
                    // Manually add process
                    System.out.print("Enter PID, Arrival Time, Burst Time, Priority: ");
                    int pid = getValidInput(scanner);
                    int arrivalTime = getValidInput(scanner);
                    int burstTime = getValidInput(scanner);
                    int priority = getValidInput(scanner);
                    Process newProcess = new Process(pid, arrivalTime, burstTime, priority);
                    processes.add(newProcess);
                    try {
                        // Save manually added process to the file
                        processManager.saveProcessesToFile(processes, "processes.txt");
                        System.out.println("Process added and saved to file.");
                    } catch (IOException e) {
                        System.out.println("Error saving process to file: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Enter filename to load processes from: ");
                    String loadFilename = scanner.next();
                    try {
                        processes = processManager.loadProcessesFromFile(loadFilename);
                        System.out.println("Processes loaded successfully!");
                    } catch (IOException e) {
                        System.out.println("Error loading file: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Enter filename to save processes: ");
                    String saveFilename = scanner.next();
                    try {
                        processManager.saveProcessesToFile(processes, saveFilename);
                        System.out.println("Processes saved successfully!");
                    } catch (IOException e) {
                        System.out.println("Error saving file: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.print("Enter the number of processes to generate: ");
                    int numProcesses = getValidInput(scanner);
                    generateRandomProcesses(processes, numProcesses);
                    try {
                        // Save generated processes to file
                        processManager.saveProcessesToFile(processes, "processes.txt");
                        System.out.println(numProcesses + " random processes generated and saved to file!");
                    } catch (IOException e) {
                        System.out.println("Error saving random processes to file: " + e.getMessage());
                    }
                    break;

                    case 5:
                    processes.clear();
                    try {
                        // Load processes from file
                        processes = processManager.loadProcessesFromFile("processes.txt");
                        System.out.println("Processes loaded from file for scheduling.");
                    } catch (IOException e) {
                        System.out.println("Error loading processes from file: " + e.getMessage());
                    }
                
                    readyQueue.clear();
                    readyQueue.addAll(processes);
                
                    // Sort readyQueue by arrival time for initial scheduling
                    Collections.sort(readyQueue, Comparator.comparingInt(Process::getArrivalTime));
                
                    // Display scheduling algorithm menu
                    System.out.println("Choose Scheduling Algorithm:");
                    System.out.println("1. FCFS");
                    System.out.println("2. Round Robin");
                    System.out.println("3. Priority");
                    System.out.println("4. Shortest Job First (SJF)");
                    System.out.println("5. Shortest Remaining Time (SRT)");
                
                    int algorithmChoice = getValidInput(scanner);
                    String ganttChart = ""; // Initialize Gantt chart string
                
                    switch (algorithmChoice) {
                        case 1: // FCFS Scheduling
                            ganttChart = scheduler.fcfs(readyQueue);
                            break;
                
                        case 2: // Round Robin Scheduling
                            System.out.print("Enter Time Quantum: ");
                            int timeQuantum = getValidInput(scanner);
                            ganttChart = scheduler.roundRobin(readyQueue, timeQuantum);
                            break;
                
                        case 3: // Priority Scheduling
                            System.out.print("Choose Priority Type (1 for Preemptive, 2 for Non-Preemptive): ");
                            int priorityType = getValidInput(scanner);
                            if (priorityType == 1) {
                                ganttChart = scheduler.priorityScheduling(readyQueue);
                            } else if (priorityType == 2) {
                                ganttChart = scheduler.priorityScheduling(readyQueue);
                            } else {
                                System.out.println("Invalid priority type. Returning to main menu.");
                                continue;
                            }
                            break;
                
                        case 4: // Shortest Job First (SJF) Scheduling
                            System.out.print("Shortest Job First(SJF) Scheduling... ");
                            ganttChart = scheduler.shortestJobFirst(readyQueue); // true for Preemptive, false for Non-Preemptive
                            break;
                
                        case 5: // Shortest Remaining Time (SRT) Scheduling
                            ganttChart = scheduler.shortestRemainingTime(readyQueue);
                            break;
                
                        default:
                            System.out.println("Invalid choice. Returning to main menu.");
                            continue;
                    }
                
                    // Generate and display statistics for the selected scheduling algorithm
                    stats = new Statistics(readyQueue, ganttChart);
                    System.out.println("Scheduling completed. View statistics now or return to main menu.");
                    break;
                


                
                
                case 6:
                    if (stats == null) {
                    System.out.println("No statistics available. Please run a scheduling algorithm first.");
                } else {
                    stats.displayStatistics();
                }
                    break;

                case 7:
                    System.out.println("Exiting the program...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Helper method for input validation
    private static int getValidInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next(); // Consume the invalid input
        }
        return scanner.nextInt();
    }

    // Method to generate random processes
    private static void generateRandomProcesses(List<Process> processes, int numProcesses) {
        Random random = new Random();
        for (int i = 0; i < numProcesses; i++) {
            int pid = i + 1;  // Process ID starts from 1
            int arrivalTime = random.nextInt(100);  // Random arrival time between 0 and 99
            int burstTime = random.nextInt(20) + 1;  // Random burst time between 1 and 20
            int priority = random.nextInt(5) + 1;  // Random priority between 1 and 5
            processes.add(new Process(pid, arrivalTime, burstTime, priority));
        }
    }
}

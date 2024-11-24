import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler {

    // FCFS Scheduling Algorithm with Real-time Demonstration
    public String fcfs(List<Process> readyQueue) {
        System.out.println("\nStarting FCFS Scheduling...");
        
        // Sorting processes by arrival time
        Collections.sort(readyQueue, Comparator.comparingInt(Process::getArrivalTime));
        
        int currentTime = 0;
        StringBuilder ganttChart = new StringBuilder();
    
        for (Process process : readyQueue) {
            // Display the process that is being scheduled and the current time
            System.out.println("At time " + currentTime + "ms, scheduling process P" + process.getPid() +
                    " (Arrival: " + process.getArrivalTime() + ", Burst: " + process.getBurstTime() + ")");
    
            // If there's idle time (if current time is less than process arrival time)
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime(); // Jump forward to process arrival time
                System.out.println("System is idle until " + currentTime + "ms.");
            }
            
            // Calculate the process's completion time, turnaround time, and waiting time
            process.setCompletionTime(currentTime + process.getBurstTime());
            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
            
            // Add process to the Gantt chart
            ganttChart.append("| P").append(process.getPid()).append(" ");
            
            // Update current time after process execution
            currentTime += process.getBurstTime();
    
            // Optional delay for real-time simulation (1-second delay per process)
            try {
                Thread.sleep(1000); // 1-second delay for real-time scheduling
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        ganttChart.append("|");
    
        // Print the final Gantt chart
        System.out.println("Gantt Chart: " + ganttChart.toString());
        
        // Save process information and Gantt chart to a file
        saveProcessesToFile(readyQueue, ganttChart.toString(), "processes_with_gantt.txt");
        
        // Return the Gantt chart
        return ganttChart.toString();
    }
    
    
    

    // Round Robin Scheduling Algorithm (Simplified)
    public String roundRobin(List<Process> readyQueue, int timeQuantum) {
        System.out.println("\nStarting Round Robin Scheduling...");
    
        // Queue to handle processes in Round Robin
        Queue<Process> queue = new LinkedList<>(readyQueue);
        int currentTime = 0;
        StringBuilder ganttChart = new StringBuilder();
        List<String> executionOrder = new ArrayList<>();
    
        // Track the remaining burst time for each process
        for (Process process : readyQueue) {
            process.setRemainingBurstTime(process.getBurstTime()); // Initialize remaining burst time
        }
    
        // Round Robin scheduling loop
        while (!queue.isEmpty()) {
            Process process = queue.poll();
    
            // Display the process that is currently running
            System.out.println("At time " + currentTime + "ms, running process P" + process.getPid() + " with remaining burst time: " + process.getRemainingBurstTime() + "ms");
    
            if (process.getRemainingBurstTime() > timeQuantum) {
                // Process has remaining burst time > timeQuantum
                currentTime += timeQuantum;
                process.setRemainingBurstTime(process.getRemainingBurstTime() - timeQuantum); // Decrease remaining burst time
                ganttChart.append("| P").append(process.getPid()).append(" ");
                executionOrder.add("P" + process.getPid());
    
                queue.offer(process);  // Re-add the process to the queue if it's not finished
            } else {
                // Process finishes in this round
                currentTime += process.getRemainingBurstTime();
                ganttChart.append("| P").append(process.getPid()).append(" ");
                executionOrder.add("P" + process.getPid());
    
                // Set the completion time and calculate the waiting and turnaround times
                process.setCompletionTime(currentTime);
                process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
                process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
    
                // Set the remaining burst time to 0 as the process has finished
                process.setRemainingBurstTime(0);
            }
    
            // Optional delay for real-time simulation (optional, to make it visible in real-time)
            try {
                Thread.sleep(1000); // 1-second delay for real-time scheduling
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        // Output the Gantt Chart
        System.out.println("Gantt Chart: " + ganttChart.toString());
    
        // Save process details to a file
        saveProcessesToFile(readyQueue, ganttChart.toString(), "processes_with_gantt.txt");
    
        // Return the Gantt Chart string
        return ganttChart.toString(); // Return the Gantt Chart as a String
    }
    
    
    //prioriy Scheduling
    public String priorityScheduling(List<Process> readyQueue) {
        System.out.println("\nStarting Priority Scheduling...");
    
        AtomicInteger currentTime = new AtomicInteger(0); // Mutable time using AtomicInteger
        StringBuilder ganttChart = new StringBuilder();
    
        // Initialize the remaining burst time for all processes
        for (Process process : readyQueue) {
            process.setRemainingBurstTime(process.getBurstTime());
        }
    
        while (!readyQueue.isEmpty()) {
            // Find the process with the highest priority that has arrived
            Process currentProcess = readyQueue.stream()
                .filter(p -> p.getArrivalTime() <= currentTime.get())
                .min(Comparator.comparingInt(Process::getPriority)) // Smaller priority value means higher priority
                .orElse(null);
    
            if (currentProcess != null) {
                // Log the running process
                System.out.println("At time " + currentTime.get() + "ms, running process P" +
                    currentProcess.getPid() + " (Priority: " + currentProcess.getPriority() +
                    ", Remaining Burst Time: " + currentProcess.getRemainingBurstTime() + "ms)");
    
                // Execute the process for one time unit (preemptive)
                currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
                ganttChart.append("| P").append(currentProcess.getPid()).append(" ");
                currentTime.incrementAndGet();
    
                // If the process finishes, calculate its metrics and remove it from the queue
                if (currentProcess.getRemainingBurstTime() == 0) {
                    currentProcess.setCompletionTime(currentTime.get());
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    readyQueue.remove(currentProcess);
                }
            } else {
                // Idle time if no process is ready
                System.out.println("At time " + currentTime.get() + "ms, CPU is idle.");
                ganttChart.append("| Idle ");
                currentTime.incrementAndGet();
            }
        }
        ganttChart.append("|");
    
        System.out.println("Gantt Chart: " + ganttChart.toString());
        saveProcessesToFile(readyQueue, ganttChart.toString(), "priority_gantt.txt");
    
        return ganttChart.toString();
    }
    
    
    //SJF

            public String shortestJobFirst(List<Process> readyQueue) {
    System.out.println("\nStarting Shortest Job First (SJF) Scheduling...");

    AtomicInteger currentTime = new AtomicInteger(0); // Mutable time using AtomicInteger
    StringBuilder ganttChart = new StringBuilder();

    // Sort processes by arrival time initially
    readyQueue.sort(Comparator.comparingInt(Process::getArrivalTime));

    while (!readyQueue.isEmpty()) {
        // Find the process with the shortest burst time that has arrived
        Process currentProcess = readyQueue.stream()
            .filter(p -> p.getArrivalTime() <= currentTime.get())
            .min(Comparator.comparingInt(Process::getBurstTime))
            .orElse(null);

        if (currentProcess != null) {
            // Log the process running
            System.out.println("At time " + currentTime.get() + "ms, running process P" +
                currentProcess.getPid() + " (Burst Time: " + currentProcess.getBurstTime() + "ms)");

            // Simulate process execution
            ganttChart.append("| P").append(currentProcess.getPid()).append(" ");
            currentTime.addAndGet(currentProcess.getBurstTime());

            // Calculate metrics and remove the process from the ready queue
            currentProcess.setCompletionTime(currentTime.get());
            currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
            currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
            readyQueue.remove(currentProcess);
        } else {
            // Idle time if no process is ready
            System.out.println("At time " + currentTime.get() + "ms, CPU is idle.");
            ganttChart.append("| Idle ");
            currentTime.incrementAndGet();
        }
    }
    ganttChart.append("|");

    System.out.println("Gantt Chart: " + ganttChart.toString());
    saveProcessesToFile(readyQueue, ganttChart.toString(), "sjf_gantt.txt");

    return ganttChart.toString();
}

    

    //SRT

    public String shortestRemainingTime(List<Process> readyQueue) {
        System.out.println("\nStarting Shortest Remaining Time (SRT) Scheduling...");
    
        AtomicInteger currentTime = new AtomicInteger(0); // Use AtomicInteger for mutable time
        StringBuilder ganttChart = new StringBuilder();
    
        // Initialize remaining burst time for all processes
        for (Process process : readyQueue) {
            process.setRemainingBurstTime(process.getBurstTime());
        }
    
        while (!readyQueue.isEmpty()) {
            // Find the process with the shortest remaining time that has arrived
            Process currentProcess = readyQueue.stream()
                .filter(p -> p.getArrivalTime() <= currentTime.get())
                .min(Comparator.comparingInt(Process::getRemainingBurstTime))
                .orElse(null);
    
            if (currentProcess != null) {
                // Log running process
                System.out.println("At time " + currentTime.get() + "ms, running process P" + currentProcess.getPid() +
                    " (Remaining Burst Time: " + currentProcess.getRemainingBurstTime() + "ms)");
    
                // Execute the process for one time unit
                currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
                ganttChart.append("| P").append(currentProcess.getPid()).append(" ");
                currentTime.incrementAndGet();
    
                // If the process finishes, calculate its metrics
                if (currentProcess.getRemainingBurstTime() == 0) {
                    currentProcess.setCompletionTime(currentTime.get());
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    readyQueue.remove(currentProcess);
                }
            } else {
                // Idle time if no process is ready
                System.out.println("At time " + currentTime.get() + "ms, CPU is idle.");
                ganttChart.append("| Idle ");
                currentTime.incrementAndGet();
            }
        }
        ganttChart.append("|");
    
        System.out.println("Gantt Chart: " + ganttChart.toString());
        saveProcessesToFile(readyQueue, ganttChart.toString(), "srt_gantt.txt");
    
        return ganttChart.toString();
    }
    
    
    


    public void saveProcessesToFile(List<Process> processes, String ganttChart, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Process Scheduling Result with Gantt Chart:");
            writer.println("Gantt Chart: " + ganttChart);
            writer.println("\nProcess Details:");
            for (Process process : processes) {
                writer.println("PID: " + process.getPid() + ", Arrival Time: " + process.getArrivalTime() +
                        ", Burst Time: " + process.getBurstTime() + ", Waiting Time: " + process.getWaitingTime() +
                        ", Turnaround Time: " + process.getTurnaroundTime() + ", Completion Time: " + process.getCompletionTime());
            }
            System.out.println("Process information and Gantt chart saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving processes to file: " + e.getMessage());
        }
    }
}



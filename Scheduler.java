import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Scheduler {

    // FCFS Scheduling Algorithm with Real-time Demonstration
    public void fcfs(List<Process> readyQueue) {
        System.out.println("\nStarting FCFS Scheduling...");

        // Sorting processes by arrival time
        Collections.sort(readyQueue, Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        StringBuilder ganttChart = new StringBuilder();
        List<String> executionOrder = new ArrayList<>();

        for (Process process : readyQueue) {
            // Simulate real-time scheduling by printing each process as it's scheduled
            System.out.println("Scheduling process " + process.getPid() + " (Arrival: " + process.getArrivalTime() + ", Burst: " + process.getBurstTime() + ")");
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();  // Jump forward if idle time is needed
            }
            process.setCompletionTime(currentTime + process.getBurstTime());
            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());

            currentTime += process.getBurstTime();
            ganttChart.append("| P").append(process.getPid()).append(" ");
            executionOrder.add("P" + process.getPid());

            // Pause for real-time demonstration (optional, can be removed or adjusted for faster simulation)
            try {
                Thread.sleep(1000); // 1-second delay for visualization of each step
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Gantt Chart: " + ganttChart.toString());
        saveProcessesToFile(readyQueue, ganttChart.toString(), "processes_with_gantt.txt");
    }

    // Save processes with Gantt Chart to a file
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

    // Round Robin Scheduling Algorithm (Simplified)
    public void roundRobin(List<Process> readyQueue, int timeQuantum) {
        System.out.println("\nStarting Round Robin Scheduling...");

        Queue<Process> queue = new LinkedList<>(readyQueue);
        int currentTime = 0;
        StringBuilder ganttChart = new StringBuilder();
        List<String> executionOrder = new ArrayList<>();

        while (!queue.isEmpty()) {
            Process process = queue.poll();
            if (process.getBurstTime() > timeQuantum) {
                process.setBurstTime(process.getBurstTime() - timeQuantum);
                currentTime += timeQuantum;
                ganttChart.append("| P").append(process.getPid()).append(" ");
                executionOrder.add("P" + process.getPid());

                queue.offer(process);  // Re-add the process to the queue if it's not finished
            } else {
                currentTime += process.getBurstTime();
                ganttChart.append("| P").append(process.getPid()).append(" ");
                executionOrder.add("P" + process.getPid());

                process.setCompletionTime(currentTime);
                process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
                process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
            }

            // Optional delay for real-time simulation
            try {
                Thread.sleep(1000); // 1-second delay for real-time scheduling
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Gantt Chart: " + ganttChart.toString());
        saveProcessesToFile(readyQueue, ganttChart.toString(), "processes_with_gantt.txt");
    }
}

import java.util.ArrayList;
import java.util.List;

public class Statistics {
    private List<Process> processes;
    private String ganttChart;

    public Statistics(List<Process> processes, String ganttChart) {
        this.processes = new ArrayList<>(processes); // Copy of processes for immutability
        this.ganttChart = ganttChart;
    }

    public void displayStatistics() {
        System.out.println("\n================ Process Statistics ================");
        System.out.printf("%-10s %-15s %-15s %-15s %-15s %-15s\n",
                "PID", "Arrival Time", "Burst Time", "Completion Time", "Turnaround Time", "Waiting Time");

        // Display each process' details
        for (Process process : processes) {
            System.out.printf("%-10d %-15d %-15d %-15d %-15d %-15d\n",
                    process.getPid(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getCompletionTime(),
                    process.getTurnaroundTime(),
                    process.getWaitingTime());
        }

        System.out.println("\n================ Gantt Chart ================");
        System.out.println(ganttChart);

        System.out.println("\n================ Average Times ================");
        System.out.printf("Average Waiting Time: %.2f\n", calculateAverageWaitingTime());
        System.out.printf("Average Turnaround Time: %.2f\n", calculateAverageTurnaroundTime());
    }

    public double calculateAverageWaitingTime() {
        return processes.stream().mapToDouble(Process::getWaitingTime).average().orElse(0);
    }

    public double calculateAverageTurnaroundTime() {
        return processes.stream().mapToDouble(Process::getTurnaroundTime).average().orElse(0);
    }
}

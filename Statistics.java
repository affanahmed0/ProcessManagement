import java.util.*;
public class Statistics {
    private List<Process> processes;

    public Statistics(List<Process> processes) {
        if (processes == null) {
            throw new IllegalArgumentException("Processes list cannot be null");
        }
        this.processes = processes;
    }

    public double calculateAverageWaitingTime() {
        if (processes.isEmpty()) {
            return 0; // No processes to calculate on
        }
        double totalWaitingTime = 0;
        for (Process process : processes) {
            totalWaitingTime += process.getWaitingTime();
        }
        return totalWaitingTime / processes.size();
    }

    public double calculateAverageTurnaroundTime() {
        if (processes.isEmpty()) {
            return 0; // No processes to calculate on
        }
        double totalTurnaroundTime = 0;
        for (Process process : processes) {
            totalTurnaroundTime += process.getTurnaroundTime();
        }
        return totalTurnaroundTime / processes.size();
    }

    // Add other statistical calculations if needed
    public double calculateAverageCompletionTime() {
        if (processes.isEmpty()) {
            return 0; // No processes to calculate on
        }
        double totalCompletionTime = 0;
        for (Process process : processes) {
            totalCompletionTime += process.getCompletionTime();
        }
        return totalCompletionTime / processes.size();
    }
}

import java.io.*;
import java.util.*;

public class ProcessManager {

    public List<Process> loadProcessesFromFile(String filename) throws IOException {
        List<Process> processes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int pid = Integer.parseInt(parts[0]);
                int arrivalTime = Integer.parseInt(parts[1]);
                int burstTime = Integer.parseInt(parts[2]);
                int priority = Integer.parseInt(parts[3]);
                processes.add(new Process(pid, arrivalTime, burstTime, priority));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
        return processes;
    }

    public void saveProcessesToFile(List<Process> processes, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Process process : processes) {
                writer.write(process.getPid() + "," + process.getArrivalTime() + "," +
                             process.getBurstTime() + "," + process.getPriority() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }
}

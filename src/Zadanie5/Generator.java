package Zadanie5;

import java.util.ArrayList;
import java.util.Random;

public class Generator {
    public static ArrayList<Process> generateProcesses(ArrayList<CPU> allCPUs) {
        ArrayList<Process> processes = new ArrayList<>();
        Random random = new Random();
        int currentTime = 0;

        for (int i = 0; i < Simulation.NUMBER_OF_PROCESSES; i++) {
            double load = random.nextDouble() / 10.0;
            currentTime += 1 + random.nextInt(10);
            int executionTime = 10 + random.nextInt(150 * Simulation.NUMBER_OF_CPUS);
            CPU cpu = allCPUs.get(random.nextInt(allCPUs.size()));

            processes.add(new Process(i, load, currentTime, executionTime, cpu));
        }

        return processes;
    }
}

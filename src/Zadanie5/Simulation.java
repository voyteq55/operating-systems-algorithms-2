package Zadanie5;

import java.util.ArrayList;

public class Simulation {
    static final int NUMBER_OF_CPUS = 50;
    static final int NUMBER_OF_PROCESSES = 20000;
    static final double OVERLOAD_CONSTANT_P = 0.7;
    static final double RELAX_CONSTANT_R = 0.5;
    static final double RANDOM_PROCESSOR_REQUEST_NUMBER_Z = 5;

    public static void start() {
        ArrayList<CPU> allCPUs = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CPUS; i++) {
            allCPUs.add(new CPU(i));
        }

        ArrayList<Process> processes = Generator.generateProcesses(allCPUs);

        System.out.println("Symulacja dla " + NUMBER_OF_CPUS + " procesorów oraz " + NUMBER_OF_PROCESSES + " procesów");
        System.out.println("Parametry symulacji: p = " + OVERLOAD_CONSTANT_P + ", r = " + RELAX_CONSTANT_R + ", z = " + RANDOM_PROCESSOR_REQUEST_NUMBER_Z);

        System.out.println("\nStrategia 1:");
        simulate(new FirstStrategy(), Generator.deepCopy(processes), allCPUs);

        System.out.println("Strategia 2:");
        simulate(new SecondStrategy(), Generator.deepCopy(processes), allCPUs);

        System.out.println("Strategia 3:");
        simulate(new ThirdStrategy(), Generator.deepCopy(processes), allCPUs);

    }

    private static void simulate(Strategy strategy, ArrayList<Process> processes, ArrayList<CPU> allCPUs) {
        allCPUs.forEach(CPU::reset);

        int currentTime = 0;
        int nextProcessIndex = 0;
        ResultCollector resultCollector = new ResultCollector(allCPUs);
        while (nextProcessIndex < processes.size()) {
            for (CPU cpu : allCPUs) {
                cpu.executeActiveProcesses();
            }

            Process nextProcess = processes.get(nextProcessIndex);
            if (nextProcess.getArrivalTime() == currentTime) {
                strategy.assignProcess(nextProcess, allCPUs, resultCollector);
                nextProcessIndex++;
            }

            resultCollector.saveLoadsOnTick();
            currentTime++;
        }

        while (isThereActiveProcesses(allCPUs)) {
            for (CPU cpu : allCPUs) {
                cpu.executeActiveProcesses();
            }
            resultCollector.saveLoadsOnTick();

            currentTime++;
        }

        resultCollector.printResults();
    }

    private static boolean isThereActiveProcesses(ArrayList<CPU> allCPUs) {
        for (CPU cpu : allCPUs) {
            if (cpu.hasActiveProcesses()) {
                return true;
            }
        }
        return false;
    }
}

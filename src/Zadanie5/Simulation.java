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

        simulate(new FirstStrategy(), Generator.deepCopy(processes), allCPUs);
        simulate(new SecondStrategy(), Generator.deepCopy(processes), allCPUs);
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

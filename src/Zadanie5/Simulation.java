package Zadanie5;

import java.util.ArrayList;
import java.util.Iterator;

public class Simulation {
    static final int NUMBER_OF_CPUS = 50;
    static final int NUMBER_OF_PROCESSES = 10000;
    static final double OVERLOAD_CONSTANT_P = 0.7;
    static final double RELAX_CONSTANT_R = 0.2;
    static final double RANDOM_PROCESSOR_REQUEST_NUMBER_Z = 5;

    public static void start() {
        ArrayList<CPU> allCPUs = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CPUS; i++) {
            allCPUs.add(new CPU(i));
        }

        ArrayList<Process> processes = Generator.generateProcesses(allCPUs);

        int currentTime = 0;
        int nextProcessIndex = 0;

        ResultCollector resultCollector = new ResultCollector(allCPUs);
        while (nextProcessIndex < processes.size()) {

            for (CPU cpu : allCPUs) {
                cpu.executeActiveProcesses();
            }

            Process nextProcess = processes.get(nextProcessIndex);
            if (nextProcess.getArrivalTime() == currentTime) {

                //add process to current processes of one processor
                CPU assignedCPU = nextProcess.getInitialProcessor();
                if (assignedCPU.isOverloaded()) {
                    ArrayList<CPU> otherRandomCPUs = assignedCPU.otherRandomCPUs(allCPUs);
                    Iterator<CPU> cpuIterator = otherRandomCPUs.iterator();
                    boolean foundNotOverloadedCPU = false;
                    while (cpuIterator.hasNext() && !foundNotOverloadedCPU) {
                        resultCollector.addMigrationRequest();
                        CPU cpu = cpuIterator.next();
                        if (!cpu.isOverloaded()) {
                            resultCollector.addMigration();
                            assignedCPU = cpu;
                            foundNotOverloadedCPU = true;
                        }
                    }
                }
                assignedCPU.assignToActiveProcesses(nextProcess);
                //end add process

                nextProcessIndex++;
            }

            // register snapshot
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
        System.out.println(currentTime);

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

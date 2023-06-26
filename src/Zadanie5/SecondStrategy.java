package Zadanie5;

import java.util.ArrayList;
import java.util.Iterator;

public class SecondStrategy implements Strategy {

    @Override
    public void assignProcess(Process nextProcess, ArrayList<CPU> allCPUs, ResultCollector resultCollector) {
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
    }
}

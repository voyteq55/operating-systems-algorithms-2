package Zadanie5;

import java.util.ArrayList;

public class FirstStrategy implements Strategy {

    @Override
    public void assignProcess(Process nextProcess, ArrayList<CPU> allCPUs, ResultCollector resultCollector) {
        CPU assignedCPU = nextProcess.getInitialProcessor();
        ArrayList<CPU> otherRandomCPUs = assignedCPU.otherRandomCPUs(allCPUs);
        int randomCPUindex = 0;
        boolean foundNotOverloadedCPU = false;
        while (randomCPUindex < Simulation.RANDOM_PROCESSOR_REQUEST_NUMBER_Z && !foundNotOverloadedCPU && randomCPUindex < otherRandomCPUs.size()) {
            resultCollector.addMigrationRequest();
            if (!otherRandomCPUs.get(randomCPUindex).isOverloaded()) {
                resultCollector.addMigration();
                assignedCPU = otherRandomCPUs.get(randomCPUindex);
                foundNotOverloadedCPU = true;
            }
            randomCPUindex++;
        }
        assignedCPU.assignToActiveProcesses(nextProcess);
    }
}

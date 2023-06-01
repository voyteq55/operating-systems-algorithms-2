package Zadanie4;

import java.util.ArrayList;
import java.util.Random;

public class WorkingSetAllocation {
    private ArrayList<Process> processes;
    ArrayList<Process> pausedProcesses;
    private Process[] finishedProcesses;
    private final int totalPhysicalMemorySize;
    private int globalPageFaults;
    private int freeFramesToAllocate;
    private int currentTime;

    private static final int DELTA_T = 15;
    private static final int WORKING_SET_CHECK_FREQUENCY = 45;

    public WorkingSetAllocation(int totalPhysicalMemorySize, ArrayList<Process> processes) {
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
        this.processes = processes;
        this.pausedProcesses = new ArrayList<>();
        this.finishedProcesses = new Process[processes.size()];
        this.globalPageFaults = 0;
        this.freeFramesToAllocate = this.totalPhysicalMemorySize;
        this.currentTime = 0;

        allocateFramesProportionally();
    }

    private void allocateFramesProportionally() {
        int totalPageSetCount = 0;
        for (Process process : processes) {
            if (!process.isPaused()) {
                totalPageSetCount += process.getPageSetCount();
            }
        }
        for (Process process : processes) {
            if (!process.isPaused()) {
                int proportionalProcessFrameCount = freeFramesToAllocate * process.getPageSetCount() / totalPageSetCount;
                if (proportionalProcessFrameCount == 0 && process.getAllocatedPhysicalMemoryCapacity() == 0) {
                    proportionalProcessFrameCount = 1;
                }
                process.setPhysicalMemoryCapacity(process.getAllocatedPhysicalMemoryCapacity() + proportionalProcessFrameCount);
                freeFramesToAllocate -= proportionalProcessFrameCount;
            }
        }
        for (int i = 0; freeFramesToAllocate > 0; i = (i+1)%(processes.size())) {
            if (!processes.get(i).isPaused()) {
                processes.get(i).setPhysicalMemoryCapacity(processes.get(i).getAllocatedPhysicalMemoryCapacity() + 1);
                freeFramesToAllocate--;
            }
        }
    }

    public void start() {
        Random random = new Random();
        while (processes.size() > 0) {
            int randomProcessIndex = random.nextInt(processes.size());
            Process currentProcess = processes.get(randomProcessIndex);

            if (currentProcess.isFinished()) {
                freeFramesToAllocate += currentProcess.getAllocatedPhysicalMemoryCapacity();
                finishedProcesses[currentProcess.getIndex()] = processes.remove(randomProcessIndex);
                allocateFramesProportionally();
            } else if (!currentProcess.isPaused()) {
                if (currentProcess.isPageFaultNextPage()) {
                    globalPageFaults++;
                }
                if (currentProcess.isFinished()) {
                    freeFramesToAllocate += currentProcess.getAllocatedPhysicalMemoryCapacity();
                    finishedProcesses[currentProcess.getIndex()] = processes.remove(randomProcessIndex);
                    if (processes.size() > 0) {
                        allocateFramesProportionally();
                    }
                    continue;
                }

                if (currentTime % WORKING_SET_CHECK_FREQUENCY == 0) {
                    int sumOfWorkingSetSizes = 0;
                    pausedProcesses.clear();
                    for (Process process : processes) {
                        process.unpause();
                        process.setWorkingSetSize(process.numberOfUniqueRecentReferences(DELTA_T));
                        sumOfWorkingSetSizes += process.getWorkingSetSize();
                    }

                    while (sumOfWorkingSetSizes > totalPhysicalMemorySize) {
                        Process largestWorkingSetSizeProcess = null;
                        for (Process process : processes) {
                            if (!process.isPaused()) {
                                largestWorkingSetSizeProcess = process;
                            }
                        }
                        for (Process process : processes) {
                            if (!process.isPaused() && process.getWorkingSetSize() > largestWorkingSetSizeProcess.getWorkingSetSize()) {
                                largestWorkingSetSizeProcess = process;
                            }
                        }

                        largestWorkingSetSizeProcess.pause();
                        pausedProcesses.add(largestWorkingSetSizeProcess);
                        sumOfWorkingSetSizes -= largestWorkingSetSizeProcess.getWorkingSetSize();

                    }
                    freeFramesToAllocate = totalPhysicalMemorySize;
                    for (Process process : processes) {
                        if (!process.isPaused()) {
                            process.modifyAllocationBasedOnWorkingSetSize();
                            freeFramesToAllocate -= process.getWorkingSetSize();
                        }
                    }
                    allocateFramesProportionally();
                }

                currentTime++;

            } else {
                currentProcess.increaseTimeSpentPaused();
            }
        }
    }

    public void showResults() {
        System.out.println("\nModel strefowy (deltaT jednego procesu = " + DELTA_T + ")");
        System.out.println("Laczna liczba bledow strony: " + globalPageFaults);
        System.out.print("Liczba bledow strony poszczegolnych procesow: ");
        for (Process process : finishedProcesses) {
            System.out.print(process.getPageFaultsCount() + ", ");
        }

        System.out.print("\nCzas wstrzymania procesow (szamotanie): ");
        for (Process process : finishedProcesses) {
            System.out.print(process.getTimeSpentPaused() + ", ");
        }
        System.out.println();
    }

}

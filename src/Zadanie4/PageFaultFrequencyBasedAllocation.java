package Zadanie4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PageFaultFrequencyBasedAllocation {
    private ArrayList<Process> processes;
    ArrayList<Process> pausedProcesses;
    private Process[] finishedProcesses;
    private final int totalPhysicalMemorySize;
    private int globalPageFaults;
    private int freeFramesToAllocate;

    private static final int DELTA_T = 20;
    private static final int PAGE_FAULTS_CHECK_FREQUENCY = 5;
    private static final int UPPER_LIMIT = 10;
    private static final int LOWER_LIMIT = 5;

    public PageFaultFrequencyBasedAllocation(int totalPhysicalMemorySize, ArrayList<Process> processes) {
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
        this.processes = processes;
        this.pausedProcesses = new ArrayList<>();
        this.finishedProcesses = new Process[processes.size()];
        this.globalPageFaults = 0;
        this.freeFramesToAllocate = 0;

        allocateFrames();
    }

    private void allocateFrames() {
        int framesToAllocateLeft = totalPhysicalMemorySize;
        int totalPageSetCount = 0;
        for (Process process : processes) {
            totalPageSetCount += process.getPageSetCount();
        }
        for (Process process : processes) {
            int proportionalProcessFrameCount = totalPhysicalMemorySize * process.getPageSetCount() / totalPageSetCount;
            if (proportionalProcessFrameCount == 0) {
                proportionalProcessFrameCount = 1;
            }
            process.setPhysicalMemoryCapacity(proportionalProcessFrameCount);
            framesToAllocateLeft -= proportionalProcessFrameCount;
        }
        for (int i = 0; framesToAllocateLeft > 0; i++) {
            processes.get(i).setPhysicalMemoryCapacity(processes.get(i).getAllocatedPhysicalMemoryCapacity() + 1);
            framesToAllocateLeft--;
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
                tryAllocatingFramesToPausedProcesses();
            } else if (!currentProcess.isPaused()) {
                if (currentProcess.isPageFaultNextPage()) {
                    globalPageFaults++;
                }
                // zeby index nie byl poza size of pagesReferences
                if (currentProcess.isFinished()) {
                    freeFramesToAllocate += currentProcess.getAllocatedPhysicalMemoryCapacity();
                    finishedProcesses[currentProcess.getIndex()] = processes.remove(randomProcessIndex);
                    tryAllocatingFramesToPausedProcesses();
                    continue;
                }

                if (currentProcess.getCurrentPageIndex() % PAGE_FAULTS_CHECK_FREQUENCY == 0) {
//                    showTest();
                    int recentPageFaults = currentProcess.numberOfRecentPageFaults(DELTA_T);
//                    System.out.print("   w procesie " + currentProcess.getIndex() + " w czasie " + currentProcess.getCurrentPageIndex() + " obecne bledy w ostatnich " + DELTA_T + ": " + recentPageFaults);
//                    System.out.print(" zapauzowane: " + Arrays.toString(pausedProcesses.toArray()));
//                    System.out.print(" pamiec wirt. procesu: " + currentProcess.getPageSetCount());

                    if (recentPageFaults < LOWER_LIMIT && currentProcess.getAllocatedPhysicalMemoryCapacity() > 1) {
                        currentProcess.freeUpOneFrame();
                        freeFramesToAllocate++;

                        tryAllocatingFramesToPausedProcesses();
                    } else if (recentPageFaults > UPPER_LIMIT) {
                        if (freeFramesToAllocate > 0) {
                            currentProcess.allocateOneMoreFrame();
                            freeFramesToAllocate--;
                        } else if (currentProcess.getAllocatedPhysicalMemoryCapacity() < totalPhysicalMemorySize){
                            freeFramesToAllocate += currentProcess.getAllocatedPhysicalMemoryCapacity();
                            currentProcess.pause();
                            pausedProcesses.add(currentProcess);

                            tryAllocatingFramesToPausedProcesses();
                        }
                    }
                }
            }
        }
    }

    private void tryAllocatingFramesToPausedProcesses() {
        for (Process process : pausedProcesses) {
            if (process.canBeUnpaused(freeFramesToAllocate)) {
                freeFramesToAllocate -= process.getFramesNeededToUnpause();
                pausedProcesses.remove(process);
                process.unpause();
                return;
            }
        }
    }

    public void showResults() {
        System.out.print("Sterowanie liczba bledow\nliczby bledow: ");
        for (Process process : finishedProcesses) {
            System.out.print(process.getPageFaultsCount() + ", ");
        }

        System.out.print("\nramki przydzielone na koncu: ");
        for (Process process : finishedProcesses) {
            System.out.print(process.getAllocatedPhysicalMemoryCapacity() + ", ");
        }

        System.out.println("lacznie bledow: " + globalPageFaults);
    }

    private void showTest() {
        System.out.print("\nramki przydzielone test ");
        for (Process process : processes) {
            System.out.print(process.getAllocatedPhysicalMemoryCapacity() + ", ");
        }
        System.out.print("   wolne ramki: " + freeFramesToAllocate);
    }


}

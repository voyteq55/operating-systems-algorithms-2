package Zadanie4;

import java.util.ArrayList;
import java.util.Random;

public class ProportionalAllocation {
    private ArrayList<Process> processes;
    private Process[] finishedProcesses;
    private final int totalPhysicalMemorySize;
    private int globalPageFaults;
    private static final int THRASHING_CHECK_FREQUENCY = 20;
    private static final int THRASHING_TRESHOLD = 12;

    public ProportionalAllocation(int totalPhysicalMemorySize, ArrayList<Process> processes) {
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
        this.processes = processes;
        this.globalPageFaults = 0;
        this.finishedProcesses = new Process[processes.size()];
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
                finishedProcesses[currentProcess.getIndex()] = processes.remove(randomProcessIndex);
            } else {
                if (currentProcess.isPageFaultNextPage()) {
                    globalPageFaults++;
                }
                if (currentProcess.getCurrentPageIndex() % THRASHING_CHECK_FREQUENCY == 0 && currentProcess.numberOfRecentPageFaults(THRASHING_CHECK_FREQUENCY) > THRASHING_TRESHOLD) {
                    currentProcess.addThrashingOccurence();
                }
            }
        }
    }

    public void showResults() {
        System.out.println("\nProporcjonalny przydzial");
        System.out.println("Laczna liczba bledow strony: " + globalPageFaults);
        System.out.print("Liczba bledow strony poszczegolnych procesow: ");
        for (Process process : finishedProcesses) {
            System.out.print(process.getPageFaultsCount() + ", ");
        }

        System.out.print("\nRamki przydzielone procesom: ");
        for (Process process : finishedProcesses) {
            System.out.print(process.getAllocatedPhysicalMemoryCapacity() + ", ");
        }
        System.out.print("\nWystapienia szamotania procesow: ");
        for (Process process : finishedProcesses) {
            System.out.print(process.getThrashingOccurences() + ", ");
        }
        System.out.println();
    }
}

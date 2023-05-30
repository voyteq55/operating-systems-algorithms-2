package Zadanie4;

import java.util.ArrayList;
import java.util.Random;

public class ProportionalAllocation {
    private ArrayList<Process> processes;
    private Process[] finishedProcesses;
    private final int totalPhysicalMemorySize;
    private int globalPageFaults;
    private int freeFramesToAllocate;

    public ProportionalAllocation(int totalPhysicalMemorySize, ArrayList<Process> processes) {
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
        this.processes = processes;
        this.globalPageFaults = 0;
        this.freeFramesToAllocate = 0;
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
            } else if (currentProcess.isPageFaultNextPage()) {
                globalPageFaults++;
            }
        }
    }

    public void showResults() {
        System.out.print("Proporcjonalnie\nliczby bledow: ");
        for (Process process : finishedProcesses) {
            System.out.print(process.getPageFaultsCount() + ", ");
        }

        System.out.print("\nramki przydzielone: ");
        for (Process process : finishedProcesses) {
            System.out.print(process.getAllocatedPhysicalMemoryCapacity() + ", ");
        }

        System.out.println("lacznie bledow: " + globalPageFaults);
    }
}

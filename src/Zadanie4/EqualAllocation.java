package Zadanie4;

import java.util.ArrayList;
import java.util.Random;

public class EqualAllocation {
    private ArrayList<Process> processes;
    private Process[] finishedProcesses;
    private final int totalPhysicalMemorySize;
    private int globalPageFaults;
    private int freeFramesToAllocate;

    public EqualAllocation(int totalPhysicalMemorySize, ArrayList<Process> processes) {
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
        this.processes = processes;
        this.globalPageFaults = 0;
        this.freeFramesToAllocate = 0;
        this.finishedProcesses = new Process[processes.size()];
        allocateFrames();
    }

    private void allocateFrames() {
        int framesToAllocateLeft = totalPhysicalMemorySize;
        int equalFrameCount = totalPhysicalMemorySize / processes.size();
        for (Process process : processes) {
            process.setPhysicalMemoryCapacity(equalFrameCount);
            framesToAllocateLeft -= equalFrameCount;
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
        System.out.print("Rowno\nliczby bledow: ");
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
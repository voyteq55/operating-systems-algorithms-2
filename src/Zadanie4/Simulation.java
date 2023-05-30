package Zadanie4;

import java.util.ArrayList;
import java.util.Arrays;

public class Simulation {
    private static final int TOTAL_PHYSICAL_MEMORY_CAPACITY = 25;
    private static final int TOTAL_VIRTUAL_MEMORY_CAPACITY = 100;
    private static final int REFERENCE_COUNT = 10000;
    private static final int NUMBER_OF_PROCESSES = 5;
    public static void start() {
        ArrayList<Process> processes = new ArrayList<>();
        int[] capacities = Generator.generateVirtualMemoryCapacities(NUMBER_OF_PROCESSES, TOTAL_VIRTUAL_MEMORY_CAPACITY);
        for (int i = 0; i < NUMBER_OF_PROCESSES; i++) {
            int processVirtualMemoryCapacity = capacities[i];
            processes.add(new Process(i, processVirtualMemoryCapacity, Generator.generatePageReferences(REFERENCE_COUNT, processVirtualMemoryCapacity)));
        }

        System.out.println(Arrays.toString(capacities));

        EqualAllocation allocation = new EqualAllocation(TOTAL_PHYSICAL_MEMORY_CAPACITY, Generator.deepCopyProcesses(processes));
        allocation.start();
        allocation.showResults();

        ProportionalAllocation proportionalAllocation = new ProportionalAllocation(TOTAL_PHYSICAL_MEMORY_CAPACITY, Generator.deepCopyProcesses(processes));
        proportionalAllocation.start();
        proportionalAllocation.showResults();

    }

}

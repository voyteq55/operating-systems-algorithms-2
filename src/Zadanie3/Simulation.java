package Zadanie3;

import java.util.ArrayList;

public class Simulation {
    private static final int PHYSICAL_MEMORY_CAPACITY = 25;
    private static final int VIRTUAL_MEMORY_CAPACITY = 100;
    private static final int REFERENCE_COUNT = 10000;
    public static void start() {
        ArrayList<Page> pagesReferences = Generator.generatePageReferences(REFERENCE_COUNT, VIRTUAL_MEMORY_CAPACITY);

        FIFOAlgorithm fifoAlgorithm = new FIFOAlgorithm(PHYSICAL_MEMORY_CAPACITY, Generator.deepCopy(pagesReferences));
        int pageFaultsFIFO = fifoAlgorithm.countPageFaults();
        System.out.println("fifo: " + pageFaultsFIFO);

        RandomAlgorithm randomAlgorithm = new RandomAlgorithm(PHYSICAL_MEMORY_CAPACITY, Generator.deepCopy(pagesReferences));
        int pageFaults = randomAlgorithm.countPageFaults();
        System.out.println("random: " + pageFaults);

    }

}
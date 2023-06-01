package Zadanie3;

import java.util.ArrayList;

public class Simulation {
    private static final int[] PHYSICAL_MEMORY_CAPACITIES = {15, 20, 25, 30};
    private static final int VIRTUAL_MEMORY_CAPACITY = 100;
    private static final int REFERENCE_COUNT = 10000;
    public static void start() {
        ArrayList<Page> pagesReferences = Generator.generatePageReferences(REFERENCE_COUNT, VIRTUAL_MEMORY_CAPACITY);

        for (int physicalMemoryCapacity : PHYSICAL_MEMORY_CAPACITIES) {
            simulateForPhysicalMemorySize(physicalMemoryCapacity, pagesReferences);
        }

    }

    private static void simulateForPhysicalMemorySize(int physicalMemoryCapacity, ArrayList<Page> pagesReferences) {
        System.out.println("\nLiczba bledow stron dla " + REFERENCE_COUNT + " odwolan, " + VIRTUAL_MEMORY_CAPACITY +
                " stron pamieci oraz " + physicalMemoryCapacity + " ramek w pamieci fizycznej");
        FIFOAlgorithm fifoAlgorithm = new FIFOAlgorithm(physicalMemoryCapacity, Generator.deepCopy(pagesReferences));
        int pageFaultsFIFO = fifoAlgorithm.countPageFaults();
        System.out.println("Algorytm FIFO: " + pageFaultsFIFO);

        RandomAlgorithm randomAlgorithm = new RandomAlgorithm(physicalMemoryCapacity, Generator.deepCopy(pagesReferences));
        int pageFaultsRandom = randomAlgorithm.countPageFaults();
        System.out.println("Algorytm losowy: " + pageFaultsRandom);

        OptimalAlgorithm optimalAlgorithm = new OptimalAlgorithm(physicalMemoryCapacity, Generator.deepCopy(pagesReferences));
        int pageFaultsOptimal = optimalAlgorithm.countPageFaults();
        System.out.println("Algorytm optymalny: " + pageFaultsOptimal);

        LRUAlgorithm lruAlgorithm = new LRUAlgorithm(physicalMemoryCapacity, Generator.deepCopy(pagesReferences));
        int pageFaultsLRU = lruAlgorithm.countPageFaults();
        System.out.println("Algorytm LRU: " + pageFaultsLRU);

        LRUAproximateAlgorithm lruAproximateAlgorithm = new LRUAproximateAlgorithm(physicalMemoryCapacity, Generator.deepCopy(pagesReferences));
        int pageFaultsLRUAproximate = lruAproximateAlgorithm.countPageFaults();
        System.out.println("Aproksymowany algorytm LRU: " + pageFaultsLRUAproximate);
    }

}

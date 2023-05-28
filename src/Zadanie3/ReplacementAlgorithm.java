package Zadanie3;

import java.util.ArrayList;

public abstract class ReplacementAlgorithm {
    ArrayList<Page> physicalMemory;
    int maxPhysicalMemoryCapacity;
    ArrayList<Page> pagesReferences;
    int pageFaultsCount;

    public ReplacementAlgorithm(int maxPhysicalMemoryCapacity, ArrayList<Page> pagesReferences) {
        this.physicalMemory = new ArrayList<>();
        this.maxPhysicalMemoryCapacity = maxPhysicalMemoryCapacity;
        this.pagesReferences = pagesReferences;
        this.pageFaultsCount = 0;
    }

    public int countPageFaults() {
        for (Page page : pagesReferences) {
            if (isPageFaultWhenReferencingPage(page)) {
                pageFaultsCount++;
            }
        }
        return pageFaultsCount;
    }

    abstract boolean isPageFaultWhenReferencingPage(Page pagetoReference);
}

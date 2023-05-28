package Zadanie3;

import java.util.ArrayList;

public class FIFOAlgorithm extends ReplacementAlgorithm {
    private int indexOfPageToRemoveNext;

    public FIFOAlgorithm(int maxPhysicalMemoryCapacity, ArrayList<Page> pagesReferences) {
        super(maxPhysicalMemoryCapacity, pagesReferences);
        this.indexOfPageToRemoveNext = 0;
    }

    @Override
    boolean isPageFaultWhenReferencingPage(Page pagetoReference) {
        for (Page pageInMemory : physicalMemory) {
            if (pageInMemory.getReference() == pagetoReference.getReference()) {
                return false;
            }
        }

        if (physicalMemory.size() < maxPhysicalMemoryCapacity) {
            physicalMemory.add(pagetoReference);
            return true;
        }

        physicalMemory.set(indexOfPageToRemoveNext, pagetoReference);
        indexOfPageToRemoveNext = (indexOfPageToRemoveNext + 1) % maxPhysicalMemoryCapacity;
        return true;
    }
}

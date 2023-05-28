package Zadanie3;

import java.util.ArrayList;

public class FIFOAlgorithm extends ReplacementAlgorithm {
    private int elementOfPageToRemoveNext;

    public FIFOAlgorithm(int maxPhysicalMemoryCapacity, ArrayList<Page> pagesReferences) {
        super(maxPhysicalMemoryCapacity, pagesReferences);
        this.elementOfPageToRemoveNext = 0;
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

        physicalMemory.set(elementOfPageToRemoveNext, pagetoReference);
        elementOfPageToRemoveNext = (elementOfPageToRemoveNext + 1) % maxPhysicalMemoryCapacity;
        return true;
    }
}

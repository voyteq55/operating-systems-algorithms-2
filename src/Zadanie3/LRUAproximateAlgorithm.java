package Zadanie3;

import java.util.ArrayList;

public class LRUAproximateAlgorithm extends ReplacementAlgorithm {
    private int indexOfPageToEvaluateNext;

    public LRUAproximateAlgorithm(int maxPhysicalMemoryCapacity, ArrayList<Page> pagesReferences) {
        super(maxPhysicalMemoryCapacity, pagesReferences);
        this.indexOfPageToEvaluateNext = 0;
    }

    @Override
    boolean isPageFaultWhenReferencingPage(Page pagetoReference) {
        for (Page pageInMemory : physicalMemory) {
            if (pageInMemory.getReference() == pagetoReference.getReference()) {
                pageInMemory.setSecondChanceBit(1);
                return false;
            }
        }

        if (physicalMemory.size() < maxPhysicalMemoryCapacity) {
            physicalMemory.add(pagetoReference);
            return true;
        }

        while (true) {
            Page firstPageInQueue = physicalMemory.get(indexOfPageToEvaluateNext);
            if (firstPageInQueue.getSecondChanceBit() == 1) {
                firstPageInQueue.setSecondChanceBit(0);
            } else {
                physicalMemory.set(indexOfPageToEvaluateNext, pagetoReference);
                indexOfPageToEvaluateNext = (indexOfPageToEvaluateNext + 1) % maxPhysicalMemoryCapacity;
                return true;
            }
            indexOfPageToEvaluateNext = (indexOfPageToEvaluateNext + 1) % maxPhysicalMemoryCapacity;
        }
    }
}

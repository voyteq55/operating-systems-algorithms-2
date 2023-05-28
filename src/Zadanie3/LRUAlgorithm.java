package Zadanie3;

import java.util.ArrayList;

public class LRUAlgorithm extends ReplacementAlgorithm {

    public LRUAlgorithm(int maxPhysicalMemoryCapacity, ArrayList<Page> pagesReferences) {
        super(maxPhysicalMemoryCapacity, pagesReferences);
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

        int leastRecentlyUsedPageFrameIndex = -1;
        int leastRecentlyUsedPageDistance = -1;

        for (int i = 0; i < physicalMemory.size(); i++) {
            int currentReferenceToCheckIndex = pagetoReference.getIndex();
            while (currentReferenceToCheckIndex >= 0) {
                if (physicalMemory.get(i).getReference() == pagesReferences.get(currentReferenceToCheckIndex).getReference()) {
                    break;
                }
                currentReferenceToCheckIndex--;
            }
            int currentDistance = pagetoReference.getIndex() - currentReferenceToCheckIndex;

            if (currentDistance > leastRecentlyUsedPageDistance) {
                leastRecentlyUsedPageDistance = currentDistance;
                leastRecentlyUsedPageFrameIndex = i;
            }
        }

        physicalMemory.set(leastRecentlyUsedPageFrameIndex, pagetoReference);
        return true;
    }
}

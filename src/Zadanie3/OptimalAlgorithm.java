package Zadanie3;

import java.util.ArrayList;

public class OptimalAlgorithm extends ReplacementAlgorithm {

    public OptimalAlgorithm(int maxPhysicalMemoryCapacity, ArrayList<Page> pagesReferences) {
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

        int longestUnusedPageFrameIndex = -1;
        int longestUnusedPageDistance = -1;

        for (int i = 0; i < physicalMemory.size(); i++) {
            int currentReferenceToCheckIndex = pagetoReference.getIndex();
            while (currentReferenceToCheckIndex < pagesReferences.size()) {
                if (physicalMemory.get(i).getReference() == pagesReferences.get(currentReferenceToCheckIndex).getReference()) {
                    break;
                }
                currentReferenceToCheckIndex++;
            }
            int currentDistance = currentReferenceToCheckIndex - pagetoReference.getIndex();

            if (currentDistance > longestUnusedPageDistance) {
                longestUnusedPageDistance = currentDistance;
                longestUnusedPageFrameIndex = i;
            }
        }

        physicalMemory.set(longestUnusedPageFrameIndex, pagetoReference);
        return true;
    }
}

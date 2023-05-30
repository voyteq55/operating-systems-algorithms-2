package Zadanie4;

import java.util.ArrayList;

public class Process {
    private final int id;
    private ArrayList<Page> physicalMemory;
    private int allocatedPhysicalMemoryCapacity;
    private final int pageSetCount;
    private ArrayList<Page> pagesReferences;
    private int pageFaultsCount;
    private boolean isPaused;
    private int currentPageIndex;



    public Process(int id, int pageSetCount, ArrayList<Page> pagesReferences) {
        this.id = id;
        this.pageSetCount = pageSetCount;
        this.physicalMemory = new ArrayList<>();
        this.allocatedPhysicalMemoryCapacity = 0;
        this.pagesReferences = pagesReferences;
        this.pageFaultsCount = 0;
        this.isPaused = false;
        this.currentPageIndex = 0;
    }

    public void setPhysicalMemoryCapacity(int capacity) {
        this.allocatedPhysicalMemoryCapacity = capacity;
    }

    public int getAllocatedPhysicalMemoryCapacity() {
        return allocatedPhysicalMemoryCapacity;
    }

//    public int countPageFaults() {
//        for (Page page : pagesReferences) {
//            if (isPageFaultWhenReferencingPage(page)) {
//                pageFaultsCount++;
//            }
//        }
//        return pageFaultsCount;
//    }

    public boolean isPageFaultNextPage() {
        boolean isPageFault = isPageFaultWhenReferencingPage(pagesReferences.get(currentPageIndex++));
        if (isPageFault) {
            pageFaultsCount++;
        }
        return isPageFault;
    }

    private boolean isPageFaultWhenReferencingPage(Page pagetoReference) {
        for (Page pageInMemory : physicalMemory) {
            if (pageInMemory.getReference() == pagetoReference.getReference()) {
                return false;
            }
        }

        if (physicalMemory.size() < allocatedPhysicalMemoryCapacity) {
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

    public Process deepCopy() {
        return new Process(id, pageSetCount, Generator.deepCopy(pagesReferences));
    }

    public boolean isFinished() {
        return this.currentPageIndex >= pagesReferences.size();
    }

    public int getIndex() {
        return id;
    }

    public int getPageSetCount() {
        return pageSetCount;
    }

    public int getPageFaultsCount() {
        return pageFaultsCount;
    }
}


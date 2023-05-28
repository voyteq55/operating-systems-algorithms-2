package Zadanie3;

import java.util.ArrayList;
import java.util.Random;

public class RandomAlgorithm extends ReplacementAlgorithm {
    private Random random;

    public RandomAlgorithm(int maxPhysicalMemoryCapacity, ArrayList<Page> pagesReferences) {
        super(maxPhysicalMemoryCapacity, pagesReferences);
        random = new Random();
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

        int randomIndex = random.nextInt(maxPhysicalMemoryCapacity);
        physicalMemory.set(randomIndex, pagetoReference);
        return true;
    }
}

package Zadanie3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Generator {
    private static final double STEPPING_INTO_LOCALITY_PROBABILITY = 0.1;
    private static final double STEPPING_OUT_OF_LOCALITY_PROBABILITY = 0.07;

    public static ArrayList<Page> generatePageReferences(int referenceCount, int virtualMemoryCapacity) {
        Random random = new Random();
        boolean isInLocality = false;
        int startNumber = 0;
        int endNumberExclusive = virtualMemoryCapacity;
        ArrayList<Page> pageReferences = new ArrayList<>();

        for (int i = 0; i < referenceCount; i++) {
            if (!isInLocality && random.nextDouble() < STEPPING_INTO_LOCALITY_PROBABILITY) {
                startNumber = random.nextInt(virtualMemoryCapacity - 1);
                endNumberExclusive = startNumber + random.nextInt(virtualMemoryCapacity - startNumber) + 1;
            } else if (random.nextDouble() < STEPPING_OUT_OF_LOCALITY_PROBABILITY) {
                startNumber = 0;
                endNumberExclusive = virtualMemoryCapacity;
            }
            int reference = startNumber + random.nextInt(endNumberExclusive - startNumber);
            pageReferences.add(new Page(i, reference));
        }

        return pageReferences;
    }

    public static ArrayList<Page> deepCopy(ArrayList<Page> pageReferences) {
        ArrayList<Page> copiedPageReferences = new ArrayList<>();
        for (Page page : pageReferences) {
            copiedPageReferences.add(page.copy());
        }
        return copiedPageReferences;
    }
}

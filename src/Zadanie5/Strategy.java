package Zadanie5;

import java.util.ArrayList;

public interface Strategy {
    void assignProcess(Process nextProcess, ArrayList<CPU> allCPUs, ResultCollector resultCollector);
}

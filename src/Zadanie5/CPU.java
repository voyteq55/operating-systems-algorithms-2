package Zadanie5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class CPU {
    private final int id;
    private final HashSet<Process> activeProcesses;
    private double load;

    public CPU(int id) {
        this.id = id;
        this.activeProcesses = new HashSet<>();
        this.load = 0.0;
    }

    public boolean isOverloaded() {
        return this.load >= Simulation.OVERLOAD_CONSTANT_P;
    }

    public boolean isRelaxed() {
        return this.load < Simulation.RELAX_CONSTANT_R;
    }

    public void assignToActiveProcesses(Process process) {
        activeProcesses.add(process);
        load += process.getLoad();
    }

    public void executeActiveProcesses() {
        for (Process process : activeProcesses) {
            process.execute();
            if (process.isDone()) {
                load -= process.getLoad();
            }
        }
        activeProcesses.removeIf(Process::isDone);

    }

    public boolean hasActiveProcesses() {
        return !activeProcesses.isEmpty();
    }

    public ArrayList<CPU> otherRandomCPUs(ArrayList<CPU> allCPUs) {
        ArrayList<CPU> otherCPUs = new ArrayList<>(allCPUs);
        otherCPUs.remove(this);
        Collections.shuffle(otherCPUs);
        return otherCPUs;
    }

    public double getLoad() {
        return load;
    }
}

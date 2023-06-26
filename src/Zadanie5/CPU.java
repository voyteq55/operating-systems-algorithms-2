package Zadanie5;

import java.util.*;

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

    public void reset() {
        this.activeProcesses.clear();
        this.load = 0.0;
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

    public void captureProcessesFrom(CPU otherCPU, ResultCollector resultCollector) {
        ArrayList<Process> leastOverloadingProcesses = new ArrayList<>(otherCPU.activeProcesses);
        leastOverloadingProcesses.sort((process1, process2) -> (int)(process1.getLoad() - process2.getLoad()));
        Iterator<Process> iterator = leastOverloadingProcesses.iterator();

        ArrayList<Process> processesToRemove = new ArrayList<>();
        resultCollector.addCaptureRequest();
        while (this.isRelaxed() && otherCPU.isOverloaded() && iterator.hasNext()) {
            resultCollector.addCapture();
            resultCollector.addCaptureRequest();
            Process processToCapture = iterator.next();
            this.assignToActiveProcesses(processToCapture);
            otherCPU.load -= processToCapture.getLoad();
            processesToRemove.add(processToCapture);
        }
        for (Process processToRemove : processesToRemove) {
            otherCPU.activeProcesses.remove(processToRemove);
        }

    }

    public double getLoad() {
        return load;
    }
}

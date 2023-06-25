package Zadanie5;

public class Process {
    private int id;
    private double load;
    private int arrivalTime;
    private int initialExecutionTime;
    private int executionTimeLeft;
    private CPU initialProcessor;
    private CPU currentProcessor;

    public Process(int id, double load, int arrivalTime, int initialExecutionTime, CPU initialProcessor) {
        this.id = id;
        this.load = load;
        this.arrivalTime = arrivalTime;
        this.initialExecutionTime = initialExecutionTime;
        this.executionTimeLeft = initialExecutionTime;
        this.initialProcessor = initialProcessor;
        this.currentProcessor = initialProcessor;
    }

    public void execute() {
        executionTimeLeft -= 1;
    }

    public boolean isDone() {
        return executionTimeLeft <= 0;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public double getLoad() {
        return load;
    }

    public CPU getInitialProcessor() {
        return initialProcessor;
    }

}

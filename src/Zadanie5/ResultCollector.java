package Zadanie5;

import java.util.ArrayList;

public class ResultCollector {
    private ArrayList<CPU> allCPUs;
    private ArrayList<ArrayList<Double>> loads;
    private int migrationsCount;
    private int migrationRequestsCount;
    private int capturesCount;
    private int captureRequestsCount;

    public ResultCollector(ArrayList<CPU> allCPUs) {
        this.allCPUs = allCPUs;
        this.loads = new ArrayList<>(allCPUs.size());
        for (int i = 0; i < allCPUs.size(); i++) {
            loads.add(new ArrayList<>());
        }
        this.migrationsCount = 0;
        this.migrationRequestsCount = 0;
        this.capturesCount = 0;
        this.captureRequestsCount = 0;
    }

    public void saveLoadsOnTick() {
        for (int i = 0; i < allCPUs.size(); i++) {
            loads.get(i).add(allCPUs.get(i).getLoad());
        }
    }

    public void addMigration() {
        migrationsCount++;
    }

    public void addMigrationRequest() {
        migrationRequestsCount++;
    }

    public void addCapture() {
        capturesCount++;
    }

    public void addCaptureRequest() {
        captureRequestsCount++;
    }

    public void printResults() {
        System.out.println();
        double totalAverage = 0.0;
        double totalStdDeviation = 0.0;
        for (ArrayList<Double> cpuStats : loads) {
            double average = cpuStats.stream().mapToDouble(d -> d).average().orElse(0.0);
            totalAverage += average;
            System.out.print("srednia: " + average);
            double stdDeviation = 0.0;
            for (double load : cpuStats) {
                stdDeviation += Math.pow(load - average, 2.0);
            }
            stdDeviation = stdDeviation / cpuStats.size();
            stdDeviation = Math.sqrt(stdDeviation);
            totalStdDeviation += Math.pow(stdDeviation, 2.0);
            System.out.print(", odch. st.: " + stdDeviation);
            System.out.println();
        }

        totalAverage = totalAverage / allCPUs.size();
        totalStdDeviation = Math.sqrt(totalStdDeviation / allCPUs.size());

        System.out.println("w sumie srednia: " + totalAverage);
        System.out.println("w sumie std deviation: " + totalStdDeviation);

        System.out.println("migrations: " + migrationsCount);
        System.out.println("migration requests: " + migrationRequestsCount);

        if (capturesCount != 0 && captureRequestsCount != 0) {
            System.out.println("captures: " + capturesCount);
            System.out.println("capture requests: " + captureRequestsCount);
        }
    }



}

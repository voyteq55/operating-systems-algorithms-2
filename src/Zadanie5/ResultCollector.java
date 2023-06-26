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
        double totalAverage = 0.0;
        double totalStdDeviation = 0.0;
        ArrayList<Double> averages = new ArrayList<>();
        ArrayList<Double> stdDeviations = new ArrayList<>();

        for (ArrayList<Double> cpuStats : loads) {
            double average = cpuStats.stream().mapToDouble(d -> d).average().orElse(0.0);
            totalAverage += average;
            averages.add(average);
            double stdDeviation = 0.0;
            for (double load : cpuStats) {
                stdDeviation += Math.pow(load - average, 2.0);
            }
            stdDeviation = stdDeviation / cpuStats.size();
            stdDeviation = Math.sqrt(stdDeviation);
            totalStdDeviation += Math.pow(stdDeviation, 2.0);
            stdDeviations.add(stdDeviation);
        }

        totalAverage = totalAverage / allCPUs.size();
        totalStdDeviation = Math.sqrt(totalStdDeviation / allCPUs.size());

        System.out.printf("%-60s%.15f", "\nŚrednie obciążenie całego systemu: ", totalAverage);
        System.out.printf("%-60s%.15f", "\nOdchylenie standardowe obciążenia całego systemu: ", totalStdDeviation);

        System.out.printf("%-60s", "\nŚrednie obciążenie dla każdego procesora:");
        for (double avg : averages) {
            System.out.printf("%10.5f", avg);
        }
        System.out.printf("%-60s", "\nOdchylenie standardowe obciążenia dla każdego procesora:");
        for (double stdD : stdDeviations) {
            System.out.printf("%10.5f", stdD);
        }

        System.out.println("\nMigracje procesów: " + migrationsCount);
        System.out.println("Zapytania o obciążenie: " + migrationRequestsCount);

        if (capturesCount != 0 && captureRequestsCount != 0) {
            System.out.println("Przejęcia procesów: " + capturesCount);
            System.out.println("Zapytania o obciążenie (przejęcia): " + captureRequestsCount);
        }
        System.out.println();
    }

}

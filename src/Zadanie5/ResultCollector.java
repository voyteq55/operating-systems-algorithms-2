package Zadanie5;

import java.util.ArrayList;

public class ResultCollector {
    private ArrayList<CPU> allCPUs;
    private ArrayList<ArrayList<Double>> loads;
    private int migrationsCount;
    private int migrationRequestsCount;

    public ResultCollector(ArrayList<CPU> allCPUs) {
        this.allCPUs = allCPUs;
        this.loads = new ArrayList<>(allCPUs.size());
        for (int i = 0; i < allCPUs.size(); i++) {
            loads.add(new ArrayList<>());
        }
        this.migrationsCount = 0;
        this.migrationRequestsCount = 0;
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

    public void printResults() {
        for (ArrayList<Double> cpuStats : loads) {
            System.out.print("srednia: " + cpuStats.stream().mapToDouble(d -> d).average().orElse(0.0));
            System.out.print(" [");
            for (int i = 1; i < 5000; i++) {
                if (!cpuStats.get(i).equals(cpuStats.get(i-1))) {
                    System.out.print(i + ": " + cpuStats.get(i) + ", ");
                }
            }
            System.out.print("]\n");
        }

        System.out.println("migrations: " + migrationsCount);
        System.out.println("migration requests: " + migrationRequestsCount);
    }



}

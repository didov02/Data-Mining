import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static GeneticKnapsack geneticKnapsack;

    private static void findSolution() {
        List<int[]> population = geneticKnapsack.initializePopulation();
        int bestFitness = 0;

        for (int generation = 0; generation <= geneticKnapsack.getConstants().MAX_GENERATIONS; generation++) {
            int[] bestIndividual = geneticKnapsack.getBestIndividual(population);
            bestFitness = geneticKnapsack.calculateFitness(bestIndividual);

            if(generation % 5 == 0) {
                System.out.println(bestFitness);
            }

            if (generation == geneticKnapsack.getConstants().MAX_GENERATIONS) {
                break;
            }

            List<int[]> newPopulation = new ArrayList<>();

            while (newPopulation.size() < geneticKnapsack.getConstants().POPULATION_SIZE) {
                int[] parent1 = geneticKnapsack.tournamentSelection(population);
                int[] parent2 = geneticKnapsack.tournamentSelection(population);

                if (geneticKnapsack.getRandom().nextDouble() < geneticKnapsack.getConstants().CROSSOVER_RATE) {
                    int[][] offspring = geneticKnapsack.crossover(parent1, parent2);
                    newPopulation.add(offspring[0]);
                    newPopulation.add(offspring[1]);
                } else {
                    newPopulation.add(parent1.clone());
                    newPopulation.add(parent2.clone());
                }
            }

            for (int[] individual : newPopulation) {
                geneticKnapsack.mutate(individual);
            }

            population = newPopulation;
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int capacity = scanner.nextInt();
        int numItems = scanner.nextInt();

        int[] weights = new int[numItems];
        int[] values = new int[numItems];

        for (int i = 0; i < numItems; i++) {
            weights[i] = scanner.nextInt();
            values[i] = scanner.nextInt();
        }

        geneticKnapsack = new GeneticKnapsack(capacity, numItems, weights, values);

        findSolution();

    }
}

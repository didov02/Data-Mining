import java.util.*;

public class GeneticKnapsack {
    private static int capacity;
    private static int numItems;
    private static int[] weights;
    private static int[] values;
    private static final int POPULATION_SIZE = 100;
    private static final int MAX_GENERATIONS = 100;
    private static final double MUTATION_RATE = 0.05;
    private static final double CROSSOVER_RATE = 0.8;
    private static final Random random = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        capacity = scanner.nextInt();
        numItems = scanner.nextInt();

        weights = new int[numItems];
        values = new int[numItems];

        for (int i = 0; i < numItems; i++) {
            weights[i] = scanner.nextInt();
            values[i] = scanner.nextInt();
        }

        List<int[]> population = initializePopulation();
        int bestFitness = 0;

        for (int generation = 0; generation <= MAX_GENERATIONS; generation++) {
            int[] bestIndividual = getBestIndividual(population);
            bestFitness = calculateFitness(bestIndividual);

            if(generation % 5 == 0) {
                System.out.println(bestFitness);
            }

            if (generation == MAX_GENERATIONS) {
                break;
            }

            List<int[]> newPopulation = new ArrayList<>();

            while (newPopulation.size() < POPULATION_SIZE) {
                int[] parent1 = tournamentSelection(population);
                int[] parent2 = tournamentSelection(population);

                if (random.nextDouble() < CROSSOVER_RATE) {
                    int[][] offspring = crossover(parent1, parent2);
                    newPopulation.add(offspring[0]);
                    newPopulation.add(offspring[1]);
                } else {
                    newPopulation.add(parent1.clone());
                    newPopulation.add(parent2.clone());
                }
            }

            for (int[] individual : newPopulation) {
                mutate(individual);
            }

            population = newPopulation;
        }
    }

    private static List<int[]> initializePopulation() {
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int[] individual = new int[numItems];
            for (int j = 0; j < numItems; j++) {
                individual[j] = random.nextBoolean() ? 1 : 0;
            }
            population.add(individual);
        }
        return population;
    }

    private static int calculateFitness(int[] individual) {
        int totalWeight = 0, totalValue = 0;
        for (int i = 0; i < numItems; i++) {
            if (individual[i] == 1) {
                totalWeight += weights[i];
                totalValue += values[i];
            }
        }

        if(totalWeight > capacity) {
            repair(individual, totalWeight, totalValue);
        }

        return totalValue;
    }

    private static void repair(int[] individual, int weight, int value) {
        PriorityQueue<RatioIndex> valueToWeightRatio = new PriorityQueue<>(Comparator.comparingDouble(o -> o.ratio));

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                double ratio = (double) values[i] / weights[i];
                valueToWeightRatio.add(new RatioIndex(i, ratio));
            }
        }

        while (weight > capacity && !valueToWeightRatio.isEmpty()) {
            RatioIndex toRemove = valueToWeightRatio.poll();
            individual[toRemove.index] = 0;
            weight -= weights[toRemove.index];
            value -= values[toRemove.index];
        }
    }

    private static int[] getBestIndividual(List<int[]> population) {
        return population.stream()
                .max(Comparator.comparingInt(GeneticKnapsack::calculateFitness))
                .orElseThrow();
    }

    private static int[] tournamentSelection(List<int[]> population) {
        int[] candidate1 = population.get(random.nextInt(POPULATION_SIZE));
        int[] candidate2 = population.get(random.nextInt(POPULATION_SIZE));
        int[] candidate3 = population.get(random.nextInt(POPULATION_SIZE));
        int[] candidate4 = population.get(random.nextInt(POPULATION_SIZE));
        int[] miniTournamentCandidate1 = calculateFitness(candidate1) > calculateFitness(candidate2) ? candidate1 : candidate2;
        int[] miniTournamentCandidate2 = calculateFitness(candidate3) > calculateFitness(candidate4) ? candidate3 : candidate4;
        return calculateFitness(miniTournamentCandidate1) > calculateFitness(miniTournamentCandidate2) ? miniTournamentCandidate1 : miniTournamentCandidate2;
    }

    private static int[][] crossover(int[] parent1, int[] parent2) {
        int crossoverPoint = random.nextInt(numItems);
        int[] offspring1 = new int[numItems];
        int[] offspring2 = new int[numItems];

        for (int i = 0; i < numItems; i++) {
            if (i < crossoverPoint) {
                offspring1[i] = parent1[i];
                offspring2[i] = parent2[i];
            } else {
                offspring1[i] = parent2[i];
                offspring2[i] = parent1[i];
            }
        }

        checkOffspring(offspring1);
        checkOffspring(offspring2);

        return new int[][]{offspring1, offspring2};
    }

    private static void checkOffspring(int[] offspring) {
        int weight = 0;
        int value = 0;

        for (int i = 0; i < numItems; i++) {
            if (offspring[i] == 1) {
                weight += weights[i];
                value += values[i];
            }
        }

        if(weight > capacity) {
            repair(offspring, weight, value);
        }
    }

    // Mutation
    private static void mutate(int[] individual) {
        for (int i = 0; i < numItems; i++) {
            if (random.nextDouble() < MUTATION_RATE) {
                individual[i] = 1 - individual[i]; // Flip the bit
            }
        }
    }
}

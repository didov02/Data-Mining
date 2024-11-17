import java.util.*;

public class GeneticKnapsack {
    private int capacity;
    private int numItems;
    private int[] weights;
    private int[] values;
    private final Random random = new Random();
    private final Constants constants = new Constants();

    public GeneticKnapsack(int capacity, int numItems, int[] weights, int[] values) {
        this.capacity = capacity;
        this.numItems = numItems;
        this.weights = weights;
        this.values = values;
    }

    public List<int[]> initializePopulation() {
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < constants.POPULATION_SIZE; i++) {
            int[] individual = new int[this.numItems];
            for (int j = 0; j < this.numItems; j++) {
                individual[j] = this.random.nextBoolean() ? 1 : 0;
            }
            population.add(individual);
        }
        return population;
    }

    public int calculateFitness(int[] individual) {
        int totalWeight = 0;
        int totalValue = 0;
        for (int i = 0; i < this.numItems; i++) {
            if (individual[i] == 1) {
                totalWeight += this.weights[i];
                totalValue += this.values[i];
            }
        }

        if(totalWeight > this.capacity) {
            repair(individual, totalWeight, totalValue);
        }

        return totalValue;
    }

    public void repair(int[] individual, int weight, int value) {
        PriorityQueue<RatioIndex> valueToWeightRatio = new PriorityQueue<>(Comparator.comparingDouble(o -> o.ratio));

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                double ratio = (double) this.values[i] / this.weights[i];
                valueToWeightRatio.add(new RatioIndex(i, ratio));
            }
        }

        while (weight > this.capacity && !valueToWeightRatio.isEmpty()) {
            RatioIndex toRemove = valueToWeightRatio.poll();
            individual[toRemove.index] = 0;
            weight -= this.weights[toRemove.index];
            value -= this.values[toRemove.index];
        }
    }

    public int[] getBestIndividual(List<int[]> population) {
        return population.stream()
                .max(Comparator.comparingInt(this::calculateFitness))
                .orElseThrow();
    }

    public int[] tournamentSelection(List<int[]> population) {
        int[] candidate1 = population.get(random.nextInt(constants.POPULATION_SIZE));
        int[] candidate2 = population.get(random.nextInt(constants.POPULATION_SIZE));
        int[] candidate3 = population.get(random.nextInt(constants.POPULATION_SIZE));
        int[] candidate4 = population.get(random.nextInt(constants.POPULATION_SIZE));
        int[] miniTournamentCandidate1 = calculateFitness(candidate1) > calculateFitness(candidate2) ? candidate1 : candidate2;
        int[] miniTournamentCandidate2 = calculateFitness(candidate3) > calculateFitness(candidate4) ? candidate3 : candidate4;
        return calculateFitness(miniTournamentCandidate1) > calculateFitness(miniTournamentCandidate2) ? miniTournamentCandidate1 : miniTournamentCandidate2;
    }

    public int[][] crossover(int[] parent1, int[] parent2) {
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

    public void checkOffspring(int[] offspring) {
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

    public void mutate(int[] individual) {
        for (int i = 0; i < numItems; i++) {
            if (random.nextDouble() < constants.MUTATION_RATE) {
                individual[i] = 1 - individual[i];
            }
        }
    }

    public Constants getConstants() {
        return constants;
    }

    public Random getRandom() {
        return random;
    }

}

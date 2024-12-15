import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int crossValidation = 10;
    private static List<String> data = new ArrayList<>();

    private static int[][] republicans = new int[3][16];
    private static int[][] democrats = new int[3][16];
    private static int size;
    private static int startIndex;
    private static boolean solution;
    private static double republicansCount = 0.0;
    private static double democratsCount = 0.0;
    private static int[][] originalRepublicans = new int[3][16];
    private static int[][] originalDemocrats = new int[3][16];

    private static void update(String row, int toBeAdded) {
        boolean republican = row.charAt(0) == 'r';
        republicansCount += republican ? toBeAdded : 0;
        democratsCount += !republican ? toBeAdded : 0;

        for (int i = 1; i < row.length(); i++) {
            int tempIndex;
            char vote = row.charAt(i);
            if (vote == 'y') {
                tempIndex = 0;
            } else if (vote == 'n') {
                tempIndex = 1;
            } else if (!solution) {
                tempIndex = 2;
            } else {
                continue;
            }

            if (republican) {
                republicans[tempIndex][i - 1] += toBeAdded;
                originalRepublicans[tempIndex][i - 1] += toBeAdded;
            } else {
                democrats[tempIndex][i - 1] += toBeAdded;
                originalDemocrats[tempIndex][i - 1] += toBeAdded;
            }
        }
    }

    private static int predict() {
        int result = 0;
        for (int i = startIndex; i < startIndex + size; i++) {
            String currRow = data.get(i);
            boolean republican = currRow.charAt(0) == 'r';
            double probabilityRepublicans = 0.0;
            double probabilityDemocrats = 0.0;

            for (int j = 1; j < currRow.length(); j++) {
                int tempIndex;
                char vote = currRow.charAt(j);
                if (vote == 'y') {
                    tempIndex = 0;
                } else if (vote == '?' && !solution) {
                    tempIndex = 2;
                } else {
                    tempIndex = 1;
                }

                probabilityRepublicans += Math.log((republicans[tempIndex][j - 1] + 1.0) / (republicansCount + 2.0));
                probabilityDemocrats += Math.log((democrats[tempIndex][j - 1] + 1.0) / (democratsCount + 2.0));
            }

            probabilityRepublicans += Math.log((republicansCount + 1.0) / (republicansCount + democratsCount + 2.0));
            probabilityDemocrats += Math.log((democratsCount + 1.0) / (republicansCount + democratsCount + 2.0));

            if ((republican && probabilityRepublicans > probabilityDemocrats) || (!republican && probabilityDemocrats > probabilityRepublicans)) {
                result++;
            }
        }
        return result;
    }

    private static void readData() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\dianv\\Desktop\\GitHub\\Data-Mining\\HW5_6MI0600107\\src\\house-votes-84.data"));
        String row;
        while ((row = reader.readLine()) != null) {
            StringBuilder processedRow = new StringBuilder();
            if (row.charAt(0) == 'r') {
                processedRow.append('r');
                for (int i = 10; i < row.length(); i++) {
                    if (row.charAt(i) != ',') {
                        processedRow.append(row.charAt(i));
                    }
                }
            } else if (row.charAt(0) == 'd') {
                processedRow.append('d');
                for (int i = 8; i < row.length(); i++) {
                    if (row.charAt(i) != ',') {
                        processedRow.append(row.charAt(i));
                    }
                }
            }
            update(processedRow.toString(), 1);
            data.add(processedRow.toString());
        }
        reader.close();
    }

    private static void abstained() {
        for (int i = 0; i < data.size(); i++) {
            StringBuilder row = new StringBuilder(data.get(i));
            boolean republican = row.charAt(0) == 'r';

            for (int j = 1; j < row.length(); j++) {
                if (row.charAt(j) == '?') {
                    if (republican) {
                        row.setCharAt(j, originalRepublicans[0][j - 1] >= originalRepublicans[1][j - 1] ? 'y' : 'n');
                        republicans[row.charAt(j) == 'y' ? 0 : 1][j - 1]++;
                    } else {
                        row.setCharAt(j, originalDemocrats[0][j - 1] >= originalDemocrats[1][j - 1] ? 'y' : 'n');
                        democrats[row.charAt(j) == 'y' ? 0 : 1][j - 1]++;
                    }
                }
            }
            data.set(i, row.toString());
        }
    }

    public static void main(String[] args) throws IOException{
        readData();

        Scanner scanner = new Scanner(System.in);
        solution = (scanner.nextInt() == 1);

        if(solution) {
            abstained();
        }

        size = data.size() / 10;
        double sumAccuracy = 0.0;
        List<Double> accuracies = new ArrayList<>();

        Collections.shuffle(data);

        for (int i = 0; i < crossValidation; i++) {
            startIndex = i * size;

            for (int j = startIndex; j < startIndex + size; j++) {
                if (startIndex != 0) {
                    update(data.get(j - size), 1);
                }
                update(data.get(j), -1);
            }

            int correct = predict();
            double curAccuracy = (double) correct / (double) size * 100.0;
            accuracies.add(curAccuracy);
            System.out.println("Accuracy Fold " + (i + 1) + ": " + curAccuracy + "%");
            sumAccuracy += curAccuracy;
        }

        double avgAccuracy = sumAccuracy / crossValidation;

        double variance = 0.0;
        for (double acc : accuracies) {
            variance += Math.pow(acc - avgAccuracy, 2);
        }
        double stdDev = Math.sqrt(variance / crossValidation);

        System.out.println("Average Accuracy: " + avgAccuracy + "%");
        System.out.println("Standard Deviation: " + stdDev + "%");
    }
}
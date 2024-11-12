import java.util.*;

public class Main {
    public static int checkEmptyTileIndex(int empty, int tilesCount) {
        if(empty == -1) {
            return tilesCount - 1;
        }

        return empty;
    }

    public static List<Integer> createTiles(int tilesCount) {
        List<Integer> tempList = new ArrayList<>();

        for (int i = 0; i < tilesCount; i++) {
            tempList.add(i);
        }

        return tempList;
    }

    public static Board getFinalBoard(Board initialBoard) {
        int count = initialBoard.manhattan();

        while (true) {
            Board result = search(initialBoard, 0, count);

            if (result != null) {
                return result;
            } else {
                count++;
            }
        }
    }

    private static Board search(Board board, int cost, int manhattanCount) {
        int value = cost + board.manhattan();

        if (value > manhattanCount) {
            return null;
        }

        if (board.isGoal()) {
            return board;
        }

        Board solution = null;
        for (Board neighbor : board.neighbours()) {
            solution = search(neighbor, cost + 1, manhattanCount);
            if (solution != null) {
                return solution;
            }
        }

        return null;
    }

    public static int[][] fillMatrix(int numberOfTilesByRow, List<Integer> possibleTiles, Scanner scanner) {
        int[][] tempMatrix = new int[numberOfTilesByRow][numberOfTilesByRow];

        List<Integer> userList = new ArrayList<>();

        for(int i = 0; i < Math.pow(numberOfTilesByRow, 2); i++) {
            int num = scanner.nextInt();

            if(possibleTiles.contains(num)) {
                userList.add(num);
                possibleTiles.remove(Integer.valueOf(num));
            } else {
                throw new IllegalArgumentException("You doesn't have a permission to choose this number.");
            }

        }

        for(int i = 0; i < numberOfTilesByRow; i++) {
            for (int j = 0; j < numberOfTilesByRow; j++) {
                tempMatrix[i][j] = userList.remove(0);
            }
        }

        return tempMatrix;
    }

    public static void checkIfTimeIsWanted(String answer, double time) {
        if(answer.equalsIgnoreCase("y")) {
            System.out.println(String.format("Duration: %.2f", time));
        }
    }

    public static void createBoardAndFindSolution() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of tiles: ");
        int tiles = scanner.nextInt() + 1;

        while(Math.sqrt((double) tiles) != Math.floor(Math.sqrt((double) tiles))) {
            System.out.println("Change number of tiles");
            tiles = scanner.nextInt() + 1;
        }

        int numberOfTilesByRow = (int) Math.sqrt((double) tiles);

        System.out.print("Enter the wanted empty tile index: ");
        int emptyTileIndexWanted = scanner.nextInt();
        emptyTileIndexWanted = checkEmptyTileIndex(emptyTileIndexWanted, tiles);

        List<Integer> possibleTiles = createTiles(tiles);

        int[][] matrix = fillMatrix(numberOfTilesByRow, possibleTiles, scanner);

        Board startBoard = new Board(matrix, emptyTileIndexWanted, 0, new ArrayList<>());
        if(!startBoard.isSolvable()) {
            System.out.println("Board couldn't be solved.");
            return;
        }

        System.out.println(startBoard.toString());

        long start = System.nanoTime();
        Board finalBoard = getFinalBoard(startBoard);
        long end = System.nanoTime();

        System.out.println(finalBoard.getMovesCount() + "\n");

        for(String move : finalBoard.getMoves()) {
            System.out.println(move);
        }

        System.out.print("Do you want to see the time for the task to be solved? [Enter: Y/N]: ");
        String answer = scanner.next();
        System.out.println();

        double wholeTime = (end - start) / 1000000000.0;
        checkIfTimeIsWanted(answer, wholeTime);
    }

    public static void main(String[] args) {
        createBoardAndFindSolution();
    }
}
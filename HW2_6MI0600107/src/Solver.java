import java.util.*;

public class Solver {
    private int[] rows;

    private int[] queensPerColumn;

    private int[] d1;

    private int[] d2;

    private int size;

    private final int NO_CONFLICTS = 0;

    private int maxConflicts;

    private int minConflicts;

    private int currentQueenConflicts;

    private List<Integer> queens;

    private Random random;

    private int oldRow;

    public Solver(int size) {
        this.size = size;
        this.rows = new int[size];
        this.queensPerColumn = new int[size];
        this.d1 = new int[2 * size - 1];
        this.d2 = new int[2 * size - 1];
        this.maxConflicts = 0;
        this.minConflicts = size;
        this.queens = new ArrayList<>();
        this.random = new Random();
        this.currentQueenConflicts = 0;
        this.oldRow = -1;
    }

    public void initialize() {
        int index = 0;

        for (int i = 0; i < this.size; i++) {
            index = random.nextInt(this.size);
            this.rows[i] = index;
            queensPerColumn[index]++;
            d1[index - i + this.size - 1]++;
            d2[i + index]++;
        }
    }

    public int getConflictsCount(int row, int col) {
        return queensPerColumn[row] + d1[row - col + this.size - 1] + d2[col + row] - 3;
    }

    public int getColWithQueenWithMaxConf() {
        this.queens.clear();
        this.maxConflicts = 0;

        for (int i = 0; i < this.size; i++) {
            this.currentQueenConflicts = getConflictsCount(this.rows[i], i);

            if (this.currentQueenConflicts == this.maxConflicts) {
                this.queens.add(i);
            } else if (this.currentQueenConflicts > this.maxConflicts) {
                this.maxConflicts = this.currentQueenConflicts;
                this.queens.clear();
                this.queens.add(i);
            }
        }

        if (maxConflicts == this.NO_CONFLICTS) {
            return -1;
        }

        int selectedCol = this.queens.get(this.random.nextInt(this.queens.size()));
        this.oldRow = this.rows[selectedCol];

        return selectedCol;
    }

    public int getRowWithMinConflict(int col) {
        this.queens.clear();
        this.minConflicts = this.size;

        for (int i = 0; i < this.size; i++) {
            this.currentQueenConflicts = getConflictsCount(i, col);

            if (this.currentQueenConflicts == this.minConflicts) {
                this.queens.add(i);
            } else if (this.currentQueenConflicts < this.minConflicts) {
                this.minConflicts = this.currentQueenConflicts;
                this.queens.clear();
                this.queens.add(i);
            }
        }

        if(!this.queens.isEmpty()) {
            return this.queens.get(random.nextInt(this.queens.size()));
        }

        return 0;
    }

    public void solve() {
        int changesMade = 0;

        while(true) {
            int col = getColWithQueenWithMaxConf();

            if(col == -1) {
                break;
            }

            this.rows[col] = getRowWithMinConflict(col);
            fixDiagonals(this.oldRow, this.rows[col], col);

            changesMade++;
            if(changesMade == this.size * 2){
                initialize();
                changesMade = 0;
            }
        }
    }

    public void fixDiagonals(int oldRow, int newRow, int col) {
        queensPerColumn[oldRow]--;
        queensPerColumn[newRow]++;

        d1[oldRow - col + this.size - 1]--;
        d1[newRow - col + this.size - 1]++;

        d2[col + oldRow]--;
        d2[col + newRow]++;
    }

    public boolean isSolvable() {
        return this.size >= 4;
    }

    public void print() {
        for (int row = 0; row < this.size ; row++){
            for (int col = 0; col < this.size; col++){
                if(this.rows[col] == row){
                    System.out.print("* ");
                }
                else{
                    System.out.print('_' + " ");
                }
            }
            System.out.println();
        }
    }
}

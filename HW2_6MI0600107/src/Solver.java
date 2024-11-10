import java.util.*;

public class Solver {
    private int[] rows;

    private int size;

    private final int NO_CONFLICTS = 0;

    private int maxConflicts;

    private int minConflicts;

    private int currentQueenConflicts;

    private List<Integer> queens;

    private Random random;

    public Solver(int size) {
        this.size = size;
        this.rows = new int[size];
        this.maxConflicts = 0;
        this.minConflicts = size;
        this.queens = new ArrayList<>();
        this.random = new Random();
        this.currentQueenConflicts = 0;
    }

    public void initialize() {
        for (int i = 0; i < this.size; i++) {
            this.rows[i] = i;
        }

        int tempNum = 0;

        for (int i = 0; i < this.size; i++) {
            int index = random.nextInt(this.size);
            tempNum = this.rows[index];
            this.rows[index] = this.rows[i];
            this.rows[i] = tempNum;
        }
    }

    public int getConflictsCount(int row, int col) {
        int conflicts = 0;

        for (int otherCol = 0; otherCol < this.size; otherCol++) {
            if(otherCol == col) {
                continue;
            }

            int otherRow = this.rows[otherCol];

            if(Math.abs(otherRow - row) == Math.abs(otherCol - col) || row == otherRow) {
                conflicts++;
            }
        }

        return conflicts;
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

        return this.queens.get(this.random.nextInt(this.queens.size()));
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

            changesMade++;
            if(changesMade == this.size * 2){
                initialize();
                changesMade = 0;
            }
        }
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

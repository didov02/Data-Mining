import java.util.ArrayList;
import java.util.List;

public class Board {

    private int[][] tiles;
    private int size;
    
    private int lineSize;

    private int movesCount;

    private List<String> moves;

    private int emptyTileIndexWanted;
    
    private int currentEmptyTileIndex;

    private static class Pair {
        private int row;

        private int col;

        public Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }

    public Board(int[][] tiles, int emptyTileIndexWanted, int movesCount, List<String> movesMade) {
        this.tiles = tiles;
        this.size = this.tiles.length * this.tiles[0].length;
        this.lineSize = getLineSize();
        this.currentEmptyTileIndex = checkEmptyTileIndex();
        this.emptyTileIndexWanted = emptyTileIndexWanted;
        this.movesCount = movesCount;
        this.moves = movesMade;
    }
    
    private int checkEmptyTileIndex() {
        int index = 0;
        
        for (int i = 0; i < this.lineSize; i++) {
            for (int j = 0; j < this.lineSize; j++) {
                if(tileAt(i, j) == 0) {
                    return index;
                }
                
                index++;
            }
        }
        
        return -1;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < this.lineSize; i++) {
            for (int j = 0; j < this.lineSize; j++) {
                result.append(tileAt(i, j));
                result.append(" ");
            }

            result.append("\n");
        }

        return result.toString();
    }

    public int tileAt(int row, int col) {
        if (areValid(row, col)) {
            return this.tiles[row][col];
        } else {
            throw new IllegalArgumentException("One of row or col is not between 0 and " + (this.lineSize - 1));
        }
    }

    private boolean areValid(int row, int col) {
        return (row >= 0 && row <= this.lineSize - 1) && (col >= 0 && col <= this.lineSize -1);
    }

    public int manhattan() {
        int manhattanCount = 0;
        for (int i = 0; i < this.lineSize; i++) {
            for (int j = 0; j < this.lineSize; j++) {
                int tile = tileAt(i, j);
                if (tile != 0) {
                    int goalRow = (tile - 1) / lineSize;
                    int goalCol = (tile - 1) % lineSize;
                    manhattanCount += Math.abs(goalRow - i) + Math.abs(goalCol - j);
                }
            }
        }
        return manhattanCount;
    }

    private Pair getEmptyTileCoordinates() {
        int row = this.currentEmptyTileIndex / this.lineSize;
        int col = this.currentEmptyTileIndex % this.lineSize;

        return new Pair(row, col);
    }

    public boolean isGoal() {
        int currentIndex = 1;

        for (int i = 0; i < this.lineSize; i++) {
            for (int j = 0; j < this.lineSize; j++) {
                if(tileAt(i, j) == 0) {
                    if (currentIndex - 1 == this.emptyTileIndexWanted) {
                        continue;
                    } else {
                        return false;
                    }
                }

                if(tileAt(i, j) != currentIndex++) {
                    return false;
                }
            }
        }

        return true;
    }

    private int getLineSize() {
        return (int) Math.sqrt(this.size);
    }

    @Override
    public boolean equals(Object o) {
        Board compare = (Board) o;

        if(compare.getLineSize() != lineSize) {
            return false;
        }

        for(int i = 0; i < this.lineSize; i++) {
            for(int j = 0; j < this.lineSize; j++) {
                if(compare.tileAt(i, j) != tileAt(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    private int[][] copyTiles() {
        int[][] temp = new int[this.lineSize][];

        for (int i = 0; i < this.lineSize; i++) {
            temp[i] = this.tiles[i].clone();
        }

        return temp;
    }

    private int[][] makeSwitch(int newRow, int newCol) {
        int[][] newTiles = copyTiles();

        int oldRow = currentEmptyTileIndex / lineSize;
        int oldCol = currentEmptyTileIndex % lineSize;

        newTiles[oldRow][oldCol] = newTiles[newRow][newCol];
        newTiles[newRow][newCol] = 0;

        return newTiles;
    }

    public Iterable<Board> neighbours() {
        List<Board> boards = new ArrayList<>();
        Pair emptyTileCoordinates = getEmptyTileCoordinates();

        int[][] directions = {
                {0, -1},
                {0, 1},
                {-1, 0},
                {1, 0}
        };

        String[] moveNames = {"right", "left", "down", "up"};

        for (int i = 0; i < directions.length; i++) {
            int newRow = emptyTileCoordinates.getRow() + directions[i][0];
            int newCol = emptyTileCoordinates.getCol() + directions[i][1];

            if (areValid(newRow, newCol)) {
                List<String> newMoves = new ArrayList<>(this.moves);
                newMoves.add(moveNames[i]);

                boards.add(new Board(makeSwitch(newRow, newCol), this.emptyTileIndexWanted, this.movesCount + 1, newMoves));
            }
        }

        return boards;
    }

    public int[] getAllTiles() {
        int[] arrayTiles = new int[this.size];
        int counter = 0;

        for (int i = 0; i < this.lineSize; i++) {
            for (int j = 0; j < this.lineSize; j++) {
                if(tileAt(i, j) != 0) {
                    arrayTiles[counter++] = tileAt(i, j);
                }
            }
        }

        return arrayTiles;
    }

    public int calculateInversions() {
        int inversions = 0;
        int[] tilesArray = getAllTiles();

        for (int i = 0; i < this.size - 1; i++) {
            for (int j = i + 1; j < this.size - 1; j++) {
                if(tilesArray[i] > tilesArray[j]) {
                    inversions++;
                }
            }
        }

        return inversions;
    }


    public boolean isSolvable() {
        int inversions = calculateInversions();
        int row = this.currentEmptyTileIndex / this.lineSize;

        if(this.size % 2 == 0) {
            if(inversions + row != 0) {
                return (inversions + row) % 2 != 0;
            }

            return true;
        } else {
            return inversions % 2 == 0;
        }
    }

    public int getMovesCount() {
        return movesCount;
    }

    public List<String> getMoves() {
        return moves;
    }
}

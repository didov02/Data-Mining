import java.util.Random;

public class Board {

    private char[][] board;

    public static final char COMPUTER = 'O';
    public static final char PLAYER = 'X';

    private final int BOARD_LINE_SIZE = 3;

    public Board() {
        this.board = new char[BOARD_LINE_SIZE][BOARD_LINE_SIZE];
        for (int i = 0; i < BOARD_LINE_SIZE; i++) {
            for (int j = 0; j < BOARD_LINE_SIZE; j++) {
                this.board[i][j] = '_';
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < BOARD_LINE_SIZE; i++) {
            for (int j = 0; j < BOARD_LINE_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean anyMovesLeft() {
        for (int i = 0; i < BOARD_LINE_SIZE; i++) {
            for (int j = 0; j < BOARD_LINE_SIZE; j++) {
                if (board[i][j] == '_') {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkMove(int row, int col) {
        return (row >= 0 && row < BOARD_LINE_SIZE && col >= 0 && col < BOARD_LINE_SIZE && board[row][col] == '_');
    }

    public boolean makeMove(int row, int col, char player) {
        if (checkMove(row, col)) {
            board[row][col] = player;
            return true;
        }
        return false;
    }

    public char checkWinner() {
        for (int i = 0; i < BOARD_LINE_SIZE; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != '_') {
                return board[i][0];
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != '_') {
                return board[0][i];
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '_') {
            return board[0][0];
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != '_') {
            return board[0][2];
        }
        return '_';
    }

    public int minimax(char player, int alpha, int beta) {
        char winner = checkWinner();
        if (winner == PLAYER) {
            return 1;
        } else if (winner == COMPUTER) {
            return -1;
        } else if (!anyMovesLeft()) {
            return 0;
        }

        if (player == PLAYER) {
            return findMax(alpha, beta);
        } else {
            return findMin(alpha, beta);
        }
    }

    private int findMin(int alpha, int beta) {
        int bestScore = Integer.MAX_VALUE;
        for (int i = 0; i < BOARD_LINE_SIZE; i++) {
            for (int j = 0; j < BOARD_LINE_SIZE; j++) {
                if (board[i][j] == '_') {
                    board[i][j] = COMPUTER;
                    int score = minimax(PLAYER, alpha, beta);
                    board[i][j] = '_';
                    bestScore = Math.min(bestScore, score);
                    beta = Math.min(beta, bestScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }
        return bestScore;
    }

    private int findMax(int alpha, int beta) {
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < BOARD_LINE_SIZE; i++) {
            for (int j = 0; j < BOARD_LINE_SIZE; j++) {
                if (board[i][j] == '_') {
                    board[i][j] = PLAYER;
                    int score = minimax(COMPUTER, alpha, beta);
                    board[i][j] = '_';
                    bestScore = Math.max(bestScore, score);
                    alpha = Math.max(alpha, bestScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }
        return bestScore;
    }

    public void setFirstMoveByComputer() {
        Random random = new Random();
        int randomRow = random.nextInt(BOARD_LINE_SIZE);
        int randomCol = random.nextInt(BOARD_LINE_SIZE);
        this.board[randomRow][randomCol] = COMPUTER;
    }

    public int[] findBestMove() {
        int bestScore = Integer.MAX_VALUE;
        int[] move = new int[]{-1, -1};

        for (int i = 0; i < BOARD_LINE_SIZE; i++) {
            for (int j = 0; j < BOARD_LINE_SIZE; j++) {
                if (board[i][j] == '_') {
                    board[i][j] = COMPUTER;
                    int score = minimax(PLAYER, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    board[i][j] = '_';

                    if (score < bestScore) { // Minimize score for COMPUTER
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        return move;
    }

    public void printWinMessage(char winner) {
        String winnerMessage = (winner == PLAYER) ? "Player" : "Computer";
        System.out.println("Winner is: " + winnerMessage);
    }
}

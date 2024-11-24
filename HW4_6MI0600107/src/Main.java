import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board game = new Board();
        Scanner scanner = new Scanner(System.in);
        int row = -1;
        int col = -1;

        System.out.println("First player?\nEnter \"P\" for Person and \"C\" for Computer");
        String start = scanner.next();

        if(start.equalsIgnoreCase("c")) {
            game.setFirstMoveByComputer();
        }

        while (true) {
            game.printBoard();
            System.out.println("Enter row and column:");
            row = scanner.nextInt();
            col = scanner.nextInt();

            if (game.makeMove(row, col, Board.PLAYER)) {
                char winner = game.checkWinner();
                if (winner != '_') {
                    game.printBoard();
                    game.printWinMessage(winner);
                    break;
                } else if(!game.anyMovesLeft()) {
                    System.out.println("It's a draw!");
                    game.printBoard();
                    break;
                }

                int[] bestMove = game.findBestMove();
                game.makeMove(bestMove[0], bestMove[1], Board.COMPUTER);
                winner = game.checkWinner();
                if (winner != '_') {
                    game.printBoard();
                    game.printWinMessage(winner);
                    break;
                } else if(!game.anyMovesLeft()) {
                    System.out.println("It's a draw!");
                    game.printBoard();
                    break;
                }
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
        scanner.close();
    }
}

import java.util.Scanner;

public class Main {

    public static Scanner scanner;

    public static Solver solver;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        int size = scanner.nextInt();

        solver = new Solver(size);
        solver.initialize();

        if(solver.isSolvable()) {
            long start = System.nanoTime();
            solver.solve();
            long end = System.nanoTime();
            double wholeTime = (end - start) / 1000000000.0;

            if(size < 100) {
                solver.print();
            }

            System.out.println(wholeTime);
        } else {
            System.out.println("This board is not solvable!");
        }
    }
}
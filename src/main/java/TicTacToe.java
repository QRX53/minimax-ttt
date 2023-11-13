import java.util.Scanner;

public class TicTacToe {
    public static void main(String[] args) {

        int boardSize = 6;

        char[][] board = new char[boardSize][boardSize];

        // create the board with all empty spots. board size is determined by variable above
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = ' ';
            }
        }

        char humanPlayer = 'X';
        char computerPlayer = 'O';

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printBoard(board);

            // human player's move
            System.out.println("Enter your move (row [0-" + (boardSize - 1) + "] and column [0-" + (boardSize - 1) + "] separated by space): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            // check if the move is valid
            if (isValidMove(board, row, col)) {
                board[row][col] = humanPlayer;
            } else {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            // check for a win or a tie
            if (isGameOver(board)) {
                printBoard(board);
                System.out.println("Game over!");
                break;
            }

            // computer players move using mini max
            int[] bestMove = minimax(board, computerPlayer);
            board[bestMove[0]][bestMove[1]] = computerPlayer;

            // check for a win or a tie after the computer's move
            if (isGameOver(board)) {
                printBoard(board);
                System.out.println("Game over!");
                break;
            }
        }

        scanner.close();
    }

    // print the tic tac toe board
    private static void printBoard(char[][] board) {
        int boardSize = board.length;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(board[i][j]);
                if (j < boardSize - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (i < boardSize - 1) {
                for (int k = 0; k < boardSize - 1; k++) {
                    System.out.print("----");
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    // check if a move is valid
    private static boolean isValidMove(char[][] board, int row, int col) {
        int boardSize = board.length;
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize && board[row][col] == ' ';
    }

    // check if the game is over (win or tie)
    private static boolean isGameOver(char[][] board) {
        return hasPlayerWon(board, 'X') || hasPlayerWon(board, 'O') || isBoardFull(board);
    }

    // check if a player has won
    private static boolean hasPlayerWon(char[][] board, char contestant) {
        int boardSize = board.length;
        // check rows and columns
        for (int i = 0; i < boardSize; i++) {
            boolean rowWin = true;
            boolean colWin = true;
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != contestant) {
                    rowWin = false;
                }
                if (board[j][i] != contestant) {
                    colWin = false;
                }
            }
            if (rowWin || colWin) {
                return true;
            }
        }

        // check diagonals
        boolean diag1Win = true;
        boolean diag2Win = true;
        for (int i = 0; i < boardSize; i++) {
            if (board[i][i] != contestant) {
                diag1Win = false;
            }
            if (board[i][boardSize - 1 - i] != contestant) {
                diag2Win = false;
            }
        }
        return diag1Win || diag2Win;
    }

    // check if the board is full (tie)
    private static boolean isBoardFull(char[][] board) {
        int boardSize = board.length;
        for (char[] chars : board) {
            for (int j = 0; j < boardSize; j++) {
                if (chars[j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // implement algorithm
    private static int[] minimax(char[][] board, char player) {
        int boardSize = board.length;
        // check if game is over
        if (hasPlayerWon(board, 'X')) {
            return new int[]{-1, -1, -1}; // Human wins
        } else if (hasPlayerWon(board, 'O')) {
            return new int[]{-1, -1, 1}; // Computer wins
        } else if (isBoardFull(board)) {
            return new int[]{-1, -1, 0}; // It's a tie
        }

        // init var to store the best move and its corresponding score
        int[] bestMove = {-1, -1, (player == 'O') ? Integer.MIN_VALUE : Integer.MAX_VALUE};

        // loop through all empty cells on the board
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') {
                    // try the current empty cell
                    board[i][j] = player;

                    // recursively call minimax to simulate the game after this move
                    int[] currentMove = minimax(board, (player == 'X') ? 'O' : 'X');

                    // undo the test move
                    board[i][j] = ' ';

                    // update the best move and its score based on the current player
                    if ((player == 'O' && currentMove[2] > bestMove[2]) || (player == 'X' && currentMove[2] < bestMove[2])) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestMove[2] = currentMove[2];
                    }
                }
            }
        }

        return bestMove;
    }
}

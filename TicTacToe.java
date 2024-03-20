import java.util.Scanner;

public class TicTacToe {

    public static final char PLAYER_X = 'X';
    public static final char PLAYER_O = 'O';
    public static final char EMPTY = ' ';


    private static final int[][] WINNING_COMBOS = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    public static void main(String[] args) {
        char[][] board = {
                {EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY}
        };

        printBoard(board);
        Scanner scanner = new Scanner(System.in);

        while (!isGameOver(board)) {
            System.out.println("Enter your move (1-9): ");
            int move = scanner.nextInt();
            while (!isValidMove(board, move)) {
                System.out.println("Invalid move. Please enter a valid move (1-9): ");
                move = scanner.nextInt();
            }

            makeMove(board, move - 1, PLAYER_X);
            printBoard(board);

            if (isGameOver(board))
                break;

            System.out.println("Computer is making its move...");
            makeMove(board, getOptimalMove(board, PLAYER_O), PLAYER_O);
            printBoard(board);
        }

        char winner = getWinner(board);
        if (winner != EMPTY) {
            System.out.println("Player " + winner + " wins!");
        } else {
            System.out.println("It's a draw!");
        }
        scanner.close();
    }

    public static void printBoard(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static boolean isGameOver(char[][] board) {
        return getWinner(board) != EMPTY || isBoardFull(board);
    }

    public static boolean isBoardFull(char[][] board) {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == EMPTY)
                    return false;
            }
        }
        return true;
    }

    public static boolean isValidMove(char[][] board, int move) {
        return move >= 1 && move <= 9 && board[(move - 1) / 3][(move - 1) % 3] == EMPTY;
    }

    public static char getWinner(char[][] board) {
        for (int[] combo : WINNING_COMBOS) {
            char firstCell = board[combo[0] / 3][combo[0] % 3];
            if (firstCell != EMPTY &&
                    firstCell == board[combo[1] / 3][combo[1] % 3] &&
                    firstCell == board[combo[2] / 3][combo[2] % 3]) {
                return firstCell;
            }
        }
        return EMPTY;
    }

    public static void makeMove(char[][] board, int move, char player) {
        int row = move / 3;
        int col = move % 3;
        board[row][col] = player;
    }

    public static int getOptimalMove(char[][] board, char player) {
        int bestMove = -1;
        int bestScore = (player == PLAYER_X) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            if (board[i / 3][i % 3] == EMPTY) {
                board[i / 3][i % 3] = player;
                int score = minimax(board, 0, false, player == PLAYER_X ? PLAYER_O : PLAYER_X);
                board[i / 3][i % 3] = EMPTY;

                if ((player == PLAYER_X && score > bestScore) || (player == PLAYER_O && score < bestScore)) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        return bestMove;
    }

    public static int minimax(char[][] board, int depth, boolean isMaximizing, char currentPlayer) {
        char winner = getWinner(board);
        if (winner != EMPTY) {
            return (winner == PLAYER_X) ? 10 - depth : -10 + depth;
        }

        if (isBoardFull(board)) {
            return 0;
        }

        int bestScore = (isMaximizing) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            if (board[i / 3][i % 3] == EMPTY) {
                board[i / 3][i % 3] = currentPlayer;
                int score = minimax(board, depth + 1, !isMaximizing, (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X);
                board[i / 3][i % 3] = EMPTY;

                if (isMaximizing) {
                    bestScore = Math.max(score, bestScore);
                } else {
                    bestScore = Math.min(score, bestScore);
                }
            }
        }

        return bestScore;
    }
}

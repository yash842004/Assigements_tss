import java.util.Scanner;

public class Game {
	private Player player1;
	private Player player2;
	private Board board;

	public Game() {
		board = new Board();
		player1 = new Player("Player 1", MarkType.X);
		player2 = new Player("Player 2", MarkType.O);
	}

	public void playGame() {
		Scanner scanner = new Scanner(System.in);
		Player currentPlayer = player1;

		board.emptyBoard();
		boolean gameEnded = false;

		while (!gameEnded) {
			board.displayBoard();
			System.out.println(
					currentPlayer.getName() + " (" + currentPlayer.getMark() + ") turn. Enter row and column (0-2): ");
			int row = scanner.nextInt();
			int col = scanner.nextInt();

			if (!board.setMark(row, col, currentPlayer.getMark())) {
				System.out.println("Cell already occupied. Try again.");
				continue;
			}

			if (board.checkWinner(currentPlayer.getMark())) {
				board.displayBoard();
				System.out.println(currentPlayer.getName() + " wins!");
				gameEnded = true;
			} else if (board.checkDraw()) {
				board.displayBoard();
				System.out.println("It's a draw!");
				gameEnded = true;
			} else {
				currentPlayer = (currentPlayer == player1) ? player2 : player1;
			}
		}

		System.out.println("Game ended. Do you want to play again? (yes/no)");
		String again = scanner.next();
		if (again.equalsIgnoreCase("yes")) {
			playGame();
		}
	}
}

import java.util.Scanner;

public class PlayGame {

	public static void main(String[] args) {

		Sign choice[][] = new Sign[3][3];
		Scanner scanner = new Scanner(System.in);

		int moves = 0;
		while (moves < 9) {
			System.out.println("Enter row (0-2):");
			int position_1 = scanner.nextInt();
			System.out.println("Enter column (0-2):");
			int position_2 = scanner.nextInt();

			if (position_1 >= 0 && position_1 < 3 && position_2 >= 0 && position_2 < 3) {
				if (choice[position_1][position_2] == Sign.EMPTY) {
					choice[position_1][position_2] = Sign.O;
					moves++;
				} else {
					System.out.println("Cell already occupied. Try again.");
				}
			} else {
				System.out.println("Invalid position. Try again.");
			}
		}

	}
}

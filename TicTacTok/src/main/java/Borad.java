
public class Borad {

	Sign[][] sign = new Sign[3][3];

	public void plainBoard() {

		for (int i = 0; i < 3; i++) {

			for (int j = 0; j < 3; j++) {
				System.out.print("   ");
				if (j < 2) {
					System.out.print("|");
				}
			}
			System.out.println();

			if (i < 2) {
				System.out.println("---|---|---");
			}
		}
	}

	public boolean checkWinner() {

		for (int i = 0; i < 3; i++) {

			if ((sign[i][0] == Sign.O && sign[i][1] == Sign.O && sign[i][2] == Sign.O)) {
				System.out.println("Winner");
				break;
			}
			if ((sign[i][0] == Sign.X && sign[i][1] == Sign.X && sign[i][2] == Sign.X)) {
				System.out.println("Winner");
				break;
			}

		}

		for (int j = 0; j < 3; j++) {
			if ((sign[0][j] == Sign.O && sign[1][j] == Sign.O && sign[2][j] == Sign.O)) {
				System.out.println("Winner");
				break;
			}
			if ((sign[0][j] == Sign.X && sign[1][j] == Sign.X && sign[2][j] == Sign.X)) {
				System.out.println("Winner");
				break;
			}

		}

		for (int a = 0; a < 3; a++) { // first diagonal.
			for (int b = 0; b < 3; b++) {
				if ((sign[a][b] == Sign.X && sign[a][b] == Sign.X && sign[a][b] == Sign.X)) {
					System.out.println("Winner");
					break;
				}
			}
			a++;
		}
		for (int a = 3; a > 0; a++) { // second diagonal.
			for (int b = 0; b < 3; b++) {
				if ((sign[a][b] == Sign.X && sign[a][b] == Sign.X && sign[a][b] == Sign.X)) {
					System.out.println("Winner");
					break;
				}
			}
			a--;
		}
		return false;

	}

	public boolean checkDraw() {

		if (checkWinner() != true) {
			System.out.println("Draw");

			return true;
		}
		return false;
	}

	public boolean isBoardFull() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (sign[i][j] == Sign.EMPTY) {
					return false;
				}
			}
		}
		System.out.println("It is full:(");
		return true;
	}

	public void emptyBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				sign[i][j] = Sign.EMPTY;

			}
		}

	}

}

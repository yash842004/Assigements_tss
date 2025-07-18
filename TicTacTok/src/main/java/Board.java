public class Board {
	private MarkType[][] cells;

	public Board() {
		cells = new MarkType[3][3];
		emptyBoard();
	}

	public void emptyBoard() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				cells[i][j] = MarkType.EMPTY;
	}

	public boolean setMark(int row, int col, MarkType mark) {
		if (cells[row][col] == MarkType.EMPTY) {
			cells[row][col] = mark;
			return true;
		}
		return false;
	}

	public boolean checkWinner(MarkType mark) {
		for (int i = 0; i < 3; i++) {
			if (cells[i][0] == mark && cells[i][1] == mark && cells[i][2] == mark)
				return true;
			if (cells[0][i] == mark && cells[1][i] == mark && cells[2][i] == mark)
				return true;
		}

		if (cells[0][0] == mark && cells[1][1] == mark && cells[2][2] == mark)
			return true;
		if (cells[0][2] == mark && cells[1][1] == mark && cells[2][0] == mark)
			return true;

		return false;
	}

	public boolean checkDraw() {
		return isBoardFull() && !checkWinner(MarkType.X) && !checkWinner(MarkType.O);
	}

	public boolean isBoardFull() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (cells[i][j] == MarkType.EMPTY)
					return false;
		return true;
	}

	public void displayBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				switch (cells[i][j]) {
				case X:
					System.out.print(" X ");
					break;
				case O:
					System.out.print(" O ");
					break;
				case EMPTY:
					System.out.print(" - ");
					break;
				}
			}
			System.out.println();
		}
	}
}

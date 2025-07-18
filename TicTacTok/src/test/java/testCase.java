import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class testCase {

    private Board board;

    @BeforeEach
    void init() {
        board = new Board(); 
    }

    @Test
    public void testRowWin() {
        board.setMark(0, 0, MarkType.X);
        board.setMark(0, 1, MarkType.X);
        board.setMark(0, 2, MarkType.X);
        assertTrue(board.checkWinner(MarkType.X), "Row win not detected");
    }

    @Test
    public void testColumnWin() {
        board.setMark(0, 1, MarkType.O);
        board.setMark(1, 1, MarkType.O);
        board.setMark(2, 1, MarkType.O);
        assertTrue(board.checkWinner(MarkType.O), "Column win not detected");
    }

    @Test
    public void testDiagonalWin() {
        board.setMark(0, 0, MarkType.X);
        board.setMark(1, 1, MarkType.X);
        board.setMark(2, 2, MarkType.X);
        assertTrue(board.checkWinner(MarkType.X), "Diagonal win not detected");
    }

    @Test
    public void testAntiDiagonalWin() {
        board.setMark(0, 2, MarkType.O);
        board.setMark(1, 1, MarkType.O);
        board.setMark(2, 0, MarkType.O);
        assertTrue(board.checkWinner(MarkType.O), "Anti-diagonal win not detected");
    }

    @Test
    public void testNoWin() {
        board.setMark(0, 0, MarkType.X);
        board.setMark(0, 1, MarkType.O);
        board.setMark(0, 2, MarkType.X);
        assertFalse(board.checkWinner(MarkType.X), "False positive win detected");
    }
}

import junit.framework.TestCase;
import org.junit.Test;


public class BoardTest extends TestCase {
	Board b;
    private Piece pyr1, pyr2, pyr3, pyr4;
    private Piece stick1, stick2, stick3, stick4;
    private Piece l1, l2, l3, l4;
    private Piece square;
    private Piece s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.

	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	protected void setUp() throws Exception {
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

        stick1 = new Piece(Piece.STICK_STR);
        stick2 = stick1.computeNextRotation();
        stick3 = stick2.computeNextRotation();
        stick4 = stick3.computeNextRotation();

        l1 = new Piece(Piece.L1_STR);
        l2 = l1.computeNextRotation();
        l3 = l2.computeNextRotation();
        l4 = l3.computeNextRotation();

        square = new Piece(Piece.SQUARE_STR);

        s = new Piece(Piece.S1_STR);
        sRotated = s.computeNextRotation();
		

	}
	
	// Check the basic width/height/max after the one placement
	public void testSample1() {
        b = new Board(3, 6);
        b.place(pyr1, 0, 0);

		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	public void testSample2() {
        b = new Board(3, 6);
        b.place(pyr1, 0, 0);

		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}

	// Makre  more tests, by putting together longer series of
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.

    //Test stick piece
    public void testDifferentPiece1(){
        b = new Board(5, 5);
        b.place(stick1, 0, 0);

        b.commit();
        assertEquals(4, b.getColumnHeight(0));
        assertEquals(0, b.getColumnHeight(1));
        assertEquals(4, b.getMaxHeight());
    }
    //Test L
    public void testDifferentPiece2(){
        b = new Board(5, 5);
        b.place(l1, 0, 0);

        b.commit();
        assertEquals(3, b.getColumnHeight(0));
        assertEquals(1, b.getColumnHeight(1));
        assertEquals(3, b.getMaxHeight());
    }
    //Square
    public void testDifferentPiece3(){
        b = new Board(5, 5);
        b.place(square, 0, 0);

        b.commit();
        assertEquals(2, b.getColumnHeight(0));
        assertEquals(2, b.getColumnHeight(1));
        assertEquals(2, b.getMaxHeight());
    }
    //S
    public void testDifferentPiece4(){
        b = new Board(5, 5);
        b.place(s, 0, 0);

        b.commit();
        assertEquals(1, b.getColumnHeight(0));
        assertEquals(2, b.getColumnHeight(1));
        assertEquals(2, b.getColumnHeight(2));
        assertEquals(2, b.getMaxHeight());
    }
    //Place and undo testers
    public void testPlaceAndUndo(){
        b = new Board(10, 10);
        b.place(stick1, 0, 0);
        b.commit();
        b.place(pyr1, 1, 0);
        b.commit();
        b.place(square, 4, 0);

        assertEquals(4, b.getColumnHeight(0));
        assertEquals(1, b.getColumnHeight(1));
        assertEquals(2, b.getColumnHeight(2));
        assertEquals(1, b.getColumnHeight(3));
        assertEquals(2, b.getColumnHeight(4));
        assertEquals(2, b.getColumnHeight(5));
        assertEquals(4, b.getMaxHeight());

        b.undo();
        assertEquals(4, b.getColumnHeight(0));
        assertEquals(1, b.getColumnHeight(1));
        assertEquals(2, b.getColumnHeight(2));
        assertEquals(1, b.getColumnHeight(3));
        assertEquals(0, b.getColumnHeight(4));
        assertEquals(0, b.getColumnHeight(4));

    }
    //Clear row tester, whole board is cleared
    public void testClearRow1(){
        b = new Board(4, 10);
        b.place(stick1, 0, 0);
        b.commit();
        b.place(stick1, 1, 0);
        b.commit();
        b.place(stick1, 2, 0);
        b.commit();
        b.place(stick1, 3, 0);
        b.commit();
        int cleared = b.clearRows();
        assertEquals(4, cleared);
        assertEquals(0, b.getColumnHeight(0));
        assertEquals(0, b.getColumnHeight(1));
        assertEquals(0, b.getColumnHeight(2));
        assertEquals(0, b.getColumnHeight(3));


    }
    //Clear row tester, only a single row is cleared
    public void testClearRow2(){
        b = new Board(4, 10);
        b.place(l1, 0, 0);
        b.commit();
        b.place(new Piece(Piece.L2_STR), 2, 0);
        b.commit();
        b.clearRows();

        assertEquals(2, b.getColumnHeight(0));
        assertEquals(0, b.getColumnHeight(1));
        assertEquals(0, b.getColumnHeight(2));
        assertEquals(2, b.getColumnHeight(3));
        assertEquals(2, b.getMaxHeight());

        assertEquals("|    |\n" +
                "|    |\n" +
                "|    |\n" +
                "|    |\n" +
                "|    |\n" +
                "|    |\n" +
                "|    |\n" +
                "|    |\n" +
                "|+  +|\n" +
                "|+  +|\n" +
                "------", b.toString());


    }
    //Test dropHeight
    public void testDropHeight(){
        b = new Board(10, 20);
        b.place(stick1, 0, 0);
        b.commit();
        assertEquals(4, b.dropHeight(stick1, 0));

        b.place(pyr2, 1, 0);
        b.commit();
        assertEquals(3, b.dropHeight(pyr1, 2));

    }
    public void testPlaceOutOfBounds() {
        b = new Board(3, 6);
        int result = b.place(pyr1, 2, 0); // goes out right edge
        assertEquals(Board.PLACE_OUT_BOUNDS, result);
    }

    public void testPlaceCollision() {
        b = new Board(3, 6);
        b.place(pyr1, 0, 0);
        b.commit();

        int result = b.place(pyr1, 0, 0); // overlaps
        assertEquals(Board.PLACE_BAD, result);
    }
    public void testUndoAfterFailedPlace() {
        b = new Board(3, 6);
        b.place(pyr1, 0, 0);
        b.commit();

        b.place(pyr1, 0, 0); // fails
        b.undo();

        assertEquals(2, b.getMaxHeight()); // should stay same
    }
    public void testDoubleUndo() {
        b = new Board(3, 6);
        b.place(pyr1, 0, 0);
        b.commit();

        b.place(square, 0, 2);
        b.undo(); // valid undo
        b.undo(); // should do NOTHING

        assertEquals(2, b.getMaxHeight());
    }
    public void testPlaceWithoutCommit() {
        b = new Board(3, 6);
        b.place(pyr1, 0, 0);

        try {
            b.place(square, 0, 2);
            fail("Should have thrown exception");
        } catch (RuntimeException e) {
            // expected
        }
    }
    public void testClearRowsNone() {
        b = new Board(4, 10);
        b.place(pyr1, 0, 0);
        b.commit();

        int cleared = b.clearRows();
        assertEquals(0, cleared);
    }

    public void testDropHeightOnEmptyBoard() {
        b = new Board(5, 10);
        assertEquals(0, b.dropHeight(pyr1, 0));
    }

    public void testDropHeightNearWall() {
        b = new Board(5, 10);
        b.place(stick1, 0, 0);
        b.commit();

        int h = b.dropHeight(pyr1, 1);
        assertTrue(h >= 0); // just ensure no crash / bad calc
    }
    public void testGetGridOutOfBounds() {
        b = new Board(3, 6);
        assertTrue(b.getGrid(-1, 0));
        assertTrue(b.getGrid(0, -1));
        assertTrue(b.getGrid(3, 0));
        assertTrue(b.getGrid(0, 6));
    }
    public void testUndoRestoresWidthsAndHeights() {
        b = new Board(5, 10);
        b.place(square, 0, 0);
        b.commit();

        b.place(stick1, 2, 0);
        b.undo();

        assertEquals(2, b.getColumnHeight(0));
        assertEquals(2, b.getColumnHeight(1));
        assertEquals(0, b.getColumnHeight(2));
    }
}

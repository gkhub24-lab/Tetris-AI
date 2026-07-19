import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
    private Piece stick1, stick2, stick3, stick4;
    private Piece l1, l2, l3, l4;
    private Piece square;
    private Piece s, sRotated;



	protected void setUp() throws Exception {
		super.setUp();
		
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
	
	// Here are some sample tests to get you started
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());

        //Check size of stick piece
        assertEquals(1, stick1.getWidth());
        assertEquals(4, stick1.getHeight());

        //Check size of L piece
        assertEquals(2, l1.getWidth());
        assertEquals(3, l1.getHeight());

        //Check size of square
        assertEquals(2, square.getWidth());
        assertEquals(2, square.getHeight());

        //Check size of s
        assertEquals(3, s.getWidth());
        assertEquals(2, s.getHeight());

		// Now try after rotation, Let's rotate every piece
		// Effectively we're testing size and rotation code here

        //Pyramid
		assertEquals(2, pyr2.getWidth());
        assertEquals(3, pyr2.getHeight());

        //Stick
        assertEquals(4, stick2.getWidth());
        assertEquals(1, stick2.getHeight());

        //L
        assertEquals(3, l2.getWidth());
        assertEquals(2, l2.getHeight());

        //Square
        assertEquals(2, square.computeNextRotation().getWidth());
        assertEquals(2, square.computeNextRotation().getHeight());

        //S
        assertEquals(2, sRotated.getWidth());
        assertEquals(3, sRotated.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.

        //Pyr skirt
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));

        //Stick skirt
        assertTrue(Arrays.equals(new int[] {0}, stick1.getSkirt()));
        assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, stick2.getSkirt()));
        assertTrue(Arrays.equals(new int[] {0}, stick3.getSkirt()));
        assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, stick4.getSkirt()));

        //L piece skirt
        assertTrue(Arrays.equals(new int[] {0, 0}, l1.getSkirt()));
        assertTrue(Arrays.equals(new int[] {0, 0, 0}, l2.getSkirt()));
        assertTrue(Arrays.equals(new int[] {2, 0}, l3.getSkirt()));
        assertTrue(Arrays.equals(new int[] {0, 1, 1}, l4.getSkirt()));

        //S piece skirt
        assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
        assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));

        //Square skirrt
        assertTrue(Arrays.equals(new int[] {0, 0}, square.getSkirt()));

	}
    public void testBody() {
        TPoint[] body = pyr1.getBody();

        assertEquals(4, body.length);

        // Check specific points exist
        List<TPoint> list = Arrays.asList(body);
        assertTrue(list.contains(new TPoint(0, 0)));
        assertTrue(list.contains(new TPoint(1, 0)));
        assertTrue(list.contains(new TPoint(1, 1)));
        assertTrue(list.contains(new TPoint(2, 0)));
    }
    public void testRotationPoints() {
        Piece rotated = pyr1.computeNextRotation();

        List<TPoint> body = Arrays.asList(rotated.getBody());

        assertTrue(body.contains(new TPoint(1, 0)));
        assertTrue(body.contains(new TPoint(1, 1)));
        assertTrue(body.contains(new TPoint(0, 1)));
        assertTrue(body.contains(new TPoint(1, 2)));
    }
    public void testStickRotation() {
        List<TPoint> body = Arrays.asList(stick2.getBody());

        assertEquals(4, stick2.getWidth());
        assertEquals(1, stick2.getHeight());

        // Should be a horizontal line at y=0
        assertTrue(body.contains(new TPoint(0, 0)));
        assertTrue(body.contains(new TPoint(1, 0)));
        assertTrue(body.contains(new TPoint(2, 0)));
        assertTrue(body.contains(new TPoint(3, 0)));
    }
    public void testLRotation() {
        // l3 is l1 rotated twice
        List<TPoint> body = Arrays.asList(l3.getBody());

        assertEquals(2, l3.getWidth());
        assertEquals(3, l3.getHeight());

        // Points for an upside down L
        assertTrue(body.contains(new TPoint(0, 2)));
        assertTrue(body.contains(new TPoint(1, 0)));
        assertTrue(body.contains(new TPoint(1, 1)));
        assertTrue(body.contains(new TPoint(1, 2)));
    }
    public void testSquareRotation() {
        Piece rotatedSq = square.computeNextRotation();

        // The points should be identical to the original square
        assertTrue(rotatedSq.equals(square));
        assertEquals(2, rotatedSq.getWidth());
        assertEquals(2, rotatedSq.getHeight());
    }
    public void testRotationCycle() {
        Piece curr = pyr1;

        for (int i = 0; i < 4; i++) {
            curr = curr.computeNextRotation();
        }

        assertTrue(curr.equals(pyr1));
    }
    public void testEquals() {
        Piece another = new Piece(Piece.PYRAMID_STR);
        assertTrue(pyr1.equals(another));

        assertFalse(pyr1.equals(pyr2));
    }
    public void testMoreSkirts() {
        Piece stick = new Piece(Piece.STICK_STR);
        assertTrue(Arrays.equals(new int[] {0}, stick.getSkirt()));

        Piece square = new Piece(Piece.SQUARE_STR);
        assertTrue(Arrays.equals(new int[] {0, 0}, square.getSkirt()));
    }
    public void testFastRotationCycle() {
        Piece[] pieces = Piece.getPieces();
        Piece stick = pieces[Piece.STICK];

        Piece curr = stick;
        int count = 0;

        do {
            curr = curr.fastRotation();
            count++;
        } while (curr != stick);

        assertEquals(2, count); // stick has 2 rotations
    }
    public void testFastVsSlowRotation() {
        Piece[] pieces = Piece.getPieces();
        Piece p = pieces[Piece.PYRAMID];

        Piece fast = p.fastRotation();
        Piece slow = p.computeNextRotation();

        assertTrue(fast.equals(slow));
    }
    public void testImmutability() {
        int[] skirt = pyr1.getSkirt();
        skirt[0] = 999;

        // Should not affect original
        assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
    }

	
	
}

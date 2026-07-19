// Board.java

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
    private int maxHeight;
    private int[] heights;
    private int[] widths;

    private boolean[][] backupGrid;
    private int bWidth;
    private int bHeight;
    private int[] backupHeights;
    private int[] backupWidths;
    private int bMaxHeight;


	// Here a few trivial methods are provided:

	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
        maxHeight = 0;
        heights = new int[width];
        widths = new int[height];

		backupGrid = new boolean[width][height];
        backupHeights = new int[width];
        backupWidths = new int[height];
        bHeight = height;
        bWidth = width;
        bMaxHeight = 0;

	}


	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}


	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}


	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
        int res = Integer.MIN_VALUE;

        for(int x : heights){
            res = Math.max(res, x);
        }
        return res;

	}


	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
    public void sanityCheck() {
        if (DEBUG) {

            int[] trueWidths = new int[height];
            int[] trueHeights = new int[width];
            int trueMaxHeight = 0;


            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (grid[x][y] == true) {
                        trueWidths[y]++;
                        trueHeights[x] = Math.max(trueHeights[x], y + 1);
                    }
                }
                // Update true max height
                trueMaxHeight = Math.max(trueMaxHeight, trueHeights[x]);
            }

            // verify Widths
            for (int y = 0; y < height; y++) {
                if (this.widths[y] != trueWidths[y]) {
                    throw new RuntimeException("Sanity Check Failed: widths array is broken at row " + y);
                }
            }

            //  verify Heights
            for (int x = 0; x < width; x++) {
                if (this.heights[x] != trueHeights[x]) {
                    throw new RuntimeException("Sanity Check Failed: heights array is broken at column " + x);
                }
            }

            //  verify Max Height
            if (this.maxHeight != trueMaxHeight) {
                throw new RuntimeException("Sanity Check Failed: maxHeight is " + this.maxHeight + " but should be " + trueMaxHeight);
            }
        }
    }

	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.

	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int[] skirt = piece.getSkirt();
        int maxY = 0;
        for(int i = 0; i < piece.getWidth(); i++){
            int boardCol = x + i;
            int landingY = heights[boardCol] - skirt[i];

            maxY = Math.max(maxY, landingY);
        }
        return maxY;
	}


	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
       return heights[x];
	}


	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
        return widths[y];
	}


	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
        if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return true; //Out of bounds, returns true

        return grid[x][y];
	}


	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.

	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
        int result = PLACE_OK;

        backUp();

        TPoint[] body = piece.getBody();
        for (TPoint point : body){
            int boardX = x + point.x;
            int boardY = y + point.y;

            if(!inBounds(boardX, boardY)) return PLACE_OUT_BOUNDS;
            if(backupGrid[boardX][boardY] == true) return PLACE_BAD;

            grid[boardX][boardY] = true;
            heights[boardX] = Math.max(heights[boardX], boardY + 1);
            maxHeight = Math.max(maxHeight, heights[boardX]);
            widths[boardY]++;

            if(widths[boardY] == this.width) result = PLACE_ROW_FILLED;
        }
        sanityCheck();
		return result;
	}
    private void backUp(){
        System.arraycopy(widths, 0, backupWidths, 0, height);
        System.arraycopy(heights, 0, backupHeights, 0, width);
        bMaxHeight = maxHeight;
        for (int x1 = 0; x1 < width; x1++) {
            System.arraycopy(grid[x1], 0, backupGrid[x1], 0, height);
        }
        committed = false;
    }

	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
        if(committed == true){
            backUp();
        }
        int toRow = 0;
        int rowsCleared = 0;
        for(int fromRow = 0; fromRow < height; fromRow++){
            if(widths[fromRow] == width){
                rowsCleared++;
            }else{
                widths[toRow] = widths[fromRow];
                for (int x=0; x < width; x++){
                    grid[x][toRow] = grid[x][fromRow];
                }
                toRow++;
            }
        }
        for (int y = toRow; y < height; y++) {
            widths[y] = 0;

            // loop to erease blocks
            for (int x = 0; x < width; x++) {
                grid[x][y] = false;
            }
        }
        recalculateHeights();
		sanityCheck();
		return rowsCleared;
	}
    private void recalculateHeights(){
        maxHeight = 0;
        for(int x = 0; x < width; x++){
           int highestY = -1;
            for (int y = 0; y < height; y++) {
                if (grid[x][y] == true) {
                    highestY = y;
                }
            }
            heights[x] = highestY + 1;
            maxHeight = Math.max(maxHeight,  heights[x]);
        }
    }


	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed == true) return;

        restore();
        commit();
        sanityCheck();
	}


	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}

    private void restore(){
        int[] temp = widths;
        widths = backupWidths;
        backupWidths = temp;

        temp = heights;
        heights = backupHeights;
        backupHeights = temp;

        boolean[][] tempG = grid;
        grid = backupGrid;
        backupGrid = tempG;

        maxHeight = bMaxHeight;
    }



	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility)
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}

    /*
    Helper function
    Returns true if point is inside of grid
     */
    private boolean inBounds(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }
}



import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tetris
 * Author: Peter Mitchell (2021)
 *
 * TetrisBoard class:
 * Defines a Tetris Board formed with a grid and
 * provides methods for inserting pieces, and
 * checking for matches.
 */
public class TetrisBoard {
    /**
     * Dimension of each cell on the grid in pixels.
     */
    public final static int CELL_DIM = 40;
    /**
     * Number of cells on the horizontal axis.
     */
    public final static int GRID_WIDTH = 10;
    /**
     * Number of cells on the vertical axis.
     */
    public final static int GRID_HEIGHT = 15;
    /**
     * Width required for the TetrisBoard to be drawn in pixels.
     */
    public final static int WIDTH = GRID_WIDTH * CELL_DIM;
    /**
     * Height required for the TetrisBoard to be drawn in pixels.
     */
    public final static int HEIGHT = GRID_HEIGHT * CELL_DIM;

    /**
     * The grid of currently placed blocks.
     */
    private int[][] tetrisGrid;
    /**
     * Matches from the latest update to be removed at the start
     * of the next update.
     */
    private List<Integer> matches;

    /**
     * Creates an empty grid to start the TetrisBoard.
     */
    public TetrisBoard() {
        tetrisGrid = new int[GRID_WIDTH][GRID_HEIGHT];
        matches = new ArrayList<>();
        reset();
    }

    /**
     * Fills the TetrisBoard with empty cells.
     */
    public void reset() {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for(int y = 0; y < GRID_HEIGHT; y++) {
                tetrisGrid[x][y] = 0;
            }
        }
    }

    /**
     * Maps the piece provided to a position on the board to merge data.
     *
     * @param piece The piece to lock in to the board.
     */
    public void lockInPiece(TetrisPiece piece) {
        Position topLeft = piece.getTopLeft();
        int[][] data = piece.getPieceData();
        for(int x = 0; x < data.length; x++) {
            for(int y = 0; y < data[0].length; y++) {
                // Only modify cells that are not empty relative position on the piece
                if(data[x][y] != 0)
                    tetrisGrid[topLeft.x+x][topLeft.y+y] = data[x][y];
            }
        }
    }

    /**
     * Checks every row for matches and adds any found matches to the matches array.
     * All the cells on the matched rows are changed to 8 to allow colour change
     * before they are removed.
     *
     * @return The number of rows that have been successfully matched.
     */
    public int checkForMatches() {
        int rowCount = 0;
        for(int y = 0; y < GRID_HEIGHT; y++) {
            boolean match = true;
            for (int x = 0; x < GRID_WIDTH; x++) {
                if(tetrisGrid[x][y] == 0) {
                    match = false;
                    break;
                }
            }

            // Match found
            if(match) {
                rowCount++;
                matches.add(y);
                for (int x = 0; x < GRID_WIDTH; x++) {
                    tetrisGrid[x][y] = 8;
                }
            }
        }
        return rowCount;
    }

    /**
     * Clears the matched rows one at a time by moving all
     * rows above the removed row. Then sets the row at the top to all 0s.
     */
    public void clearMatchedRows() {
        for(int i = 0; i < matches.size(); i++) {
            for(int y = matches.get(i); y > 0; y--) {
                // Move down all from the row above
                for(int x = 0; x < GRID_WIDTH; x++) {
                    tetrisGrid[x][y] = tetrisGrid[x][y-1];
                }
            }
            // clear the top row
            for(int x = 0; x < GRID_WIDTH; x++) {
                tetrisGrid[x][0] = 0;
            }
        }
        matches.clear();
    }

    /**
     * Determines the updated bounding box using the translation and then checks
     * if the move is valid using isValidOnBoard().
     *
     * @param possibleTranslation The translation to test if valid.
     * @param piece The piece to be moved with the translation.
     * @return True if the piece can be moved with the translation.
     */
    public boolean isMoveValid(Position possibleTranslation, TetrisPiece piece) {
        Position newPos = piece.getTopLeft();
        newPos.move(possibleTranslation);
        return isValidOnBoard(newPos, piece.getPieceWidth(), piece.getPieceHeight(), piece.getPieceData());
    }

    /**
     * Tests first if the bounding box is valid inside the board. It is ignored and assumed true if the
     * piece is still off the top. Then checks for the data if it would all sit inside empty cells on the board.
     *
     * @param topLeft Top left grid cell position on the grid where the piece is to go.
     * @param width Width of the piece.
     * @param height Height of the piece.
     * @param data Data related to the piece.
     * @return True if the bounding box matches and the data can be filled into empty cells.
     */
    public boolean isValidOnBoard(Position topLeft, int width, int height, int[][] data) {
        // Check inside board space
        if(topLeft.x < 0 || topLeft.x+width > GRID_WIDTH
                || topLeft.y + height > GRID_HEIGHT)
            return false;
        // Ignore if still coming down from the top to enter board
        if(topLeft.y < 0) return true;

        // Check every cell to make sure filled cells in the data can be placed in empty
        // cells in the tetris Grid.
        for(int x = 0; x < data.length; x++) {
            for(int y = 0; y < data[0].length; y++) {
                if(data[x][y] != 0 && tetrisGrid[topLeft.x+x][topLeft.y+y] != 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * Draws the filled cells to the grid. Colours are based on the numbers related to the pieces
     * used to fill the grid.
     *
     * @param g Reference to the Graphics object for drawing.
     */
    public void paint(Graphics g) {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for(int y = 0; y < GRID_HEIGHT; y++) {
                if(tetrisGrid[x][y] != 0) {
                    g.setColor(TetrisPiece.pieceColours[tetrisGrid[x][y]]);
                    g.fillRect(x * TetrisBoard.CELL_DIM + 3, y * TetrisBoard.CELL_DIM + 3,
                            TetrisBoard.CELL_DIM - 6, TetrisBoard.CELL_DIM - 6);
                }
            }
        }
    }
}

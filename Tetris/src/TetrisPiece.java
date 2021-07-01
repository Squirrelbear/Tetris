import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tetris
 * Author: Peter Mitchell (2021)
 *
 * TetrisPiece class:
 * Defines a tetris piece with its bounds and how the cells are positioned relative to the grid.
 */
public class TetrisPiece {
    /**
     * The line piece.
     */
    private final int[][] PIECE_1 = { {1,1,1,1} }; // line
    /**
     * The arrow pointing left.
     */
    private final int[][] PIECE_2 = { {2,0},{2,2},{0,2} }; // point one
    /**
     * The arrow pointing right.
     */
    private final int[][] PIECE_3 = { {0,3},{3,3}, {3,0} }; // point two
    /**
     * The T shaped piece.
     */
    private final int[][] PIECE_4 = { {4,0},{4,4}, {4,0} }; // T
    /**
     * The square shaped piece.
     */
    private final int[][] PIECE_5 = { {5,5}, {5,5} }; // square
    /**
     * The L shaped piece.
     */
    private final int[][] PIECE_6 = { {6,6,6}, {0,0,6} }; // L
    /**
     * The backwards L shaped piece.
     */
    private final int[][] PIECE_7 = { {0,0,7}, {7,7,7} }; // backwards L

    /**
     * Rotation centre for pieces based on piece number.
     */
    private final Position[] pieceCentres = { new Position(0,1), new Position(1,1), new Position(1,1),
                                              new Position(1,0), new Position(0,0), new Position(0,1),
                                                new Position(1,1)};
    /**
     * Colours to draw cells related to each piece. The numbers are stored in the piece data.
     * Position 0 is ignored, and position 8 (WHITE) is used for showing matched cells.
     */
    public static final Color[] pieceColours = { Color.BLACK, Color.CYAN, Color.RED, Color.GREEN,
                                            new Color(128, 0, 255), Color.YELLOW,
                                            Color.ORANGE, Color.BLUE, Color.WHITE };

    /**
     * List of all the pieces defined in PIECE_X where X is the piece number.
     */
    private static List<int[][]> listOfPieceData;

    /**
     * The piece number. Is not actually used outside of storing the random number.
     */
    private int pieceType;
    /**
     * Position where the piece should appear on the grid currently.
     */
    private Position position;
    /**
     * Rotation centre of the piece based on the piece type using the piecesCentre values.
     */
    private Position pieceCentre;
    /**
     * Data taken from list of pieces data to represent the appropriate piece type.
     */
    private int[][] pieceData;
    /**
     * The position where the piece should start at above the game board.
     */
    private Position startPosition;
    /**
     * Reference to the Random object.
     */
    private Random rand;
    /**
     * Width of the piece based on how wide the data is.
     */
    private int pieceWidth;
    /**
     * Height of the piece based on how high the data is.
     */
    private int pieceHeight;

    /**
     * Creates a new random tetris piece with random type.
     *
     * @param startPosition The position to start the piece at.
     */
    public TetrisPiece(Position startPosition) {
        this.startPosition = new Position(startPosition);
        fillPieceData();
        rand = new Random();
        setupRandomPiece();
    }

    /**
     * Draws the piece to the board.
     *
     * @param g Reference to the Graphics object for drawing.
     */
    public void paint(Graphics g) {
        int startX = (position.x - pieceCentre.x) * TetrisBoard.CELL_DIM;
        int startY = (position.y - pieceCentre.y) * TetrisBoard.CELL_DIM;

        drawPieceUsingOffset(g, startX, startY);
    }

    /**
     * Calculates the offset based on centring the piece and then draws it.
     * Used for the status panel to show the next piece.
     *
     * @param g Reference to the Graphics object for drawing.
     * @param centreX Centre X position.
     * @param centreY Centre Y position.
     */
    public void paintAtCentre(Graphics g, int centreX, int centreY) {
        int startX = centreX - (pieceWidth*TetrisBoard.CELL_DIM / 2);
        int startY = centreY - (pieceHeight*TetrisBoard.CELL_DIM / 2);

        drawPieceUsingOffset(g, startX, startY);
    }

    /**
     * Moves the piece by the amount defined in the movement vector.
     *
     * @param movementVector Direction/amount to move.
     */
    public void move(Position movementVector) {
        position.move(movementVector);
    }

    /**
     * Attempts to rotate the piece to the right and then validates that the
     * rotation places it in a legal position. The update to rotation is only
     * applied if it is valid.
     *
     * @param tetrisBoard Reference to the tetris board to validate the rotation.
     */
    public void rotateClockwise(TetrisBoard tetrisBoard) {
        // Do not rotate if outside the bounds.
        if(getTopLeft().y < 0) return;

        int[][] result = new int[pieceHeight][pieceWidth];
        // transpose
        for(int x = 0; x < pieceWidth; x++) {
            for(int y = 0; y < pieceHeight; y++) {
                result[y][x] = pieceData[x][y];
            }
        }

        // reverse each row
        for(int x = 0; x < pieceHeight / 2; x++) {
            for (int y = 0; y < pieceWidth; y++) {
                swap(result, x, y, pieceHeight - x-1, y);
            }
        }

        if(!tetrisBoard.isValidOnBoard(getTopLeft(), result.length, result[0].length, result))
            return;

        // Rotation is valid, save it and update dimensions
        pieceData = result;
        pieceWidth = pieceData.length;
        pieceHeight = pieceData[0].length;
    }

    /**
     * Attempts to rotate the piece to the left and then validates that the
     * rotation places it in a legal position. The update to rotation is only
     * applied if it is valid.
     *
     * @param tetrisBoard Reference to the tetris board to validate the rotation.
     */
    public void rotateCounterClockwise(TetrisBoard tetrisBoard) {
        // Do not rotate if outside the bounds.
        if(getTopLeft().y < 0) return;

        int[][] result = new int[pieceHeight][pieceWidth];
        // transpose
        for(int x = 0; x < pieceWidth; x++) {
            for(int y = 0; y < pieceHeight; y++) {
                result[y][x] = pieceData[x][y];
            }
        }

        // reverse each column
        for(int x = 0; x < pieceHeight; x++) {
            for (int y = 0; y < pieceWidth / 2; y++) {
                swap(result, x, y, x, pieceWidth - y - 1);
            }
        }

        if(!tetrisBoard.isValidOnBoard(getTopLeft(), result.length, result[0].length, result))
            return;

        // Rotation is valid, save it and update dimensions
        pieceData = result;
        pieceWidth = pieceData.length;
        pieceHeight = pieceData[0].length;
    }

    /**
     * Resets to a new random piece.
     */
    public void reset() {
        setupRandomPiece();
    }

    /**
     * Gets the top left cell based on offsetting the position by the centre.
     *
     * @return Returns the top left grid cell that would be related to this piece.
     */
    public Position getTopLeft() {
        return new Position(position.x-pieceCentre.x, position.y-pieceCentre.y);
    }

    /**
     * Gets the raw data about the piece.
     *
     * @return The data related to this piece.
     */
    public int[][] getPieceData() {
        return pieceData;
    }

    /**
     * Gets the width of the piece.
     *
     * @return The width of the piece.
     */
    public int getPieceWidth() { return pieceWidth; }

    /**
     * Gets the height of the piece.
     *
     * @return The height of the piece.
     */
    public int getPieceHeight() { return pieceHeight; }

    /**
     * Selects a random piece and then sets the variables
     * to reflect that piece including the piece data, width, height, and centre.
     */
    private void setupRandomPiece() {
        int pieceType = rand.nextInt(listOfPieceData.size());
        pieceData = listOfPieceData.get(pieceType);
        position = new Position(startPosition);
        pieceCentre = pieceCentres[pieceType];
        pieceWidth = pieceData.length;
        pieceHeight = pieceData[0].length;
    }

    /**
     * Prepares the array of piece data based on the constant definitions of all the pieces.
     */
    private void fillPieceData() {
        listOfPieceData = new ArrayList<>();
        listOfPieceData.add(PIECE_1);
        listOfPieceData.add(PIECE_2);
        listOfPieceData.add(PIECE_3);
        listOfPieceData.add(PIECE_4);
        listOfPieceData.add(PIECE_5);
        listOfPieceData.add(PIECE_6);
        listOfPieceData.add(PIECE_7);
    }

    /**
     * Draws the piece using pieceData shown with pieceColours at the correct position
     * on either the board or other panel. The generalises the code used for drawing to
     * make it usable for both draw methods.
     *
     * @param g Reference to the graphics object for drawing.
     * @param startX Top left corner in pixels to start drawing at.
     * @param startY Top left corner in pixels to start drawing at.
     */
    private void drawPieceUsingOffset(Graphics g, int startX, int startY) {
        for(int x = 0; x < pieceWidth; x++) {
            for(int y = 0; y < pieceHeight; y++) {
                if(pieceData[x][y] != 0) {
                    g.setColor(pieceColours[pieceData[x][y]]);
                    g.fillRect(startX + x * TetrisBoard.CELL_DIM + 3, startY + y * TetrisBoard.CELL_DIM + 3,
                            TetrisBoard.CELL_DIM - 6, TetrisBoard.CELL_DIM - 6);
                }
            }
        }
    }

    /**
     * Performs a swap between cells (x1,y1) and (x2,y2) in the
     * referenced data array.
     *
     * @param data Array of data to modify.
     * @param x1 X coordinate of the first element.
     * @param y1 Y coordinate of the first element.
     * @param x2 X coordinate of the second element.
     * @param y2 Y coordinate of the second element.
     */
    private void swap(int[][] data, int x1, int y1, int x2, int y2) {
        int temp = data[x1][y1];
        data[x1][y1] = data[x2][y2];
        data[x2][y2] = temp;
    }
}

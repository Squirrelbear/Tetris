import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Tetris
 * Author: Peter Mitchell (2021)
 *
 * TetrisPanel class:
 * Displays the Tetris grid and current piece with a
 * timer to manage time between updates.
 */
public class TetrisPanel extends JPanel implements ActionListener {
    /**
     * Reference to the Game for passing changes to score/level/nextpiece.
     */
    private Game game;
    /**
     * The current piece that is moving down the screen.
     */
    private TetrisPiece currentPiece;
    /**
     * The next piece that will be used after the current piece.
     */
    private TetrisPiece nextPiece;
    /**
     * Position above the top of the game where the piece will start.
     */
    private Position pieceStartPosition;
    /**
     * Reference to the timer that forces updates to occur at set intervals.
     * The timer is modified based on the level to increase rate for higher levels.
     */
    private Timer gameTimer;
    /**
     * The next move based on keyboard input.
     */
    private Position nextMove;
    /**
     * The current score based on matched rows.
     */
    private int score;
    /**
     * The current level based on total score.
     */
    private int level;
    /**
     * Reference to the tetris board containing the grid of placed pieces.
     */
    private TetrisBoard tetrisBoard;
    /**
     * Game state to indicate the game has been lost if true.
     */
    private boolean gameOver;
    /**
     * The amount of score required to increase the level number.
     */
    private final int SCORE_PER_LEVEL = 5;

    /**
     * Sets up all the tetris panel components including the tetris board,
     * current/next piece, the timer for triggering updates, and starts the timer
     * to begin the game.
     *
     * @param game Reference to the Game object to pass updates to status panel.
     */
    public TetrisPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(TetrisBoard.WIDTH,TetrisBoard.HEIGHT));
        setBackground(Color.BLACK);
        pieceStartPosition = new Position(TetrisBoard.GRID_WIDTH/2, -2);

        tetrisBoard = new TetrisBoard();
        currentPiece = new TetrisPiece(pieceStartPosition);
        nextPiece = new TetrisPiece(pieceStartPosition);
        game.setNextPiece(nextPiece);
        nextMove = Position.DOWN;
        gameTimer = new Timer(300,this);
        gameTimer.setRepeats(true);
        gameOver = false;
        gameTimer.start();
    }

    /**
     * Draws the grid, any filled cells on the grid, the current piece,
     * and a message relevant to any game over or paused state if necessary.
     *
     * @param g Reference to the Graphics object for drawing.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawGrid(g);
        tetrisBoard.paint(g);
        currentPiece.paint(g);
        if(gameOver)
            drawCenteredMessage(g, "GAME OVER. R to restart!");
        else if(!gameTimer.isRunning())
            drawCenteredMessage(g, "PAUSED. P to unpause!");
    }

    /**
     * Draws a grid of lines to represent the spacing of the grid.
     *
     * @param g Reference to the Graphics object for drawings.
     */
    private void drawGrid(Graphics g) {
        g.setColor(Color.WHITE);
        // Draw vertical lines
        int y2 = 0;
        int y1 = TetrisBoard.HEIGHT;
        for(int x = 0; x < TetrisBoard.GRID_WIDTH; x++)
            g.drawLine(x * TetrisBoard.CELL_DIM, y1, x * TetrisBoard.CELL_DIM, y2);

        // Draw horizontal lines
        int x2 = 0;
        int x1 = TetrisBoard.WIDTH;
        for(int y = 0; y < TetrisBoard.GRID_HEIGHT; y++)
            g.drawLine(x1, y * TetrisBoard.CELL_DIM, x2, y * TetrisBoard.CELL_DIM);
    }

    /**
     * Draws the specified message centered in the panel with a white box around it.
     *
     * @param g Reference to the Graphics object for drawing.
     * @param message The message to be printed in the middle of the screen.
     */
    private void drawCenteredMessage(Graphics g, String message) {
        Font font = new Font("Arial", Font.BOLD, 20);
        g.setFont(font);
        int messageWidth = g.getFontMetrics().stringWidth(message);

        g.setColor(Color.WHITE);
        g.fillRect(TetrisBoard.WIDTH/2-messageWidth/2-5,TetrisBoard.HEIGHT/2-20, messageWidth+10, 30);

        g.setColor(Color.RED);
        g.drawString(message, TetrisBoard.WIDTH/2-messageWidth/2, TetrisBoard.HEIGHT/2);
    }

    /**
     * Toggles the state of the timer to start or stop making updates start or stop.
     */
    public void togglePause() {
        if(gameTimer.isRunning()) {
            gameTimer.stop();
            repaint();
        } else
            gameTimer.start();
    }

    /**
     * Resets the state of all elements back to their defaults and restarts the timer.
     */
    public void restart() {
        score = 0;
        level = 1;
        game.setScore(score);
        game.setLevel(level);
        nextMove = Position.DOWN;
        currentPiece.reset();
        nextPiece.reset();
        game.setNextPiece(nextPiece);
        tetrisBoard.reset();
        gameOver = false;
        gameTimer.start();
    }

    /**
     * Forces the piece to continually move down until it is locked in place.
     */
    public void hardDropCurrent() {
        nextMove = Position.DOWN;
        while(tetrisBoard.isMoveValid(nextMove,currentPiece))
            currentPiece.move(nextMove);
        lockInPiece();
    }

    /**
     * Locks in the piece to the grid. If the piece is locked in outside of the grid
     * it causes an instant game over. Once the piece is locked in it will check for matches,
     * and change to the next piece. Score and level are updated to reflect changes based on matches.
     */
    public void lockInPiece() {
        if(currentPiece.getTopLeft().y < 0) {
            gameOver = true;
            return;
        }

        tetrisBoard.lockInPiece(currentPiece);
        currentPiece = nextPiece;
        nextPiece = new TetrisPiece(pieceStartPosition);
        game.setNextPiece(nextPiece);
        int matches = tetrisBoard.checkForMatches();
        if(matches > 0) {
            score += matches * matches;
            game.setScore(score);
            level = 1+score/SCORE_PER_LEVEL;
            game.setLevel(level);
            gameTimer.setDelay(Math.max(30,300-(level-1)*30));
        }
    }

    /**
     * Triggered by the timer to perform regular updates.
     * Removes any matched rows from the previous update, makes the piece move if possible,
     * or otherwise locks in the piece where it is located. Then repaints the board.
     *
     * @param e Not used.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        tetrisBoard.clearMatchedRows();
        if(tetrisBoard.isMoveValid(nextMove,currentPiece))
            currentPiece.move(nextMove);
        else if(nextMove.x == 0)
            lockInPiece();
        nextMove = Position.DOWN;
        repaint();
    }

    /**
     * Checks for input from the user to apply game changes.
     * The tetris piece controls are only checked if the game is not paused and it is not a game over.
     * Piece can be moved with left/right/down arrow keys. Defaults back to down after each move.
     * Space will instantly move the piece down to a final position.
     * Z and X will rotate the piece left or right.
     *
     * At any time the game can be paused/unpaused by pressing P to toggle.
     * At any time the game can be restarted by pressing R.
     * At any time the game can be quit by pressing Escape.
     *
     * @param e Information about the key that was pressed.
     */
    public void handleKeyEvent(KeyEvent e) {
        if(gameTimer.isRunning() && !gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT)
                nextMove = Position.LEFT;
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                nextMove = Position.RIGHT;
            else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                nextMove = Position.DOWN;
            else if (e.getKeyCode() == KeyEvent.VK_SPACE)
                hardDropCurrent();
            else if (e.getKeyCode() == KeyEvent.VK_Z)
                currentPiece.rotateCounterClockwise(tetrisBoard);
            else if (e.getKeyCode() == KeyEvent.VK_X)
                currentPiece.rotateClockwise(tetrisBoard);
        }
        if(e.getKeyCode() == KeyEvent.VK_P)
            togglePause();
        else if(e.getKeyCode() == KeyEvent.VK_R)
            restart();
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Tetris
 * Author: Peter Mitchell (2021)
 *
 * Game class:
 * Defines the entry point for the game and manages passing of information between panels.
 */
public class Game implements KeyListener {
    /**
     * Creates a new game object to exist as an entry point.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        Game game = new Game();
    }

    /**
     * Panel showing the score, level and next piece.
     */
    private StatusPanel statusPanel;
    /**
     * Panel with the main game where Tetris is played.
     */
    private TetrisPanel tetrisPanel;

    /**
     * Creates the JFrame and inserts a status panel and tetris panel.
     */
    public Game() {
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        statusPanel = new StatusPanel();
        tetrisPanel = new TetrisPanel(this);

        frame.getContentPane().add(tetrisPanel, BorderLayout.WEST);
        frame.getContentPane().add(statusPanel, BorderLayout.EAST);

        frame.addKeyListener(this);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Passes a message to the status panel indicating the next piece to be played.
     *
     * @param nextPiece The next tetris piece to be played.
     */
    public void setNextPiece(TetrisPiece nextPiece) {
        statusPanel.setNextPiece(nextPiece);
    }

    /**
     * Passes a message to the status panel indicating the new score.
     *
     * @param newScore The new score to display on the status panel.
     */
    public void setScore(int newScore) {
        statusPanel.setScore(newScore);
    }

    /**
     * Passes a message to the status panel indicating the new level.
     *
     * @param newLevel The new level to display on the status panel.
     */
    public void setLevel(int newLevel) {
        statusPanel.setLevel(newLevel);
    }

    /**
     * Passes a message to the tetris panel with key press event information.
     *
     * @param e The information about what key was pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        tetrisPanel.handleKeyEvent(e);
    }

    /**
     * Not used,
     * @param e Not used.
     */
    @Override
    public void keyReleased(KeyEvent e) {}
    /**
     * Not used,
     * @param e Not used.
     */
    @Override
    public void keyTyped(KeyEvent e) {}
}

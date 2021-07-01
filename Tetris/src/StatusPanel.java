import javax.swing.*;
import java.awt.*;

/**
 * Tetris
 * Author: Peter Mitchell (2021)
 *
 * StatusPanel class:
 * Defines a panel to show the current score, level, and next piece.
 */
public class StatusPanel extends JPanel {
    /**
     * The font used for all labels.
     */
    private Font font = new Font("Arial", Font.BOLD, 55);
    /**
     * Reference to the label where the score will be updated.
     */
    private JLabel scoreValueLabel;
    /**
     * Reference to the label where the level will be updated.
     */
    private JLabel levelValueLabel;
    /**
     * Reference to the panel where a single tetris piece will be drawn.
     */
    private PreviewPanel previewPanel;

    /**
     * Creates and configures all components ready for use.
     */
    public StatusPanel() {
        setPreferredSize(new Dimension(200, TetrisBoard.HEIGHT));
        setBackground(Color.gray);

        JLabel scoreLabel = createJLabelFactory("SCORE");
        scoreValueLabel = createJLabelFactory("0");
        JLabel levelLabel = createJLabelFactory("LEVEL");
        levelValueLabel = createJLabelFactory("1");
        JLabel fillerLabel = createJLabelFactory("             ");
        previewPanel = new PreviewPanel();

        add(scoreLabel);
        add(scoreValueLabel);
        add(levelLabel);
        add(levelValueLabel);
        add(fillerLabel);
        add(previewPanel);
    }

    /**
     * Changes the preview panel display to show the new next piece.
     *
     * @param nextPiece The next piece to be displayed.
     */
    public void setNextPiece(TetrisPiece nextPiece) {
        previewPanel.setNextPiece(nextPiece);
    }

    /**
     * Sets the value of the score label to the new score.
     *
     * @param newScore The new score to display.
     */
    public void setScore(int newScore) {
        scoreValueLabel.setText(String.valueOf(newScore));
    }

    /**
     * Sets the value of the level label to the new level.
     *
     * @param newLevel The new level to display.
     */
    public void setLevel(int newLevel) {
        levelValueLabel.setText(String.valueOf(newLevel));
    }

    /**
     * Creates a JLabel with the specified text and applies the font.
     *
     * @param text The text to place in a JLabel.
     * @return The newly created JLabel.
     */
    private JLabel createJLabelFactory(String text) {
        JLabel result = new JLabel(text);
        result.setFont(font);
        return result;
    }
}

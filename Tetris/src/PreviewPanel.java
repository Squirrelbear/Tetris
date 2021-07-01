import javax.swing.*;
import java.awt.*;

/**
 * Tetris
 * Author: Peter Mitchell (2021)
 *
 * PreviewPanel class:
 * Renders a preview of the next piece to be played.
 */
public class PreviewPanel extends JPanel {
    /**
     * Reference to the next piece from the Tetris Panel.
     */
    private TetrisPiece nextPiece;

    /**
     * Creates a correctly sized dark gray panel.
     */
    public PreviewPanel() {
        setPreferredSize(new Dimension(180, 180));
        setBackground(Color.DARK_GRAY);
    }

    /**
     * Draws the next piece centered in the panel.
     *
     * @param g Reference to the Graphics object for drawing.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(nextPiece != null)
            nextPiece.paintAtCentre(g,90,90);
    }

    /**
     * Changes the next piece to be displayed and forces a repaint.
     *
     * @param nextPiece The next piece to be displayed.
     */
    public void setNextPiece(TetrisPiece nextPiece) {
        this.nextPiece = nextPiece;
        repaint();
    }
}

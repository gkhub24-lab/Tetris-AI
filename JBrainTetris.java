import javax.swing.*;
import java.awt.*;

public class JBrainTetris extends JTetris {

    private Brain brain;
    private JCheckBox brainMode;
    private Brain.Move move;
    private Brain.Move currentMove;

    private int lastCount;

    // Adversary
    private JSlider adversary;
    private JLabel status;

    public JBrainTetris(int pixels) {
        super(pixels);
        brain = new DefaultBrain();
        move = new Brain.Move();
        currentMove = null;
        lastCount = -1;
    }

    public static void main(String[] args) {
        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(tetris);
        frame.setVisible(true);
    }

    @Override
    public JComponent createControlPanel() {
        JComponent panel = super.createControlPanel();

        // Brain checkbox
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        panel.add(brainMode);

        // adversary controls
        JPanel little = new JPanel();
        little.add(new JLabel("Adversary:"));

        adversary = new JSlider(0, 100, 0);
        adversary.setPreferredSize(new Dimension(100, 15));
        little.add(adversary);

        status = new JLabel("ok");
        little.add(status);

        panel.add(little);

        return panel;
    }

    @Override
    public void tick(int verb) {

        if (brainMode.isSelected() && verb == DOWN) {

            // detect new piece
            if (count != lastCount) {
                board.undo();

                currentMove = brain.bestMove(board, currentPiece, HEIGHT, move);
                lastCount = count;
            }

            if (currentMove != null) {
                // Try to rotate toward target
                if (currentPiece != currentMove.piece) {
                    super.tick(ROTATE);
                }
                // move horizontally toward target
                else if (currentX < currentMove.x) {
                    super.tick(RIGHT);
                }
                else if (currentX > currentMove.x) {
                    super.tick(LEFT);
                }

                else {

                }
            }
        }


        super.tick(verb);
    }

    @Override
    public Piece pickNextPiece() {
        int adv = adversary.getValue();
        int rand = (int)(Math.random() * 100);

        // Normal random behavior
        if (rand >= adv) {
            status.setText("ok");
            return super.pickNextPiece();
        }

        // adversary chooses worst piece
        status.setText("*ok*");

        Piece worstPiece = null;
        double worstScore = Double.NEGATIVE_INFINITY;

        for (Piece p : pieces) {
            board.undo(); // ensure clean state

            Brain.Move m = brain.bestMove(board, p, HEIGHT, move);

            if (m != null && m.score > worstScore) {
                worstScore = m.score;
                worstPiece = p;
            }
        }

        return worstPiece != null ? worstPiece : super.pickNextPiece();
    }
}
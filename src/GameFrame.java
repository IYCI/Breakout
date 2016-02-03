import javax.swing.*;
import java.awt.*;

/**
 * Created by jason on 1/19/2016.
 */
public class GameFrame extends JFrame {
    static private int mFrameRate;
    static private int mSpeed;

    public GameFrame(int frameRate, int speed) throws HeadlessException {
        mFrameRate = frameRate;
        mSpeed = speed;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println(this.getWidth() + " " + this.getHeight());

        setMinimumSize(new Dimension(1366, 768));


        // center
        setLocationRelativeTo(null);
    }

    public void startGame(int level){
        //removeAll();
        GameBoard mGameBoard = new GameBoard(level, mFrameRate, mSpeed);
        setContentPane(mGameBoard);
        revalidate();
        mGameBoard.requestFocusInWindow();
        mGameBoard.setFocusable(true);
    }

}

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by jason on 1/27/2016.
 */
public class Splashscreen extends JComponent implements KeyListener {
    private BufferedImage mBackGroundImage;
    private Main mMain;
    private GameFrame mGameFrame;
    private GameBoard mGameBoard;

    public Splashscreen(GameFrame gameframe) {
        mGameFrame = gameframe;

        this.addKeyListener(this);
        this.setFocusable(true);
        try {
            mBackGroundImage = ImageIO.read(new File("src/blur_Background.png"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(mBackGroundImage, 0, 0, this.getWidth(), this.getHeight(), null);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Roboto", Font.PLAIN, this.getHeight()/11));
        g2.drawString("Super-awesome BreakOut!", this.getWidth()/5, this.getHeight()/3);

        g2.setFont(new Font("Roboto", Font.ITALIC, this.getHeight()/44));
        g2.drawString("by: Jiazhi Feng j27feng 20475044", this.getWidth()*7/10, this.getHeight()*6/7 + this.getHeight()/8);

        g2.setFont(new Font("Roboto", Font.ITALIC, this.getHeight()/25));
        g2.drawString("Controls: " , this.getWidth()/5, this.getHeight()/2 + this.getHeight()/8);

        g2.setFont(new Font("Roboto", Font.PLAIN, this.getHeight()/35));
        g2.drawString("Use the mouse or left/right arrow keys to move the paddle" , this.getWidth()/5 + this.getWidth()/8, this.getHeight()/2 + this.getHeight()*2/12);
        g2.drawString("Press Left Mouse Button to shoot the ball" , this.getWidth()/5 + this.getWidth()/8, this.getHeight()/2 + this.getHeight()*5/24);

        g2.setColor(Color.GREEN);
        g2.drawString("Press 1 now to start the game level 1, press 2 to select level 2" , this.getWidth()/5 + this.getWidth()/8, this.getHeight()/2 + this.getHeight()*7/24);
    }

    /*** implements KeyListener ***/
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("Pressed " + e.getExtendedKeyCode());
        if(e.getKeyCode() == KeyEvent.VK_1){
            mGameFrame.startGame(1);
        }
        else if(e.getKeyCode() == KeyEvent.VK_2){
            System.out.println("level 2 choose");
            mGameFrame.startGame(2);
        }
        else{
            return;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}

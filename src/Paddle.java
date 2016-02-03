import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by jason on 1/19/2016.
 */
public class Paddle {
    private int KEYBOARD_MOVE_UNIT = 20;
    private GameBoard mGameBoard;
    private int paddleWidth;
    private int paddleHeight;
    private int bottomMargin = 10;
    private int paddleArcRadius = 5;
    private int startPosnX;
    private int startPosnY;
    private int frameWidth, frameHeight;
    private Color paddleColor;
    private BufferedImage mPaddleImage;


    public Paddle(GameBoard gameBoard, Color color){
        mGameBoard = gameBoard;
        frameWidth = mGameBoard.getWidth();
        frameHeight = mGameBoard.getHeight();
        paddleColor = color;

        startPosnX = (frameWidth - paddleWidth)/2;
        startPosnY = frameHeight - paddleHeight - bottomMargin;
        try {
            mPaddleImage = ImageIO.read(new File("src/paddle.png"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    protected void paintComponent(Graphics2D g2, int width, int height) {
        frameWidth = width;
        frameHeight = height;

        paddleWidth = frameWidth/6;
        paddleHeight = paddleWidth/6;
        paddleArcRadius = paddleHeight;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(32));           // 32 pixels wide
        g2.setColor(paddleColor);

        startPosnY = frameHeight - paddleHeight - bottomMargin;

        // if mouse out of the screen, make it attach to the border
        Double relativeX = MouseInfo.getPointerInfo().getLocation().getX() - mGameBoard.getLocationOnScreen().getX();
        if (relativeX < 0) {
            startPosnX = 0;
        }
        else if(relativeX > frameWidth - (paddleWidth/2)){
            startPosnX = frameWidth - paddleWidth;
        }

        g2.drawImage(mPaddleImage, startPosnX, startPosnY, paddleWidth, paddleHeight, null);
        //g2.fillRoundRect(startPosnX, startPosnY, paddleWidth, paddleHeight, paddleArcRadius, paddleArcRadius);
        Toolkit.getDefaultToolkit().sync();

    }

    protected Point getCenterTopPoint(){
        return new Point(startPosnX + paddleWidth/2, startPosnY);
    }

    protected void moveLeft() {
        KEYBOARD_MOVE_UNIT = frameWidth/50;
        update(startPosnX - KEYBOARD_MOVE_UNIT);
    }

    protected void moveRight() {
        KEYBOARD_MOVE_UNIT = frameWidth/50;
        update(startPosnX + KEYBOARD_MOVE_UNIT);
    }

    protected void updateWithMouse(int x){
         update(x - (paddleWidth/2));
    }

    private void update(int x){
        //System.out.println(startPosnX + " " + startPosnY + ", x is: " + x);
        if (x < 0) {
            startPosnX = 0;
        }
        else if(x > frameWidth - paddleWidth){
            startPosnX = frameWidth - paddleWidth;
        }
        else{
            startPosnX = x;
        }
    }

    protected Line2D getTopBorder(){
        return new Line2D.Double(startPosnX, startPosnY, startPosnX + paddleWidth, startPosnY);
    }

    protected Line2D getMidLineHorizontal(){
        return new Line2D.Double(startPosnX, startPosnY+paddleHeight/2, startPosnX + paddleWidth, startPosnY+paddleHeight/2);
    }

    protected Line2D getLeftBorder(){
        return new Line2D.Double(startPosnX, startPosnY, startPosnX, startPosnY + paddleHeight);
    }

    protected Line2D getRightBorder(){
        return new Line2D.Double(startPosnX + paddleWidth, startPosnY, startPosnX + paddleWidth, startPosnY + paddleHeight);
    }

    protected Point getUpperLeftPoint(){
        return new Point(startPosnX, startPosnY);
    }

    protected Point getUpperRightPoint(){
        return new Point(startPosnX + paddleWidth, startPosnY);
    }

    protected int getLeftMostX(){
        return startPosnX + paddleHeight/2;
    }

    protected int getFirstPointX(){
        return getLeftMostX() + (paddleWidth - paddleHeight)/4;
    }

    protected int getSecondPointX(){
        return getLeftMostX() + (paddleWidth - paddleHeight)/2;
    }

    protected int getThirdPointX(){
        return getLeftMostX() + 3*(paddleWidth - paddleHeight)/4;
    }

    protected int getRightMostx(){
        return startPosnX + paddleWidth - paddleHeight/2;
    }
}

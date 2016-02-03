import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jason on 1/20/2016.
 */
public class Ball {
    private GameBoard mGameBoard;
    private Paddle mPaddle;
    private ArrayList<BrickItem> mBricks;
    private int posnX, posnY;
    private int ballRadius;
    private int frameWidth, frameHeight;
    private Color ballColor;
    private boolean randomAngleInit = true;
    private double vectorLengthSq;
    private BufferedImage mBallImage;


    private int dx = 0;
    private int dy = 0;

    public Ball(GameBoard gameBoard, Paddle paddle, Color color) {
        mGameBoard = gameBoard;
        mPaddle = paddle;
        frameHeight = mGameBoard.getHeight();
        frameWidth = mGameBoard.getWidth();
        ballColor = color;
        mBricks = mGameBoard.getBricks().getBricks();

        try {
            mBallImage = ImageIO.read(new File("src/ball-hd.png"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }

    protected void setInitPosition(){
        dx = 0;
        dy = 0;
        Double x = mPaddle.getCenterTopPoint().getX() - ballRadius;
        posnX = x.intValue();
        Double y = mPaddle.getCenterTopPoint().getY() - ballRadius*2;
        posnY = y.intValue();
    }

    protected void move(){
        Line2D topBorder = new Line2D.Double(0, 0, frameWidth, 0);
        //topBorder.setLine(0, 0, frameWidth, 0);
        Line2D leftBorder = new Line2D.Double(0, 0, 0, frameHeight);
        //leftBorder.setLine(0, 0, 0, frameHeight);

        Line2D rightBorder = new Line2D.Double(frameWidth, 0, frameWidth, frameHeight);
        //rightBorder.setLine(frameWidth, 0, frameWidth, frameHeight);

        Line2D bottomBorder = new Line2D.Double(0, frameHeight, frameWidth, frameHeight);
        //bottomBorder.setLine(0, frameHeight, frameWidth, frameHeight);

        Line2D paddleTop = mPaddle.getTopBorder();
        int speedFactor = mGameBoard.getSpeedFactor();


        Point2D ball2D = new Point2D.Double(posnX + ballRadius, posnY + ballRadius);
        //= new Point2D(posnX + ballRadius, posnY + ballRadius);

        // check collision
        if (dx == 0 && dy == 0) {
            if(randomAngleInit){
                dx = (new Random().nextInt(8) - 3);
            }
            System.out.println("dx is " + dx);
            dy = -4;

            vectorLengthSq = dx*dx + dy*dy;
        }
        else if(bottomBorder.ptLineDist(ball2D) <= ballRadius ||
                posnY > bottomBorder.getY1()){
            mGameBoard.lostLive();
            mGameBoard.stop();
        }
        else if(topBorder.ptLineDist(ball2D) <= ballRadius ||
                posnY < topBorder.getY1()) {
            dy = dy * -1;
        }
        else if(leftBorder.ptLineDist(ball2D) <= ballRadius ||
                posnX < leftBorder.getX1() ||
                rightBorder.ptLineDist(ball2D) <= ballRadius ||
                posnX > rightBorder.getX1()) {
            dx = dx * -1;
        }
        else if (posnY + 2 * ballRadius > paddleTop.getY1() &&
                mPaddle.getUpperLeftPoint().getX() - 2*ballRadius < posnX &&
                mPaddle.getUpperRightPoint().getX() > posnX) {
            if (posnX + ballRadius < mPaddle.getLeftMostX()){
                int tmp = dx;
                dx = -1 * dy;
                dy = -1 * tmp;
            }
            else if (posnX + ballRadius < mPaddle.getFirstPointX()){
                try {
                    if (Math.abs(dx) / Math.abs(dy) >= 4 || Math.abs(dy) / Math.abs(dx) >= 4) {
                        dy = dy * -1;
                    } else {
                        dx = dx * 2;
                        dy = dy * -1;
                    }
                }
                catch (Exception e){

                }
            }
            else if (posnX + ballRadius < mPaddle.getThirdPointX()){
                dy = dy * -1;
            }
            else if (posnX + ballRadius < mPaddle.getRightMostx()){
                if(Math.abs(dx)/Math.abs(dy) >= 4){
                    dy = dy * -1;
                }
                else {
                    dx = dx * 2;
                    dy = dy * -1;
                }
            }
            else{
                int tmp = dx;
                dx = dy;
                dy = tmp;
            }

            if(dy >= 0){
                dy = -1;
            }

        }

        // check for bricks collision
        for(BrickItem mBrick : mBricks){
            if (!mBrick.isExist()){
                continue;
            }
            else if (posnX + ballRadius > mBrick.getLeftBorder().getX1() &&
                     posnX + ballRadius < mBrick.getRightBorder().getX1() &&
                     posnY + 2*ballRadius > mBrick.getTopBorder().getY1() &&
                     posnY < mBrick.getBottomBorder().getY1()) {
                System.out.println("collision!: up down now dx dy: " + dx + " " + dy);
                dy = dy * -1;
                System.out.println("          changed to dx dy: " + dx + " " + dy);
                mBrick.gone();
                double z = Math.sqrt(vectorLengthSq/(dy*dy + dx*dx));
                if(mBrick.getTopBorder().ptLineDist(ball2D) < mBrick.getBottomBorder().ptLineDist(ball2D)){
                    Double tmp = mBrick.getTopBorder().getY1() - ballRadius*2;
                    posnY = tmp.intValue();
                }
                else{
                    Double tmp = (mBrick.getBottomBorder().getY1());
                    posnY = tmp.intValue();
                }
                return;
                //break;
            }
            else if (posnY + ballRadius > mBrick.getTopBorder().getY1() &&
                     posnY + ballRadius < mBrick.getBottomBorder().getY1() &&
                     posnX + 2*ballRadius > mBrick.getLeftBorder().getX1() &&
                     posnX < mBrick.getRightBorder().getX1()) {
                System.out.println("collision!: left right now dx dy: " + dx + " " + dy);
                dx = dx * -1;
                System.out.println("          changed to dx dy: " + dx + " " + dy);
                mBrick.gone();
                double z = Math.sqrt(vectorLengthSq/(dy*dy + dx*dx));
                if(mBrick.getLeftBorder().ptLineDist(ball2D) < mBrick.getRightBorder().ptLineDist(ball2D)){
                    Double tmp = (mBrick.getLeftBorder().getX1() - ballRadius*2);
                    posnX = tmp.intValue();
                }
                else{
                    Double tmp = (mBrick.getRightBorder().getX1());
                    posnX = tmp.intValue();
                }
                return;
            }
            else if ((mBrick.getTopBorder().ptLineDist(ball2D) < ballRadius &&
                    mBrick.getRightBorder().ptLineDist(ball2D) < ballRadius &&
                    posnX + ballRadius > mBrick.getRightBorder().getX1() &&
                    posnY + ballRadius < mBrick.getTopBorder().getY1())){
                // if hit top-right corner
                System.out.println("collision!: corners now dx dy: " + dx + " " + dy);
                int tmp = dx;
                dx = dy;
                dy = tmp;
                System.out.println("          changed to dx dy: " + dx + " " + dy);
                mBrick.gone();
                break;
            }
            else if ((mBrick.getTopBorder().ptLineDist(ball2D) < ballRadius &&
                    mBrick.getLeftBorder().ptLineDist(ball2D) < ballRadius &&
                    posnX + ballRadius < mBrick.getLeftBorder().getX1() &&
                    posnY + ballRadius < mBrick.getTopBorder().getY1())){
                // if hit top-left corner
                System.out.println("collision!: corners now dx dy: " + dx + " " + dy);
                int tmp = dx;
                dx = dy;
                dy = tmp;
                System.out.println("          changed to dx dy: " + dx + " " + dy);
                mBrick.gone();
                break;
            }
            else if((mBrick.getBottomBorder().ptLineDist(ball2D) < ballRadius &&
                    mBrick.getLeftBorder().ptLineDist(ball2D) < ballRadius &&
                    posnX + ballRadius < mBrick.getLeftBorder().getX1() &&
                    posnY + ballRadius > mBrick.getBottomBorder().getY1())){
                // if hit bottom-left corner
                System.out.println("collision!: corners now dx dy: " + dx + " " + dy);
                int tmp = dx;
                dx = dy;
                dy = tmp;
                System.out.println("          changed to dx dy: " + dx + " " + dy);
                mBrick.gone();
                break;
            }
            else if((mBrick.getBottomBorder().ptLineDist(ball2D) < ballRadius &&
                    mBrick.getRightBorder().ptLineDist(ball2D) < ballRadius &&
                    posnX + ballRadius > mBrick.getRightBorder().getX1() &&
                    posnY + ballRadius > mBrick.getBottomBorder().getY1())){
                // if hit bottom-right corner
                System.out.println("collision!: corners now dx dy: " + dx + " " + dy);
                int tmp = dx;
                dx = dy;
                dy = tmp;
                System.out.println("          changed to dx dy: " + dx + " " + dy);
                mBrick.gone();
                break;
            }
        }

        if(dy >= 17){
            dy = 1;
        }
        //System.out.println("********************changed to dx dy: " + dx + " " + dy);

        // update position
        double z = Math.sqrt(vectorLengthSq/(dy*dy + dx*dx));
        posnX += z*dx*speedFactor;
        posnY += z*dy*speedFactor;




    }

    protected void paintComponent(Graphics2D g2, int width, int height) {
        ballRadius = frameWidth / 100;
        frameWidth = width;
        frameHeight = height;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(5));           // 32 pixels wide
        g2.setColor(ballColor);

        if(!mGameBoard.isStarted()){
            setInitPosition();
        }

        Line2D paddleTop = mPaddle.getTopBorder();
        Double x1 = mPaddle.getUpperLeftPoint().getX();
        Double y1 = mPaddle.getUpperLeftPoint().getY();
        Double x2 = mPaddle.getUpperRightPoint().getX();
        Double y2 = mPaddle.getUpperRightPoint().getY();

        g2.drawImage(mBallImage, posnX, posnY, ballRadius*2, ballRadius*2, null);
        Toolkit.getDefaultToolkit().sync();
        //g2.fillOval(posnX, posnY, ballRadius*2, ballRadius*2);
        //Toolkit.getDefaultToolkit().sync();
    }


    protected int getBallPosnX(){
        return posnX;
    }

    protected int getBallPosnY(){
        return posnY;
    }

    protected void setBallPosn(int x, int y){
        posnX = x;
        posnY = y;
    }

    protected int getFrameWidth(){
        return frameWidth;
    }

    protected int getFrameHeight(){
        return frameHeight;
    }
}
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jason on 1/21/2016.
 */
public class Bricks {
    private int mGameLevel;
    private GameBoard mGameBoard;
    private int brickNumWidth = 10;
    private int brickNumHeight = 5;
    private int maxBrickWidthNum;
    private int frameWidth;
    private int frameHeight;
    private int brickWidth;
    private int brickHeight;
    private int brickArcDiameter;
    private int brickGap;
    private int startPosnX, startPosnY;
    private ArrayList<BrickItem> mBrickArray;

    public Bricks(GameBoard gameBoard, int gameLevel) {
        mGameLevel = gameLevel;
        mGameBoard = gameBoard;
        frameWidth = mGameBoard.getWidth();
        frameHeight = mGameBoard.getHeight();
        brickWidth = frameWidth/12;
        brickHeight = brickWidth/3;
        brickGap = frameWidth/200;
        startPosnX = (frameWidth - (brickNumWidth * brickWidth) - (brickNumWidth - 1) * brickGap) / 2;
        startPosnY = frameHeight/8;

        mBrickArray = new ArrayList<>();
        int curPosnX = startPosnX;
        int r = 220;
        int g = 250;
        int b = 15;

        if(gameLevel == 1) {
            for (int i = 0; i < brickNumHeight; i++) {
                Color mColor = new Color(r, g, b);
                for (int j = 0; j < brickNumWidth; j++) {
                    mBrickArray.add(new BrickItem(mGameBoard, brickWidth, brickHeight, curPosnX, startPosnY, mColor));
                    curPosnX += brickGap + brickWidth;
                }
                r -= 20;
                g -= 20;
                b += 10;
                startPosnY += brickGap + brickHeight;
                curPosnX = startPosnX;
            }
        }
        else if(gameLevel == 2){
            maxBrickWidthNum = 7;
            for (int i = 1; i <= maxBrickWidthNum; i++){
                Color mColor = new Color(r, g, b);
                curPosnX = (frameWidth - (i * brickWidth) - (i - 1) * brickGap) / 2;
                for (int j = 1; j <= i; j++){
                    mBrickArray.add(new BrickItem(mGameBoard, brickWidth, brickHeight, curPosnX, startPosnY, mColor));
                    curPosnX += brickGap + brickWidth;
                }
                r -= 20;
                g -= 20;
                b += 10;
                startPosnY += brickGap + brickHeight;
            }
        }
    }

    protected void paintComponent(Graphics2D g2, int width, int height) {
        frameWidth = width;
        frameHeight = height;
        brickWidth = frameWidth/12;
        brickHeight = brickWidth/3;
        brickGap = frameWidth/200;
        startPosnX = (frameWidth - (brickNumWidth * brickWidth) - (brickNumWidth - 1) * brickGap) / 2;
        startPosnY = frameHeight/8;
        brickArcDiameter = frameWidth/60;
        int curPosnX = startPosnX;
        if (mGameLevel == 1) {
            for (int i = 0; i < brickNumHeight; i++) {
                for (int j = 0; j < brickNumWidth; j++) {
                    mBrickArray.get(i * brickNumWidth + j).paintComponent(g2, brickWidth, brickHeight, brickGap, curPosnX, startPosnY, brickArcDiameter);
                    curPosnX += brickGap + brickWidth;
                }
                startPosnY += brickGap + brickHeight;
                curPosnX = startPosnX;
            }
        }
        else if (mGameLevel == 2){
            int index = 0;
            for (int i = 1; i <= maxBrickWidthNum; i++){
                curPosnX = (frameWidth - (i * brickWidth) - (i - 1) * brickGap) / 2;
                for (int j = 1; j <= i; j++){
                    mBrickArray.get(index).paintComponent(g2, brickWidth, brickHeight, brickGap, curPosnX, startPosnY, brickArcDiameter);
                    index++;
                    curPosnX += brickGap + brickWidth;
                }
                startPosnY += brickGap + brickHeight;
            }
        }
    }

    protected ArrayList<BrickItem> getBricks(){
        return mBrickArray;
    }

    protected Boolean isEmpty(){
        for (BrickItem brick : mBrickArray){
            if (brick.isExist() == true){
                return false;
            }
        }
        return true;
    }
}

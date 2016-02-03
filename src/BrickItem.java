import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by jason on 1/21/2016.
 */
public class BrickItem {
    private int frameWidth;
    private int frameHeight;
    private GameBoard mGameboard;
    private Color color;
    private boolean exist = true;
    private int brickWidth;
    private int brickHeight;
    private int brickGap;
    private int startPosnX;
    private int startPosnY;
    private int brickArcDiameter;


    public BrickItem(GameBoard gameboard, int width, int height, int startPosnX, int startPosnY, Color color) {
        this.mGameboard = gameboard;
        this.startPosnX = startPosnX;
        this.startPosnY = startPosnY;
        this.color = color;
        this.brickWidth = width;
        this.brickHeight = height;
    }

    protected void paintComponent(Graphics2D g2, int width, int height, int gap, int posnX, int posnY, int diameter) {
        if (!exist) {
            return;
        }
        brickWidth = width;
        brickHeight = height;
        startPosnX = posnX;
        startPosnY = posnY;
        brickGap = gap;
        brickArcDiameter = diameter;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(5));           // 32 pixels wide
        g2.setColor(color);

        //System.out.println(posnX + " " + posnY);
        //g2.fill3DRect(startPosnX, startPosnY, brickWidth, brickHeight, true);
        g2.fillRoundRect(startPosnX, startPosnY, width, height, brickArcDiameter, brickArcDiameter);
    }

    protected void gone(){
        System.out.println("i'm gone!!!");
        exist = false;
        mGameboard.addScore(1);
    }

    protected boolean isExist(){
        return exist;
    }

    protected Line2D getTopBorder(){
        return new Line2D.Double(startPosnX, startPosnY, startPosnX + brickWidth, startPosnY);
    }

    protected Line2D getBottomBorder(){
        return new Line2D.Double(startPosnX, startPosnY + brickHeight, startPosnX + brickWidth, startPosnY + brickHeight);
    }

    protected Line2D getLeftBorder(){
        return new Line2D.Double(startPosnX, startPosnY, startPosnX, startPosnY + brickHeight);
    }

    protected Line2D getRightBorder(){
        return new Line2D.Double(startPosnX + brickWidth, startPosnY, startPosnX + brickWidth, startPosnY + brickHeight);
    }

    protected int getArcRadius(){
        return brickArcDiameter/2;
    }
}

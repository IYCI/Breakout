import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by jason on 1/19/2016.
 */
public class GameBoard extends JComponent implements KeyListener, MouseMotionListener, MouseListener, ComponentListener{
    private final int MAX_SPEED = 7;

    private int mFrames = 60;
    private Paddle mPaddle;
    private Ball mBall;
    private Bricks mBricks;
    private int mGameLevel = 1;
    private Boolean firstTime = true;
    private Boolean start = false;
    private Boolean resizing = false;
    private Boolean gameOver = false;
    private int frameWidth, frameHeight;
    private int speedFactor = 2;
    private int score = 0;
    private int lives = 3;
    private BufferedImage mLifeImage;

    private int mouseX, mouseY;

    public GameBoard(int gameLevel, int frameRate, int speed) {
        mGameLevel = gameLevel;
        mFrames = frameRate;
        speedFactor = speed;

        mPaddle = new Paddle(this, Color.CYAN);
        mBricks = new Bricks(this, mGameLevel);
        mBall = new Ball(this, mPaddle, Color.RED);



        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addComponentListener(this);

        try {
            mLifeImage = ImageIO.read(new File("src/heart.png"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        frameWidth = this.getWidth();
        frameHeight = this.getHeight();
        Graphics2D g2 = (Graphics2D) g;               // 2D drawing
        if (firstTime) {
            // start Timer
            Timer mTimer = new Timer(1000 / mFrames, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    repaint();
                }
            });
            mTimer.start();

            Timer mTimer2 = new Timer(1000 / 100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //mPaddle.update();
                    if(start){
                        mBall.move();
                    }
                    if(mBricks.isEmpty() || lives <= 0){
                        start = false;
                        gameOver = true;
                    }
                }
            });
            mTimer2.start();
            firstTime = false;
        }



        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(32));           // 32 pixels wide
        g2.setColor(Color.BLACK);                     // make it blue

        // draw black background
        g2.fillRect(0 ,0, frameWidth, frameHeight);


        // *** display #1, mouse location ***
        //g2.setColor(Color.YELLOW);
        //String mouse = "Mouse: " + mouseX + ", " + mouseY;
        //g2.drawString(mouse, frameWidth/3, 40);
        // *** display #2, speed ***
        Double relativeX = MouseInfo.getPointerInfo().getLocation().getX() - this.getLocationOnScreen().getX();
        String speedLabel = "Speed: " + speedFactor;
        g2.setColor(Color.BLUE);
        g2.setFont(new Font("Roboto", Font.PLAIN, frameHeight/38));
        g2.drawString(speedLabel, frameWidth/5, frameHeight/15);

        // score display
        g2.setColor(Color.PINK);
        g2.setFont(new Font("Roboto", Font.PLAIN, frameHeight/38));
        g2.drawString("Score:   " + score, frameWidth*2/5, frameHeight/15);

        // lives display
        int heartStartPosnX = frameWidth*4/5 - frameWidth/110;
        for(int i = 0; i < lives; i++){
            g2.drawImage(mLifeImage, heartStartPosnX, frameHeight/15 - frameWidth/100, frameWidth/55, frameWidth/55, null);
            heartStartPosnX += frameWidth/45;
        }

        mBall.paintComponent(g2, frameWidth, frameHeight);
        mBricks.paintComponent(g2, frameWidth, frameHeight);
        mPaddle.paintComponent(g2, frameWidth, frameHeight);

        if (gameOver){
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.80f));
            g2.setColor(Color.GRAY);
            g2.fillRoundRect(frameWidth/4, frameHeight/7, frameWidth/2, frameHeight*5/7, frameHeight/15, frameHeight/15);
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.90f));

            g2.setFont(new Font("Roboto", Font.PLAIN, frameHeight/10));
            g2.setColor(Color.RED);
            g2.drawString("Game Over!!!", frameWidth/3, frameHeight/4);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Roboto", Font.PLAIN, frameHeight/12));
            g2.drawString("Score:  " + (score + lives*10), frameWidth/3, frameHeight/2);

            Toolkit.getDefaultToolkit().sync();
        }
    }

    protected void lostLive(){
        lives -= 1;
    }

    protected void stop(){
        start = false;
    }

    protected boolean isStarted(){
        return start;
    }

    protected int getSpeedFactor(){
        return speedFactor;
    }

    protected Bricks getBricks(){
        return mBricks;
    }

    protected boolean isResizing(){
        return resizing;
    }

    protected int getScore(){
        return score;
    }

    protected void addScore(int count){
        score += count;
    }




    /*** implements KeyListener ***/
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Pressed " + e.getExtendedKeyCode());
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            mPaddle.moveLeft();
            System.out.println("left pressed");
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            mPaddle.moveRight();
            System.out.println("right pressed");
        }
        else if(e.getKeyCode() == KeyEvent.VK_MINUS){
            if(speedFactor - 1 < 1) return;
            else speedFactor -= 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_EQUALS){
            if(speedFactor + 1 > MAX_SPEED) return;
            else speedFactor += 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_3){
            gameOver = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            start = true;
        }
    }


    /*** implements MouseMotionListener ***/
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            mouseX = e.getX();
            mouseY = e.getY();
            mPaddle.updateWithMouse(mouseX);
        }
    }
    @Override
    public void mouseDragged(MouseEvent e) {

    }


    /*** implements MouseInputListener ***/
    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("mouse releases");
        if (SwingUtilities.isLeftMouseButton(e)){
            start = true;
        }
        else if(SwingUtilities.isRightMouseButton(e)){
            start = false;
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mousePressed(MouseEvent e) {

    }


    /*** implements ComponentListener ***/
    @Override
    public void componentResized(ComponentEvent e) {
        start = false;
        resizing = true;
        int oldWdith = mBall.getFrameWidth();
        int oldHeight = mBall.getFrameHeight();
        int ballX = mBall.getBallPosnX();
        int ballY = mBall.getBallPosnY();

        if(oldHeight == 0 || oldWdith == 0) return;
        mBall.setBallPosn(ballX * getWidth() / oldWdith,
                ballY * getHeight() / oldHeight);
        //System.out.println("set: " + (int)mBall.getBallPosn().getKey() * getWidth() / (int)oldFrame.getKey()
         //       + ", " + (int)mBall.getBallPosn().getValue() * getHeight() / (int)oldFrame.getValue());
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JLabel;
import javax.swing.JFrame;

public class Board extends JPanel implements Runnable, KeyListener {

    private Image tile;
    private Image playerIcon;
    private Image hTileIcon;
    
    public String running = "game";
    
    static int columns = 6;
    static int rows = 6;
    
    static int tileSize = 48;
    
    public int maxHighlightedTiles = (rows * columns) / 9; //tiles divided by 9 is the max amount of highlighted tiles
    public int highlightedTileSpeed = 14;
    public int highlightedTileCounter = 0;
    public int highlightedTileAmount = 1;
    
    static int newHighlightedTileSpeed = 4000;
    
    
    private final int DELAY = 25;
    
    public double timeLasted = 0;
    

    
    
    Entity player = new Entity();
    Entity[] hTileArray = new Entity[maxHighlightedTiles];
    
    
    JLabel endText = new JLabel("Seconds Lasted: ");
    JLabel infoText = new JLabel("You'd better watch out! Maneuver yourself away from the randomly moving electric tile to survive as long as possible. Controls: Move - WASD or Arrow Keys");
    
    
    KeyCheck keyCheck = new KeyCheck();
    

    
    private Thread animator;

    public Board() {

        initBoard();
    }
    
    private void initBoard() {
        
        for (int i = 0; i < hTileArray.length; i++) {
            hTileArray[i] = new Entity();
            hTileArray[i].iconPath = "gamehighlight.png";
        }
        for (int i = 0; i < highlightedTileAmount; i++) {
            while(hTileArray[i].x == player.x && hTileArray[i].y == player.y) {
                hTileArray[i].x = (int)(Math.random() * rows);
                hTileArray[i].x *= tileSize;
                hTileArray[i].y = (int)(Math.random() * columns);
                hTileArray[i].y *= tileSize;
            }
        }
        
        player.iconPath = "gameplayer.png";
        
        loadImage();
        
        
        int w = (tile.getWidth(this)) * rows;
        int h = (tile.getHeight(this)) * columns;
        setPreferredSize(new Dimension(w, h));
        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
        add(endText);
        endText.setVisible(false);
        
    }
    
    public void keyPressed(KeyEvent e) {
        if(running == "game") {
            if ((e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) && player.y >= tileSize) {

                player.y -= tileSize;
            } else if ((e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) && player.x >= tileSize) {

                player.x -= tileSize;
            } else if ((e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) && player.y < tileSize * (columns - 1)) {

                player.y += tileSize;
            } else if ((e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) && player.x < tileSize * (columns - 1)) {

                player.x += tileSize;
            }
        }
    }
    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    
    private void loadImage() {
        
        ImageIcon ii = new ImageIcon("tile.png");
        tile = ii.getImage();
        ImageIcon iii = new ImageIcon(player.iconPath);
        playerIcon = iii.getImage();
        ImageIcon iv = new ImageIcon(hTileArray[0].iconPath);
        hTileIcon = iv.getImage();
        
        
    }
    
    public void moveHighlighted() {
        
        
        
        
        
        //if new location is the same as a different tile, change location
        for (int i = 0; i < highlightedTileAmount; i++) {

            hTileArray[i].moveLocation();
            
            for (int j = 0; j < highlightedTileAmount; j++) {
                while (hTileArray[j].x == hTileArray[i].x && hTileArray[j].y == hTileArray[i].y && j != i) {
                    
                    if (hTileArray[i].moveDirection == "up") {
                        hTileArray[i].upTaken = true;
                    } else if (hTileArray[i].moveDirection == "down") {
                        hTileArray[i].downTaken = true;
                    } else if (hTileArray[i].moveDirection == "left") {
                        hTileArray[i].leftTaken = true;
                    } else if (hTileArray[i].moveDirection == "right") {
                        hTileArray[i].rightTaken = true;
                    }
                    
                    hTileArray[i].x = hTileArray[i].lastX;
                    hTileArray[i].y = hTileArray[i].lastY;
                    
                    if(hTileArray[i].upTaken == false || hTileArray[i].downTaken == false || hTileArray[i].leftTaken == false || hTileArray[i].rightTaken == false) {
                        hTileArray[i].moveLocation();
                    }
                }
            }
        }
        for (int i = 0; i < highlightedTileAmount; i++) {
            hTileArray[i].upTaken = false;
            hTileArray[i].downTaken = false;
            hTileArray[i].leftTaken = false;
            hTileArray[i].rightTaken = false;
        }
    }
    public void setEndScreen() {
        
        endText.setText("Seconds Lasted: " + Integer.toString((int)(timeLasted / 1000)));
        endText.setForeground(Color.red);
        endText.setVisible(true);
    }
    
    
    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }
    
    @Override
    public void paintComponent(Graphics g) {

        int tileX = 0;
        int tileY = 0;
        
        while(tileY < tileSize * columns) {
            
            g.drawImage(tile, tileX, tileY, null);
            
            tileX += tileSize;
            
            if(tileX >= tileSize * rows) {
                tileX = 0;
                tileY += tileSize;
            }
        }
        
        if(running == "game" || running == "deathScreen") {
            for (int i = 0; i < highlightedTileAmount; i++) {
                g.drawImage(hTileIcon, hTileArray[i].x, hTileArray[i].y, null);
            }
            g.drawImage(playerIcon, player.x, player.y, null);
        }

        
    }
    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {
            

            repaint();
            
            if (running == "game") {
                
                timeLasted += 25;
                

                if(timeLasted >= highlightedTileAmount * highlightedTileAmount * newHighlightedTileSpeed && highlightedTileAmount < maxHighlightedTiles) {
                    highlightedTileAmount++;
                    while(hTileArray[highlightedTileAmount-1].x == player.x && hTileArray[highlightedTileAmount-1].y == player.y) {
                        hTileArray[highlightedTileAmount-1].x = (int)(Math.random() * rows);
                        hTileArray[highlightedTileAmount-1].x *= tileSize;
                        hTileArray[highlightedTileAmount-1].y = (int)(Math.random() * columns);
                        hTileArray[highlightedTileAmount-1].y *= tileSize;
                    }
                    
                    
                }
                
                
                highlightedTileCounter++;
                if(highlightedTileCounter >= highlightedTileSpeed){

                    moveHighlighted();
                    highlightedTileCounter = 0;
                }
                
                for (int i = 0; i < highlightedTileAmount; i++) {
                    if(player.x == hTileArray[i].x && player.y == hTileArray[i].y) {

                        running = "deathScreen";
                        setEndScreen();

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {

                            String msg = String.format("Thread interrupted: %s", e.getMessage());

                            JOptionPane.showMessageDialog(this, msg, "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }


                    }
                }
            }
            

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {

                String msg = String.format("Thread interrupted: %s", e.getMessage());

                JOptionPane.showMessageDialog(this, msg, "Error",
                    JOptionPane.ERROR_MESSAGE);
            }

            beforeTime = System.currentTimeMillis();
        }
    }
}













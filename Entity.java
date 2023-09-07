import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Entity {
    
    int x = 0;
    int y = 0;
    
    int xChange = 0;
    int yChange = 0;
    
    String iconPath = "gameplayer.png";
    
    int lastX = 0;
    int lastY = 0;
    
    int chance = 0;
    
    boolean upTaken = false;
    boolean downTaken = false;
    boolean leftTaken = false;
    boolean rightTaken = false;
    
    
    String moveDirection = "";
    
    public void moveLocation() {
        do {
            
            chance = randomNum();

            xChange = 0;
            yChange = 0;

            if(chance == 1) {
                xChange = Board.tileSize;
                moveDirection = "right";
            } else if(chance == 2) {
                yChange = Board.tileSize;
                moveDirection = "down";
            } else if(chance == 3) {
                xChange = -Board.tileSize;
                moveDirection = "left";
            } else if(chance == 4) {
                yChange = -Board.tileSize;
                moveDirection = "up";
            }
        }
        while (x + xChange < 0 || x + xChange >= Board.rows * Board.tileSize || y + yChange < 0 || y + yChange >= Board.columns * Board.tileSize);

        //System.out.println(hTileArray[0].y + yChange);
        //System.out.println("x" + Integer.toString(hTileArray[0].x + xChange));
        lastX = x;
        lastY = y;
        x += xChange;
        y += yChange;
    }
    
    static int randomNum() {
        return (int) (Math.random() * 4) + 1;
    }
    
}



















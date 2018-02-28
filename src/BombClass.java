// The "BombClass" class.
import java.awt.*;
//import hsa.Console;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// Bomb inherits ObjectClass as they are objects
public class BombClass extends ObjectClass
{
    // File that stores all images to be used for drawing bombs, All bomb instances use the same file.
    private static File spriteSheetFile= new File("BombSprites.png");

    // image that sotres the spritesheet
    private static BufferedImage spriteSheet=new BufferedImage(120,40,BufferedImage.TYPE_INT_ARGB);
    // stores if the image has been read yet
    private static boolean spriteSheetInit=false;
    
    // time remaining before the bomb automatically explodes
    private int countDown=3000;
    // owner of the bomb, the player who placed the bomb
    private int player=0;
    // array of sprites to be used for drawing. All bomb instances use the same array.
    private static BufferedImage[] sprites=new BufferedImage[3];
    
    private int bombPower=1;            // power of the bomb
    private boolean isMoving;           // sotres if the bomb is moving

    // constructor for bomb given the player, bomb power, and coordinates
    BombClass(int p,int pow,int x, int y){
        super.setCoordinates(x,y);
        xDist=16;
        yDist=16;
        speed=100;
        width=32;
        height=32;
        setPlayer(p);
        setBombPower(pow);
        isMoving=false;
        countDown=3000;
        // if images files have not yet been read, the images files will be read
        if (!spriteSheetInit){
            initializeSpriteSheet();
            spriteSheetInit=true;
        } 
    }
    
    // constructor for bomb object given no parameters
    BombClass(){
        super.setCoordinates(160,160);
        xDist=16;
        yDist=16;
        speed=100;
        width=32;
        height=32;
        player=0;
        bombPower=1;
        isMoving=false;
        countDown=3000;
        if (!spriteSheetInit){
            initializeSpriteSheet();
            spriteSheetInit=true;
        }      
    }
    
    // Reads image file and saves to spritesheet
    public void initializeSpriteSheet(){
        try
        {
            spriteSheet = ImageIO.read (spriteSheetFile);
        }
        catch (IOException e)
        {
            System.out.println("ERRORRRRRRRR Bomb spritesheet not found");
        }        
        setupSprites();
    }    
    
    // assigns sprites to the image array
    public void setupSprites(){
        for(int i=0;i<3;i++){
            sprites[i]=new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);    
            sprites[i]=spriteSheet.getSubimage(i*32,6,32,32);
        }
    }
    
    // draws bomb based on their countdown timer
    public void draw(Graphics g){
        int s=0;
        if((countDown/200)%2==1){
            s=1;
        }
        else if((countDown/200)%4==2){
            s=2;
        }
        g.drawImage (sprites[s], TL_CORNERX+pixelLocationX-xDist, TL_CORNERY+pixelLocationY-yDist, null); 
    }
    
    // sets image File containing the bomb spritesheet
    public void setSpriteSheetFile(File f){
        spriteSheetFile=f;
    }
    
    // returns image file containing the bomb spritesheet
    public File getSpriteSheetFile(){
        return spriteSheetFile;
    }
    
    // decreases the bomb countdown by n
    public void lowerCountDown(int n){
        countDown-=n;
    }
    
    //returns the bomb countdown
    public int getCountDown(){
        return countDown;
    }
    
    // sets the power of the bomb
    public void setBombPower(int n){
        if(n>0){
            bombPower=n;
        }
    }
    
    // gets the bomb power
    public int getBombPower(){
        return bombPower;
    }
    
    // gets if the bomb is moving
    public boolean getIsMoving(){
        return isMoving;
    }
    
    // sets if the bomb is moving
    public void setIsMoving(boolean b){
        isMoving=b;
    }
    
    // sets the player that placed the bomb
    public void setPlayer(int n){
        if(n>=0)
            player=n;
    }
    
    // returns the player that placed the bomb
    public int getPlayer(){
        return player;
    }
} // BombClass class

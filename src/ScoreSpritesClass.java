// The "ScoreSprites" class.
import java.awt.*;
//import hsa.Console;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;


// objects that writes the score of eahc player using images
public class ScoreSpritesClass
{
    // Image file that stores the spritesheet
    private static URL spriteFile = ScoreSpritesClass.class.getClassLoader().getResource("scores.png");
    // Sprite sheet
    private static BufferedImage spriteSheet = new BufferedImage(235,35,BufferedImage.TYPE_INT_ARGB); 
    // width of each image 
    private static int spriteWidth = 16;
    // array that stores the image for each digit
    private static BufferedImage[] sprites = new BufferedImage[10];
    // stores if the image file has been read or not
    private static boolean variablesInitialized = false;
    
    // constructor that assigns value to image array
    ScoreSpritesClass(){
        if(!variablesInitialized){
            initSpriteSheet();
            variablesInitialized=true;
        }
    }
    
    // reads image file and saves all images into the array
    public void initSpriteSheet(){
        try
        {
            spriteSheet = ImageIO.read (spriteFile);
        }
        catch (IOException e)
        {
            System.out.println("ERRORRRRRRRR score spritesheet not found");
        } 
        for(int i=0;i<10;i++){
            sprites[i]= new BufferedImage(spriteWidth,30,BufferedImage.TYPE_INT_ARGB);
            sprites[i]= spriteSheet.getSubimage(20*i,0,spriteWidth,30);            
        }
    }
    
    // writes score onto the screen at a given x and y value
    public void writeScore(Graphics g,int s,int x, int y){
        int score=s;
        // scores must be from 0 to 99
        if(score>100){
            score=99;
        }
        if(score>=0){
            g.drawImage(sprites[score/10],x,y,null);        
            g.drawImage(sprites[score%10],x+spriteWidth,y,null);            
        }
    }
    
    // writs a number onto thescreen at a given x and y value
    public void writeNumber(Graphics g, int i, int x, int y){
        if(i>=0&&i<10){
            g.drawImage(sprites[i],x,y,null);
        }    
    }
    
} // ScoreSprites class

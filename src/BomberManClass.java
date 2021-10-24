// The "BomberManClass" class.
import java.awt.*;
//import hsa.Console;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// Bomberman inherits ObjectClass as they are objects.
public class BomberManClass extends ObjectClass
{

    // Files that store all images to be used for drawing the Bombermen. They are static because all instances will use the same file.
    private static URL spriteSheetFile=BomberManClass.class.getClassLoader().getResource("SpriteSheetDraft15.png");
    private static URL playerIconFile=BomberManClass.class.getClassLoader().getResource("PlayerIconSpritesDraft.png");
    // Images that store the spriteSheets for the Bombermen. 
    private static BufferedImage spriteSheet=new BufferedImage(1000,1000,BufferedImage.TYPE_INT_ARGB);
    private static BufferedImage iconSheet=new BufferedImage(100,125,BufferedImage.TYPE_INT_ARGB);
    
    private static int iconSize=30;                     // stores the pixel width of the player icon
    private static boolean variablesInitialized=false;  // stores whether or not the ImageFile has been read yet

    private int playerColor;            // player Color, also used to determine team
    private int player;                 // player number
    
    // Array of Images to store the sprites this bomberman will use
    private BufferedImage [][] sprites=new BufferedImage[5][4];
    private BufferedImage [] playerIcon=new BufferedImage[2];

    private boolean isAlive;            // stores if the player is alive 
    private boolean isDying;            // store sif player is performing death animation
    private int animationCounter=0;     // counter used for animating the sprites
    // speed of Bomberman during its death animation
    private int dyingMovements=-60;
    // final speed of Bomberman during its death animation
    private int lastDyingMovement=60;
    
    private int score;                  // score, number of rounds won      
    
    private int skullTimerStart=20000;  // Duration of the skull Powerup
    private int skullTimer=0;           // Time remaining with the skull Powerup
    private int skullType=0;            // Skull Type (3 possible types)
    
    private int bombPower;              // store power of bombs
    private int speedPower;             // store speed level of bomberman
    private int maxBombs;               // stores the maximum number of bombs bomberman can place
    
    private static int maxBombPower;            // stores maximum power of bombs
    private static int maxSpeedPower;           // stores the maximum speed level for bombermen
    // array of integers assigning each speed level a specific speed
    private static int[] speedList=new int[8];

    private int bombsPlaced=0;          // stores the number of bombs Bomberman currently has in the field
    private boolean canKick;            // stores if the bomberman can kick bombs
    private boolean isMoving;           // stores if the bomberman is moving

    // Is called by constructor methods and reset methods. Assigns values to each variable
    public void initBomberManClass(){
        xCoordinate=160;
        yCoordinate=160;
        pixelLocationX=16;
        pixelLocationY=16;
        xDist=16;
        yDist=42;
        direction=0;
        speed=40;       
        height=52;
        width=30; 
        isAlive=true;
        isDying=false;
        bombPower=1;
        speedPower=1;
        maxBombs=1;
        maxBombPower=10;
        maxSpeedPower=8;
        bombsPlaced=0;
        canKick=false;
        isMoving=false;
        skullTimer=0;
        setSkullType(0);
        skullTimerStart=20000;
        animationCounter=0;
        dyingMovements=-60;
        lastDyingMovement=60;
        // if images files have not yet been read, the images files will be read
        if (!variablesInitialized){
            initializeSpriteSheet();
            assignSpeeds();
            variablesInitialized=true;
        }
    }    
    
    // constructor given a specific player number and color.
    BomberManClass(int i,int n){
        if(n>=0&&n<4){
            playerColor=n;
        }
        else{
            playerColor=0;
        }
        player=i;
        score=0;
        // initializes all other variables
        initBomberManClass();
        // sets array of sprites
        setupSprites();
    }
    
    // constructor given no parameters
    BomberManClass(){
        playerColor=0;
        player=0;
        score=0;
        initBomberManClass();
        setupSprites();
    }
    
    // resets everything in the bomberman (Except player, score, and team)  
    public void resetPlayer(){
        initBomberManClass();
    }
    
    // updates the speed of the bomberman based on the current speed level of the bomberman
    public void assignSpeeds(){
        speedList[0]=40;
        speedList[1]=50;
        speedList[2]=65;
        speedList[3]=80;
        speedList[4]=95;
        speedList[5]=110;
        speedList[6]=125;
        speedList[7]=140;
    }
    
    // initializes all spreadSheets and reads all image files
    public void initializeSpriteSheet(){
        try
        {
            spriteSheet = ImageIO.read (spriteSheetFile);
            iconSheet=ImageIO.read(playerIconFile);
        }
        catch (IOException e)
        {
            System.out.println("ERRORRRRRRRR Bomberman spritesheet not found");
        }        
    }
    
    
    // sets up the array of sprites to be used for drawing
    public void setupSprites(){        
        for(int i=0;i<4;i++){
            for(int j=0;j<3;j++){
                sprites[i][j] = new BufferedImage(30,52,BufferedImage.TYPE_INT_ARGB);
                sprites[i][j] = spriteSheet.getSubimage (36*((i*3)+j), 54*playerColor, 30, 52);                    
            }
        }
        for(int i=0;i<4;i++){
            sprites[i][3] = new BufferedImage(30,52,BufferedImage.TYPE_INT_ARGB);
            sprites[i][3] = spriteSheet.getSubimage (36*(12+i), 54*playerColor, 30, 52);           
        }
        for(int i=0;i<4;i++){
            sprites[4][i] = new BufferedImage(30,52,BufferedImage.TYPE_INT_ARGB);
            sprites[4][i] = spriteSheet.getSubimage (36*(16+i), 54*playerColor, 30, 52); 
        }

        for(int i=0;i<2;i++){
            playerIcon[i] = new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
            playerIcon[i] = iconSheet.getSubimage(30*i, 31*playerColor, 30, 30);
        }
    }

    // draws the sprite 
    public void draw(Graphics g){
        //preventative meausre in case the animation counter appreoaches maxint
        if(animationCounter>=72800){    
            animationCounter=0;
        }
        if(isDying){            // dying animation
            // if the dying animation is completed, isDying is set to false
            if(dyingMovements>lastDyingMovement){
                g.drawImage (sprites[4][3],TL_CORNERX+pixelLocationX-xDist, TL_CORNERY+pixelLocationY-yDist, null); 
                if(animationCounter==22){
                    isDying=false;
                }
            }
            else{
            // the dying animation makes the bomberman jump and fall in a parbolic shape.
            setCoordinates(xCoordinate,yCoordinate+dyingMovements);
            dyingMovements+=10; 
            g.drawImage (sprites[4][(animationCounter)/4],TL_CORNERX+pixelLocationX-xDist, TL_CORNERY+pixelLocationY-yDist, null);             
            }
            increaseAnimationCounter();
        }
        else if(isAlive){       // animation when alive
            int spriteNum;
            // if the bomberman is moving, the sprite to use is based on the how long the unit is walking
            if(isMoving){       
                if((animationCounter/6)%2==1){
                    spriteNum=1;
                }
                else{
                    spriteNum=(animationCounter/6)%4;
                }
            }
            else{
                spriteNum=1;    // idle standing sprite
            }
            g.drawImage (sprites[direction][spriteNum],TL_CORNERX+pixelLocationX-xDist, TL_CORNERY+pixelLocationY-yDist, null); 
        }
    }   

    public void draw(Graphics g,int x,int y,int i,int j){
        g.drawImage(sprites[i][j],x,y,null); 
    }
    
    // draws an icon given an x and y coordinate
    public void drawIcon(Graphics g,int x,int y){
        if (isAlive){
            g.drawImage(playerIcon[0],x,y,null);
        }
        else{
            g.drawImage(playerIcon[1],x,y,null);        
        }
    }
    
    // draws an icon given an x and y coordinate and boolean value.
    // boolean value tells which icon to draw. 
    public void drawIcon(Graphics g, int x, int y, boolean bo){
        if (bo){
            g.drawImage(playerIcon[0],x,y,null);
        }
        else{
            g.drawImage(playerIcon[1],x,y,null);        
        }        
    }
    
    // overides move() in objectClass. calls move method in abstract class
    public void move(){
        increaseAnimationCounter();
        isMoving=true;
        super.move();
    }
    
    // sets the animation counter
    public void setAnimationCounter(int n){
        if (n>=0){
            animationCounter=n;
        }
    }
    
    // increments the animation counter by one
    public void increaseAnimationCounter(){
        animationCounter++;    
    }
    
    // returns the animation counter
    public int getAnimationCounter(){
        return animationCounter;
    }
    
    // sets the direction of the bomberman, 
    public void setDirection(int d){
        //makes sure the bomberman actually changes direction before reseting the direction
        if(d!=direction){
            setAnimationCounter(0);
            super.setDirection(d);
        }
    }    
    
    // sets the player number
    public void setPlayer(int i){
        if(i>=0){    
            player=i;
        }
    }
    
    // returns the player number
    public int getPlayer(){
        return player;
    }
    
    // set the player color
    public void setPlayerColor(int i){
        if(i>=0&&i<4){    
            playerColor=i;
        }        
    }
    
    // gets the player color
    public int getPlayerColor(){
        return playerColor;
    }

    // sets if the player is alive
    public void setIsAlive(boolean b){
        isAlive=b;
    }
    
    // returns if the player is alive
    public boolean getIsAlive(){
        return isAlive;
    }
    
    // sets if player is dying
    public void setIsDying(boolean b){
        isDying=b;
    }
    
    // gets if plyaer is dying
    public boolean getIsDying(){
        return isDying;
    }
    
    // sets the time remaining for the skull powerup
    public void setSkullTimer(int n){
        if(n>=0){
            skullTimer=n;
        }
    }
    
    // gets the time remaining for the skull powerup
    public int getSkullTimer(){
        return skullTimer;
    }
    
    // sets the starting duration of the skull powerup
    public void setSkullTimerStart(int n){
        if(n>0){
            skullTimerStart=n;
        }
    }
    
    // returns the starting duration of the skull powerup
    public int getSkullTimerStart(){
        return skullTimerStart;
    }    
    
    // returns the skull type (0 means you do not have the powerup)
    public int getSkullType(){
        return skullType;
    }
    
    // sets the skull type of the Bomberman
    public void setSkullType(int n){
        if(n>=0&&n<=3){
            if(n==1){
                speed=15;
            }
            else{
                speed=speedList[speedPower-1];
            }
            skullType=n;
        }
    }

    // sets the power of the bomb
    public void setBombPower(int i){
        if (i>0&&i<=maxBombPower){    
            bombPower=i;
        }
    }
    
    // gets the power of the bombs from the bomberman
    public int getBombPower(){
        return bombPower;
    }
    
    // gets the speed level of the bomberman
    public void setSpeedPower(int s){
        if(s>0&&s<=maxSpeedPower){
            speedPower=s;
        }
        // does not update speed if player has skull powerup, type 1
        if(skullType!=1){
            speed=speedList[speedPower-1];
        }
    }
    
    // gets the speed level of the bomberman
    public int getSpeedPower(){
        return speedPower;
    }

    // sets the maximum number of bombs the bomberman can have on the field
    public void setMaxBombs(int n){
        if (n>0){
            maxBombs=n;
        }
    }
    
    // returns the maximum number of bombs the bomberman can have on the field
    public int getMaxBombs(){
        return maxBombs;
    }
    
    // sets the maximum speed power of the player
    public void setMaxSpeedPower(int s){
        if(s>0){
            maxSpeedPower=s;
        }
    }
    
    // returns the maximum speed level of the player
    public int getMaxSpeed(){
        return maxSpeedPower;
    }

    // sets the maximum power of each bomb
    public void setMaxBombPower(int n){
        if(n>0){
            maxBombPower=n;
        }
    }

    // returns the maximum power of each bomb
    public int getMaxBombPower(){
        return maxBombPower;
    }
    
    // sets if the player can kick
    public void setCanKick(boolean b){
        canKick=b;
    }
    
    // returns if the player cna kick
    public boolean getCanKick(){
        return canKick;
    }
    
    // sets the score of the player
    public void setScore(int n){
        if(n>=0){
            score=n;
        }
    }
    
    // returns the score of the player
    public int getScore(){
        return score;
    }
    
    // sets if the player is moving
    public void setIsMoving(boolean b){
        isMoving=b;
        setAnimationCounter(0);
    }
    
    // gets if the player is moving
    public boolean getIsMoving(){
        return isMoving;
    }
    
    // gets the number of bombs the player has on the field
    public int getBombsPlaced(){
        return bombsPlaced;
    }

    // sets the number of bombs the player has on the field
    public void setBombsPlaced(int s){
        if(s>=0){
            bombsPlaced=s;
        }
    }
    
    // sets the image file storing the sprite Sheet of bombermen
    public void setSpriteSheetFile(URL f){
        spriteSheetFile=f;
    }
    
    // gets the image file storing the spriteSheet of bombermen
    public URL getSpriteSheetFile(){
        return spriteSheetFile;
    }
    
    // sets the image file storing the player icons
    public void setPlayerIconFile(URL f){
        playerIconFile=f;
    }
    
    // returns the image file storing the player icons
    public URL getPlayerIconFile(){
        return playerIconFile;
    }

    // adds a bomb onto the playing field
    public void addBomb(BombCollectionClass bombList){
        bombsPlaced++;
        // adds a bomb to the bombList vector based the player's bombPower and location.
        bombList.addBomb(player,bombPower,super.getCol(),super.getRow());
    }
    
    // removes a bomb
    public void removeBomb(){
        if (bombsPlaced>0)    
            bombsPlaced--;
    } 
    
    // returns if the Bomberman can place more bombs
    public boolean canPlaceMoreBombs(){
        return bombsPlaced<maxBombs;
    }
    
    // kills the bomberman
    public void kill(){
        isDying=true;
        isAlive=false;
        animationCounter=0;
    }    
    
    
} // BomberManClass class

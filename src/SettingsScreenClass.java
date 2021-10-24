import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.image.BufferedImage;

public class SettingsScreenClass {

    // Graphics modules for preliminary drawing
    private Graphics g;
    // preliminary drawing canvas for doublebuffering
    BufferedImage l1;

    private int cursorRow=1;                // current row of the cursor
    private int counter=0;                  // stops rapid changes in values when a button is held
    
    private final int maxPoint=20;                  // maximum points in a game
    private final int maxTime=480000;               // maximum time in a game

    private boolean endPart=false;                  // stores whether to end the section or not
    
    Color []fadeColors=new Color[13];                           // array storing colors used for fading to black
    int fadeAmount=0;                                           // fadeAmount for the first rectangle (for drawing winner screen)

    // Files that store all the images to be used in the Menu
    private static final URL spriteSheetFile = SettingsScreenClass.class.getClassLoader().getResource("BombSprites.png");
    private static final URL playerIconFile = SettingsScreenClass.class.getClassLoader().getResource("PlayerIconSpritesDraft.png");
    // private static final File spriteSheetFile = new File("BombSprites.png");
    // private static final File playerIconFile = new File("PlayerIconSpritesDraft.png");
    
    // image that stores the spritesheets
    private static BufferedImage spriteSheet=new BufferedImage(120,40,BufferedImage.TYPE_INT_ARGB);   
    private static BufferedImage iconSheet=new BufferedImage(100,125,BufferedImage.TYPE_INT_ARGB);    
    
    // array of images that are used for drawing
    private static BufferedImage[] bomb=new BufferedImage[3];
    private static BufferedImage[] icons=new BufferedImage[4];
    
    // prepares the images and graphics module fore drawing
    private void setImage(int w, int h){
        l1= new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        l1.createGraphics();
        g=(Graphics2D)l1.getGraphics (); 
    }
    
    // constructor always to be used
    SettingsScreenClass(Dimension dim){
        cursorRow=0;
        counter=0;
        endPart=false;
        initSprites();
        setImage(dim.width,dim.height);
        fadeAmount=12;
        for(int i=0;i<12;i++){
            fadeColors[i]=new Color(0,0,0,(i)*20);
        }
    }
    
    // constructor with no parameters, should be avoided
    SettingsScreenClass(){
        cursorRow=0;
        counter=0;
        endPart=false;
        initSprites();
        setImage(512,500);
        fadeAmount=12;
        for(int i=0;i<12;i++){
            fadeColors[i]=new Color(0,0,0,(i)*20);
        }
    }    
    
    
    // Reads image file and saves to spritesheet
    private void initSprites(){
        try
        {
            spriteSheet = ImageIO.read (spriteSheetFile);
        }
        catch (IOException e)
        {
            System.out.println("SPRITE spritesheet not found");
        } 
        try
        {
            iconSheet=ImageIO.read(playerIconFile);
        }
        catch (IOException e)
        {
            System.out.println("PLAYERICON spritesheet not found");
        }         

        // saves images to array
        for(int i=0;i<3;i++){
            bomb[i]=new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);    
            bomb[i]=spriteSheet.getSubimage(i*32,6,32,32);
        }
        for(int i=0;i<4;i++){
            icons[i] = new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
            icons[i] = iconSheet.getSubimage(0, 31*i, 30, 30);
        }
    }    

    // draws the text on the left coloumn    
    private void drawLeftColoumnText(SettingsClass s){
        Font font=new Font("SanSerif",Font.BOLD,40);
        g.setColor(Color.black);
        g.setFont(font);

        g.drawString("Players",50,40+50*1);
        g.drawString("Match Point",50,40+50*2);
        g.drawString("Time Limit",50,40+50*3);
        g.drawString("Sudden Death",50,40+50*4);
        
        // draws player numbers
        font=new Font("SanSerif",Font.BOLD,30);
        g.setColor(Color.black);
        g.setFont(font);
        for(int i=0;i<s.getNumPlayers();i++){
            String text="Player "+(char)(i+1+'0');
            g.drawString(text,50,300+40*i);            
        }
    }
    
    // takes an integer less than 100 and greater/equal to 0. It returns the string version of the number
    private String intString(int n){
        String s="";
        if(n>9){
            s+=(char)(Math.min(n/10,9)+'0');
        }
        s+=(char)(n%10+'0');
        return s;    
    }
    
    // draws value based on font, text, and coordinates
    private void drawNumber(Font f,String s,int x,int y){
        // finds width of the string when drawn
        FontMetrics metrics=g.getFontMetrics(f);
        int n=(int)metrics.stringWidth(s);        
        g.drawString(s,x-n/2,y);      
    
    }
    
    // takes an integer and returns it as a String showing the time
    private String intTime(long l){
        String s="";
        s+=(char)(((l/1000)/60)+'0')+":"+(char)((((l/1000)%60)/10)+'0')+(char)((((l/1000)%60)%10)+'0');    
        return s;
    }
    
    // assigns value to the 2 arrays to be used for drawing triangles
    private void findPoints(int[] xPoints,int[]yPoints,int x,int y,int x2,int h){
        xPoints[0]=x;
        xPoints[1]=x2;
        xPoints[2]=x;
        yPoints[0]=y;
        yPoints[1]=y+h/2;
        yPoints[2]=y+h;        
    }
    
    // draws a triangles that is either gray or red based on if condition is true
    private void drawT(int[] xPoints,int[] yPoints,boolean condition){
        if(condition)
            g.setColor(Color.red);        
        else
            g.setColor(Color.gray); 
        g.fillPolygon(xPoints,yPoints,3);
        g.setColor(Color.black);
        g.drawPolygon(xPoints,yPoints,3);    
    }
    
    // draws all triangles
    private void drawTriangles(SettingsClass s, int w){
        int[] xPoints=new int[3];
        int[] yPoints=new int[3];
        //draws player
        findPoints(xPoints,yPoints,w-35,5+50*1,w-15,40);
        drawT(xPoints,yPoints,s.getNumPlayers()<4);
        findPoints(xPoints,yPoints,w-40-105,5+50*1,w-40-105-20,40);
        drawT(xPoints,yPoints,s.getNumPlayers()>2);
        //draws matchPoint
        findPoints(xPoints,yPoints,w-35,5+50*2,w-15,40);
        drawT(xPoints,yPoints,s.getWinningPoint()<maxPoint);    
        findPoints(xPoints,yPoints,w-40-105,5+50*2,w-40-105-20,40);
        drawT(xPoints,yPoints,s.getWinningPoint()>1);
        //draws timelimit
        findPoints(xPoints,yPoints,w-35,5+50*3,w-15,40);
        drawT(xPoints,yPoints,s.getTimeLimit()<maxTime);    
        findPoints(xPoints,yPoints,w-40-105,5+50*3,w-40-105-20,40);
        drawT(xPoints,yPoints,s.getTimeLimit()>60000);  
        //draw suddenDeath
        findPoints(xPoints,yPoints,w-35,5+50*4,w-15,40);
        drawT(xPoints,yPoints,s.getSuddenDeath());    
        findPoints(xPoints,yPoints,w-40-105,5+50*4,w-40-105-20,40);
        drawT(xPoints,yPoints,!s.getSuddenDeath());        
        
        // draws each player  
        for(int i=0;i<s.getNumPlayers();i++){
        findPoints(xPoints,yPoints,w-65,275+40*i,w-45,30);
        drawT(xPoints,yPoints,s.getPlayerColor(i)<3);    
        findPoints(xPoints,yPoints,w-40-75,275+40*i,w-40-75-20,30);
        drawT(xPoints,yPoints,s.getPlayerColor(i)>0);          
        }
    }
    
    // draws everything at the right side
    private void drawRightColoumn(SettingsClass s,int w){
    
        // draws black rectangles
        g.setColor(Color.black);
        for(int i=0;i<4;i++){
            g.fillRect(w-40-100,5+50*(i+1),100,40);
        }
        
        // writes current values of SettingsClass
        
        // sets font
        Font font=new Font("SanSerif",Font.BOLD,40);        
        g.setColor(Color.white);
        g.setFont(font);
        
        // num players
        drawNumber(font,intString(s.getNumPlayers()),w-40-50,40+50*1);
        //match point
        drawNumber(font,intString(s.getWinningPoint()),w-40-50,40+50*2);        
        //time limit
        drawNumber(font,intTime(s.getTimeLimit()),w-40-50,40+50*3);
        //sudden death
        if(s.getSuddenDeath()){
            drawNumber(font,"Yes",w-40-50,40+50*4);        
        }  
        else{
            drawNumber(font,"No",w-40-50,40+50*4);            
        }

        // draws icons of players
        for(int i=0;i<s.getNumPlayers();i++){
            g.drawImage(icons[s.getPlayerColor(i)],w-40-65,275+40*i,null);
        }
        
        // draws all the triangles
        drawTriangles(s,w);
        
    }
    
    // draws cursor, consists of a rectangle and a bomb
    private void drawCursor(int w,int h){
        int n;
        // determines which bomb sprite to use in the animation
        if((counter/5)%2==0){
            if((counter/5)%4==2){
                n=2;
            }
            else{
                n=0;
            }
        }
        else{
            n=1;
        }        
        g.setColor(Color.white);
        // draws bombs and rectangles at specified cursor rows
        if(cursorRow==10){
            g.drawImage(bomb[n],15,h-60,null);            
        }
        else if(cursorRow<4){
            g.drawImage(bomb[n],10,10+50*(cursorRow+1),null);
            g.drawRect(45,50*(cursorRow+1),w-55,50);
        }
        else{
            g.drawImage(bomb[n],10,275+40*(cursorRow-4),null);        
            g.drawRect(45,270+40*(cursorRow-4),w-55,40);
        }
    
    }
    
    // draws the start button at the bottom
    private void drawStartButton(int w,int h){
        g.setColor(Color.gray);    
        g.fillRect(60,h-70,w-80,50);
        g.setColor(Color.black);
        g.drawRect(60,h-70,w-80,50); 
        
        Font font=new Font("SanSerif",Font.BOLD,50);  
        g.setFont(font);
        g.drawString("Start Game",120,h-25);
    }
    
    // returns if there is more than one team in the game
    private boolean multipleTeams(SettingsClass s){
        int a =s.getPlayerColor(0);
        for(int i=0;i<s.getNumPlayers();i++){
            if(a!=s.getPlayerColor(i)){
                return true;
            }
        }
        return false;
    }
    
    // reads key inputs
    public void readInputs(boolean[] keyStates,SettingsClass s){
        // counter must be greater than 3 in order to read inputs, 
        if(counter>3){
            if(keyStates[87]){              // moving up
                //moves cursor up if possible
                if(cursorRow==10)
                    cursorRow=3+s.getNumPlayers();
                else if(cursorRow!=0){
                    cursorRow--;                
                }
                counter=0;
            }
            else if(keyStates[83]){          // moving down
                //oves cursor down if possible
                if(cursorRow==3+s.getNumPlayers())
                    cursorRow=10;
                else if(cursorRow!=10){
                    cursorRow++;                
                }
                counter=0;
            }     
            else if(keyStates[68]){          // moving right
                //changes setting if possible
                switch(cursorRow){
                    case 0:                     //numPLayers 
                        if(s.getNumPlayers()<4){
                            s.setNumPlayers(s.getNumPlayers()+1);
                        }
                        break;
                    case 1:                     //winnning point
                        if(s.getWinningPoint()<maxPoint){
                            s.setWinningPoint(s.getWinningPoint()+1);                            
                        }
                        break;
                    case 2:                     //timelimit
                        if(s.getTimeLimit()<maxTime){
                            s.setTimeLimit((long)(s.getTimeLimit()+15000));
                        }
                        break;
                    case 3:                     //sudden death
                        if(s.getSuddenDeath()){
                            s.setSuddenDeath(false);
                        }
                        break;
                    case 10:
                        break;
                    default:                    //player colors
                        if(s.getPlayerColor(cursorRow-4)<3){
                           s.setPlayerColor(cursorRow-4,s.getPlayerColor(cursorRow-4)+1); 
                        }
                        break;
                }
                counter=0;
            }   
            else if(keyStates[65]){          // moving left
                //changes settings if possible
                switch(cursorRow){
                    case 0:
                        if(s.getNumPlayers()>0){
                            s.setNumPlayers(s.getNumPlayers()-1);
                        }
                        break;
                    case 1:
                        if(s.getWinningPoint()>1){
                            s.setWinningPoint(s.getWinningPoint()-1);                            
                        }
                        break;
                    case 2:
                        if(s.getTimeLimit()>60000){
                            s.setTimeLimit((long)(s.getTimeLimit()-15000));
                        }
                        break;
                    case 3:
                        if(!s.getSuddenDeath()){
                            s.setSuddenDeath(true);
                        }
                        break;
                    case 10:
                        break;
                    default:
                        if(s.getPlayerColor(cursorRow-4)>0){
                           s.setPlayerColor(cursorRow-4,s.getPlayerColor(cursorRow-4)-1); 
                        }
                        break;
                }
                counter=0;
            }
            // is space is pressed when cursor row is at row 10 and there are multiple teams, the program moves on to the game.
            else if(keyStates[32]&&cursorRow==10&&multipleTeams(s)){
                endPart=true;
            }
        }
    }
    
    // draws all other text, notices/instructions
    public void drawNoticeText(int h){
        Font font=new Font("SanSerif",Font.BOLD,15);        
        g.setColor(Color.red);
        g.setFont(font);
        String t="NOTE: All players cannot be on the same team.";
        g.drawString(t,50,265);
        t="Use WASD to move.";
        g.drawString(t,50,48); 
        t="Press SPACE on the Start Button to Begin!";
        g.drawString(t,120,h-5);    
        
        font=new Font("SanSerif",Font.BOLD,40);        
        g.setColor(Color.black);
        g.setFont(font);        
        t="Settings";
        g.drawString(t,330,40);        
    }
    
    // runs Setting Menu, if returns true, the program should move on to the rest of the game
    public boolean runSettings(Graphics finalImage,SettingsClass settings,boolean [] keyStates,Dimension dim, JPanel p){
        //draws background rectangle
        g.setColor(Color.lightGray);
        g.fillRect(0,0,dim.width,dim.height);
        
        //reads inputs
        if (!endPart){
            readInputs(keyStates,settings);
        }
            
        //increments counter
        counter++;
        if(counter>1000000){
            counter=10;
        }
        
        //draws everything
        drawLeftColoumnText(settings);
        drawRightColoumn(settings,dim.width);
        drawStartButton(dim.width,dim.height);
        drawCursor(dim.width,dim.height);
        drawNoticeText(dim.height);
    
        // works with fading from/to black when starting/ending the menu
        if(!endPart&&counter%2==0){
            fadeAmount=Math.max(0,fadeAmount-1);
        }
        else if(endPart&&counter%2==0){
            fadeAmount=Math.min(12,fadeAmount+1);
        }

        g.setColor(fadeColors[fadeAmount]);
        g.fillRect(0,0,dim.width,dim.height);
        
        // double buffers
        finalImage.drawImage(l1, 0, 0, p);
        if(endPart&&fadeAmount==12){
            return true;
        }
        return false;
    }

}










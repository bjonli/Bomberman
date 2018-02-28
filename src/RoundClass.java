// The "RoundClass" class.
import java.applet.*;
import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class RoundClass extends Applet
{
    // Graphics modules for preliminary drawing
    Graphics g, Buffered_Image; 
    // preliminary canvases that are used before the visuals are doubleBuffered
    BufferedImage offscreen,combinedImage;
    // THe distance from the top of the screen to the top of the battle arena, not the same as TL_CORNERY in ObjectClass
    int TL_CORNERY=104;                     //
    // Message to display in the center
    String message="";
    // section of a round
    /*
       -1=Black screen fades to game screen
        0=Countdown to round start
        1=Actual Gameplay
        2=Fades to round end screen
        3=Drawing round end screen
        4=Fade to black screen
    */
    int roundSection=-1;
    // the remaining time the center message will be displayed
    int textTimer=10000;
    // measure the amount of time after all but one teams dies. For a team a win, they must last longer in the arena by atleast 1 second
    int afterDeathTimer=-1;
    int counter=0;                                              // counter for fading 
    Color []fadeColors=new Color[13];                           // array storing colors used for fading to black
    int fadeAmount=0;                                           // fadeAmount for the first rectangle (for drawing winner screen)
    int fadeAmount2=12;                                         // fadeAmount for the seond rectangle (for starting new round)
    long countDown= 120000;                                     // time remaining in a round
    int winningPoint=5;                                         // how many points required before winning
    boolean hasWinner=false;
    long countDownStart=120000;
    boolean endRound=false;
    boolean suddenDeath=true;
    
    BomberMapClass map;                                                         // map object 
    BombCollectionClass bombList;                                               // vector of bombs
    int numPlayers=4;                                                           // number of players 
    BomberManClass[] b;                                                         // array of bomberman objects
    BomberKeyBindingsClass[] controls;                                          // array of player controls
    ScoreSpritesClass scoreSprites;                                             // score object
    
    private void initRound(int w, int h,SettingsClass s){
        setGraphics(w,h);                                                       // sets up the graphics and images for drawing
        // sets up the fade colors
        for(int i=0;i<12;i++){
            fadeColors[i]=new Color(0,0,0,(i)*20);
        }
        fadeColors[12]=(Color.black);

        // sets up objects 
        bombList=new BombCollectionClass();
        scoreSprites=new ScoreSpritesClass();
        map=new BomberMapClass();
        map.drawAll(g);      
        
        //Sets up a Bomberman and controls for each player
        numPlayers=s.getNumPlayers();
        b=new BomberManClass[numPlayers];
        controls=new BomberKeyBindingsClass[numPlayers]; 
        for(int i=0;i<numPlayers;i++){
            b[i] = new BomberManClass (i,s.getPlayerColor(i));
            switch(i){
                case 0:
                    b[i].setCoordinatesByColRow(0,0);
                    break;
                case 1:
                    b[i].setCoordinatesByColRow(12,0);
                    break;
                case 2:
                    b[i].setCoordinatesByColRow(0,10);
                    break;
                case 3:
                    b[i].setCoordinatesByColRow(12,10);
                    break;
            }
            controls[i]=new BomberKeyBindingsClass(i); 
        }    
        winningPoint=s.getWinningPoint();
        hasWinner=false;
        countDownStart=s.getTimeLimit();
        countDown=countDownStart;
        endRound=false;
        suddenDeath=s.getSuddenDeath();
    }
    
    // Constructor given window resolution and number of players
    RoundClass(Dimension dim,SettingsClass s){ 
        initRound(dim.width,dim.height,s);
    }

    // Constructor with no parameters, should never be called
    RoundClass(){                         
        initRound(512,500,new SettingsClass());
    }
    
    // prepares the imges and graphics module for drawing
    public void setGraphics(int w, int h){ 
        // first draw
        offscreen= new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        offscreen.createGraphics();
        g=(Graphics2D)offscreen.getGraphics ();
        
        // second draw
        combinedImage=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        combinedImage.createGraphics();
        Buffered_Image=(Graphics2D)combinedImage.getGraphics(); 
    }
    
    // restarts the game by reseting the scores of every player
    public void restartGame(){
        hasWinner=false;
        for(int i=0;i<numPlayers;i++){
            b[i].setScore(0);
        }
    }
    
    // changes the countdown to a string value
    public String countDownToString(long cd){
        long n=cd/1000;
        String s=""+(char)((n/60)+'0')+":"+(char)(((n%60)/10)+'0')+""+(char)(((n%60)%10)+'0');
        return s;
    }
    
    // draws the top bar of the screen
    public void drawTopBar(Dimension dim){
        // draws background rectangle, also erases what was on the top bar before
        g.setColor(Color.lightGray);
        g.fillRect(0,0,dim.width,TL_CORNERY);
        int x=16;
        for(int j=1;j>-2;j-=2){ // j will be 1, then -1
            int s=-1*(j-1)/2;   // s will be either 0 or 1
            for(int i=0;i<2;i++){
                // draws player icon (assuming player exists)
                if(i*2+s<numPlayers){
                    b[i*2+s].drawIcon(g,Math.min(x,x+j*30),TL_CORNERY-40*(2-i));
                    // shows score
                    scoreSprites.writeScore(g,b[i*2+s].getScore(),Math.min(x+j*(36),x+j*(36+32)),TL_CORNERY-40*(2-i)+2);
                    //shows any significant powerups 
                    if(b[i*2+s].getBombPower()==b[i*2+s].getMaxBombPower()){
                        map.drawPowerUp(g,6,Math.min(x+j*(74),x+j*(74+32)),TL_CORNERY-40*(2-i));
                    }
                    if(b[i*2+s].getCanKick()){
                        map.drawPowerUp(g,8,Math.min(x+j*(108),x+j*(108+32)),TL_CORNERY-40*(2-i));            
                    }
                    if(b[i*2+s].getSkullType()>0){
                        map.drawPowerUp(g,7,Math.min(x+j*(142),x+j*(142+32)),TL_CORNERY-40*(2-i));            
                    }
                }
            }
            x=dim.width-16;
        }
        // draws the time
        g.setColor(Color.black);
        Font timeFont=new Font("SanSerif",Font.BOLD,60);
        g.setFont(timeFont);
        g.drawString(countDownToString(countDown),(dim.width)/2-60,TL_CORNERY-30);
        // draws the word "TIME" belowe the time
        Font wordFont=new Font("SanSerif",Font.BOLD,28);
        g.setFont(wordFont);        
        g.drawString("TIME",(dim.width)/2-35,TL_CORNERY-2);
    }
    
    // convert from number to string. Numbers from 0 to 99.
    public String numToString(int n){
        String s="";
        if(n>9){
            s+=(char)(Math.min(n/10,9)+'0');
        }
        s+=(char)(n%10+'0');
        return s;
    }
    
    // returns if a player reached roundPoint
    private boolean winnerFound(){
        for(int i=0;i<numPlayers;i++){
            if(b[i].getScore()>=winningPoint){
                return true;
            }
        }
        return false;
    }
    
    // returns the number of winners
    private int numWinners(){
        int n=0;
        for(int i=0;i<numPlayers;i++){
            if(b[i].getScore()>=winningPoint){
                n++;
            }
        }
        return n;    
    }
    
    // returns the color of the winngin team in a round
    private int findRoundWinnerColor(){
        for(int i=0;i<numPlayers;i++){
            if(b[i].getIsAlive()){
                return b[i].getPlayerColor();
            }
        }        
        return -1;
    }
    
    // draws the round end screen on top of the game screen.
    public void drawRoundEndImages(Graphics layer,Dimension dim){
        int numSurviving=findTeamsSurviving();          // find number of survivng teams

        // If one team wins, their sore increases by one
        int roundWinner=findRoundWinnerColor();
        if(numSurviving==1&&roundSection==2){
            for(int i=0;i<numPlayers;i++){
                if(b[i].getPlayerColor()==roundWinner){
                    b[i].setScore(b[i].getScore()+1);
                }
            } 
            if(winnerFound()){
                hasWinner=true;
            }
        } 
        // sets round Section to 3 (now drawing round end)
        if(roundSection==2){
           roundSection=3; 
        }
        layer.setColor(Color.yellow);
        
        // Draws the matchPoint Text
        Font FirstToFont=new Font("SanSerif",Font.PLAIN,30);
        FontMetrics metrics=layer.getFontMetrics(FirstToFont);
        String text="First to "+numToString(winningPoint)+".";
        int w=(int)metrics.stringWidth(text);
        layer.setFont(FirstToFont);
        layer.drawString(text,(dim.width-w)/2,TL_CORNERY+70);

        if(hasWinner){
            //writes message no 1
            Font nextFont=new Font("SanSerif",Font.ITALIC,20);        
            metrics=layer.getFontMetrics(nextFont);
            text="Press \"Q\" to go to Main Menu.";
            w=(int)metrics.stringWidth(text);
            layer.setFont(nextFont);
            layer.drawString(text,(dim.width-w)/2,TL_CORNERY+320);
        
            // writes message no 2
            text="Press the Space Bar to Play Another Game.";
            w=(int)metrics.stringWidth(text);
            layer.drawString(text,(dim.width-w)/2,TL_CORNERY+345);        
            
            //writes message no 1
            Font finalFont=new Font("SanSerif",Font.BOLD,50);        
            metrics=layer.getFontMetrics(finalFont);
            text="FINAL STANDINGS";
            w=(int)metrics.stringWidth(text);
            layer.setFont(finalFont);
            layer.drawString(text,(dim.width-w)/2,TL_CORNERY-30);
            
            // draws winner
            int n=numWinners();
            int e=0,f=0;
            for(int i=0;i<numPlayers;i++){
                if(b[i].getScore()>=winningPoint){
                    b[i].draw(layer,(dim.width-(b[i].getWidth()*n+20*(n-1)))/2+e*(b[i].getWidth()+20),TL_CORNERY-5,0,1);  
                    e++;
                }
                else{
                    b[i].draw(layer,(dim.width-(b[i].getWidth()*(numPlayers-n)+20*(numPlayers-n-1)))/2+f*(b[i].getWidth()+20),TL_CORNERY+250,4,2);  
                    f++; 
                }
            }
            
            layer.drawRect(10,10,dim.width-20,dim.height-20);
        }
        else{
            // Draws the "spacebar to continue" text
            Font nextFont=new Font("SanSerif",Font.ITALIC,20);        
            metrics=layer.getFontMetrics(nextFont);
            text="Press the Space Bar to Continue.";
            w=(int)metrics.stringWidth(text);
            layer.setFont(nextFont);
            layer.drawString(text,(dim.width-w)/2,TL_CORNERY+300); 
        }
        
        // draws player icon and player scores above text
        for(int i=0;i<numPlayers;i++){
            if(hasWinner){
                b[i].drawIcon(layer,(dim.width/2)-8-80+i*48,TL_CORNERY+80,b[i].getScore()>=winningPoint);                
            }
            else{
                b[i].drawIcon(layer,(dim.width/2)-8-80+i*48,TL_CORNERY+80);
            }
            scoreSprites.writeScore(layer,b[i].getScore(),(dim.width/2)-8-80+i*48,TL_CORNERY+120);            
        }
        
    }
    
    // read controls and moves bombeerman/place bombs if needed
    public void readControls(boolean [] keyStates){
        for(int i=0;i<numPlayers;i++){                  // checks each bomberman
            if (b[i].getIsAlive())                      // only checks if bomberman is alive
            {
                if (keyStates[controls[i].keyUp()])         // move up
                {
                    // Type 2 Skull reverses controls
                    if(b[i].getSkullType()!=2)              
                        b[i].setDirection (2);
                    else
                        b[i].setDirection (0);                    
                    if(map.canMove(bombList,b[i])) 
                        b[i].move();
                }
                else if (keyStates[controls[i].keyLeft()])  // moves left
                {
                    if(b[i].getSkullType()!=2)
                        b[i].setDirection (1);
                    else
                        b[i].setDirection (3); 
                    if(map.canMove(bombList,b[i]))
                        b[i].move();
                }
                else if (keyStates[controls[i].keyDown()])  // moves down
                {
                    if(b[i].getSkullType()!=2)
                        b[i].setDirection (0);
                    else
                        b[i].setDirection (2);
                    if(map.canMove(bombList,b[i])) 
                        b[i].move();
                }
                else if (keyStates[controls[i].keyRight()]) // moves right
                {
                    if(b[i].getSkullType()!=2)
                        b[i].setDirection (3);
                    else
                        b[i].setDirection (1); 
                    if(map.canMove(bombList,b[i]))
                        b[i].move();
                }
                else{   // if none of the above keys were pressed, the bomerman is not moving
                    b[i].setIsMoving (false);
                }
                if(keyStates[controls[i].keyBomb()]){               // plants bomb if possible
                    if(map.canPlaceBomb(b[i].getCol(),b[i].getRow())&&b[i].canPlaceMoreBombs()){
                        b[i].addBomb(bombList);
                        map.setTileTerrainState(b[i].getCol(),b[i].getRow(),'X');       
                    }
                } 
            }
        } 
    }
    
    // draws the center front message
    public void drawFrontMessage(String text,Graphics layer,Dimension dim){
        Font font=new Font("SanSerif",Font.BOLD,50);
        FontMetrics metrics=layer.getFontMetrics(font);
        int w=(int)metrics.stringWidth(text);
        layer.setColor(Color.yellow);
        layer.setFont(font);
        // automatically centers the text
        layer.drawString(text,(int)(dim.width-w)/2,(dim.height+TL_CORNERY)/2);
    }
    
    // find the number of teams tbat are still alive
    public int findTeamsSurviving(){
        int n=0;
        // checks each color, if  player that is alive is found with that color, n increments by one
        for(int i=0;i<4;i++){
            for(int j=0;j<numPlayers;j++){
                if(b[j].getPlayerColor()==i&&b[j].getIsAlive()){
                    n++;
                    break;
                }
            }
        }
        return n;
    }
    
    // changes the fade amount depending on the counter and the current round section
    // Also does constant checks to change rounds if necessary
    public void changeFadeAmount(boolean [] keyStates){
        switch(roundSection){
            case -1:
                // fadeAmount2 decreases before round start, black screen to no screen
                counter++;
                if(fadeAmount2>0&&counter%2==0){
                    fadeAmount2--;
                }
                // after ddafing, roundSection 0 begins
                else if(fadeAmount2==0){
                    message="3";
                    textTimer=1000;
                    roundSection=0;
                }
                break;
            case 2:
                // fadeAmount increases before round end screen
                counter++;
                if(fadeAmount<7&&counter%2==0&&counter>40){
                    fadeAmount++;
                }
                break;
            case 3:
                if(keyStates[81]){
                    endRound=true;
                    roundSection=4;
                }
                // checks if space bar is pressed, if it is, move to next roundSection 4
                else if(keyStates[32]){
                    roundSection=4;
                }            
                break;
            case 4:
                // fades to complete black
                counter++;
                if(fadeAmount2<12&&counter%2==0){
                    fadeAmount2++;
                }
                // after fading to complete black, a new round begins, and startNewRound is called.
                else if(fadeAmount2==12){
                    roundSection=-1;
                    counter=0;
                    // if a winner has already been declared, a new game is played.
                    if(hasWinner){
                        restartGame();
                    }
                    startNewRound();
                }
                break;
            default:
                break;
        }
    }
    
    // starts new round resets bombs, bombermen, map tiles, countDown, fadeAmounts, etc. Everything to be able to start a new round. Draws everything.
    public void startNewRound(){
        map.resetTiles();                                       // reset map
        for(int i=0;i<numPlayers;i++){                          // reset players
            b[i].resetPlayer();
            switch(i){
                case 0:
                    b[i].setCoordinatesByColRow(0,0);
                    break;
                case 1:
                    b[i].setCoordinatesByColRow(12,0);
                    break;
                case 2:
                    b[i].setCoordinatesByColRow(0,10);
                    break;
                case 3:
                    b[i].setCoordinatesByColRow(12,10);
                    break;
            }
        }   
        bombList.clearBombs();                                  // reset bombs
        fadeAmount=0;                                           // reset fade amounts
        fadeAmount2=12;
        message="";                                             // reset message
        countDown=countDownStart;                               // reset countdown
        afterDeathTimer=-1;                                     // reset afterDeathTimer

        map.drawAll(g);                                         // draws everything
    }
    

    public void changeCenterText(){
        switch(roundSection){
            // countdowns from 3 to 1, then start
            case 0:
                if(message.equals("1")){
                    roundSection=1;
                    message="START!";
                }
                else{
                    message=""+(char)(message.charAt(0)-1);
                }
                textTimer=1000;
                break;
            // removes text after a second
            case 1:
                message="";
                break;
            default:
        } 
    }
    
    // runs an average round
    public boolean runRound (Graphics finalImage,boolean [] keyStates,int delay,Dimension dim)
    {
        // chenges the text if the textTimer=0
        if(textTimer==0){
            changeCenterText();
        }
    
        // erases and redraws objects stuff
        map.redrawCoveredTiles(g,b,numPlayers,bombList);
        
        // counts down stuff        
        bombList.countDown(delay);
        textTimer=Math.max(0,textTimer-delay);
        
        // reads key inputs and runs gameplay if roundSection is 1
        if(roundSection==1){
            //lowers countDown timer
            countDown=Math.max(0,countDown-delay);
            // case when countDown is 30 seconds
            if((countDown/1000)==30 && textTimer==0&&suddenDeath){
                message="HURRY UP!";
                textTimer=1500;
            }
            // if countDown is less than 30, map starts being filled with walls
            if(countDown<=30000&&countDown%200==0&&suddenDeath){
                map.fillMap(b,numPlayers,g,bombList);
            }
            
            // reads input keys
            readControls(keyStates);
            // automatically plant bomb if player has skull powerup type 3 and is on an empty tile
            for(int i=0;i<numPlayers;i++){
                if(b[i].getIsAlive()&&b[i].getSkullType()==3&&map.canPlaceBomb(b[i].getCol(),b[i].getRow())&&b[i].canPlaceMoreBombs()){
                    b[i].addBomb(bombList);
                    map.setTileTerrainState(b[i].getCol(),b[i].getRow(),'X');       
                }
            }
            
            // Finds all the tiles that are currently occupied by a bomb/player
            map.findOccupiedTiles(b,numPlayers,bombList);
            
            // moves bombs
            bombList.moveBombs(map);       
            
            // kill/explode objects
            bombList.explodeBombs(map,b);
            for(int i=0;i<numPlayers;i++){
                if(b[i].getIsAlive()){
                    // check every live player if they collected a powerup, stood in an explosion, or skull timer runs out
                    map.checkBomberMan(b[i]);
                    b[i].setSkullTimer(Math.max(0,b[i].getSkullTimer()-delay));
                    if(b[i].getSkullTimer()==0){
                        b[i].setSkullType(0);
                    }
                }
            }
            
            // checks the number of teams left
            int teamsSurviving=findTeamsSurviving();
            // if there is one team left, afterDeathTimer starts ticking and remaining team must survive until afterDeathTimer reaches 0 to win the round
            if(teamsSurviving==1){
                if(afterDeathTimer==-1){
                    afterDeathTimer=2000;
                }
                else{
                    afterDeathTimer=Math.max(0,afterDeathTimer-delay);
                }
            }
            
            // ends round if afterDeathTimer=0, time runs out, or every team dies
            if(teamsSurviving==0||afterDeathTimer==0||countDown==0){
                message="FINISH!";
                textTimer=2000;
                roundSection=2;         // starts roundSection 2, fade into round ending screen
                counter=0;
            }
            
        }
        
        //drawing other stuff. These will be drawn no matter which roundSection the round is at 
        drawTopBar(dim);                                // draws top bar
        bombList.drawBombs(g,map);                      // draws bombs
        map.drawExplosions(g);                          // draws explosions
        
        // sort players by their order to be drawn. This is determined by the player's current location (row number)
        // farther bombermen should be drawn before close bombermen to avoid farther bombermen to be drawn on top of closer bombermen
        Integer[] orderOfBM=new Integer[numPlayers];
        for(int i=0;i<numPlayers;i++){
            orderOfBM[i]=new Integer(i);
        }
        Arrays.sort(orderOfBM, new Comparator() {
            public int compare(Object x, Object y) {
                int ix = ((Integer)x).intValue();
                int iy = ((Integer)y).intValue();
                if(b[ix].getYCoordinate() < b[iy].getYCoordinate())
                    return -1;
                if(b[ix].getYCoordinate()>b[iy].getYCoordinate())
                    return 1;
                return 0;
            } 
        });
        
        // draws all bombermen
        for(int i=0;i<numPlayers;i++){
            b[orderOfBM[i].intValue()].draw(g);
        }
        
        // output image into the second image canvas
        Buffered_Image.drawImage(offscreen,0,0,this); 

        changeFadeAmount(keyStates);
        
        // adds second layer 
        Buffered_Image.setColor(fadeColors[fadeAmount]);
        Buffered_Image.fillRect(0,0,dim.width,dim.height);     
        
        // adds center message
        drawFrontMessage(message,Buffered_Image,dim);  
        
        // if the roundSection is about to go to 3, or is already passed that point, the round-end screen will be displayed
        if((roundSection==2&&counter>70)||roundSection>2){
            if(roundSection==2){
                counter=0;
            }
            drawRoundEndImages(Buffered_Image,dim);
        }
        
        // if the roundSection is -1 or 4, another layer is added for dimming
        if(roundSection==4||roundSection==-1){
            Buffered_Image.setColor(fadeColors[fadeAmount2]);
            Buffered_Image.fillRect(0,0,dim.width,dim.height);            
        }
        
        // draws the final image
        finalImage.drawImage(combinedImage,0,0,this);
        
        // if player chooses to quit game, runRound returns true.
        if(roundSection==-1&&endRound){
            return true;
        }
        
        return false;
    }
} // RoundClass class

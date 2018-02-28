// The "BomberManMain" class.
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;

public class BomberManMain extends Applet implements KeyListener
{
    //LIST OF VARIABLES
    
    // applet variables for graphics
    Graphics finalImage;
    Dimension dim= new Dimension(510,500);

    // Variables for timer
    Timer timer;
    int delay=40; 
    
    // number of players playing
    int numPlayers=4;
    int [] playerColors;
    int winningPoint=2;

    SettingsClass s;
    // stores if each key is pressed down or not
    boolean [] keyStates=new boolean[223];
    
    int gamePart=5;
    /*
        3=settingsMenu
        5=round
    */
    RoundClass round;           // used for running a round
    SettingsScreenClass setMenu;
    
    long cTime=0;               // for test runs
    
    public void init ()
    {   
        // sets graphics
        setSize(dim);
        finalImage=getGraphics();
        
        // set up boolean array
        for(int i=0;i<223;i++){
            keyStates[i]=false;
        } 

        gamePart=3;
        s=new SettingsClass();
        // add class
        addKeyListener (this);
        setMenu=new SettingsScreenClass(dim);
        
        // opens instruction text file
        try{
            openInstructions();
        }
        catch(Exception e){}
        
        timer=new Timer();
        timer.scheduleAtFixedRate(new timeTask(),200,delay); 
        // Place the body of the initialization method here
    } // init method
    
    
    public void main() {
    	init();
    }
    
    // opens text file outlining player contrls and instructions on playing bomberman
    public void openInstructions() throws Exception {
        Process p =Runtime.getRuntime().exec("notepad BombermanInstructions.txt");    
    
    }
    
    //redraw whole map when WindomOpen
    public class timeTask extends TimerTask{
        public void run(){
            switch(gamePart){
                // case 3 is running the settings menu
                case 3:
                    if(setMenu.runSettings(finalImage,s,keyStates,dim)){
                        round=new RoundClass(dim,s); 
                        gamePart=5;
                    }
                    break;
                // case 5 is running the actual game
                case 5:
                    if(round.runRound(finalImage,keyStates,delay,dim)){
                        setMenu=new SettingsScreenClass(dim);
                        gamePart=3;
                    }
                    break;
                default:
                    
            
            }
        }
    }
    
    // key listeners for getting key inputs
    public void keyTyped(KeyEvent e){
    
    }

    public void keyPressed (KeyEvent e)
    {
        keyStates[e.getKeyCode()]=true;
    }


    public void keyReleased (KeyEvent e)
    {
        keyStates[e.getKeyCode()]=false;
    }

    public boolean action (Event e, Object o)
    {
        return true;
    }
    
    // paint function not used
    public void paint (Graphics g)
    {
    } // paint method
    

} // BomberManMain class

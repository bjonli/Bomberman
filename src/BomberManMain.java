// The "BomberManMain" class.
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.*;
import java.net.URL;

public class BomberManMain extends JFrame implements KeyListener
{
    //LIST OF VARIABLES
	
    // variables for graphics
    Graphics finalImage;
    Dimension dim= new Dimension(510,500);
    JPanel p = new JPanel();
    
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
    
    BomberManMain() {
        // sets graphics
        setSize(dim);
        JPanel p = new JPanel();
        p.setPreferredSize(dim);
        this.add(p);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finalImage=p.getGraphics();
        
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
    }
    
    // opens text file outlining player controls and instructions on playing bomberman
    public void openInstructions() throws Exception {
    	URL u = BomberManMain.class.getClassLoader().getResource("BombermanInstructions.txt");
    	File file = new File(u.toURI());
        Process p =Runtime.getRuntime().exec("notepad " + file.getPath());    
    }
    
    //redraw whole map when WindomOpen
    public class timeTask extends TimerTask{
        public void run(){
            switch(gamePart){
                // case 3 is running the settings menu
                case 3:
                    if(setMenu.runSettings(finalImage,s,keyStates,dim,p)){
                    	round=new RoundClass(dim,s); 
                        gamePart=5;
                    }
                    break;
                // case 5 is running the actual game
                case 5:
                    if(round.runRound(finalImage,keyStates,delay,dim,p)){
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

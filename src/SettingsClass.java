// The "SettingsClass" class.
import java.awt.*;

public class SettingsClass
{
    private int numPlayers=4;
    private int winningPoint=3;
    private long timeLimit=120000;
    private boolean suddenDeath=true;
    private int [] playerColors;
    
    SettingsClass(){
        numPlayers=4;
        winningPoint=3;
        timeLimit=120000;
        suddenDeath=true;   
        playerColors=new int[numPlayers];
        for(int i=0;i<numPlayers;i++){
            playerColors[i]=0;
        }
    }
    
    public void setWinningPoint(int n){
        if(n>0){
            winningPoint=n;
        }
    }
    
    public int getWinningPoint(){
        return winningPoint;
    }
    
    public void setTimeLimit(long n){
        if(n%1000==0&&n>0){
            timeLimit=n;
        }
    }
    
    public long getTimeLimit(){
        return timeLimit;
    }    

    public void setSuddenDeath(boolean b){
        suddenDeath=b;
    }

    public boolean getSuddenDeath(){
        return suddenDeath;
    }
    
    public void setNumPlayers(int n){
        if(n>1&&n<=4&&n!=numPlayers){
            int s=numPlayers;
            int[] temp=new int[numPlayers];
            for(int i=0;i<numPlayers;i++){
                temp[i]=playerColors[i];
            }
            numPlayers=n;
            playerColors=new int[numPlayers];
            for(int i=0;i<numPlayers;i++){
                if(i>=s){
                    playerColors[i]=0;
                }
                else{
                    playerColors[i]=temp[i];                
                }
            }
        }
    }
    
    public int getNumPlayers(){
        return numPlayers;
    }
    
    public void setPlayerColor(int i,int n){
        if(i>=0&&i<numPlayers&&n>=0&&n<4){
            playerColors[i]=n;
        }
    }
    
    public int getPlayerColor(int i){
        if(i>=0&&i<numPlayers){
            return playerColors[i];
        }
        return -1;
    }
    
} // SettingsClass class

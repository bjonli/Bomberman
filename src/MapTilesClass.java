// The "MapTilesClass" class.
import java.awt.*;
//import hsa.Console;

/*
    Each Tile has its own type, and can store a powerup, and stores where it is in an explosion
    Bombs be one of the following types:
    O=open
    X=bomb
    B=brick
    D=detonating
    E=explosion
    W=wall    
*/

public class MapTilesClass
{
    
    private int explodeState = 0;               // explode state of the tile, used for animating explosion
    private int explodeSprite = 0;              // sprite to use when drawing explosion
    private int powerUpType = -1;
    //0 = not exploding
    //1-20, 5 states of exploding

    private static int explodeDuration = 27;    // length (frames) of an explosion
    private char terrainState = 'O';            // type of tile
    
    private boolean isOccupied;                 // stores if the tile is occupied by a player/bomb
    private boolean canWalkOn;                  // stores if a player can walk on tile
    
    // constructor, given a specific tile type
    MapTilesClass (char type)
    {
        powerUpType=-1;
        explodeState = 0;
        switch (type)
        {
            case 'O':
                if ((int) (Math.random () * 10) == 0)
                {
                    terrainState = 'O';
                    isOccupied = false;
                    canWalkOn = true;
                }
                else
                {
                    terrainState = 'B';
                    isOccupied = true;
                    canWalkOn = false;
                }
                break;
            case 'W':
                isOccupied = true;
                terrainState = 'W';
                canWalkOn = false;
                break;
            case 'S':
                terrainState = 'O';
                isOccupied = false;
                canWalkOn = true;
                break;
            default:
                System.out.println ("Invalid Tiles Type");
                break;
        }
    }

    // constructor with no parameters. Makes the tile an open area 10% of the time.
    MapTilesClass ()
    {
        if ((int) (Math.random () * 10) == 0)
        {
            terrainState = 'O';
            isOccupied = false;
            canWalkOn = true;
        }
        else
        {
            terrainState = 'B';
            isOccupied = true;
            canWalkOn = false;
        }
    }

    // sets the state of the tile
    public void setTerrainState(char c){
        switch(c){
            case 'O':
                terrainState = 'O';
                isOccupied = false;
                canWalkOn = true;
                break;
            case 'E':
                terrainState = 'E';
                isOccupied = false;
                canWalkOn = true;
                explodeState=0;
                powerUpType=-1;
                break;
            case 'X':
                terrainState = 'X';
                isOccupied = true;
                canWalkOn = false;
                break;
            case 'D':
                terrainState = 'D';
                isOccupied = false;
                canWalkOn = true;
                explodeState=0;
                break;                
            case 'B':
                terrainState = 'B';
                isOccupied = true;
                canWalkOn = false;
                break;
            case 'W':
                terrainState = 'W';
                isOccupied = true;
                canWalkOn = false;
                break;
            // Z is an extra character that cna be used when assigning a value. 
            // If c is 'Z' the terrainstate has at 10% chnace of being 'O' and 90% of being 'B'
            case 'Z':
                if ((int) (Math.random () * 10) == 0)
                {
                    terrainState = 'O';
                    isOccupied = false;
                    canWalkOn = true;
                }
                else
                {
                    terrainState = 'B';
                    isOccupied = true;
                    canWalkOn = false;
                }
                break;
            default:
                System.out.println ("Invalid Tiles Type");
            break;
        }
    }

    // returns the state of the tile
    public char getTerrainState(){
        return terrainState;
    }
    
    // sets if the tile is occupied
    public void setIsOccupied(boolean b){
        isOccupied=b;
    }
    
    // returns if the tile is occupied
    public boolean getIsOccupied(){
        return isOccupied;
    }
    
    // sets if a player can walk on this tile
    public void setCanWalkOn(boolean b){
        canWalkOn=b;
    }
    
    // returns if player can walk on this tile
    public boolean getCanWalkOn(){
        return canWalkOn;
    }
    
    // sets the explosion state of the tile
    public void setExplodeState(int i){
        if(i>=0&&i<=explodeDuration)
            explodeState=i;
    }
    
    // returns the explosion state of the tile 
    public int getExplodeState(){
        return explodeState;
    }
    
    // increments the explosions state by one
    public void incrementExplodeState(){
        explodeState++;
    }
    
    // sets the duration of an explosion
    public void setExplodeDuration(int i){
        if(i>=explodeState)
            explodeDuration=i;
    }
    
    // returns the duration of an explosion
    public int getExplodeDuration(){
        return explodeDuration;
    }
    
    // sets the sprite number used for animating explosion
    public void setExplodeSprite(int n){
        explodeSprite=n;
    }
    
    // returns the sprite number used for animating explosion
    public int getExplodeSprite(){
        return explodeSprite;
    }
    
    // sets up the powerup type of the tile, -1 means that it has no powerup
    public void setPowerUpType(int n){
        if(n<9&&n>=-1)
            powerUpType=n;
    }
    
    // returns the powerup type of the tile
    public int getPowerUpType(){
        return powerUpType;
    }
} // MapTilesClass class

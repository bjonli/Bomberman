// The "BomberKeyBindingsClass" class.
import java.awt.*;
//import hsa.Console;

// The purpose of this class is to save all the keys that are responsible for bomberman movements/controls
public class BomberKeyBindingsClass
{

    private int moveUp;         // key that moves player up 
    private int moveDown;       // key that moves player down
    private int moveRight;      // key that moves player right
    private int moveLeft;       // key that moves player left
    private int plantBomb;      // key that plants bomb
     
    // constructor given a player n.
    BomberKeyBindingsClass(int n){
        switch(n){
            case 0:             // keys for player 0
                moveUp=87;
                moveDown=83;
                moveLeft=65;
                moveRight=68;
                plantBomb=69;
                break;
            case 1:             // keys for player 1
                moveUp=38;
                moveDown=40;
                moveLeft=37;
                moveRight=39;
                plantBomb=16;
                break;
            case 2:             // keys for player 2
                moveUp=84;
                moveDown=71;
                moveLeft=70;
                moveRight=72;
                plantBomb=89;
                break;
            case 3:             // keys for player 3
                moveUp=73;
                moveDown=75;
                moveLeft=74;
                moveRight=76;
                plantBomb=79;
                break;

            default:
        }
    }

    // constructor with default keys for player 
    BomberKeyBindingsClass(){
        moveUp=87;
        moveDown=83;
        moveLeft=65;
        moveRight=68;
        plantBomb=69;
    }
    
    // sets the key that moves plyaer up
    public void setKeyUp(int k){
        moveUp=k;
    }

    // sets the key that moves player down
    public void setKeyDown(int k){
        moveDown=k;
    }
    
    // sets the key that moves player left
    public void setKeyLeft(int k){
        moveLeft=k;
    }
    
    // sets the key that moves player right
    public void setKeyRight(int k){
        moveRight=k;
    }
    
    // sets key that plants bomb
    public void setKeyBomb(int k){
        plantBomb=k;
    }
    
    // returns key that moves player up
    public int keyUp(){
        return moveUp;
    }
    
    // returns key that moves player down
    public int keyDown(){
        return moveDown;
    }
    
    // returns the key that moves player left
    public int keyLeft(){
        return moveLeft;
    }
    
    // returns key that moves player right
    public int keyRight(){
        return moveRight;
    }

    // returns key that plants bomb
    public int keyBomb(){
        return plantBomb;
    }
} // BomberKeyBindingsClass class

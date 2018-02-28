// The "BombCollectionClass" class.
import java.awt.*;
//import hsa.Console;
import java.util.*;

// container class that contains a vector of bomb objects
public class BombCollectionClass
{
    private Vector bombs=new Vector();      // vecotr sotring all bomb objects currently in the field
    // used so that spriteSheets will still be saved and won't need to be reintialized. This object is actualy never used 
    private BombClass savedBomb=new BombClass();                
    
    //constuctor, nothing needed
    BombCollectionClass(){ 
    }

    // removes all bombs in the vector
    public void clearBombs(){
        bombs.clear();
    }
    
    // counts down all bombs in the vector by n
    public void countDown(int n){
        for(int i=0;i<bombs.size();i++){
            ((BombClass)bombs.elementAt(i)).lowerCountDown(n);
        }
    }
    
    // adds a bomb object to the vector given a location, power, and player
    public void addBomb(int p,int pow,int col,int row){
        bombs.add(new BombClass(p,pow,col*320+160,row*320+160));
    }
    
    // Returns the index of a bomb that needs to detonate. It either detonates if it is on an exploding tile, or if its countdown reaches zero.
    private int returnExplodingBomb(BomberMapClass map){
        for(int i=0;i<bombs.size();i++){
            if(((BombClass)bombs.elementAt(i)).getCountDown()<=0||map.getTileTerrainState(((BombClass)bombs.elementAt(i)).getCol(),((BombClass)bombs.elementAt(i)).getRow())=='E')
                return i;
        }
        return -1;              // -1 signifies that no bombs need to be detonated anymore
    }
    
    // detonates all bombs that needs to be detonated. 
    public void explodeBombs(BomberMapClass map,BomberManClass[] b){
        int i=returnExplodingBomb(map);
        // continues to start explosions as long as i does not equal -1. If it is equal to -1, then no more bombs need to be detonated.        
        while(i!=-1){   
            map.startExplosion(this,((BombClass)bombs.elementAt(i)).getCol(),((BombClass)bombs.elementAt(i)).getRow(),b);
            i=returnExplodingBomb(map);
        }
        //calls returnExplodingBomb
    }
    
    // removes the bomb from the vector
    public void removeBomb(int i,BomberManClass[] b){
        // calls removeBomb in bomberman class to decrement numBombs by 1
        b[((BombClass)bombs.elementAt(i)).getPlayer()].removeBomb();
        bombs.remove(i);
    }
    
    // removes the bomb that is in a certain col and row. It returns the power of that bomb.
    public int removeBombGetPower(int col,int row,BomberManClass[] b){
        int pow=0;
        // finds the bomb and checks if the cols and rows match. It it does, that bomb is removed.
        for(int i=0;i<bombs.size();i++){
            if(((BombClass)bombs.elementAt(i)).getCol()==col&&((BombClass)bombs.elementAt(i)).getRow()==row){
                pow=((BombClass)bombs.elementAt(i)).getBombPower();
                removeBomb(i,b);
                return pow;
            }            
        }
        return 0;
    }
    
    // draws all bombs onto the screen
    public void drawBombs(Graphics g,BomberMapClass map){
        for(int i=0;i<bombs.size();i++){
            map.drawTile(((BombClass)bombs.elementAt(i)).getCol(),((BombClass)bombs.elementAt(i)).getRow(),g);
            ((BombClass)bombs.elementAt(i)).draw(g);
        }
    }
    
    // moves all bombs in the vector
    public void moveBombs(BomberMapClass map){
        for(int i=0;i<bombs.size();i++){
            if(((BombClass)bombs.elementAt(i)).getIsMoving()&&map.canMove((BombClass)bombs.elementAt(i))){
               ((BombClass)bombs.elementAt(i)).move(); 
            }
        }
    }
    
    // returns the number of bombs in the vector
    public int getSize(){
        return bombs.size();
    }
    
    // Finds a bomb with a given col and row, it then sets that bomb to move
    public void setBombMoving(int col,int row,int dir){
        for(int i=0;i<bombs.size();i++){
            if(((BombClass)bombs.elementAt(i)).getCol()==col&&((BombClass)bombs.elementAt(i)).getRow()==row){
                ((BombClass)bombs.elementAt(i)).setIsMoving(true);
                ((BombClass)bombs.elementAt(i)).setDirection(dir);
                break;
            }
        }
    }
    
    // returns a bomb object. Does not remove bomb from vector.
    public BombClass returnBomb(int i){
        return (BombClass)bombs.elementAt(i);
    }
    // add duration for each sprite?
     
} // BombCollectionClass class

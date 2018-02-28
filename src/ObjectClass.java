// The "ObjectClass" class.
import java.awt.*;
//import hsa.Console;

// abstract class that is for all objects that have a hitbox
// This includes BombermanClass & BombClass
public abstract class ObjectClass
{
    // Refers to the coordinates of the Top-Left corner of the Game Arena
    protected static int TL_CORNERX=48;
    protected static int TL_CORNERY=120; 
    
    // The game arena stretches 4160 by 3520
    // The location of each object is measured in tenths of a pixel
    protected int xCoordinate=160;      
    protected int yCoordinate=160;
    // pixel Location of each object 
    protected int pixelLocationX=16;
    protected int pixelLocationY=16;
    // number of pixels from the center of the object to the top left corner of the object
    protected int xDist=16;    
    protected int yDist=42;
    protected int direction=2;      // direction the sprite is facing
    protected int speed=40;         // speed of the object
    protected int height=52;        // dimensions of the object
    protected int width=30;
    
    // sets the coordinates of the object, automatically updates the objects pixel Location as well
    public void setCoordinates(int x, int y){
        xCoordinate=x;
        yCoordinate=y;
        pixelLocationX=x/10;
        pixelLocationY=y/10;
    }
    
    // sets the coordinates of the object to the center of the tile located at a certain row and coloumn
    public void setCoordinatesByColRow(int col,int row){
        setCoordinates(col*320+160,row*320+160);
    }
    
    // returns the X-coordinate of the object
    public int getXCoordinate(){
        return xCoordinate;
    }
    
    // returns the Y-coordinate of the object
    public int getYCoordinate(){
        return yCoordinate;
    }
    
    // returns the X pixel Location of the object
    public int getPixelLocationX(){
        return pixelLocationX;
    }

    //returns the Y pixel Location of the object 
    public int getPixelLocationY(){
        return pixelLocationY;
    }
    
    // sets the X distance from the centre of the object to the top-left of the object
    public void setXDist(int d){
        xDist=d;
    }
    
    // sets the Y distance from the centre of the object to the top-left of the object
    public void setYDist(int d){
        yDist=d;
    }
    
    // returns the x distance between the centre of the object to the top left of the object 
    public int getXDist(){
        return xDist;
    }

    // returns the Y distance between the centre of the object to the top left of the object     
    public int getYDist(){
        return yDist;
    }
    
    // sets the direction of the object
    public void setDirection(int d){
        if(d>=0&&d<4)
            direction=d;
    }
    
    // returns the direction of the object
    public int getDirection(){
        return direction;
    }
    
    // sets the speed of the object
    public void setSpeed(int s){
        if(s>=0)    
            speed=s;
    }
    
    // increases the speed of the object by a certain number of pixels
    public void increaseSpeed(int s){
        if(speed+s>0)
            speed+=s;
    }
    
    // retursn the speed of the object
    public int getSpeed(){
        return speed;
    }
    
    // sets the height of the object
    public void setHeight(int h){       
        if (h>=0)
            height=h;
    }
    
    // returns the height of the object
    public int getHeight(){
        return height;
    }
    
    // sets the width of the object
    public void setWidth(int w){
        if (w>=0)
            width=w;
    }
    
    // returns the width of the object
    public int getWidth(){
        return width;
    }
    
    // moves the object based on the objects current speed and direction
    public void move(){
        switch (direction){
            case 0:
                setCoordinates(xCoordinate,yCoordinate+speed);
                break;
            case 1:
                setCoordinates(xCoordinate-speed,yCoordinate);
                break;
            case 2:
                setCoordinates(xCoordinate,yCoordinate-speed);
                break;
            case 3:
                setCoordinates(xCoordinate+speed,yCoordinate);
                break;
            default:
                break;
        }
    }
    
    // moves the object s units based on the objects current direction
    public void move(int s){
        if(s>0){
            switch (direction){
                case 0:
                    setCoordinates(xCoordinate,yCoordinate+s);
                    break;
                case 1:
                    setCoordinates(xCoordinate-s,yCoordinate);
                    break;
                case 2:
                    setCoordinates(xCoordinate,yCoordinate-s);
                    break;
                case 3:
                    setCoordinates(xCoordinate+s,yCoordinate);
                    break;
                default:
                    break;
            }
        }
    }    
    
    // gets the row the object is in
    public int getRow(){
        return yCoordinate/320;
    }
    
    // gets the coloumn the object is in
    public int getCol(){
        return xCoordinate/320;
    }
} // ObjectClass class

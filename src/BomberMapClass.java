// The "BomberMapClass" class.
import java.awt.*;
//import hsa.Console;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// class that performs all actions related to the battle arena. Stores all sprites used for drawing explosions/tiles. 
public class BomberMapClass
{
    // pixel location of the top left corner of the battle arena
    protected static int TL_CORNERX = 48;
    protected static int TL_CORNERY = 120;

    // Files that store sprites of each tile, explosion, and powerup
    private static File mapFile = new File ("DefaultMap.png");
    private static File explosionFile = new File ("ExplosionSprites.png");
    private static File powerUpFile=new File("PowerUps.png");
    
    // Sprite sheet that reads Image files
    private static BufferedImage mapSheet = new BufferedImage (1000, 1000, BufferedImage.TYPE_INT_ARGB);
    private static BufferedImage explosionSheet = new BufferedImage (500, 500, BufferedImage.TYPE_INT_ARGB);
    private static BufferedImage powerUpSheet=new BufferedImage(175, 75, BufferedImage.TYPE_INT_ARGB);
    
    // Array that stores the most common tiles (Walls,2 open tiles, barriers)
    private static BufferedImage[] commonBlocks = new BufferedImage [4];
    // Multidimensional array that stores the all the border sprites
    private static BufferedImage[] [] borderBlocks = new BufferedImage [3] [4];
    // 2d array that stores all the sprites used for explosions
    private static BufferedImage[] [] explosions = new BufferedImage [7] [5];
    // array used to draw all powerups
    private static BufferedImage[] powerUps= new BufferedImage[9];
    // array that stores the spawnrate of powerups
    private static int [] pUpSpawnRate = new int[9];

    // stores if the image files have been read
    private static boolean imagesSetup = false;
    // The area is bassiclaly a 11x13 grid of tiles
    private static MapTilesClass[] [] tiles = new MapTilesClass [13] [11];
    
    // variables used during sudden death, stores the tile that needs to be replaced by a wall
    private static int rowBlocked=0;
    private static int colBlocked=0;
    private static int currentRow=0;
    private static int currentCol=0;
    
    // constructor for the Map Object
    BomberMapClass ()
    {
        initializeSpriteSheet ();
        setupSprites ();
        setupTiles();
        setupPSpawnRates();
        rowBlocked=0;
        colBlocked=0;
        currentRow=0;
        currentCol=0;
    }

    // sets the image file used for map tiles
    public void setMapFile (File f)
    {
        mapFile = f;
    }


    // returns the image file used for map tiles
    public File getMapFile ()
    {
        return mapFile;
    }

    // sets the image file used for explosions
    public void setExplosionFile (File f)
    {
        explosionFile = f;
    }


    // retursn the image file used for explosions
    public File getExplosionFile ()
    {
        return explosionFile;
    }

    // sets the file used for powerups
    public void setPowerUpFile (File f)
    {
        powerUpFile = f;
    }


    // returns the file used for powerups
    public File getPowerUpFile ()
    {
        return powerUpFile;
    }
    
    // reads image files and stores it into the spritesheet variables
    public void initializeSpriteSheet ()
    {
        try
        {
            mapSheet = ImageIO.read (mapFile);
            explosionSheet = ImageIO.read (explosionFile);
            powerUpSheet=ImageIO.read(powerUpFile);
        }
        catch (IOException e)
        {
            System.out.println ("ERRORRRRRRRR Map/Explosion spritesheet not found");
        }
    }

    // sets up all the image arrays. The image arrays takes subimages from the sprite sheets
    public void setupSprites ()
    {
        // sets images for common blocks
        for (int i = 0 ; i < 4 ; i++)
        {
            commonBlocks [i] = new BufferedImage (32, 32, BufferedImage.TYPE_INT_ARGB);
            commonBlocks [i] = mapSheet.getSubimage (2 + i * 34, 1, 32, 32);
        }
        
        // sets images for border tiles
        for (int i = 0 ; i < 3 ; i++)
        {
            for (int j = 0 ; j < 4 ; j++)
            {
                borderBlocks [i] [j] = new BufferedImage (32, 32, BufferedImage.TYPE_INT_ARGB);
            }
        }
        borderBlocks [0] [0] = mapSheet.getSubimage (2 + 2 * 34, 1 + 1 * 34, 32, 32);
        borderBlocks [0] [1] = mapSheet.getSubimage (2 + 1 * 34, 1 + 2 * 34, 32, 32);
        borderBlocks [0] [2] = mapSheet.getSubimage (2 + 3 * 34, 1 + 2 * 34, 32, 32);
        borderBlocks [0] [3] = mapSheet.getSubimage (2 + 2 * 34, 1 + 3 * 34, 32, 30);
        borderBlocks [1] [0] = mapSheet.getSubimage (2 + 1 * 34, 1 + 1 * 34, 32, 32);
        borderBlocks [1] [1] = mapSheet.getSubimage (2 + 3 * 34, 1 + 1 * 34, 32, 32);
        borderBlocks [1] [2] = mapSheet.getSubimage (2 + 1 * 34, 1 + 3 * 34, 32, 30);
        borderBlocks [1] [3] = mapSheet.getSubimage (2 + 3 * 34, 1 + 3 * 34, 32, 30);
        borderBlocks [2] [0] = mapSheet.getSubimage (2 + 0 * 34, 1 + 1 * 34, 32, 32);
        borderBlocks [2] [1] = mapSheet.getSubimage (2 + 4 * 34, 1 + 1 * 34, 32, 32);
        borderBlocks [2] [2] = mapSheet.getSubimage (2 + 0 * 34, 1 + 3 * 34, 32, 30);
        borderBlocks [2] [3] = mapSheet.getSubimage (2 + 4 * 34, 1 + 3 * 34, 32, 30);

        // sets the array of explosion images
        for (int i = 0 ; i < 7 ; i++){
            for (int j = 0 ; j < 5 ; j++){
                explosions [i] [j] = new BufferedImage (32, 32, BufferedImage.TYPE_INT_ARGB);
                explosions [i] [j] = explosionSheet.getSubimage (j * 33, 1 + i * 33, 32, 32);
            }
        }
        
        // sets the array of powerup images
        for(int i=0;i<3;i++){
            for(int j=0;j<2;j++){
                powerUps[j*3+i]=new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
                powerUps[j*3+i]=powerUpSheet.getSubimage(i*34,j*34,32,32);
            }
        }
        powerUps[6]=new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
        powerUps[6]=powerUpSheet.getSubimage(3*34,1*34,32,32);  
        powerUps[7]=new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
        powerUps[7]=powerUpSheet.getSubimage(3*34,0*34,32,32);  
        powerUps[8]=new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
        powerUps[8]=powerUpSheet.getSubimage(4*34,1*34,32,32);  
    }
    
    // resets all the tiles back to the orignal 
    // It's like the next function but it does not instansiates any objects
    public void resetTiles ()
    {
        for (int i = 0 ; i < 13 ; i++)
        {
            for (int j = 0 ; j < 11 ; j++)
            {
                tiles [i] [j].setTerrainState ('Z');
                tiles [i] [j].setPowerUpType(-1);
            }
        }
        for (int i = 1 ; i < 13 ; i+=2)
        {
            for (int j = 1 ; j < 11 ; j+=2)
            {
                tiles [i] [j].setTerrainState ('W');
            }
        }
        
        for (int i = 0 ; i < 13 ; i += 12)
        {
            tiles [i] [0].setTerrainState ('O');
            tiles [i] [1].setTerrainState ('O');
            tiles [i] [9].setTerrainState ('O');
            tiles [i] [10].setTerrainState ('O');
        }
        tiles [1] [0].setTerrainState ('O');
        tiles [11] [0].setTerrainState ('O');
        tiles [11] [10].setTerrainState ('O');
        tiles [1] [10].setTerrainState ('O');

        rowBlocked=0;
        colBlocked=0;
        currentRow=0;
        currentCol=0;
        
    }

    // setup the tiles in the battle area, constructs all tiles.
    public void setupTiles (){
        for (int i = 0 ; i < 13 ; i++)
        {
            for (int j = 0 ; j < 11 ; j++)
            {
                tiles [i] [j] = new MapTilesClass ('O');
            }
        }
        for (int i = 1 ; i < 13 ; i+=2)
        {
            for (int j = 1 ; j < 11 ; j+=2)
            {
                tiles [i] [j].setTerrainState ('W');
            }
        }
        for (int i = 0 ; i < 13 ; i += 12)
        {
            tiles [i] [0].setTerrainState ('O');
            tiles [i] [1].setTerrainState ('O');
            tiles [i] [9].setTerrainState ('O');
            tiles [i] [10].setTerrainState ('O');
        }
        tiles [1] [0].setTerrainState ('O');
        tiles [11] [0].setTerrainState ('O');
        tiles [11] [10].setTerrainState ('O');
        tiles [1] [10].setTerrainState ('O');
    }
    
    // sets up the spawnrates of each powerup when a block is destroyed
    public void setupPSpawnRates(){
        pUpSpawnRate[0]=7;
        pUpSpawnRate[1]=7;
        pUpSpawnRate[2]=7;
        pUpSpawnRate[3]=3;
        pUpSpawnRate[4]=3;
        pUpSpawnRate[5]=3;
        pUpSpawnRate[6]=2;
        pUpSpawnRate[7]=3;
        pUpSpawnRate[8]=5;
    }

    // returns the explosions state of a tile in a given row and col
    public char getTileTerrainState(int col, int row){
        return tiles[col][row].getTerrainState();
    }

    // sets the terrain state of a tile in a given row and col
    public void setTileTerrainState(int col, int row, char state){
        if(col>=0&&col<13&&row>=0&&row<11){
            tiles[col][row].setTerrainState(state);
        }
    }
    
    // sets the explosions state of a tile if that tile is at the tip of an explosion
    private void setExplosionSpriteCorner(int x,int y, int a, int b){
        if(a==0){
            if(b>0){
                tiles[x][y].setExplodeSprite(2); 
            }
            else{
                tiles[x][y].setExplodeSprite(0);                             
            }
        }
        else{
            if(a>0){
                tiles[x][y].setExplodeSprite(1);                              
            }
            else{
                tiles[x][y].setExplodeSprite(3);                            
            }
        }     
    }
    
    // moves outwards in one direction to check the magnitude of each explosion
    private void explosionSpread(BombCollectionClass bombList,int col,int row,int a,int b,int pow,BomberManClass[] bc){
    // shouldBreak stores if the explosion reached a dead end   
    boolean shouldBreak=false;
        // the explosion length cannot be greater than the max explosion length or go out of bounds 
        for(int x=col+a, y=row+b; y<11 && y>=0 && Math.abs(y-row)<=pow && x<13 && x>=0 && Math.abs(x-col)<=pow; y+=b,x+=a){
            // breaks if reached deadend
            if(shouldBreak){
                break;
            }
            switch(tiles[x][y].getTerrainState()){
                case 'W':                   // wall blocks explosion
                    shouldBreak=true;   
                    break;
                case 'D':                   // technically still a barrier, acts as a wall for the explosion
                    shouldBreak=true;
                    break;
                case 'X':                   // if it hits another bomb, it starts another explosion. Recursive process
                    startExplosion(bombList,x,y,bc);
                    break;
                case 'B':                   // barrier breaks, and blocks the explosion
                    tiles[x][y].setTerrainState('D');
                    setExplosionSpriteCorner(x,y,a,b);
                    shouldBreak=true;
                    break; 
                case 'O':                   // if it is an open tile, it simply becomes an explosion tile.
                    tiles[x][y].setTerrainState('E');  
                    // Determines which explosion sprite the tile should use
                    if(Math.abs(y-row)==pow||Math.abs(x-col)==pow){  
                        setExplosionSpriteCorner(x,y,a,b);          // corner sprite
                    }
                    else if(a==0){
                        tiles[x][y].setExplodeSprite(4);            // vertical explosion sprite
                    }
                    else{
                        tiles[x][y].setExplodeSprite(5);            // horizontal explosion sprite
                    }
                    break;
                case 'E':                   // if it is an exploding tile, explosion timer is reset and the sprite to be used might need to change
                    tiles[x][y].setExplodeState(0);             
                    // if moving vertically
                    if(a==0){
                        if(tiles[x][y].getExplodeSprite()%2==1){
                            tiles[x][y].setExplodeSprite(6);                        
                        }
                        else if(tiles[x][y].getExplodeSprite()<4){
                            if((b>0&&tiles[x][y].getExplodeSprite()==0)||(b<0&&tiles[x][y].getExplodeSprite()==2)){
                                tiles[x][y].setExplodeSprite(4);
                            }
                        }
                    }
                    //if moving horizontally
                    else{
                        if(tiles[x][y].getExplodeSprite()%2==0){
                            tiles[x][y].setExplodeSprite(6);                        
                        }
                        else if(tiles[x][y].getExplodeSprite()<5){
                            if((a>0&&tiles[x][y].getExplodeSprite()==3)||(a<0&&tiles[x][y].getExplodeSprite()==1)){
                                tiles[x][y].setExplodeSprite(5);
                            }
                        }                    
                    }
                    break;
            }
        }

    } 
    
    // starts an explosion given a specific col and row the explosion is at
    public void startExplosion(BombCollectionClass bombList,int col,int row,BomberManClass[] bc){
        // gets the power of the bomb and removes the bomb from the vector
        int pow = bombList.removeBombGetPower(col,row,bc);
        //explosion starting tile is set to exploding
        tiles[col][row].setExplodeSprite(6);        
        tiles[col][row].setTerrainState('E'); 
        explosionSpread(bombList,col,row,0,1,pow,bc);   // moving downwards
        explosionSpread(bombList,col,row,0,-1,pow,bc);  // moving upwards
        explosionSpread(bombList,col,row,1,0,pow,bc);   // moving to the right
        explosionSpread(bombList,col,row,-1,0,pow,bc);  // moving to the left
    }
    
    // returns if a player can place a bomb on tile given a specific row and col
    public boolean canPlaceBomb(int col,int row){
        return tiles[col][row].getTerrainState()=='O';
    }
    
    // spawns a random powerup based on the spawnrate of each powerup
    public int spawnRandomPowerUp(){
        int n=(int)(Math.random()*100);
        for(int i=0,j=0;j<9;j++){
            if(n<i+pUpSpawnRate[j]){
                return j;
            }
            i+=pUpSpawnRate[j];
        }
        return -1;
    }
    
    // draws all explosions onto the map
    public void drawExplosions(Graphics g){
        // checks all tiles on the map
        for(int col=0;col<13;col++){
            for(int row=0;row<11;row++){
                // changes all tiles of state 'D' to state 'E'
                if(tiles[col][row].getTerrainState()=='D'){
                    tiles[col][row].setTerrainState('E');
                    tiles[col][row].setPowerUpType(spawnRandomPowerUp());
                }
                if(tiles[col][row].getTerrainState()=='E'){
                    // if the epxplosion is fading, then I can't draw an explosion sprite on top of the previous one. I will need to redraw the tile before redrawing the xplosion sprite.
                    if(tiles[col][row].getExplodeState()>(tiles[col][row].getExplodeDuration())/2){ 
                        drawTile(col,row,g);
                    }
                    // if the explosion is done, the state of the tile is set to 'O' and the open tile is drawn
                    if(tiles[col][row].getExplodeState()>=tiles[col][row].getExplodeDuration()){
                        tiles[col][row].setTerrainState('O');
                        drawTile(col,row,g);
                    }
                    else{                                       // draws the explosion
                        int i=Math.min((tiles[col][row].getExplodeState()/3-0),(8-tiles[col][row].getExplodeState()/3));// might change the 4
                        g.drawImage (explosions [tiles[col][row].getExplodeSprite()][i], TL_CORNERX + col * 32, TL_CORNERY + row * 32, null);
                        tiles[col][row].incrementExplodeState();
                    }
                }
            }
        }
    }
    
    // draws a powerUp image given at a specific x and y coordinate
    // not really used in teh battle arena, it is just in this class because the powerup Image array is in here
    public void drawPowerUp(Graphics g,int type,int x,int y){
        if(type>=0&&type<9){    
            g.drawImage(powerUps[type],x,y,null);
        }
    }
    
    // Draws the tile at a certain col and row
    public void drawTile (int col, int row, Graphics g)
    {
        switch (tiles [col] [row].getTerrainState ())
        {
            case 'W':   // draws wall
                g.drawImage (commonBlocks [0], TL_CORNERX + col * 32, TL_CORNERY + row * 32, null);
                break;
            case 'B':   // draws Barrier
                g.drawImage (commonBlocks [1], TL_CORNERX + col * 32, TL_CORNERY + row * 32, null);
                break;

            default:
                // draws powerup if the Tile has one
                if((tiles[col][row].getPowerUpType())!=-1&&(tiles[col][row].getTerrainState())!='E'){
                    g.drawImage (powerUps[tiles[col][row].getPowerUpType()], TL_CORNERX + col * 32, TL_CORNERY + row * 32, null);
                }
                // The following cases just check if the tile should have a shadow 
                else if (row == 0)
                {
                    g.drawImage (commonBlocks [2], TL_CORNERX + col * 32, TL_CORNERY + row * 32, null);
                }
                else if (tiles [col] [row - 1].getTerrainState () == 'W')
                {
                    g.drawImage (commonBlocks [2], TL_CORNERX + col * 32, TL_CORNERY + row * 32, null);
                }
                else    // no shadow
                {
                    g.drawImage (commonBlocks [3], TL_CORNERX + col * 32, TL_CORNERY + row * 32, null);
                }
                break;
        }
    }


    // Draws All Tiles on the map
    public void drawAllTiles (Graphics g){
        for (int i = 0 ; i < 13 ; i++){
            for (int j = 0 ; j < 11 ; j++){
                drawTile (i, j, g);
            }
        }
    }

    // draws all the border tiles
    public void drawAllBorder (Graphics g)
    {
        for (int i = 0 ; i < 12 ; i++)
        {
            g.drawImage (borderBlocks [0] [0], TL_CORNERX + 16 + i * 32, TL_CORNERY - 16, null);
        }
        for (int i = 0 ; i < 10 ; i++)
        {
            g.drawImage (borderBlocks [0] [1], TL_CORNERX - 16, TL_CORNERY + 16 + 32 * i, null);
        }
        for (int i = 0 ; i < 10 ; i++)
        {
            g.drawImage (borderBlocks [0] [2], TL_CORNERX + 32 * 13 - 16, TL_CORNERY + 16 + 32 * i, null);
        }
        for (int i = 0 ; i < 12 ; i++)
        {
            g.drawImage (borderBlocks [0] [3], TL_CORNERX + 16 + i * 32, TL_CORNERY + 32 * 11 - 18, null);
        }
        g.drawImage (borderBlocks [1] [0], TL_CORNERX - 16, TL_CORNERY - 16, null);
        g.drawImage (borderBlocks [1] [1], TL_CORNERX + 32 * 13 - 16, TL_CORNERY - 16, null);
        g.drawImage (borderBlocks [1] [2], TL_CORNERX - 16, TL_CORNERY + 32 * 11 - 18, null);
        g.drawImage (borderBlocks [1] [3], TL_CORNERX + 32 * 13 - 16, TL_CORNERY + 32 * 11 - 18, null);
        for (int i = 0 ; i < 11 ; i++)
        {
            g.drawImage (borderBlocks [2] [0], TL_CORNERX - 16 - 32, TL_CORNERY - 16 + i * 32, null);
        }
        for (int i = 0 ; i < 11 ; i++)
        {
            g.drawImage (borderBlocks [2] [1], TL_CORNERX + 13 * 32 + 16, TL_CORNERY - 16 + i * 32, null);
        }
        g.drawImage (borderBlocks [2] [2], TL_CORNERX - 16 - 32, TL_CORNERY - 18 + 11 * 32, null);
        g.drawImage (borderBlocks [2] [3], TL_CORNERX + 13 * 32 + 16, TL_CORNERY - 18 + 11 * 32, null);
    }

    // helper function that redraws all tiles from a certain x and y range
    private void redrawCoveredTilesHelper (Graphics g, int x1, int y1, int w, int h)
    {
        // redraws tiles that the object was drawn on.
        for (int i = Math.max (0, x1 / 32) ; i * 32 < x1 + w && i<13 ; i++)
        {
            for (int j = Math.max (0, y1 / 32) ; j * 32 < y1 + h && j<11  ; j++)
            {
                drawTile (i, j, g);
            }
        }
        // redraws border blocks if necessary
        if (y1 < 0)
        {
            for (int i = Math.max (1, (x1+32) / 32) ; i <= (x1 + w+ 32+16)/32 ; i++)
            {
                g.drawImage (borderBlocks [0] [0], TL_CORNERX - 16 + (i-1) * 32, TL_CORNERY - 16, null);
            }
        }
    }
    
    // draws all tiles that were covered when a bomberman/bomb was drawn on top. Avoids drawing all tiles every time.
    public void redrawCoveredTiles(Graphics g, BomberManClass[] b,int numBombers,BombCollectionClass list){
        // draws tiles covered by bombermen
        for(int i=0;i<numBombers;i++){
            redrawCoveredTilesHelper(g,b[i].getPixelLocationX()-b[i].getXDist(),b[i].getPixelLocationY()-b[i].getYDist(),b[i].getWidth(),b[i].getHeight());
        }
        // draws tiles covered by bombs
        for(int i=0;i<list.getSize();i++){
            redrawCoveredTilesHelper(g,list.returnBomb(i).getPixelLocationX()-list.returnBomb(i).getXDist(),list.returnBomb(i).getPixelLocationY()-list.returnBomb(i).getYDist(),list.returnBomb(i).getWidth(),list.returnBomb(i).getHeight());
        }
        // draws tiles that may be covered by the center message
        for(int i=2;i<=10;i++){
            for(int j=4;j<=5;j++){
                drawTile(i,j,g);
            }
        }
        
    }

    // draws the entire map, including tiles and borders
    public void drawAll (Graphics g)
    {
        drawAllTiles (g);
        drawAllBorder (g);
    }
    
    // checks if player is reasonably well centered. Used to check if bomberman can move to another tile.
    private boolean withinRange(int c){
        if(c%320<220&&c%320>100){
            return true;
        }
        return false;
    }
    
    // returns if a tile can even be walked on. 
    private boolean canEnterNextTile(int col,int row){
        if (col>=0 && col<13 && row>=0 && row<11){
            if(tiles[col][row].getCanWalkOn()){
                return true;
            }
        }
        return false;
    } 
    
    // returns whether a certain bomb can be kicked.
    private boolean canKickBomb(int col, int row){
        if(col>=0&&row>=0&&col<13&&row<11){
            if(tiles[col][row].getTerrainState()=='X'){
                return true;
            }
        }
        return false;
    }
    
    // readjusts the locatin of the bomberman 
    private boolean adjustBomberMan(BombCollectionClass bc,BomberManClass b,int x, int y, int x2,int y2){
        // if moving vertically
        if(x==x2){
            if(y2>y){                                                   // if moving downwards
                // checks if it can enter the next tile if it were to move again
                if(canEnterNextTile(x2/320,y2/320+1))
                    return true;
                else{
                    // checks if it can kcik a bomb
                    if(canKickBomb(x2/320,y2/320+1)&&b.getCanKick()){
                        bc.setBombMoving(x2/320,y2/320+1,0);
                    }
                    b.setCoordinates(x,Math.min(y2,(y/320)*320+190));
                    b.increaseAnimationCounter();
                    return false;
                }
            }
            else{                                                       // if moving upwards
                if(canEnterNextTile(x2/320,y2/320-1))   
                    return true; 
                else{
                    if(canKickBomb(x2/320,y2/320-1)&&b.getCanKick()){
                        bc.setBombMoving(x2/320,y2/320-1,2);
                    }
                    b.setCoordinates(x,Math.max(y2,(y/320)*320+130));  
                    b.increaseAnimationCounter();
                    return false;
                }            
            }
        }
        // if moving horizontally
        else{                                                          
            if(x2>x){                                                   // if moving to the right
                if(canEnterNextTile(x2/320+1,y2/320))
                    return true; 
                else{
                    if(canKickBomb(x2/320+1,y2/320)&&b.getCanKick()){
                        bc.setBombMoving(x2/320+1,y2/320,3);
                    }
                    b.setCoordinates(Math.min(x2,(x/320)*320+190),y); 
                    b.increaseAnimationCounter();  
                    return false;
                }
            }
            else{                                                       // if moving to the left
                if(canEnterNextTile(x2/320-1,y2/320))
                    return true; 
                else{
                    if(canKickBomb(x2/320-1,y2/320)&&b.getCanKick()){
                        bc.setBombMoving(x2/320-1,y2/320,1);
                    }
                    b.setCoordinates(Math.max(x2,(x/320)*320+130),y);   
                    b.increaseAnimationCounter();
                    return false;
                }
            }
        }
    
    }
    
    // checks if the bomberman can move
    private boolean canMoveHelper(BombCollectionClass bc,BomberManClass b,int x, int y, int x2,int y2){
        // if the bomberman stays in the same tile, the location of the bomberman is readjusted
        if(x/320 == x2/320&&y/320 == y2/320)
            return adjustBomberMan(bc,b,x,y,x2,y2);
        // if the bomberman can enter the next tile
        else if(canEnterNextTile(x2/320,y2/320)){
            // since the bomberman is no longer on the tile, the tile will not be occupied.
            tiles[x/320][y/320].setIsOccupied(false);
            if (x==x2){
                b.setCoordinates((x/320)*320+160,y);
                return true;
            }
            else{
                b.setCoordinates(x,(y/320)*320+160);
                return true;
            }
        }
        // if the bomberman cannot enter the next tile, the location of the bomberman is set to the edge of the tile.
        else{
            if(x==x2){
                if(y2>y)
                    b.setCoordinates(x,(y/320)*320+270); 
                else
                    b.setCoordinates(x,(y/320)*320+50); 
            }
            else{
                if(x2>x)
                    b.setCoordinates((x/320)*320+270,y); 
                else
                    b.setCoordinates((x/320)*320+50,y); 
            }
            b.increaseAnimationCounter();
            return false;
        }
    }
    
    // helper function that returns if a bomb can move 
    public boolean canMoveHelperFunction(int x1,int y1,int x2,int y2,int disX,int disY,BombClass b){
        // A bomb cannot move out of bounds
        if(x2/320<0||x2/320>=13||y2/320<0||y2/320>=11){
            b.setCoordinates((x1/320)*320+160,(y1/320)*320+160);
            b.setIsMoving(false);
            return false;         
        }
        // If the bomb is moving to an unOccupied Tile
        else if((y2/320!=y1/320||x2/320!=x1/320) && !(tiles[x2/320][y2/320].getIsOccupied())){
            if(tiles[x2/320][y2/320].getTerrainState()!='E'){            
                tiles[x2/320][y2/320].setTerrainState('X');
            }
            tiles[x2/320][y2/320].setPowerUpType(-1);   
            tiles[x1/320][y1/320].setTerrainState('O');
            return true;
        }
        // if the bomb is going to partially/fully enter another tile
        else if((y2+disY)/320!=(y1+disY)/320||(x2+disX)/320!=(x1+disX)/320||(x2+disX)<0||(y2+disY)<0){
            // a bomb cannot partially enter a new Tille
            if((x2+disX)/320<0||(x2+disX)/320>=13||(y2+disY)/320<0||(y2+disY)/320>=11){
                b.setCoordinates((x1/320)*320+160,(y1/320)*320+160);
                b.setIsMoving(false);
                return false;                
            }
            // case when the bomb is partially moving into an unOccupied Tile
            else if(tiles[(x2+disX)/320][(y2+disY)/320].getIsOccupied()==false){
                return true;
            }
            // case when the bomb hits a barrier (Ex: bomb, wall)
            else{
                b.setCoordinates((x1/320)*320+160,(y1/320)*320+160);
                b.setIsMoving(false);
                return false;
            }
        }    
        // case where the bomb stays in its original tile
        else{       
            return true;
        }
    }
    
    // checks if a bomb can move
    public boolean canMove(BombClass b){
        switch(b.getDirection()){
            case 0:
                return canMoveHelperFunction(b.getXCoordinate(),b.getYCoordinate(),b.getXCoordinate(),b.getYCoordinate()+b.getSpeed(),0,159,b);           
            case 1:
                return canMoveHelperFunction(b.getXCoordinate(),b.getYCoordinate(),b.getXCoordinate()-b.getSpeed(),b.getYCoordinate(),-159,0,b); 
            case 2:
                return canMoveHelperFunction(b.getXCoordinate(),b.getYCoordinate(),b.getXCoordinate(),b.getYCoordinate()-b.getSpeed(),0,-159,b); 
            case 3:
                return canMoveHelperFunction(b.getXCoordinate(),b.getYCoordinate(),b.getXCoordinate()+b.getSpeed(),b.getYCoordinate(),159,0,b); 
            default:
                return false;
        }
    }
    
    // checks if a bomberman can move 
    public boolean canMove (BombCollectionClass bc,BomberManClass b)
    {
        // checks the direction the bomberman is facing
        switch (b.getDirection())
        {
            case 0: 
                return (canMoveHelper(bc,b,b.getXCoordinate(),b.getYCoordinate(),b.getXCoordinate(),b.getYCoordinate()+b.getSpeed()));
            case 1:
                return (canMoveHelper(bc,b,b.getXCoordinate(),b.getYCoordinate(),b.getXCoordinate()-b.getSpeed(),b.getYCoordinate()));
            case 2:
                return (canMoveHelper(bc,b,b.getXCoordinate(),b.getYCoordinate(),b.getXCoordinate(),b.getYCoordinate()-b.getSpeed()));
            case 3:
                return (canMoveHelper(bc,b,b.getXCoordinate(),b.getYCoordinate(),b.getXCoordinate()+b.getSpeed(),b.getYCoordinate()));
            default:
                return false;
        }
    }
    
    public void checkBomberMan(BomberManClass b){
        // If the bomberman is on a tile with a pwoerup, the bomberman collects that powerup
        if(tiles[b.getCol()][b.getRow()].getPowerUpType()!=-1){
            switch(tiles[b.getCol()][b.getRow()].getPowerUpType()){
                case 0:                                     // increase max bombs
                    b.setMaxBombs(b.getMaxBombs()+1);
                    break;
                case 1:                                     // increase speed level
                    b.setSpeedPower(b.getSpeedPower()+1);
                    break;
                case 2:                                     // increase bomb power
                    b.setBombPower(b.getBombPower()+1);
                    break;
                case 3:                                     // decrease max bombs
                    b.setMaxBombs(b.getMaxBombs()-1);
                    break;
                case 4:                                     // decrease speed power
                    b.setSpeedPower(b.getSpeedPower()-1);
                    break;
                case 5:                                     // decrease bomb power
                    b.setBombPower(b.getBombPower()-1);
                    break;
                case 6:                                     // set bomb power to max
                    b.setBombPower(b.getMaxBombPower());            
                    break;
                case 7:                                     // skull powerup, has 3 possible types
                    b.setSkullType((int)(Math.random()*3)+1);
                    b.setSkullTimer(b.getSkullTimerStart());
                    break;
                case 8:                                     // set can kick
                    b.setCanKick(true);
                    break;
            }
            tiles[b.getCol()][b.getRow()].setPowerUpType(-1);   
        }
        // if the bomberman is inside of an explosion, the bomberman dies.
        if(tiles[b.getCol()][b.getRow()].getTerrainState()=='E'){
            b.kill();
        }
     
    }
    
    // Changes the map so that every tile that a bomberman/bomb is on is occupied
    public void findOccupiedTiles(BomberManClass[] b,int s,BombCollectionClass list){
        for(int i=0;i<s;i++){               // bombermen
            tiles[b[i].getCol()][b[i].getRow()].setIsOccupied(true);
        }
        for(int i=0;i<list.getSize();i++){  //bombs
            tiles[list.returnBomb(i).getCol()][list.returnBomb(i).getRow()].setIsOccupied(true);        
        }
    }
    
    // fillmap gradually shortens the battle arena be placing outer tiles with walls. 
    public void fillMap(BomberManClass[] b,int num,Graphics g,BombCollectionClass bombList){
        // sets the sepcific tile to wall and redraws
        tiles[currentCol][currentRow].setTerrainState('W');
        drawTile(currentCol,currentRow,g); 
        // removes any bombs on that tile
        bombList.removeBombGetPower(currentCol,currentRow,b);
        // kills any bombermen on that tile
        for(int i=0;i<num;i++){
            if(b[i].getIsAlive()&&b[i].getRow()==currentRow&&b[i].getCol()==currentCol){
                b[i].kill();
            }
        }
        // locates the next tile to change into a wall
        // If all tiles in the outermost layer are walls, the next outermost layer will be filled
        if(currentCol-1==colBlocked&&currentRow==rowBlocked){       
            // stops once 4 layers have been filled, creates 3x5 battle arena
            if(currentCol!=4){
                colBlocked=currentCol;
                currentRow++;
                rowBlocked++; 
            }
        }
        // starts at the top left corner of the layer
        else if(currentRow==rowBlocked &&currentCol==colBlocked){
            currentRow++;
        }
        // if on top side, moving left
        else if(currentRow==rowBlocked){
            currentCol--;
        }
        // if on right side, moving up
        else if(currentCol==12-colBlocked){
            currentRow--;
        }
        // if on bottom side, moving right
        else if(currentRow==10-rowBlocked){
            currentCol++;
        }
        else{   // if moving down, on left side
            currentRow++;
        }
    }

    
} // BomberMap class

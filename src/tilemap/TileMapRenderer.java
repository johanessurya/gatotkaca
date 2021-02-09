package tilemap;

import java.awt.*;
import java.util.Iterator;

import graphics.Sprite;

import tilegame.sprites.Creature;
import tilegame.sprites.JabangTetuka;
import tilegame.background.*;

/**
 * Class <code>TileMapRenderer</code> berguna dalam menggambar <code>TileMap</code>
 * di layar termasuk <code>Sprite</code> yang ada di <code>TileMap</code> tersebut
 * @author Yohanes Surya
 */
public class TileMapRenderer {
	/**
	 * Membuat object baru
	 */
    public TileMapRenderer(){
    	//Untuk 2.5D
    	this.offsetYplus=0;    	
    }
    
    /**
     * Penambahan posisi dari semuah tile
     * @param y <code>int</code> yang ditetapkan
     */
    public void setOffsetYplus(int y){
    	this.offsetYplus=y;
    }
    
    /**
     * Mengubah posisi pixel(dalam satuan pixel) ke posisi tile(index 
     * <code>TileMap</code>)
     * @param pixels <code>float</code> pixel yang ditetapkan
     * @return <code>int</code> yang diminta
     */
    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }

    /**
     * Mengubah posisi pixel(dalam satuan pixel) ke posisi tile(index 
     * <code>TileMap</code>)
     * @param pixels <code>int</code> pixel yang ditetapkan
     * @return <code>int</code> yang diminta
     */
    public static int pixelsToTiles(int pixels) {
        // use shifting to get correct values for negative pixels
        return pixels >> TILE_SIZE_BITS;

        // or, for tile sizes that aren't a power of two,
        // use the floor function:
        //return (int)Math.floor((float)pixels / TILE_SIZE);
    }
    /**
     * Mengubah posisi tile ke posisi pixel
     * @param pixels <code>float</code> pixel yang ditetapkan
     * @return <code>int</code> yang diminta
     */

    /**
     * Mengubah posisi tile ke posisi pixel
     * @param numTiles <code>int</code> index <code>TileMap</code> yang 
     * ditetapkan
     * @return pixel yang diminta
     */
    public static int tilesToPixels(int numTiles) {
        // no real reason to use shifting here.
        // it's slighty faster, but doesn't add up to much
        // on modern processors.
        return numTiles << TILE_SIZE_BITS;

        // use this if the tile size isn't a power of 2:
        //return numTiles * TILE_SIZE;
    }

    /**
     * Menggambar <code>TileMap</code> yang ditetapkan
     * @param g <code>Graphics2D</code> yang ditetapkan
     * @param map <code>TileMap</code> yang ditetapkan
     * @param screenWidth <code>int</code> yang ditetapkan
     * @param screenHeight <code>int</code> yang ditetapkan
     */
    public void draw(Graphics2D g, TileMap map,
        int screenWidth, int screenHeight)
    {
        Sprite player = map.getPlayer();
        int mapWidth = tilesToPixels(map.getWidth());
        
        //System.out.println(mapWidth);

        // get the scrolling position of the map
        // based on player's position
        int offsetX = screenWidth / 2 -
            Math.round(player.getX()) - TILE_SIZE;
        
        //System.out.printf("PlayerX=%s; Tile Size=%s \n",player.getX(), TILE_SIZE);
        //System.out.println(offsetX);
        
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidth);

        //Modified
        int offsetY=screenHeight/2 - Math.round(player.getY())-TILE_SIZE;
        offsetY=Math.min(offsetY,0);
        offsetY=Math.max(offsetY, screenHeight-tilesToPixels(map.getHeight()));
        
        // get the y offset to draw all sprites and tiles
//        int offsetY = screenHeight -
//            tilesToPixels(map.getHeight());
         
        //System.out.printf("ScreenWidth=%s; PlayerX=%s; OffsetX=%s; OffsetY=%s; \n",
          //      screenWidth,player.getX(),offsetX,offsetY);
        
        // draw black background, if needed
        if (background == null ||
            screenHeight > background.getHeight(null))
        {
            g.setColor(Color.black);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }

        // draw parallax background image
        if (background != null) {
            int x = offsetX *
                (screenWidth - background.getWidth(null)) /
                (screenWidth - mapWidth);
            int y = screenHeight - background.getHeight(null);

            g.drawImage(background, x, y, null);
        }
        
        //Background Parallax
        if(backgroundParallax!=null){
        	backgroundParallax.update(offsetX);
        	backgroundParallax.draw(g);        	
        }
        
        // draw the visible tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX +
            pixelsToTiles(screenWidth) + 1;
        for (int y=0; y<map.getHeight(); y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                Image image = map.getTile(x, y);
                if (image != null) {
                    g.drawImage(image,
                        tilesToPixels(x) + offsetX,
                        tilesToPixels(y) + offsetY,
                        null);
                }
            }
        }

        // draw status player
        JabangTetuka jt=(JabangTetuka)player;

        // draw player
        int tempX=this.offsetYplus;
        if(!jt.isOnGround())
        	tempX=0;
        player.draw(g, offsetX, offsetY+tempX);

        // draw sprites
        Iterator<Sprite> i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            int x = Math.round(sprite.getX()) + offsetX;
            
            sprite.draw(g, offsetX, offsetY+offsetYplus);

            // wake up the creature when it's on screen
            if (sprite instanceof Creature &&
                x >= 0 && x < screenWidth)
            {
                ((Creature)sprite).wakeUp();
            }
        }
        
        //Foreground Parallax
        if(foregroundParallax!=null){
        	foregroundParallax.update(offsetX);
        	foregroundParallax.draw(g);        	
        }

    }

    /**
     * Menambahkan <code>ParallaxManager</code> untuk background
     * @param p <code>ParallaxManager</code> yang ditetapkan
     */
    public void addBackgroundParallaxManager(ParallaxManager p){
    	backgroundParallax=p;
    }
    
    /**
     * Menambahkan <code>ParallaxManager</code> untuk foreground
     * @param p <code>ParallaxManager</code> yang ditetapkan
     */
    public void addForegroundParallaxManager(ParallaxManager p){
    	foregroundParallax=p;
    }
    
    private static final int TILE_SIZE = 64;
    // the size in bits of the tile
    // Math.pow(2, TILE_SIZE_BITS) == TILE_SIZE
    private static final int TILE_SIZE_BITS = 6;

    private Image background;
    private int offsetYplus;
    private ParallaxManager backgroundParallax;
    private ParallaxManager foregroundParallax;
}

package tilemap;

import java.awt.Image;
import java.util.LinkedList;
import java.util.Iterator;

import graphics.Sprite;

/**
 * Class <code>TileMap</code> membawa data-data Sprite yang mengacu ke object 
 * gambar
 * @author Yohanes Surya
 */
public class TileMap {
	/**
	 * Membuat object baru dengan lebar dan panjang yang di tetapkan
	 * @param width <code>int</code> yang ditetapkan
	 * @param height <code>int</code> yang ditetapkan
	 */
    public TileMap(int width, int height) {
        tiles = new Image[width][height];
        sprites = new LinkedList<Sprite>();
    }

    /**
     * Mendapatkan lebar <code>TileMap</code>
     * @return lebar <code>TileMap</code>
     */
    public int getWidth() {
        return tiles.length;
    }

    /**
     * Mendapatkan panjang <code>TileMap</code>
     * @return panjang <code>TileMap</code>
     */
    public int getHeight() {
        return tiles[0].length;
    }

    /**
     * Mendapatkan tile di lokasi yang ditetapkan
     * @param x index X
     * @param y index Y
     * @return gambar tile
     */
    public Image getTile(int x, int y) {
        if (x < 0 || x >= getWidth() ||
            y < 0 || y >= getHeight())
        {
            return null;
        }
        else {
            return tiles[x][y];
        }
    }

    /**
     * Memasang tile di lokasi yang ditetapkan
     * @param x index X
     * @param y index Y
     * @param tile gambar tile
     */
    public void setTile(int x, int y, Image tile) {
        tiles[x][y] = tile;
    }

    /**
     * Mendapatkan <code>Sprite</code> player
     * @return <code>Sprite</code> player
     */
    public Sprite getPlayer() {
        return player;
    }

    /**
     * Memasang <code>Sprite</code> player
     * @param player <code>Sprite</code> player
     */
    public void setPlayer(Sprite player) {
        this.player = player;
    }

    /**
     * Menambah <code>Sprite</code> object
     * @param sprite <code>Sprite</code> yang ditetapkan
     */
    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    /**
     * Menghapus <code>Sprite</code> object
     * @param sprite <code>Sprite</code> yang ditetapkan
     */
    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    }

    /**
     * Mendapatkan semua <code>Sprite</code> di map ini. Tidak termasuk 
     * <code>Sprite</code> player
     * @return <code>Sprite</code> <code>Iterator</code>
     */
    public Iterator<Sprite> getSprites() {
        return sprites.iterator();
    }

    private Image[][] tiles;
    private LinkedList<Sprite> sprites;
    private Sprite player;
}

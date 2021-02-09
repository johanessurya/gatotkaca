package tilemap;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Class <code>MenuList</code> menghandle penggambaran beberapa 
 * <code>MenuEntry</code> ke layar.
 * @author Yohanes Surya
 */
public class MenuList{
	/**
	 * Menggambar <code>MenuList</code> kelayar
	 * @param g <code>Graphics2D</code> yang ditetapkan
	 */
    public void draw(Graphics2D g) {
		g.setRenderingHint(
	            RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    	for(int i=0; i<menuEntries.size(); i++){
    			menuEntries.get(i).draw(g);
    	}
    }	
	
    /**
     * Menambah class <code>MenuEntry</code> kedalam <code>MenuList</code>
     * @param menuEntry <code>MenuEntry</code> yang ditetapkan
     */
	public void addMenuEntry(MenuEntry menuEntry){
		menuEntries.add(menuEntry);
	}
	
	/**
	 * Mengambil <code>MenuEntry</code> index ke-n
	 * @param index index yang ditetapkan
	 * @return <code>MenuEntry</code> index ke-n
	 */
	public MenuEntry getMenuEntry(int index){
		return menuEntries.get(index);
	}
	
	/**
	 * Memposisikan tulisan di tengah-tengah dari kelebaran yang ditetapkan
	 * @param g <code>Graphics2D</code> yang ditetapkan
	 * @param font <code>Font</code> yang ditetapkan
	 * @param text <code>String</code> yang ditetapkan
	 * @param width lebar yang ditetapkan
	 * @return posisi X
	 */
	public static int getCenterPosX(Graphics2D g, Font font, String text, int width){
		FontRenderContext context=g.getFontRenderContext();
		Rectangle2D bounds=font.getStringBounds(text, context);
		
		int x=(int)((width-bounds.getWidth())/2);
		return x;
	}

    private ArrayList<MenuEntry> menuEntries=new ArrayList<MenuEntry>();
}

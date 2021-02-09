package tilemap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Class <code>MenuEntry</code> adalah abstract class jadi tidak dapat digunakan
 * secara langsung. Class ini berguna untuk membuat menu list menurun. Class yang
 * digunakan haruslah subclassnya
 * @author Yohanes Surya
 *
 */
public abstract class MenuEntry {
	/**
	 * Membuat object baru
	 * @param point <code>Point</code> yang ditetapkan
	 */
	public MenuEntry(Point point){
		this.point=point;
	}
	
	/**
	 * Mengambil <code>Point<code>
	 * @return <code>Point</code> object <code>MenuEntry</code>
	 */
	public Point getPoint(){
		return point;
	}
	
	/**
	 * Untuk mengambar list menu
	 * @param g <code>Graphics2D</code> yang ditetapkan
	 */
	public abstract void draw(Graphics2D g);
	
	/**
	 * Mengambil object
	 * @return <code>Object</code> yang diminta
	 */
	public abstract Object getContent();
	
	/**
	 * Class <code>Image</code> berguna untuk membuat menu list berupa gambar
	 * @author Yohanes Surya
	 *
	 */
	public static class Image extends MenuEntry{
		/**
		 * Membuat object baru
		 * @param image <code>Image</code> yang ditetapkan
		 * @param point <code>Point</code> yang ditetapkan
		 */
		public Image(java.awt.Image image,Point point){
			super(point);
			this.image=image;
		}
		
		@Override
		public java.awt.Image getContent() {
			return image;
		}
		
		public void draw(Graphics2D g){
	    	g.drawImage(image, getPoint().x, getPoint().y, null);
		}
		
		private java.awt.Image image;

	}
	
	/**
	 * Class <code>Text</code> berguna untuk membuat menu list berupa text
	 * @author Yohanes Surya
	 */
	public static class Text extends MenuEntry{
		/**
		 * Membuat object baru
		 * @param font <code>Font</code> yang ditetapkan
		 * @param color <code>Color</code> yang ditetapkan
		 * @param text <code>String</code> yang ditetapkan
		 * @param point <code>Point</code> yang ditetapkan
		 */
		public Text(Font font, Color color, 
				String text,Point point){
			super(point);
			this.font=font;
			this.text=text;
			this.color=color;
		}
		
		@Override
		public String getContent() {
			return text;
		}
		
		@Override
		public void draw(Graphics2D g) {
			// TODO Auto-generated method stub
	    	g.setFont(font);
	    	g.setColor(color);
	    	g.drawString(text, getPoint().x, getPoint().y);			
		}

		private String text;
		private Font font;
		private Color color;
	}
	
	private Point point;
}

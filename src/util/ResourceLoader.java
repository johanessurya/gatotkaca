package util;

import gatotkaca.Main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Class <code>ResourceLoader</code> berguna untuk mengload default resource yang
 * ada pada file jar itu sendiri.
 * @author Yohanes Surya
 */
public class ResourceLoader {
	/**
	 * Mengload font file
	 * @param filename lokasi file font
	 * @return font yang diminta
	 */
	public static Font loadFont(String filename){
		//System.out.println(filename);
		InputStream in=Main.class.getResourceAsStream("font/"+filename);
		Font font=null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, in);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return font;
	}
	
	/**
	 * Mengload file
	 * @param filename lokasi file
	 * @return <code>BufferedReader</code> yang diminta
	 */
	public static BufferedReader loadFile(String filename){
		BufferedReader reader=null;
		InputStream inputStream=Main.class.getResourceAsStream(filename);
		InputStreamReader inputStreamReader=new InputStreamReader(
				inputStream);
		reader=new BufferedReader(inputStreamReader);			
		return reader;
	}
	
	/**
	 * Mengload semua tile image yang ada
	 * @return Array of Image
	 */
	public static ArrayList<Image> loadTileImages(){
        // keep looking for tile A,B,C, etc. this makes it
        // easy to drop new tiles in the images/ directory
        ArrayList<Image> tiles = new ArrayList<Image>();
        char ch = 'A';
        while (true) {
            String name = "tile_" + ch + ".png";
            InputStream in=Main.class.getResourceAsStream("images/" + name);
            if (in==null) {
                break;
            }
            tiles.add(loadImage("images/"+name));
            ch++;
        }
		return tiles;
	}
	
	/**
	 * Mengload <code>Image</code> dari lokasi yang ditetapkan
	 * @param name lokasi file
	 * @return <code>Image</code> yang diminta
	 */
    public static Image loadImage(String name) {
    	URL url=Main.class.getResource(name);
        return new ImageIcon(url).getImage();
    }
    
    /**
     * Mengload file
     * @param name lokasi file
     * @return <code>InputStream</code> yang diminta
     */
    public static InputStream loadInputStream(String name){
    	return Main.class.getResourceAsStream(name);
    }
}

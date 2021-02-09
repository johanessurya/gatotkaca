package util;

import java.awt.Image;
import java.util.ArrayList;

import tilemap.TileMap;

/**
 * Class <code>SectionResource</code> menyimpan resource satu episode
 * @author Yohanes Surya
 */
public class SectionResource {
	/**
	 * Membuat object baru
	 * @param title judul episode
	 * @param isJabangTetuka <code>true</code> jika jabang tetuka ingin ditampilkan 
	 * dan <code>false</code> jika sebaliknya
	 */
	public SectionResource(String title, boolean isJabangTetuka){
		this.title=title;
		this.isJabangTetuka=isJabangTetuka;
		this.opening=new ArrayList<Image>();
		this.maps=new ArrayList<TileMap>();
		this.ending=new ArrayList<Image>();
	}
	
	/**
	 * Menambah gambar komik pembuka
	 * @param comic <code>Image</code> yang ditetapkan
	 */
	public void addOpening(Image comic){
		opening.add(comic);
	}
	
	/**
	 * Mengambil title episode ini
	 * @return <code>String</code> episode
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * Menambah map
	 * @param map <code>TileMap</code> yang ditetapkan
	 */
	public void addMap(TileMap map){
		maps.add(map);
	}
	
	/**
	 * Menambah gambar komik penutup
	 * @param comic <code>Image</code> yang ditetapkan
	 */
	public void addEnding(Image comic){
		ending.add(comic);
	}
	
	/**
	 * Mendapatkan komik dalam bentuk array of <code>Image</code>
	 * @return semua komik pembuka
	 */
	public ArrayList<Image> getOpening(){
		return opening;
	}
	
	/**
	 * Mendapatkan semua map di episode ini dalam bentuk array of 
	 * <code>TileMap</code>
	 * @return semua map yang ada
	 */
	public ArrayList<TileMap> getMaps(){
		return maps;
	}
	
	/**
	 * Mendapatkan komik dalam bentuk array of <code>Image</code>
	 * @return semua komik penutup
	 */
	public ArrayList<Image> getEnding(){
		return ending;
	}
	
	/**
	 * Check apakah class <code>JabangTetuka<code> yang aktif di episode ini
	 * @return <code>true</code> jika jabang tetuka ingin ditampilkan 
	 * dan <code>false</code> jika sebaliknya
	 */
	public boolean isJabangTetuka(){
		return isJabangTetuka;
	}

	private String title;
	private boolean isJabangTetuka;
	private ArrayList<Image> opening;
	private ArrayList<TileMap> maps;
	private ArrayList<Image> ending;
}

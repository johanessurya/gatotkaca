package util;
import java.util.ArrayList;

/**
 * Class <code>SimpleSection</code> menyimpan data mentah sebelum dijadikan ke 
 * <code>SectionResource</code>. 
 * @author Yohanes Surya
 */
public class SimpleSection {
	/**
	 * Membuat class baru tanpa judul yang spesifik. Class ini akan mengisinya 
	 * dengan title "Untitle"
	 */
	public SimpleSection(){
		this.title="Untitle";
		this.opening=new ArrayList<String>();
		this.maps=new ArrayList<String>();
		this.ending=new ArrayList<String>();
		this.isJabangTetuka=true;
	}
	
	/**
	 * Membuat class baru dengan judul yang spesifik
	 * @param title judul yang ditetapkan
	 */
	public SimpleSection(String title){
		this();
		this.title=title;
	}
	
	/**
	 * Mengset title baru
	 * @param title judul yang ditetapkan
	 */
	public void setTitle(String title){
		this.title=title;
	}
	
	/**
	 * Set hero untuk episode ini.
	 * @param isJabangTetuka <code>true</code> jika jabang tetuka ingin ditampilkan 
	 * dan <code>false</code> jika sebaliknya
	 */
	public void setPlayer(boolean isJabangTetuka){
		this.isJabangTetuka=isJabangTetuka;
	}
	
	/**
	 * Menambah nama komik pembuka
	 * @param comic <code>String</code> yang ditetapkan
	 */
	public void addOpening(String comic){
		opening.add(comic);
	}

	/**
	 * Menambah nama komik penutup
	 * @param comic <code>String</code> yang ditetapkan
	 */
	public void addEnding(String comic){
		ending.add(comic);
	}

	/**
	 * Menambah map
	 * @param map <code>String</code> yang ditetapkan
	 */
	public void addMap(String map){
		this.maps.add(map);
	}
	
	/**
	 * Mengambil title episode ini
	 * @return <code>String</code> episode
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	 * Mendapatkan komik dalam bentuk array of <code>String</code>
	 * @return semua komik pembuka
	 */
	public ArrayList<String> getOpening(){
		return opening;
	}
	
	/**
	 * Mendapatkan komik dalam bentuk array of <code>Image</code>
	 * @return semua komik penutup
	 */
	public ArrayList<String> getEnding(){
		return ending;
	}

	/**
	 * Mendapatkan semua map di episode ini dalam bentuk array of 
	 * <code>String</code>
	 * @return semua map yang ada
	 */
	public ArrayList<String> getMaps(){
		return maps;
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
	private ArrayList<String> opening;
	private ArrayList<String> maps;
	private ArrayList<String> ending;
	private boolean isJabangTetuka;
}

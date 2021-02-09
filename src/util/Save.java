package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class <code>Save</code> berfungsi untuk membaca maupun menulis data-data save 
 * game yang diperlukan dan menyimpannya kedalam sebuah file.
 * @author Yohanes Surya
 */
public class Save {
	/**
	 * Membuat object baru
	 */
	public Save(){
		this.filename="save.dat";
		read();
	}
	
	/**
	 * Membaca save file yang ada. Jika tidak ditemukan maka akan membuat file 
	 * dengan isi yang mengidentifikasikan bahwa program baru saja di mainkan di
	 * komputer ini
	 */
	public void read(){
		File file=new File(filename);
		if(file.exists()){
			BufferedReader buf;
			try {
				buf = new BufferedReader(new FileReader(filename));
				stage=buf.readLine();
				buf.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			write(0);
			read();
		}
	}
	
	/**
	 * Menulis stage pada file
	 * @param stage state yang akan di save.
	 */
	public void write(int stage){
		try {
			PrintWriter out=new PrintWriter(new FileOutputStream(filename));
			out.println(MD5Encrypt.encrypt(Integer.toString(stage)));
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Memdapatkan stage
	 * @return String stage yang diminta ("1", "2", dst)
	 */
	public String getStage(){
		return stage;
	}
	
	private String filename;
	private String stage;
}

package util;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Class <code>HighScoreManager</code> menghandel penulisan dan pembacaan 
 * high score kedalam file
 * @author Yohanes Surya
 */
public class HighScoreManager {
	/**
	 * Membuat object baru
	 */
	public HighScoreManager(){
		read();
	}
	
	/**
	 * Mendapatkan object <code>HighScore</code>
	 * @return <code>HighScore</code> yang diminta
	 */
	public HighScore getHighScore(){
		return highScore;
	}
	
	/**
	 * Menambah score baru. Method ini sama dengan memanggil method 
	 * <code>addScore()</code> yang ada dalam object <code>HighScore</code>
	 * @param score <code>Score</code> yang ditetapkan
	 */
	public void addHighScore(Score score){
		highScore.addScore(score);
	}
	
	/**
	 * Menulis high score ke file
	 */
	public void write(){
		String filename="highscore.dat";
		try {
			File file=new File(filename);
			file.createNewFile();
			ObjectOutputStream out=new ObjectOutputStream(
					new FileOutputStream(file));
			out.writeObject(highScore);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Membaca high score dari file
	 */
	public void read(){
		//init file
		File highScoreFile=new File("highscore.dat");
		
		//create file if not exist
		if(!highScoreFile.exists()){
			highScore=new HighScore();
		}else{
			try {
				ObjectInputStream in=new ObjectInputStream(
						new FileInputStream(highScoreFile));
				
				Object temp=in.readObject();
				in.close();
				if(temp instanceof HighScore)
				{
					highScore=(HighScore)temp;
				}else {
					highScore=new HighScore();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				highScore=new HighScore();
				e.printStackTrace();
			} catch (EOFException e) {
				highScore=new HighScore();
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				highScore=new HighScore();
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				highScore=new HighScore();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				highScore=new HighScore();
				e.printStackTrace();
			}
		}
		
	}

	private HighScore highScore;
}

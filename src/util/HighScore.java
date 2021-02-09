package util;

import java.io.Serializable;

/**
 * Class <code>HighScore</code> menghandel pengolahan <code>HighScore</code>.
 * Class ini secara otomatis akan mengurutkan dari yang terbesar hingga yang
 * terkecil
 * @author Yohanes Surya
 */
public class HighScore implements Serializable{
	/**
	 * Membuat object baru
	 */
	public HighScore(){
		score=new Score[5];
		loadBlank();
	}
	
	private void loadBlank(){
		for(int i=0; i<score.length; i++){
			score[i]=new Score("BLANK",0);
		}
	}
	
	/**
	 * Mendapatkan semua score dalam bentuk array
	 * @return array score
	 */
	public Score[] getHighScore(){
		return score;
	}
	
	/**
	 * Menambahkan score baru. Secara otomatis akan membuang score yang paling 
	 * kecil dan mengganti score yang lebih tinggi.
	 * @param score <code>Score</code> yang ditetapkan
	 */
	public void addScore(Score score){
		for(int i=0; i<this.score.length; i++){
			if(score.getScore()>this.score[i].getScore()){
				Score temp=score;
				score=this.score[i];
				this.score[i]=temp;
			}
		}
	}
	
	/**
	 * Untuk mencetak semua high score
	 */
	public void printInfo(){
		for(int i=0; i<score.length; i++){
			System.out.println(score[i].getName()+" "+score[i].getScore());
		}
		
	}
	
	private Score[] score;
}

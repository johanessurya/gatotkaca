package util;

import java.io.Serializable;

/**
 * Class <code>Score</code> menghandel satu buah score yang terdiri dari nama dan
 * score itu sendiri. Di class ini terdiri dari 2 method yaitu 
 * <code>getName()</code> dan <code>getScore()</code>.
 * @author Yohanes Surya
 */
public class Score implements Serializable{
	/**
	 * Membuat object baru
	 * @param name <code>String</code> yang ditetapkan
	 * @param score <code>int</code> yang ditetapkan
	 */
	public Score(String name, int score){
		this.name=name;
		this.score=score;
	}
	
	/**
	 * Mendapatkan nama
	 * @return nama yang diminta
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Mendapatkan score
	 * @return score yang diminta
	 */
	public int getScore(){
		return score;
	}
	
	private String name;
	private int score;
}

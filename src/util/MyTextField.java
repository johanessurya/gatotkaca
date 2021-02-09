package util;

import java.awt.Color;
import javax.swing.JTextField;

/**
 * Class <code>MyTextField</code> adalah extends dari JTextField. Class ini berguna
 * untuk submit score pada akhir permainan.
 * @author Yohanes Surya
 */
public class MyTextField extends JTextField{
	/**
	 * Membuat object baru
	 * @param text <code>String</code> yang akan ditampilkan di textbox pertama kali
	 */
	public MyTextField(String text){		
		super(text);
		
		setOpaque(false);
		setBorder(null);
		setForeground(new Color(36,16,13));
        setLocation(100, 100);
	}
}
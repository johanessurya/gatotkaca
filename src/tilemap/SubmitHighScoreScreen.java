package tilemap;

import graphics.ScreenManager;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import tilegame.background.ParallaxByTime;
import util.GameCore;
import util.HighScoreManager;
import util.MyTextField;
import util.NullRepaintManager;
import util.ResourceLoader;
import util.Score;

/**
 * Class <code>SubmitHingScoreScreen</code> adalah class untuk menhandel 
 * display high score dan memasukkannya ke database
 * @author Yohanes Surya
 */
public class SubmitHighScoreScreen extends GameCore
	implements ActionListener{
	/**
	 * Membuat object baru
	 * @param screen <code>ScreenManager</code> yang ditetapkan
	 * @param score <code>int</code> yang ditetapkan
	 */
	public SubmitHighScoreScreen (ScreenManager screen, int score){
		this.screen=screen;
		this.score=score;
		
		this.background=loadImage("layer0.jpg");
		this.parallax=new ParallaxByTime(background,.6f, screen.getFrame());
		this.highScoreManager=new HighScoreManager();
	}
	
	private Image loadImage(String filename){
		return ResourceLoader.loadImage("images/bg/"+filename);
	}

    public void run() {
        init();
        gameLoop();
    }

	@Override
	public void init() {
		initSwing();

		//Init bg scroll
        scrollImage=ResourceLoader.loadImage("images/highscore_submit.png");

		//initInput();
    	highScoreTextField.requestFocusInWindow();
		start();
	}
	
	private void initSwing(){
        font=ResourceLoader.loadFont("taileb.ttf");
		font=font.deriveFont(Font.BOLD, 30f);

		NullRepaintManager.install();

		frame = super.screen.getFrame();
        Container contentPane = frame.getContentPane();
        
        // make sure the content pane is transparent
        if (contentPane instanceof JComponent) {
            ((JComponent)contentPane).setOpaque(false);
        }

        highScoreTextField=new MyTextField("YourName");
        highScoreTextField.addActionListener(this);
        highScoreTextField.setFont(font);
        highScoreTextField.selectAll();
        highScoreTextField.setFocusable(true);
        
        highScoreTextField.setSize(highScoreTextField.getPreferredSize());
        highScoreTextField.setLocation(
        		(screen.getWidth()-highScoreTextField.getWidth())/2-100, 
        		(screen.getHeight()-highScoreTextField.getHeight())/2+25);

        screen.getFrame().getLayeredPane().
        	add(highScoreTextField,JLayeredPane.MODAL_LAYER);

        // explicitly lay out components (needed on some systems)
        frame.validate();
	}

	public void stop(){
		highScoreManager.write();
		frame.removeAll();
		frame.requestFocusInWindow();
		super.stop();
	}
	
	@Override
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
    	//checkInput();
    	parallax.update(elapsedTime);
	}

	@Override
	public void draw(final Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		
		parallax.draw(g);
		drawScroll(g);
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
			    public void run() {
			    	frame.getLayeredPane().paintComponents(g);
			    }
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//frame.getLayeredPane().paintComponents(g);
		drawScore(g);
	}

	private void drawScore(Graphics2D g){
		g.setFont(font);
		g.setColor(new Color(36,16,13));
		g.drawString(Integer.toString(score), highScoreTextField.getX()+250, highScoreTextField.getY()+30);
	}
	
	private void drawScroll(Graphics2D g){
		int x,y;
		x=(screen.getWidth()-scrollImage.getWidth(null))/2;
		y=(screen.getHeight()-scrollImage.getHeight(null))/2;
		
		g.drawImage(scrollImage, x, y, null);
	}
	        
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String temp=highScoreTextField.getText();
		temp=filter(temp);
		
		//highScore.addScore(new Score(temp,score));
		highScoreManager.addHighScore(new Score(temp,score));
		stop();
	}
	
	private String filter(String target){
		String temp="";
		String permitStr="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		
		for(int i=0; i<target.length(); i++)
		{
			boolean isValid=false;
			int j=0;
			while(j<permitStr.length() && !isValid){
				if(target.charAt(i)==permitStr.charAt(j)){
					isValid=true;
					temp+=target.charAt(i);
				}
				j++;
			}
		}
		
		return temp;
	}
	
    private Image background;
    private ParallaxByTime parallax;

    //Game Action
	private MyTextField highScoreTextField;
    private JFrame frame;
    
    private Image scrollImage;
    private Font font;
    
    private HighScoreManager highScoreManager;
    private int score;
}

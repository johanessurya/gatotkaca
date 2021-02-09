package tilemap;

import input.GameAction;
import input.InputManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;

import graphics.ScreenManager;
import sound.Sound;
import sound.SoundManager;
import util.GameCore;
import util.ResourceLoader;

/**
 * Class <code>CreditScreen</code> menghandle display pada saat "credit"
 * @author Yohanes Surya
 *
 */
public class CreditScreen extends GameCore{
	/**
	 * Membuat object baru
	 * @param screen <code>ScreenManager</code> yang ditetapkan
	 * @param resourceManager <code>ResourceManager</code> yang ditetapkan 
	 */
	public CreditScreen(ScreenManager screen, ResourceManager resourceManager){
		this.screen=screen;
		this.resourceManager=resourceManager;
		this.soundManager=this.resourceManager.getSoundManager();

		font=ResourceLoader.loadFont("arial.ttf");
		font=font.deriveFont(30f);

		bgImage=ResourceLoader.loadImage("images/loading/credit.jpg");
		bgMusic=resourceManager.getCreditBgMusic();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		initInput();
		initSound();
		start();
	}

	private void initSound(){
		bgMusicInputStream=soundManager.play(bgMusic,null,true);
	}

	@Override
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
		checkInput();
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setRenderingHint(
	            RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.drawImage(bgImage, 0, 0, null);
		g.fillRect(600, 0, screen.getWidth()-600, screen.getHeight());

		int y=200;
		drawString(g, font.deriveFont(Font.BOLD,40f), "Created By", y);
		drawString(g, font.deriveFont(Font.BOLD,20f), "Yohanes Surya Kusuma", y+50);

		y+=200;
		drawString(g, font.deriveFont(Font.BOLD,40f), "Special Thanks To", y);
		drawString(g, font.deriveFont(Font.BOLD,20f), "Benny Swatan Susanto", y+50);
	}

	private void drawString(Graphics2D g,Font font, String text, int y){
		int x=0;
		int width=screen.getWidth();

		FontRenderContext context=g.getFontRenderContext();
		Rectangle2D bounds=font.getStringBounds(text, context);

		x=(int)((width-bounds.getWidth())/2)+300;

		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(text, x, y);
	}

    public void run(){
        init();
        gameLoop();
    }

    private void initInput(){
        //Init Game Action
        moveRight=new GameAction("right", GameAction.DETECT_INITAL_PRESS_ONLY);
        moveLeft=new GameAction("left", GameAction.DETECT_INITAL_PRESS_ONLY);
        moveUp=new GameAction("up");
        moveDown=new GameAction("down");
        skip=new GameAction("skip");
        exit=new GameAction("exit", GameAction.DETECT_INITAL_PRESS_ONLY);

        //Init Input
        inputManager=new InputManager(screen.getFrame());
        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(moveUp, KeyEvent.VK_UP);
        inputManager.mapToKey(moveDown, KeyEvent.VK_DOWN);
        inputManager.mapToKey(skip, KeyEvent.VK_ENTER);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
    }

    private void checkInput() {
        if (exit.isPressed() || skip.isPressed()) {
            stop();
        }
    }

    public void stop(){
    	if(bgMusicInputStream!=null)
    	try {
			bgMusicInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	super.stop();
    }

    private Font font;

    private GameAction moveRight;
    private GameAction moveLeft;
    private GameAction moveUp;
    private GameAction moveDown;
    private GameAction skip;
    private GameAction exit;

	private InputManager inputManager;

	private Image bgImage;

	private Sound bgMusic;
	private InputStream bgMusicInputStream;

	private ResourceManager resourceManager;
	private SoundManager soundManager;
}

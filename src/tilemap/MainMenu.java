package tilemap;

import graphics.ImageMan;
import graphics.ScreenManager;

import input.GameAction;
import input.InputManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import sound.SoundManager;
import tilegame.background.ParallaxByTime;
import util.GameCore;
import util.HighScore;
import util.HighScoreManager;
import util.MD5Encrypt;
import util.ResourceLoader;
import util.Save;
import util.Score;
import util.SectionResource;
import util.SimpleMath;

/**
 * Class <code>MainMenu</code> berguna untuk menampilkan main menu pada awal 
 * permainan ini
 * @author Yohanes Surya
 */
public class MainMenu extends GameCore {
	/**
	 * State Main Menu
	 */
    public static final int STATE_MAIN_MENU=-1;
    
    /**
     * State New Game
     */
    public static final int STATE_NEW_GAME=0;
    
    /**
     * State Level Select
     */
    public static final int STATE_LEVEL_SELECT=1;
    
    /**
     * State High Score
     */
    public static final int STATE_HIGH_SCORE=2;
    
    /**
     * State Exit
     */
    public static final int STATE_EXIT=3;

    /**
     * Membuat object baru
	 * @param screen <code>ScreenManager</code> yang ditetapkan
	 * @param resourceManager <code>ResourceManager</code> yang ditetapkan 
     */
	public MainMenu(ScreenManager screen, 
			ResourceManager resourceManager){
		this.screen=screen;
		this.font=ResourceLoader.loadFont("taileb.ttf");
		this.font=font.deriveFont(Font.PLAIN, 30);
		this.highScore=new HighScoreManager().getHighScore();
		this.resourceManager=resourceManager;
		this.soundManager=resourceManager.getSoundManager();
		this.sectionResources=resourceManager.getSectionResources();
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.state=STATE_MAIN_MENU;
		initBackground();
		initMenuList();
		initInput();
		initSound();
		initMaxLevel();
		start();
	}
	
	private void initMaxLevel(){
		Save save=new Save();
		
		for(int i=0; i<sectionResources.size(); i++)
			if(save.getStage().equals(MD5Encrypt.encrypt(Integer.toString(i))))
				this.maxLevel=i;
	}
	
    public void run() {
        init();
        gameLoop();
    }

    private void initSound(){
    	bgMusicIs=soundManager.play(resourceManager.getBgMainMenuMusic(),null,true);
    }
    
	private void initMenuList(){
		this.cursor=loadImage("cursor.png");
		
        this.menuList=new MenuList();
        
        Image[] images=new Image[4];
        images[0]=loadImage("newgame.png");
        images[1]=loadImage("selectlevel.png");
        images[2]=loadImage("highscore.png");
        images[3]=loadImage("exit.png");
        
        Point tempPoint;
        for(int i=0; i<images.length; i++){
            tempPoint=new Point(300,(i*40)+350);
            tempPoint.x=tempPoint.x+(screen.getWidth()-images[i].getWidth(null))/2;
            this.menuList.addMenuEntry(
            		new MenuEntry.Image(images[i], tempPoint));
        }
	}
	
	private void initBackground(){
		this.transparancy=screen.createCompatibleImage(screen.getWidth(),
				screen.getHeight(), Transparency.TRANSLUCENT);
		Graphics2D g=(Graphics2D)transparancy.getGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		
		this.transparancy=ImageMan.drawTransparent(this.transparancy, 0.9f);
		
		this.title=loadImage("title.png");
		this.scroll=loadImage("scroll.png");
		this.scrollLvl=loadImage("levelselect.png");
		this.background=loadImage("layer0.jpg");
		this.gatotkaca=loadImage("gatotkaca.png");
		this.arrowUp=loadImage("arrowup.png");
		this.arrowDown=loadImage("arrowdown.png");
		this.parallax=new ParallaxByTime(background,.6f, screen.getFrame());
	}
	
	private Image loadImage(String filename){
		return ResourceLoader.loadImage("images/bg/"+filename);
	}
	
    private void initInput(){
        //Init Game Action
        moveUp=new GameAction("up", GameAction.DETECT_INITAL_PRESS_ONLY);
        moveDown=new GameAction("down", GameAction.DETECT_INITAL_PRESS_ONLY);
        backspace=new GameAction("back", GameAction.DETECT_INITAL_PRESS_ONLY);
        enter=new GameAction("enter", GameAction.DETECT_INITAL_PRESS_ONLY);
        exit=new GameAction("exit", GameAction.DETECT_INITAL_PRESS_ONLY);

        //Init Input
        inputManager=new InputManager(screen.getFrame());
        inputManager.mapToKey(moveUp, KeyEvent.VK_UP);
        inputManager.mapToKey(moveDown, KeyEvent.VK_DOWN);
        inputManager.mapToKey(enter, KeyEvent.VK_ENTER);
        inputManager.mapToKey(backspace, KeyEvent.VK_BACK_SPACE);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
    }

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setRenderingHint(
	            RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		parallax.draw(g);

		g.drawImage(gatotkaca, 0, 0, null);

        if(state==STATE_MAIN_MENU){
    		g.drawImage(title, 
    				676, 
    				247, 
    				null);

    		menuList.draw(g);

    		int x,y;
    		MenuEntry menuEntry=menuList.getMenuEntry(choose);
    		x=menuEntry.getPoint().x-cursor.getWidth(null)-10;
    		y=menuEntry.getPoint().y;
    		g.drawImage(cursor, x, y, null);     
        }
        else if(state==STATE_HIGH_SCORE){
        	g.drawImage(transparancy, 0, 0, null);
        	drawHighScore(g);
        }
        
        else if(state==STATE_LEVEL_SELECT){
        	g.drawImage(transparancy, 0, 0, null);
        	
        	//draw scroll
        	g.drawImage(scrollLvl, 
        			(screen.getWidth()-scrollLvl.getWidth(null))/2, 
        			(screen.getHeight()-scrollLvl.getHeight(null))/2, 
        			null);
        	g.setFont(font);
        	g.setColor(new Color(36,16,13));
        	
        	//draw arrow up
        	g.drawImage(arrowUp, 
        			(screen.getWidth()-arrowUp.getWidth(null))/2, 
        			300, 
        			null);

        	//draw arrow down
        	g.drawImage(arrowDown, 
        			(screen.getWidth()-arrowUp.getWidth(null))/2, 
        			400, 
        			null);
        	
        	String partTitle=sectionResources.get(indexLevel).getTitle();
        	Point point=getCenterPos(g,font,partTitle);
        	//g.drawString(partTitle, point.x, point.y+50);
        	g.drawString(partTitle, point.x, 390); //465,390
        	g.setFont(font.deriveFont(14f));
        	g.drawString("Backspace to back", 565, 452);
        	
        	//g.drawString(Integer.toString(indexLevel), 300, 300);
        }
        
        //g.drawString(Integer.toString(choose), 300, 300);
	}
	
	private Point getCenterPos(Graphics2D g, Font font, String text){
		Point point=new Point();
		FontRenderContext context=g.getFontRenderContext();
		Rectangle2D bounds=font.getStringBounds(text, context);
		
		point.x=(int)((screen.getWidth()-bounds.getWidth())/2);
		point.y=(int)((screen.getHeight()-bounds.getWidth())/2);
		
		return point;
	}

	@Override
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
        checkInput();
		parallax.update(elapsedTime);
	}

    private void checkInput(){
    	if (exit.isPressed()) {
  	      stop();
  	  	}

    	if(state==STATE_MAIN_MENU){
    		if(moveUp.isPressed()){
    			choose -=1;
    		}
    		else if(moveDown.isPressed()){
    			choose +=1;
    		}
    		else if(enter.isPressed()){
    			switch(choose){
    			case STATE_MAIN_MENU:
    				state=STATE_MAIN_MENU;
    				break;
    			case STATE_NEW_GAME:
    				state=STATE_NEW_GAME;
    				stop();
    				break;
    			case STATE_HIGH_SCORE:
    				state=STATE_HIGH_SCORE;
    				break;
    			case STATE_LEVEL_SELECT:
    				state=STATE_LEVEL_SELECT;
    				break;
    			case STATE_EXIT:
    				state=STATE_EXIT;
    				stop();
    				break;
    			}
    		}
    		
    		choose=SimpleMath.minMax(0, choose, 3);
    	}
    	else if(state==STATE_HIGH_SCORE){
    		if(enter.isPressed()){
    			state=STATE_MAIN_MENU;
    		}
    	}
    	else if(state==STATE_LEVEL_SELECT){
    		if(moveUp.isPressed()){
    			indexLevel -=1;
    		}
    		else if(moveDown.isPressed()){
    			indexLevel +=1;
    		}
    		else if(backspace.isPressed()){
    			state=STATE_MAIN_MENU;
    		}
    		else if(enter.isPressed()){
        		indexLevel=SimpleMath.minMax(0, indexLevel, maxLevel);
    			stop();
    		}    		
    		indexLevel=SimpleMath.minMax(0, indexLevel, maxLevel);
    	}
    }
    
	/**
	 * Mengambil keadaan object ini
	 * @return konstanta STATE
	 */
    public int getState(){
    	return state;
    }
    
    /**
     * Mengambil level yang di pilih
     * @return level yang di pilih
     */
    public int getLevelChoose(){
    	return indexLevel;
    }
    
    public void stop(){
    	if(bgMusicIs!=null){
    		try {
				bgMusicIs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

    	super.stop();
    }
    
    private void drawHighScore(Graphics2D g){
    	g.drawImage(scroll, 270, 40, null);
    	
		//Draw Name
    	int x=308;
    	int y=265;
    	g.setFont(font);
    	g.setColor(new Color(36,16,13));
    	int j=0;
    	
    	//Center Title HIGH SCORE
    	int width=screen.getWidth();
    	drawCenterString(g,font,"HIGH SCORE",width);
    	Score[] temp=highScore.getHighScore();
    	for(int i=0; i<temp.length; i++){
    		j=i+1;
    		//Name
    		g.drawString(Integer.toString(j)+". "+temp[i].getName(), x, y);
    		//Score
    		g.drawString(Integer.toString(temp[i].getScore()), x+300, y);
    		y+=50;
    	}
    }
    
    private void drawCenterString(Graphics2D g, 
    		Font font, String text, int width){
    	FontRenderContext context=g.getFontRenderContext();
    	Rectangle2D rect=font.getStringBounds(text, context);
    	
    	double x=(width-rect.getWidth())/2;
    	
    	g.drawString(text, (int)x, 150);
    }
    
	
	private InputManager inputManager;
	private ArrayList<SectionResource> sectionResources;
    
    //Game Action
    private GameAction moveUp;
    private GameAction moveDown;
    private GameAction enter;
    private GameAction exit;
    private GameAction backspace;
    
    private Image title;
    private Image transparancy;
    private Image background;
    private Image cursor;
    private Image gatotkaca;
    private Image scroll;
    private Image scrollLvl;
    private Image arrowUp;
    private Image arrowDown;
    private ParallaxByTime parallax;
    private MenuList menuList;
    
    private InputStream bgMusicIs;
    
    private ResourceManager resourceManager;
	private SoundManager soundManager;
    
	//High Score
    private HighScore highScore;
    
    private Font font;
    
    private int choose;
    private int indexLevel;
    private int state;
    private int maxLevel;    
}

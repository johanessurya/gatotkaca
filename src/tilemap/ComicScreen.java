package tilemap;

import input.GameAction;
import input.InputManager;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import sound.Sound;
import sound.SoundManager;
import util.GameCore;
import util.SimpleMath;

import graphics.ScreenManager;

/**
 * Class <code>ComicScreen</code> menghandle permainan saat pada keadaan KOMIK
 * @author Yohanes Surya 
 *
 */
public class ComicScreen extends GameCore{
	/**
	 * Membuat object baru
	 * @param screen <code>ScreenManager</code> yang ditetapkan
	 * @param resourceManager <code>ResourceManager</code> yang ditetapkan 
	 * @param comics ArrayList Image
	 */
	public ComicScreen(ScreenManager screen, ResourceManager resourceManager,
				ArrayList<Image> comics){
		this.screen=screen;
		this.comics=comics;
		this.index=0;
		this.pos=new Point();
		this.resourceManager=resourceManager;
		this.bgMusic=resourceManager.getComicBgMusic();
		this.pageNext=resourceManager.getPageNextSound();
		this.pageBack=resourceManager.getPageBackSound();
		this.soundManager=resourceManager.getSoundManager();
		this.showTutorial=false;
		this.hasShowed=false;
		this.stateTime=0;
	}
	
	/**
	 * Membuat object baru
	 * @param screen <code>ScreenManager</code> yang ditetapkan
	 * @param resourceManager <code>ResourceManager</code> yang ditetapkan 
	 * @param comics ArrayList Image
	 * @param showTutorial <code>true</code> akan memunculkan tutorial singkat
	 * dan <code>false</code> untuk sebaliknya
	 */
	public ComicScreen(ScreenManager screen, ResourceManager resourceManager,
			ArrayList<Image> comics,
			boolean showTutorial){
		this(screen,resourceManager,comics);

		this.showTutorial=showTutorial;
	}
	
	public void init(){
        initInput();
        initSound();
        initFramePopUp();

		this.state=STATE_PLAY;
        start();
	}
	
	private void initFramePopUp(){
    	framePopUp=resourceManager.getComicFramePopUp();
	}

	private void initSound(){
		bgMusicIs=soundManager.play(bgMusic,null,true);
	}
	
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		if(comics.size()>0)
			g.drawImage(comics.get(index), pos.x, pos.y, null);
		
		if(state==STATE_FRAME_POP_UP)
	    	framePopUp.draw(g, 
	    			(screen.getWidth()-framePopUp.getWidth())/2, 
	    			(screen.getHeight()-framePopUp.getHeight())/2);
	}

	public void update(long elapsedTime){
    	if(state==STATE_PLAY){
    		statePlay(elapsedTime);
    	}
    	else if(state==STATE_FRAME_POP_UP){
    		stateFramePopUp(elapsedTime);
    	}		
	}
	
	private void statePlay(long elapsedTime){
    	if(!hasShowed && showTutorial){
        	stateTime+=elapsedTime;
        	if(stateTime>TUTORIAL_SHOW){
        		state=STATE_FRAME_POP_UP;
        		hasShowed=true;
        		framePopUp.fadeIn();
        	}    		
    	}

    	checkInput();		
	}
	
    private void stateFramePopUp(long elapsedTime){
    	checkInput();
    	framePopUp.update(elapsedTime);
    	
    	if(framePopUp.getState()==FramePopUp.STATE_FADE_OUT && 
    			framePopUp.isFinish()){
    		state=STATE_PLAY;
    	}
    }
	
    private void initInput(){
        //Init Game Action
        moveRight=new GameAction("right", GameAction.DETECT_INITAL_PRESS_ONLY);
        moveLeft=new GameAction("left", GameAction.DETECT_INITAL_PRESS_ONLY);
        moveUp=new GameAction("up");
        moveDown=new GameAction("down");
        skip=new GameAction("skip", GameAction.DETECT_INITAL_PRESS_ONLY);
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
    
    public void stop(){
    	try {
			bgMusicIs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	super.stop();
    }

    private void checkInput() {    	
        if (exit.isPressed()) {
            stop();
        }
        
        if(state==STATE_PLAY)
        {
	        if (moveLeft.isPressed()) {
	        	index--;
	        	if(index<comics.size() && index>=0){
	        		soundManager.play(pageBack);
	        		pos.y=0;
	        	}
	        }
	        else if (moveRight.isPressed()) {
	        	index++;
	        	if(index<comics.size() && index>=0){
	        		soundManager.play(pageNext);
	        		pos.y=0;
	        	}
	        }
	        else if (moveUp.isPressed()) {
	        	pos.y+=10;
	        }
	        else if (moveDown.isPressed()) {
	        	pos.y-=10;
	        }
	        else if(skip.isPressed()){
	        	stop();
	        }
	        
	        //checkIndex();
	        index=SimpleMath.minMax(0, index, comics.size()-1);
	        pos.y=SimpleMath.minMax(
	        		screen.getHeight()-comics.get(index).getHeight(null),
	        		pos.y, 0);
        }
        else if(state==STATE_FRAME_POP_UP){
        	if(skip.isPressed()){
        		framePopUp.fadeOut();
        	}
        }
    }
    
    public void run(){
        init();
        gameLoop();
    }
        
    private GameAction moveRight;
    private GameAction moveLeft;
    private GameAction moveUp;
    private GameAction moveDown;
    private GameAction skip;
    private GameAction exit;
    
    private Sound bgMusic;
    private Sound pageNext;
    private Sound pageBack;

    private SoundManager soundManager;
    
    private ResourceManager resourceManager;
    private InputStream bgMusicIs;

    private int index;
    
	private ArrayList<Image> comics;
	private InputManager inputManager;
	private Point pos;
	
	private FramePopUp framePopUp;
	
	private int state;
	private long stateTime;
	
	private boolean showTutorial;
	private boolean hasShowed;

    private static final long TUTORIAL_SHOW=1000;
    
    private static final int STATE_PLAY=0;    
    private static final int STATE_FRAME_POP_UP=2;

}

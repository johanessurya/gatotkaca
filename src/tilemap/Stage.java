package tilemap;

import graphics.*;
import input.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import sound.Sound;
import sound.SoundManager;

import tilegame.sprites.*;
import util.GameCore;
import util.ResourceLoader;

/**
 * Class <code>Stage</code> menghandel permainan itu sendiri. Membuat tiap 
 * karakter dapat berjalan dan saling berinteraksi
 * @author Yohanes Surya
 */
public class Stage extends GameCore{
	/**
	 * State PLAY
	 */
    public static final int STATE_PLAY=0;
    
    /**
     * State WIN
     */
    public static final int STATE_WIN=1;
    
    /**
     * State LOSE
     */
    public static final int STATE_LOSE=2;
    
    /**
     * State PAUSE
     */
    public static final int STATE_PAUSE=3;
    

    /**
     * State EXIT
     */
    public static final int STATE_EXIT=4;

    /**
     * Membuat object <code>Stage</code> baru
     * @param screen <code>ScreenManager</code> yang ditetapkan
     * @param resourceManager <code>ResourceManager</code> yang ditetapkan
     * @param map <code>TileMap</code> yang ditetapakan
     * @param loading <code>LoadingScreen</code> yang ditetapkan
     * @param player <code>JabangTetuka</code> yang ditetapkan
     */
	public Stage(ScreenManager screen, 
			ResourceManager resourceManager,
			TileMap map, LoadingScreen loading,
			JabangTetuka player){
		
		this.screen=screen;
		this.resourceManager=resourceManager;
		this.loading=loading;
        this.soundManager = resourceManager.getSoundManager();
        this.renderer=this.resourceManager.getTileMapRenderer();	
		this.map=map;
		this.showTutorial=false;
		this.player=player;
		this.timeFont=ResourceLoader.loadFont("comicbd.ttf");
		this.timeFont=timeFont.deriveFont(Font.BOLD, 40f);
		
        //Sprite player = (Sprite)playerSprite.clone();
        player.setX(TileMapRenderer.tilesToPixels(3));
        player.setY(-400);
        map.setPlayer(player);
        
        if(player instanceof JabangTetuka){
        	JabangTetuka temp=(JabangTetuka) player;
        	temp.addMap(map);        	
        }

		
		this.bossStage=false;
		Iterator<Sprite> i=map.getSprites();
        while(i.hasNext()){
        	Sprite temp=(Sprite)i.next();
        	if(temp instanceof Nagapecona){
        		this.bossStage=true;
        		this.bossSprite=(Creature)temp;
        	}
        }
        
        //Load Sound
        if(bossStage)
        	stageSound=resourceManager.getBossBgMusic();
        else
        	stageSound=resourceManager.getNormalBgMusic();
        
        this.cursor=loadImage("bg/cursor.png");
        this.pauseAnim=new PauseAnimation(loadAlphaBg());
        this.pause_choose=1;
        
        //Load Menu List for pause menu
        loadMenuList();
	}
	
	/**
     * Membuat object <code>Stage</code> baru
     * @param screen <code>ScreenManager</code> yang ditetapkan
     * @param resourceManager <code>ResourceManager</code> yang ditetapkan
     * @param map <code>TileMap</code> yang ditetapakan
     * @param loading <code>LoadingScreen</code> yang ditetapkan
     * @param player <code>JabangTetuka</code> yang ditetapkan
	 * @param showTutorial <code>true</code> jika tutorial ingin ditampilkan dan 
	 * <code>false</code> jika sebaliknya
	 */
	public Stage(ScreenManager screen, 
			ResourceManager resourceManager,
			TileMap map, LoadingScreen loading,
			JabangTetuka player, boolean showTutorial){
		this(screen,resourceManager,map,loading,player);
		
		this.showTutorial=showTutorial;
	}
		
	private void loadMenuList(){
        this.menuList=new MenuList();
        
        Image[] images=new Image[3];
        images[0]=loadImage("bg/pause.png");
        images[1]=loadImage("bg/resume.png");
        images[2]=loadImage("bg/exit.png");
        
        Point tempPoint=new Point(0,200);
        tempPoint.x=(screen.getWidth()-images[0].getWidth(null))/2;
        this.menuList.addMenuEntry(
        		new MenuEntry.Image(images[0], tempPoint));	
        for(int i=1; i<images.length; i++){
            tempPoint=new Point(0,(i*30)+230);
            tempPoint.x=(screen.getWidth()-images[i].getWidth(null))/2;
            this.menuList.addMenuEntry(
            		new MenuEntry.Image(images[i], tempPoint));	
        }

	}
	
	private Image loadImage(String filename){
		return ResourceLoader.loadImage("images/"+filename);
	}
	
	private Image loadAlphaBg(){
        GraphicsConfiguration gc=screen.getFrame().getGraphicsConfiguration();
		Image bg=gc.createCompatibleImage(screen.getWidth(), screen.getHeight());
		
		Graphics2D g=(Graphics2D)bg.getGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, bg.getWidth(null), bg.getHeight(null));

		return bg;
	}
		
    @Override
    public void draw(Graphics2D g) {
    	if(isRunning()){
            renderer.draw(g, map, screen.getWidth(), screen.getHeight());        
            
            drawTime(g);
            if(state==STATE_WIN)
            	drawWin(g);
            
            if(state==STATE_PAUSE){
            	g.drawImage(pauseAnim.getImage(), 0, 0, null);            	
            	if(pauseAnim.isFinish()){
            		//g.drawString(Integer.toString(pause_choose), 500, 500);
            		menuList.draw(g);

            		int x,y;
            		MenuEntry menuEntry=menuList.getMenuEntry(pause_choose);
            		x=menuEntry.getPoint().x-cursor.getWidth(null)-10;
            		y=menuEntry.getPoint().y;
            		g.drawImage(cursor, x, y, null);
            	}
            }
            else if(state==STATE_FRAME_POP_UP){
            	framePopUp.draw(g, 
            			(screen.getWidth()-framePopUp.getWidth())/2, 
            			(screen.getHeight()-framePopUp.getHeight())/2);
            }
    	}
    }
    
    private void drawTime(Graphics2D g){
        int timeInSec=(int)Math.ceil(time/1000);
        String string="Time: "+Integer.toString(timeInSec);
        g.setFont(timeFont);
		g.setColor(new Color(42,42,42));
        g.drawString(string, 600, 50);
    }

    public void run() {
        init();
        gameLoop();
    }

    public void init(){
        //super.init();
    	
    	// 60s =60000ms
    	time=PLAY_TIME;
    	
    	stateTime=0;
    	hasShowed=false;
    	
    	if(player instanceof Gatotkaca)
    		framePopUp=resourceManager.getGatotkacaFramePopUp();
    	else
    		framePopUp=resourceManager.getJabangTetukaFramePopUp();
    		
        // load sounds
    	initSound();

        durationElapsed=0;
        initInput();
        
		start();
    }
    
    private void initSound(){
        // load sounds
        stageIs=soundManager.play(stageSound,null,true);    		
    }
    
    private void initInput(){
        //Init Game Action
        moveRight=new GameAction("right");
        moveLeft=new GameAction("left");
        moveUp=new GameAction("up", GameAction.DETECT_INITAL_PRESS_ONLY);
        moveDown=new GameAction("down", GameAction.DETECT_INITAL_PRESS_ONLY);
        jump=new GameAction("jump", GameAction.DETECT_INITAL_PRESS_ONLY);
        attack=new GameAction("attack", GameAction.DETECT_INITAL_PRESS_ONLY);
        auraAttack=new GameAction("auraAttack", GameAction.DETECT_INITAL_PRESS_ONLY);
        biteAttack=new GameAction("biteAttack", GameAction.DETECT_INITAL_PRESS_ONLY);
        shield=new GameAction("shield", GameAction.DETECT_INITAL_PRESS_ONLY);
        pause=new GameAction("pause", GameAction.DETECT_INITAL_PRESS_ONLY);
        exit=new GameAction("exit", GameAction.DETECT_INITAL_PRESS_ONLY);
        enter=new GameAction("enter", GameAction.DETECT_INITAL_PRESS_ONLY);
        
        //cheat only
        lose=new GameAction("lose", GameAction.DETECT_INITAL_PRESS_ONLY);
        
        //Init Input
        inputManager=new InputManager(screen.getFrame());
        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(moveUp, KeyEvent.VK_UP);
        inputManager.mapToKey(moveDown, KeyEvent.VK_DOWN);
        inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.mapToKey(attack, KeyEvent.VK_A);
        inputManager.mapToKey(auraAttack, KeyEvent.VK_S);
        inputManager.mapToKey(biteAttack, KeyEvent.VK_D);
        inputManager.mapToKey(shield, KeyEvent.VK_W);
        inputManager.mapToKey(pause, KeyEvent.VK_P);
        inputManager.mapToKey(enter, KeyEvent.VK_ENTER);
        inputManager.mapToKey(lose, KeyEvent.VK_R);
    }
    
    public void update(long elapsedTime){
    	checkInput();
    	if(state==STATE_PLAY){
    		statePlay(elapsedTime);
    	}
    	else if(state==STATE_PAUSE){
    		statePause(elapsedTime);
    	}
    	else if(state==STATE_WIN){
    		stateWin(elapsedTime);
    	}
    	else if(state==STATE_FRAME_POP_UP){
    		stateFramePopUp(elapsedTime);
    	}
    }
    
    private void statePause(long elapsedTime){
    	//checkInput();
    	pauseAnim.update(elapsedTime);
    }
    
    private void stateFramePopUp(long elapsedTime){
    	//checkInput();
    	framePopUp.update(elapsedTime);
    	
    	if(framePopUp.getState()==FramePopUp.STATE_FADE_OUT && 
    			framePopUp.isFinish()){
    		state=STATE_PLAY;
    	}
    }
    
    private void stateWin(long elapsedTime){
    	//checkInput();
    	Creature player=(Creature) map.getPlayer();
    	updateCreature(player, elapsedTime);
    	player.update(elapsedTime);
    	
    	Iterator<Sprite> i=map.getSprites();
        while(i.hasNext()){
        	Sprite newSprite=i.next();
        	if(newSprite instanceof Creature){
            	Creature creature=(Creature)newSprite;
        		if(creature instanceof AuraShot){
            		AuraShot auraShot=(AuraShot)newSprite;
                	updateCreature(auraShot, elapsedTime);        		
            	}
        		
        		if(creature.getState()==Creature.STATE_DEAD){
        			i.remove();
        		}
        	}
        	newSprite.update(elapsedTime);
        }        	
    	
		durationElapsed +=elapsedTime;
        checkStatus();
    }
    
    private void statePlay(long elapsedTime){
    	time -=elapsedTime;
    	
    	//Rebirth again on x=this and y=0
    	if(time<=0){
    		time=PLAY_TIME;
        	JabangTetuka newSprite=(JabangTetuka)player;
        	newSprite.rebirth();
        	newSprite.setY(0);
        	newSprite.setX(newSprite.getX());
        	if(newSprite.getState()==JabangTetuka.STATE_DEAD)
        		state=STATE_LOSE;
    	}
    	
    	if(!hasShowed && showTutorial){
        	stateTime+=elapsedTime;
        	if(stateTime>TUTORIAL_SHOW){
        		state=STATE_FRAME_POP_UP;
        		hasShowed=true;
        		framePopUp.fadeIn();
        	}    		
    	}
    	
    	// get keyboard/mouse input
        //checkInput();

		Creature player=(Creature) map.getPlayer();

		// update player
        updateCreature(player, elapsedTime);
        player.update(elapsedTime);

        if(player instanceof JabangTetuka && 
        		player.getState()==Creature.STATE_DEAD)
        {
        	JabangTetuka newSprite=(JabangTetuka)player;
        	newSprite.rebirth();
        	newSprite.setY(0);
        	newSprite.setX(100);
        	if(newSprite.getState()==JabangTetuka.STATE_DEAD)
        		state=STATE_LOSE;
        	
        	if(bossStage)
	        	//Rebirth Nagapecona too
	        	bossSprite.setHp(1f);
        }

        // update other sprites
        Iterator<Sprite> i = map.getSprites();
        
        // temporary linkedlist
        LinkedList<Sprite> longRangeSprites=new LinkedList<Sprite>();
        
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            if (sprite instanceof Creature) {
                Creature creature = (Creature)sprite;

                if (creature.getState() == Creature.STATE_DEAD) {
                    i.remove();
                    this.player.setScore(this.player.getScore()+creature.getScore());
                }
                else {
                	Sprite longRangeSprite=checkLongRangeAttack(creature);
                	
                	if(longRangeSprite!=null)
                		longRangeSprites.add(longRangeSprite);

                	updateCreature(creature, elapsedTime);
                }
            }
            
            sprite.update(elapsedTime);
        }
        
        if(longRangeSprites.size()>0){
            i = longRangeSprites.iterator();
            while(i.hasNext()){
            	Sprite temp=(Sprite)i.next();
            	temp.update(elapsedTime);
            	map.addSprite(temp);
            }        	
        }
        
        //Load Next Map if finish
        // player is dead! start map over
        checkStatus();
    }
    
    private void checkStatus(){
    	if(state==STATE_PLAY){
    		if(bossStage){
    			if(bossSprite.getState()==Creature.STATE_DEAD){
    	        	JabangTetuka newSprite=(JabangTetuka)player;
    	        	newSprite.setScore(bossSprite.getScore());
    				state=STATE_WIN;
    			}
    		}
    		else{
        		Sprite player=map.getPlayer();
                int finishLine=TileMapRenderer.tilesToPixels(map.getWidth())-(screen.getWidth()/2);
                if(player.getX()>=finishLine){
                	state=STATE_WIN;
                }    			
    		}
    	}
    	else if(state==STATE_WIN){
    		if(durationElapsed>WINNING_DURATION){
    			stop();
    		}
    	}
    	else if(state==STATE_LOSE){
    		stop();
    	}
    }
    
    private Sprite checkLongRangeAttack(Creature creature){
    	if(creature instanceof ButoAbang){
    		ButoAbang newSprite=(ButoAbang)creature;
    		if(newSprite.getState()==ButoAbang.STATE_ATTACK &&
    				newSprite.canShot()){
        		return newSprite.auraShot();
    		}
    	}    	
    	return null;
    }
    
    public void stop() {
    	loading.run();
    	if(stageIs!=null)
	    	try {
				stageIs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		super.stop();
    }
    
    private void checkInput() {
        if (exit.isPressed()) {
        	state=STATE_WIN;
            stop();
        }
        else if(lose.isPressed()){
        	state=STATE_LOSE;
        	stop();
        }
        
        
        JabangTetuka player=(JabangTetuka) map.getPlayer();

        if(state==STATE_PLAY){
            //Player player = (Player)map.getPlayer();
            if (player.isAlive() && player.getState()==Creature.STATE_NORMAL) {
                float velocityX = 0;
                if (moveLeft.isPressed()) {
                    velocityX-=player.getMaxSpeed();
                }
                else if (moveRight.isPressed()) {
                    velocityX+=player.getMaxSpeed();
                }
                
                if (jump.isPressed()) {
                	player.jump(false);
                }
                if (attack.isPressed()){
                    //soundManager.play(prizeSound);
                	player.attack();
                }
                else if(auraAttack.isPressed()){
                	player.attackAura();
                }
                else if(biteAttack.isPressed()){
                	player.attackBite();
                }
                else if(shield.isPressed()){
                	player.shield();
                }
                else if(pause.isPressed()){
                	state=STATE_PAUSE;
                	pauseAnim.fadeIn();
                }
                player.setVelocityX(velocityX);
            }
        }
        else if(state==STATE_PAUSE)
        {
        	if(pause.isPressed()){
        		state=STATE_PLAY;
        		pauseAnim.fadeOut();
        	}
        	else if(moveUp.isPressed()){
        		pause_choose -=1;
        	}
        	else if(moveDown.isPressed()){
        		pause_choose +=1;
        	}
        	else if(enter.isPressed()){
        		switch(pause_choose){
	        		case 1:
	        			state=STATE_PLAY;
	        			pauseAnim.fadeOut();
	        			break;
	        			
	        		case 2:
	        			state=STATE_EXIT;
	        			stop();
	        			break;
        		}
        	}
        	
        	pause_choose=Math.min(pause_choose,2);
        	pause_choose=Math.max(1,pause_choose);
        }
        else if(state==STATE_WIN){
        	player.setVelocityX(0);
        }
        else if(state==STATE_FRAME_POP_UP){
        	if(enter.isPressed()){
        		framePopUp.fadeOut();
        	}
        }
    }
        
    private void updateCreature(Creature creature,
        long elapsedTime)
    {
        // apply gravity
        if (!creature.isFlying()) {
            creature.setVelocityY(creature.getVelocityY() + GRAVITY * elapsedTime);
        }
        
        // change x
        float dx = creature.getVelocityX();
        float oldX = creature.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile =
            getTileCollision(creature, newX, creature.getY());
        if (tile == null) {
            creature.setX(newX);
        }
        else if(!(creature instanceof Flame)){
            // line up with the tile boundary
            if (dx > 0) {
                creature.setX(
                    TileMapRenderer.tilesToPixels(tile.x) -
                    creature.getWidth());
            }
            else if (dx < 0) {
                creature.setX(
                    TileMapRenderer.tilesToPixels(tile.x + 1));
            }
            creature.collideHorizontal();
        }        

        // change y
        float dy = creature.getVelocityY();
        float oldY = creature.getY();
        float newY = oldY + dy * elapsedTime;
        tile = getTileCollision(creature, creature.getX(), newY);
        if (tile == null) {
            creature.setY(newY);
        }
        else if(!(creature instanceof Flame)){
            // line up with the tile boundary
            if (dy > 0) {
                creature.setY(
                    TileMapRenderer.tilesToPixels(tile.y) -
                    creature.getHeight());
            }
            else if (dy < 0) {
                creature.setY(
                    TileMapRenderer.tilesToPixels(tile.y + 1));
            }
            creature.collideVertical();
        }
        
        if(creature instanceof JabangTetuka &&
        		creature.getY()>=TileMapRenderer.tilesToPixels(map.getHeight())){
        	creature.setState(JabangTetuka.STATE_DYING);
        }
                
        if (creature instanceof JabangTetuka ||
        		creature instanceof AuraShot ||
        		creature instanceof BiteShot) {
            //boolean canKill = (oldY < creature.getY());
            if(creature instanceof BiteShot)
            	System.out.println();
        	checkCollisionFor(creature);
        }

    }
    
    private void checkCollisionFor(Creature creature)
    {
    	if (creature.isDied()) {
            return;
        }

        // check for player collision with other sprites
        Sprite collisionSprite = getSpriteCollision(creature);
        
        if(creature instanceof Gatotkaca && collisionSprite instanceof ClawShot){
        	System.out.println("Stage.java:298");
        }
        
        if(collisionSprite!=null)
        	creature.notifyObjectCollision(collisionSprite);
    }

    private Sprite getSpriteCollision(Sprite sprite) {

        // run through the list of Sprites
        Iterator<Sprite> i = map.getSprites();
        while (i.hasNext()) {
            Sprite otherSprite = (Sprite)i.next();
            if(sprite instanceof Flame && otherSprite instanceof JabangTetuka){
        		System.out.println("Flame");
            }
            
            if (isCollision(sprite, otherSprite)) {
                // collision found, return the Sprite
                return otherSprite;
            }
        }

        // no collision found
        return null;
    }
    
    private boolean isCollision(Sprite s1, Sprite s2) {
        // if the Sprites are the same, return false
        if (s1 == s2) {
            return false;
        }

        if(!(s1 instanceof JabangTetuka))
        {	        	
	        // if one of the Sprites is a dead Creature, return false
	        if (s1 instanceof Creature && !((Creature)s1).isAlive()) {
	            return false;
	        }
	        if (s2 instanceof Creature && !((Creature)s2).isAlive()) {
	            return false;
	        }
        }
        else
        {
	        if (!((JabangTetuka)s1).isAlive()) {
	            return false;
	        }
        }

        // get the pixel location of the Sprites
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());
        
        // check if the two sprites' boundaries intersect
        boolean isIntersect=false;
        if((s1x < s2x + s2.getWidth() &&
                s2x < s1x + s1.getWidth() &&
                s1y < s2y + s2.getHeight() &&
                s2y < s1y + s1.getHeight())){
        	
        	Rectangle2D.Float rect1=s1.getFitRectangle();
        	Rectangle2D.Float rect2=s2.getFitRectangle();
        	
        	isIntersect=rect1.intersects(rect2);
        	
//        	Ellipse2D.Float ellipse1=s1.getEllipse2D();
//        	Ellipse2D.Float ellipse2=s2.getEllipse2D();
//        	isIntersect=ellipse1.intersects(
//        			ellipse2.x,ellipse2.y,
//        			ellipse2.width, ellipse2.height);
        	
        	if(isIntersect){
        		//Check pixel to pixel
            	if(s1 instanceof Gatotkaca && s2 instanceof Nagapecona){
            		System.out.println("Stage.java:isCollision()");
            	}
        	}
        }
        return isIntersect;
    }
    
    private Point getTileCollision(Sprite sprite,
        float newX, float newY)
    {
    	//Get Sprite Position
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);
        
        // get the tile locations
        int fromTileX = TileMapRenderer.pixelsToTiles(fromX);
        int fromTileY = TileMapRenderer.pixelsToTiles(fromY);
        int toTileX = TileMapRenderer.pixelsToTiles(
            toX + sprite.getWidth() - 1);
        int toTileY = TileMapRenderer.pixelsToTiles(
            toY + sprite.getHeight() - 1);

        // check each tile for a collision
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= map.getWidth() ||
                    map.getTile(x, y) != null)
                {
                    // collision found, return the tile
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }

        // no collision found
        return null;
    }
    
    private void drawWin(Graphics2D g){
    	//g.drawString("Winning effect should appear", 500, 500);
    }
    
    /**
     * Mendapatkan keadaan(state) stage ini
     * @return konstanta <code>int</code> State
     */
    public int getState(){
    	return state;
    }
    
    /**
     * Mendapatkan <code>JabangTetuka</code>
     * @return <code>JabangTetuka</code> yang di minta
     */
    public JabangTetuka getPlayer(){
    	return player;
    }
            
    private InputManager inputManager;

    //Game Action
    private GameAction exit;
    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction moveUp;
    private GameAction moveDown;
    private GameAction jump;
    private GameAction attack;
    private GameAction auraAttack;
    private GameAction biteAttack;
    private GameAction shield;
    private GameAction pause;
    private GameAction enter;
    
    //cheat only
    private GameAction lose;

    private ResourceManager resourceManager;
    private TileMapRenderer renderer;
    private TileMap map;
    
    private SoundManager soundManager;

    // Transparent Background for pause state
    private PauseAnimation pauseAnim;
    private int pause_choose;
    private Image cursor;
    
    //Sprite
    private JabangTetuka player;
    private int state=0;
    
    private FramePopUp framePopUp;
        
    private MenuList menuList;
    
    private boolean bossStage;
    private Creature bossSprite;
    
    //Sound
    private Sound stageSound;
    private InputStream stageIs;
    
    private long durationElapsed;

    private static final int WINNING_DURATION=2000;
    
    private Point pointCache=new Point();
    
    private LoadingScreen loading;
    
    private long stateTime;
    private long time;
    private boolean hasShowed;
	private boolean showTutorial;
    
	private Font timeFont;
	
	private static final long TUTORIAL_SHOW=1000;
	private static final long PLAY_TIME=60000;

	private static final float GRAVITY = 0.002f;

	private static final int STATE_FRAME_POP_UP=5;
}



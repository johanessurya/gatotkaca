package tilegame.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;

import sound.Sound;
import sound.SoundManager;
import util.ResourceLoader;
import graphics.Animation;
import graphics.ImageMan;

public class Nagapecona extends ButoAbang {
	/**
	 * State LONG HAND ATTACK
	 */
    public static final int STATE_LONG_HAND_ATTACK=5;
	
    /**
     * Lama waktu invisible
     */
    protected static final int INVISIBLE_TIME=5000;
    
    /**
     * Berguna untuk pembuat effect blinking
     */
    protected GraphicsConfiguration gc;
    
    /**
     * Membuat object baru
     * @param flyLeft Animasi bergerak kekiri
     * @param flyRight Animasi bergerak kekanan
     * @param deadLeft Animasi mati kekiri
     * @param deadRight Animasi mati kekanan
     * @param hurtLeft Animasi kesakitan(hurt) kekiri
     * @param hurtRight Animasi kesakitan(hurt) kekanan
     * @param attackLeft Animasi menyerang menghadap kekiri
     * @param attackRight Animasi menyerang menghadap kekiri
     * @param longHandShot Animasi serangan
     * @param soundManager <code>SoundManager</code> untuk memainkan sound FX
     * @param hurtSound suara kesakitan 
     * @param dieSound suara mati
     */
    public Nagapecona(Animation flyLeft, Animation flyRight,
    		Animation deadLeft, Animation deadRight,
    		Animation hurtLeft, Animation hurtRight,
    		Animation attackLeft, Animation attackRight,
    		ClawShot longHandShot,
    		SoundManager soundManager, 
            Sound hurtSound, Sound dieSound)
    {
    	
    	super(flyLeft, flyRight,
    			deadLeft,deadRight,
    			attackLeft,attackRight,
    			hurtLeft,hurtRight, longHandShot,
    			soundManager,hurtSound,dieSound);
    	
    	this.attackLeft=attackLeft;
    	this.attackRight=attackRight;
    	this.longShot=longHandShot;

    	//start facing right
    	this.anim=this.left;
    
    	//Set HP
    	//Jabang Tetuka belum mempunyai skill apapun.
    	//Jadi tidak perlu mengset MPnya
    	setHp(MAX_HP);
        setAttackDamage(ATTACK_DAMAGE);
        setMaxSpeed(MOVE_SPEED);

        this.inviTimeElapsed=0;

        this.gc=null;
    }
    
    /**
     * Membuat object baru
     * @param flyLeft Animasi bergerak kekiri
     * @param flyRight Animasi bergerak kekanan
     * @param deadLeft Animasi mati kekiri
     * @param deadRight Animasi mati kekanan
     * @param hurtLeft Animasi kesakitan(hurt) kekiri
     * @param hurtRight Animasi kesakitan(hurt) kekanan
     * @param attackLeft Animasi menyerang menghadap kekiri
     * @param attackRight Animasi menyerang menghadap kekiri
     * @param longHandShot Animasi serangan
     * @param gc berguna untuk pembuatan efek blinking saat terkena serangan musuh
     * @param soundManager <code>SoundManager</code> untuk memainkan sound FX
     * @param hurtSound suara kesakitan 
     * @param dieSound suara mati
     */
    public Nagapecona(Animation flyLeft, Animation flyRight,
    		Animation deadLeft, Animation deadRight,
    		Animation hurtLeft, Animation hurtRight,
    		Animation attackLeft, Animation attackRight,
    		ClawShot longHandShot,
    		GraphicsConfiguration gc,
    		SoundManager soundManager, 
            Sound hurtSound, Sound dieSound)
    {
    	this(flyLeft,flyRight,
    			deadLeft,deadRight,
    			hurtLeft,hurtRight,
    			attackLeft, attackRight,
    			longHandShot,
    			soundManager,hurtSound,dieSound);
    	
    	this.gc=gc;
    }    
    
    public void draw(Graphics2D g, float x, float y){
    	super.draw(g, x, y);

    	Image status=getStatusBar();
    	int screenWidth, screenHeight;
    	screenWidth=1024;
    	screenHeight=768;
    	Point pos=new Point(screenWidth,screenHeight);
    	
    	pos.x=pos.x-status.getWidth(null);
    	pos.y=pos.y-status.getHeight(null);
    	
    	g.drawImage(status, pos.x, pos.y, null);
    	//g.drawString(Integer.toString((int)getX()), 300, 300);
    }
    
	/**
	 * Mengambil object status bar
	 * @return gambar object status bar yang siap untuk di gambar di layar
	 * @see tilegame.sprites.JabangTetuka#getStatusBar()
	 */
    public Image getStatusBar(){
		int width,height;
		width=512;
		height=64;
				
		Image statusBar=gc.createCompatibleImage(width,height,
				Transparency.TRANSLUCENT);
		
		Graphics2D g=(Graphics2D) statusBar.getGraphics();
		g.setRenderingHint(
	            RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		String dir="nagapecona/statusbar/";
		Image hpFrame,hpFill;
		hpFrame=loadImage(dir+"frame.png");
		hpFill=loadImage(dir+"fill.png");
		
		//HP
		g.drawImage(hpFill, 0, 0, null);

		//Blank Bar
		drawHpBlankBar(g);

		//HP Frame
		g.drawImage(hpFrame, 0, 0, null);
		
		return statusBar;
    }
    
	/**
	 * Menggambar HP tanpa terisi
	 * @param g object <code>Graphics2D</code>
	 */
    public void drawHpBlankBar(Graphics2D g){
		int max=468;
		float perc=getHp();
		float width=max*(1-perc);
		g.setColor(new Color(42,42,42));
		if(width<=max)
			g.fillRect(10, 12, (int)width, 41);
    }
    
    private Image loadImage(String filename){
    	return ResourceLoader.loadImage("images/"+filename);
    }

	public Image getImage(){
		Image newImage=null;
		if(isBlink())
			newImage=ImageMan.drawTransparent(anim.getImage(), 0.5f);
		else
			newImage=anim.getImage();
		
		return newImage;
	}
	
	private boolean isBlink(){
		boolean temp=false;
		
		if(blinkTimeElapsed>=BLINK_TIME)
		{
			blinkTimeElapsed %=BLINK_TIME;
			temp=true;
		}
		
		return temp;
	}
	
	
    private boolean isAttackTime(){
    	boolean attack=false;
    	if(attackTimeElapsed>ATTACK_TIME)
    	{
    		attackTimeElapsed %=ATTACK_TIME;
    		attack=true;
    	}
    	
    	return attack;
    }
	
    public void update(long elapsedTime){
    	if(isImmune){
			inviTimeElapsed+=elapsedTime;
			blinkTimeElapsed+=elapsedTime;
			
			if(inviTimeElapsed>=INVISIBLE_TIME){
				inviTimeElapsed=0;
	    		isImmune=false;
			}
	    }

    	if(!isAttackTime())
    		attackTimeElapsed+=elapsedTime;        	
    	    	
        // select the correct Animation
        Animation newAnim = anim;
        if(state==STATE_NORMAL)
        {
		    if (!isFacingRight()) {
		        newAnim = left;
		    }
		    else{
		        newAnim = right;
		    }
		    
		    if(isAttackTime())
	        	setState(STATE_ATTACK);

        }
        else if(state==STATE_DYING)
        {
	        if (!isFacingRight()) {
	            newAnim = deadLeft;
	        }
	        else{
	            newAnim = deadRight;
	        }
        }
        else if(state==STATE_HURT){
        	setVelocityX(0);
        	isImmune=true;
        	if(isFacingRight()){
	        	newAnim=hurtRight;
	        	if(hurtRight.isFinised()){
	        		hurtRight.start();
	        		newAnim=right;
	        		setVelocityX(getMaxSpeed());
	        		setState(STATE_NORMAL);
	        	}
        	}else
        	{
	        	newAnim=hurtLeft;
	        	if(hurtLeft.isFinised()){
	        		hurtLeft.start();
	        		newAnim=left;
	        		setVelocityX(-getMaxSpeed());
	        		setState(STATE_NORMAL);
	        	}        		
        	}
        }
    	else if(state==STATE_ATTACK){
    		setVelocityX(0);
		    if (!isFacingRight()) {
		        newAnim = attackLeft;
    		    if(newAnim.isFinised())
    		    {
	        		setCanShot(true);
    		    	attackLeft.start();
    		    	attackTimeElapsed=0;
    		    	newAnim=left;
	        		setVelocityX(-getMaxSpeed());
    		    	setState(STATE_NORMAL);
    		    }
		    }
		    else{
		        newAnim = attackRight;
    		    if(newAnim.isFinised())
    		    {
	        		setCanShot(true);
    		    	attackRight.start();
    		    	attackTimeElapsed=0;
    		    	newAnim=right;
	        		setVelocityX(getMaxSpeed());
    		    	setState(STATE_NORMAL);
    		    }
		    }
    		
    	}
    	
        // update the Animation
        if (anim != newAnim) {
            anim = newAnim;
            anim.start();
        }
        else {
            anim.update(elapsedTime);
        }            
        
        // update to "dead" state
        // stateTime += elapsedTime;
        if (state == STATE_DYING && anim.isFinised()) {
            setState(STATE_DEAD);
        }
    }
    
    public Nagapecona clone(){
    	Nagapecona newSprite= new Nagapecona(
    			(Animation) left.clone(), 
    			(Animation) right.clone(),
    			(Animation) deadLeft.clone(), 
    			(Animation) deadRight.clone(),
    			(Animation) hurtLeft.clone(), 
    			(Animation) hurtRight.clone(),
    			(Animation) attackLeft.clone(), 
    			(Animation) attackRight.clone(),
    			(ClawShot) longShot.clone(),
    			gc,
    			soundManager,hurtSound,dieSound);
    	
    	return newSprite;
    }
    
    public float getAttackDamage(){
    	return ATTACK_DAMAGE;
    }
    
    public ClawShot auraShot(){
		setCanShot(false);
    	ClawShot newAuraShot=longShot.clone();
    	//newAuraShot.setY(this.getY());
    	newAuraShot.setY(this.getY()+18);
    	
    	if(isFacingRight()){
        	//newAuraShot.setX(this.getX()+70);
        	newAuraShot.setX(this.getX()+84);
    		newAuraShot.shotRight();
    	}
    	else{
        	//newAuraShot.setX(this.getX()-89);
        	newAuraShot.setX(this.getX()-211);
    		newAuraShot.shotLeft();        		
    	}
    	
    	//map.addSprite(newAuraShot);
    	return newAuraShot;
    }
    
	public int getScore(){
		return 1000;
	}

    
	private ClawShot longShot;

    private Animation attackLeft;
    private Animation attackRight;
        
    private int attackTimeElapsed;
    private int inviTimeElapsed;
    private int blinkTimeElapsed;
    
    private static final int ATTACK_TIME=2000;
    private static final int BLINK_TIME=50;

    private static final float ATTACK_DAMAGE=0.1f;
    private static final float MOVE_SPEED=0.2f;
    private static final float MAX_HP=1f;
}

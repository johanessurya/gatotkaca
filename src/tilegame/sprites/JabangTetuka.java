package tilegame.sprites;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;

import sound.Sound;
import sound.SoundManager;
import tilemap.TileMap;
import util.ResourceLoader;

import graphics.Animation;
import graphics.ImageMan;
import graphics.Sprite;

public class JabangTetuka extends Nature{
	/**
	 * State ATTACK
	 */
    public static final int STATE_ATTACK=4;
	
    /**
     * Apakah JabangTetuka berada ditanah
     */
    protected boolean onGround;

	/**
	 * Animasi stand-by menghadap kekiri
	 */
    protected Animation idleLeft;

	/**
	 * Animasi stand-by menghadap kekanan
	 */
    protected Animation idleRight;

    /**
	 * Animasi melompat menghadap kekiri
	 */
    protected Animation jumpLeft;

    /**
	 * Animasi melompat menghadap kekanan
	 */
    protected Animation jumpRight;
    
    /**
	 * Animasi menyerang menghadap kekiri
	 */
    protected Animation attackLeft;
    
    /**
	 * Animasi menyerang menghadap kekanan
	 */
    protected Animation attackRight;
    
    /**
	 * Object AuraShot untuk mengeluarkan serangan aura
	 */
    protected AuraShot auraShot;
    
    /**
     * Untuk mendekteksi apakah waktunya kebal(invisible)
     */
    protected int inviTimeElapsed;
    
    /**
     * Untuk mendeteksi apakah waktunnya blinking
     */
    protected int blinkTimeElapsed;
            
    /**
     * Image circle untuk status bar
     */
	protected Image circle;
	
	/**
     * Image bubble untuk status bar
     */
	protected Image bubble;
	
	/**
     * Image hp bar untuk status bar
     */
	protected Image hpBar;
	
	/**
     * Image hp(fill background) untuk status bar
     */
	protected Image hp;

	/**
	 * Lama waktu kebal(invisible)
	 */
    protected static final int INVISIBLE_TIME=1000;
    
    /**
     * Lama waktu tiap blink
     */
	protected static final int BLINK_TIME=50;

	/**
	 * Berguna untuk pembuatan efek
	 */
    protected GraphicsConfiguration gc;
    
    /**
     * Membuat object baru
     * @param runLeft Animasi bergerak kekiri
     * @param runRight Animasi bergerak kekanan
     * @param deadLeft Animasi mati kekiri
     * @param deadRight Animasi mati kekanan
     * @param jumpLeft Animasi lompat menghadap kekiri
     * @param jumpRight Animasi lompat menghadap kekanan
     * @param idleLeft Animasi stand-by menghadap kekiri
     * @param idleRight Animasi stand-by menghadap kekiri
     * @param attackLeft Animasi menyerang menghadap kekiri
     * @param attackRight Animasi menyerang menghadap kekiri
     * @param hurtLeft Animasi kesakitan(hurt) kekiri
     * @param hurtRight Animasi kesakitan(hurt) kekanan
     * @param auraShot Animasi serangan
     * @param gc berguna untuk pembuatan efek blinking saat terkena serangan musuh
     * @param soundManager <code>SoundManager</code> untuk memainkan sound FX
     * @param hurtSound suara kesakitan 
     * @param dieSound suara mati
     */
    public JabangTetuka(Animation runLeft, Animation runRight,
    		Animation deadLeft, Animation deadRight, 
    		Animation jumpLeft, Animation jumpRight,
    		Animation idleLeft, Animation idleRight,
    		Animation attackLeft, Animation attackRight,
    		Animation hurtLeft, Animation hurtRight,
    		AuraShot auraShot, GraphicsConfiguration gc,
    		SoundManager soundManager, Sound hurtSound, Sound dieSound)
    {
    	super(runLeft,runRight,deadLeft,deadRight,hurtLeft,hurtRight,
    			soundManager, hurtSound, dieSound);
    	this.idleLeft=idleLeft;
    	this.idleRight=idleRight; 
    	this.jumpLeft=jumpLeft;
    	this.jumpRight=jumpRight;
    	this.attackLeft=attackLeft;
    	this.attackRight=attackRight;
    	this.auraShot=auraShot;
        	
    	this.map=null;
    	this.canShot=true;

    	
    	//start facing right
    	this.anim=idleRight;
    	this.inviTimeElapsed=0;
    	this.score=0;
    	this.live=2;
    
    	//For Live
    	this.font=ResourceLoader.loadFont("comicbd.ttf");
    	this.font=font.deriveFont(Font.BOLD);
    	
    	//For Score
    	this.scoreFont=ResourceLoader.loadFont("avgardd.ttf");
    	this.scoreFont=scoreFont.deriveFont(Font.BOLD, 14f);
    
    	//Set HP
    	//Jabang Tetuka belum mempunyai skill apapun.
    	//Jadi tidak perlu mengset MPnya
    	setHp(MAX_HP);
        setAttackDamage(ATTACK_DAMAGE);
        setMaxSpeed(MOVE_SPEED);
    	setJumpSpeed(JUMP_SPEED);

		circle=loadImage("circle.png");
		bubble=loadImage("bubble.png");
		hpBar=loadImage("hpBar.png");
		hp=loadImage("hp.png");

    	this.gc=gc;
    }
    
    /**
     * Membuat object baru
     * @param runLeft Animasi bergerak kekiri
     * @param runRight Animasi bergerak kekanan
     * @param deadLeft Animasi mati kekiri
     * @param deadRight Animasi mati kekanan
     * @param jumpLeft Animasi lompat menghadap kekiri
     * @param jumpRight Animasi lompat menghadap kekanan
     * @param idleLeft Animasi stand-by menghadap kekiri
     * @param idleRight Animasi stand-by menghadap kekiri
     * @param attackLeft Animasi menyerang menghadap kekiri
     * @param attackRight Animasi menyerang menghadap kekiri
     * @param hurtLeft Animasi kesakitan(hurt) kekiri
     * @param hurtRight Animasi kesakitan(hurt) kekanan
     * @param auraShot Animasi serangan
     * @param gc berguna untuk pembuatan efek blinking saat terkena serangan musuh
     * @param map berguna dalam pemposisian seperti pemanggilan method
     * <code>rebirth()</code>
     * @param soundManager <code>SoundManager</code> untuk memainkan sound FX
     * @param hurtSound suara kesakitan 
     * @param dieSound suara mati
     */
    public JabangTetuka(Animation runLeft, Animation runRight,
    		Animation deadLeft, Animation deadRight, 
    		Animation jumpLeft, Animation jumpRight,
    		Animation idleLeft, Animation idleRight,
    		Animation attackLeft, Animation attackRight,
    		Animation hurtLeft, Animation hurtRight,
    		AuraShot auraShot, GraphicsConfiguration gc,
    		TileMap map, SoundManager soundManager, 
            Sound hurtSound, Sound dieSound)
    {
    	this(runLeft, runRight,
    		deadLeft, deadRight, 
    		jumpLeft, jumpRight,
    		idleLeft, idleRight,
    		attackLeft, attackRight,
    		hurtLeft, hurtRight,
    		auraShot, gc, soundManager, hurtSound, dieSound);
    	this.map=map;
    }
    
    /**
     * Method ini sebaiknya di panggil setelah JabangTetuka mati (HP<=0) dan akan
     * hidup kembali dengan mengurangi nyawa
     */
    public void rebirth(){
    	setHp(MAX_HP);
    	this.live -=1;
    	this.isImmune=true;
    	setState(STATE_NORMAL);
    	if(live<0)
    		setState(STATE_DEAD);
    }

    public void collideHorizontal() {
        setVelocityX(0);
    }


    public void collideVertical() {
        // check if collided with ground
        if (getVelocityY() > 0) {
            onGround = true;
        }
        setVelocityY(0);
    }


    public void setY(float y) {
        // check if falling
        if (Math.round(y) > Math.round(getY())) {
            onGround = false;
        }
        super.setY(y);
    }


    public void wakeUp() {
        // do nothing
    }

    /**
     * Membuat melompat
     * @param forceJump memaksa untuk melompat jika di set <code>true</code>
     */
    public void jump(boolean forceJump) {
        if (onGround || forceJump) {
            onGround = false;
            setVelocityY(jumpSpeed);
        }
    }
    
    public void update(long elapsedTime){
    	if(isImmune){
    		inviTimeElapsed+=elapsedTime;
    		blinkTimeElapsed+=elapsedTime;
    		
    		if(inviTimeElapsed>INVISIBLE_TIME){
    			inviTimeElapsed=0;
        		isImmune=false;
    		}
    	}
    	
    	//testing only
    	//isImmune=true;
    	
        // select the correct Animation
        Animation newAnim = anim;
        if(state==STATE_ATTACK)
        {
        	setVelocityX(0);
        	if(isFacingRight()){
	        	newAnim=attackRight;
	        	if(canShot && newAnim.getCurrentFrame()==1)auraShot();
	        	if(attackRight.isFinised()){
	        		canShot=true;
	        		attackRight.start();
	        		newAnim=idleRight;
	        		setState(STATE_NORMAL);
	        	}        		
        	}else
        	{
	        	newAnim=attackLeft;
	        	if(canShot && newAnim.getCurrentFrame()==1)auraShot();
	        	if(attackLeft.isFinised()){
	        		canShot=true;
	        		attackLeft.start();
	        		newAnim=idleLeft;
	        		setState(STATE_NORMAL);
	        	}        		
        	}
        }
        else if(state==STATE_HURT){
        	setVelocityX(0);
        	if(isFacingRight()){
	        	newAnim=hurtRight;
	        	if(hurtRight.isFinised()){
	        		hurtRight.start();
	        		newAnim=idleRight;
	        		setState(STATE_NORMAL);
	        	}        		
        	}else
        	{
	        	newAnim=hurtLeft;
	        	if(hurtLeft.isFinised()){
	        		hurtLeft.start();
	        		newAnim=idleLeft;
	        		setState(STATE_NORMAL);
	        	}        		
        	}
        }
        else if(!onGround){
            	if(isFacingRight())
            		newAnim=jumpRight;
            	else newAnim=jumpLeft;
        }
        else
        {
            if(getVelocityX()==0){
            	if(isFacingRight())newAnim=idleRight;
            	else newAnim=idleLeft;
            } 
            if (getVelocityX() < 0) {
                newAnim = left;
            }
            else if (getVelocityX() > 0) {
                newAnim = right;
            }        	
        }        
        
        if (state == STATE_DYING) {
        	if(!isFacingRight())
        		newAnim = deadLeft;
        	else newAnim=deadRight;
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
        if (state == STATE_DYING && anim.isFinised()) {
            setState(STATE_DEAD);
        }
    }
        
    /**
     * Apakah animasi yang sedang aktif sudah selesai(reach end of frame)
     * @return <code>true</code> jika selesai dan <code>false</code>
     * jika sebaliknya
     */
    public boolean isFinised(){
    	return anim.isFinised();
    }
    
    public JabangTetuka clone(){
    	JabangTetuka newSprite=new JabangTetuka(
    			(Animation)left.clone(),
    			(Animation)right.clone(),
    			(Animation)deadLeft.clone(),
    			(Animation)deadRight.clone(),
    			(Animation)jumpLeft.clone(),
    			(Animation)jumpRight.clone(),
    			(Animation)idleLeft.clone(),
    			(Animation)idleRight.clone(),
    			(Animation)attackLeft.clone(),
    			(Animation)attackRight.clone(),
    			(Animation)hurtLeft.clone(),
    			(Animation)hurtRight.clone(),
    			(AuraShot)auraShot.clone(),gc,
    			map,
    			soundManager, hurtSound, dieSound);
    	
    	return newSprite;
    }
    
    /**
     * Suara memukul
     * @return object suara
     */
    protected Sound getPunchSound(){
    	return null;
    }
    
    /**
     * Membuat object menyerang
     */
    public void attack(){
    	if(onGround){
    		soundManager.play(getPunchSound());
    		setState(STATE_ATTACK);
    	}
    }
    
    /**
     * Membuat object mengeluarkan serangan aura. Dalam kasus ini
     * JabangTetuka tidak memiliki kemampuan untuk mengeluarkan serangan aura 
     * sehingga method ini tidak melakukan apa-apa.
     */
    public void attackAura(){
    	//Do Nothing
    }
    
    /**
     * Membuat object mengeluarkan serangan gigit. Dalam kasus ini
     * JabangTetuka tidak memiliki kemampuan untuk mengeluarkan serangan gigit
     * sehingga method ini tidak melakukan apa-apa.
     */
    public void attackBite(){
    	//Do Nothing
    }
    
    /**
     * Membuat object mengeluarkan pelindung. Dalam kasus ini
     * JabangTetuka tidak memiliki kemampuan untuk mengeluarkan pelindung
     * sehingga method ini tidak melakukan apa-apa.
     */
    public void shield(){
    	//Do Nothing
    }
            
    /**
     * Mengcheck apakah posisi(state)nya bisa untuk menyerang
     * @param badguy musuh
     * @return <code>true</code> jika valid dan <code>false</code>
     * jika sebaliknya
     */
    public boolean isValidKill(Nature badguy){
    	if(badguy.isImmune())
    		return false;
    	
    	if(isFacingRight()){
        	return getX()<=badguy.getX();    		
    	}
    	else
    	{
        	return getX()>badguy.getX();
    	}    	
    }
    		
    /**
     * Mengset <code>GraphicsConfiguration<code> yang berguna untuk pembuatan
     * efek blink
     * @param gc object <code>GraphicsConfiguration<code>
     */
	public void setGraphicsConfiguration(GraphicsConfiguration gc){
		this.gc=gc;
	}
	
	/**
	 * Mengambil object status bar
	 * @return gambar object status bar yang siap untuk di gambar di layar
	 */
	public Image getStatusBar(){
		int width,height;
		width=192;
		height=64;
				
		Image statusBar=gc.createCompatibleImage(width,height,
				Transparency.TRANSLUCENT);
		
		Graphics2D g=(Graphics2D) statusBar.getGraphics();
		g.setRenderingHint(
	            RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawImage(hp, 0, 0, null);
		drawHpBlankBar(g);
		g.drawImage(hpBar, 0, 0, null);
		g.drawImage(circle, 0, 0, null);
		drawLive(g);
		g.drawImage(bubble, 0, 0, null);		
		drawScore(g,5, 45);
		
		return statusBar;
	}
	
	/**
	 * Menggambar Score
	 * @param g object <code>Graphics2D</code>
	 * @param x posisi X
	 * @param y posisi Y
	 */
	protected void drawScore(Graphics2D g,int x, int y){
		String score=Integer.toString(this.score);
		g.setFont(scoreFont);
		g.setColor(new Color(42,42,42));
		g.drawString("Score: "+score, x,y);
	}
	
	/**
	 * Menggambar nyawa
	 * @param g object <code>Graphics2D</code>
	 */
	protected void drawLive(Graphics2D g){
		String live=Integer.toString(this.live);
		
		Point p;
		if(live.length()<=1){			
			//Posisi untuk 1 digit
			//p=new Point(171,29);
			p=new Point(169,31);
			font=font.deriveFont(20f);
		}
		else{
			//Posisi untuk 2 digit
			p=new Point(166,29);			
			font=font.deriveFont(15f);
		}
			
		
		g.setColor(new Color(204,153,0));
		g.setFont(font);
		g.drawString(live, p.x, p.y);
	}
	
	/**
	 * Menggambar HP tanpa terisi
	 * @param g object <code>Graphics2D</code>
	 */
	protected void drawHpBlankBar(Graphics2D g){
		int max=190;
		float x=max-(max*(1-getHp()));
		g.setColor(new Color(42,42,42));
		if(x!=max)
			g.fillRect((int)x, 2, (int)(max-x), 27);
	}
		
	/**
	 * Untuk mengload image
	 * @param filename filename
	 * @return object Image
	 */
	protected Image loadImage(String filename){
		return ResourceLoader.loadImage("images/statusbar/"+filename);
	}
	
	public void setHp(float hp){
		super.setHp(Math.min(1, hp));
	}
		
	public Image getImage(){
		Image newImage=null;
		if(isBlink())
			newImage=ImageMan.drawTransparent(anim.getImage(), 0.5f);
		else
			newImage=anim.getImage();
		
		return newImage;
	}
	
	/**
	 * Mengambil banyaknya nyawa
	 * @return nyawa
	 */
	public int getLive(){
		return this.live;
	}
	
	/**
	 * Mengset nyawa 
	 * @param live
	 */
	public void setLive(int live){
		this.live=live;
	}
	
	/**
	 * Mengset score
	 * @param score
	 */
	public void setScore(int score){
		this.score=score;
	}
	
	public int getScore(){
		return score;
	}
	
	private boolean isBlink(){
		boolean temp=false;
		if(blinkTimeElapsed>BLINK_TIME)
		{
			blinkTimeElapsed %=BLINK_TIME;
			temp=true;
		}
		
		return temp;
	}
		
	/**
	 * check apakah object ini ada ditanah atau di udara
	 * @return <code>true</code> jika ada ditanah dan <code>false</code>
     * jika sebaliknya
	 */
	public boolean isOnGround(){
		return onGround;
	}
	
	public void notifyObjectCollision(Sprite collisionSprite)
	{		
		if(getState()==JabangTetuka.STATE_HURT){
			return;
		}
		
		if(collisionSprite instanceof PowerUp){
			PowerUp powerUp=(PowerUp) collisionSprite;
        	map.removeSprite(powerUp);
        	powerUp.acquire();
	        if (powerUp instanceof PowerUp.Live) {
	            // do something here, like give the player points
	            //soundManager.play(prizeSound);
	        	int effect=(int)powerUp.getEffect();
	        	this.setLive(getLive()+effect);
	        }
	        else if (powerUp instanceof PowerUp.Kendi) {
	            // change the music
	            //soundManager.play(prizeSound);
	            //toggleDrumPlayback();
	        	float effect=powerUp.getEffect();
	        	this.setHp(getHp()+effect);
	        }
	        else if (powerUp instanceof PowerUp.Star) {
	            // change the music
	            //soundManager.play(prizeSound);
	            //toggleDrumPlayback();
	        	float effect=powerUp.getEffect();
	        	this.setMp(getMp()+effect);
	        }
	        
        	this.setScore(getScore()+powerUp.getScore());
		}
		else if (collisionSprite instanceof Flame){
        	Flame newSprite=(Flame)collisionSprite;
        	newSprite.notifyObjectCollision(this);
		}
		else if (collisionSprite instanceof Nature) {
            Nature badguy=(Nature)collisionSprite;
            
            if (isAttackState() &&
            		!badguy.isImmune() &&
            		isValidKill(badguy)) {
                // kill the badguy and make player bounce
                //soundManager.play(boopSound);
            	badguy.setHp(badguy.getHp()-getAttackDamage());
            	badguy.damaged();
            	
            	if(badguy.isDied())
            		setScore(getScore()+badguy.getScore());
            	
//                player.setY(badguy.getY() - player.getHeight());
//                player.jump(true);
            	
            }
            else 
            	if(badguy.getState()!=Nature.STATE_HURT && 
            		!isAttackState() && !isImmune()) {
            	// Player get damaged
            	badguy.notifyObjectCollision(this);
            }            
        }
	}
	
	/**
	 * Set kecepatan lompat
	 * @param jumpSpeed kecepatan lompat
	 */
	public void setJumpSpeed(float jumpSpeed){
		this.jumpSpeed=jumpSpeed;
	}
	
	/**
	 * Menambahkan map
	 * @param map map
	 */
    public void addMap(TileMap map){
    	this.map=map;
    }
    
    /**
     * Mengcheck apakah object ini siap untuk menyerang lagi. NOTE: Karena 
     * mempunyai delay antara serangannya. Apakah dapat menyerang kembali.
     * @return <code>true</code> jika dapat menyerang dan <code>false</code>
     * jika sebaliknya
     */
    public boolean canShot(){
    	return canShot;
    }
    
    /**
     * Untuk "memaksa" object ini dapat atau tidak dapat menyerang
     * @param canShot <code>true</code> jika dapat menyerang dan 
     * <code>false</code>jika sebaliknya
     */
    public void setCanShot(boolean canShot){
    	this.canShot=canShot;
    }
    
    /**
     * Untuk mengeluarkan aura shot
     */
    protected void auraShot(){
    	canShot=false;
    	AuraShot newAuraShot=auraShot.clone();
    	newAuraShot.setX(this.getX());
    	newAuraShot.setY(this.getY()+32);
    	
    	if(isFacingRight())
    		newAuraShot.shotRight();
    	else
    		newAuraShot.shotLeft();
    	
    	map.addSprite(newAuraShot);
    }
    
    /**
     * Mengambil map
     * @return map yang diambil
     */
    protected TileMap getTileMap(){
    	return map;
    }
    
    /**
     * Menggambar object ini ke layar
     */
    public void draw(Graphics2D g, float x, float y){
    	super.draw(g, x, y);
        g.drawImage(getStatusBar(), 5, 10, null);
    }
    
    /**
     * Mengcheck apakah sedang dalam keadaan menyerang
     * @return <code>true</code> jika sedang menyerang dan 
     * <code>false</code>jika sebaliknya
     */
	public boolean isAttackState(){
		return getState()==JabangTetuka.STATE_ATTACK;
	}
	
	/**
	 * Mengubah JabangTetuka menjadi Gatotkaca
	 * @param gatotkaca object gatotkaca
	 */
	public void growUp(Gatotkaca gatotkaca){
		gatotkaca.setHp(getHp());
		gatotkaca.setMp(getHp());
		gatotkaca.setScore(score);
		gatotkaca.setLive(live);
	}
    
    private int score;
    private int live;
    private Font font;
    private Font scoreFont;

    //Attack demage
    private static final float JUMP_SPEED=-.6f;
    private static final float ATTACK_DAMAGE=0.1f;
    private static final float MOVE_SPEED=0.2f;
    private static final float MAX_HP=1f;
    
    private float jumpSpeed;
    
    private TileMap map;
    private boolean canShot;
}

package tilegame.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;

import graphics.Animation;
import graphics.Sprite;
import sound.Sound;
import sound.SoundManager;
import tilemap.TileMap;

public class Gatotkaca extends JabangTetuka{
	/**
	 * State AURA ATTACK
	 */
    public static final int STATE_AURA_ATTACK=5;
    
    /**
     * State BITE ATTACK
     */
    public static final int STATE_BITE_ATTACK=6;
	
    /**
     * MP Frame Image
     */
	protected Image mpFrame;
	
	/**
	 * MP Bar Image
	 */
	protected Image mpBar;

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
	 * @param auraPunchLeft Animasi api aura kekiri
	 * @param auraPunchRight Animasi api aura kekanan
	 * @param biteLeft Animasi gigitan kekiri
	 * @param biteRight Animasi gigitan kekanan
	 * @param auraSmallShot Animasi api aura kekiri pada serangan normal
	 * @param auraBigShot Animasi api aura kekanan pada serangan normal
	 * @param biteShot Animasi kepala 
	 * @param shield Animasi api(shield)
     * @param gc berguna untuk pembuatan efek blinking saat terkena serangan musuh
     * @param soundManager <code>SoundManager</code> untuk memainkan sound FX
	 * @param punchSound suara pukulan
     * @param hurtSound suara kesakitan 
     * @param dieSound suara mati
	 * @param auraPunchSound suara jurus 1
	 * @param biteSound suara jurus 2
	 * @param shieldSound suara jurus 3
	 */
    public Gatotkaca(Animation runLeft, Animation runRight,
    		Animation deadLeft, Animation deadRight, 
    		Animation jumpLeft, Animation jumpRight,
    		Animation idleLeft, Animation idleRight,
    		Animation attackLeft, Animation attackRight,
    		Animation hurtLeft, Animation hurtRight,
    		Animation auraPunchLeft, Animation auraPunchRight,
    		Animation biteLeft, Animation biteRight,
    		AuraShot auraSmallShot,
    		GKAuraShot auraBigShot, BiteShot biteShot, Sprite shield,
    		GraphicsConfiguration gc,
    		SoundManager soundManager, 
            Sound punchSound, Sound hurtSound, Sound dieSound,
            Sound auraPunchSound, Sound biteSound, Sound shieldSound)
    {
    	super(runLeft,runRight,
    			deadLeft,deadRight,
        		jumpLeft, jumpRight,
        		idleLeft, idleRight,
        		attackLeft, attackRight,
        		hurtLeft,hurtRight,auraSmallShot,gc,
        		soundManager, hurtSound, dieSound);
    	
    	this.biteLeft=biteLeft;
    	this.biteRight=biteRight;    	
		this.auraPunchLeft=auraPunchLeft;
		this.auraPunchRight=auraPunchRight;
		this.auraBigShot=auraBigShot;
		this.biteShot=biteShot;
		this.shield=shield;
		this.isShieldOn=false;
		
		//Sound
		this.auraPunchSound=auraPunchSound;
		this.biteSound=biteSound;
		this.shieldSound=shieldSound;
		
    	setJumpSpeed(JUMP_SPEED);
    	setHp(MAX_HP);
        setAttackDamage(ATTACK_DAMAGE);
        setMaxSpeed(MOVE_SPEED);
        setMp(1f);
        
		mpFrame=loadImage("mpBar.png");
		mpBar=loadImage("mp.png");

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
	 * @param auraPunchLeft Animasi api aura kekiri
	 * @param auraPunchRight Animasi api aura kekanan
	 * @param biteLeft Animasi gigitan kekiri
	 * @param biteRight Animasi gigitan kekanan
	 * @param auraSmallShot Animasi api aura kekiri pada serangan normal
	 * @param auraBigShot Animasi api aura kekanan pada serangan normal
	 * @param biteShot Animasi kepala 
	 * @param shield Animasi api(shield)
     * @param gc berguna untuk pembuatan efek blinking saat terkena serangan musuh
     * @param map map
     * @param soundManager <code>SoundManager</code> untuk memainkan sound FX
	 * @param punchSound suara pukulan
     * @param hurtSound suara kesakitan 
     * @param dieSound suara mati
	 * @param auraPunchSound suara jurus 1
	 * @param biteSound suara jurus 2
	 * @param shieldSound suara jurus 3
     */
    public Gatotkaca(Animation runLeft, Animation runRight,
    		Animation deadLeft, Animation deadRight, 
    		Animation jumpLeft, Animation jumpRight,
    		Animation idleLeft, Animation idleRight,
    		Animation attackLeft, Animation attackRight,
    		Animation hurtLeft, Animation hurtRight,
    		Animation auraPunchLeft, Animation auraPunchRight,
    		Animation biteLeft, Animation biteRight,
    		AuraShot auraSmallShot,
    		GKAuraShot auraBigShot, BiteShot biteShot, Sprite shield,
    		GraphicsConfiguration gc, TileMap map,
    		SoundManager soundManager, 
            Sound punchSound, Sound hurtSound, Sound dieSound, 
            Sound auraPunchSound, Sound biteSound, Sound shieldSound)
    {
    	this(runLeft,runRight,
    			deadLeft,deadRight,
        		jumpLeft, jumpRight,
        		idleLeft, idleRight,
        		attackLeft, attackRight,
        		hurtLeft,hurtRight,
        		auraPunchLeft, auraPunchRight,
        		biteLeft, biteRight,
        		auraSmallShot,
        		auraBigShot, biteShot, shield,
        		gc,
        		soundManager, punchSound ,hurtSound, dieSound,
        		auraPunchSound, biteSound, shieldSound);
    	addMap(map);
    }
    
    public void draw(Graphics2D g, float x, float y){
    	super.draw(g, x, y);
    	
    	if(isShieldOn){
	    	shield.setX(getX()-60);
	    	shield.setY(getY()-42);
	    	g.drawImage(shield.getImage(), (int)(x+shield.getX()), 
	    			(int)(y+shield.getY()), null);
    	}

//        Shape fullFrame=new Rectangle2D.Float(0, 0,
//        		anim.getImage().getWidth(null),
//        		anim.getImage().getHeight(null));

//        g.draw(shield.getEllipse2D());
//        g.draw(getFitRectangle());
    }
    
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
		g.drawImage(mpBar, 0, 0, null);
		drawMpBlankBar(g);
		g.drawImage(mpFrame, 0, 0, null);
		g.drawImage(hp, 0, 0, null);
		drawHpBlankBar(g);
		g.drawImage(hpBar, 0, 0, null);
		g.drawImage(circle, 0, 0, null);
		drawLive(g);
		g.drawImage(bubble, 0, 0, null);
		drawScore(g, 5, 63);
		
		return statusBar;
	}

	public boolean isAttackState(){
		if(state==STATE_ATTACK ||
				state==STATE_AURA_ATTACK ||
				isShieldOn){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Sama halnya dengan <code>drawHpBlankBar</code>
	 * @param g graphics2D
	 * @see tilegame.sprites.JabangTetuka#drawHpBlankBar(Graphics2D)
	 */
	private void drawMpBlankBar(Graphics2D g){
		int max=180;
		float x=max-(max*(1-getMp()));
		g.setColor(new Color(42,42,42));
		if(x!=max)
			g.fillRect((int)x, 27, (int)(max-x), 20);

	}
	
    public void attackAura(){
    	float tempMp=getMp()-getAuraAttackMp();
    	if(tempMp>=0){
    		soundManager.play(auraPunchSound);
    		setState(STATE_AURA_ATTACK);
        	setMp(getMp()-getAuraAttackMp());    		
    	}
    }
    
    public void attackBite(){
    	float tempMp=getMp()-getBiteAttackMp();
    	if(tempMp>=0){
    		setState(STATE_BITE_ATTACK);
        	setMp(getMp()-getBiteAttackMp());    		
    	}
    }
    
    public void shield(){
    	isShieldOn=!isShieldOn;
		
    	if(isShieldOn)
			if(getMp()>0)
				soundManager.play(shieldSound);
    }
    
	public boolean isImmune(){
		if(isShieldOn())
		{
			return true;
		}
		return isImmune;
	}

    /**
     * Check apakah shild masih dalam keadaan ON
     * @return <code>true</code> jika shield masih dalam keadaan ON dan 
     * <code>false</code>jika sebaliknya
     */
    public boolean isShieldOn(){
    	if(getMp()>0)
    	{
    		return isShieldOn;
    		
    	}
    	else{
    		isShieldOn=false;    	
    		return isShieldOn;
    	}
    }

    public void damaged(){
    	super.damaged();
    }
    
    private float getAuraAttackMp(){
    	return AURA_ATTACK_MP;
    }
    
    private float getBiteAttackMp(){
    	return BITE_ATTACK_MP;
    }
    
    public void update(long elapsedTime){
    	if(isShieldOn())
    		setMp(getMp()-SHIELD_MP);
    	
    	shield.update(elapsedTime);
    	if(isImmune){
    		inviTimeElapsed+=elapsedTime;
    		blinkTimeElapsed+=elapsedTime;
    		
    		if(inviTimeElapsed>INVISIBLE_TIME){
    			inviTimeElapsed=0;
        		isImmune=false;
    		}
    	}
    	
        // select the correct Animation
        Animation newAnim = anim;
        if(state==STATE_ATTACK)
        {
        	setVelocityX(0);
        	if(isFacingRight()){
	        	newAnim=attackRight;
	        	if(canShot() && newAnim.getCurrentFrame()==1)auraShot();
	        	if(attackRight.isFinised()){
	        		setCanShot(true);
	        		attackRight.start();
	        		newAnim=idleRight;
	        		setState(STATE_NORMAL);
	        	}        		
        	}else
        	{
	        	newAnim=attackLeft;
	        	if(canShot() && newAnim.getCurrentFrame()==1)auraShot();
	        	if(attackLeft.isFinised()){
	        		setCanShot(true);
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
        else if(state==STATE_AURA_ATTACK){
        	float speed=(float)(getMaxSpeed()*2f);
        	if(isFacingRight()){
            	setVelocityX(speed);
	        	newAnim=auraPunchRight;
	        	if(canShot()&& newAnim.getCurrentFrame()==1)auraBigShot();
	        	if(auraPunchRight.isFinised()){
	        		setCanShot(true);
	        		auraPunchRight.start();
	        		newAnim=idleRight;
	        		setState(STATE_NORMAL);
	        	}        		
        	}else
        	{
            	setVelocityX(-speed);
	        	newAnim=auraPunchLeft;
	        	if(canShot() && newAnim.getCurrentFrame()==1)auraBigShot();
	        	if(auraPunchLeft.isFinised()){
	        		setCanShot(true);
	        		auraPunchLeft.start();
	        		newAnim=idleRight;
	        		setState(STATE_NORMAL);
	        	}        		
        	}
        }
        else if(state==STATE_BITE_ATTACK){
        	float speed=0;
        	setVelocityX(speed);
        	if(isFacingRight()){
	        	newAnim=biteRight;
	        	if(canShot() && biteRight.getCurrentFrame()==6)biteShot();
	        	if(biteRight.isFinised()){
	        		setCanShot(true);
	        		biteRight.start();
	        		newAnim=idleRight;
	        		setState(STATE_NORMAL);
	        	}        		
        	}else
        	{
	        	newAnim=biteLeft;
	        	if(canShot() && biteLeft.getCurrentFrame()==6)biteShot();
	        	if(biteLeft.isFinised()){
	        		setCanShot(true);
	        		biteLeft.start();
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

    public Rectangle2D.Float getFitRectangle(){
    	if(!isShieldOn){
    		return super.getFitRectangle();
    	}
    	else{
	    	Rectangle2D.Float rect=
	    		new Rectangle2D.Float(shield.getX(), shield.getY(),
	    			shield.getWidth(),shield.getHeight());
	    	return rect;
    	}
    }

    protected void auraShot(){
    	setCanShot(false);
    	AuraShot newAuraShot=auraShot.clone();
    	newAuraShot.setY(this.getY()+55);
    	
    	if(isFacingRight()){
        	newAuraShot.setX(this.getX()+90);
    		newAuraShot.shotRight();
    	}
    	else{
        	newAuraShot.setX(this.getX()-3);
    		newAuraShot.shotLeft();
    	}
    	TileMap map=getTileMap();
    	
    	map.addSprite(newAuraShot);
    }
    
    /**
     * Jurus ke 1 Gatotkaca
     */
    protected void auraBigShot(){
    	setCanShot(false);
    	AuraShot newAuraShot=auraBigShot.clone();
    	newAuraShot.setMaxSpeed(getMaxSpeed()*2);
    	newAuraShot.setX(this.getX());
    	newAuraShot.setY(this.getY()+32);
    	
    	if(isFacingRight())
    		newAuraShot.shotRight();
    	else
    		newAuraShot.shotLeft();
    	
    	TileMap map=getTileMap();
    	
    	map.addSprite(newAuraShot);    	
    }

    /**
     * Jurus ke 2 Gatotkaca
     */
    protected void biteShot(){
		soundManager.play(biteSound);
    	setCanShot(false);
    	BiteShot newAuraShot=biteShot.clone();
    	newAuraShot.setMaxSpeed(getMaxSpeed());
    	newAuraShot.setX(this.getX());
    	newAuraShot.setY(this.getY()-32);
    	
    	if(isFacingRight())
    		newAuraShot.shotRight();
    	else
    		newAuraShot.shotLeft();
    	
    	TileMap map=getTileMap();
    	
    	map.addSprite(newAuraShot);
    }

    public float getAttackDamage(){
    	float damage=super.getAttackDamage();
    	if(state==STATE_AURA_ATTACK){
    		damage *=2;
    	}
    	return damage;
    }
    
    public Gatotkaca clone(){
    	Gatotkaca newSprite=new Gatotkaca(
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
    			(Animation)auraPunchLeft.clone(),
    			(Animation)auraPunchRight.clone(),
    			(Animation)biteLeft.clone(),
    			(Animation)biteRight.clone(),
    			(AuraShot)auraShot.clone(),
    			(GKAuraShot)auraBigShot.clone(),
    			(BiteShot)biteShot.clone(),
    			(Sprite)shield.clone(),
    			gc,
    			getTileMap(),
    			soundManager, punchSound, hurtSound, dieSound,
    			auraPunchSound, biteSound, shieldSound);
    	newSprite.setGraphicsConfiguration(gc);
    	newSprite.addMap(getTileMap());
    	
    	return newSprite;
    }
    
    protected Sound getPunchSound(){
    	return punchSound;
    }
    
    public boolean isValidKill(Nature badguy){
    	if(badguy.isImmune())
    		return false;
    	
    	if(isShieldOn())
    		return true;
    	
    	if(isFacingRight()){
        	return getX()<=badguy.getX();    		
    	}
    	else
    	{
        	return getX()>badguy.getX();
    	}
    }
    
    public void rebirth(){
    	setMp(1f);
    	super.rebirth();
    }

    private Animation biteLeft;
    private Animation biteRight;
	private Animation auraPunchLeft;
	private Animation auraPunchRight;
	private GKAuraShot auraBigShot;
	private BiteShot biteShot;
	private Sprite shield;
	
	private boolean isShieldOn;
    	
	//Sound
	private Sound punchSound;
	private Sound auraPunchSound;
	private Sound biteSound;
	private Sound shieldSound;
	
    private static final float JUMP_SPEED=-0.9f;
    private static final float ATTACK_DAMAGE=0.1f;
    private static final float MOVE_SPEED=0.5f;
    private static final float MAX_HP=1f;

    private static final float AURA_ATTACK_MP=.1f;
    private static final float BITE_ATTACK_MP=.2f;
    private static final float SHIELD_MP=.001f;

}

package tilegame.sprites;

import sound.Sound;
import sound.SoundManager;
import graphics.Animation;

/**
 * Class <code>ButoAbang</code> menghandle semua animasi dan interaksi Buto Abang
 * @author Yohanes Surya
 */
public class ButoAbang extends ButoIjo{
	/**
	 * State ATTACK
	 */
    public static final int STATE_ATTACK=4;

    private static final float ATTACK_DAMAGE=0.1f;
    private static final float MOVE_SPEED=0.1f;
    private static final float MAX_HP=0.3f;

    /**
     * Membuat object baru
     * @param left animasi gerakan kekiri
     * @param right animasi gerakan kekanan
     * @param deadLeft animasi dead/dying left
     * @param deadRight animasi dead/dying right
     * @param attackLeft animasi serangan kekiri
     * @param attackRight animasi serangan kekanan
     * @param hurtLeft animasi kesakitan(hurt) kekiri
     * @param hurtRight animasi kesakitan(hurt) kekanan
     * @param flame animasi serangan
     * @param soundManager <code>SoundManager</code> untuk memainkan sound FX
     * @param hurtSound suara kesakitan 
     * @param dieSound suara mati
     */
    public ButoAbang(Animation left, Animation right,
            Animation deadLeft, Animation deadRight,
            Animation attackLeft, Animation attackRight,
            Animation hurtLeft, Animation hurtRight,
            Flame flame,
            SoundManager soundManager, 
            Sound hurtSound, Sound dieSound)
        {
            super(left, right, deadLeft, deadRight, hurtLeft, hurtRight,
            		soundManager, hurtSound, dieSound);
            
            this.attackLeft=attackLeft;
            this.attackRight=attackRight;
            this.flame=flame;
            this.tempFlame=flame.clone();
            this.canShot=true;
            
            setHp(MAX_HP);
            setAttackDamage(ATTACK_DAMAGE);
            setMaxSpeed(MOVE_SPEED);
        }
        
        public void update(long elapsedTime){
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
    	            	isImmune=false;
    	        		hurtRight.start();
    	        		newAnim=right;
    	        		setVelocityX(getMaxSpeed());
    	        		setState(STATE_NORMAL);
    	        	}
            	}else
            	{
    	        	newAnim=hurtLeft;
    	        	if(hurtLeft.isFinised()){
    	            	isImmune=false;
    	        		hurtLeft.start();
    	        		newAnim=left;
    	        		setVelocityX(-getMaxSpeed());
    	        		setState(STATE_NORMAL);
    	        	}        		
            	}
            	
            	tempFlame.setState(Flame.STATE_DEAD);
            }
        	else if(state==STATE_ATTACK){
        		setVelocityX(0);
    		    if (!isFacingRight()) {
    		        newAnim = attackLeft;
        		    if(attackLeft.isFinised())
        		    {
    	        		canShot=true;
        		    	attackLeft.start();
        		    	attackTimeElapsed=0;
        		    	newAnim=left;
    	        		setVelocityX(-getMaxSpeed());
        		    	setState(STATE_NORMAL);
                    	tempFlame.setState(Flame.STATE_DEAD);
        		    }
    		    }
    		    else{
    		        newAnim = attackRight;
        		    if(attackRight.isFinised())
        		    {
    	        		canShot=true;
        		    	attackRight.start();
        		    	attackTimeElapsed=0;
        		    	newAnim=right;
    	        		setVelocityX(getMaxSpeed());
        		    	setState(STATE_NORMAL);
                    	tempFlame.setState(Flame.STATE_DEAD);
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
        
        /**
         * Method untuk menyerang. Method ini tidak akan melakukan apa-apa jika
         * Buto Abang belum dapat menyerang yang dapat di check dengan method 
         * <code>canShot()</code>
         * @return Object <code>Flame<code>
         * @see tilegame.sprites.Flame
         * @see tilegame.sprites.ButoAbang#canShot()
         */
        public Flame auraShot(){
        	canShot=false;
        	Flame newAuraShot=flame.clone();
        	newAuraShot.setY(this.getY()+40);
        	
        	if(isFacingRight()){
            	newAuraShot.setX(this.getX()+181);
        		newAuraShot.shotRight();
        	}
        	else{
            	newAuraShot.setX(this.getX()-290);
        		newAuraShot.shotLeft();        		
        	}
        	
        	//map.addSprite(newAuraShot);
        	tempFlame=newAuraShot;
        	return newAuraShot;
        }
        
        /**
         * Mengcheck apakah object ini siap untuk menyerang lagi. NOTE: Karena 
         * Buto Abang mempunyai delay antara serangannya. Apakah dapat menyerang
         * kembali.
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
        
        public ButoAbang clone(){
        	ButoAbang ba=new ButoAbang(
        			(Animation)left.clone(), 
        			(Animation)right.clone(),
        			(Animation)deadLeft.clone(), 
        	        (Animation)deadRight.clone(),
        			(Animation)attackLeft.clone(), 
        	        (Animation)attackRight.clone(), 			
        			(Animation)hurtLeft.clone(),
        			(Animation)hurtRight.clone(),
        			(Flame)flame.clone(),
        			soundManager, hurtSound, dieSound
        			);
        	
        	return ba;
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
        
        /**
         * @return mengembalikan score 400
         */
        public int getScore(){
        	return 400;
        }
        
        /**
         * Object Flame
         * @see tilegame.sprite.Flame
         */
        protected Flame flame;

        /**
         * Object Animation
         * @see graphics.Animation
         */
        protected Animation attackLeft;

        /**
         * Object Animation
         * @see graphics.Animation
         */
        protected Animation attackRight;
        
        // to keep track
        private Flame tempFlame; 
        
        private int attackTimeElapsed;
        private boolean canShot;
        
        private static final int ATTACK_TIME=2000;
}

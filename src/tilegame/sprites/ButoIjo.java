package tilegame.sprites;

import sound.Sound;
import sound.SoundManager;
import graphics.Animation;
import graphics.Sprite;

/**
 * Class <code>ButoIjo</code> berjalan dari kiri kekanan dan akan berbalik jika
 * bertabrakan dengan tembok.
 * @author Yohanes Surya
 */
public class ButoIjo extends Nature {
	/**
	 * Membuat object baru
     * @param left animasi gerakan kekiri
     * @param right animasi gerakan kekanan
     * @param deadLeft animasi dead/dying left
     * @param deadRight animasi dead/dying right
     * @param hurtLeft animasi kesakitan(hurt) kekiri
     * @param hurtRight animasi kesakitan(hurt) kekanan
     * @param soundManager <code>SoundManager</code> untuk memainkan sound FX
     * @param hurtSound suara kesakitan 
     * @param dieSound suara mati
	 */
    public ButoIjo(Animation left, Animation right,
        Animation deadLeft, Animation deadRight,
        Animation hurtLeft, Animation hurtRight,
        SoundManager soundManager, 
        Sound hurtSound, Sound dieSound)
    {
        super(left, right, deadLeft, deadRight, hurtLeft, hurtRight,
        		soundManager, hurtSound, dieSound);
        
        setHp(MAX_HP);
        setMaxSpeed(MOVE_SPEED);
        setAttackDamage(ATTACK_DAMAGE);
    }
    
    public ButoIjo clone(){
    	ButoIjo bi=new ButoIjo(
    			(Animation)left.clone(), 
    			(Animation)right.clone(),
    			(Animation)deadLeft.clone(), 
    	        (Animation)deadRight.clone(),    			
    			(Animation)hurtLeft.clone(),
    			(Animation)hurtRight.clone(),
    			soundManager, hurtSound, dieSound
    			);
    	
    	return bi;
    }
    
    @Override
	public void notifyObjectCollision(Sprite collisionSprite) {
    	// Win condition only
    	Nature newSprite=(Nature)collisionSprite;
    	newSprite.setHp(newSprite.getHp()-getAttackDamage());
    	newSprite.damaged();
	}
    
    /**
     * @return mengembalikan score 200
     */
    public int getScore(){
    	return 200;
    }

    private static final float MAX_HP=0.25f;
    private static final float MOVE_SPEED=0.05f;
    private static final float ATTACK_DAMAGE=0.05f;
	
}

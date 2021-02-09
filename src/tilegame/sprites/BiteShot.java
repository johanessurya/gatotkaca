package tilegame.sprites;

import graphics.Animation;
import graphics.Sprite;

/**
 * Class <code>BiteShot</code> menghandle semua animasi 
 * jurus <code>Gatotkaca</code> yang ke 2
 * @author Yohanes
 * @see tilegame.sprites.Gatotkaca#biteShot()
 */
public class BiteShot extends Creature {
    private static final float ATTACK_DAMAGE=0.1f;
    private static final float MOVE_SPEED=0.1f;
    private static final float MAX_HP=0.2f;

    /**
     * Membuat object baru
     * @param left moving left
     * @param right moving right
     * @param deadLeft dead/dying left
     * @param deadRight dead/dying right
     */
    public BiteShot(Animation left, Animation right,
            Animation deadLeft, Animation deadRight)
        {
            super(left, right, deadLeft, deadRight);
        	setHp(MAX_HP);
            setAttackDamage(ATTACK_DAMAGE);
            setMaxSpeed(MOVE_SPEED);
        }
    
    /**
     * Selalu bernilai <code>true</code>
     */
    public boolean isFlying() {
        return true;
    }

    public BiteShot clone(){
    	BiteShot newSprite=new BiteShot(
    			(Animation)left.clone(),
    			(Animation)right.clone(),
    			(Animation)deadLeft.clone(),
    			(Animation)deadRight.clone());
    	
    	return newSprite;
    }

    /**
     * Memanggil method ini sama dengan mengset state dengan STATE_DYING
     */
    public void collideHorizontal() {
        setState(BiteShot.STATE_DYING);
    }

    /**
     * Memberitahu object ini bertumbukan dengan object <code>Sprite</code>
     */
	@Override
	public void notifyObjectCollision(Sprite collisionSprite) {
		// Win condition 
		Sprite sprite=collisionSprite;
		if(sprite instanceof ButoIjo || 
				sprite instanceof ButoAbang){
			//Damage enamy
			Nature newSprite=(Nature)sprite;
			newSprite.setHp(newSprite.getHp()-getAttackDamage());
			newSprite.damaged();
			
			//Demage it selft
			setHp(getHp()-newSprite.getAttackDamage());
			if(isDied()){
				setState(STATE_DYING);
			}
		}

	}

}

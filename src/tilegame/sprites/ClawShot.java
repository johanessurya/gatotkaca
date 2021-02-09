package tilegame.sprites;

import graphics.Animation;
import graphics.Sprite;

/**
 * Class <code>ClawShot</code> adalah serangan dari <code>Nagapecona</code>
 * @author Yohanes Surya
 * @see tilegame.sprites.Nagapecona#auraShot()
 */
public class ClawShot extends Flame {
	/**
	 * Membuat object baru
     * @param left moving left
     * @param right moving right
	 * @param hp banyak HP
	 * @param damage besar serangan
	 * @param speed kecepatan bergerak
	 */
	public ClawShot(Animation left, Animation right,
			float hp, float damage, float speed){
		super(left,right,left.clone(),right.clone(),
				hp, damage, speed);
	}
	
    public void update(long elapsedTime){
        // select the correct Animation
        Animation newAnim = anim;
        if (isFacingRight()) {
            newAnim = right;
        }
        else
        {
            newAnim = left;
        }

        // update the Animation
        if (anim != newAnim) {
            anim = newAnim;
        }
        else {
            anim.update(elapsedTime);
        }

        // update to "dead" state
        if (anim.isFinised()) {
            setState(STATE_DEAD);
        }    	

    }
	
    @Override
	public void notifyObjectCollision(Sprite collisionSprite) {
		//Do nothing
    	if(collisionSprite instanceof JabangTetuka){
    		JabangTetuka newSprite=(JabangTetuka)collisionSprite;
    		if(!newSprite.isImmune()){
    			newSprite.setHp(newSprite.getHp()-getAttackDamage());
    			newSprite.damaged();
    			setState(STATE_DEAD);    			
    		}
    	}
	}

    public ClawShot clone(){
    	ClawShot newSprite=new ClawShot(
    			(Animation)left.clone(),
    			(Animation)right.clone(),
    			getHp(), 
    			getAttackDamage(), 
    			getMaxSpeed());
    	
    	return newSprite;
    }

}

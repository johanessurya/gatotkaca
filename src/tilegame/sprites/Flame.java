package tilegame.sprites;

import graphics.Animation;
import graphics.Sprite;

public class Flame extends AuraShot {
	/**
	 * Membuat object baru
     * @param left moving left
     * @param right moving right
     * @param deadLeft dead/dying left
     * @param deadRight dead/dying right
	 * @param hp banyak HP
	 * @param damage besar serangan
	 * @param speed kecepatan bergerak
	 */
    public Flame(Animation left, Animation right,
            Animation deadLeft, Animation deadRight, 
            float hp, float damage, float speed){
        super(left, right, deadLeft, deadRight);
    	setHp(hp);
        setAttackDamage(damage);
        setMaxSpeed(speed);
    }

    @Override
	public void notifyObjectCollision(Sprite collisionSprite) {
		//Win Condition
    	if(collisionSprite instanceof Gatotkaca){
    		Gatotkaca newSprite=(Gatotkaca)collisionSprite;
    		if(!newSprite.isShieldOn()){
    			newSprite.setHp(newSprite.getHp()-getAttackDamage());
    			newSprite.damaged();    			
    		}
    	}
    	else if(collisionSprite instanceof JabangTetuka){
    		JabangTetuka newSprite=(JabangTetuka)collisionSprite;
			newSprite.setHp(newSprite.getHp()-getAttackDamage());
			newSprite.damaged();    			
    	}
	}
    
    public Flame clone(){
    	Flame newSprite=new Flame(
    			(Animation)left.clone(),
    			(Animation)right.clone(),
    			(Animation)deadLeft.clone(),
    			(Animation)deadRight.clone(),
    			getHp(), getAttackDamage(), getMaxSpeed());
    	
    	return newSprite;
    }

}

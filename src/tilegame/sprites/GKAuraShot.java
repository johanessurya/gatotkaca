package tilegame.sprites;

import graphics.Animation;
import graphics.Sprite;

public class GKAuraShot extends AuraShot {
	/**
	 * Membuat object baru
     * @param left animasi gerakan kekiri
     * @param right animasi gerakan kekanan
     * @param deadLeft animasi dead/dying left
     * @param deadRight animasi dead/dying right
	 */
    public GKAuraShot(Animation left, Animation right,
            Animation deadLeft, Animation deadRight)
        {
            super(left, right, deadLeft, deadRight);
        	setHp(MAX_HP);
            setAttackDamage(ATTACK_DAMAGE);
            setMaxSpeed(MOVE_SPEED);
        }    

    public GKAuraShot clone(){
    	GKAuraShot newSprite=new GKAuraShot(
    			(Animation)left.clone(),
    			(Animation)right.clone(),
    			(Animation)deadLeft.clone(),
    			(Animation)deadRight.clone());
    	
    	return newSprite;
    }

	@Override
	public void notifyObjectCollision(Sprite collisionSprite) {
		// TODO Auto-generated method stub
		Sprite sprite=collisionSprite;
		if(sprite instanceof ButoIjo || sprite instanceof ButoAbang){
			Nature newSprite=(Nature)sprite;
			newSprite.setHp(newSprite.getHp()-getAttackDamage());
			newSprite.damaged();
		}
	}

    private static final float ATTACK_DAMAGE=0.1f;
    private static final float MOVE_SPEED=0.5f;
    private static final float MAX_HP=0.1f;
}

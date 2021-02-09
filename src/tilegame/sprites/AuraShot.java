package tilegame.sprites;

import graphics.Animation;
import graphics.Sprite;

/**
 * Class <code>AuraShot</code> adalah <code>Creature</code> yang digunakan untuk
 * menghandel serangan object <code>JabangTetuka</code> dan <code>Gatotkaca</code>
 * @author Yohanes Surya
 *
 */
public class AuraShot extends Creature{
    private static final float ATTACK_DAMAGE=0.1f;
    private static final float MOVE_SPEED=0.4f;
    private static final float MAX_HP=0.1f;

    /**
     * Membuat object <code>AuraShot</code> dan mengset HP, Attack, dan Speed
     * secara default
     * @param left moving left
     * @param right moving right
     * @param deadLeft dead/dying left
     * @param deadRight dead/dying right
     */
    public AuraShot(Animation left, Animation right,
            Animation deadLeft, Animation deadRight)
        {
            super(left, right, deadLeft, deadRight);
        	setHp(MAX_HP);
            setAttackDamage(ATTACK_DAMAGE);
            setMaxSpeed(MOVE_SPEED);
        }
    
    /**
     * Membuat object <code>AuraShot</code> dan mengset HP, attack, dan speed
     * secara manual
     * @param left moving left
     * @param right moving right
     * @param deadLeft dead/dying left
     * @param deadRight dead/dying right
     * @param hp banyak HP
     * @param damage kekuatan serangan
     * @param speed kecepatan bergerak
     */
    public AuraShot(Animation left, Animation right,
            Animation deadLeft, Animation deadRight, 
            float hp, float damage, float speed){
        super(left, right, deadLeft, deadRight);
    	setHp(hp);
        setAttackDamage(damage);
        setMaxSpeed(speed);   
    }

    /**
     * Selalu bernilai <code>true</code>
     */
        public boolean isFlying() {
            return true;
        }
                
        /**
         * Akan mengset state(<code>setState()</code>) menjadi STATE_DYING
         * @see tilegame.sprites.Creature#collideHorizontal()
         */
        public void collideHorizontal() {
        	setState(STATE_DYING);
        }
        
        public AuraShot clone(){
        	AuraShot newSprite=new AuraShot(
        			(Animation)left.clone(),
        			(Animation)right.clone(),
        			(Animation)deadLeft.clone(),
        			(Animation)deadRight.clone(),
        			getHp(), getAttackDamage(), getMaxSpeed());
        	
        	return newSprite;
        }
        
        public void update(long elapsedTime){
        	super.update(elapsedTime);
        	if(anim.isFinised() && state==STATE_NORMAL)
        		setState(STATE_DYING);
        }

        /**
         * Memberitahu object ini bertumbukan dengan object <code>Sprite</code>
         */
		@Override
		public void notifyObjectCollision(Sprite collisionSprite) {
			// Win Condition Only
			Sprite sprite=collisionSprite;
			if(sprite instanceof ButoIjo || 
					sprite instanceof ButoAbang ||
					sprite instanceof Nagapecona){
				Nature newSprite=(Nature)sprite;
				if(!newSprite.isImmune()){
					newSprite.setHp(newSprite.getHp()-getAttackDamage());
					newSprite.damaged();
					setState(STATE_DEAD);
				}
			}
		}
}

package tilegame.sprites;

import graphics.*;

/**
 * Class <code>Creature</code> adalah <code>Sprite</code> dengan mengimplementasi
 * grafitasi dan dapat mati. Class ini mempunyai 4 animasi bergerak kekiri & 
 * kekanan, mati menghadap kekiri & kekanan.
 * @author Yohanes Surya
 */
public abstract class Creature extends Sprite implements Collision{
	/**
	 * Set state to NORMAL
	 */
    public static final int STATE_NORMAL = 0;
    
    /**
     * Set state to DYING
     */
    public static final int STATE_DYING = 1;
    
    /**
     * Set state to DEAD
     */
    public static final int STATE_DEAD = 2;

    /**
     * Moving left
     */
    protected Animation left;
    
    /**
     * Moving right
     */
    protected Animation right;
    
    /**
     * Dead/dying left
     */
    protected Animation deadLeft;
    
    /**
     * Dead/dying right
     */
    protected Animation deadRight;
    
    /**
     * State(STATE_NORMAL, STATE_DYING, or STATE_DEAD)
     */
    protected int state;
    
    
    //protected long stateTime;

    private float hp;
    private float mp;
    private float damage;
    private float speed;
    
    /**
     * Membuat object <code>Creature</code> baru dengan memasukkan 
     * <code>Animasi</code> yang di inginkan.
     * @param left Animasi bergerak kekiri
     * @param right Animasi bergerak kekanan
     * @param deadLeft Animasi mati kekiri
     * @param deadRight Animasi mati kekanan
     * @see graphics.Animation
     */
    public Creature(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(right);
        this.left = left;
        this.right = right;
        this.deadLeft = deadLeft;
        this.deadRight = deadRight;
        state = STATE_NORMAL;
    }

    /**
     * Membangunkan(wakeup) object ini saat pertama kali tampil di layar.
     * Secara default berjalan kekiri.
     */
    public void wakeUp() {
        if (getState() == STATE_NORMAL && getVelocityX() == 0) {
            setVelocityX(-getMaxSpeed());
        }
    }

    /**
     * Mendapatkan state object ini.
     * @return STATE_NORMAL, STATE_DYING, atau STATE_DEAD
     */
    public int getState() {
        return state;
    }

    /**
     * Set state
     * @param state STATE_NORMAL, STATE_DYING, atau STATE_DEAD
     */
    public void setState(int state) {
        if (this.state != state) {
            this.state = state;
            //stateTime = 0;
            if (state == STATE_DYING) {
                setVelocityX(0);
                setVelocityY(0);
            }
        }
    }

    /**
     * Check apakah <code>Creature</code> ini masih hidup
     * @return <code>true</code> jika hidup dan <code>false</code> 
     * jika sebaliknya 
     */
    public boolean isAlive() {
        return (state == STATE_NORMAL);
    }

    /**
     * Check apakah object ini terbang
     * @return <code>true</code> jika terbang dan <code>false</code> 
     * jika sebaliknya 
     */
    public boolean isFlying() {
        return false;
    }

    /**
     * Dipanggil sebelum <code>update()</code> jika bertabrakan dengan tembok
     * secara horizontal.
     */
    public void collideHorizontal() {
        setVelocityX(-getVelocityX());
    }


    /**
     * Dipanggil sebelum <code>update()</code> jika bertabrakan dengan tembok
     * secara vertikal.
     */
    public void collideVertical() {
        setVelocityY(0);
    }

    /**
     * @see graphics.Sprite
     */
    public void update(long elapsedTime) {
        // select the correct Animation
        Animation newAnim = anim;
        if (getVelocityX() < 0) {
            newAnim = left;
        }
        else if (getVelocityX() > 0) {
            newAnim = right;
        }
        if (state == STATE_DYING && newAnim == left) {
            newAnim = deadLeft;
        }
        else if (state == STATE_DYING && newAnim == right) {
            newAnim = deadRight;
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
     * Mendapatkan HP dari <code>Creature</code> ini
     * @return sisa HP
     */
    public float getHp(){
    	return hp;
    }
    
    /**
     * Mendapatkan HP dari <code>Creature</code> ini
     * @return sisa MP
     */
	public float getMp(){
		return mp;
	}
	
	/**
	 * Untuk mengset HP
	 * @param hp banyak HP
	 */
	public void setHp(float hp){
		this.hp=Math.max(hp,0);
	}
	
	/**
	 * Untuk mengset MP
	 * @param mp banyak MP
	 */
	public void setMp(float mp){
		this.mp=Math.max(mp,0);
		this.mp=Math.min(1, this.mp);
	}

	/**
	 * Untuk mengset kekuatan serangan
	 * @param damage kekuatan serangan
	 */
    public void setAttackDamage(float damage){
    	this.damage=damage;
    }

    /**
     * Mengambil kekuatan serangan
     * @return kekuatan serangan
     */
    public float getAttackDamage(){
    	return damage;
    }

    /**
     * Mengambil kecepatan jalan dari <code>Creature</code> ini
     * @return kecepatan jalan
     */
    public float getMaxSpeed() {
        return speed;
    }

    /**
     * Mengset kecepatan jalan
     * @param speed kecepatan jalan
     */
    public void setMaxSpeed(float speed){
    	this.speed=speed;
    }

    /**
     * Mengcheck apakah mati
     * @return <code>true</code> jika mati dan <code>false</code> 
     * jika sebaliknya 
     */
	public boolean isDied(){
		return hp<=0;
	}
	
	/**
	 * Mengambil score
	 * @return score
	 */
	public int getScore(){
		return 0;
	}
	
	/**
	 * Bergerak kekiri. Ini digunakan untuk <code>Sprite</code> serangan api
	 */
    public void shotLeft(){
    	setVelocityX(-getMaxSpeed());
    }
    
    /**
     * Bergerak kekanan. Ini digunakan untuk <code>Sprite</code> serangan api
     */
    public void shotRight(){
    	setVelocityX(getMaxSpeed());        	
    }
}

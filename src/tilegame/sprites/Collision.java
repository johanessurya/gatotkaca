package tilegame.sprites;

import graphics.Sprite;

/**
 * Interface <code>Collision</code> adalah untuk <code>Sprite</code> yang mempunyai reaksi 
 * tumbukan. Untuk <code>Sprite</code> yang tidak perlu adanya tumbukan tidak 
 * perlu menggunakan interface ini
 * @author Yohanes Surya
 * @see graphics.Sprite
 */
public interface Collision {
	/**
	 * Memberitahu object bahwa berinteraksi dengan <code>Sprite</code> yang 
	 * sudah ditentukan
	 * @param collisionSprite <code>Sprite</code> yang bertumbukan
	 */
	void notifyObjectCollision(Sprite collisionSprite);
}

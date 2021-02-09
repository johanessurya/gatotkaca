package graphics;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Class <code>Animation</code> menghandle gambar beruntun(frame) yang 
 * membentuk sebuah animasi bergerak. Setiap gambar(frame) mempunyai
 * waktu untuk ditampilkan.
 * @author Yohanes Surya
 */
public class Animation {
    private ArrayList<AnimFrame> frames;
    private int currFrameIndex;
    private long animTime;
    private long totalDuration;
    private boolean reachEnd;
    
    private boolean isCollisionObject;
    private boolean loop;

    /**
        Membuat baru dengan animasi masih kosong
    */
    public Animation(){
        this(new ArrayList<AnimFrame>(), 0, true, true);
    }

    /**
     * @param isCollisionObject variable dimana mengidentifikan apakah
     * object <code>Animation</code> ini nantinya perlu pengecheckan 
     * tumbukan(collision)
     * 
     * @param loop variable apakah animasi akan diulang.
     */
    public Animation(boolean isCollisionObject, boolean loop){
        this(new ArrayList<AnimFrame>(), 0,
        		isCollisionObject,loop);
    }
    
    /**
     * Adalah constructor yang berguna dalam penggandaan object(cloning)
     * @param frames runtutan frame
     * @param totalDuration total durasi animasi ini
     * @param isCollisionObject variable dimana mengidentifikan apakah
     * object <code>Animation</code> ini nantinya perlu pengecheckan 
     * tumbukan(collision)
     * 
     * @param loop variable apakah animasi akan diulang.
     */
    private Animation(ArrayList<AnimFrame> frames, long totalDuration,
    		boolean isCollisionObject, boolean loop) {
        this.frames = frames;
        this.totalDuration = totalDuration;
        this.isCollisionObject=isCollisionObject;
        this.loop=loop;
        this.reachEnd=false;
        start();
    }

    /**
     * Untuk menggandakan object ini
     */
    public Animation clone() {
        return new Animation(frames, totalDuration, 
        		isCollisionObject, loop);
    }

    /**
     * Menambahkan frame baru 
     * @param image gambar yang akan ditambahkan
     * @param duration lama waktu yang akan ditampilkan pada gambar ini
     */
    public synchronized void addFrame(Image image,
        long duration)
    {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }

    /**
     * Memulai animasi dari awal 
     */
    public synchronized void start() {
        animTime = 0;
        currFrameIndex = 0;
        reachEnd=false;
    }

    /**
     * Mengupdate animasi dengan spesifik waktu yang ditentukan
     * @param elapsedTime waktu untuk mengupdate animasi ini(dalam millisecond)
     */
    public synchronized void update(long elapsedTime) {
        if (frames.size() >= 1) {
            animTime += elapsedTime;
            if (animTime >= totalDuration) {
            	reachEnd=true;
                animTime = animTime % totalDuration;
                if(loop)
                	currFrameIndex = 0;
            }

            while (animTime > getFrame(currFrameIndex).endTime) {
                currFrameIndex++;
            }
        }
    }

    /**
    Gets this Animation's current image. Returns null if this
    animation has no images.
     */

    /**
     * Mengambil gambar yang sedang aktif
     * @return <code>null</code> jika tidak ada gambar di animasi ini
     */
    public synchronized Image getImage() {
        if (frames.size() == 0) {
            return null;
        }
        else {
            return getFrame(currFrameIndex).image;
        }
    }
    
    /**
     * Method untuk mengcheck tumbukan antar object(Collision Detection)
     */
    public Rectangle getFitRectangleCurrentFrame(){
    	Rectangle temp=null;
    	temp=getFrame(currFrameIndex).rect;
    	return temp;
    }
    
    private AnimFrame getFrame(int i) {
        return (AnimFrame)frames.get(i);
    }

    /**
     * Check apakah animasi ini sudah sampai akhir
     * @return <code>true</code> jika selesai <code>false</code> untuk sebaliknya
     */
    public boolean isFinised()
    {
    	return reachEnd;
    }
    
    /**
     * Mengambil total durasi
     * @return total durasi
     */
    public long getTotalDuration(){
    	return this.totalDuration;
    }
        
    private class AnimFrame {

        public Image image;
        public Rectangle rect;
        public long endTime;

        public AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
            if(isCollisionObject)
            	this.rect=ImageMan.getFitRectangle(image);
        }
    }
    
    /**
     * Mengambil frame yang sedang aktif
     * @return index frame
     */
    public int getCurrentFrame(){
    	return currFrameIndex;
    }
}

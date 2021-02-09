package tilemap;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import graphics.*;
import sound.Sound;
import sound.SoundManager;
import tilegame.background.Parallax;
import tilegame.background.ParallaxManager;
import tilegame.sprites.*;
import util.Loader;
import util.ResourceLoader;
import util.SectionResource;
import util.SimpleMath;
import util.SimpleSection;
import util.StoryLoader;

/**
 * Class <code>ResourceManager</code> menghandel semua resource(music dan gambar)
 * @author Yohanes Surya
 */
public class ResourceManager {
	/**
	 * Membuat object baru dengan <code>ScreenManager</code> yang ditetapkan
	 * @param screen <code>ScreenManager</code> yang ditetapkan
	 */
    public ResourceManager(ScreenManager screen) {
    	this.screen=screen;
    	
        this.gc = screen.getFrame().getGraphicsConfiguration();
        im=new ImageMan(this.gc);
        
        sectionResources=new ArrayList<SectionResource>();

        loadSound();
        loadTileImages();
        loadCreatureSprites();
        loadPowerUpSprites();
        loadFramePopUp();
        //loadSection();
    }
    
    private void loadFramePopUp(){
    	loadJabangTetukaPopUp();
    	loadGatotkacaPopUp();
    	loadComicPopUp();
    }
    
    private void loadJabangTetukaPopUp(){
		Animation fadeIn=new Animation(false,false);
		Animation fadeOut=new Animation(false,false);
		Animation normal=new Animation(false,false);
		
		Image frameImage=ResourceLoader.loadImage("images/jt.png");
		
		float acc=0;
		for(int i=0; i<=10; i++){
			fadeIn.addFrame(ImageMan.drawTransparent(frameImage, acc),50);			
			acc+=0.1;
			acc=SimpleMath.minMax(0, acc, 1);
		}

		acc=1;
		for(int i=0; i<=10; i++){
			fadeOut.addFrame(ImageMan.drawTransparent(frameImage, acc),50);		
			acc-=0.1;
			acc=SimpleMath.minMax(0, acc, 1);
		}
		
		normal.addFrame(frameImage, 100);

		jabangTetukaFrame=new FramePopUp(fadeIn,normal,fadeOut,0,0,
				soundManager,popUpSound);
    }
    
    private void loadGatotkacaPopUp(){
		Animation fadeIn=new Animation(false,false);
		Animation fadeOut=new Animation(false,false);
		Animation normal=new Animation(false,false);
		
		Image frameImage=ResourceLoader.loadImage("images/gk.png");
		
		float acc=0;
		for(int i=0; i<=10; i++){
			fadeIn.addFrame(ImageMan.drawTransparent(frameImage, acc),50);			
			acc+=0.1;
			acc=SimpleMath.minMax(0, acc, 1);
		}

		acc=1;
		for(int i=0; i<=10; i++){
			fadeOut.addFrame(ImageMan.drawTransparent(frameImage, acc),50);		
			acc-=0.1;
			acc=SimpleMath.minMax(0, acc, 1);
		}
		
		normal.addFrame(frameImage, 100);

		gatotkacaFrame=new FramePopUp(fadeIn,normal,fadeOut,0,0,
				soundManager,popUpSound);
    }
    
    private void loadComicPopUp(){
		Animation fadeIn=new Animation(false,false);
		Animation fadeOut=new Animation(false,false);
		Animation normal=new Animation(false,false);
		
		Image frameImage=ResourceLoader.loadImage("images/comic.png");
		
		float acc=0;
		for(int i=0; i<=10; i++){
			fadeIn.addFrame(ImageMan.drawTransparent(frameImage, acc),50);			
			acc+=0.1;
			acc=SimpleMath.minMax(0, acc, 1);
		}

		acc=1;
		for(int i=0; i<=10; i++){
			fadeOut.addFrame(ImageMan.drawTransparent(frameImage, acc),50);		
			acc-=0.1;
			acc=SimpleMath.minMax(0, acc, 1);
		}
		
		normal.addFrame(frameImage, 100);

		comicFrame=new FramePopUp(fadeIn,normal,fadeOut,0,0,
				soundManager,popUpSound);
    }

    /**
     * Mengambil object <code>JabangTetuka</code>
     * @return object <code>JabangTetuka</code>
     */
    public JabangTetuka getJabangTetuka(){
    	return (JabangTetuka)jabangTetukaSprite;
    }
    
    /**
     * Mengambil object <code>Gatotkaca</code>
     * @return object <code>Gatotkaca</code>
     */
    public Gatotkaca getGatotkaca(){
    	return (Gatotkaca)gatotkacaSprite;
    }    
    
    private void loadSound(){
        //soundManager = new SoundManager(PLAYBACK_FORMAT);
        
        DataLine.Info lineInfo = new DataLine.Info(
                SourceDataLine.class, PLAYBACK_FORMAT);

        Mixer mixer=getBestMixer(lineInfo);
        
        //Jika mixer null hentikan game
        if(mixer==null){
        	System.out.println("Komputer ini tidak support suara.");
        	System.out.println("Game dihentikan.");
        	System.exit(0);
        }
        
        //Print mixer info
        Mixer.Info mixerInfo=mixer.getMixerInfo();
        System.out.println("MIXER terbaik yang ditemukan di komputer ini:");
        System.out.printf("Name: %s \nVendor: %s \nVersion: %s \nDescription: %s \n",
        		mixerInfo.getName(),
        		mixerInfo.getVendor(),
        		mixerInfo.getVersion(),
        		mixerInfo.getDescription());
        System.out.println();
        
        int maxLines=mixer.getMaxLines(lineInfo);
        if(maxLines==AudioSystem.NOT_SPECIFIED){
        	maxLines=32;
        }

        if(maxLines<32){
        	System.out.println();
        	System.out.println("MIXER hanya support "+maxLines+" line");
        	System.out.println("Game ini minimal membutuhkan mixer yang support 32 line");
        	System.out.println("Game tidak menggunakan sound");
        	maxLines=-1;
        }
        
        soundManager=
        	new SoundManager(PLAYBACK_FORMAT,mixer,maxLines);
        bgMusic=soundManager.getSound("sounds/stage.wav");
        bossMusic=soundManager.getSound("sounds/boss.wav");
        comicMusic=soundManager.getSound("sounds/comicbg.wav");
        creditMusic=soundManager.getSound("sounds/credit.wav");
        bgMainMenuMusic=soundManager.getSound("sounds/mainmenubg.wav");
        pageNext=soundManager.getSound("sounds/comicnext.wav");
        pageBack=soundManager.getSound("sounds/comicback.wav");
        popUpSound=soundManager.getSound("sounds/popup.wav");
        
        kendiSound=soundManager.getSound("sounds/kendi.wav");
        liveSound=soundManager.getSound("sounds/lifeup.wav");
        waskitaSound=soundManager.getSound("sounds/waskita.wav");   
    }
    
    private Mixer getBestMixer(DataLine.Info lineInfo){
    	Mixer.Info[] mixers=AudioSystem.getMixerInfo();
    	
    	Mixer mixer=null;
    	Mixer temp=null;
    	for(int i=0; i<mixers.length; i++){
        	temp=AudioSystem.getMixer(mixers[i]);
        	
        	if(temp.getMaxLines(lineInfo)==AudioSystem.NOT_SPECIFIED){
        		return temp;
        	}
        	
        	if(mixer==null){
        		mixer=AudioSystem.getMixer(mixers[i]);
        	}
        	else if(temp.getMaxLines(lineInfo)>mixer.getMaxLines(lineInfo)){
        		mixer=temp;
        	}
    	}

    	return mixer;
    }
    
    /**
     * Mengambil object <code>SoundManager</code>
     * @return object SoundManager
     */
    public SoundManager getSoundManager(){
    	return soundManager;
    	//return new SoundManager(PLAYBACK_FORMAT);
    }

    private Image loadImage(String name) {
        return ResourceLoader.loadImage("images/"+name);
    }

    private Image getMirrorImage(Image image) {
        return im.getMirrorImage(image);
    }

    /**
     * Load map dari sebuah object <code>Bufferedreader</code>
     * @param bufReader <code>Bufferedreader</code> yang ditetapkan
     * @return object <code>TileMap</code>
     * @throws IOException jika terjadi error dalam membaca file.
     */
    public TileMap loadMap(BufferedReader bufReader)
        throws IOException
    {
        ArrayList<String> lines = new ArrayList<String>();
        int width = 0;
        int height = 0;

        // read every line in the text file into the list
        BufferedReader reader = bufReader;
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, tiles.get(tile));
                }

                // check if the char represents a sprite
                else if (ch == 'l') {
                    addSprite(newMap, liveSprite, x, y);
                }
                else if (ch == 's') {
                    addSprite(newMap, starSprite, x, y);
                }
                else if (ch == 'k') {
                    addSprite(newMap, kendiSprite, x, y);
                }
                else if (ch == '1') {
                    addSprite(newMap, butoIjoSprite, x, y);
                }
                else if (ch == '2') {
                    addSprite(newMap, butoAbangSprite, x, y);
                }
                else if (ch == '3') {
                    addSprite(newMap, nagapeconaSprite, x, y);
                }
            }
        }

        return newMap;
    }

    private void addSprite(TileMap map,
        Sprite hostSprite, int tileX, int tileY)
    {
        if (hostSprite != null) {
            // clone the sprite from the "host"
        	
            //Sprite sprite = (Sprite)hostSprite.clone();
        	Sprite sprite = clone(hostSprite);

        	
            // center the sprite
            sprite.setX(
                TileMapRenderer.tilesToPixels(tileX) +
                (TileMapRenderer.tilesToPixels(1) -
                sprite.getWidth()) / 2);

            // bottom-justify the sprite
            sprite.setY(
                TileMapRenderer.tilesToPixels(tileY + 1) -
                sprite.getHeight());

            // add it to the map
            map.addSprite(sprite);
        }
    }
    
    private Sprite clone(Sprite sprite){
    	Sprite temp=null;
    	
    	if(sprite instanceof Gatotkaca){
        	Gatotkaca jt=(Gatotkaca)sprite;
        	Gatotkaca newJt=jt.clone();
        	temp=newJt;
    	}
    	else if(sprite instanceof JabangTetuka){
        	JabangTetuka jt=(JabangTetuka)sprite;
        	JabangTetuka newJt=jt.clone();
        	temp=newJt;
        }
    	else if(sprite instanceof ButoIjo){
    		ButoIjo bi=(ButoIjo)sprite;
    		ButoIjo newBi=bi.clone();
    		temp=(Sprite)newBi;
    	}
    	else if(sprite instanceof ButoAbang){
    		ButoAbang ba=(ButoAbang)sprite;
    		ButoAbang newBa=ba.clone();
        	temp=newBa;
        }
    	else if(sprite instanceof Nagapecona){
    		Nagapecona ba=(Nagapecona)sprite;
    		Nagapecona newBa=ba.clone();
        	temp=newBa;
        }
    	else if(sprite instanceof AuraShot){
    		AuraShot ba=(AuraShot)sprite;
    		AuraShot newBa=ba.clone();
        	temp=newBa;    		
    	}
    	else if(sprite instanceof PowerUp)
    	{
        	if(sprite instanceof PowerUp.Star){
        		PowerUp.Star newSprite=(PowerUp.Star)sprite;
        		temp=newSprite.clone();
        	}
        	else if(sprite instanceof PowerUp.Kendi){
        		PowerUp.Kendi newSprite=(PowerUp.Kendi)sprite;
        		temp=newSprite.clone();
        	}    		
        	else if(sprite instanceof PowerUp.Live){
        		PowerUp.Live newSprite=(PowerUp.Live)sprite;
        		temp=newSprite.clone();
        	}    		
    	}

    	return temp;
    }

    private void loadTileImages() {
    	tiles=ResourceLoader.loadTileImages();
    }

    private void loadCreatureSprites() {
        gatotkacaSprite=(Sprite)loadGatotkaca();
    	jabangTetukaSprite=(Sprite)loadJabangTetuka();
    	
        //Load enemy - Buto Ijo
        butoIjoSprite=loadButoIjo();
        
        //Load enemy - Buto Abang
        butoAbangSprite=loadButoAbang();
        
        //Load enemy - Nagapecona
        nagapeconaSprite=loadNagapecona();
    }
        
    private Gatotkaca loadGatotkaca(){
    	String dir="gatotkaca";
    	
        int idle=3;
        int run=10;
        int jump=1;
        int attack=3;
        int hurt=1;
        int auraPunch=2;
        int chargingAura=7;
        int ball=40;

        Animation[] runAnim=loadAnimation(dir+"/run",run,100);
        Animation[] attackAnim=loadAnimation(dir+"/attack",attack,100);
        Animation[] hurtAnim=loadAnimation(dir+"/hurt",hurt,100);
        Animation[] jumpAnim=loadAnimation(dir+"/jump",jump,100);
        Animation[] idleAnim=loadAnimation(dir+"/idle",idle,300);
        Animation[] auraPunchAnim=loadAuraPunchAnimation(dir+"/aurapunch",auraPunch);
        
        Animation[] chargingAuraAnim=
        	loadBiteActionAnimation(dir+"/biteaction",chargingAura);

        AuraShot auraShot=loadGatotkacaAuraShot();
        GKAuraShot auraBigShot=loadGatotkacaAuraBigShot();
        BiteShot biteShot=loadGatotkacaBiteShot();
        Animation[] anim=loadAnimation(dir+"/ball/ball",ball,35);
    	Sprite shield=new Sprite(anim[0]);
    	
    	Sound punchSound=loadSound("gkpunch.wav");
    	Sound hurtSound=loadSound("gkhurt.wav");
    	Sound dieSound=loadSound("gkdie.wav");
    	Sound auraPunchSound=loadSound("gkaurapunch.wav");
    	Sound biteSound=loadSound("gkbite.wav");
    	Sound shieldSound=loadSound("gkshield.wav");
    	
    	Gatotkaca newSprite=new Gatotkaca(runAnim[0], runAnim[1],
        		hurtAnim[0].clone(), hurtAnim[1].clone(),
        		jumpAnim[0],jumpAnim[1],
        		idleAnim[0],idleAnim[1],
        		attackAnim[0], attackAnim[1],
        		hurtAnim[0],hurtAnim[1],
        		auraPunchAnim[0], auraPunchAnim[1],
        		chargingAuraAnim[0],chargingAuraAnim[1],
        		auraShot,
        		auraBigShot,biteShot,shield,
        		gc,
        		soundManager,punchSound, hurtSound,dieSound,
        		auraPunchSound, biteSound, shieldSound);

    	return newSprite;

    }
    
    private Sound loadSound(String filename){
    	return soundManager.getSound("sounds/"+filename);
    }
    
    private Animation[] loadAuraPunchAnimation(String filename, int n){
        int max=n;
        Image[] left=new Image[max];
        Image[] right=new Image[max];

        //For idle
        Animation leftAnim=new Animation();
        Animation rightAnim=new Animation();

        for(int i=0; i<max; i++)
        {
            //for load image
            int j=i+1;
            
            //For Idle
            if(j<=max)
            {
            	int delay=0;
            	if(j==1){
            		delay=500;
            	}
            	else{
            		delay=200;
            	}
                right[i]=loadImage(filename+j+".png");
                rightAnim.addFrame(right[i], delay);

                left[i]=getMirrorImage(right[i]);
                leftAnim.addFrame(left[i], delay);                
            }
        }
        
        Animation[] animTemp=new Animation[2];
        animTemp[0]=leftAnim;
        animTemp[1]=rightAnim;
        
        return animTemp;
    }

    private Animation[] loadBiteActionAnimation(String filename, int n){
        int max=n;
        Image[] left=new Image[max];
        Image[] right=new Image[max];

        //For idle
        Animation leftAnim=new Animation();
        Animation rightAnim=new Animation();

        for(int i=0; i<max; i++)
        {
            //for load image
            int j=i+1;
            
            //For Idle
            if(j<=max)
            {
            	int delay=0;
            	if(j==max){
            		delay=300;
            	}
            	else{
            		delay=100;
            	}
                right[i]=loadImage(filename+j+".png");
                rightAnim.addFrame(right[i], delay);

                left[i]=getMirrorImage(right[i]);
                leftAnim.addFrame(left[i], delay);                
            }
        }
        
        Animation[] animTemp=new Animation[2];
        animTemp[0]=leftAnim;
        animTemp[1]=rightAnim;
        
        return animTemp;
    }    
    
    private AuraShot loadGatotkacaAuraShot(){
    	String dir="gatotkaca";
    	
        int run=1;
        int dead=1;

        Animation[] runAnim=loadAnimation(dir+"/smallaurashot",run,400);
        Animation[] deadAnim=loadAnimation(dir+"/smallbroken",dead,100);
    	
    	AuraShot newSprite=new AuraShot(runAnim[0], runAnim[1],
    			(Animation)deadAnim[0], (Animation)deadAnim[1]);

    	return newSprite;
    }
    
    private GKAuraShot loadGatotkacaAuraBigShot(){
    	String dir="gatotkaca";
    	
        int run=1;
        int dead=1;

        Animation[] runAnim=loadAnimation(dir+"/aurashot",run,500);
        Animation[] deadAnim=loadAnimation(dir+"/broken",dead,100);
    	
    	GKAuraShot newSprite=new GKAuraShot(runAnim[0], runAnim[1],
    			deadAnim[0], deadAnim[1]);

    	return newSprite;
    }
    
    private BiteShot loadGatotkacaBiteShot(){
    	String dir="gatotkaca";
    	
        int run=2;

        Animation[] runAnim=loadAnimation(dir+"/bite",run,200);
        Animation[] dieAnim=loadAnimation(dir+"/bite",1,10);
    	
        BiteShot newSprite=new BiteShot(runAnim[0], runAnim[1],
    			(Animation)dieAnim[0], (Animation)dieAnim[1]);
    	
    	return newSprite;
    }
    
    private JabangTetuka loadJabangTetuka(){
    	String dir="jabangtetuka";
    	
        int run=4;
        int dead=1;
        int jump=1;
        int hurt=1;

        Animation[] runAnim=loadAnimation(dir+"/run",run,100);
        Animation[] deadAnim=loadAnimation(dir+"/hurt",dead,100);
        Animation[] attackAnim=loadJabangTetukaAttack();
        Animation[] hurtAnim=loadAnimation(dir+"/hurt",hurt,100);
        Animation[] jumpAnim=loadAnimation(dir+"/jump",jump,100);
        Animation[] idleAnim=loadJabangTetukaIdle();

        AuraShot auraShot=loadJabangtetukaAuraShot();

    	Sound hurtSound=loadSound("jthurt.wav");
    	Sound dieSound=hurtSound;

    	JabangTetuka jt=new JabangTetuka(runAnim[0], runAnim[1],
        		deadAnim[0], deadAnim[1],
        		jumpAnim[0],jumpAnim[1],
        		idleAnim[0],idleAnim[1],
        		attackAnim[0], attackAnim[1],
        		hurtAnim[0],hurtAnim[1],auraShot,
        		gc,
        		soundManager,hurtSound,dieSound);
    	
    	return jt;
    }
    
    private Animation[] loadJabangTetukaIdle(){
    	String filename="jabangtetuka/idle";
    	
    	int max=2;
        Image[] left=new Image[max];
        Image[] right=new Image[max];

        //For idle
        Animation leftAnim=new Animation();
        Animation rightAnim=new Animation();

        for(int i=0; i<max; i++)
        {
            //for load image
            int j=i+1;
            
            //For Idle
            if(j<=max)
            {
                right[i]=loadImage(filename+j+".png");
                left[i]=getMirrorImage(right[i]);            }
        }
        
        rightAnim.addFrame(right[0], 200);
        rightAnim.addFrame(right[1], 200);
        rightAnim.addFrame(right[0], 800);
        rightAnim.addFrame(right[1], 200);
        rightAnim.addFrame(right[0], 200);
        rightAnim.addFrame(right[1], 200);
        
        leftAnim.addFrame(left[0], 200);
        leftAnim.addFrame(left[1], 200);
        leftAnim.addFrame(left[0], 800);
        leftAnim.addFrame(left[1], 200);
        leftAnim.addFrame(left[0], 200);
        leftAnim.addFrame(left[1], 200);

        Animation[] animTemp=new Animation[2];
        animTemp[0]=leftAnim;
        animTemp[1]=rightAnim;
        
        return animTemp;
    }
    
    private Animation[] loadJabangTetukaAttack(){
    	String filename="jabangtetuka/attack";
    	
    	int max=2;
        Image[] left=new Image[max];
        Image[] right=new Image[max];

        //For idle
        Animation leftAnim=new Animation();
        Animation rightAnim=new Animation();

        for(int i=0; i<max; i++)
        {
            //for load image
            int j=i+1;
            
            //For Idle
            if(j<=max)
            {
            	int delay=0;
            	if(j==1){
            		delay=100;
            	}
            	else
            	{
            		delay=800;
            	}
                right[i]=loadImage(filename+j+".png");
                rightAnim.addFrame(right[i], delay);

                left[i]=getMirrorImage(right[i]);
                leftAnim.addFrame(left[i], delay);                
            }
        }
        
        Animation[] animTemp=new Animation[2];
        animTemp[0]=leftAnim;
        animTemp[1]=rightAnim;
        
        return animTemp;
    }
    
    private AuraShot loadJabangtetukaAuraShot(){
    	String dir="jabangtetuka";
    	
        int run=4;

        Animation[] runAnim=loadAnimation(dir+"/aurashot",run,200);
    	
    	AuraShot newSprite=new AuraShot(runAnim[0], runAnim[1],
    			(Animation)runAnim[0].clone(), (Animation)runAnim[1].clone(),
    			1f,.1f,.1f);
    	
    	return newSprite;
    }

    private ButoIjo loadButoIjo(){
    	String dir="butoijo";
        
        int run=4;
        int dead=1;
        int hurt=1;

        Animation[] runAnim=loadAnimation(dir+"/run",run,300);
        Animation[] deadAnim=loadAnimation(dir+"/die",dead,100);
        Animation[] hurtAnim=loadAnimation(dir+"/hurt",hurt,1000);   	
        
    	Sound hurtSound=loadSound("bihurt.wav");
    	Sound dieSound=loadSound("bidie.wav");

        return new ButoIjo(runAnim[0], runAnim[1],
        		deadAnim[0], deadAnim[1], 
        		hurtAnim[0],hurtAnim[1],
        		soundManager, hurtSound,dieSound);
        }

    private ButoAbang loadButoAbang(){
    	String dir="butoabang";
        
        int run=1;
        int dead=1;
        int attack=1;
        int hurt=1;

        Flame auraShot=loadButoAbangAuraShot();

        Animation[] runAnim=loadAnimation(dir+"/run",run,100);
        Animation[] deadAnim=loadAnimation(dir+"/hurt",dead,100);
        Animation[] attackAnim=loadAnimation(dir+"/attack",attack,300*8);
        Animation[] hurtAnim=loadAnimation(dir+"/hurt",hurt,300);

    	Sound hurtSound=loadSound("bahurt.wav");
    	Sound dieSound=loadSound("badie.wav");

        ButoAbang ba=new ButoAbang(runAnim[0], runAnim[1],
        		deadAnim[0], deadAnim[1],
        		attackAnim[0], attackAnim[1],
        		hurtAnim[0],hurtAnim[1],auraShot,
        		soundManager, hurtSound,dieSound);
        
    	return ba;
    }
    
    private Flame loadButoAbangAuraShot(){
        Animation[] runAnim=loadButoAbangFlame();
    	
    	Flame newSprite=new Flame(runAnim[0], runAnim[1],
    			(Animation)runAnim[0].clone(), (Animation)runAnim[1].clone(),
    			1f,.05f,.0001f);
    	
    	return newSprite;
    }
    
    private Animation[] loadButoAbangFlame(){
    	String filename="butoabang/flame";
    	
    	int max=2;
        Image[] left=new Image[max];
        Image[] right=new Image[max];
        
        right[0]=loadImage(filename+"1.png");
        right[1]=loadImage(filename+"2.png");

        left[0]=getMirrorImage(right[0]);
        left[1]=getMirrorImage(right[1]);

        //For idle
        Animation leftAnim=new Animation();
        Animation rightAnim=new Animation();

        int delay=300;
        rightAnim.addFrame(right[0], delay);
        rightAnim.addFrame(right[1], delay);
        rightAnim.addFrame(right[0], delay);
        rightAnim.addFrame(right[1], delay);
        rightAnim.addFrame(right[0], delay);
        rightAnim.addFrame(right[1], delay);
        rightAnim.addFrame(right[0], delay);
        rightAnim.addFrame(right[1], delay);

        leftAnim.addFrame(left[0], delay);                
        leftAnim.addFrame(left[1], delay);                
        leftAnim.addFrame(left[0], delay);                
        leftAnim.addFrame(left[1], delay);                
        leftAnim.addFrame(left[0], delay);                
        leftAnim.addFrame(left[1], delay);                
        leftAnim.addFrame(left[0], delay);                
        leftAnim.addFrame(left[1], delay);                
        
        Animation[] animTemp=new Animation[2];
        animTemp[0]=leftAnim;
        animTemp[1]=rightAnim;
        
        return animTemp;
    }
    
    private ButoAbang loadNagapecona(){
    	String dir="nagapecona";
        
        int run=9;
        int hurt=1;

        ClawShot longHand=loadNagapeconaLongHand();

        Animation[] runAnim=loadAnimation(dir+"/fly",run,100);
        
        //Animation[] deadAnim=loadAnimation(dir+"/hurt",dead,100);
        //Animation[] attackAnim=loadAnimation(dir+"/claw",attack,200);
        //Animation[] attackAnim=loadClawAttack();
        Animation[] attackAnim=loadAnimation(dir+"/long",1,1000);
        Animation[] hurtAnim=loadAnimation(dir+"/fly",hurt,300);

    	Sound hurtSound=loadSound("nphurt1.wav");
    	Sound dieSound=loadSound("npdie.wav");

        Nagapecona newSprite=new Nagapecona(runAnim[0], runAnim[1],
        		runAnim[0].clone(), runAnim[1].clone(),
        		hurtAnim[0], hurtAnim[1],
        		attackAnim[0], attackAnim[1],
        		longHand,gc,
        		soundManager, hurtSound,dieSound);
        
        
    	return newSprite;
    }
    
    private ClawShot loadNagapeconaLongHand(){
    	String dir="nagapecona/longshot";
    	    	
    	int max=4;
        Image[] left=new Image[max];
        Image[] right=new Image[max];
        
        for(int i=1; i<=max; i++)
        {
        	right[i-1]=loadImage(dir+i+".png");
        	left[i-1]=getMirrorImage(right[i-1]);
        }

        //For idle
        Animation leftAnim=new Animation();
        Animation rightAnim=new Animation();

        int delay=100;
        rightAnim.addFrame(right[0], delay);
        rightAnim.addFrame(right[1], delay);
        rightAnim.addFrame(right[2], delay);
        rightAnim.addFrame(right[3], delay);
        rightAnim.addFrame(right[2], delay);
        rightAnim.addFrame(right[1], delay);
        rightAnim.addFrame(right[0], delay);


        leftAnim.addFrame(left[0], delay);                
        leftAnim.addFrame(left[1], delay);                
        leftAnim.addFrame(left[2], delay);                
        leftAnim.addFrame(left[3], delay);                        
        leftAnim.addFrame(left[2], delay);                
        leftAnim.addFrame(left[1], delay);                
        leftAnim.addFrame(left[0], delay);                
        
        ClawShot newSprite=new ClawShot(leftAnim, rightAnim,
    			1f,.05f,.0001f);
    	
    	return newSprite;

    }    
    
    private Animation[] loadAnimation(String filename, int n,int delay){
        int max=n;
        Image[] left=new Image[max];
        Image[] right=new Image[max];

        //For idle
        Animation leftAnim=new Animation();
        Animation rightAnim=new Animation();

        for(int i=0; i<max; i++)
        {
            //for load image
            int j=i+1;
            
            //For Idle
            if(j<=max)
            {
                right[i]=loadImage(filename+j+".png");
                rightAnim.addFrame(right[i], delay);

                left[i]=getMirrorImage(right[i]);
                leftAnim.addFrame(left[i], delay);                
            }
        }
        
        Animation[] animTemp=new Animation[2];
        animTemp[0]=leftAnim;
        animTemp[1]=rightAnim;
        
        return animTemp;
    }

    private void loadPowerUpSprites() {
    	String dir="item";
        // create "goal" sprite
        Animation anim = new Animation();
        anim.addFrame(loadImage(dir+"/nyawa1.png"),1500);
        anim.addFrame(loadImage(dir+"/nyawa2.png"),1500);
        liveSprite=new PowerUp.Live(anim, soundManager, liveSound);

        anim=new Animation();
        anim.addFrame(loadImage(dir+"/kendi.png"),1000);
        kendiSprite=new PowerUp.Kendi(anim, soundManager, kendiSound);

        anim=new Animation();
        anim.addFrame(loadImage(dir+"/waskita1.png"),200);
        anim.addFrame(loadImage(dir+"/waskita2.png"),200);
        anim.addFrame(loadImage(dir+"/waskita3.png"),200);
        anim.addFrame(loadImage(dir+"/waskita4.png"),200);
        starSprite=new PowerUp.Star(anim, soundManager, waskitaSound);
    }
    
    private void loadSection(){
    	String filename="story.xml";
    	File file=new File(filename);
    	
    	Loader loader=null;
    	StoryLoader story=null;
    	ArrayList<SimpleSection> simpleSections=null;
    	sectionResources=new ArrayList<SectionResource>();
    	//Check if story.xml exists
    	if(file.exists())
	    	//Try load external resource
	    	try {
				story=
					new StoryLoader(new FileInputStream(filename));
	        	simpleSections=story.getSimpleSections();
	        	for(int i=0; i<simpleSections.size(); i++)
	        	{
	            	loader=
	            		new Loader.External(simpleSections.get(i), this);
	            	if(loader.getSectionResource()==null)break;
		        	sectionResources.add(loader.getSectionResource());
	        	}
	
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block	    	
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		//If story still null load default resource
		if(story==null || loader==null || loader.getSectionResource()==null)
	    	try {
				story=
					new StoryLoader(ResourceLoader.loadInputStream(filename));
	        	simpleSections=story.getSimpleSections();
	        	
	        	for(int i=0; i<simpleSections.size(); i++)
	        	{
		        	loader=
		        		new Loader.Default(simpleSections.get(i), this);
		        	sectionResources.add(loader.getSectionResource());
	        	}
			} catch (FileNotFoundException e) {
				// This will not happen
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// This will not happen
				e.printStackTrace();
			} catch (SAXException e) {
				// This will not happen
				e.printStackTrace();
			} catch (IOException e) {
				// This will not happen
				e.printStackTrace();
			}
    }

    /**
     * Membuat object <code>TileMapRenderer</code> dan mengembalikannya
     * @return object <code>TileMapRenderer</code>
     */
    public TileMapRenderer getTileMapRenderer(){
    	TileMapRenderer renderer;
        renderer=new TileMapRenderer();
        renderer.setOffsetYplus(38);
        //renderer.setBackground(loadImage("bg/layer0.jpg"));

        //For background parallax
        ParallaxManager background=new ParallaxManager();
        background.addParallax(new Parallax(loadImage("bg/layer0.jpg"),
        		0.5f, screen.getFrame()));
        

        //For foreground parallax
        ParallaxManager foreground=new ParallaxManager();
        foreground.addParallax(new Parallax(loadImage("bg/layer3.png"),
        		0.4f, screen.getFrame()));

        renderer.addBackgroundParallaxManager(background);
        renderer.addForegroundParallaxManager(foreground);
    	return renderer;
    }
    
    /**
     * Mendapatkan objcet <code>SectionResource</code> dalam betuk 
     * <code>ArrayList</code>
     * @return objcet <code>SectionResource</code>
     */
    public ArrayList<SectionResource> getSectionResources(){
    	loadSection();
    	return sectionResources;
    }
    
    /**
     * Mendapatkan musik background
     * @return object <code>Sound</code> musik background
     */
    public Sound getNormalBgMusic(){
    	return bgMusic;
    }

    /**
     * Mendapatkan musik credit
     * @return object <code>Sound</code> musik credit
     */
    public Sound getCreditBgMusic(){
    	return creditMusic;
    }

    /**
     * Mendapatkan musik boss
     * @return object <code>Sound</code> musik boss
     */
    public Sound getBossBgMusic(){
    	return bossMusic;
    }
    
    /**
     * Mendapatkan musik comic
     * @return object <code>Sound</code> musik comic
     */
    public Sound getComicBgMusic(){
    	return comicMusic;
    }
    
    /**
     * Mendapatkan musik "next comic"
     * @return object <code>Sound</code> musik "next comic"
     */
    public Sound getPageNextSound(){
    	return pageNext;
    }
    
    /**
     * Mendapatkan musik "back comic"
     * @return object <code>Sound</code> musik "back comic"
     */
    public Sound getPageBackSound(){
    	return pageBack;
    }    
    
    /**
     * Mendapatkan musik background main menu
     * @return object <code>Sound</code> musik background main menu
     */
    public Sound getBgMainMenuMusic(){
    	return bgMainMenuMusic;
    }
    
    /**
     * Mengambil <code>FramePopUp</code> object untuk frame pop up Jabang Tetuka
     * jika pertama kali memainkan permainan ini
     * @return <code>FramePopUp</code> yang diminta
     */
    public FramePopUp getJabangTetukaFramePopUp(){
    	return jabangTetukaFrame;
    }
    
    /**
     * Mengambil <code>FramePopUp</code> object untuk frame pop up Gatotkaca
     * jika pertama kali memainkan permainan ini
     * @return <code>FramePopUp</code> yang diminta
     */
    public FramePopUp getGatotkacaFramePopUp(){
    	return gatotkacaFrame;
    }

    /**
     * Mengambil <code>FramePopUp</code> object untuk frame pop up Comic
     * jika pertama kali memainkan permainan ini
     * @return <code>FramePopUp</code> yang diminta
     */
    public FramePopUp getComicFramePopUp(){
    	return comicFrame;
    }

	//Default AudioFormat
    private static final AudioFormat PLAYBACK_FORMAT =
        new AudioFormat(44100, 16, 2, true, false);

    private ArrayList<Image> tiles;
    private GraphicsConfiguration gc;
    private ImageMan im;
    private Sound bgMusic;
    private Sound bossMusic;
    private Sound comicMusic;
    private Sound creditMusic;
    private Sound bgMainMenuMusic;
    private Sound pageNext;
    private Sound pageBack;
    private Sound popUpSound;
    
    // host sprites used for cloning
    private Sprite gatotkacaSprite;
	private Sprite jabangTetukaSprite;
    private Sprite starSprite;
    private Sprite liveSprite;
    private Sprite kendiSprite;
    private Sprite butoIjoSprite;
    private Sprite butoAbangSprite;
    private Sprite nagapeconaSprite;
    
    //Frame Pop Up
    private FramePopUp jabangTetukaFrame;
    private FramePopUp gatotkacaFrame;
    private FramePopUp comicFrame;
    
    private ScreenManager screen;
    private SoundManager soundManager;
    
    //Sound
    private Sound liveSound;
    private Sound kendiSound;
    private Sound waskitaSound;
    
    private ArrayList<SectionResource> sectionResources;    
}

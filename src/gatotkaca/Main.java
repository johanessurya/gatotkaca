package gatotkaca;

import graphics.*;

import sound.SoundManager;

import tilemap.LoadingScreen;
import tilemap.MainMenu;
import tilemap.ResourceManager;
import tilemap.GameStopper;

import util.SectionManager;

/**
 * Class <code>Main</code> adalah Class utama untuk menjalankan game
 * "Gatotkaca - Otot Kawat Tulang Besi"
 * @author Yohanes Surya
 */
public class Main{
	/** Method main adalah method utama untuk menjalankan game ini */
    public static void main(String[] args){
    	new Main();
    }
    
    /** 
     * Method init adalah method untuk meng-load semua data(resource) yang
     * diperlukan dalam menjalankan game ini seperti gambar, musik, stage, 
     * loading dll.
     */
    public void init(){
    	//Adding some comment
        screen=new ScreenManager("Gatotkaca - Otot Kawat Tulang Besi");
        screen.setFullScreen(true);
    	loading=new LoadingScreen(screen);
    	loading.run();

        resourceManager=new ResourceManager(screen);
        soundManager=resourceManager.getSoundManager();
        
    	stopper=new GameStopper(screen, soundManager);
    }
    
    /**
     * Method Main adalah constructor class ini
     */
    public Main(){
    	//Log message
    	System.out.println("Diciptakan oleh Yohanes Surya Kusuma");
    	System.out.println();
    	System.out.println("Log message:");

    	init();
    	play();
    }
    
    /**
     * Method play adalah method untuk menjalankan game ini.
     */
    public void play(){
    	MainMenu mainMenu=new MainMenu(screen,resourceManager);

    	do{
        	mainMenu=new MainMenu(screen,resourceManager);
        	mainMenu.run();
        	
        	SectionManager sectionManager=
        		new SectionManager(screen,loading,resourceManager);
        	switch(mainMenu.getState()){
        	case MainMenu.STATE_NEW_GAME:
        		sectionManager.newGame();
            	sectionManager.play();
        		break;
        	case MainMenu.STATE_LEVEL_SELECT:
        		//sectionManager.setTutorial(false);
        		sectionManager.play(mainMenu.getLevelChoose());
        		break;
        	}
    	}while(mainMenu.getState()!=MainMenu.STATE_EXIT);
    	
    	stopper.run();
    }
    
    private ScreenManager screen;
    private ResourceManager resourceManager;
    private LoadingScreen loading;
    private SoundManager soundManager;
    private GameStopper stopper;
}
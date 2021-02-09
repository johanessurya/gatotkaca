package util;

import graphics.ScreenManager;
import java.util.ArrayList;
import sound.*;
import tilegame.sprites.JabangTetuka;
import tilemap.ComicScreen;
import tilemap.LoadingScreen;
import tilemap.ResourceManager;
import tilemap.Stage;
import tilemap.TileMap;

/**
 * Class <code>Section</code> menghandel satu episode dalam game ini yang terdiri
 * dari komik pembuka, stage, komik penutup.
 * @author Yohanes Surya
 */
public class Section {
	/**
	 * State WIN
	 */
	public static final int STATE_WIN=0;
	
	/**
	 * State LOSE
	 */
	public static final int STATE_LOSE=1;
	
	/**
	 * State EXIT
	 */
	public static final int STATE_EXIT=2;
	
	/**
	 * Membuat object baru
	 * @param screen <code>ScreenManager</code> yang ditetapkan
	 * @param loading <code>LoadingScreen</code> yang ditetapkan
	 * @param resourceManager <code>ResourceManager</code> yang ditetapkan 
	 * @param soundManager <code>SoundManager</code> yang ditetapkan
	 * @param sectionResource <code>SectionResource</code> yang ditetapkan
	 * @param player <code>JabangTetuka</code> yang ditetapkan
	 * @param comicTutorial <code>true</code> jika tutorial ingin ditampilkan dan 
	 * <code>false</code> jika sebaliknya
	 * @param playTutorial <code>true</code> jika tutorial ingin ditampilkan dan 
	 * <code>false</code> jika sebaliknya
	 */
	public Section(ScreenManager screen,
		    LoadingScreen loading, 
		    ResourceManager resourceManager,
		    SoundManager soundManager,
		    SectionResource sectionResource, 
		    JabangTetuka player,
		    boolean comicTutorial, 
		    boolean playTutorial){
		
		this.screen=screen;
		this.loading=loading;
		this.resourceManager=resourceManager;
		this.sectionResource=sectionResource;
		this.player=player;
		
		this.comicTutorial=comicTutorial;
		this.playTutorial=playTutorial;
		
		this.stage=null;
		this.state=STATE_EXIT;
	}
		
	/**
	 * Run this section
	 */
	public void run(){
		loading.run();
		
		ComicScreen comicScreen=
			new ComicScreen(screen,  resourceManager, 
					sectionResource.getOpening(),
					comicTutorial);
		comicScreen.run();
		
		
		ArrayList<TileMap> maps=sectionResource.getMaps();
		int i=0;
		do{
			stage=new Stage(screen,resourceManager,
					maps.get(i),
					loading,player,
					playTutorial);
			stage.run();
			i++;
			playTutorial=false;
		}while(stage.getState()!=Stage.STATE_LOSE && 
				stage.getState()!=Stage.STATE_EXIT &&
				i<maps.size());
		
		if(stage.getState()==Stage.STATE_WIN){
			comicScreen=
				new ComicScreen(screen, resourceManager, 
						sectionResource.getEnding());
			comicScreen.run();
		}
		
		setState(stage.getState());
	}
	
	private void setState(int stageState){
		switch(stageState){
		case Stage.STATE_WIN:
			state=STATE_WIN;
			break;
		case Stage.STATE_LOSE:
			state=STATE_LOSE;
			break;
		case Stage.STATE_EXIT:
			state=STATE_EXIT;
			break;
		}
	}
	
	/**
	 * Mengambil state dari section ini
	 * @return konstanta "State"
	 */
	public int getState(){
		return state;
	}
				
	private ScreenManager screen;
	private LoadingScreen loading;
	private ResourceManager resourceManager;
	private SectionResource sectionResource;
	private Stage stage;
	private JabangTetuka player;
	
	private int state;
	
	private boolean comicTutorial;
	private boolean playTutorial;
}
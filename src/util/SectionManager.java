package util;

import graphics.ScreenManager;

import java.util.ArrayList;

import sound.SoundManager;
import tilegame.sprites.Gatotkaca;
import tilegame.sprites.JabangTetuka;
import tilemap.CreditScreen;
import tilemap.LoadingScreen;
import tilemap.ResourceManager;
import tilemap.SubmitHighScoreScreen;

/**
 * Class <code>SectionManager</code> menghandel semua <code>SectionResource</code>
 * yang ada di game ini dan menjalankannya secara terurut.
 * @author Yohanes Surya
 */
public class SectionManager {
	/**
	 * Membuat object baru
     * @param screen <code>ScreenManager</code> yang ditetapkan
     * @param loading <code>LoadingScreen</code> yang ditetapkan
     * @param resourceManager <code>ResourceManager</code> yang ditetapkan
	 */
	public SectionManager(ScreenManager screen, LoadingScreen loading,
			ResourceManager resourceManager){
		this.loading=loading;
		this.screen=screen;
		this.resourceManager=resourceManager;
		this.soundManager=resourceManager.getSoundManager();
		this.comicTutorial=false;
		this.player=resourceManager.getJabangTetuka();
		this.growUp=true;
	}
	
	/**
	 * Jika pemain memilih new game <code>newGame()</code> seharusnya di panggil
	 * untuk memerintah class ini untuk memulai permainan baru. Method ini 
	 * berfungsi untuk menampilkan tutorial.
	 */
	public void newGame(){
		this.comicTutorial=true;
		this.playTutorial=true;
	}
	
	/**
	 * Memulai permainan dari level ke-<code>level</code>
	 * @param level level yang ditentukan
	 */
	public void play(int level){
		this.sectionResources=resourceManager.getSectionResources();
		//Play section
		int i=level;
		
		/*
		 * Check apakah masih bisa bertumbuh. 
		 * Jika sudah menjadi Gatotkaca tidak akan dapat berubah lagi.
		 * Apalagi menjadi Jabang Tetuka kembali
		 */
		if(!sectionResources.get(i).isJabangTetuka()){
			growUp=false;
			player=resourceManager.getGatotkaca();
		}
		
		do{
			
			if(!sectionResources.get(i).isJabangTetuka() && growUp){
				Gatotkaca gatotkaca=resourceManager.getGatotkaca();
				player.growUp(gatotkaca);
				player=gatotkaca;
				growUp=false;
			}
			
			section=new Section(screen,loading,resourceManager,
					soundManager,sectionResources.get(i),player,comicTutorial,playTutorial);
			section.run();

			//Tampilkan tutorial comic sekali saja
			if(comicTutorial)
				comicTutorial=false;
			
			//Tampilkan tutorial play jika berubah menjadi gatotkaca saja
			if(i!=sectionResources.size()-1){
				//jika berubah menjadi gatotkaca munculkan tutorial
				if(sectionResources.get(i).isJabangTetuka() && 
						!sectionResources.get(i+1).isJabangTetuka()){
					playTutorial=true;
				}
				else
					playTutorial=false;
			}

			state=section.getState();
			i++;
			
		}while(i<sectionResources.size() && 
				section.getState()==Section.STATE_WIN);
		
		//if player lose or win(END) submit score and save current level;
		if(state==Section.STATE_LOSE || state==Section.STATE_WIN){
			SubmitHighScoreScreen submitHighScore=
				new SubmitHighScoreScreen(screen, player.getScore());
			submitHighScore.run();
			
			Save save=new Save();
			save.write(i-1);
			
			if(state==Section.STATE_WIN){
				CreditScreen credit=new CreditScreen(screen,resourceManager);
				credit.run();
			}
		}
	}
	
	/**
	 * Memulai permainan dari level pertama
	 */
	public void play(){
		play(0);
	}
	
	private ArrayList<SectionResource> sectionResources;
	private Section section;
	private boolean comicTutorial;
	private boolean playTutorial;
	private ScreenManager screen;
	private LoadingScreen loading;
	private SoundManager soundManager;
	private ResourceManager resourceManager;
	private JabangTetuka player;
	private boolean growUp;
	
	//Save for last state(win,lose, or exit)
	private int state;
	
}

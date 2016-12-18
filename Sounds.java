package progettoIGPE.davide.giovanni.unical2016;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sounds {

		public Sounds(){
			
		}
		
		public void startSound() {
				
				File file = new File("sounds/stageStart.wav");
			
				 try {
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(file));
					clip.start();
				  }
				  catch (Exception e) {
				    e.printStackTrace();
				  }
			}

		public void gameOverSound() {
			File start = new File("sounds/gameOver.wav");
		
			 try {
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(start));
				clip.start();
			  }
			  catch (Exception e) {
			    e.printStackTrace();
			  }
		}
		
		public void rocketShot(){
			File file = new File("sounds/bullet_shot.wav");
			
			 try {
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(file));
				clip.start();
			  }
			  catch (Exception e) {
			    e.printStackTrace();
			  }
		}
		
		public void powerUpPick(){
			File file = new File("sounds/powerup_pick.wav");
			
			 try {
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(file));
				clip.start();
			  }
			  catch (Exception e) {
			    e.printStackTrace();
			  }	
		}
		
		public void powerUpAppear(){
			File file = new File("sounds/powerup_appear.wav");
			
			 try {
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(file));
				clip.start();
			  }
			  catch (Exception e) {
			    e.printStackTrace();
			  }	
		}
}

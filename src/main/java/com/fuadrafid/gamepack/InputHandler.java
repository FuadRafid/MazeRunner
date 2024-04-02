package com.fuadrafid.gamepack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class InputHandler implements KeyListener {

	public static class Key
	{
		private int numTimesPressed=0;
		public boolean pressed=false;
		public int TimesPressed()
		{
			return numTimesPressed;
		}
		
		
		public boolean isPressed()
		{
			return pressed;
		}
		public void toggle(boolean isPressed)
		{
			pressed=isPressed;
			if(isPressed)
				numTimesPressed++;
		}
		
	}
	public InputHandler(Game game)
	{
		game.addKeyListener(this);
	}
	
	
	//public List<Key> keys=new ArrayList<Key>();
	
	public Key up= new Key();
	public Key down= new Key();
	public Key left= new Key();
	public Key right= new Key();
	public Key enter= new Key();
	public Key x= new Key();
	public Key z= new Key();
	public Key G= new Key();
	public Key M= new Key();
	public Key Bk= new Key();
	public Key H= new Key();
	public Key P= new Key();
	public Key Sound= new Key();
	public Key Music= new Key();
	public Key VolUp= new Key();
	public Key VolDown= new Key();
	public Key LVolUp= new Key();
	public Key LVolDown= new Key();
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(),true);
		
	}

	
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(),false);
		
	}

	
	public void keyTyped(KeyEvent e) {
		
		
	}
	
	public void toggleKey(int keyCode, boolean isPressed)
	{
		if(keyCode==KeyEvent.VK_UP||keyCode==KeyEvent.VK_W)
		{
			up.toggle(isPressed);
		}
		if(keyCode==KeyEvent.VK_DOWN||keyCode==KeyEvent.VK_S)
		{
			down.toggle(isPressed);
		}
		if(keyCode==KeyEvent.VK_LEFT||keyCode==KeyEvent.VK_A)
		{
			left.toggle(isPressed);
		}
		if(keyCode==KeyEvent.VK_RIGHT||keyCode==KeyEvent.VK_D)
		{
			right.toggle(isPressed);
		}
		if(keyCode==KeyEvent.VK_ENTER)
		{
			
			enter.toggle(isPressed);
			
		}
		if(keyCode==KeyEvent.VK_X)
		{
			x.toggle(isPressed);
			
			
		}
		if(keyCode==KeyEvent.VK_Z)
		{
			z.toggle(isPressed);
			
		}
		if(keyCode==KeyEvent.VK_G)
		{
			G.toggle(isPressed);
			
			
		}
		if(keyCode==KeyEvent.VK_M)
		{
			M.toggle(isPressed);
			
		}
		if(keyCode==KeyEvent.VK_BACK_SPACE)
		{
			Bk.toggle(isPressed);
			
		}
		if(keyCode==KeyEvent.VK_H)
		{
			H.toggle(isPressed);
			
		}
		if(keyCode==KeyEvent.VK_P)
		{
			P.toggle(isPressed);
			
		}
		if(keyCode==KeyEvent.VK_1)
		{
			Sound.toggle(isPressed);
			
		}
		if(keyCode==KeyEvent.VK_2)
		{
			//System.out.println("off");
			Music.toggle(isPressed);
			
		}
		if(keyCode==61 || keyCode==107)
		{
			
			System.out.println("lll");
			VolUp.toggle(isPressed);
			
		}
		if(keyCode==KeyEvent.VK_MINUS || keyCode==109)
		{
			
			VolDown.toggle(isPressed);
			
		}
		if(keyCode==KeyEvent.VK_U)
		{
			
			LVolUp.toggle(isPressed);
			
		}
		if(keyCode==KeyEvent.VK_Y)
		{
			
			LVolDown.toggle(isPressed);
			
		}
		//System.out.println(keyCode);
		
		
		
	}
	 public void disableButtons()
	 {
		 H.pressed=false;
		 P.pressed=false;
 		up.pressed=false;
 		down.pressed=false;
 		left.pressed=false;
 		right.pressed=false;
 		x.pressed=false;
 		z.pressed=false;
 		G.pressed=false;
 		VolUp.pressed=false;
 		VolDown.pressed=false;
 		LVolDown.pressed=false;
		 
	 }
}

package com.fuadrafid.gamepack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener{
   
	boolean isPressed;
	public MouseHandler(Game game)
	{
		game.addMouseListener(this);
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		isPressed=true;
		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {


	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}
	public boolean isPressed()
	{
		return isPressed;
	}
	

}

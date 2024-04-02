package com.fuadrafid.gfx;

public class GameFont {
	private static final String chars= "ABCDEFGHIJKLMNOPQRSTUVWXYZ-+    0123456789.,:;'\"!?$%()-=+/      ";
	
	public static void render(String msg, Screen screen,int x,int y,int colour,int scale)
	{
		msg=msg.toUpperCase();
		
		for(int i=0;i<msg.length();i++)
		{
			int charIndex=chars.indexOf(msg.charAt(i));
			if(charIndex>=0)
				screen.render(x+(i*8*scale),y,charIndex+30*32,colour,0x00,scale);
		}
	}
	public static void render(String msg, Screen screen,int x,int y,int colour,int scale,int mrr)
	{
		msg=msg.toUpperCase();
		
		for(int i=0;i<msg.length();i++)
		{
			int charIndex=chars.indexOf(msg.charAt(i));
			if(charIndex>=0)
				screen.render(x+(i*8),y,charIndex+30*32,colour,mrr,scale);
		}
	}

}

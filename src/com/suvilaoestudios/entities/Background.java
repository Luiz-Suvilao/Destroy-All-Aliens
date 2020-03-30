package com.suvilaoestudios.entities;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Background 
{
	private static  BufferedImage backgound;

	public Background(String path)
	{
		try {
			backgound = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height)
	{
		return backgound.getSubimage(x, y, width, height);
		
	}
}

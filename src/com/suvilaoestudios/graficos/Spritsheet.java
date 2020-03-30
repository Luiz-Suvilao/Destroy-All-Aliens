package com.suvilaoestudios.graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spritsheet 
{
	private static  BufferedImage spritsheet;

		public Spritsheet(String path)
		{
			try {
				spritsheet = ImageIO.read(getClass().getResource(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public BufferedImage getSprite(int x, int y, int width, int height)
		{
			return spritsheet.getSubimage(x, y, width, height);
			
		}
}
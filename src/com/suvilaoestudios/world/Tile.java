package com.suvilaoestudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.suvilaoestudios.main.Game;

public class Tile 
{
	public static final int maskYY = 0;
	public static final int maskWidth = 0;
	public static final int maskHeight = 0;
	public static BufferedImage tile_floor = Game.spritsheet.getSprite(0, 0, 16, 16);
	public static BufferedImage tile_wall = Game.spritsheet.getSprite(16, 0, 16, 16);
	
	public static BufferedImage tile_floor2 = Game.spritsheet.getSprite(0, 48, 16, 16);
	
	private BufferedImage sprite;
	private int x, y;
	
	public Tile(int x, int y, BufferedImage sprite)
	{
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics graphics)
	{
		graphics.drawImage(sprite, x - Camera.cameraX, y - Camera.cameraY, null);
	}
}

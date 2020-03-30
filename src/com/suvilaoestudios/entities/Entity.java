package com.suvilaoestudios.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import com.suvilaoestudios.main.Game;
import com.suvilaoestudios.world.Camera;

public class Entity 
{
	public static BufferedImage lifePack =  Game.spritsheet.getSprite(96, 0, 16, 16);
	public static BufferedImage weapon =  Game.spritsheet.getSprite(112, 0, 16, 16);
	public static BufferedImage ammo =  Game.spritsheet.getSprite(96, 16, 16, 16);
	public static BufferedImage enemy =  Game.spritsheet.getSprite(112, 16, 16, 16);
	public static BufferedImage enemyDamage = Game.spritsheet.getSprite(144, 16, 16, 16);
	public static BufferedImage gunRight = Game.spritsheet.getSprite(128, 0, 16, 16);
	public static BufferedImage gunLeft = Game.spritsheet.getSprite(144, 0, 16, 16);
	public static BufferedImage gunRightDamage = Game.spritsheet.getSprite(0, 32, 16, 16);
	public static BufferedImage gunLeftDamage = Game.spritsheet.getSprite(16, 32, 16, 16);
	public static BufferedImage background = Game.background.getSprite(0, 0, 640, 480);

	public double x;
	public double y;
	protected int z;
	protected int width;
	protected int height;
	
	public int depth;
	
	private BufferedImage sprite;
	
	private int maskXX, maskYY, maskWidth, maskHeight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskXX = 0;
		this.maskYY = 0;
		this.maskWidth = width;
		this.maskHeight = height;
	}
	
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>()
	{
		public int compare(Entity e1, Entity e2) 
		{
			if(e1.depth < e2.depth)
				return -1;
			if(e1.depth > e2.depth)
				return +1;
			return 0;
		}
		
	};
	
	public void setMask(int maskX, int maskY, int maskWidth, int maskHeight)
	{
		this.maskXX = maskX;
		this.maskYY = maskY;
		this.maskWidth = maskWidth;
		this.maskHeight = maskHeight;
	}
	
	public void setX(int newX) 
	{
		this.x = newX;
	}
	
	public void setY(int newY)
	{
		this.y = newY;
	}
	
	public int getX()
	{
		return (int) this.x;
	}
	
	public int getY()
	{
		return (int) this.y;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public void update()
	{
		
	}
	
	public static boolean isColliding(Entity col1, Entity col2)
	{
		Rectangle mask1 = new Rectangle(col1.getX() + col1.maskXX, col1.getY() + col1.maskYY, col1.maskWidth, col1.maskHeight);
		Rectangle mask2 = new Rectangle(col2.getX() + col1.maskXX, col2.getY() + col2.maskYY, col2.maskWidth, col2.maskHeight);
		
		if(mask1.intersects(mask2) && col1.z == col2.z) {
			return true;
		}
		
		return false;
	}
	
	public void render(Graphics graphics)
	{
		graphics.drawImage(sprite, this.getX() - Camera.cameraX, this.getY() - Camera.cameraY, null);
	}

}
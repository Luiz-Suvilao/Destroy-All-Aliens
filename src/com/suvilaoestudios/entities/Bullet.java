package com.suvilaoestudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.suvilaoestudios.main.Game;
import com.suvilaoestudios.world.Camera;
public class Bullet extends Entity
{
	private double directionX;
	private double directionY;
	private double speed = 4;
	
	private int bulletLife = 30, currentLife = 0;
	
	public Bullet(int x, int y, int width, int height, BufferedImage sprite, double directionX, double directionY) 
	{
		super(x, y, width, height, sprite);
		
		this.directionX = directionX;
		this.directionY = directionY;
	}

	public void update()
	{
		x += directionX * speed;
		y += directionY * speed;
		
		currentLife++;
		if(currentLife == bulletLife)
			Game.bullets.remove(this);
		return;
	
	}
	
	public void render(Graphics graphics)
	{
		graphics.setColor(Color.YELLOW);
		graphics.fillOval(this.getX() - Camera.cameraX, this.getY() - Camera.cameraY, width, height);
	}
}

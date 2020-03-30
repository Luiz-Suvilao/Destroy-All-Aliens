package com.suvilaoestudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.suvilaoestudios.main.Game;
import com.suvilaoestudios.world.Camera;

public class Particle extends Entity
{
	public int lifeTime = 15;
	
	public int currentLife = 0;
	
	public int speed = 2;
	
	public double directionX = 0;
	
	public double directionY = 0;

	public Particle(int x, int y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, sprite);
		
		directionX = new Random().nextGaussian();
		directionY = new Random().nextGaussian();
		
	}
	
	public void update() 
	{
		x += directionX * speed;
		y += directionY * speed;
		
		currentLife++;
		if(lifeTime == currentLife) {
			Game.entities.remove(this);
		}
	}
	
	public void render(Graphics graphics) 
	{
		graphics.setColor(Color.RED);
		graphics.fillRect(this.getX() - Camera.cameraX, this.getY() - Camera.cameraY, width, height);
	}
}

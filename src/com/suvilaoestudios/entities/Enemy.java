package com.suvilaoestudios.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.suvilaoestudios.main.Game;
import com.suvilaoestudios.main.Sound;
import com.suvilaoestudios.world.Camera;
import com.suvilaoestudios.world.World;

public class Enemy extends Entity
{
	private double speed = 0.4;
	
	private int maskX = 8, maskY = 8, maskWidth = 10, maskHeight = 10;
	
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;
	
	private BufferedImage[] sprites;
	
	private int life = 20;
	
	private boolean isDamage = false;
	private int framesDamage = 10, currentDamage = 0;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritsheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritsheet.getSprite(128, 16, 16, 16);
	}
	
	public void update()
	{	
		depth = 0;
		
		if(this.isCollidingWithPlayer() == false) {
			if( (int) x < Game.player.getX() && World.isFree( (int) (x + speed), this.getY() ) && 
				!isColliding( (int) (x + speed), this.getY())) {
						x += speed;
			}
			
		else if( (int) x > Game.player.getX() && World.isFree( (int) (x - speed), this.getY() ) && 
				!isColliding( (int) (x - speed), this.getY()) ) {
						x -= speed;
			}
				
		if( (int) y < Game.player.getY() && World.isFree( this.getX(), (int) (y + speed) ) && 
				!isColliding(this.getX(), (int) (y + speed))) {
					y += speed;
			}
		else if( (int) y > Game.player.getY() && World.isFree( this.getX(), (int) (y - speed) ) &&
				!isColliding(this.getX(), (int) (y - speed))) {
					y -= speed;
			}
		
		} else {
			
			if(Game.random.nextInt(100) < 10) {
				Sound.hurt.play();
				Game.player.life -= Game.random.nextInt(3);
				Game.player.isDamage = true;
			}
		}
		
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			
			if(index > maxIndex)
				index = 0;
		}
		
		collidingBullet();
		if(life <= 0) {
			destroySelf();
			return;
		}
		//feedback de dano do inimigo
		if(isDamage) {
			this.currentDamage++;
			if(this.currentDamage == this.framesDamage) {
				this.currentDamage = 0;
				this.isDamage = false;
			}
		}
	}
	
	public void destroySelf()
	{
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet()
	{
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity entity = Game.bullets.get(i);
			if(Entity.isColliding(this, entity)) {
				isDamage = true;
				Sound.hurt_enemy.play();
				life--;
				World.generateParticles(100, (int) x, (int) y);
				Game.bullets.remove(i);
				return;
			}
		}
	}
	
	public boolean isCollidingWithPlayer()
	{
		Rectangle enemyCurrent = new Rectangle(this.getX(), this.getY(), World.tile_size, World.tile_size);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColliding(int xNext, int yNext)
	{
		Rectangle enemyCurrent = new Rectangle(xNext + maskX, yNext + maskY, maskWidth, maskHeight);
		
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy enemy = Game.enemies.get(i);
			if(enemy == this) 
				continue;
			
			Rectangle targetEnemy = new Rectangle(enemy.getX() + maskX, enemy.getY() + maskY, maskWidth, maskHeight);
			
			if(enemyCurrent.intersects(targetEnemy))
				return true;
		}	
		return false;
	}
	
	public void render(Graphics graphics)
	{
		if(!isDamage) {
			graphics.drawImage(sprites[index], this.getX() - Camera.cameraX, this.getY() - Camera.cameraY, null);
		}
		else {
			graphics.drawImage(Entity.enemyDamage, this.getX() - Camera.cameraX, this.getY() - Camera.cameraY, null);
		}
	}
}

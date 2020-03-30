package com.suvilaoestudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.suvilaoestudios.main.Game;
import com.suvilaoestudios.main.Sound;
import com.suvilaoestudios.world.Camera;
import com.suvilaoestudios.world.World;

public class Player extends Entity 
{
	public boolean right, left, up, down;
	public int right_direction = 0, left_direction = 1;
	public int direction;
	public double speed = 1;
	
	private int frames = 0, maxFrames = 14, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerDamage;
	
	public  double life = 100, maxLife = 100;
	
	public static int ammo = 0;
	
	public boolean isDamage = false;
	
	private int damgeFrames = 0;
	
	private boolean hasGun = false;
	
	public boolean shoot = false, mouseShoot = false;
	
	public int mouseX, mouseY;
	
	public boolean jump = false;
	
	public boolean isJumping = false;
	
	public static int axisZ = 0;
	
	public boolean jumpUp = false, jumpDown = false;
	
	public int jumpSpeed = 2;
	
	public int jumpHeight = 35, currentJump = 0;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritsheet.getSprite(0, 16, 16, 16);
		
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritsheet.getSprite(32 + (i * 16), 0, 16, 16);
		}
		
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritsheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
	}
	
	public void update()
	{
		depth = 1;
		
		if(jump) {
			if(isJumping == false) {
				jump = false;
				isJumping = true;
				jumpUp = true;
			}
		}
		
		if(isJumping == true) {
			
				if(jumpUp) {
					currentJump += jumpSpeed;
				}else if(jumpDown) {
					currentJump -= jumpSpeed;
					if(currentJump <= 0) {
						isJumping = false;
						jumpDown = false;
						jumpUp = false;
					}
				}
				
				axisZ = currentJump;
				if(currentJump >= jumpHeight ) {
					jumpUp = false;
					jumpDown = true;
				}
			
		}
		
		moved = false;
		
		if(right && World.isFree( (int) (x + speed), this.getY() ) ) {
			moved = true;
			direction = right_direction;
			x += speed;
		}
		
		else if(left && World.isFree( (int) (x - speed), this.getY() ) ) {
			moved = true;
			direction = left_direction;
			x -= speed;
		}
		
		if(up && World.isFree(this.getX(), (int) (y - speed) )) {
			moved = true;
			y -= speed;
		}
		
		else if(down && World.isFree(this.getX(), (int) (y + speed) )) {
			moved = true;
			y += speed;
		}
		
		if(moved) {
			frames++;
			
			if(frames == maxFrames) {
				frames = 0;
				index++;
				
				if(index > maxIndex)
					index = 0;
			}
		}
		
		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionGun();
		
		if(isDamage) {
			this.damgeFrames++;
			if(this.damgeFrames == 8) {
				this.damgeFrames = 0;
				isDamage = false;
			}
		}
		
		if(shoot) {
			shoot = false;
			if(hasGun && ammo > 0) {
				Sound.shoot.play();
				ammo--;
				int directionX = 0;
				int shootX = 0;
				int shootY = 8;
				if(direction == right_direction) {
					shootX = 20;
					 directionX = 1;
				} else {
					shootX = -9;
					 directionX = -1;
				}
				Bullet bullet = new Bullet(this.getX() + shootX, this.getY() + shootY, 3, 3, null, directionX, 0);
				Game.bullets.add(bullet);
			}
		}
		
		if(mouseShoot) {
			//System.out.println("BANG");
			mouseShoot = false;
			double angle = Math.atan2(mouseY - (this.getY()+8 - Camera.cameraY), mouseX - (this.getX()+8 - Camera.cameraX));
			if(hasGun && ammo > 0) {
				Sound.shoot.play();
				ammo--;
				double directionX = Math.acos(angle);
				double directionY = Math.sin(angle);
				int shootX = 0, shootY = 0;
				if(direction == right_direction) {
					shootX = 20;
					shootY = 8;
					directionX = 1;
				} else {
					shootX = -13;
					shootY = 8;
					directionX = -1;
				}
	
				Bullet bullet = new Bullet(this.getX() + shootX, this.getY() + shootY, 3, 3, null, directionX, directionY);
				Game.bullets.add(bullet);
			}
		}
		
		if(life <= 0) {
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		updateCamera();
	}
	
	public void updateCamera()
	{
		Camera.cameraX = Camera.clamp(this.getX() - (Game.WIDTH / 3), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.cameraY = Camera.clamp(this.getY() - (Game.HEIGHT / 3), 0, (World.HEIGHT * 16) - Game.HEIGHT);
	}
	
	public void checkCollisionGun()
	{
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity currentEntity = Game.entities.get(i);
			if(currentEntity instanceof Weapon) {
				
			if(Entity.isColliding(this, currentEntity)) {
				hasGun = true;
				Sound.collectingItem_sound.play();
				//System.out.println("HasGun");
				Game.entities.remove(currentEntity);
				}
			}
		}
	}
	
	public void checkCollisionAmmo()
	{
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity currentEntity = Game.entities.get(i);
			if(currentEntity instanceof Ammo) {
				
			if(Entity.isColliding(this, currentEntity)) {
				Sound.collectingItem_sound.play();
				ammo += 15;
				if(ammo > 500)
					ammo = 500;
				Game.entities.remove(currentEntity);
				}
			}
		}
	}
	
	public void checkCollisionLifePack()
	{
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity currentEntity = Game.entities.get(i);
			if(currentEntity instanceof LifePack) {
				
			if(Entity.isColliding(this, currentEntity)) {
				Sound.collectingItem_sound.play();
				life += Game.random.nextInt(10);
				if(life > 100)
					life = 100;
				Game.entities.remove(currentEntity);
				}
			}
		}
	}
	
	public void render(Graphics graphics)
	{
		if(!isDamage) {
			if(direction == right_direction) {
				graphics.drawImage(rightPlayer[index], this.getX() - Camera.cameraX, this.getY() - Camera.cameraY - axisZ, null);
				
				if(hasGun) {
					//rigth gun
					graphics.drawImage(Entity.gunRight, this.getX() + 9 - Camera.cameraX, this.getY() + 5 - Camera.cameraY - axisZ, null);
				}
			}
			
			else if(direction == left_direction) {
				graphics.drawImage(leftPlayer[index], this.getX()  - Camera.cameraX, this.getY() - Camera.cameraY - axisZ, null);
				
				if(hasGun) {
					//left gun
					graphics.drawImage(Entity.gunLeft, this.getX() - 10 - Camera.cameraX, this.getY() + 5 - Camera.cameraY - axisZ, null);
				}
			}
			
		} else {
			graphics.drawImage(playerDamage, this.getX() - Camera.cameraX, this.getY() - Camera.cameraY - axisZ, null);
			if(hasGun) {
				if(direction == left_direction) {
					graphics.drawImage(Entity.gunLeftDamage, this.getX() - 10 - Camera.cameraX, this.getY() + 5 - Camera.cameraY - axisZ, null);
				} else {
					graphics.drawImage(Entity.gunRightDamage, this.getX() + 9 - Camera.cameraX, this.getY() + 5 - Camera.cameraY - axisZ, null);
				}
			}
		}
		
		if(isJumping) {
			graphics.setColor(Color.black);
			graphics.fillOval(this.getX() - Camera.cameraX + 8, this.getY() - Camera.cameraY + 16, 8, 8);
		}
	}

}
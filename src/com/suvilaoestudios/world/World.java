package com.suvilaoestudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.suvilaoestudios.entities.*;
import com.suvilaoestudios.graficos.Spritsheet;
import com.suvilaoestudios.main.Game;

public class World 
{
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int tile_size = 16;
	
	public World(String path)
	{
		try {
			
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			
			tiles = new Tile[map.getWidth() * map.getHeight()];
			
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for (int axisX = 0; axisX < map.getWidth(); axisX++) {
				
				for (int axisY = 0; axisY < map.getHeight(); axisY++) {
					
					int current_pixel = pixels[axisX + (axisY * map.getWidth())];
					
					tiles[axisX + (axisY * WIDTH)] = new FloorTile(axisX * 16, axisY * tile_size, Tile.tile_floor);
					
					if (current_pixel ==  0xFFFFD800) {
						//floor
						tiles[axisX + (axisY * WIDTH)] = new FloorTile(axisX * 16, axisY * 16, Tile.tile_floor);
					}
					else if(current_pixel ==  0xFF7F3F5B) {
						//floor2k
						tiles[axisX + (axisY * WIDTH)] = new FloorTile(axisX * 16, axisY * 16, Tile.tile_floor2);
					}
					else if (current_pixel ==  0xFFFFFFFF) {
						//wall
						tiles[axisX + (axisY * WIDTH)] = new WallTile(axisX * 16, axisY * 16, Tile.tile_wall);
					}
					
					else if (current_pixel == 0xFF0A0AFF) {
						//player
						Game.player.setX(axisX * 16);
						Game.player.setY(axisY * 16);
					}
					
					else if (current_pixel == 0xFFFF0000) {
						//enemy
						Enemy enemy = new Enemy(axisX * 16, axisY * 16, 16, 16, Entity.enemy);
						Game.entities.add(enemy);
						Game.enemies.add(enemy);
					}
					else if (current_pixel == 0xFF404040) {
						//weapon
						Game.entities.add(new Weapon(axisX * 16, axisY * 16, 16, 16, Entity.weapon));
					}
					else if (current_pixel == 0xFF7F3300) {
						//ammun
						Game.entities.add(new Ammo(axisX * 16, axisY * 16, 16, 16, Entity.ammo));
					}
					else if (current_pixel == 0xFFFF7F7F) {
						//lifePack
						Game.entities.add(new LifePack(axisX * 16, axisY * 16, 16, 16, Entity.lifePack));
					}
				}
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void generateParticles(int amount, int x, int y) 
	{
		for(int i  = 0; i < amount; i++) {
			Game.entities.add(new Particle(x, y, 1, 1, null));
		}
	}
	
	public static boolean isFree(int xNext, int yNext)
	{
		int xOne = xNext / tile_size;
		int yOne = yNext / tile_size;
		
		int xTwo = (xNext + tile_size - 1) / tile_size;
		int yTwo = yNext / tile_size;
		
		int xTree = xNext / tile_size;
		int yTree = (yNext + tile_size - 1) / tile_size;
		
		int xFour = (xNext + tile_size - 1) / tile_size;
		int yFour = (yNext + tile_size - 1) / tile_size;
		
		 if(!( (tiles[xOne + (yOne * World.WIDTH)] instanceof WallTile) || 
				(tiles[xTwo + (yTwo * World.WIDTH)] instanceof WallTile) || 
				(tiles[xTree + (yTree * World.WIDTH)] instanceof WallTile) || 
				(tiles[xFour + (yFour * World.WIDTH)] instanceof WallTile) )) {
			 return true;
		 }
		 if(Player.axisZ > 0) {
			 return true;
		 }
		 return false;
	}
	
	public static void restartGame(String level)
	{
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritsheet = new Spritsheet("/spritsheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritsheet.getSprite(32, 16, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		Player.ammo = 0;
	}
	
	public void render(Graphics graphics)
	{
		int xStart = Camera.cameraX / 16;
		int yStart = Camera.cameraY / 16;
		
		int xFinal = xStart + (Game.WIDTH / 16);
		
		int yFinal = yStart + (Game.HEIGHT / 16);
		
		for(int axisX = xStart; axisX <= xFinal; axisX++) {
			
			for(int axisY = yStart; axisY <= yFinal; axisY++) {
				
				if(axisX < 0 || axisY < 0 || axisX >= WIDTH || axisY >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[axisX + (axisY * WIDTH)];
				tile.render(graphics);
			}
		}
	}
	
	
}

package com.suvilaoestudios.main;

import java.awt.Canvas;
import java.awt.Color;
//import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
//import java.awt.Point;
//import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.suvilaoestudios.entities.Background;
import com.suvilaoestudios.entities.Bullet;
import com.suvilaoestudios.entities.Enemy;
import com.suvilaoestudios.entities.Entity;
import com.suvilaoestudios.entities.Player;
import com.suvilaoestudios.graficos.Spritsheet;
import com.suvilaoestudios.graficos.UI;
import com.suvilaoestudios.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static  int WIDTH = 160;
	public static int HEIGHT = 120;
	public static  int SCALE = 4;
	
	private int currentLevel  = 1, maxLevel = 2;
	
	private static BufferedImage image;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Bullet> bullets;
	
	public static Spritsheet spritsheet;
	
	public static Background background;
	
	public static Player player;
	
	public static Enemy enemy;
	
	public static World world;
	
	public static Random random;
	
	public UI ui;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
	
	public Font font;
	
	public MenuPrincipal menu;
	
	public int[] pixels;
	
	public static BufferedImage minimap;
	
	public static int[] miniMapPixels;
	
	public static String gameState = "MENU";
	
	private boolean showMessageGameOver = true;
	
	private int framesGameOver = 0;
	
	private boolean restartGame = false;
	
	public static boolean saveGame = false;
	//Sistem de cutscene
	
	public static int entrada = 1;
	
	public static int comecar = 2;
	
	public static int jogando = 3;
	
	public int timeScene = 0, maxTime = 60*3;
	
	public static int sceneState = entrada;
	
	public static void main(String args[])
	{
		Game game = new Game();
		game.start();
		image = new BufferedImage(160, 120, BufferedImage.TYPE_INT_RGB);
	}
	
	public void initFrame() 
	{
		//Janela
		frame = new JFrame("Destroy All Aliens");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		
		//Imagem da janela
		Image imagem = null;
		try {
			imagem = ImageIO.read(getClass().getResource("/icon.png"));
		}catch(IOException e) {e.printStackTrace();}
		//Toolkit toolkit = Toolkit.getDefaultToolkit();
		frame.setIconImage(imagem);
		
		//Cursor do mouse
		//Image image = toolkit.getImage(getClass().getResource("/cursor.png"));
		//Cursor cursor = toolkit.createCustomCursor(image, new Point(0, 0), "img");
		//frame.setCursor(cursor);
	}
	
	public Game()
	{
		ui = new UI();
		random = new Random();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension (WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		
		//Iniciando coisas do mundo e o mundo
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Bullet>();
		
		background = new Background("/background.jpg");
		spritsheet = new Spritsheet("/spritsheet.png");
		player = new Player(0, 0, 16, 16, spritsheet.getSprite(32, 16, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		//minimap = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		//miniMapPixels = ((DataBufferInt)minimap.getRaster().getDataBuffer()).getData();
		
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		menu = new MenuPrincipal();
		
	}
	
	public synchronized void start()
	{
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop()
	{
		isRunning = false;
		
		try {
			thread.join();
		} catch (InterruptedException exception) {
			
			exception.printStackTrace();
			System.out.println(exception);
		}
	}
	
	public void update()
	{
		if(gameState == "NORMAL") {
			//World.renderMiniMap();
			Sound.gameOve_sound.stop();
			Sound.menu_sound.stop();
			Sound.game_sound.loop();
			if(Game.saveGame) {
				Game.saveGame = false;
				String[] opt1 =  {"level", "life", "ammo"};
				int[] opt2 = {this.currentLevel, (int) player.life, (int) Player.ammo};
				MenuPrincipal.saveGame(opt1, opt2, 20);
				System.out.println("GAME SAVE");
			}
			
			this.restartGame = false;
			if(Game.sceneState == Game.jogando) {
				for(int i = 0; i < entities.size(); i++) {
					Entity entitie = entities.get(i);
					entitie.update();
				}
				
				for(int i = 0; i < bullets.size(); i++) {
					bullets.get(i).update();
				}
			} else {
				if(Game.sceneState == Game.entrada) {
					if(player.getX() < 100) {
						player.x++;
					}
					else {
						Game.sceneState = Game.comecar;
					}
				} else if(Game.sceneState == Game.comecar) {
					timeScene++;
					if(timeScene == maxTime)
						Game.sceneState = Game.jogando;
				}
			}
			
			if(enemies.size() == 0) {
				//Avançar level!
				currentLevel++;
				if(currentLevel > maxLevel) {
					currentLevel = 1;
				}
				String newWorld = "level" + currentLevel + ".png";
				World.restartGame(newWorld);
			}
		} else if(gameState == "GAME_OVER") {
			Sound.game_sound.stop();
			Sound.gameOve_sound.loop();
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				
				if(this.showMessageGameOver)
					this.showMessageGameOver = false;
				else
					this.showMessageGameOver = true;
			}
		}
		if(restartGame) {
			this.restartGame = false;
			gameState = "NORMAL";
			currentLevel = 1;
			String newWorld = "level" + currentLevel + ".png";
			World.restartGame(newWorld);
		} else if(gameState == "MENU") {
			Sound.menu_sound.loop();
			menu.update();
		}
	}
	
	public void render()
	{
		
		BufferStrategy buffer = this.getBufferStrategy();
		
		if(buffer == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = image.getGraphics();
		graphics.setColor(new Color(0, 0, 0));
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Graphics2D graphics2 = (Graphics2D) graphics;
		
		world.render(graphics);
		
		Collections.sort(entities, Entity.nodeSorter);
		
		for(int i = 0; i < entities.size(); i++) {
			Entity entitie = entities.get(i);
			entitie.render(graphics);
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(graphics);
		}

		ui.render(graphics);
		
		graphics.dispose();
		graphics = buffer.getDrawGraphics();
		graphics.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		graphics.setFont(new Font("arial",Font.BOLD, 17 ));
		graphics.setColor(Color.white);
		graphics.setFont(font);
		graphics.drawString("Munição: " + Player.ammo, 500, 60);
		
		
		graphics.drawImage(minimap, 536, 375, World.WIDTH * 5, World.HEIGHT * 5 ,null);
		
		if(gameState == "GAME_OVER") {
			Graphics2D graphics2 = (Graphics2D) graphics;
			graphics2.setColor(new Color(0, 0, 0, 100));
			graphics2.fillRect(0, 0, WIDTH * SCALE, WIDTH * SCALE);
			
			graphics.setFont(new Font("arial",Font.BOLD, 30 ));
			graphics.setColor(Color.white);
			graphics.drawString("> Game Over! <", 220, 220);
			
			graphics.setFont(new Font("arial",Font.BOLD, 25 ));
			
			if(showMessageGameOver) {
				graphics.drawString("> Pressione Enter para reiniciar <", 140, 270);
			}
		} else if(gameState == "MENU") {
			Sound.game_sound.stop();
			menu.render(graphics);
		} else if(Game.gameState == "SOBRE") {
				menu.renderSobre(graphics);
			}
		
		if(Game.sceneState == Game.comecar) {
			graphics.setColor(Color.orange);
			graphics.setFont(new Font("arial",Font.BOLD, 25 ));
			graphics.drawString("Bora descer o sarrafo nesses aliens ?", 140, 290);
		}
		
		buffer.show();
	}
	
	public void run() 
	{
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfUpdates = 60.0;
		double nanoSeconds = 1000000000 / amountOfUpdates;
		double delta = 0;
		while(isRunning) {
			
			long now = System.nanoTime();
			delta+= (now - lastTime) / nanoSeconds;
			lastTime = now;
			
			if(delta >=1 ) {
				update();
				render();
				delta--;
			}
		}
		stop();
	}

	//EVENTOS DO TECLADO!
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			
			if(gameState == "MENU") {
				menu.up = true;
			}
			
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X ) {
			player.shoot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			
			if(gameState == "GAME_OVER") {
				this.restartGame = true;
			}
			
			if(gameState == "MENU") {
				menu.enter = true;
				MenuPrincipal.pause = false;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			MenuPrincipal.pause = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_R) {
			if(gameState == "NORMAL")
				Game.saveGame = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
		
	}
	
	//EVENTOS DO MOUSE!
	
	@Override
	public void mousePressed(MouseEvent event) 
	{
		player.mouseShoot = true;
		player.mouseX = (event.getX() / 4);
		player.mouseY = (event.getY() / 4);
	}
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
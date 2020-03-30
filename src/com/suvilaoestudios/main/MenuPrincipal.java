package com.suvilaoestudios.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.suvilaoestudios.entities.Entity;
import com.suvilaoestudios.entities.Player;
import com.suvilaoestudios.world.World;

public class MenuPrincipal 
{
	
	public String[] options = {"New Game", "Load Game", "About", "Exit"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	
	public static boolean pause = false;
	
	public static boolean saveExists = false;
	
	public static boolean saveGame = false;
	
	public void update()
	{
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		} else {
			saveExists = false;
		}
		
		if(up) {
			Sound.menu.play();
			up = false;
			currentOption--;
			if(currentOption < 0)
				currentOption = maxOption;
		}
		if(down) {
			Sound.menu.play();
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if(enter) {
			Sound.menu.play();
			enter = false;
			if(options[currentOption] == "New Game") {
				Game.gameState = "NORMAL";
				pause = false;
				
				file = new File("save.txt");
				file.delete();
			}
			else if(options[currentOption] == "Continue") {
				Game.gameState = "NORMAL";
				pause = false;
			}
			else if(options[currentOption] == "Load Game") {
				file = new File("save.txt");
				
				if(file.exists()) {
					String saver = loadGame(20);
					applySave(saver);
				}
			}
			else if(options[currentOption] == "Exit") {
				System.exit(1);
			}
			else if(options[currentOption] == "About") {
				Game.gameState = "SOBRE";
			}
		}
	}
	
	public static void applySave(String str)
	{
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) {
			case "level":
				World.restartGame("level" + spl2[1] + ".png");
				Game.gameState = "NORMAL";
				pause = false;
			break;
			case "life":
				Game.player.life = Integer.parseInt(spl2[1]);
				break;
			case "ammo":
				Player.ammo = Integer.parseInt(spl2[1]);
				break;
			}
		}
	}
	
	public static String loadGame(int encode) 
	{
		String line = "";
		File file = new File("save.txt");
		
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] transition = singleLine.split(":");
						char[] value = transition[1].toCharArray();
						transition[1] = "";
						for(int i = 0; i < value.length; i++) {
							value[i] -= encode;
							transition[1] += value[i];
						}
						line += transition[0];
						line += ":";
						line += transition[1];
						line += "/";
					}
				}catch(IOException e) {}
			}catch(FileNotFoundException e) {}
		}
		return line;
	}
	
	public static void saveGame(String[] value1, int[] value2, int encode)
	{
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < value1.length; i++) {
			String current = value1[i];
			current += ":";
			char[] value = Integer.toString(value2[i]).toCharArray();
			
			for(int j = 0; j < value.length; j++) {
				value[j] += encode;
				current += value[j];
			}
			
			try {
				writer.write(current);
				if(i < value1.length - 1)
					writer.newLine();
			}catch(IOException e) {}
		}
		
		try {
			writer.flush();
			writer.close();
		}catch(IOException e) {}
	}
	
	public void renderSobre(Graphics graphics) 
	{
		graphics.drawImage(Entity.background, 0, 0, null);
		graphics.setColor(Color.orange);
		graphics.setFont(new Font("arial", Font.BOLD, 30));
		graphics.drawString(" Destroy All Aliens ", 170, 110);
		
		graphics.setColor(Color.white);
		graphics.setFont(new Font("arial", Font.BOLD, 20));
		
		graphics.drawString("Para andar use: WASD ou UP, DOWN, LEFT e RIGHT.", 60, 170);
		graphics.drawString("Para salvar o jogo use: R", 60, 200);
		graphics.drawString("Para atirar use: X ou Esquerdo Do Mouse", 60, 227);
		graphics.drawString("Para pular use: ESPAÇO", 60, 255);
		
		graphics.setFont(new Font("arial", Font.BOLD, 20));
		
		graphics.setColor(Color.orange);
		graphics.drawString("Pressione ESC para voltar", 150, 400);
		graphics.drawString(" Desenvolvido por: Luiz Filipe - 2020 ", 110, 470);

	}
	
	public void render(Graphics graphics)
	{
		//FUNDO TRANSPARENTE!
		//graphics2.setColor(new Color(0, 0, 0, 200));
		//graphics2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		
		//Renderizando fundo com foto
		Graphics2D graphics2 = (Graphics2D) graphics;
		graphics2.drawImage(Entity.background, 0, 0, null);
		//TITULO
		graphics.setColor(Color.orange);
		graphics.setFont(new Font("arial", Font.BOLD, 36));
		graphics.drawString(" Destroy All Aliens ", 140, 110);
		
		//OPÇÕES DO MENU
		graphics.setColor(Color.white);
		graphics.setFont(new Font("arial", Font.BOLD, 26));
		
		if(pause == false)
			graphics.drawString("New Game", 170, 160);
		else
			graphics.drawString("Continue", 170, 160);
		
		graphics.drawString("Load Game", 170, 192);
		graphics.drawString("About", 170, 224);
		graphics.drawString("Exit", 170, 255);
		
		graphics.setColor(Color.orange);
		
		if(options[currentOption] == "New Game") {
			graphics.drawString(">", 150, 160);
		} else if(options[currentOption] == "Load Game") {
			graphics.drawString(">", 150, 192);
		} else if(options[currentOption] == "About") {
			graphics.drawString(">", 150, 225);
		} else if(options[currentOption] == "Exit") {
			graphics.drawString(">", 150, 255);
		}
	}
}

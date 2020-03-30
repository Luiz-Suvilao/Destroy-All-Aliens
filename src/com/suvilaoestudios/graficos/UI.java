package com.suvilaoestudios.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


import com.suvilaoestudios.main.Game;

public class UI 
{
	public void render(Graphics graphics)
	{
		graphics.setColor(Color.red);
		graphics.fillRect(109, 5, 50, 5);
		
		graphics.setColor(Color.green);
		graphics.fillRect(109, 5, (int) ((Game.player.life/ Game.player.maxLife) * 50), 5);
		
		graphics.setFont(new Font("arial", Font.BOLD, 7));
		graphics.setColor(Color.black);
		graphics.drawString( (int) Game.player.life + "/" + (int) Game.player.maxLife, 120, 10);
	}
}

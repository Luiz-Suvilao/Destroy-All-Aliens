package com.suvilaoestudios.world;

public class Camera {

	public static int cameraX = 0;
	public static int cameraY = 0;
	
	public static int clamp(int current,int min,int max){
		if(current <= min){
			current = min;
		}
		
		if(current >= max) {
			current = max;
		}
		
		return current;
	}
	
}

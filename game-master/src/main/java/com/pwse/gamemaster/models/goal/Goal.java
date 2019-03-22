package com.pwse.gamemaster.models.goal;

import com.pwse.gamemaster.models.Colors;

public class Goal {

	private int posX;
	private int posY;
	private boolean isActive;



	public Goal(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		this.isActive = true;
	}

	public void deactivate() {
		isActive = false;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public boolean isActive() {
		return isActive;
	}

	public String getPrintString() {
		return Colors.goal + 'g' + Colors.ANSI_RESET;
	}
}

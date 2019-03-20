package com.pwse.gamemaster.models.player;

import java.util.ArrayList;

public class Team {

	private String color;
	private ArrayList<Player> players;



	public Team(String color) {
		this.color = color;

		this.players = new ArrayList<>(0);
	}

	public String getColor() {
		return color;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public int getNumOfPlayers() {
		return players.size();
	}
}

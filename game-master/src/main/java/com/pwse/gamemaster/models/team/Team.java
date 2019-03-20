package com.pwse.gamemaster.models.team;

import com.pwse.gamemaster.models.player.Player;

import java.util.ArrayList;

public class Team {

	private String color;
	private ArrayList<Player> players;
	private int score;



	public Team(String color) {
		this.color = color;

		this.players = new ArrayList<>(0);
		this.score = 0;
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

	public void addPoint() {
		score++;
	}

	public int getScore() {
		return score;
	}
}

package com.pwse.gamemaster.models;


import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.goal.Goal;

import java.util.ArrayList;

/**
 * This class encapsulates game data (no fckn way)
 */
public class GameData {

	private int numOfPlayers;
	private int initNumOfPieces;
	private double shamProbability;
	private int pieceSpawnFrequency;
	private BoardDimensions boardDimensions;
	private ArrayList<Goal> goals;



	public GameData(
			int numOfPlayers,
			int initNumOfPieces,
			double shamProbability,
			int pieceSpawnFrequency,
			BoardDimensions boardDimensions,
			ArrayList<Goal> goals
	) {
		this.numOfPlayers = numOfPlayers;
		this.initNumOfPieces = initNumOfPieces;
		this.shamProbability = shamProbability;
		this.pieceSpawnFrequency = pieceSpawnFrequency;
		this.boardDimensions = boardDimensions;
		this.goals = goals;
	}

	public int getNumOfPlayers() {
		return numOfPlayers;
	}

	public int getInitNumOfPieces() {
		return initNumOfPieces;
	}

	public double getShamProbability() {
		return shamProbability;
	}

	public int getPieceSpawnFrequency() {
		return pieceSpawnFrequency;
	}

	public BoardDimensions getBoardDimensions() {
		return boardDimensions;
	}

	public ArrayList<Goal> getGoals() {
		return goals;
	}
}

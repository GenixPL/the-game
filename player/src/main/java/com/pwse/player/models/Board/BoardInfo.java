package com.pwse.player.models.Board;


import com.pwse.player.models.BoardDimensions;
import com.pwse.player.models.Position;
import java.util.ArrayList;



public class BoardInfo {

	private ArrayList<Position> knownGoals;
	private ArrayList<Position> knownPieces;
	private BoardDimensions dim;



	public BoardInfo(BoardDimensions boardDimensions) {
		this.dim = boardDimensions;

		knownGoals = new ArrayList<>(0);
		knownPieces = new ArrayList<>(0);
	}

	public void addGoal(Position p) {
		knownGoals.add(p);
	}

	public void removeGoal(Position p) {
		knownGoals.remove(p); //TODO: check if this is correct
	}

	public ArrayList<Position> getKnownGoals() {
		return knownGoals;
	}

	public void addPiece(Position p) {
		knownPieces.add(p);
	}

	public void removePiece(Position p) {
		knownPieces.remove(p); //TODO: check if this is correct
	}

	public ArrayList<Position> getKnownPieces() {
		return knownPieces;
	}

	public void updateInfo(String infoMsg) {
		//TODO: it should take some arguments and change knownGoals/knownPieces after receiving info from other players
	}

	public BoardDimensions getDimensions() {
		return dim;
	}

}

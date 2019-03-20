package com.pwse.gamemaster.controllers.helpers;

import com.pwse.gamemaster.models.piece.Piece;

import java.util.ArrayList;

public class BoardChecker {

	private BoardChecker() { }



	public static boolean isPlaceAvailableForPiece(int posX, int posY, ArrayList<Piece> pieces) {
		for (Piece p : pieces) {
			if (p.getPosX() == posX && p.getPosY() == posY) {
				return false;
			}
		}

		return true;
	}
}

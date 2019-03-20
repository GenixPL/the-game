package com.pwse.gamemaster.view;

import com.pwse.gamemaster.models.Colors;
import com.pwse.gamemaster.models.goal.Goal;
import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.piece.Piece;
import com.pwse.gamemaster.models.player.Player;

import java.util.ArrayList;

public class BoardPrinter {

	private BoardPrinter() { }


	/**
	 * Print Player over Goal
	 * Print Goal over Piece //TODO: or not? (what when player drops sham and leaves)
	 * Print Piece over background
	 */
	public static void print(BoardDimensions dim, ArrayList<Player> players, ArrayList<Piece> pieces, ArrayList<Goal> goals) {
		//make space for readability
		System.out.println();
		System.out.println();

		//print upper line
		printHorizontalLine(dim.getWidth());

		for (int y = 0; y < dim.getHeight(); y++) {
			//print team areas separators
			if (y == (dim.getHeightOfTeamArea()) || y == (dim.getHeight() - dim.getHeightOfTeamArea())) {
				printHorizontalLine(dim.getWidth()); //TODO: it's possible to print vertical lines in those area's in team colors
			}

			//print board itself
			System.out.print("\t|");
			for (int x = 0; x < dim.getWidth(); x++) {
				String toPrint = getBackgroundPrintString();

				Piece pc = getPieceAtPosition(x, y, pieces);
				if (pc != null) {
					toPrint = pc.getPrintSting();
				}

				Goal g = getGoalAtPosition(x, y, goals);
				if (g != null) {
					toPrint = g.getPrintString();
				}

				Player pl = getPlayerAtPosition(x, y, players);
				if (pl != null) {
					toPrint = pl.getPrintString();
				}

				System.out.print(toPrint);
			}
			System.out.print("|\n");
		}

		//print bottom line
		printHorizontalLine(dim.getWidth());
	}



	private static Piece getPieceAtPosition(int posX, int posY, ArrayList<Piece> pieces) {
		for (Piece p: pieces) {
			if (p.getPosX() == posX && p.getPosY() == posY) {
				return p;
			}
		}

		return null;
	}

	private static Goal getGoalAtPosition(int posX, int posY, ArrayList<Goal> goals) {
		for (Goal g: goals) {
			if (g.getPosX() == posX && g.getPosY() == posY) {
				return g;
			}
		}

		return null;
	}

	private static Player getPlayerAtPosition(int posX, int posY, ArrayList<Player> players) {
		for (Player p: players) {
			if (p.getPosX() == posX && p.getPosY() == posY) {
				return p;
			}
		}

		return null;
	}

	private static void printHorizontalLine(int length) {
		System.out.print("\t|");
		for (int x = 0; x < length; x++) {
			System.out.print("-");
		}
		System.out.print("|\n");
	}

	private static String getBackgroundPrintString() {
		return Colors.background + 'b' + Colors.ANSI_RESET;
	}

}

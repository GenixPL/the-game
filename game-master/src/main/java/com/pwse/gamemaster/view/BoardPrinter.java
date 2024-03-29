package com.pwse.gamemaster.view;

import com.pwse.gamemaster.models.Colors;
import com.pwse.gamemaster.models.goal.Goal;
import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.piece.Piece;
import com.pwse.gamemaster.models.player.Player;
import com.pwse.gamemaster.models.team.Team;

import java.util.ArrayList;

public class BoardPrinter {

	private BoardPrinter() { }


	/**
	 * Print Player over Goal
	 * Print Goal over Piece //TODO: or not? (what when player drops sham and leaves)
	 * Print Piece over background
	 */
	public static void print(BoardDimensions dim, ArrayList<Player> players, ArrayList<Piece> pieces, ArrayList<Goal> goals, Team redTeam, Team blueTeam) {
		//make space for readability
		System.out.println();
		System.out.println();

		printScores(redTeam, blueTeam);

		//print upper line
		printHorizontalLine(dim.getWidth(), Colors.redTeam);

		for (int y = 0; y < dim.getHeight(); y++) {
			//print team areas separators
			if (y == (dim.getHeightOfTeamArea())) {
				printHorizontalLine(dim.getWidth(), Colors.redTeam);

			} else if (y == (dim.getHeight() - dim.getHeightOfTeamArea())) {
				printHorizontalLine(dim.getWidth(), Colors.blueTeam);
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
			System.out.print("|" + y + "\n");
		}

		//print bottom line
		printHorizontalLine(dim.getWidth(), Colors.blueTeam);
	}

	private static void printScores(Team red, Team blue) {
		System.out.println("\t" + Colors.redTeam + "Score: " + red.getScore() + Colors.ANSI_RESET);
		System.out.println("\t" + Colors.blueTeam + "Score: " + blue.getScore() + Colors.ANSI_RESET);
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

	private static void printHorizontalLine(int length, String color) {
		System.out.print(color + "\t|");
		for (int x = 0; x < length; x++) {
			System.out.print("-");
		}
		System.out.print("|\n" + Colors.ANSI_RESET);
	}

	private static String getBackgroundPrintString() {
		return Colors.background + 'b' + Colors.ANSI_RESET;
	}

}

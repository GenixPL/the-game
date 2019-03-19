package com.pwse.gamemaster.models;

public class Board {
	private BoardDimensions dim;
	private BoardField[][] fields;



	public Board(BoardDimensions boardDimensions) {
		this.dim = boardDimensions;

		createFields();
		initFields();
	}

	public void print() {
		//make space for readability
		System.out.println();
		System.out.println();

		//print upper line
		printFrame();

		//print board itself
		for (int y = 0; y < dim.getHeight(); y++) {
			System.out.print("\t|");
			for (int x = 0; x < dim.getWidth(); x++) {
				fields[x][y].print();
			}
			System.out.print("|\n");
		}

		//print bottom line
		printFrame();
	}


	private void createFields() {
		fields = new BoardField[dim.getWidth()][dim.getHeight()];

		for (int x = 0; x < dim.getWidth(); x++) {
			for (int y = 0; y < dim.getHeight(); y++) {
				fields[x][y] = new BoardField();
			}
		}
	}

	/**
	 * t - tasks area
	 * g - goal area
	 */
	private void initFields() {
		for (int x = 0; x < dim.getWidth(); x++) {
			for (int y = 0; y < dim.getHeight(); y++) {
				if (y < dim.getHeightOfTeamArea()) {
					//mark red goal area
					fields[x][y].fieldChar = 'g'; //TODO: all of those should be moved to separate functions, not sure which class should be responsible
					fields[x][y].fieldColor = FieldColors.redTeamColor;

				} else if (y >= (dim.getHeight() - dim.getHeightOfTeamArea())) {
					//mark blue goal area
					fields[x][y].fieldChar = 'g';
					fields[x][y].fieldColor = FieldColors.blueTeamColor;

				} else {
					//mark tasks area
					fields[x][y].fieldChar = 't';
					fields[x][y].fieldColor = FieldColors.taskFieldColor;
				}
			}
		}
	}

	private void printFrame() {
		System.out.print("\t|");
		for (int x = 0; x < dim.getWidth(); x++) {
			System.out.print("-");
		}
		System.out.print("|\n");
	}
}

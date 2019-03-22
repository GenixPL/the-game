package com.pwse.player.models.player;


import com.pwse.player.models.Exceptions.WrongTeamNameException;
import com.pwse.player.models.Position;

public class PlayerInfo {

	private Position position;
	private String team;



	public PlayerInfo(Position position, String team) throws WrongTeamNameException {
		this.position = position;
		this.team = team;
	}

	public int getPosX() {
		return position.getX();
	}

	public int getPosY() {
		return position.getY();
	}

	public void setPosX(int posX) {
		position.setX(posX);
	}

	public void setPosY(int posY) {
		position.setY(posY);
	}

	public String getTeam() {
		if (team == null) {
			return "no team";
		}

		return team;
	}


	private  void setTeam(String teamColor) throws WrongTeamNameException {
		if (!teamColor.equals("red") || !teamColor.equals("blue")) { //TODO: is this warning correct?
			throw new WrongTeamNameException();
		}

		team = teamColor;
	}
}

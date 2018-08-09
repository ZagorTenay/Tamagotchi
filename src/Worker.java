import java.util.Random;

import org.w3c.dom.css.ElementCSSInlineStyle;

import enigma.core.Enigma;

public class Worker {
	// Attributes
	public static enigma.console.Console cn = Enigma.getConsole("Tamagotchi");
	private String name;
	static int wage = 30;
	static int counter = 1;
	private int getTurn;
	private boolean isHere;
	private boolean isBusy;
	private boolean isBuying;
	private boolean isFinding;
	private String status;

	// Constructor
	public Worker() {

		if (counter < 10)
			name = "wr0" + counter;
		else
			name = "wr" + counter;
		isHere = false;
		isBuying = false;
		getTurn = 0;
		isFinding=false;
		counter++;
		status="";
	}

	// Methods
	public void check() {
		if (Management.turn == getTurn + 10) {
			stop();
			getTurn = 0;
		}
		if(isBusy && isHere && !isBuying)
			status="- Finding child";
	}
	public void stop() {
		isBusy = false;
		isBuying = false;
		status="";
	}
	public void leave() {
		if (!isHere) {
			Management.clearLogLine();
			cn.getTextWindow().output("Worker already left!");
		}
		else if (isBusy) {
			Management.clearLogLine();
			cn.getTextWindow().output("Worker is busy!");
		}
		else{
			Management.clearLogLine();
			cn.getTextWindow().output(name+" left.");
			isBusy = true;
			isHere = false;
			Management.credit -= 50;
		}
	}
	public void buy(int fq, int gq, int hq) {
		if(isHere){
			if (isBusy) {
				Management.clearLogLine();
				cn.getTextWindow().output("Worker is busy.");
			} else if (Management.credit < (0.2 * gq + 0.2 * hq + 0.2 * fq)) {
				Management.clearLogLine();
				cn.getTextWindow().output("Not enough credit.");
			} else if ((gq < 0 || gq > 100) || (hq < 0 || hq > 100) || (fq < 0 || fq > 100)) {
				Management.clearLogLine();
				cn.getTextWindow().output("Value should be between 0 and 100.");
			} else {
				status = "- Market";
				isBuying = true;
				isBusy = true;
				Storage.food += fq;
				Storage.game += gq;
				Storage.hygiene += hq;
				Management.credit -= (0.2 * fq + 0.2 * gq + 0.2 * hq);
				getTurn = Management.turn;
			}
		}
		else{
			Management.clearLogLine();
			cn.getTextWindow().output("Worker is not here.");
		}
	}


	// Getters and setters
	public boolean isHere() {
		return isHere;
	}
	public void setHere(boolean isHere) {
		this.isHere = isHere;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static int getWage() {
		return wage;
	}
	public static void setWage(int wage) {
		Worker.wage = wage;
	}
	public int getGetTurn() {
		return getTurn;
	}
	public void setGetTurn(int getTurn) {
		this.getTurn = getTurn;
	}
	public boolean isBusy() {
		return isBusy;
	}
	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}
	public boolean isBuying() {
		return isBuying;
	}
	public void setBuying(boolean isBuying) {
		this.isBuying = isBuying;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}

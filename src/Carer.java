import java.util.Random;

import enigma.core.Enigma;

public class Carer {
	public static enigma.console.Console cn = Enigma.getConsole("Tamagotchi");
	// ATTRIBUTES
	private String name;
	private float foodInc;
	private float gameInc;
	private float sleepInc;
	private float hygieneInc;
	private boolean isBusy;
	private boolean isHere;
	private boolean isFeeding;
	private boolean isSleeping;
	private boolean isPlaying;
	private boolean isCleaning;
	private int foodOn;
	private int hygieneOn;
	private int gameOn;
	private int sleepOn;
	private int checkOn;
	private int caringChId;
	static int wage = 50;
	static int carerNum = 0;
	static int counter = 1;

	// CONSTRUCTOR
	public Carer() {
		Random rnd = new Random();

		if (counter < 10)
			name = "cr0" + counter;
		else
			name = "cr" + counter;

		foodInc = (float) (rnd.nextFloat() + 6);
		gameInc = (float) (rnd.nextInt(9) + 3);
		sleepInc = (float) (rnd.nextInt(15) + 5);
		hygieneInc = (float) (rnd.nextInt(24) + 8);
		isBusy = false;
		isHere = false;
		isFeeding = false;
		isCleaning = false;
		isPlaying = false;
		isSleeping = false;
		foodOn = 0;
		hygieneOn = 0;
		gameOn = 0;
		sleepOn = 0;
		caringChId = -1;
		counter++;
	}

	// METHODS
	public void leave() {
		isBusy = true;
		isHere = false;
		Management.credit -= 50;

	}

	public void print() {
		cn.getTextWindow().output(this.name + " - ");
		cn.getTextWindow().output("F: " + this.foodInc);
		cn.getTextWindow().output(" S: " + this.sleepInc);
		cn.getTextWindow().output(" G: " + this.gameInc);
		cn.getTextWindow().output(" H: " + this.hygieneInc);

	}

	public void checkOperations() {
		if (foodOn > 0 || foodOn == -1) {
			feed();
		}
		if (hygieneOn > 0 || hygieneOn == -1)
			clean();
		if (gameOn > 0 || gameOn == -1) {
			play();
		}
		if (checkOn == 2 || checkOn == 1)
			check();
		if (sleepOn > 0) {
			sleep();
		}
	}

	public void feed() {
		isBusy = true;
		if (foodInc < foodOn)
			foodOn -= foodInc;
		else if (foodOn == -1) {
			//
		} else
			foodOn = 0;
		if (foodOn == 0)
			isBusy = false;
		if (Storage.food < foodInc)
			stop();
	}

	public void play() {
		isBusy = true;
		if (gameInc < gameOn)
			gameOn -= gameInc;
		else if (gameOn == -1) {
			//
		} else
			gameOn = 0;
		if (Storage.game < gameInc) {
			stop();
		}
		if (gameOn == 0)
			isBusy = false;
	}

	public void clean() {
		isBusy = true;
		if (hygieneInc < hygieneOn)
			hygieneOn -= hygieneInc;
		else if (hygieneOn == -1) {
			//
		} else
			hygieneOn = 0;
		if (hygieneOn == 0)
			isBusy = false;
		if (Storage.hygiene < hygieneInc) {
			stop();
		}
	}

	public void sleep() {
		isBusy = true;
		if (sleepOn == 0)
			isBusy = false;
	}

	public void check() {
		isBusy = true;
		checkOn -= 1;
		if (checkOn == 0) {
			isBusy = false;
		}
	}

	public void stop() {
		isBusy = false;
		isFeeding = false;
		isCleaning = false;
		isPlaying = false;
		isSleeping = false;
		// caringChId=-1;
	}

	public void terminate() {
		foodOn = 0;
		gameOn = 0;
		hygieneOn = 0;
		sleepOn = 0;
		stop();
	}

	// Getters and setters
	public static int getCarerNum() {
		return carerNum;
	}

	public static void setCarerNum(int carerNum) {
		Carer.carerNum = carerNum;
	}

	public boolean isHere() {
		return isHere;
	}

	public void setHere(boolean isHere) {
		this.isHere = isHere;
	}

	public float getFoodInc() {
		return foodInc;
	}

	public void setFoodInc(float foodInc) {
		this.foodInc = foodInc;
	}

	public float getGameInc() {
		return gameInc;
	}

	public void setGameInc(float gameInc) {
		this.gameInc = gameInc;
	}

	public float getSleepInc() {
		return sleepInc;
	}

	public void setSleepInc(float sleepInc) {
		this.sleepInc = sleepInc;
	}

	public float getHygieneInc() {
		return hygieneInc;
	}

	public void setHygieneInc(float hygieneInc) {
		this.hygieneInc = hygieneInc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public static int getWage() {
		return wage;
	}

	public static void setWage(int wage) {
		Carer.wage = wage;
	}

	public static enigma.console.Console getCn() {
		return cn;
	}

	public static void setCn(enigma.console.Console cn) {
		Carer.cn = cn;
	}

	public boolean isFeeding() {
		return isFeeding;
	}

	public void setFeeding(boolean isFeeding) {
		this.isFeeding = isFeeding;
	}

	public boolean isSleeping() {
		return isSleeping;
	}

	public void setSleeping(boolean isSleeping) {
		this.isSleeping = isSleeping;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public boolean isCleaning() {
		return isCleaning;
	}

	public void setCleaning(boolean isCleaning) {
		this.isCleaning = isCleaning;
	}

	public int getFoodOn() {
		return foodOn;
	}

	public void setFoodOn(int foodOn) {
		this.foodOn = foodOn;
	}

	public int getHygieneOn() {
		return hygieneOn;
	}

	public void setHygieneOn(int hygieneOn) {
		this.hygieneOn = hygieneOn;
	}

	public int getGameOn() {
		return gameOn;
	}

	public void setGameOn(int gameOn) {
		this.gameOn = gameOn;
	}

	public int getCaringChId() {
		return caringChId;
	}

	public void setCaringChId(int caringChId) {
		this.caringChId = caringChId;
	}

	public int getSleepOn() {
		return sleepOn;
	}

	public void setSleepOn(int sleepOn) {
		this.sleepOn = sleepOn;
	}

	public int getCheckOn() {
		return checkOn;
	}

	public void setCheckOn(int checkOn) {
		this.checkOn = checkOn;
	}

}

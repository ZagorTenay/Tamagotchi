import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import enigma.core.Enigma;
public class Child {
	// Attributes
	public static enigma.console.Console cn = Enigma.getConsole("Tamagotchi");
	static int counter = 1;
	private String name;
	private String status;
	private float food;
	private float sleep;
	private float game;
	private float hygiene;
	private float foodDec;
	private float sleepDec;
	private float gameDec;
	private float hygieneDec;
	private float happiness;
	private float foodInc;
	private float hygieneInc;
	private float sleepInc;
	private float gameInc;
	private int missing;
	private boolean isMissing; // kayýp için
	private boolean isHere; // çocuk dahil mi deðil mi
	private boolean happy;
	private boolean isBusy;
	private boolean isEating;
	private boolean isPlaying;
	private boolean isCleaning;
	private boolean isSleeping;
	private boolean workerInterest;
	private int foodOn;
	private int hygieneOn;
	private int gameOn;
	private int checkOn;
	private int workerID;
	private int statusValue;
	private int caredBy;

	//Constructor
	public Child() {
		Random rnd = new Random();

		if (counter < 10)
			name = "ch0" + counter;
		else
			name = "ch" + counter;

		foodDec = (float) ((rnd.nextInt(101) + 50) * 0.01);
		gameDec = (float) ((rnd.nextInt(101) + 50) * 0.01);
		sleepDec = (float) ((rnd.nextInt(51) + 25) * 0.01);
		hygieneDec = (float) ((rnd.nextInt(31) + 20) * 0.01);
		happiness = (float) rnd.nextInt(40) + 25;
		food = (float) rnd.nextInt(26) + 25;
		hygiene = (float) rnd.nextInt(26) + 25;
		game = (float) rnd.nextInt(26) + 25;
		sleep = (float) rnd.nextInt(26) + 25;
		missing = 0;
		happy = true;
		isMissing = false;
		isHere = false;
		isBusy = false;
		isEating = false;
		isPlaying = false;
		isCleaning = false;
		status = "";
		foodOn = 0;
		gameOn = 0;
		hygieneOn = 0;
		counter++;

	}

	//Methods
	public void checkOperations() {
		status = "";
		statusValue = 0;
		if (foodOn > 0 || foodOn == -1)
			eat();
		if (hygieneOn > 0 || hygieneOn == -1)
			clean();
		if (gameOn > 0 || gameOn == -1)
			play();
		if (checkOn > 0) {
			check();
		}
		if (isSleeping)
			sleep();
	

	}
	public void print() {
		//NumberFormat formatter = new DecimalFormat("#0.00");
		if (missing == 0) {
			cn.getTextWindow().output(name + " - ");
			cn.getTextWindow().output("F: " + String.format("%.2f", this.food) + "");
			cn.getTextWindow().output(" S: " + String.format("%.2f", this.sleep) + "");
			cn.getTextWindow().output(" G: " + String.format("%.2f", this.game) + "");
			cn.getTextWindow().output(" H: " + String.format("%.2f", this.hygiene) + "");
			cn.getTextWindow().output(" Hp: " + String.format("%.2f", this.happiness));
		} else if (missing > 0) {
			cn.getTextWindow().output("Child " + (counter-1) + " is missing for " + missing + " turns.");
		}
	}
	public void calculateHappiness() {
		if (food > 75) {
			happiness -= 2 * 0.24;
		} else if (food < 25) {
			happiness -= 4 * 0.24;
		} else {
			happiness += 0.24;
		}

		if (hygiene > 75) {
			happiness -= 2 * 0.06;
		} else if (hygiene < 25) {
			happiness -= 4 * 0.06;
		} else {
			happiness += 0.06;
		}

		if (sleep > 75) {
			happiness -= 2 * 0.08;
		} else if (sleep < 25) {
			happiness -= 4 * 0.08;
		} else {
			happiness += 0.08;
		}

		if (game > 75) {
			happiness -= 2 * 0.12;
		} else if (game < 25) {
			happiness -= 4 * 0.12;
		} else {
			happiness += 0.12;
		}

		if (happiness < 25) {
			happy = false;
		} else
			happy = true;

		if (!isBusy && (!happy && !isMissing)) {
			if (Math.random() < 0.1) {
				isMissing = true;

			}
		}

	}
	public void eat() {
		isBusy = true;
		isEating = true;
		status = "F";
		if (foodInc <= foodOn) {
			this.food += foodInc;
			foodOn -= foodInc;
			Storage.food -= foodInc;
		} else if (foodOn == -1) {
			this.food += foodInc;
			Storage.food -= foodInc;
			if (Storage.food <= foodInc) {
				stop();
			}
		} else {
			this.food += foodOn;
			Storage.food -= foodOn;
			foodOn = 0;
		}
		if (foodOn == 0) {
			isBusy = false;
			isEating = false;
		}

		statusValue = foodOn;
	}
	public void clean() {
		isBusy = true;
		isCleaning = true;
		status = "H";
		if (hygieneInc <= hygieneOn) {
			this.hygiene += hygieneInc;
			hygieneOn -= hygieneInc;
			Storage.hygiene -= hygieneInc;
		} else if (hygieneOn == -1) {
			this.hygiene += hygieneInc;
			Storage.hygiene -= hygieneInc;
			if (Storage.hygiene <= hygieneInc)
				stop();
		} else {
			this.hygiene += hygieneOn;
			Storage.hygiene -= hygieneOn;
			hygieneOn = 0;
		}
		if (hygieneOn == 0) {
			isBusy = false;
			isCleaning = false;
		}
		statusValue = hygieneOn;
	}
	public void sleep() {
		isBusy = true;
		status = "S";
		this.sleep += sleepInc;
		if (!isSleeping) {
			isBusy = false;

		}
	}
	public void play() {
		isBusy = true;
		isPlaying = true;
		status = "G";
		if (gameInc <= gameOn) {
			this.game += gameInc;
			gameOn -= gameInc;
			Storage.game -= gameInc;
		} else if (gameOn == -1) {
			this.game += gameInc;
			Storage.game -= gameInc;
			if (Storage.game <= gameInc)
				stop();
		} else {
			this.game += gameOn;
			Storage.game -= gameOn;
			gameOn = 0;
		}
		if (gameOn == 0) {
			isBusy = false;
			isPlaying = false;
		}
		statusValue = gameOn;
	}
	public void check() {
		isBusy = true;
		status = "C";
		checkOn -= 1;
		if (checkOn == 0) {
			isBusy = false;
		}
	}
	public void decreaseSupplies() {
		this.food -= this.foodDec;
		this.game -= this.gameDec;
		this.sleep -= this.sleepDec;
		this.hygiene -= this.hygieneDec;
	}
	public void stop() {
		isEating = false;
		isCleaning = false;
		isPlaying = false;
		isSleeping = false;
		isBusy = false;
		foodOn = 0;
		hygieneOn = 0;
		gameOn = 0;
	}
	public void leave() {
		if (happiness < 10) {
			isHere = false;
			Management.credit -= 50;

		}
	}
	public void missingInc() {
		if (isMissing == true && workerInterest == false) {
			missing++;
		}
	}
	public void missingDec() {
		if (isMissing == true && workerInterest == true && missing != 0) {
			missing--;
			if (missing == 0) {
				workerInterest = false;
				isMissing = false;
			}

		}

	}
	
	// Setters getters
	public String getName() {
		return name;
	}
	public float getFoodInc() {
		return foodInc;
	}
	public void setFoodInc(float foodInc) {
		this.foodInc = foodInc;
	}
	public boolean isHappy() {
		return happy;
	}
	public void setHappy(boolean happy) {
		this.happy = happy;
	}
	public boolean isBusy() {
		return isBusy;
	}
	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}
	public boolean isEating() {
		return isEating;
	}
	public void setEating(boolean isEating) {
		this.isEating = isEating;
	}
	public int getFoodOn() {
		return foodOn;
	}
	public void setFoodOn(int foodOn) {
		this.foodOn = foodOn;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getHygieneInc() {
		return hygieneInc;
	}
	public void setHygieneInc(float hygieneInc) {
		this.hygieneInc = hygieneInc;
	}
	public float getSleepInc() {
		return sleepInc;
	}
	public void setSleepInc(float sleepInc) {
		this.sleepInc = sleepInc;
	}
	public float getGameInc() {
		return gameInc;
	}
	public void setGameInc(float gameInc) {
		this.gameInc = gameInc;
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
	public float getFood() {
		return food;
	}
	public void setFood(float food) {
		this.food = food;
	}
	public float getSleep() {
		return sleep;
	}
	public void setSleep(float sleep) {
		this.sleep = sleep;
	}
	public float getGame() {
		return game;
	}
	public void setGame(float game) {
		this.game = game;
	}
	public float getHygiene() {
		return hygiene;
	}
	public void setHygiene(float hygiene) {
		this.hygiene = hygiene;
	}
	public float getFoodDec() {
		return foodDec;
	}
	public void setFoodDec(float foodDec) {
		this.foodDec = foodDec;
	}
	public float getSleepDec() {
		return sleepDec;
	}
	public void setSleepDec(float sleepDec) {
		this.sleepDec = sleepDec;
	}
	public float getGameDec() {
		return gameDec;
	}
	public void setGameDec(float gameDec) {
		this.gameDec = gameDec;
	}
	public float getHygieneDec() {
		return hygieneDec;
	}
	public void setHygieneDec(float hygieneDec) {
		this.hygieneDec = hygieneDec;
	}
	public float getHappiness() {
		return happiness;
	}
	public void setHappiness(float happiness) {
		this.happiness = happiness;
	}
	public int getMissing() {
		return missing;
	}
	public void setMissing(int missing) {
		this.missing = missing;
	}
	public boolean isMissing() {
		return isMissing;
	}
	public void setMissing(boolean isMissing) {
		this.isMissing = isMissing;
	}
	public boolean isHere() {
		return isHere;
	}
	public void setHere(boolean isHere) {
		this.isHere = isHere;
	}
	public boolean isSleeping() {
		return isSleeping;
	}
	public void setSleeping(boolean isSleeping) {
		this.isSleeping = isSleeping;
	}
	public boolean isWorkerInterest() {
		return workerInterest;
	}
	public void setWorkerInterest(boolean workerInterest) {
		this.workerInterest = workerInterest;
	}
	public int getWorkerID() {
		return workerID;
	}
	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getStatusValue() {
		return statusValue;
	}
	public void setStatusValue(int statusValue) {
		this.statusValue = statusValue;
	}
	public int getCaredBy() {
		return caredBy;
	}
	public void setCaredBy(int caredBy) {
		this.caredBy = caredBy;
	}
	public int getCheckOn() {
		return checkOn;
	}
	public void setCheckOn(int checkOn) {
		this.checkOn = checkOn;
	}

}
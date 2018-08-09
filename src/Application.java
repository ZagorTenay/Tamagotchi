
public class Application {
	private String name;
	private String objectType;
	private int displayTurn;
	private boolean isAccepted;
	private boolean isDisplay;
	static int counter = 1;

	public Application(String objectType) {
		super();

		if (counter < 10)
			name = "ap0" + counter;
		else
			name = "ap" + counter;

		this.objectType = objectType;
		isAccepted = false;
		isDisplay = true;
		displayTurn = 0;
		counter++;
	}

	public void display() {
		displayTurn++;

		if (displayTurn >= 10) {
			isDisplay = false;
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public boolean isDisplay() {
		return isDisplay;
	}

	public void setDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}

	public static int getCounter() {
		return counter;
	}

	public static void setCounter(int counter) {
		Application.counter = counter;
	}

	public int getDisplayTurn() {
		return displayTurn;
	}

	public void setDisplayTurn(int displayTurn) {
		this.displayTurn = displayTurn;
	}


}

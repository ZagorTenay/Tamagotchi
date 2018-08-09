import java.util.*;
import java.util.zip.Inflater;
import javax.swing.text.AsyncBoxView.ChildState;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import enigma.core.Enigma;
import enigma.core.Enigma;

public class Management {
	// Attributes
	public static enigma.console.Console cn = Enigma.getConsole("Tamagotchi", 400, 300, true);
	private String name;
	private String commandString;
	static int day = 1;
	static int turn = 0;
	static int appcnt = 0;
	static int crcnt = 0;
	static int wrcnt = 0;
	static int chcnt = 0;
	static int y;
	static double score = 0;
	static double credit;
	static double avgHappiness = 0;
	private Child children[];
	private Worker workers[];
	private Carer carers[];
	private Application apps[];
	private int keypr, keyCode, loopCounter;
	static boolean flag1 = false;
	static boolean flag2 = false;
	
	// Constructor
	public Management() throws InterruptedException {
		// setting general variables
		name = "Tamagotchi";
		children = new Child[50];
		workers = new Worker[50];
		carers = new Carer[50];
		apps = new Application[150];
		credit = 200;
		loopCounter = 0;
		keypr = 0;
		keyCode = 0;
		commandString = "";
		KeyListener klis = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (keypr == 0) {
					keypr = 1;
					keyCode = e.getKeyCode();
				}

			}
		};
		cn.getTextWindow().addKeyListener(klis);
		
		// loading screen visualization
		cn.getTextWindow().setCursorPosition(25, 20);
		cn.getTextWindow().output("Tamagotchi is loading...Please wait...");
		Thread.sleep(500);
		cn.getTextWindow().setCursorPosition(3, 5);
		cn.getTextWindow().output("########    ###    ##     ##    ###     ######    #######  ########  ######  ##     ## ####"); 
		cn.getTextWindow().setCursorPosition(3, 6);
		Thread.sleep(500);
		cn.getTextWindow().output("   ##      ## ##   ###   ###   ## ##   ##    ##  ##     ##    ##    ##    ## ##     ##  ##  ");
		cn.getTextWindow().setCursorPosition(3, 7);
		Thread.sleep(500);
		cn.getTextWindow().output("   ##     ##   ##  #### ####  ##   ##  ##        ##     ##    ##    ##       ##     ##  ##  ");
		cn.getTextWindow().setCursorPosition(3, 8);
		Thread.sleep(500);
		cn.getTextWindow().output("   ##    ##     ## ## ### ## ##     ## ##   #### ##     ##    ##    ##       #########  ##  ");
		cn.getTextWindow().setCursorPosition(3, 9);
		Thread.sleep(500);
		cn.getTextWindow().output("   ##    ######### ##     ## ######### ##    ##  ##     ##    ##    ##       ##     ##  ##  ");
		cn.getTextWindow().setCursorPosition(3, 10);
		Thread.sleep(500);
		cn.getTextWindow().output("   ##    ##     ## ##     ## ##     ## ##    ##  ##     ##    ##    ##    ## ##     ##  ##  ");
		cn.getTextWindow().setCursorPosition(3, 11);
		Thread.sleep(500);
		cn.getTextWindow().output("   ##    ##     ## ##     ## ##     ##  ######    #######     ##     ######  ##     ## #### ");
		cn.getTextWindow().setCursorPosition(25, 20);
		cn.getTextWindow().output("Game started! Have fun, and good luck!");
		Thread.sleep(3000);
	
		
		// Game starts
		// initial people
		addChild();
		children[chcnt - 1].setHere(true);
		addCarer();
		carers[crcnt - 1].setHere(true);
		addWorker();
		workers[wrcnt - 1].setHere(true);

		y = 3;
		Design(y);
		printCommand(commandString);
		// game loop
		while (true) {
			// key listener
			if (keypr == 1) {
				switch (keyCode) {
				case 8: // backspace
					if (commandString.length() > 0)
						commandString = commandString.substring(0, commandString.length() - 1);
					break;
				case 10: // enter
					try {
						parseCommand(commandString);
					} catch (Exception e) {
						clearLogLine();
						cn.getTextWindow().output("Can't run this command, please type valid one!");
					}

					commandString = "";
					break;
				case 27: // esc,
					switchDebugMode();
					break;

				default: // any char
					if (commandString.length() < 20) {
						commandString += (char) keyCode;
					}
					break;
				}
				printCommand(commandString);
				keypr = 0;
			}
			Design(y);
			// every turn
			if (loopCounter % 50 == 0) {
				turn++;
				//checking children - missing
				for (int i = 0; i < chcnt; i++) {
					if (children[i].isWorkerInterest() && children[i].isMissing()) {
						children[i].missingDec();

						if (children[i].getMissing() == 0) {
							workers[children[i].getWorkerID() - 1].setBusy(false);
							children[i].setWorkerID(-1);
						}

					} else if (!children[i].isWorkerInterest() && children[i].isMissing()) {
						children[i].missingInc();
					}
				}

				
				int childCounter = 0;
				avgHappiness = 0;
				// decreasing values for every turn & calculating average happiness
				for (int i = 0; i < chcnt; i++) {
					if (children[i].isHere() == true) {
						children[i].decreaseSupplies();
						children[i].calculateHappiness();
						children[i].checkOperations();
						avgHappiness += children[i].getHappiness();
						childCounter++;
					}
				}
				if(childCounter == 0)
					avgHappiness = 0;
				else
					avgHappiness = avgHappiness / childCounter;

				// Checking and printing
				for (int i = 0; i < crcnt; i++) {
					carers[i].checkOperations();
				}
				for (int i = 0; i < wrcnt; i++) {
					workers[i].check();
				}

				for (int i = 0; i < appcnt; i++) {
					apps[i].display();
				}
				
				application();
				
				// end of the day
				if (turn % 50 == 0) {
					
					flag1 = false;
					flag2 = false;
					day++;
					score();
					// check if child is happy or leaving
					for (int i = 0; i < chcnt; i++) {
						credit += children[i].getHappiness() * (1 + ((children[i].getHappiness() - 50) / 50));
						children[i].leave();
					}
					// daily wages
					for (int i = 0; i < crcnt; i++) {
						if (carers[i].isHere())
							credit -= Carer.wage;
					}
					for (int i = 0; i < wrcnt; i++) {
						if (workers[i].isHere())
							credit -= Worker.wage;
					}
					//game over screen
					if (credit < 0 || avgHappiness<20) {
						clearAllConsole();
						cn.getTextWindow().setCursorPosition(10, 10);
						cn.getTextWindow().output(" #####                                                      ###  ");
						cn.getTextWindow().setCursorPosition(10, 11);
						cn.getTextWindow().output("#     #   ##   #    # ######     ####  #    # ###### #####  ### ");
						cn.getTextWindow().setCursorPosition(10, 12);
						cn.getTextWindow().output("#        #  #  ##  ## #         #    # #    # #      #    # ### ");
						cn.getTextWindow().setCursorPosition(10, 13);
						cn.getTextWindow().output("#  #### #    # # ## # #####     #    # #    # #####  #    #  #  ");
						cn.getTextWindow().setCursorPosition(10, 14);
						cn.getTextWindow().output("#     # ###### #    # #         #    # #    # #      #####      ");
						cn.getTextWindow().setCursorPosition(10, 15);
						cn.getTextWindow().output("#     # #    # #    # #         #    #  #  #  #      #   #  ### "); 
						cn.getTextWindow().setCursorPosition(10, 16);
						cn.getTextWindow().output(" #####  #    # #    # ######     ####    ##   ###### #    # ### ");
						cn.getTextWindow().setCursorPosition(0, 25);
						cn.getTextWindow().output("                                   ");
						cn.getTextWindow().setCursorPosition(17, 20);
						cn.getTextWindow().output(">> Your final score is "+score+". << ");
						break;
					}

				}
			}

			loopCounter++;
			Thread.sleep(40);
		} // end while

	}

	// Methods
	public void score() {
		score += children.length * (avgHappiness - 50);
	}
	static public void clearLogLine(){
		cn.getTextWindow().setCursorPosition(0, 26);
		cn.getTextWindow().output("                       		      				                    ");
		cn.getTextWindow().setCursorPosition(0, 26);
	}
	static public void clearAllConsole(){
		for (int i = 0; i < 30; i++) {
			cn.getTextWindow().setCursorPosition(0, i);
			cn.getTextWindow().output("                      						                                                                                     ");
		}
		cn.getTextWindow().setCursorPosition(0, 0);
	}
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}
	
	// commands and screen
	private void printCommand(String commStr) {
		cn.getTextWindow().setCursorPosition(11, 25);
		cn.getTextWindow().output("                                  ");
		cn.getTextWindow().setCursorPosition(11, 25);
		cn.getTextWindow().output(commStr.toLowerCase() + "_");

	}
	private void parseCommand(String commStr) throws InterruptedException {
		int wordsNum = 0;
		commStr = commStr.toLowerCase();
		//splitting command depending on the word count
		for (int i = 0; i < commStr.length(); i++) {
			if (commStr.charAt(i) == ' ') {
				wordsNum++;
			}
		}
		wordsNum++;
		String[] words = new String[wordsNum];
		words = commStr.split(" ");
		boolean isValid = true;
		int id1 = 0;
		int id2 = 0;
		//checking if word length is absurd
		if (words[0].length() < 5) {
			//checking first two letters
			switch (words[0].substring(0, 2)) {
			case "cr": // Carer operations
				if (isInteger(words[0].substring(2, words[0].length()))) {
					//getting thhe ID
					id1 = Integer.parseInt(words[0].substring(2, words[0].length()));
					if (carers[id1 - 1].isHere()) {
						switch (words[1].charAt(0)) {
						//checking operation
						case 'f':// feed
							if (words[2].substring(0, 2).equals("ch")) {
								if (isInteger(words[2].substring(2, words[2].length()))) {
									id2 = Integer.parseInt(words[2].substring(2, words[2].length()));
									if (words.length == 3) {
										// feed until stopped
										feed(-1, id1, id2);

									} else {
										int foodNum = Integer.parseInt(words[3]);
										feed(foodNum, id1, id2);

									}

								} else
									isValid = false;
							} else
								isValid = false;

							break;
						case 's':// sleep
							if (words[2].substring(0, 2).equals("ch")) {
								if (isInteger(words[2].substring(2, words[2].length()))) {
									id2 = Integer.parseInt(words[2].substring(2, words[2].length()));
									if (words.length == 3) {
										// sleep until stopped
										sleep(id1, id2);

									}
								} else
									isValid = false;
							} else
								isValid = false;
							break;
						case 'g':// game
							if (words[2].substring(0, 2).equals("ch")) {
								if (isInteger(words[2].substring(2, words[2].length()))) {
									id2 = Integer.parseInt(words[2].substring(2, words[2].length()));
									if (words.length == 3) {
										// game until stopped
										play(-1, id1, id2);

									} else {
										int gameNum = Integer.parseInt(words[3]);
										play(gameNum, id1, id2);

									}

								} else
									isValid = false;
							} else
								isValid = false;
							break;
						case 'h':// hygiene
							if (words[2].substring(0, 2).equals("ch")) {
								if (isInteger(words[2].substring(2, words[2].length()))) {
									id2 = Integer.parseInt(words[2].substring(2, words[2].length()));
									if (words.length == 3) {
										// hyg until stopped
										clean(-1, id1, id2);

									} else {
										int hygieneNum = Integer.parseInt(words[3]);
										clean(hygieneNum, id1, id2);
									}

								} else
									isValid = false;
							} else
								isValid = false;
							break;
						case 'c':// check
							if (words[2].substring(0, 2).equals("ch")) {
								if (isInteger(words[2].substring(2, words[2].length()))) {
									id2 = Integer.parseInt(words[2].substring(2, words[2].length()));
									if (chcnt > id2 - 1 && children[id2 - 1].isHere()) {
										check(id1, id2);
									} else
										isValid = false;
								} else
									isValid = false;
							} else
								isValid = false;
							break;
						case 'r':// stop
							terminate(id1);
							break;
						case 't':// leave
							carers[id1 - 1].leave();
							break;
						default:// wrong command
							isValid = false;
							break;
						}
					} else {
						clearLogLine();
						cn.getTextWindow().output("Carer is not here.");
					}

				} else { // cr## is not an integer
					isValid = false;
				}
				break;
			case "wr": // Worker operations
				if (isInteger(words[0].substring(2, words[0].length()))) {
					id1 = Integer.parseInt(words[0].substring(2, words[0].length()));

					switch (words[1].charAt(0)) {
					case 'm': // market fgh
						if (words.length == 5) {
							int fq = 0;
							int gq = 0;
							int hq = 0;
							if (words[2].substring(0, 1).equals("f")
									&& isInteger(words[2].substring(1, words[2].length()))) {
								fq = Integer.parseInt(words[2].substring(1, words[2].length()));

							}
							if (words[3].substring(0, 1).equals("g")
									&& isInteger(words[3].substring(1, words[3].length()))) {
								gq = Integer.parseInt(words[3].substring(1, words[3].length()));

							}
							if (words[4].substring(0, 1).equals("h")
									&& isInteger(words[4].substring(1, words[4].length()))) {
								hq = Integer.parseInt(words[4].substring(1, words[4].length()));
							}
							workers[id1 - 1].buy(fq, gq, hq);

						} else {
							clearLogLine();
							cn.getTextWindow().output("Please enter the command like: wr1 m f30 g0 h50");
						}
						break;

					case 'c':// finding
						if (words[2].substring(0, 2).equals("ch")) {
							if (isInteger(words[2].substring(2, words[2].length()))) {
								id2 = Integer.parseInt(words[2].substring(2, words[2].length()));

								if (children[id2 - 1].isMissing() == false) {
									clearLogLine();
									cn.getTextWindow().output("This child is not missing!");
								} else if (workers[id1 - 1].isBusy()) {
									clearLogLine();
									cn.getTextWindow().output("Worker is busy now!");
								} else if (children[id2 - 1].isWorkerInterest()) {
									clearLogLine();
									cn.getTextWindow().output("This child is already searching from another worker now!");
								} else if (workers[id1 - 1].isBusy() == false && children[id2 - 1].isMissing() == true
										&& children[id2 - 1].isWorkerInterest() == false) {
									workers[id1 - 1].setBusy(true);
									children[id2 - 1].setWorkerInterest(true);
									children[id2 - 1].setWorkerID(id1);
								}

							} else
								isValid = false;
						} else
							isValid = false;
						break;
					case 't': // leave
						workers[id1 - 1].leave();
						break;

					default: // wrong command
						isValid = false;
						break;
					}
				} else {
					isValid = false;
				}
				break;
			case "ap": // Applications
				int appNum = Integer.parseInt(words[0].substring(2, words[0].length()));

				if (apps[appNum - 1].isAccepted() == true || apps[appNum - 1].isDisplay() == false) {
					clearLogLine();
					cn.getTextWindow().output("Invalid command or the command can not be applied.");
				}

				apps[appNum - 1].setAccepted(true);
				apps[appNum - 1].setDisplay(false);
				accept(apps[appNum - 1].getObjectType());

				break;
			default: // Wrong command
				isValid = false;
				break;

			}

		} else
			isValid = false;

		if (!isValid) {
			clearLogLine();
			cn.getTextWindow().output("Invalid command or the command can not be applied.");

		}
		if (commStr.equalsIgnoreCase("cls")) {
			clearLogLine();
		}
	}
	void Design(int y) {
		for (int i = 0; i < 24; i++) {
			cn.getTextWindow().setCursorPosition(0, i);
			cn.getTextWindow().output(
					"                                                                                                                           ");
		}

		cn.getTextWindow().setCursorPosition(0, 0);
		cn.getTextWindow().output("Day: " + day + " ");

		cn.getTextWindow().setCursorPosition(9, 0);
		cn.getTextWindow().output("Turn: " + ((turn % 50) + 1) + " ");

		cn.getTextWindow().setCursorPosition(19, 0);
		NumberFormat formatter = new DecimalFormat("#0.00");
		cn.getTextWindow().output("Current Avg. Happiness: " + formatter.format(avgHappiness));

		cn.getTextWindow().setCursorPosition(50, 0);
		cn.getTextWindow().output("Credit: " + formatter.format(credit));

		cn.getTextWindow().setCursorPosition(67, 0);
		cn.getTextWindow().output("Score: " + formatter.format(score));

		cn.getTextWindow().setCursorPosition(0, 2);
		cn.getTextWindow().output("---Applications---");

		int cnt = 1;

		for (int i = 0; i < appcnt; i++) {

			if (apps[i].isAccepted() == false && apps[i].isDisplay() == true && cnt % 2 == 1) {
				cn.getTextWindow().setCursorPosition(0, 3);
				
				if (apps[i].getObjectType().substring(0, 2).equalsIgnoreCase("ch")) {
					
					int id = Integer.parseInt(apps[i].getObjectType().substring(2, apps[i].getObjectType().length())) - 1;
					
					cn.getTextWindow().output(i + 1 + "- " + "Child   F: " + children[id].getFood() + " G: " + children[id].getGame()
							+ " H: " + children[id].getHygiene() + " S: " + children[id].getSleep());
				} else if (apps[i].getObjectType().substring(0, 2).equalsIgnoreCase("cr")) {
					cn.getTextWindow().output(i + 1 + "- " + "Carer");
				} else if (apps[i].getObjectType().substring(0, 2).equalsIgnoreCase("wr")) {
					cn.getTextWindow().output(i + 1 + "- " + "Worker");
				}

				cnt++;
			} else if (apps[i].isAccepted() == false && apps[i].isDisplay() == true && cnt % 2 == 0) {
				cn.getTextWindow().setCursorPosition(0, 4);

				if (apps[i].getObjectType().substring(0, 2).equalsIgnoreCase("ch")) {

					int id = Integer.parseInt(apps[i].getObjectType().substring(2, apps[i].getObjectType().length())) - 1;

					cn.getTextWindow().output(i + 1 + "- " + "Child   F: " + children[id].getFood() + " G: " + children[id].getGame()
							+ " H: " + children[id].getHygiene() + " S: " + children[id].getSleep());
				} else if (apps[i].getObjectType().substring(0, 2).equalsIgnoreCase("cr")) {
					cn.getTextWindow().output(i + 1 + "- " + "Carer");
				} else if (apps[i].getObjectType().substring(0, 2).equalsIgnoreCase("wr")) {
					cn.getTextWindow().output(i + 1 + "- " + "Worker");
				}

				cnt++;
			}

		}

		y++;
		cn.getTextWindow().setCursorPosition(0, y + 2);
		cn.getTextWindow().output("---Children---");
		for (int i = 0; i < chcnt; i++) {
			if (i % 4 == 0) {
				y += 3;
			}
			cn.getTextWindow().setCursorPosition(13 * (i % 4), y + 1);
			if (children[i].isHere()) {
				if (children[i].isBusy() == false) {
					cn.getTextWindow().output(children[i].getName() + "        ");
					cn.getTextWindow().setCursorPosition(13 * (i % 4), y + 2);
					cn.getTextWindow().output("               ");
				} else {
					cn.getTextWindow().output(children[i].getName() + " (" + children[i].getStatus() + ":"
							+ (children[i].getStatusValue()==-1 ? "∞":children[i].getStatusValue()) + ")");
					cn.getTextWindow().setCursorPosition(13 * (i % 4), y + 2);
					cn.getTextWindow().output("(" + carers[children[i].getCaredBy()].getName() + ") ");
					// Kimin çocukla ne için ilgilendiği (ex: cr02 F: 23)
				}
			} else {
				if (i % 4 == 1) {
					y -= 3;
				}
			}
				
		}
		y++;

		cn.getTextWindow().setCursorPosition(0, y + 4);
		cn.getTextWindow().output("---Carers---");
		for (int i = 0; i < crcnt; i++) {
			y++;
			if (carers[i].isHere()) {
				cn.getTextWindow().setCursorPosition(0, y + 4);
				if (carers[i].isBusy() == false) {
					cn.getTextWindow().output("Carer " + carers[i].getName().substring(2, carers[i].getName().length())
							+ "                                 ");
				} else {
					cn.getTextWindow()
							.output("Carer " + carers[i].getName().substring(2, carers[i].getName().length()) + "  ");
					cn.getTextWindow()
							.output("(" + children[carers[i].getCaringChId()].getStatus() + ": "
									+ (children[carers[i].getCaringChId()].getStatusValue()==-1 ? "∞":children[i].getStatusValue()) + " "
									+ children[carers[i].getCaringChId()].getName() + ")");
					// buraya carerın o an ne ile meşgul oluduğu gelecek
					// Ex: Carer 02 (Child 05 Food: 23) - "childın değerleri"(F:
					// G: S: H:)
				}
			} else
				y--;
		}

		y++;

		for (int k = 5; k < 10; k++) {
			int tempY = y;
			cn.getTextWindow().setCursorPosition(0, tempY + k);
			cn.getTextWindow().output("                             ");
		}

		cn.getTextWindow().setCursorPosition(0, y + 5);

		cn.getTextWindow().output("---Workers---");
		for (int i = 0; i < wrcnt; i++) {
			y++;
			;
			cn.getTextWindow().setCursorPosition(0, y + 5);
			if (workers[i].isHere()) {
				if (workers[i].isBusy() == false) {
					cn.getTextWindow().output("Worker " + workers[i].getName().substring(2, workers[i].getName().length())+"                       ");
				} else {
					cn.getTextWindow().output("Worker " + workers[i].getName().substring(2, workers[i].getName().length()) + " "  + workers[i].getStatus());
					// workerin o an ne ile meşgul oluduğu gelecek
					// Ex: Worker 01 (Market - Food: 100)
				}
			} else
				y--;
		}

		cn.getTextWindow().setCursorPosition(63, 2);
		cn.getTextWindow().output("---Supplies---");
		cn.getTextWindow().setCursorPosition(63, 3);
		cn.getTextWindow().output("Food: " + Storage.food);
		cn.getTextWindow().setCursorPosition(63, 4);
		cn.getTextWindow().output("Toy: " + Storage.game);
		cn.getTextWindow().setCursorPosition(63, 5);
		cn.getTextWindow().output("Hygiene: " + Storage.hygiene);

		y = 7;

		for (int j = 7; j < 25; j++) {
			cn.getTextWindow().setCursorPosition(63, y);
			y++;
			cn.getTextWindow().output("                                ");
		}
		y = 7;
		for (int i = 0; i < chcnt; i++) {
			cn.getTextWindow().setCursorPosition(63, y);
			y++;
			if (children[i].isBusy() == true) { // child classının içindeki
												// print fonksiyonu gibi
												// değerlerin azalıp artması
												// lazım
				cn.getTextWindow().output("Child" + children[i].getName().substring(2, carers[i].getName().length()));
				if (children[i].isMissing() == true) {
					cn.getTextWindow().setCursorPosition(63, y);
					cn.getTextWindow().output("Missing: " + formatter.format(children[i].getMissing()));
				} else {
					cn.getTextWindow().setCursorPosition(63, y);
					cn.getTextWindow().output("Happines: " + formatter.format(children[i].getHappiness()));
					y++;
					cn.getTextWindow().setCursorPosition(63, y);
					cn.getTextWindow().output("F: " + formatter.format(children[i].getFood()) + "  " + "G: "
							+ formatter.format(children[i].getGame()));
					y++;
					cn.getTextWindow().setCursorPosition(63, y);
					cn.getTextWindow().output("S: " + formatter.format(children[i].getSleep()) + "  " + "H: "
							+ formatter.format(children[i].getHygiene()));
					y++;
					cn.getTextWindow().setCursorPosition(63, y);
					cn.getTextWindow().output("Last checked on turn " + Management.turn);
					y++;
				}
			}
			y++;
		}

		cn.getTextWindow().setCursorPosition(0, 25);
		cn.getTextWindow().output("Command > ");
		cn.getTextWindow().setCursorPosition(0, 28);
		cn.getTextWindow().output("You can clear system messages with \"cls\" command.");
	}

	// applications
	public void addChild() {
		children[chcnt] = new Child();
		chcnt++;
	}
	public void addCarer() {
		carers[crcnt] = new Carer();
		crcnt++;
	}
	public void addWorker() {
		workers[wrcnt] = new Worker();
		wrcnt++;
	}
	public void application() {
		if (avgHappiness > 65 && Math.random() < 0.4 && flag1 == false) {
			addChild();
			apps[appcnt] = new Application(children[chcnt - 1].getName());
			appcnt++;
			flag1 = true;
		}
		if (Math.random() < 0.2 && flag2 == false) {
			addCarer();
			apps[appcnt] = new Application(carers[crcnt - 1].getName());
			appcnt++;
			flag2 = true;
		} else if (Math.random() < 0.2 && flag2 == false) {
			addWorker();
			apps[appcnt] = new Application(workers[wrcnt - 1].getName());
			appcnt++;
			flag2 = true;
		}

	}
	public void accept(String str) {
		int id = Integer.parseInt(str.substring(2, str.length())) - 1;
		if (str.substring(0, 2).equalsIgnoreCase("ch")) {
			children[id].setHere(true);
		} else if (str.substring(0, 2).equalsIgnoreCase("cr")) {
			carers[id].setHere(true);
		} else if (str.substring(0, 2).equalsIgnoreCase("wr")) {
			workers[id].setHere(true);
		}
	}

	// carer operations
	public void feed(int f, int cr, int ch) {
		if (carers[cr - 1].isBusy()) {
			clearLogLine();
			cn.getTextWindow().output("Carer is busy");
		} else if(!carers[cr-1].isHere()){
			clearLogLine();
			cn.getTextWindow().output("Carer is not here");
	    } else if (children[ch - 1].isBusy()) {
			clearLogLine();
			cn.getTextWindow().output("Child is already being cared");
		} else if (f > Storage.food) {
			clearLogLine();
			cn.getTextWindow().output("Not enough food in storage");
		} else if (children[ch - 1].isMissing() == true || children[ch - 1].isHere() == false) {
			clearLogLine();
			cn.getTextWindow().output("This child has gone or has not been accepted to Care Center");
		} else {
			carers[cr - 1].setFoodOn(f);
			carers[cr - 1].setCaringChId(ch - 1);
			children[ch - 1].setCaredBy(cr - 1);
			children[ch - 1].setFoodInc(carers[cr - 1].getFoodInc());
			children[ch - 1].setFoodOn(f);
			clearLogLine();
			cn.getTextWindow().output("Carer "+cr+" started to feed Child "+ch+", the value of " + (f==-1 ? "∞.":f+"."));
		}
	}
	public void sleep(int cr, int ch) {
		if (carers[cr - 1].isBusy()) {
			clearLogLine();
			cn.getTextWindow().output("Carer is busy");
		} else if (children[ch - 1].isBusy()) {
			clearLogLine();
			cn.getTextWindow().output("Child is already being cared");
		} else if (children[ch - 1].isMissing() == true || children[ch - 1].isHere() == false) {
			clearLogLine();
			cn.getTextWindow().output("This child has gone or has not been accepted to Care Center");
		} else {
			carers[cr - 1].setCaringChId(ch - 1);
			children[ch - 1].setCaredBy(cr - 1);
			children[ch - 1].setSleepInc(carers[cr - 1].getSleepInc());
			children[ch - 1].setSleeping(true);
			carers[cr - 1].setSleepOn(1);
			clearLogLine();
			cn.getTextWindow().output("Carer "+cr+" started to sleep Child "+ch+".");
		}
	}
	public void play(int g, int cr, int ch) {
		if (carers[cr - 1].isBusy()) {
			clearLogLine();
			cn.getTextWindow().output("Carer is busy");
		} else if(!carers[cr-1].isHere()){
			clearLogLine();
			cn.getTextWindow().output("Carer is not here");
	    }else if (children[ch - 1].isBusy()) {
			clearLogLine();
			cn.getTextWindow().output("Child is already being cared");
		} else if (g > Storage.game) {
			clearLogLine();
			cn.getTextWindow().output("Not enough toys in storage");
		} else if (children[ch - 1].isMissing() == true || children[ch - 1].isHere() == false) {
			clearLogLine();
			cn.getTextWindow().output("This child has gone or has not been accepted to Care Center");
		} else {
			children[ch - 1].setCaredBy(cr - 1);
			carers[cr - 1].setCaringChId(ch - 1);
			carers[cr - 1].setGameOn(g);
			children[ch - 1].setGameInc(carers[cr - 1].getGameInc());
			children[ch - 1].setGameOn(g);
			clearLogLine();
			cn.getTextWindow().output("Carer "+cr+" started to play games with Child "+ch+", the value of " + (g==-1 ? "∞.":g+"."));
		}
	}
	public void clean(int h, int cr, int ch) {
		if (carers[cr - 1].isBusy()) {
			cn.getTextWindow().setCursorPosition(0, 26);
			cn.getTextWindow().output("Carer is busy");
		} else if (children[ch - 1].isBusy()) {
			cn.getTextWindow().setCursorPosition(0, 26);
			cn.getTextWindow().output("Child is already being cared");
		} else if (h > Storage.hygiene) {
			cn.getTextWindow().setCursorPosition(0, 26);
			cn.getTextWindow().output("Not enough cleaning stuff in storage");
		} else if (children[ch - 1].isMissing() == true || children[ch - 1].isHere() == false) {
			cn.getTextWindow().setCursorPosition(0, 26);
			cn.getTextWindow().output("This child has gone or has not been accepted to Care Center");
		} else {
			carers[cr - 1].setHygieneOn(h);
			children[ch - 1].setHygieneInc(carers[cr - 1].getHygieneInc());
			children[ch - 1].setHygieneOn(h);
			carers[cr - 1].setCaringChId(ch - 1);
			children[ch - 1].setCaredBy(cr - 1);
			clearLogLine();
			cn.getTextWindow().output("Carer "+cr+" started to clean Child "+ch+" for the value of " + (h==-1 ? "∞.":h+"."));
		}
	}
	public void check(int cr, int ch) {
		if (carers[cr - 1].isBusy()) {
			clearLogLine();
			cn.getTextWindow().output("Carer is busy");
		} else if(!carers[cr-1].isHere()){
			clearLogLine();
			cn.getTextWindow().output("Carer is not here");
	    } else if (children[ch - 1].isBusy()) {
			clearLogLine();
			cn.getTextWindow().output("Child is already being cared");
		} else {
			carers[cr - 1].setCaringChId(ch - 1);
			children[ch - 1].setCaredBy(cr - 1);
			carers[cr - 1].setCheckOn(2);
			children[ch - 1].setCheckOn(2);
			cn.getTextWindow().output("Carer "+cr+" checked Child "+ch+".");
		}
	}
	public void switchDebugMode() throws InterruptedException {
		while (true) {
			clearAllConsole();
			cn.getTextWindow().output("Debug Mode: \n \n");
			int lastY=0;
			//cn.getTextWindow().output(\n);
			for (int i = 0; i < chcnt; i++) {
				if (children[i].isHere()) {
					children[i].print();
					System.out.println("\n");
					lastY=i;
				}
			}

			for (int i = 0; i < chcnt+chcnt%3; i++) {
				cn.getTextWindow().setCursorPosition(0, (lastY+2)*2);
				cn.getTextWindow().output("Returning to the game in " + (chcnt-i) + " seconds...");
				Thread.sleep(1000);
			}
			clearAllConsole();
			break;
			// if esc pressed, break
		}
	}
	public void terminate(int crId) {
		children[carers[crId - 1].getCaringChId()].stop();
		carers[crId - 1].terminate();
		clearLogLine();
		cn.getTextWindow().output("Carer "+crId+" stopped, now available for tasks!");
	}


	// getset
	public Child[] getChilds() {
		return children;
	}
	public void setChilds(Child[] children) {
		this.children = children;
	}
	public Worker[] getWorkers() {
		return workers;
	}
	public void setWorkers(Worker[] workers) {
		this.workers = workers;
	}
	public Carer[] getCarers() {
		return carers;
	}
	public void setCarers(Carer[] carers) {
		this.carers = carers;
	}
}

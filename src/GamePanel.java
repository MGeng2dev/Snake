import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; // units of the game board
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75; // delay for timer
	
	// Arrays to hold coordinates for snake's body parts
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	int bodyParts = 6; // start with 6 body parts
	int applesEaten; // initially 0
	int appleX; // x-coord. of apple location (random each time)
	int appleY; 
	char direction = 'R';  // R, L, U, D
	boolean running = false;
	Timer timer;
	Random random; 
	
	GamePanel() {
		// Construct, this call startGame() method
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		
		startGame();
	}
	
	/** Method to start game */
	public void startGame() {
		newApple();  // create new apple on the screen
		running = true; 
		timer = new Timer(DELAY, this);
		timer.start(); // call timer start function
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if (running) {
			/** Make a gid to help see game board during development
			for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0,  i*UNIT_SIZE,  SCREEN_WIDTH,  i * UNIT_SIZE);
			}
			*/
			
			// draw apple
			g.setColor(Color.red);
			g.fillOval(appleX,  appleY,  UNIT_SIZE,  UNIT_SIZE);
			
			// draw snake
			for(int i = 0; i < bodyParts; i++) {
				if (i == 0) {  // snake head
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {  // snake body
					g.setColor(new Color(45, 180, 0));
					//g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));  // make snake random colors
					g.fillRect(x[i],  y[i],  UNIT_SIZE,  UNIT_SIZE);
				}
			}
			// draw current score
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			// instance of font metrics to center text
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
	}
	
	public void newApple() {
		// generate coordinates of new apple when game begins or apple is eaten
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
	}
	
	public void move() {
		// iterate thru all body parts of snake
		for (int i = bodyParts; i > 0; i--) {
			// shift parts
			x[i] = x[i - 1];
			y[i] = y[i - 1];
			
		}
		
		// changes direction snake is headed (R, L, U, D are direction chars)
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE; // y-coordinate of snake's head
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE; 
		}
	}
	
	public void checkApple() {
		// examine coordinates of snake and apple
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;  // score
			newApple();  // call to generate new apple
		}
	}
	
	public void checkCollisions() {
		// check if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {  // x[0] is head
				running = false;  // triggers game over
			}
		}
		
		// check if head touches left border
		if (x[0] < 0) {
			running = false;  // triggers game over
		}
		
		// check if head touches right border
		if (x[0] > SCREEN_WIDTH) {
			running = false;  // triggers game over
		}
		
		// check if head touches top border
		if (y[0] > SCREEN_HEIGHT) {
			running = false;  // triggers game over
		}
		
		// check if head touches bottom border
		if (y[0] < 0) {
			running = false;  // triggers game over
		}
		
		// stop timer at game over
		if (!running) {
			timer.stop();
		}
		
	}
	
	public void gameOver(Graphics g) {
		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		// instance of font metrics to center text
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		// Display score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		// instance of font metrics to center text
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {  // if game is running
			move();
			checkApple(); // check if ran into apple
			checkCollisions();
		}
		repaint();
		
	}
	
	/** inner class */
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:  // limit user to 90 degree turns (not 180)
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT: 
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
			
		}

		
	}
}

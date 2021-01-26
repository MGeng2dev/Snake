import javax.swing.JFrame;

public class GameFrame extends JFrame {
	
	GameFrame() {
		
		this.add(new GamePanel()); // same as creating instance in separate line
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();  // fits JFrame snuggly around components in frame
		this.setVisible(true);
		this.setLocationRelativeTo(null); // puts in the middle of window
		
		
	}
	
}

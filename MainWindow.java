import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*****************
 * Class to hold the MainWindow frame for VM simulator
 * @author Andy Exley
 *
 */
public class MainWindow extends JFrame 
implements ActionListener, ChangeListener {

	public static void main(String[] args) {
		MainWindow tWindow = new MainWindow();
		tWindow.setVisible(true);
	}
	
	private static final long serialVersionUID = 1L;
	private JToolBar gToolBar;
	private SimulationComponent gSimComp;
	private Timer gRunTimer;
	private JToggleButton gRunButton;

	/***************
	 * Constructor sets up the basics for the frame and application
	 */
	public MainWindow() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// set title
		setTitle("Virtual Machine Simulator");

		// use the default border layout
		setLayout(new BorderLayout());

		// add component
		this.add(getSimComponent());

		// add tool bar
		this.add(getToolBar(), BorderLayout.NORTH);

		// set up the timer
		gRunTimer = new Timer(250, new TimerTranslator(this, "step"));

		pack();
	}

	private JToggleButton getRunButton() {
		if(gRunButton == null) {
			gRunButton = new JToggleButton("Run");
			gRunButton.addChangeListener(this);
		}
		return gRunButton;
	}

	private SimulationComponent getSimComponent() {
		if(gSimComp == null) {
			gSimComp = new SimulationComponent();
		}
		return gSimComp;
	}

	/*******
	 * Initializes and populates the toolbar (if necessary) and returns it 
	 * @return
	 */
	private JToolBar getToolBar() {
		if(gToolBar == null) {
			gToolBar = new JToolBar();

			// Open button
			JButton open = new JButton("Load");
			open.setActionCommand("load");
			open.addActionListener(this);
			gToolBar.add(open);

			gToolBar.add(new JToolBar.Separator());

			// Run button
			gToolBar.add(getRunButton());
			
			gToolBar.add(new JToolBar.Separator());

			// Quit button
			JButton quit = new JButton("Quit");
			quit.setActionCommand("quit");
			quit.addActionListener(this);
			gToolBar.add(quit);
		}
		return gToolBar;
	}

	/************
	 * Takes care of action events (button clicks)
	 */
	public void actionPerformed(ActionEvent mEvent) {
		String tCommand = mEvent.getActionCommand();
		if(tCommand.equals("load")) {
			openFile();
		} else if (tCommand.equals("quit")) {
			System.exit(0);
		} else if (tCommand.equals("run")) {
			if(!gRunTimer.isRunning()) { gRunTimer.start(); }
		} else if (tCommand.equals("step")) {
			gSimComp.step();
			if(!gSimComp.isProgramRunning()) { gRunButton.setSelected(false); }
		}
	}

	/**
	 * Open a file.
	 */
	private void openFile() {
		JFileChooser c = new JFileChooser(System.getProperty("user.dir", "~"));
		if (c.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			getSimComponent().addProgram(c.getSelectedFile());
		}
	}

	/*******************************
	 * Translate Timer ActionEvents into action events for another listener.
	 * @author Michael Ekstrand <ekstrand@cs.umn.edu>
	 *
	 * In Java 6, the Timer supports action commands.  This is not, however, the
	 * case in Java 5.  Therefore, we need this adaptor class to translate timer
	 * events into events with commands.
	 */
	private class TimerTranslator implements ActionListener {
		private ActionListener delegate;
		private String command;

		public TimerTranslator(ActionListener l, String cmd) {
			delegate = l;
			command = cmd;
		}

		public void actionPerformed(ActionEvent e) {
			ActionEvent e2 = new ActionEvent(e.getSource(), e.getID(), command);
			delegate.actionPerformed(e2);
		}
	}

	/******************
	 * Deal with the Run button being toggled.
	 */
	private void onRunToggled() {
		if (gRunButton.isSelected()) {
			gRunTimer.start();
		} else {
			gRunTimer.stop();
		}
	}

	public void stateChanged(ChangeEvent e) {
		if(gRunButton == e.getSource()) {
			onRunToggled();
		}
	}
}

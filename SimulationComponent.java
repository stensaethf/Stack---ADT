import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**********************
 * Class SimulationComponent is a component that handles the graphics
 * of a Java Virtual Machine Simulator
 * @author Andy Exley
 * 
 */
public class SimulationComponent extends JComponent {

	private static final long serialVersionUID = 1L;
	private Vector<String> gProgNames;
	private Program gCurrentProgram;
	private boolean gProgramRunning;
	private GraphicsComponent gGraphicsComp;
	private JList gProgList;
	private JTextArea gStatusArea;

	/***********
	 * Constructor - initializes private attributes and sets up all graphics stuff
	 */
	public SimulationComponent() {
		
		// add graphics component
		this.setLayout(new BorderLayout());

		this.setMinimumSize(new Dimension(600, 400));
		this.setPreferredSize(new Dimension(600,400));
		
		// simple panel to hold memory stuff
		JPanel tMemPanel = new JPanel();
		tMemPanel.setLayout(new BorderLayout());
		tMemPanel.setMinimumSize(new Dimension(150, 400));
		tMemPanel.setPreferredSize(new Dimension(150, 400));
	
		JLabel tStackLabel = new JLabel("VM Memory", SwingConstants.CENTER);
		tStackLabel.setMinimumSize(new Dimension(100, 20));
		tStackLabel.setPreferredSize(new Dimension(100, 20));
		tMemPanel.add(tStackLabel, BorderLayout.NORTH);

		gGraphicsComp = new GraphicsComponent();
		tMemPanel.add(gGraphicsComp, BorderLayout.SOUTH);

		this.add(tMemPanel, BorderLayout.WEST);
	
		// simple panel to hold program list.
		JPanel tOtherPanel = new JPanel();
		tOtherPanel.setMinimumSize(new Dimension(450, 400));
		tOtherPanel.setPreferredSize(new Dimension(450, 400));
		tOtherPanel.setLayout(new BorderLayout());
	
		
		// simple panel to hold program list.
		JPanel tListPanel = new JPanel();
		tListPanel.setMinimumSize(new Dimension(450, 200));
		tListPanel.setPreferredSize(new Dimension(450, 200));
		tListPanel.setLayout(new BorderLayout());

		JLabel tListLabel = new JLabel("Program List");
		tListLabel.setMinimumSize(new Dimension(450, 20));
		tListLabel.setPreferredSize(new Dimension(450, 20));
		tListPanel.add(tListLabel, BorderLayout.NORTH);

		// add list component
		gProgList = new JList();
		gProgList.setMinimumSize(new Dimension(450, 180));
		gProgList.setPreferredSize(new Dimension(450, 180));
		gProgList.setEnabled(false);		
		tListPanel.add(gProgList, BorderLayout.SOUTH);

		tOtherPanel.add(tListPanel, BorderLayout.NORTH);
		
		// simple panel to hold status info
		JPanel tStatusPanel = new JPanel();
		tStatusPanel.setMinimumSize(new Dimension(450,200));
		tStatusPanel.setPreferredSize(new Dimension(450,200));
		tStatusPanel.setLayout(new BorderLayout());
		
		JLabel tStatusLabel = new JLabel("Status Information");
		tStatusLabel.setMinimumSize(new Dimension(450, 20));
		tStatusLabel.setPreferredSize(new Dimension(450, 20));
		tStatusPanel.add(tStatusLabel, BorderLayout.NORTH);
	
		gStatusArea = new JTextArea(7, 40);
		gStatusArea.setEditable(false);

		JScrollPane tTextPane = new JScrollPane(gStatusArea);
		tTextPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tTextPane.setMinimumSize(new Dimension(450, 180));
		tTextPane.setPreferredSize(new Dimension(450, 180));
		
		tStatusPanel.add(tTextPane, BorderLayout.SOUTH);
		
		tOtherPanel.add(tStatusPanel, BorderLayout.SOUTH);
		
		this.add(tOtherPanel, BorderLayout.EAST);
		
		gProgNames = new Vector<String>();
		
	}
	
	/*******************
	 * step method - called every time MainWindow's timer ticks
	 */
	public void step() {
		gProgramRunning = true;
		if(gCurrentProgram == null) {
			return;
		} else if(gCurrentProgram.isFinished()) {
			gProgramRunning = false;
		} else {
			gProgramRunning = true;
			gCurrentProgram.step(this);
		}
		gGraphicsComp.repaint();
	}

	
	/***************
	 * Add a program to the SimulationComponent from the given file.
	 * If the file doesn't load properly, it isn't added
	 * @param tFile the file to read
	 */
	public void addProgram(File tFile) { 
		Program tProg = null;
		// if no queue, can only run one at a time.
		try {

			// attempt to instantiate the program from the file
			tProg = new Program(tFile, this);
			// if success, set as simulator's current program... if queue is here, add to queue.
/*			if(gProgQueue != null) {
				gProgQueue.enqueue(tProg);
			} else {
*/				gCurrentProgram = tProg;
				gGraphicsComp.clearMethods();
				Object[] arr = new Object[1];
				arr[0] = tProg.getName();
				gProgList.setListData(arr);
//			}
		} catch (SyntaxErrorException e) {
			gStatusArea.append("Syntax error in " + tFile.getName() + ":\n");
			gStatusArea.append(e.getMessage() + "\n");
			gStatusArea.append("Program not loaded.\n");
		}
		
		gGraphicsComp.repaint();
	}
	
	/*************
	 * Adds the given method to the graphical stack representation
	 * @param mMethod the method to add
	 */
	public void addMethodToGraphicalStack(Method mMethod) {
		gGraphicsComp.addMethod(mMethod);
	}
	
	/**************
	 * Attempts to remove the given method from the graphical stack representation
	 * @param mMethod the method to remove
	 */
	public void removeMethodFromGraphicalStack(Method mMethod) {
		gGraphicsComp.removeMethod(mMethod);
	}
	
	/**************
	 * Adds the given program to the graphical queue representation
	 * @param mProg the program to add
	 */
	public void addProgramToGraphicalQueue(Program mProg) {
		gProgNames.add(mProg.getName());
		gProgList.setListData(gProgNames);
		repaint();
	}
	
	/***************
	 * Attempts to remove the given program from the graphical queue representation
	 * @param mProg the program to remove
	 */
	public void removeProgramFromGraphicalQueue(Program mProg) {
		if(gProgNames.firstElement().equals(mProg.getName())) {
			gProgNames.remove(0);
		}
		gProgList.setListData(gProgNames);
		repaint();
	}

	/****************
	 * Appends the given string to the status text area. This method also adds a 
	 * newline ('\n') to the String given
	 * @param str the string to append
	 */
	public void addStatusMessage(String str) {
		gStatusArea.append(str + "\n");
	}
	
	/****************
	 * Returns true if a program is currently being run by the simulator
	 * @return true if a program is currently being run by the simulator
	 */
	public boolean isProgramRunning() { return gProgramRunning; }

	/****************
	 * Orders the simulation to halt
	 */
	public void halt() { gProgramRunning = false; }
}

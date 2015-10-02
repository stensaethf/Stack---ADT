import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

/************************
 * GraphicsComponent is the part of the SimulationComponent that 
 * keeps track of the graphical representation of the VM memory
 * @author Andy Exley
 *
 */
public class GraphicsComponent extends JComponent {

	private static final long serialVersionUID = 1L;

	private ArrayList<Method> gList;

	/*********
	 * Constructor
	 */
	public GraphicsComponent() {
		this.setMinimumSize(new Dimension(150, 380));
		this.setPreferredSize(new Dimension(150,380));
		this.setVisible(true);
		gList = new ArrayList<Method>();
	}
	
	/************
	 * Overriding paintComponent method.
	 */
	public void paintComponent(Graphics mGraphics) {
		Graphics2D g2 = (Graphics2D) mGraphics;

		int TOP_BUFFER = 20;
		int LEFT_BUFFER = 10;
		int ADDRESS_WIDTH = 130;
		int ADDRESS_HEIGHT = 340;

		// "stack"
		g2.drawRect(LEFT_BUFFER, TOP_BUFFER, ADDRESS_WIDTH, ADDRESS_HEIGHT);
		int counter = 0;
		for(Method tMethod: gList) {
			counter += tMethod.getSize();
			g2.setColor(Color.RED);
			if(ADDRESS_HEIGHT - counter < 0) {
				System.err.println("ERROR! Stack overflow!!");
				System.exit(1);
			}
			g2.fillRect(LEFT_BUFFER + 1, TOP_BUFFER + ADDRESS_HEIGHT - counter, ADDRESS_WIDTH - 1, tMethod.getSize());
			
			int vertavg = (TOP_BUFFER + ADDRESS_HEIGHT - counter) + tMethod.getSize()/2;
			g2.setColor(Color.BLACK);
			g2.drawString(tMethod.getName(), LEFT_BUFFER + 2, vertavg);
			counter++;
		}
	}
	
	/**********
	 * Adds a method to the 'memory' space
	 * @param mMethod the method to add
	 */
	public void addMethod(Method mMethod) {
		gList.add(mMethod);
	}
	
	/************
	 * Removes a method from the 'memory' space. A method passed to this will only be removed
	 * if it is on the top of the stack
	 * @param mMethod the method to remove
	 */
	public void removeMethod(Method mMethod) {
		if(gList.get(gList.size()-1).equals(mMethod)) { gList.remove(mMethod); }
	}
	
	/**********
	 * Clears the memory space
	 */
	public void clearMethods() { gList.clear(); }
}

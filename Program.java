import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/******************
 * Program - A class to represent a Java program within the
 * VM Simulator
 * 
 * @author Andy Exley
 *
 */
public class Program {
	
	/*************
	 * A simple syntax-checking mechanism that makes sure our programs have matching
	 * parentheses (), brackets [], and squiggly-brackets {}.
	 * To be implemented by students.
	 * @param mScanner the scanner passed in from the constructor
	 * @throws SyntaxErrorException if a syntax error is encountered, with a helpful message.
	 * @return true if syntax passes.
	 */
	private boolean syntaxCheck(Scanner mScanner) throws SyntaxErrorException {
		Stack<String> stack = new LinkedStackImplementation<String>();
		int count = 0;
		while(mScanner.hasNext()){
			count++;
			String next_line = mScanner.nextLine();
			System.out.println(next_line);
			String[] characters = next_line.split("");
			// for(String x : characters){
			// 	System.out.println(x);
			// }
			for(int i = 0; i < characters.length; i++) {
				String next_character = characters[i];
				if(next_character.equals("/") && characters[i+1].equals("/")) {
					i = characters.length;
				} else 
				if(next_character.equals("(") || next_character.equals("[") 
					|| next_character.equals("{")) {
					stack.push(next_character);
				} else if(next_character.equals(")")) {
					if(!stack.peek().equals("(")) {
						throw new SyntaxErrorException("Extra closing Parenthesis in line " + count + "!");	
					} else {
						stack.pop();
					}
				} else if(next_character.equals("]")) {
					if(!stack.peek().equals("[")) {
						throw new SyntaxErrorException("Extra closing Bracket in line " + count + "!");
					} else {
						stack.pop();
					}
				} else if(next_character.equals("}")) {
					if(!stack.peek().equals("{")) {
						throw new SyntaxErrorException("Extra closing Brace in line " + count + "!");
					} else {
						stack.pop();
					}
				}		
			}
		}
		if(!stack.isEmpty()) {
			throw new SyntaxErrorException("Syntax Error: Unclosed brace, bracket, or parenthesis.");
		}
		return true;
		// TODO: implement me!
		// throw new SyntaxErrorException("Syntax checking not implemented!");
	}
	
	/*******************
	 * Program's constructor. Dynamically tries to instantiate its call stack... 
	 * If you haven't written class CallStack yet, it won't bomb.
	 * @param mFile The file to read for the program.
	 * @param mComp The simulation component that creates this program
	 * @throws SyntaxErrorException
	 */
	public Program(File mFile, SimulationComponent mComp) throws SyntaxErrorException {
		callStack = instantiate(mComp);
		bFinished = false;
		gName = mFile.getName();
		Scanner s = null;
		try {
			s = new Scanner(mFile);
		} catch (FileNotFoundException e) {
			System.err.println("Error: File " + mFile.getName() + " not found!");
			e.printStackTrace();
			System.exit(1);
		}
		if(syntaxCheck(s)) {
			s.close();
			try {
				s = new Scanner(mFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			loadMethodList(s);
			gMethodList.get(0).setStack(callStack);
		}
	}
	
	/************
	 * Should only be called by unit test classes
	 * @param mProgramString
	 * @throws SyntaxErrorException
	 */
	protected Program(String mProgramString) throws SyntaxErrorException {
		gName = "Unnamed program";
		Scanner s = new Scanner(mProgramString);
		if(syntaxCheck(s)) {
			s = new Scanner(mProgramString);
			loadMethodList(s);
		}		
	}

	/***********
	 * Gets the name of this program
	 * @return
	 */
	public String getName() { return gName;	}
	
	private Stack<Method> callStack;

	private void loadMethodList(Scanner mScanner) {
		String tLine = null;
		gMethodList = new LinkedList<Method>();

		while(mScanner.hasNextLine()) {
			tLine = mScanner.nextLine();
			Pattern tPattern = Pattern.compile(".*?(\\w*)\\(.*?\\)\\s*\\{.*");
			Matcher tM1 = tPattern.matcher(tLine);
			if(tM1.matches()) {
				Method tMethod = new Method(tM1.group(1), this);
				int bracketcounter = 1;
				while(bracketcounter > 0 && mScanner.hasNextLine()) {
					tLine = mScanner.nextLine();
					tMethod.addLine(tLine);
					char tPrevChar = ' ';
					for(int i=0; i<tLine.length(); i++) {
						char tChar = tLine.charAt(i);
						if(tChar == '/' && tPrevChar == '/') { i = tLine.length(); }
						else if(tChar == '{') { bracketcounter++; }
						else if(tChar == '}') { bracketcounter--; }
						tPrevChar = tChar;
					}
				}
				gMethodList.add(tMethod);
			}
		}
	}
	
	// Name of the program's file
	private String gName;
	
	// ordered list of Methods that this Program calls
	private List<Method> gMethodList;
	
	private boolean bFinished;
	private boolean gStarted = false;
	
	/***********
	 * Gets a method given a name
	 * @param mName the name of the Method to retrieve
	 * @return the method
	 */
	public Method getMethod(String mName) {
		for(Method tMeth: gMethodList) {
			if(tMeth.getName().equals(mName)) {
				Method ret = tMeth.copy();
				ret.setStack(callStack);
				return ret;
			}
		}
		return null;
	}

	/******************
	 * Executes one step of this method, using the given SimulationComponent to display
	 * what is going on
	 * @param mComp the SimulationComponent in which to display things
	 */
	public void step(SimulationComponent mComp) {
		if(callStack == null) {
			if(!gMethodList.get(0).isFinished()) {
				gMethodList.get(0).step(mComp);
			} else { bFinished = true; }
			return;
		}
		if(!gStarted && callStack != null) {
			callStack.push(gMethodList.get(0));
			gStarted = true;
		}
		if(callStack != null && !callStack.isEmpty()) { 
			if(callStack.peek().isFinished()) { callStack.pop(); }
			else { callStack.peek().step(mComp); }
		}
		else { bFinished = true; }
	}
	
	/*************
	 * Returns whether this program is finished executing
	 * @return true if the program is finished
	 */
	public boolean isFinished() {
		return bFinished;
	}
	
	
	/************
	 * Crazy little method to dynamically instantiate CallStack class
	 * @param mComp
	 * @return an instantiation of CallStack, or null if failed
	 */
	@SuppressWarnings("unchecked")
	private Stack<Method> instantiate(SimulationComponent mComp) {
		try {
			Constructor<?> m = Class.forName("CallStack").getConstructor(SimulationComponent.class);
			System.err.println("Call Stack instantiated... will attempt to run on VM!");
			return (Stack<Method>) m.newInstance(mComp);
		} catch (SecurityException e) {
			System.err.println("Cannot instantiate CallStack. Method calls won't work");
		} catch (NoSuchMethodException e) {
			System.err.println("Cannot instantiate CallStack: constructor invalid. Method calls won't work.");
		} catch (ClassNotFoundException e) {
			System.err.println("Cannot instantiate CallStack. Method calls won't work.");
		} catch (IllegalArgumentException e) {
			System.err.println("Cannot instantiate CallStack: constructor invalid. Method calls won't work.");
		} catch (InstantiationException e) {
			System.err.println("Cannot instantiate CallStack. Method calls won't work.");
		} catch (IllegalAccessException e) {
			System.err.println("Cannot instantiate CallStack. Method calls won't work.");
		} catch (InvocationTargetException e) {
			System.err.println("Cannot instantiate CallStack. Method calls won't work.");
		}
		return null;
	}
}

/*****************
 * Simple Exception to report Syntax errors in programs
 * @author Andy Exley
 *
 */
public class SyntaxErrorException extends Exception {

	public SyntaxErrorException(String mMessage) { 
		super(mMessage);
	}
	
	private static final long serialVersionUID = 1L;

}

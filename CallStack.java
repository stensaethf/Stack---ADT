import java.util.*;
/** 
 * CallStack.java
 * Class for the implementation of a stack using an array.
 * Takes items of type method.
 * @author Frederik Roenn Stensaeth and Javier Moran
 * 05.18.14
 */
public class CallStack implements Stack<Method> {
    /** Array used to represent stack. */
    private Method[] array;
    /** int size keeps track of the size of the stack. */
    private int size = 0;
    /** SimulationComponent variable is variable to be passed to Graphical
     * stack.
     */
    private SimulationComponent simulation_variable;

    /* Constructor for CallStack class. Initializes array and 
     * simluation_variable.
     */
    public CallStack(SimulationComponent sc) {
        simulation_variable = sc;
        array = new Method[8];
    }

    /** Adds an item to the top of this stack.
     * @param item The item to add.
     */
    public void push(Method item) {
        // Checks if item is valid.
        if(item != null) {
            ensureCapacity();
            // Adds item to array at index size.
            array[size] = item;
            simulation_variable.addMethodToGraphicalStack(item);
            size++;
        }
    }
    
    /** Removes the item from the top of this stack, and returns it.
     * Note that some implementations may throw an EmptyStackException when
     * the stack is empty, rather than returning a sentinel value.
     * @return the item at the top of the stack, or null if empty.
     */
    public Method pop() {
        // Checks if stack is empty and returns null.
        if(size == 0) {
            return null;
        }
        // Retrieves item at index size - 1 of array and returns it.
        Method value = array[size - 1];
        array[size - 1] = null;
        simulation_variable.removeMethodFromGraphicalStack(value);
        size--;
        return value;
    }  

    /** Returns the item on top of the stack, without removing it.
     * Note that some implementations may throw an EmptyStackException when
     * the stack is empty, rather than returning a sentinel value.
     * @return the item at the top of the stack, or null if empty.
     */
    public Method peek() {
        // Checks if stack is empty and returns null.
        if(size == 0) {
            return null;
        }
        // Returns top item of stack.
        return array[size - 1];
    }
    
    /** Returns true if the stack is empty. */
    public boolean isEmpty() {
        // Checks if stack is empty and returns true.
        if(size == 0) {
            return true;
        }
        return false;
    }
    
    /** Removes all items from the stack. */
    public void clear() {
        // Sets  all items in array to null.
        for(int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    private void ensureCapacity() {
        System.out.println(array);
        // Checks if array is full.
        if(array.length == size) {
            // Creates a new array of double the length of array and copies
            // items from array over to the new array.
            Method[] tmp = new Method[array.length * 2];
            for(int i = 0; i < array.length; i++) {
                tmp[i] = array[i];
            }
            array = tmp;
        }
    }
}

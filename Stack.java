/** 
 * An interface for the Stack ADT.
 * 
 * @param <T> The type of data that the stack stores.
 * 
 * @author Jadrian Miles
 */
public interface Stack<T> {
    /** Adds an item to the top of this stack.
     * @param item The item to add.
     */
    public void push(T item);
    
    /** Removes the item from the top of this stack, and returns it.
     * Note that some implementations may throw an EmptyStackException when
     * the stack is empty, rather than returning a sentinel value.
     * @return the item at the top of the stack, or null if empty.
     */
    public T pop();
    
    /** Returns the item on top of the stack, without removing it.
     * Note that some implementations may throw an EmptyStackException when
     * the stack is empty, rather than returning a sentinel value.
     * @return the item at the top of the stack, or null if empty.
     */
    public T peek();
    
    /** Returns true if the stack is empty. */
    public boolean isEmpty();
    
    /** Removes all items from the stack. */
    public void clear();
}

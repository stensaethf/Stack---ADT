/** 
 * LinkedStackImplementation.java
 * An implementation of the Stack ADT using linked nodes.
 * Created by Frederik Roenn Stensaeth and Javier Moran.
 * 05.12.14
 */
import java.util.EmptyStackException;
/** Class for the implementation of a Stack using linked nodes. */
public class LinkedStackImplementation<T> implements Stack<T> {
    private Node top_node = null;
    
    /** Class for Nodes. Nodes keep track of value and what is the next
     * node. */
    private class Node {
        /** T variable val is the value a node holds. */
        public T val;
        /** Node variable next is the next node in the chain. */
        public Node below;
        /** Creates an instance of the node. */
        private Node(T value) {
            val = value;
            below = null;
        }
    }

    /** Adds an item to the top of this stack.
     * @param item The item to add.
     */
    public void push(T item) {
        // Checks if item is valid.
        if(item != null) {
            // Creates node with item and adds it to the top of the chain.
            Node node = new Node(item);
            node.below = top_node;
            top_node = node;
        }
    }
    
    /** Removes the item from the top of this stack, and returns it.
     * Note that some implementations may throw an EmptyStackException when
     * the stack is empty, rather than returning a sentinel value.
     * @return the item at the top of the stack, or null if empty.
     */
    public T pop() {
        // Checks if stack is empty and throws exception.
        if(top_node == null) {
            throw new EmptyStackException();
        }
        // Gets top_node in order to return its value
        //and reassigns top_node.
        Node target = top_node;
        top_node = top_node.below;
        return target.val;
    }
    
    /** Returns the item on top of the stack, without removing it.
     * Note that some implementations may throw an EmptyStackException when
     * the stack is empty, rather than returning a sentinel value.
     * @return the item at the top of the stack, or null if empty.
     */
    public T peek() {
        // Checks if stack is empty and throws exception.
        if(top_node == null) {
            throw new EmptyStackException();
        }
        return top_node.val;
    }
    
    /** Returns true if the stack is empty. */
    public boolean isEmpty() {
        if(top_node == null) {
            return true;
        }
        return false;
    }
    
    /** Removes all items from the stack. */
    public void clear() {
        top_node = null;
    }

    public static void main(String[] args) {
        Stack<String> stack = new LinkedStackImplementation<String>();
        stack.push("leg");
        stack.push("arm");
        stack.push("body");
        stack.push("null");
        // A series of tests to ensure that the methods of the stack
        //implementation works as intended.
        if(!stack.peek().equals("null")) {
            System.out.printf(
                "*** push FAILED; Expected null, got %s ***\n", stack.peek());
        }
        String top = stack.pop();
        if(!top.equals("null")) {
            System.out.printf(
                "*** push FAILED; Expected null, got %s ***\n", top);
        }
        top = stack.pop();
        if(!top.equals("body")) {
            System.out.printf(
                "*** push FAILED; Expected body, got %s ***\n", top);
        }
        if(stack.isEmpty() == true) {
            System.out.println(
                "*** push FAILED; Expected false, got true ***");
        }
        stack.clear();
        if(stack.isEmpty() == false) {
            System.out.println(
                "*** push FAILED; Expected true, got false ***");
        }
        stack.push("0");
        top = stack.pop();
        if(!top.equals("0")) {
            System.out.printf(
                "*** push FAILED; Expected 0, got %s ***\n", top);
        }
        stack.clear();
        stack.clear();
        if(stack.isEmpty() == false) {
            System.out.println(
                "*** push FAILED; Expected true, got false ***");
        }
        stack.push(null);
        if(stack.isEmpty() == false) {
            System.out.println(
                "*** push FAILED; Expected true, got false ***");
        }
    }
}
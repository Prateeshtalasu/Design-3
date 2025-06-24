class LRUCache {
    class Node {
        int key, value;
        Node prev, next;

        Node(int k, int v) {
            key = k;
            value = v;
        }
    }

    private final int capacity;
    private final Node head, tail;
    private HashMap<Integer, Node> map;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;

    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        Node nodevalue = map.get(key);
        remove(nodevalue); // First remove the node from its current position
        movetofrontathead(nodevalue); // Then move it to the head
        return nodevalue.value;

    }

    public void movetofrontathead(Node node) {
        node.next = head.next;
        head.next.prev = node; // Link the old head.next back to the new node
        head.next = node;
        node.prev = head;

    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            remove(node); // Remove the existing node first
            node.value = value; // Update the value
            movetofrontathead(node); // Then move it to the front
        } else {
            Node node = new Node(key, value);
            map.put(key, node);
            movetofrontathead(node);
        }
        if (map.size() > capacity) {
            Node lru = removeLast(); // remove tail.prev (LRU)
            map.remove(lru.key); // remove from map
        }

    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private Node removeLast() {
        Node lru = tail.prev;
        remove(lru);
        return lru;
    }
}

//
/**
 * // This is the interface that allows for creating nested lists.
 * // You should not implement it, or speculate about its implementation
 * public interface NestedInteger {
 *
 * // @return true if this NestedInteger holds a single integer, rather than a
 * nested list.
 * public boolean isInteger();
 *
 * // @return the single integer that this NestedInteger holds, if it holds a
 * single integer
 * // Return null if this NestedInteger holds a nested list
 * public Integer getInteger();
 *
 * // @return the nested list that this NestedInteger holds, if it holds a
 * nested list
 * // Return empty list if this NestedInteger holds a single integer
 * public List<NestedInteger> getList();
 * }
 */

// The NestedInteger interface is provided by LeetCode.
// You use it, but don't need to implement it yourself.
// It has methods:
// - isInteger(): returns true if it holds a single integer
// - getInteger(): returns the integer if it's an integer
// - getList(): returns the nested list if it's a list

public class NestedIterator implements Iterator<Integer> {

    // Stack to track where we are in all nested levels
    private Stack<Iterator<NestedInteger>> stack;

    // Temporarily holds the next integer found
    private Integer nextInteger;

    // Constructor
    public NestedIterator(List<NestedInteger> nestedList) {
        stack = new Stack<>();
        stack.push(nestedList.iterator()); // Start with the outer list
        nextInteger = null; // Nothing found yet
    }

    // Return the next integer
    @Override
    public Integer next() {
        // Make sure there's a next element
        if (!hasNext()) {
            return null;
        }

        Integer result = nextInteger; // Use the stored result
        nextInteger = null; // Reset for future calls
        return result;
    }

    // Check if there's another integer to return
    @Override
    public boolean hasNext() {
        // If we already found the next integer, return true
        if (nextInteger != null)
            return true;

        // Loop while we still have something to process
        while (!stack.isEmpty()) {
            Iterator<NestedInteger> current = stack.peek(); // Check top list

            if (!current.hasNext()) {
                // This list is finished — go one level up
                stack.pop();
                continue;
            }

            NestedInteger ni = current.next(); // Get next item

            if (ni.isInteger()) {
                // If it's a number, store it and return true
                nextInteger = ni.getInteger();
                return true;
            } else {
                // If it's a list, go deeper into it
                stack.push(ni.getList().iterator());
            }
        }

        // No more items found
        return false;
    }
}

/**
 * Your NestedIterator object will be instantiated and called as such:
 * NestedIterator i = new NestedIterator(nestedList);
 * while (i.hasNext()) v[f()] = i.next();
 */
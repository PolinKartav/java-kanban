package manager;

public class Node<T> {
    public Node<T> next;
    public Node<T> prev;
    public T elem;

    Node(Node<T> prev, T elem, Node<T> next){
        this.elem = elem;
        this.prev = prev;
        this.next = next;
    }
}

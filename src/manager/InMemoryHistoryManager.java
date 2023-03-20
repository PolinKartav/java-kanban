package manager;

import tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    public CustomLinkedList<Task> history;
    public InMemoryHistoryManager() {
        history = new CustomLinkedList<>();
    }

    @Override
    public void add(Task task) {
        history.add(task);
    }

    @Override
    public void remove(Task task){ history.remove(task); }

    @Override
    public List<Task> getHistoryTasks() {
        return history.getTasks();
    }
    @Override
    public void clearHistory(){
        history.clear();
    }


    //Расчитать время для выполнения Эпика
}

class CustomLinkedList<T>{
    private final Map<Integer, Node<T>> accordance; //соотвествие id Task с Node
    public Node<T> head;
    public Node<T> tail;
    private int size = 0;

    public CustomLinkedList() {
        accordance = new HashMap<>();
    }

    public void add(T element){
        var key = ((Task) element).getId();
        if(accordance.containsKey(key)){
            removeNode(accordance.get(key));
            accordance.remove(key);
        }
        accordance.put(key, linkLast(element));
    }

    public void remove(T element){
        var key = ((Task) element).getId();
        if(accordance.containsKey(key)){
            removeNode(accordance.get(key));
            accordance.remove(key);
        }
    }

    private Node<T> linkLast(T element){
        Node<T> last = tail;
        Node<T> newNode = new Node<>(last, element, null);
        tail = newNode;

        if(last == null){
            head = newNode;
        } else {
            last.next = newNode;
        }
        size++;
        return newNode;
    }

    public List<T> getTasks() {
        List<T> listOfTasks = new ArrayList<>();//все задачи родятеля Task

        if(head != null){
            Node<T> node = head;
            do{
                listOfTasks.add(node.elem);
                node = node.next;
            }while(node != null);
        }
        return listOfTasks;
    }

    private void removeNode(Node<T> node){
        Node<T> prev = node.prev;
        Node<T> next = node.next;

        if(prev == null){
            head = next;
        }else{
            prev.next = next;
            node.prev = null;
        }

        if(next == null){
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.elem = null;
        size--;
    }

    void clear() {
        for (Node<T> node = head; node != null; ) {
            Node<T> next = node.next;
            node.elem = null;
            node.next = null;
            node.prev = null;
            node = next;
        }
        head = tail = null;
        size = 0;

        accordance.clear();
    }
}

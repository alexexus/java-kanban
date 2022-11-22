package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.model.Task;

import java.util.HashMap;
import java.util.Map;

public class CustomLinkedList<E> {

    transient Node<E> last;
    transient Node<E> first;
    transient int size = 0;

    Map<Integer, Node<E>> map = new HashMap<>();

    public Map<Integer, Node<E>> getMap() {
        return map;
    }

    public void remove(int id) {
        map.remove(id);
    }

    private static class Node<E> {
        Task item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, Task task, Node<E> next) {
            this.item = task;
            this.next = next;
            this.prev = prev;
        }
    }

    public boolean add(Task e) {
        linkLast(e);
        return true;
    }

    void linkLast(Task e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        map.put(e.getId(), newNode);
    }

    void removeNode(Node<E> x) {
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
    }

    public Node<E> get(int index) {
        return node(index);
    }

    Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }
}

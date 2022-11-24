package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodes = new HashMap<>();

    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (nodes.get(task.getId()) != null) {
            removeNode(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(int taskId) {
        if (nodes.get(taskId) != null) {
            removeNode(taskId);
        }
        /*
        В общем оказалось что надо было просто добавить проверку на то, есть ли в
        истории задача которую мы хотим удалить и только потом удалять её.
         */
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    private void linkLast(Task task) {
        final Node lastNode = tail;
        final Node newNode = new Node(lastNode, task , null);
        tail = newNode;
        if (lastNode == null)
            head = newNode;
        else
            lastNode.next = newNode;
        nodes.put(task.getId(), newNode);
    }

    private void removeNode(int id) {
        Node node = nodes.get(id);
        final Node next = node.next;
        final Node prev = node.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.task = null;
    }

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Node prev, Task task, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }
}

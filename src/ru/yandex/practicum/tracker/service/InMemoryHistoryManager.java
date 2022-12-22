package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodes = new HashMap<>();

    private Node head;
    private Node tail;

    @Override
    public void clearNodes() {
        nodes.clear();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            removeNode(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int taskId) {
        removeNode(taskId);
    }

    @Override
    public List<Integer> getHistory() {
        List<Integer> tasks = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.task.getId());
            currentNode = currentNode.next;
        }
        return tasks;
    }

    private void linkLast(Task task) {
        final Node lastNode = tail;
        final Node newNode = new Node(lastNode, task, null);
        tail = newNode;
        if (lastNode == null) {
            head = newNode;
        } else {
            lastNode.next = newNode;
        }
        nodes.put(task.getId(), newNode);
    }

    private void removeNode(int id) {
        Node node = nodes.get(id);
        if (node == null) {
            return;
        }
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

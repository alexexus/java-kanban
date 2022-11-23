package ru.yandex.practicum.tracker.service;

import ru.yandex.practicum.tracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodes = new HashMap<>();

    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        if (nodes.get(task.getId()) != null) {
            removeNode(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(int taskId) {
        removeNode(taskId);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        for (Node node : nodes.values()) {
            tasks.add(node.task);
        }
        return tasks;
    }

    private void linkLast(Task task) {
        final Node lastNode = last;
        final Node newNode = new Node(task);
        last = newNode;
        if (lastNode == null) {
            first = newNode;
        } else {
            lastNode.next = newNode;
        }
        nodes.put(task.getId(), newNode);
    }

    private void removeNode(int id) {
        final Node next = nodes.get(id).next;
        final Node prev = nodes.get(id).prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            nodes.get(id).prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            nodes.get(id).next = null;
        }

        nodes.remove(id);
    }

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }
}

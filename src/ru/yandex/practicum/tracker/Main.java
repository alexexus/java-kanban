package ru.yandex.practicum.tracker;

import ru.yandex.practicum.tracker.service.FileBackedTasksManager;

import java.io.File;

import static ru.yandex.practicum.tracker.service.FileBackedTasksManager.loadFromFile;

public class Main {

    public static void main(String[] args) {

        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("resources/file2.csv"));

        System.out.println(fileBackedTasksManager.getTaskById(1));
        System.out.println(fileBackedTasksManager.getTaskById(4));
        System.out.println(fileBackedTasksManager.getEpicById(2));
        System.out.println(fileBackedTasksManager.getSubtaskById(3));
        System.out.println(fileBackedTasksManager.getSubtaskById(5));

    }
}

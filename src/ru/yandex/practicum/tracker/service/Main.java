package ru.yandex.practicum.tracker.service;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
    }
}

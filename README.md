# Kanban
### Описание
Сервис для хранения задач. Позволяет эффективно организовать совместную работу над задачами. Данные хранятся в файлах.
### Инструкция по развёртыванию
Запустить файл Main. Отправлять запросы по URL - http://localhost:8080/
Сохранить задачу - POST http://localhost:8080/save/task/task?API_TOKEN=...
Получить задачу из сервера - GET http://localhost:8080/load/task/task?API_TOKEN=...

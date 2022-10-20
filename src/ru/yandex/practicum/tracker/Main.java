package ru.yandex.practicum.tracker;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TasksManager tasksManager = new TasksManager();

        tasksManager.createTask(new Task("������ 1", TaskStatus.NEW, tasksManager.getGeneratorId(), "�������� ������ 1"));
        tasksManager.createTask(new Task("������ 2", TaskStatus.NEW, tasksManager.getGeneratorId(), "�������� ������ 2"));

        tasksManager.createEpic(new Epic("���� 1", TaskStatus.NEW, tasksManager.getGeneratorId(), "�������� ����� 1", new ArrayList<>()));
        tasksManager.createSubtask(new Subtask("��������� 1 � ����� 1", TaskStatus.NEW,
                tasksManager.getGeneratorId(), "�������� ��������� 1 � ����� 1", 3));
        tasksManager.createSubtask(new Subtask("��������� 2 � ����� 1", TaskStatus.NEW,
                tasksManager.getGeneratorId(), "�������� ��������� 2 � ����� 1", 3));

        tasksManager.createEpic(new Epic("���� 2", TaskStatus.NEW, tasksManager.getGeneratorId(), "�������� ����� 2", new ArrayList<>()));
        tasksManager.createSubtask(new Subtask("��������� 1 � ����� 2", TaskStatus.NEW,
                tasksManager.getGeneratorId(), "�������� ��������� 1 � ����� 2", 6));

        for (Task task : tasksManager.getTasks().values()) {
            System.out.println("���: " + task.getName() + " " + "��������: " + task.getDescription()
                    + " " + "������: " + task.getStatus() + " " + "�����: " + task.getId());
        }

        for (Epic epic : tasksManager.getEpics().values()) {
            System.out.println("���: " + epic.getName() + " " + "��������: " + epic.getDescription()
                    + " " + "������: " + epic.getStatus() + " " + "�����: " + epic.getId() + " " + "������ ���������: " + epic.getSubTaskIds());
        }

        for (Subtask subtask : tasksManager.getSubtasks().values()) {
            System.out.println("���: " + subtask.getName() + " " + "��������: " + subtask.getDescription()
                    + " " + "������: " + subtask.getStatus() + " " + "�����: " + subtask.getId() + " " + "����� �����: " + subtask.getEpicId());
        }

        tasksManager.updateTask(new Task("������ 1 (update)", TaskStatus.IN_PROGRESS, 1, "�������� ������ 1 (update)"));
        tasksManager.updateTask(new Task("������ 2 (update)", TaskStatus.IN_PROGRESS, 2, "�������� ������ 2 (update)"));

        for (Task task : tasksManager.getTasks().values()) {
            System.out.println("���: " + task.getName() + " " + "��������: " + task.getDescription()
                    + " " + "������: " + task.getStatus() + " " + "�����: " + task.getId());
        }

        tasksManager.updateEpic(new Epic("���� 1 (update)", TaskStatus.NEW, 3,
                "�������� ����� 1 (update)", tasksManager.getEpics().get(3).getSubTaskIds()));
        tasksManager.updateSubtask(new Subtask("��������� 1 � ����� 2 (update)", TaskStatus.DONE,
                4, "�������� ��������� 1 � ����� 2 (update)", 6));
        tasksManager.updateSubtask(new Subtask("��������� 2 � ����� 1 (update)", TaskStatus.IN_PROGRESS,
                5, "�������� ��������� 2 � ����� 1 (update)", 3));

        for (Epic epic : tasksManager.getEpics().values()) {
            System.out.println("���: " + epic.getName() + " " + "��������: " + epic.getDescription()
                    + " " + "������: " + epic.getStatus() + " " + "�����: " + epic.getId() + " " + "������ ���������: " + epic.getSubTaskIds());
        }

        for (Subtask subtask : tasksManager.getSubtasks().values()) {
            System.out.println("���: " + subtask.getName() + " " + "��������: " + subtask.getDescription()
                    + " " + "������: " + subtask.getStatus() + " " + "�����: " + subtask.getId() + " " + "����� �����: " + subtask.getEpicId());
        }

        tasksManager.removeTaskById(2);
        tasksManager.removeEpicById(3);

        for (Epic epic : tasksManager.getEpics().values()) {
            System.out.println("���: " + epic.getName() + " " + "��������: " + epic.getDescription()
                    + " " + "������: " + epic.getStatus() + " " + "�����: " + epic.getId() + " " + "������ ���������: " + epic.getSubTaskIds());
        }

        for (Task task : tasksManager.getTasks().values()) {
            System.out.println("���: " + task.getName() + " " + "��������: " + task.getDescription()
                    + " " + "������: " + task.getStatus() + " " + "�����: " + task.getId());
        }
    }
}

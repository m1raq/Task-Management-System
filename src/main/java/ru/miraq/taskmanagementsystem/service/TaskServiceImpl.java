package ru.miraq.taskmanagementsystem.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.miraq.taskmanagementsystem.dto.comment.GetCommentDTO;
import ru.miraq.taskmanagementsystem.dto.task.*;
import ru.miraq.taskmanagementsystem.entity.task.TaskEntity;
import ru.miraq.taskmanagementsystem.entity.task.TaskStatus;
import ru.miraq.taskmanagementsystem.entity.user.UserEntity;
import ru.miraq.taskmanagementsystem.exception.CreateTaskException;
import ru.miraq.taskmanagementsystem.exception.InputParamException;
import ru.miraq.taskmanagementsystem.exception.TaskNotFoundException;
import ru.miraq.taskmanagementsystem.mapper.TaskMapper;
import ru.miraq.taskmanagementsystem.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final UserServiceImpl userService;

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(UserServiceImpl userServiceImpl, TaskMapper taskMapper, TaskRepository taskRepository) {
        this.userService = userServiceImpl;
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
    }

    @Override
    public void createTask(CreateTaskDTO createTaskDTO, String email) throws CreateTaskException {
        if(createTaskDTO.getName().isEmpty() || createTaskDTO.getDescription().isEmpty()) {
            throw new CreateTaskException("Заполните поля - Название, Описание и Приоритетность");
        }
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName(createTaskDTO.getName());
        taskDTO.setDescription(createTaskDTO.getDescription());
        taskDTO.setPriority(createTaskDTO.getPriority());
        taskDTO.setAuthor(userService.getUserByEmail(email));
        taskDTO.setStatus(TaskStatus.WAITING);
        taskRepository.save(taskMapper.toEntity(taskDTO));
        log.info("Пользователь {} создал задачу", email);
    }



    @Override
    public void updateTask(UpdateTaskDTO updateTaskDTO, String ownerEmail) {
        TaskDTO taskDTO = userService.getUserByEmail(ownerEmail)
                .getTask().stream()
                .filter(task -> task.getName().equals(updateTaskDTO.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskDTO.setName(updateTaskDTO.getName());
        taskDTO.setDescription(updateTaskDTO.getDescription());
        taskDTO.setPriority(updateTaskDTO.getPriority());
        if (updateTaskDTO.getExecutorEmail().equals("1")) {
            taskDTO.setExecutor(null);
            taskDTO.setStatus(TaskStatus.COMPLETED);
        } else if (updateTaskDTO.getExecutorEmail().equals("0")) {
            taskDTO.setExecutor(null);
            taskDTO.setStatus(TaskStatus.WAITING);
        } else {
            taskDTO.setExecutor(userService.getUserByEmail(updateTaskDTO.getExecutorEmail()));
        }
        taskRepository.save(taskMapper.toEntity(taskDTO));
        log.info("Пользователь {} отредактировал задачу {}", ownerEmail, taskDTO.getName());
    }

    @Override
    public void setExecutor(String executorEmail, String taskName, String ownEmail) throws TaskNotFoundException {
        TaskDTO taskDTO = userService.getUserByEmail(ownEmail)
                .getTask()
                .stream()
                .filter(task -> task.getName().equals(taskName))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Неуспешная попытка назначения исполнителя на задание {}", taskName);
                    return new TaskNotFoundException("Задание с названием " + taskName + " не найдено");
                });
        taskDTO.setExecutor(userService.getUserByEmail(executorEmail));
        taskDTO.setStatus(TaskStatus.IN_PROGRESS);
        taskRepository.save(taskMapper.toEntity(taskDTO));

    }

    @Override
    public List<GetTaskDTO> getOwnTasks(String ownerEmail) {
        ArrayList<GetTaskDTO> ownTasks = new ArrayList<>();
        taskRepository.findByAuthorEmail(ownerEmail)
                .forEach(task -> ownTasks.add(GetTaskDTO.builder()
                        .name(task.getName())
                        .description(task.getDescription())
                        .priority(task.getPriority())
                        .status(task.getStatus())
                        .executor(Optional.ofNullable(task.getExecutor()).orElse(new UserEntity()).getEmail())
                        .author(task.getAuthor().getEmail())
                        .commentDTO(getCommentsDTO(task))
                        .build()));
        return ownTasks;

    }

    @Override
    public List<GetTaskDTO> getSomeoneTasks(String ownerEmail) {
        return taskRepository.findByAuthorEmailNot(ownerEmail).stream()
                .map(task -> GetTaskDTO.builder()
                        .name(task.getName())
                        .description(task.getDescription())
                        .priority(task.getPriority())
                        .status(task.getStatus())
                        .author(task.getAuthor().getEmail())
                        .executor(Optional.ofNullable(task.getExecutor()).orElse(new UserEntity()).getEmail())
                        .commentDTO(getCommentsDTO(task))
                        .build())
                .toList();

    }

    @Override
    public void deleteTask(String taskName, String ownerEmail) {
        userService.getUserByEmail(ownerEmail).getTask()
                .forEach(task -> {
                    if (task.getName().equals(taskName)) {
                        taskRepository.deleteById(task.getId());
                    }
                });

    }


    @Override
    public void updateTaskStatus(String taskName, TaskStatus taskStatus, String executorEmail) throws TaskNotFoundException {
        TaskDTO taskDTO = userService.getUserByEmail(executorEmail)
                .getTaskInProgress()
                .stream()
                .filter(task -> task.getName().equals(taskName))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Неуспешная попытка изменения статуса задания {} исполнителем {}", taskName, executorEmail);
                    return new TaskNotFoundException("Задание с названием " + taskName + " не найдено");
                });
        taskDTO.setStatus(taskStatus);
        taskRepository.save(taskMapper.toEntity(taskDTO));
    }

    @Override
    public List<GetTaskDTO> getTasksByEmail(String email, String sortType) throws InputParamException {
        if(sortType.equals("1")){
            return taskRepository.findByAuthorEmail(email)
                    .stream()
                    .map(task -> GetTaskDTO.builder()
                            .name(task.getName())
                            .description(task.getDescription())
                            .priority(task.getPriority())
                            .status(task.getStatus())
                            .author(task.getAuthor().getEmail())
                            .executor(Optional.ofNullable(task.getExecutor()).orElse(new UserEntity()).getEmail())
                            .commentDTO(getCommentsDTO(task))
                            .build()
                    ) .toList();
        } else if(sortType.equals("2")){
            return getGetTaskDTOS(email);
        } else {
            log.error("Неуспешная попытка получения списка заданий по email {}. Неверный параметр сортировки", email);
            throw new InputParamException("Неверный параметр сортировки");
        }
    }

    @Override
    public List<GetTaskDTO> getOwnTasksInProgress(String ownerEmail) {
        return getGetTaskDTOS(ownerEmail);
    }

    @Override
    public TaskEntity getTaskByName(String taskName) {
        return taskRepository.findByName(taskName);
    }

    private List<GetTaskDTO> getGetTaskDTOS(String ownerEmail) {
        return taskRepository.findByExecutorEmail(ownerEmail)
                .stream()
                .map(task -> GetTaskDTO.builder().name(task.getName())
                        .description(task.getDescription())
                        .priority(task.getPriority())
                        .status(task.getStatus())
                        .author(task.getAuthor().getEmail())
                        .executor(Optional.ofNullable(task.getExecutor()).orElse(new UserEntity()).getEmail())
                        .commentDTO(task.getComments()
                                .stream()
                                .map(comment -> GetCommentDTO.builder()
                                        .comment(comment.getText())
                                        .author(task.getAuthor().getEmail())
                                        .build())
                                .toList())
                .build())
                .toList();
    }

    private List<GetCommentDTO> getCommentsDTO(TaskEntity task){
        return task.getComments()
                .stream()
                .map(comment -> GetCommentDTO.builder()
                        .comment(comment.getText())
                        .author(task.getAuthor().getEmail())
                        .build())
                .toList();
    }


}

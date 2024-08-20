package ru.miraq.taskmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.miraq.taskmanagementsystem.dto.TaskDTO;
import ru.miraq.taskmanagementsystem.entity.task.TaskStatus;
import ru.miraq.taskmanagementsystem.mapper.TaskMapper;
import ru.miraq.taskmanagementsystem.repository.TaskRepository;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final UserServiceImpl userServiceImpl;

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(UserServiceImpl userServiceImpl, TaskMapper taskMapper, TaskRepository taskRepository) {
        this.userServiceImpl = userServiceImpl;
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
    }

    @Override
    public void createTask(TaskDTO taskDTO, String email) {
        taskDTO.setAuthor(userServiceImpl.getUserByEmail(email));
        taskDTO.setStatus(TaskStatus.WAITING);
        taskRepository.save(taskMapper.toEntity(taskDTO));
    }

    @Override
    public void updateTask(TaskDTO taskDTO) {
        taskRepository.save(taskMapper.toEntity(taskDTO));
    }


    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void setExecutor(String executorEmail, Long taskId) {
        TaskDTO taskDTO = taskMapper.toDto(taskRepository.findById(taskId).
                orElseThrow(() -> new RuntimeException("Task not found")));
        taskDTO.setExecutor(userServiceImpl.getUserByEmail(executorEmail));
        taskRepository.save(taskMapper.toEntity(taskDTO));
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        return taskMapper.toDto(taskRepository.findById(id).orElseThrow());
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}

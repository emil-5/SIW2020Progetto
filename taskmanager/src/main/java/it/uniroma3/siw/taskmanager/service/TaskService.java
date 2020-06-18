package it.uniroma3.siw.taskmanager.service;



import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.repository.TaskRepository;

@Service
public class TaskService {
	
	@Autowired
	protected TaskRepository taskRepository;
	
	@Transactional
	public Task getTask(Long id) {
		Optional<Task> result = this.taskRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Task getTask(String name) {
		Optional<Task> result = this.taskRepository.findByName(name);
		return result.orElse(null);
	}
	
	@Transactional
	public List<Task> getTask(Project project) {
		return this.taskRepository.findByProject(project);
	}
	
	@Transactional
	public void saveTask(Task task) {
		this.taskRepository.save(task);
	}
	
	@Transactional
	public void deleteTask(Task task) {
		this.taskRepository.delete(task);
	}
	
	@Transactional
	public void addTag(Task task, Tag tag) {
		task.addTag(tag);
		this.taskRepository.save(task);
	}

	public void addProject(@Valid Task task, Project project) {
		task.setProject(project);
		taskRepository.save(task);
		
	}
	
	

}

package it.uniroma3.siw.taskmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Task;

public interface TaskRepository extends CrudRepository<Task,Long>{
	
	public Optional<Task> findById(Long id);
	
	public Optional<Task> findByName(String name);
	
	public List<Task> findByProject(Project project);
	

}

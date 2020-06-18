package it.uniroma3.siw.taskmanager.repository;

import it.uniroma3.siw.taskmanager.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Task;

public interface CommentRepository extends CrudRepository<Comment, Long>{
	
	public List<Comment> findByTask(Task task);
	
	public Optional<Comment> findById(Long id);

}

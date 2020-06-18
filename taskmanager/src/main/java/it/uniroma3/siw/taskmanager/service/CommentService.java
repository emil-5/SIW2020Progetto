package it.uniroma3.siw.taskmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.taskmanager.repository.CommentRepository;

import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Task;

@Service
public class CommentService {
	
	@Autowired
	protected CommentRepository commentRepository;
	
	@Transactional
	public List<Comment> getComments(Task task) {
		return this.commentRepository.findByTask(task);
	}
	
	
	@Transactional
	public void saveComment(Comment comment) {
		this.commentRepository.save(comment);
	}
	
	@Transactional
	public void deleteComment(Comment comment) {
		this.commentRepository.delete(comment);
	}
	

}

package it.uniroma3.siw.taskmanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.validation.CommentValidator;
import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.service.CommentService;
import it.uniroma3.siw.taskmanager.service.TaskService;

@Controller
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	CommentValidator commentValidator;
	
	@Autowired
	TaskService taskService;
	
	@RequestMapping(value="/task/addComment/{id}", method=RequestMethod.GET)
	public String showCommentForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("commentForm", new Comment());
		model.addAttribute("task", taskService.getTask(id));
		return "commentForm";
	}
	
	@RequestMapping(value="/task/addComment/{id}", method=RequestMethod.POST)
	public String createComment(@PathVariable("id") Long id, @Valid @ModelAttribute("commentForm") Comment comment, BindingResult commentBindingResult, Model model) {
		commentValidator.validate(comment, commentBindingResult);
		if(!commentBindingResult.hasErrors()) {
			Task task = taskService.getTask(id);
			task.addComment(comment);
			comment.setTask(task);
			commentService.saveComment(comment);
			return "commentCreationSuccessful";
		}
		return "commentForm";
	}
	

}

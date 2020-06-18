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

import it.uniroma3.siw.taskmanager.controller.validation.TagValidator;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.service.TagService;
import it.uniroma3.siw.taskmanager.service.TaskService;

@Controller
public class TagController {
	
	@Autowired
	TagService tagService;
	
	@Autowired
	TagValidator tagValidator;
	
	@Autowired
	TaskService taskService;
	
	@RequestMapping(value="/task/viewTags/{id}", method=RequestMethod.GET)
	public String viewTags(@PathVariable("id") Long id, Model model) {
		model.addAttribute("task", taskService.getTask(id));
		model.addAttribute("tags", taskService.getTask(id).getAllTags());
		return "viewTags";
	}
	
	@RequestMapping(value="/task/createTag/{id}", method=RequestMethod.GET)
	public String showTagCreation(@PathVariable("id") Long id, Model model) {
		model.addAttribute("tag", new Tag());
		model.addAttribute("task", this.taskService.getTask(id));
		return "tagForm";
	}
	
	@RequestMapping(value="/task/createTag/{id}", method=RequestMethod.POST)
	public String createTag(@Valid @ModelAttribute("tag") Tag tag, BindingResult tagBindingResult,@PathVariable("id") Long id, Model model) {
		this.tagValidator.validate(tag, tagBindingResult);
		if(!tagBindingResult.hasErrors()) {
			Task task = taskService.getTask(id);
			task.addTag(tag);
			tag.addTask(task);
			tagService.saveTag(tag);
			return "tagCreationSuccessful";
		}
		return "tagForm";
	}
}

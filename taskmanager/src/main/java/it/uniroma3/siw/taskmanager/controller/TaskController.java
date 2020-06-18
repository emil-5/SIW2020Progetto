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
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.TaskValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TaskService;
import it.uniroma3.siw.taskmanager.service.UserService;
import it.uniroma3.siw.taskmanager.service.CommentService;
import it.uniroma3.siw.taskmanager.service.CredentialsService;

@Controller
public class TaskController {
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TaskValidator taskValidator;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	CredentialsService credentialsService;
	
	@RequestMapping(value="/project/createTask/{id}", method=RequestMethod.GET)
	public String showTaskForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("task", new Task());
		model.addAttribute("project", projectService.getProject(id));
		return "taskForm";
	}
	
	@RequestMapping(value="/project/createTask/{id}", method=RequestMethod.POST)
	public String createTask( @Valid @ModelAttribute("task") Task task, @PathVariable("id") Long id, BindingResult bindingResult, Model model) {
		this.taskValidator.validate(task, bindingResult);
		if(!bindingResult.hasErrors()) {
			User falseUser = new User("USER", "NOT SET");
			task.setUser(falseUser);
			userService.saveUser(falseUser);
			Project project = projectService.getProject(id);
			task.setProject(project);
			taskService.saveTask(task);
            return "taskCreationSuccessful";
		}
		return "taskForm";
	}
	
	@RequestMapping(value="/project/viewTasks/{id}", method=RequestMethod.GET)
	public String viewTasks(@PathVariable("id") Long id, Model model) {
		model.addAttribute("tasks", taskService.getTask(projectService.getProject(id)));
		return "viewTasks";
	}
	
    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public String getTask(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("task", taskService.getTask(id));
    	model.addAttribute("comments", commentService.getComments(taskService.getTask(id)));
    	model.addAttribute("user", taskService.getTask(id).getUser());
    	return "task.html";
    }
    
    @RequestMapping(value="/task/confirmDeleteTask/{id}", method = RequestMethod.GET)
	public String getTaskToDelete(@PathVariable("id") Long id, Model model) {
		model.addAttribute("task", taskService.getTask(id));
		return "confirmDeleteTask";
	}
    
    @RequestMapping(value = "/task/taskDeleteSuccessful/{id}", method = RequestMethod.GET)
	public String deleteTask(@PathVariable("id") Long id, Model model) {
    	Project project = taskService.getTask(id).getProject();
    	Task task = taskService.getTask(id);
    	project.getTasks().remove(task);
        taskService.deleteTask(task);
        projectService.saveProject(project);
		return "taskDeleteSuccessful";
   }
    
    @RequestMapping(value = "/task/modifyTask/{id}", method = RequestMethod.POST)
	public String modifyTask(@PathVariable("id") Long id,
			 @Valid @ModelAttribute("newtask") Task newtask, BindingResult newTaskBindingResult, Model model,
			                        @RequestParam("scales")String[] checkboxValue) {
		this.taskValidator.validate(newtask, newTaskBindingResult);
		if(!newTaskBindingResult.hasErrors()) {
			Boolean completed= false;
			if(checkboxValue!=null) {
				completed = true;
			}
			taskService.getTask(id).setName(newtask.getName());
			taskService.getTask(id).setDescription(newtask.getDescription());
			taskService.getTask(id).setCompleted(completed);
			taskService.saveTask(taskService.getTask(id));
			return "taskModificationSuccessful";
		}
		return "modifyTask";
    }
    
    @RequestMapping(value ="/task/modifyTask/{id}", method = RequestMethod.GET)
    public String getTaskToModify(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("task", this.taskService.getTask(id));
    	model.addAttribute("newtask", new Task());
    	return "modifyTask";
    }
    
    @RequestMapping(value="/task/addUser/{id}", method = RequestMethod.GET)
    public String addUserForm(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("task", this.taskService.getTask(id));
    	model.addAttribute("user", new Credentials());
    	return "addUserForm";
    }
    
    @RequestMapping(value="/task/addUser/{id}", method=RequestMethod.POST)
    public String addUser(@PathVariable("id") Long id, @ModelAttribute("user") Credentials credentials, Model model) {
    	Credentials userToAddCredentials = credentialsService.getCredentials(credentials.getUserName());
    	User userToAdd = userToAddCredentials.getUser();
    	if(!userToAdd.equals(null)) {
    		Task task = taskService.getTask(id);
    		task.setUser(userToAdd);
    		taskService.saveTask(task);
    		return "taskAssignedToUser";
    	}
    	return "addUserForm";
    }
    
	@RequestMapping(value="/project/viewTasksForMembers/{id}", method=RequestMethod.GET)
	public String viewTasksForMembers(@PathVariable("id") Long id, Model model) {
		model.addAttribute("tasks", taskService.getTask(projectService.getProject(id)));
		return "viewTasksForMembers";
	}
	
    @RequestMapping(value = "/viewTaskForMembers/{id}", method = RequestMethod.GET)
    public String getTaskForMembers(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("task", taskService.getTask(id));
    	model.addAttribute("comments", commentService.getComments(taskService.getTask(id)));
    	model.addAttribute("user", taskService.getTask(id).getUser());
    	return "viewTaskForMembers";
    }

}

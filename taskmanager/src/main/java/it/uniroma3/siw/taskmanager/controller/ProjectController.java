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

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.ProjectValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class ProjectController {
	
	@Autowired
	ProjectService projectService;
	@Autowired
	ProjectValidator projectValidator;
    @Autowired
    SessionData sessionData;
    @Autowired
    UserService userService;
    @Autowired
    CredentialsService credentialsService;

	
	@RequestMapping(value = { "/users/createProject" }, method = RequestMethod.POST)
	public String createNewProject(@Valid @ModelAttribute("project") Project project,
            BindingResult projectBindingResult,Model model) {
		User loggedUser = sessionData.getLoggedUser();
		this.projectValidator.validate(project, projectBindingResult);
		if(!projectBindingResult.hasErrors()) {
			project.setOwner(loggedUser);
			loggedUser.addOwnedProject(project);
			this.projectService.saveProject(project);
			return "projectCreationSuccessful";
		}
		return "projectForm";
	}
	
    @RequestMapping(value = { "/users/createProject" }, method = RequestMethod.GET)
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "projectForm";
    }
    
    @RequestMapping(value = { "/users/ownedProjects" }, method = RequestMethod.GET)
    public String showCreatedProjects(Model model) {
    	User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("projects", this.userService.getUser(loggedUser.getId()).getOwnedProjects());
        return "ownedProjects";
    }

    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public String getProject(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("project", this.projectService.getProject(id));
    	return "project.html";
    }
    
    @RequestMapping(value ="/project/modifyProject/{id}", method = RequestMethod.GET)
    public String getProjectToModify(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("project", this.projectService.getProject(id));
    	model.addAttribute("newproject", new Project());
    	return "modifyProject";
    }
    
    @RequestMapping(value = "/project/modifyProject/{id}", method = RequestMethod.POST)
	public String modifyProject(@PathVariable("id") Long id,
			 @Valid @ModelAttribute("newproject") Project newproject, BindingResult newprojectBindingResult, Model model) {
		this.projectValidator.validate(newproject, newprojectBindingResult);
		if(!newprojectBindingResult.hasErrors()) {
			this.projectService.getProject(id).setName(newproject.getName());
			this.projectService.getProject(id).setDescription(newproject.getDescription());
			this.projectService.saveProject(this.projectService.getProject(id));
			return "projectModificationSuccessful";
		}
		return "modifyProject";
    }
    
    @RequestMapping(value="/project/confirmDeleteProject/{id}", method = RequestMethod.GET)
    	public String getProjectToDelete(@PathVariable("id") Long id, Model model) {
    		model.addAttribute("project", this.projectService.getProject(id));
    		return "confirmDeleteProject";
    	}
    
    @RequestMapping(value = "/project/projectDeleteSuccessful/{id}", method = RequestMethod.GET)
	public String deleteProject(@PathVariable("id") Long id, Model model) {
        this.projectService.deleteProject(this.projectService.getProject(id));
		return "projectDeleteSuccessful";
   }
    
    @RequestMapping(value = "/project/shareProject/{id}", method = RequestMethod.GET)
    public String shareProjectToUser(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("project", projectService.getProject(id));
    	model.addAttribute("user", new Credentials());
    	return "shareToUser";
    }
    
    @RequestMapping(value = "/project/shareProject/{id}", method = RequestMethod.POST)
    public String shareProject(@ModelAttribute("user") Credentials user, @PathVariable("id") Long id, Model model) {
    	User toAdd = credentialsService.getCredentials(user.getUserName()).getUser();
    	if(!(toAdd==null)) {
    		Project project = projectService.getProject(id);
    		project.addMember(toAdd);
    		toAdd.addProject(project);
    		userService.saveUser(toAdd);
    		return "shareSuccessful";
    	}
    	return "shareToUser";
    }
    
    @RequestMapping(value="/viewProjectForMembers/{id}", method = RequestMethod.GET)
    public String viewProjectForMembers(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("project", projectService.getProject(id));
    	return "viewProjectForMembers";
    }
    
    @RequestMapping(value="/projectForAdmin/{id}", method = RequestMethod.GET)
    public String viewProjectForAdmin(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("project", projectService.getProject(id));
    	return "projectForAdmin";
    }
    
    
    

}

package it.uniroma3.siw.taskmanager.controller;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.CredentialsValidator;
import it.uniroma3.siw.taskmanager.controller.validation.UserValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.UserRepository;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.UserService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserService userService;

    @Autowired
    UserValidator userValidator;
    
    @Autowired
    CredentialsValidator credentialsValidator;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SessionData sessionData;
    
    @Autowired
    CredentialsService credentialsService;
    
    @Autowired
    ProjectService projectService;

    @RequestMapping(value = { "/home" }, method = RequestMethod.GET)
    public String home(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "home";
    }
    
    @RequestMapping(value = { "/users/me" }, method = RequestMethod.GET)
    public String me(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        Credentials credentials = sessionData.getLoggedCredentials();
        System.out.println(credentials.getPassword());
        model.addAttribute("user", loggedUser);
        model.addAttribute("credentials", credentials);

        return "userProfile";
    }

    @RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
    public String admin(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "admin";
    }
    
    @RequestMapping(value = "/users/modifyUser", method = RequestMethod.POST)
	public String modifyUser(@Valid @ModelAttribute("newuser") User newuser, BindingResult newUserBindingResult,
			                 @Valid @ModelAttribute("newcredentials") Credentials newcredentials, BindingResult newCredentialsBindingResult,Model model) {
		this.userValidator.validate(newuser, newUserBindingResult);
		this.credentialsValidator.validate(newcredentials, newCredentialsBindingResult);
		if(!newUserBindingResult.hasErrors() && !newCredentialsBindingResult.hasErrors()) {
			User user = this.sessionData.getLoggedUser();
			Credentials credentials = this.sessionData.getLoggedCredentials();
			user.setFirstName(newuser.getFirstName());
			user.setLastName(newuser.getLastName());
			credentials.setUserName(newcredentials.getUserName());
			credentials.setPassword(newcredentials.getPassword());
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			return "userModificationSuccessful";
		}
		return "modifyUser";
    }
    
    @RequestMapping(value ="/users/modifyUser", method = RequestMethod.GET)
    public String getUserToModify(Model model) {
    	model.addAttribute("newuser", new User());
    	model.addAttribute("user", this.sessionData.getLoggedUser());
    	model.addAttribute("newcredentials", new Credentials());
    	model.addAttribute("credentials", this.sessionData.getLoggedCredentials());
    	return "modifyUser";
    }
    
    @RequestMapping(value = "/users/sharedProjects", method = RequestMethod.GET)
    public String viewSharedProjects(Model model) {
    	model.addAttribute("projects", this.projectService.getVisibleProjects(this.sessionData.getLoggedUser()));
    	return "sharedProjects";
    }
    
    @RequestMapping(value = "/admin/viewAllUsersAdmin", method = RequestMethod.GET)
    public String viewAllUsersAdmin(Model model) {
    	model.addAttribute("users", userService.getAllUsers());
    	return "viewAllUsersAdmin";
    }
    
    @RequestMapping(value = "/admin/confirmDeleteUserAdmin/{id}", method = RequestMethod.GET)
    public String getUserToDeleteAdmin(@PathVariable ("id") Long id, Model model) {
    	model.addAttribute("user", userService.getUser(id));
    	return "confirmDeleteUserAdmin";
    }
    
    @RequestMapping(value = "/admin/userDeleteSuccessfulAdmin/{id}", method = RequestMethod.GET)
	public String deleteProject(@PathVariable("id") Long id, Model model) {
    	credentialsService.deleteCredentials(credentialsService.getCredentials(userService.getUser(id)));
		return "userDeleteSuccessfulAdmin";
   }
    
    @RequestMapping(value = "/admin/userProfileAdmin/{id}", method = RequestMethod.GET)
    public String viewUserAdmin(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("user", userService.getUser(id));
    	model.addAttribute("projects", userService.getUser(id).getOwnedProjects());
    	return "userProfileAdmin";
    }
    
    @RequestMapping(value = "/admin/searchByUsername", method = RequestMethod.GET)
    public String searchUserAdmin(Model model) {
    	model.addAttribute("credentials", new Credentials());
    	return "searchUserAdmin";
    }
    
    @RequestMapping(value = "/admin/searchByUsername", method = RequestMethod.POST)
    public String getUserByUsername(@ModelAttribute("credentials") Credentials credentials, Model model) {
    	Credentials toDelete = credentialsService.getCredentials(credentials.getUserName());
    	if (toDelete.equals(null)) {
    		return "userNotFound";
    	}
    	Long id = toDelete.getUser().getId();
    	String userpage = new String("redirect:/admin/userProfileAdmin/" + id);
    	return userpage;
    }
    
    

}

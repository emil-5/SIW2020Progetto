package it.uniroma3.siw.taskmanager.controller.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.UserService;

@Component
public class ProjectValidator implements Validator{
	
	final Integer MIN_NAME_LENGTH = 4;
	final Integer MAX_NAME_LENGTH = 20;
	final Integer MIN_DESCRIPTION_LENGTH = 1;
	final Integer MAX_DESCRIPTION_LENGTH = 200;
	
	@Autowired
	UserService userService;
	
	@Override
	public void validate(Object o, Errors errors) {
		Project project = (Project) o;
		String name = project.getName();
		String description = project.getDescription();
        if (name.isBlank())
            errors.rejectValue("name", "required");
        else if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
            errors.rejectValue("name", "size");

        if (description.isBlank())
            errors.rejectValue("lastName", "required");
        else if (description.length() < MIN_DESCRIPTION_LENGTH || description.length() > MAX_DESCRIPTION_LENGTH)
            errors.rejectValue("description", "size");
    }
		
    @Override
    public boolean supports(Class<?> clas) {
        return User.class.equals(clas);
    }


}

package it.uniroma3.siw.taskmanager.controller.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.TagService;

@Component
public class TagValidator implements Validator{
	
	final Integer MIN_NAME_LENGTH = 4;
	final Integer MAX_NAME_LENGTH = 40;
	
	@Autowired
	TagService tagService;
	
    @Override
    public void validate(Object o, Errors errors) {
        Tag tag= (Tag) o;
        String name = tag.getName().trim();

        if (name.isBlank())
            errors.rejectValue("name", "required");
        else if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
            errors.rejectValue("name", "size");

    }
    
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

}

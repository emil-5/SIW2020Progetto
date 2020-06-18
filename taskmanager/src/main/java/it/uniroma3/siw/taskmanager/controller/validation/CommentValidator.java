package it.uniroma3.siw.taskmanager.controller.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CommentService;

@Component
public class CommentValidator implements Validator{
	
	final Integer MIN_COMMENT_LENGTH = 4;
	final Integer MAX_COMMENT_LENGTH = 40;
	
	@Autowired
	CommentService commentService;
	
    @Override
    public void validate(Object o, Errors errors) {
        Comment comment= (Comment) o;
        String comm = comment.getComment().trim();

        if (comm.isBlank())
            errors.rejectValue("comm", "required");
        else if (comm.length() < MIN_COMMENT_LENGTH || comm.length() > MAX_COMMENT_LENGTH)
            errors.rejectValue("comm", "size");

    }
    
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

}
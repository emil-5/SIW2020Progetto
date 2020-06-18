package it.uniroma3.siw.taskmanager.service;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The ProjectService handles logic for Projects.
 * This mainly consists in CRUD operations, as well as sharing projects with other users.
 */
@Service
public class ProjectService {

    @Autowired
    protected ProjectRepository projectRepository;
    
    @Transactional
    public Project getProject(long id) {
        Optional<Project> result = this.projectRepository.findById(id);
        return result.orElse(null);
    }
    
    @Transactional
    public Project saveProject(Project project) {
        return this.projectRepository.save(project);
    }
    
    @Transactional
    public void deleteProject(Project project) {
        this.projectRepository.delete(project);
    }

    @Transactional
    public Project shareProjectWithUser(Project project, User user) {
        project.addMember(user);
        return this.projectRepository.save(project);
    }
    
    @Transactional
    public void addTask(Project project, Task task) {
    	project.addTask(task);
    	this.projectRepository.save(project);
    }
    
    @Transactional
    public List<Project> getVisibleProjects(User user) {
    	return this.projectRepository.findByMembers(user);
    }
    

    
}

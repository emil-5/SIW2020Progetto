package it.uniroma3.siw.taskmanager.repository;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends CrudRepository<Project, Long> {

	public Optional<Project> findById(Long id);

    public List<Project> findByOwner(User owner);
    
    public List<Project> findByMembers(User member);
}


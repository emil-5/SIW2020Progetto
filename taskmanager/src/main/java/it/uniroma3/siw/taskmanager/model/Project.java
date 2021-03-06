package it.uniroma3.siw.taskmanager.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> members;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="project")
    private List<Task> tasks;

    public Project() {
        this.members = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public Project(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public void addMember(User user) {
        if (!this.members.contains(user))
            this.members.add(user);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {

        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(name, project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

	public void addTask(Task task) {
		this.tasks.add(task);
	}
}

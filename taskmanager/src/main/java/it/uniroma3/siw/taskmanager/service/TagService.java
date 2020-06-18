package it.uniroma3.siw.taskmanager.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired
	protected TagRepository tagRepository;
	
	@Transactional
	public Tag getTag(Long id) {
		Optional<Tag> result = this.tagRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Tag getTag(String name) {
		Optional<Tag> result = this.tagRepository.findByName(name);
		return result.orElse(null);
	}
	
	@Transactional
	public void saveTag(Tag tag) {
		this.tagRepository.save(tag);
	}
	
	@Transactional
	public void deleteTag(Tag tag) {
		this.tagRepository.delete(tag);
	}
	
	@Transactional
	public void addTask(Tag tag, Task task) {
		tag.addTask(task);
		this.tagRepository.save(tag);
	}

	

}

package com.example.coderlab.service;

import com.example.coderlab.entity.Level;
import com.example.coderlab.entity.Tag;
import com.example.coderlab.repository.TagRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    public List<Tag> getListTag(){
        return tagRepository.findAll();
    }
    public Tag getTagById(Long id){
        return tagRepository.findById(id).orElseThrow();
    }
    @PostConstruct
    public void initTags() {
        if (tagRepository.count() == 0) {

            Tag sort = new Tag();
            sort.setName("Sort");
            tagRepository.save(sort);

            Tag search = new Tag();
            search.setName("Search");
            tagRepository.save(search);

            Tag recursion = new Tag();
            recursion.setName("Recursion");
            tagRepository.save(recursion);

            Tag graph= new Tag();
            graph.setName("Graph Theory");
            tagRepository.save(graph);
        }
    }
}

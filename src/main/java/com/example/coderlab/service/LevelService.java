package com.example.coderlab.service;

import com.example.coderlab.entity.Level;
import com.example.coderlab.repository.LevelRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelService {
    @Autowired
    private LevelRepository levelRepository;
    public List<Level> getListLevel(){
        return levelRepository.findAll();
    }
    public Level getLevelById(Long id){
        return levelRepository.findById(id).orElseThrow();
    }
    @PostConstruct
    public void initLevels() {
        if (levelRepository.count() == 0) {
            Level easy = new Level();
            easy.setName("Easy");
            levelRepository.save(easy);

            Level medium = new Level();
            medium.setName("Medium");
            levelRepository.save(medium);

            Level hard = new Level();
            hard.setName("Hard");
            levelRepository.save(hard);
        }
    }
}

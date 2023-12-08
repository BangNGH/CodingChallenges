package com.example.coderlab.service;

import com.example.coderlab.entity.Language;
import com.example.coderlab.entity.Role;
import com.example.coderlab.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepository;

    public List<Language> getAllLanguages(){
        return languageRepository.findAll();
    }

    public Optional<Language> findByLanguageID(Long languageID) {
        return languageRepository.findById(languageID);

    }

    public void addLanguage() {
        if (this.getAllLanguages().isEmpty()) {
            Language java_language = new Language();
            Language csharp_language = new Language();
            Language python_language = new Language();
            Language cpp_language = new Language();

            java_language.setName("Java");
            csharp_language.setName("C#");
            python_language.setName("Python");
            cpp_language.setName("C++");

            languageRepository.save(java_language);
            languageRepository.save(csharp_language);
            languageRepository.save(python_language);
            languageRepository.save(cpp_language);
            System.out.println("Added 3 languages");

        }
    }

    public void save(Language foundLanguage) {
        languageRepository.save(foundLanguage);
    }
}

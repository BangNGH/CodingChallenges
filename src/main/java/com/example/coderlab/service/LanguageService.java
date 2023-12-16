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
            java_language.setValue("text/x-java");
            java_language.setDescription(" Java là một ngôn ngữ miễn phí và linh hoạt được sử dụng rộng rãi!");

            csharp_language.setName("C#");
            csharp_language.setDescription("C# là một ngôn ngữ lập trình hướng đối tượng đa năng, là khởi đầu cho .NET");
            csharp_language.setValue("text/x-csharp");

            python_language.setName("Python");
            python_language.setValue("text/x-python");
            python_language.setDescription("Python là một ngôn ngữ lập trình bậc cao cho các mục đích lập trình đa năng!");

            cpp_language.setName("C++");
            cpp_language.setValue("text/x-c++src");
            cpp_language.setDescription("C++ là một ngôn ngữ lập trình bậc cao   là phần mở rộng của ngôn ngữ C!");

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

    public Optional<Language> findLanguageByValue(String languageValue) {
        return languageRepository.findByValue(languageValue);
    }
}

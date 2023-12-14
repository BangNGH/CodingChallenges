package com.example.coderlab.dto;

import com.example.coderlab.entity.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LanguagePercentageDTO {
    Language language;
    String percent;


    @Override
    public String toString() {
        return "LanguagePercentageDTO{" +
                "language=" + language +
                ", percent='" + percent + '\'' +
                '}';
    }
}

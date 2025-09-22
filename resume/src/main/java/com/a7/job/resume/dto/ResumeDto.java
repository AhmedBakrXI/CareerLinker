package com.a7.job.resume.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDto {
    private String name;
    private String email;
    private String phone;
    @JsonProperty("years_of_experience")
    private String yearsOfExperience;
    private List<String> skills;
    private List<EducationDto> educations;
    private List<ExperienceDto> experiences;
}

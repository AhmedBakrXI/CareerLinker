package com.a7.job.resume.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "resumes")
public class ResumeModel {
    @Id
    private ObjectId id;
    private String name;
    private String email;
    private String phone;
    private String yearsOfExperience;
    private List<String> skills;
    private List<EducationModel> educations;
    private List<ExperienceModel> experiences;
}


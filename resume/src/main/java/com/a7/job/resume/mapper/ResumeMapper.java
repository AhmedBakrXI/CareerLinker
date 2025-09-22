package com.a7.job.resume.mapper;

import com.a7.job.resume.dto.EducationDto;
import com.a7.job.resume.dto.ExperienceDto;
import com.a7.job.resume.dto.ResumeDto;
import com.a7.job.resume.model.EducationModel;
import com.a7.job.resume.model.ExperienceModel;
import com.a7.job.resume.model.ResumeModel;

public class ResumeMapper {
    public static ResumeModel mapResumeDtoToModel(ResumeDto resumeDto) {
        return ResumeModel.builder()
                .name(resumeDto.getName())
                .email(resumeDto.getEmail())
                .phone(resumeDto.getPhone())
                .yearsOfExperience(resumeDto.getYearsOfExperience())
                .skills(resumeDto.getSkills())
                .educations(resumeDto.getEducations().stream().map(ResumeMapper::mapEducationDtoToModel).toList())
                .experiences(resumeDto.getExperiences().stream().map(ResumeMapper::mapExperienceDtoToModel).toList())
                .build();
    }

    public static ExperienceModel mapExperienceDtoToModel(ExperienceDto experienceDto) {
        return ExperienceModel.builder()
                .title(experienceDto.getTitle())
                .company(experienceDto.getCompany())
                .duration(experienceDto.getDuration())
                .description(experienceDto.getDescription())
                .build();
    }

    public static EducationModel mapEducationDtoToModel(EducationDto educationDto) {
        return EducationModel.builder()
                .degree(educationDto.getDegree())
                .institution(educationDto.getInstitution())
                .year(educationDto.getYear())
                .build();
    }

    public static ResumeDto mapResumeModelToDto(ResumeModel savedModel) {
        return ResumeDto.builder()
                .name(savedModel.getName())
                .email(savedModel.getEmail())
                .phone(savedModel.getPhone())
                .yearsOfExperience(savedModel.getYearsOfExperience())
                .skills(savedModel.getSkills())
                .educations(savedModel.getEducations().stream().map(ResumeMapper::mapEducationModelToDto).toList())
                .experiences(savedModel.getExperiences().stream().map(ResumeMapper::mapExperienceModelToDto).toList())
                .build();
    }

    private static ExperienceDto mapExperienceModelToDto(ExperienceModel experienceModel) {
        return ExperienceDto.builder()
                .title(experienceModel.getTitle())
                .company(experienceModel.getCompany())
                .duration(experienceModel.getDuration())
                .description(experienceModel.getDescription())
                .build();
    }

    private static EducationDto mapEducationModelToDto(EducationModel educationModel) {
        return EducationDto.builder()
                .degree(educationModel.getDegree())
                .institution(educationModel.getInstitution())
                .year(educationModel.getYear())
                .build();
    }
}

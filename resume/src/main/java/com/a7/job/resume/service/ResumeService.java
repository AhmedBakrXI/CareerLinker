package com.a7.job.resume.service;

import com.a7.job.resume.dto.ResumeDto;
import com.a7.job.resume.mapper.ResumeMapper;
import com.a7.job.resume.model.ResumeModel;
import com.a7.job.resume.repository.ResumeRepository;
import org.apache.tika.Tika;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ResumeService {
    private final Logger logger = Logger.getLogger(ResumeService.class.getName());

    private final ChatClient chatClient;

    private final ResumeRepository resumeRepository;

    private final static String RESUME_SYSTEM_PROMPT = """
            You are a precise resume parsing AI. Extract the following information and return ONLY valid JSON:
            
            REQUIRED JSON STRUCTURE:
            {
                "name": "string (full name)",
                "email": "string (email address)",
                "phone": "string (phone number)",
                "skills": ["string", "string", ...],
                "years_of_experience": "string (total years of experience)",
                "educations": [
                    {
                        "degree": "string (degree name)",
                        "institution": "string (school/university)",
                        "year": "string (graduation year or dates)"
                    }
                ],
                "experiences": [
                    {
                        "title": "string (job title)",
                        "company": "string (company name)",
                        "duration": "string (employment period)",
                        "description": "string (role description)"
                    }
                ]
            }
            
            SPECIFIC INSTRUCTIONS:
            1. Field names are CASE-SENSITIVE: use "educations" and "experiences" (plural)
            2. Split skills by commas into individual array items
            3. For education: extract degree, institution, and year/date range
            4. For experience: include title, company, duration, and a concise description
            5. If information is missing, use empty values: "" for strings, [] for arrays
            6. NEVER add comments, explanations, or text outside the JSON object
            7. Ensure the output is valid JSON that can be parsed by Jackson
            
            EXAMPLE OF EXPECTED OUTPUT:
            {
                "name": "John Doe",
                "email": "john@email.com",
                "phone": "+1234567890",
                "skills": ["Java", "Spring Boot", "Python"],
                "years_of_experience": "5",
                "educations": [
                    {
                        "degree": "Bachelor of Computer Science",
                        "institution": "University of Example",
                        "year": "2023"
                    }
                ],
                "experiences": [
                    {
                        "title": "Software Engineer",
                        "company": "Tech Corp",
                        "duration": "2022-2023",
                        "description": "Developed web applications using Spring Boot"
                    }
                ]
            }
            
            CRITICAL FORMATTING RULES:
            - The output must be pure, valid JSON only. Do not include any additional text.
            - Use double quotes (") for all strings and field names.
            - Maintain the exact field names and structure as specified above.
            - Ensure adding all skills, experiences, and educations as arrays, even if empty. Do not omit any fields or reduce the list size.
            - Validate the JSON structure strictly to ensure compatibility with Jackson parsing.
            
            TAKE THIS SERIOUSLY:
            - Do not include any comments (like // or /* */), explanations, or text inside or outside the JSON structure.
            """;

    @Autowired
    public ResumeService(ChatClient chatClient, ResumeRepository resumeRepository) {
        this.chatClient = chatClient;
        this.resumeRepository = resumeRepository;
    }

    public Optional<ResumeDto> parseResume(MultipartFile resumeFile) {
        Optional<String> fileContentOpt = parseResumeToString(resumeFile);
        if (fileContentOpt.isEmpty()) {
            return Optional.empty();
        }
        String fileContent = fileContentOpt.get();
        try {
            ResumeDto resumeDto = chatClient.prompt()
                    .system(RESUME_SYSTEM_PROMPT)
                    .user(fileContent)
                    .call()
                    .entity(ResumeDto.class);
            if (resumeDto == null) {
                return Optional.empty();
            }
            return Optional.of(resumeDto);
        } catch (Exception e) {
            logger.warning(e.getMessage());
            return Optional.empty();
        }
    }

    public ResumeDto createResume(ResumeDto resumeDto) {
        ResumeModel resumeModel = ResumeMapper.mapResumeDtoToModel(resumeDto);
        ResumeModel savedModel = resumeRepository.save(resumeModel);
        return ResumeMapper.mapResumeModelToDto(savedModel);
    }

    public boolean deleteResume(String email) {
        Optional<ResumeModel> existingResumeOpt = resumeRepository.findByEmail(email);
        if (existingResumeOpt.isEmpty()) {
            return false;
        }
        resumeRepository.delete(existingResumeOpt.get());
        return true;
    }

    public ResumeDto updateResume(Long resumeId, ResumeDto resumeDto) {
        Optional<ResumeModel> existingResumeOpt = resumeRepository.findById(resumeId);
        if (existingResumeOpt.isEmpty()) {
            throw new RuntimeException("Resume not found for ID: " + resumeId);
        }
        ResumeModel existingResume = existingResumeOpt.get();
        existingResume.setName(resumeDto.getName());
        existingResume.setEmail(resumeDto.getEmail());
        existingResume.setPhone(resumeDto.getPhone());
        existingResume.setYearsOfExperience(resumeDto.getYearsOfExperience());
        existingResume.setSkills(resumeDto.getSkills());
        existingResume.setEducations(resumeDto.getEducations().stream()
                .map(ResumeMapper::mapEducationDtoToModel).toList());
        existingResume.setExperiences(resumeDto.getExperiences().stream()
                .map(ResumeMapper::mapExperienceDtoToModel).toList());
        ResumeModel updatedModel = resumeRepository.save(existingResume);
        return ResumeMapper.mapResumeModelToDto(updatedModel);
    }

    private Optional<String> parseResumeToString(MultipartFile resumeFile) {
        String fileType = resumeFile.getContentType();
        if (fileType == null ||
                !(fileType.equals("application/pdf") || fileType.equals("application/msword"))) {
            return Optional.empty();
        }
        try (InputStream is = resumeFile.getInputStream()) {
            Tika tika = new Tika();
            String fileContent = tika.parseToString(is);
            return Optional.of(fileContent);
        } catch (Exception e) {
            logger.warning(e.getMessage());
            return Optional.empty();
        }
    }




}

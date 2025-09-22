package com.a7.job.resume.repository;

import com.a7.job.resume.model.ResumeModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResumeRepository extends MongoRepository<ResumeModel, Long> {
    Optional<ResumeModel> findByEmail(String email);
}

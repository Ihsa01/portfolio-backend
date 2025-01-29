package com.example.demo.services;


import com.example.demo.entity.Education;
import com.example.demo.repository.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EducationService {

    @Autowired
    private EducationRepository educationRepository;

    public List<Education> getAllEducation() {
        return educationRepository.findAll();
    }

    public Education createEducation(Education education) {
        return educationRepository.save(education);
    }

    public Optional<Education> updateEducation(UUID id, Education educationDetail) {
        Optional<Education> existingEdu = educationRepository.findById(id);
        if (existingEdu.isPresent()) {
            Education education = existingEdu.get();
            education.setStd(educationDetail.getStd());
            education.setSchool(educationDetail.getSchool());
            educationRepository.save(education);
        }
        return existingEdu;
    }
    public boolean deleteEducation(UUID id) {
        if (educationRepository.existsById(id)) {
            educationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Education> getEducationById(UUID id) {

           return educationRepository.findById(id);


    }
}

package com.example.demo.services;

import com.example.demo.entity.Skills;
import com.example.demo.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public List<Skills> getAllSkills() {
        return skillRepository.findAll();
    }

    public Optional<Skills> getSkillById(UUID id) {
        return skillRepository.findById(id);
    }

    public Skills createSkill(Skills skill) {
        return skillRepository.save(skill);
    }

    public Optional<Skills> updateSkill(UUID id, Skills skillDetails) {
        Optional<Skills> existingSkill = skillRepository.findById(id);
        if (existingSkill.isPresent()) {
            Skills skill = existingSkill.get();
            skill.setName(skillDetails.getName());
            skill.setImage(skillDetails.getImage());
            skillRepository.save(skill);
        }
        return existingSkill;
    }

    public boolean deleteSkill(UUID id) {
        if (skillRepository.existsById(id)) {
            skillRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

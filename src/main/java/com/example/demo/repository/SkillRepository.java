package com.example.demo.repository;
import com.example.demo.entity.Skills;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skills, UUID> {
}
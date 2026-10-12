package com.medicalexplainer.repository;

import com.medicalexplainer.entity.MedicalReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<MedicalReport, Long> {
    List<MedicalReport> findByUserIdOrderByUploadDateDesc(Long userId);
    Optional<MedicalReport> findByIdAndUserId(Long id, Long userId);
    long countByUserId(Long userId);
}

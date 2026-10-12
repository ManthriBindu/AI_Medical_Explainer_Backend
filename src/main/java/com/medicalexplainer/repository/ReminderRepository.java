package com.medicalexplainer.repository;

import com.medicalexplainer.entity.MedicationReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<MedicationReminder, Long> {
    List<MedicationReminder> findByMedicineId(Long medicineId);

    @Query("SELECT r FROM MedicationReminder r JOIN r.medicine m WHERE m.user.id = :userId ORDER BY r.reminderTime ASC")
    List<MedicationReminder> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM MedicationReminder r JOIN r.medicine m WHERE m.user.id = :userId AND r.status = 'Upcoming'")
    long countUpcomingByUserId(@Param("userId") Long userId);
}

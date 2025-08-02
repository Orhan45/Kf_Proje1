package com.example.demo.repository;

import com.example.demo.entity.EgmStateInformation; // Import güncellendi
import com.example.demo.entity.EgmStateInformationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EgmStateInformationRepository extends JpaRepository<EgmStateInformation, EgmStateInformationId> { // Tip güncellendi

    List<EgmStateInformation> findByProductLineId(Long productLineId); // Metot imzası güncellendi

    void deleteByProductLineId(Long productLineId);
}
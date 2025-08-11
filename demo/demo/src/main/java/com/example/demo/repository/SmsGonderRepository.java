package com.example.demo.repository;

import com.example.demo.entity.SmsGonder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsGonderRepository extends JpaRepository<SmsGonder, Long> {
}
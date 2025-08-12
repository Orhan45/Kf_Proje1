package com.example.demo.controller;

import com.example.demo.service.SmsGonderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class SmsGonderController {

    private final SmsGonderService smsGonderService;

    @GetMapping("/records")
    public List<Map<String, Object>> getSmsRecords(
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate) {
        return smsGonderService.getSmsRecordsByPhoneAndDate(phoneNumber, startDate, endDate);
    }
}

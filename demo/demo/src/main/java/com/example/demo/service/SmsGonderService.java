package com.example.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SmsGonderService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String, Object>> getSmsRecordsByPhoneAndDate(String phoneNumber, String smsKod, LocalDate startDate, LocalDate endDate) {
        String baseQuery = buildUnionQuery();

        // This is a new helper method to generate all possible phone number formats
        List<String> phoneNumbersToSearch = preparePhoneNumbersForSearch(phoneNumber);

        String filteredQuery = applyFilters(baseQuery, phoneNumbersToSearch, smsKod, startDate, endDate);

        Query query = entityManager.createNativeQuery(filteredQuery);

        // Bind parameters
        if (!phoneNumbersToSearch.isEmpty()) {
            query.setParameter("phoneNumbers", phoneNumbersToSearch);
        }
        if (smsKod != null && !smsKod.isEmpty()) {
            query.setParameter("smsKod", smsKod);
        }
        if (startDate != null) {
            query.setParameter("startDate", Date.valueOf(startDate));
        }
        if (endDate != null) {
            query.setParameter("endDate", Date.valueOf(endDate));
        }

        @SuppressWarnings("unchecked")
        List<Object[]> results = (List<Object[]>) query.getResultList();

        List<Map<String, Object>> dtoList = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("phoneNumber", row[0]);
            map.put("messageBody", row[1]);
            map.put("insertDate", row[2]);
            map.put("smsKod", row[3]);
            map.put("gonderilenProg", row[4]);
            map.put("kaynakTablo", row[5]);
            dtoList.add(map);
        }
        return dtoList;
    }

    private String buildUnionQuery() {
        return "SELECT PHONE_NUMBER, MESSAGE_BODY, INSERT_DATE, SMS_KOD, GONDERILEN_PROG, 'SMS_GONDER' AS KAYNAK_TABLO FROM SMS_GONDER " +
                "UNION ALL " +
                "SELECT PHONE_NUMBER, MESSAGE_BODY, INSERT_DATE, SMS_KOD, GONDERILEN_PROG, 'SMS_GONDER_HIZLIPAHALI' AS KAYNAK_TABLO FROM SMS_GONDER_HIZLIPAHALI " +
                "UNION ALL " +
                "SELECT PHONE_NUMBER, MESSAGE_BODY, INSERT_DATE, SMS_KOD, GONDERILEN_PROG, 'SMS_GONDER_ARA' AS KAYNAK_TABLO FROM SMS_GONDER_ARA " +
                "UNION ALL " +
                "SELECT PHONE_NUMBER, MESSAGE_BODY, INSERT_DATE, SMS_KOD, GONDERILEN_PROG, 'SMS_GONDER_ESKI' AS KAYNAK_TABLO FROM SMS_GONDER_ESKI";
    }

    private String applyFilters(String unionQuery, List<String> phoneNumbers, String smsKod, LocalDate startDate, LocalDate endDate) {
        StringBuilder sql = new StringBuilder("SELECT * FROM (")
                .append(unionQuery)
                .append(") t WHERE 1=1 ");

        // Updated filter logic for phone numbers
        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
            sql.append("AND PHONE_NUMBER IN (:phoneNumbers) ");
        }
        if (smsKod != null && !smsKod.isEmpty()) {
            sql.append("AND SMS_KOD = :smsKod ");
        }
        if (startDate != null && endDate != null) {
            sql.append("AND INSERT_DATE BETWEEN :startDate AND :endDate ");
        } else if (startDate != null) {
            sql.append("AND INSERT_DATE >= :startDate ");
        } else if (endDate != null) {
            sql.append("AND INSERT_DATE <= :endDate ");
        }
        sql.append("ORDER BY INSERT_DATE DESC");
        return sql.toString();
    }

    /**
     * Helper method to generate a list of all possible phone number formats.
     * It handles a number entered as 05..., 5..., or +905...
     */
    private List<String> preparePhoneNumbersForSearch(String phoneNumber) {
        List<String> formats = new ArrayList<>();
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return formats;
        }

        // Remove all non-digit characters
        String cleanedNumber = phoneNumber.replaceAll("[^0-9]", "");

        if (cleanedNumber.isEmpty()) {
            return formats;
        }

        // Base case: If number starts with 0 and is 11 digits, or is 10 digits
        if (cleanedNumber.length() == 11 && cleanedNumber.startsWith("0")) {
            formats.add(cleanedNumber); // e.g., 05123456789
            formats.add(cleanedNumber.substring(1)); // e.g., 5123456789
            formats.add("+90" + cleanedNumber.substring(1)); // e.g., +905123456789
        } else if (cleanedNumber.length() == 10) {
            formats.add(cleanedNumber); // e.g., 5123456789
            formats.add("0" + cleanedNumber); // e.g., 05123456789
            formats.add("+90" + cleanedNumber); // e.g., +905123456789
        } else if (cleanedNumber.length() == 12 && cleanedNumber.startsWith("90")) {
            formats.add(cleanedNumber); // e.g., 905123456789
            formats.add("0" + cleanedNumber.substring(2)); // e.g., 05123456789
            formats.add("+" + cleanedNumber); // e.g., +905123456789
        } else {
            // For any other format, just use the cleaned number as a fallback
            formats.add(cleanedNumber);
        }
        return formats;
    }
}
package com.example.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SmsGonderService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String, Object>> getSmsRecordsByPhoneAndDate(String phoneNumber, String smsKod, LocalDateTime startDate, LocalDateTime endDate) {
        String baseQuery = buildUnionQuery();
        List<String> phoneNumbersToSearch = preparePhoneNumbersForSearch(phoneNumber);
        String filteredQuery = applyFilters(baseQuery, phoneNumbersToSearch, smsKod, startDate, endDate);
        Query query = entityManager.createNativeQuery(filteredQuery);

        if (phoneNumbersToSearch != null && !phoneNumbersToSearch.isEmpty()) {
            for (int i = 0; i < phoneNumbersToSearch.size(); i++) {
                query.setParameter("phoneNumber" + i, phoneNumbersToSearch.get(i));
            }
        }
        if (smsKod != null && !smsKod.isEmpty()) {
            query.setParameter("smsKod", smsKod);
        }
        if (startDate != null) {
            query.setParameter("startDate", Date.valueOf(startDate.toLocalDate()));
        }
        if (endDate != null) {
            query.setParameter("endDate", Date.valueOf(endDate.toLocalDate()));
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

    private String applyFilters(String unionQuery, List<String> phoneNumbers, String smsKod, LocalDateTime startDate, LocalDateTime endDate) {
        StringBuilder sql = new StringBuilder("SELECT * FROM (")
                .append(unionQuery)
                .append(") t WHERE 1=1 ");
        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
            sql.append("AND (");
            for (int i = 0; i < phoneNumbers.size(); i++) {
                sql.append("PHONE_NUMBER = :phoneNumber").append(i);
                if (i < phoneNumbers.size() - 1) {
                    sql.append(" OR ");
                }
            }
            sql.append(") ");
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

    private List<String> preparePhoneNumbersForSearch(String phoneNumber) {
        List<String> formats = new ArrayList<>();
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return formats;
        }
        String cleanedNumber = phoneNumber.replaceAll("[^0-9]", "");
        if (phoneNumber.startsWith("+90") && cleanedNumber.length() == 12) {
            cleanedNumber = cleanedNumber.substring(2);
        } else if (cleanedNumber.length() == 12 && cleanedNumber.startsWith("90")) {
            cleanedNumber = cleanedNumber.substring(2);
        } else if (cleanedNumber.length() == 11 && cleanedNumber.startsWith("0")) {
            cleanedNumber = cleanedNumber.substring(1);
        }
        formats.add(cleanedNumber);
        formats.add("0" + cleanedNumber);
        formats.add("90" + cleanedNumber);
        formats.add("+90" + cleanedNumber);
        return formats;
    }
}
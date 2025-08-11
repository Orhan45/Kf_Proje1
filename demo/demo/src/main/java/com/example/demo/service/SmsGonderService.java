package com.example.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SmsGonderService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getSmsRecordsByPhoneAndDate(String phoneNumber, LocalDate startDate, LocalDate endDate) {
        String baseQuery = buildUnionQuery();
        String filteredQuery = applyFilters(baseQuery, phoneNumber, startDate, endDate);

        Query query = entityManager.createNativeQuery(filteredQuery);
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            query.setParameter("phoneNumber", phoneNumber);
        }
        if (startDate != null && endDate != null) {
            query.setParameter("startDate", Date.valueOf(startDate));
            query.setParameter("endDate", Date.valueOf(endDate));
        }
        @SuppressWarnings("unchecked")
        List<Object[]> results = (List<Object[]>) query.getResultList();
        return results;
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

    private String applyFilters(String unionQuery, String phoneNumber, LocalDate startDate, LocalDate endDate) {
        StringBuilder sql = new StringBuilder("SELECT * FROM (")
                .append(unionQuery)
                .append(") t WHERE 1=1 ");

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            sql.append("AND PHONE_NUMBER = :phoneNumber ");
        }
        if (startDate != null && endDate != null) {
            sql.append("AND INSERT_DATE BETWEEN :startDate AND :endDate ");
        }
        sql.append("ORDER BY INSERT_DATE DESC");
        return sql.toString();
    }
}
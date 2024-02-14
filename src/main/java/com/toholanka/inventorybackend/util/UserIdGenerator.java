package com.toholanka.inventorybackend.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserIdGenerator implements IdentifierGenerator {

    private static final String PREFIX = "UUID";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        String query = "SELECT u.userId FROM Users u WHERE u.userId LIKE :prefix ORDER BY u.userId DESC";
        List<?> ids = session.createQuery(query)
                .setParameter("prefix", PREFIX + "%")
                .setMaxResults(1)
                .list();

        int nextNumber = 1; // Start from initial value if no IDs found
        if (!ids.isEmpty()) {
            String lastId = (String) ids.get(0);
            Pattern pattern = Pattern.compile(PREFIX + "(\\d+)");
            Matcher matcher = pattern.matcher(lastId);
            if (matcher.find()) {
                nextNumber = Integer.parseInt(matcher.group(1)) + 1;
            }
        }

        return PREFIX + String.format("%03d", nextNumber);
    }
}

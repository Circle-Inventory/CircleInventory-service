package com.toholanka.inventorybackend.util;

import jakarta.persistence.Embeddable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemIdGenerator implements IdentifierGenerator {
    private static final String PREFIX = "ITEM";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        // Define the query to find the highest current item ID with the specified prefix
        String query = "SELECT i.itemId FROM Item i WHERE i.itemId LIKE :prefix ORDER BY i.itemId DESC";
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

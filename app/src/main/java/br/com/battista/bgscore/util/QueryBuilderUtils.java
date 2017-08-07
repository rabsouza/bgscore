package br.com.battista.bgscore.util;

import com.google.common.collect.Maps;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

public class QueryBuilderUtils {

    private static final String PROP_SINGLE_ORDER_BY = "{0} {1}";
    private static final String PROP_SEPARATOR_ORDER_BY = ", ";

    public enum Order {
        DESC, ASC
    }

    private Map<String, Order> mapOrderBy;

    private QueryBuilderUtils() {
        mapOrderBy = Maps.newLinkedHashMap();
    }

    public static QueryBuilderUtils newInstance() {
        return new QueryBuilderUtils();
    }

    public QueryBuilderUtils addPropOrderBy(String prop, Order order) {
        mapOrderBy.put(prop, order);
        return this;
    }

    public String buildOrderBy() {
        StringBuilder orderBy = new StringBuilder();
        if (!mapOrderBy.isEmpty()) {
            Iterator<String> iteratorOrder = mapOrderBy.keySet().iterator();
            while (iteratorOrder.hasNext()) {
                String prop = iteratorOrder.next();
                orderBy.append(MessageFormat.format(PROP_SINGLE_ORDER_BY,
                        prop, mapOrderBy.get(prop)));
                if (iteratorOrder.hasNext()) {
                    orderBy.append(PROP_SEPARATOR_ORDER_BY);
                }
            }
        }
        return orderBy.toString();
    }

}

package br.com.battista.bgscore.util;

import static br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry.COLUMN_NAME_CREATED_AT;
import static br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT;
import static br.com.battista.bgscore.util.QueryBuilderUtils.Order.ASC;
import static br.com.battista.bgscore.util.QueryBuilderUtils.Order.DESC;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import java.text.MessageFormat;

public class QueryBuilderUtilsTest {

    private QueryBuilderUtils builder;

    @Before
    public void setup() {
        builder = QueryBuilderUtils.newInstance();
    }

    @Test
    public void testOrderByShouldReturnEmptyWhenEmptyOrder() {
        assertThat(builder.buildOrderBy(), equalTo(""));
    }

    @Test
    public void testOrderByShouldReturnOrderByWhenAdd1FieldOrderAndOrder() {
        builder.addPropOrderBy(COLUMN_NAME_UPDATED_AT, ASC);

        assertThat(builder.buildOrderBy(), equalTo(
                MessageFormat.format("{0} ASC", COLUMN_NAME_UPDATED_AT)));
    }

    @Test
    public void testOrderByShouldReturnOrderByWhenAdd2FieldOrderAndOrder() {
        builder.addPropOrderBy(COLUMN_NAME_UPDATED_AT, ASC);
        builder.addPropOrderBy(COLUMN_NAME_CREATED_AT, DESC);

        assertThat(builder.buildOrderBy(), equalTo(
                MessageFormat.format("{0} ASC, {1} DESC",
                        COLUMN_NAME_UPDATED_AT, COLUMN_NAME_CREATED_AT)));
    }

}
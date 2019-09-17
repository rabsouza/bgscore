package br.com.battista.bgscore.util

import br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry.COLUMN_NAME_CREATED_AT
import br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT
import br.com.battista.bgscore.util.QueryBuilderUtils.Order.ASC
import br.com.battista.bgscore.util.QueryBuilderUtils.Order.DESC
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat

import org.junit.Before
import org.junit.Test

import java.text.MessageFormat

class QueryBuilderUtilsTest {

    private lateinit var builder: QueryBuilderUtils

    @Before
    fun setup() {
        builder = QueryBuilderUtils.newInstance()
    }

    @Test
    fun testOrderByShouldReturnEmptyWhenEmptyOrder() {
        assertThat(builder.buildOrderBy(), equalTo(""))
    }

    @Test
    fun testOrderByShouldReturnOrderByWhenAdd1FieldOrderAndOrder() {
        builder.addPropOrderBy(COLUMN_NAME_UPDATED_AT, ASC)

        assertThat(builder.buildOrderBy(), equalTo(
                MessageFormat.format("{0} ASC", COLUMN_NAME_UPDATED_AT)))
    }

    @Test
    fun testOrderByShouldReturnOrderByWhenAdd2FieldOrderAndOrder() {
        builder.addPropOrderBy(COLUMN_NAME_UPDATED_AT, ASC)
        builder.addPropOrderBy(COLUMN_NAME_CREATED_AT, DESC)

        assertThat(builder.buildOrderBy(), equalTo(
                MessageFormat.format("{0} ASC, {1} DESC",
                        COLUMN_NAME_UPDATED_AT, COLUMN_NAME_CREATED_AT)))
    }

}
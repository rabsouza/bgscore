package br.com.battista.bgscore.model.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

import br.com.battista.bgscore.constants.EntityConstant;
import br.com.battista.bgscore.util.LogUtils;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderByDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer position = 0;
    private String value;
    private String query;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean isDefaultFilter() {
        return position == 0;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }


    public OrderByDto position(Integer position) {
        this.position = position;
        return this;
    }

    public OrderByDto value(String value) {
        this.value = value;
        return this;
    }

    public OrderByDto query(String query) {
        this.query = query;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderByDto)) return false;
        OrderByDto orderByDto = (OrderByDto) o;
        return Objects.equal(getPosition(), orderByDto.getPosition()) &&
                Objects.equal(getValue(), orderByDto.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPosition(), getValue());
    }

    @Override
    public String toString() {
        if(!LogUtils.isLoggable()){
            return EntityConstant.EMPTY_STRING;
        }

        return MoreObjects.toStringHelper(this)
                .add("position", position)
                .add("defaultFilter", isDefaultFilter())
                .add("value", value)
                .add("query", query)
                .toString();
    }

}

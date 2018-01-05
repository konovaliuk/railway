package com.liashenko.app.persistance.domain;

import com.liashenko.app.persistance.dao.Identified;

import java.io.Serializable;

public class RouteRate implements Serializable, Identified {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Float rate;

    public RouteRate() {
    }

    public RouteRate(Integer id, Float rate) {
        this.id = id;
        this.rate = rate;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteRate)) return false;

        RouteRate routeRate = (RouteRate) o;

        if (id != null ? !id.equals(routeRate.id) : routeRate.id != null) return false;
        return rate != null ? rate.equals(routeRate.rate) : routeRate.rate == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (rate != null ? rate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RouteRate{");
        sb.append("id=").append(id);
        sb.append(", rate=").append(rate);
        sb.append('}');
        return sb.toString();
    }
}

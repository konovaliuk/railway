package com.liashenko.app.persistance.domain;

import com.liashenko.app.persistance.dao.Identified;

import java.io.Serializable;

public class Train implements Serializable, Identified {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String number;
    private Long routeNumId;
    private Integer vagonCount;

    public Train() {
    }

    public Train(Long id, String number, Long routeNumId, Integer vagonCount) {
        this.id = id;
        this.number = number;
        this.routeNumId = routeNumId;
        this.vagonCount = vagonCount;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getRouteNumId() {
        return routeNumId;
    }

    public void setRouteNumId(Long routeNumId) {
        this.routeNumId = routeNumId;
    }

    public Integer getVagonCount() {
        return vagonCount;
    }

    public void setVagonCount(Integer vagonCount) {
        this.vagonCount = vagonCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Train)) return false;

        Train train = (Train) o;

        if (id != null ? !id.equals(train.id) : train.id != null) return false;
        if (number != null ? !number.equals(train.number) : train.number != null) return false;
        if (routeNumId != null ? !routeNumId.equals(train.routeNumId) : train.routeNumId != null) return false;
        return vagonCount != null ? vagonCount.equals(train.vagonCount) : train.vagonCount == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (routeNumId != null ? routeNumId.hashCode() : 0);
        result = 31 * result + (vagonCount != null ? vagonCount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Train{");
        sb.append("id=").append(id);
        sb.append(", number='").append(number).append('\'');
        sb.append(", routeNumId=").append(routeNumId);
        sb.append(", vagonCount=").append(vagonCount);
        sb.append('}');
        return sb.toString();
    }
}

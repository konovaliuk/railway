package com.liashenko.app.persistance.domain;

import com.liashenko.app.persistance.dao.Identified;
import com.liashenko.app.persistance.result_parser.Column;

import java.io.Serializable;

public class RouteNum implements Serializable, Identified {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private Integer number;

    public RouteNum() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteNum)) return false;

        RouteNum routeNum = (RouteNum) o;

        if (id != null ? !id.equals(routeNum.id) : routeNum.id != null) return false;
        return number != null ? number.equals(routeNum.number) : routeNum.number == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RouteNumDao{");
        sb.append("id=").append(id);
        sb.append(", number=").append(number);
        sb.append('}');
        return sb.toString();
    }
}

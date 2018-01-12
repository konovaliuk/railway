package com.liashenko.app.service.dto;

import java.io.Serializable;

public class RouteDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String from;
    private Long fromId;
    private Long toId;
    private String to;
    private String date;

    public RouteDto() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteDto)) return false;

        RouteDto routeDto = (RouteDto) o;

        if (from != null ? !from.equals(routeDto.from) : routeDto.from != null) return false;
        if (fromId != null ? !fromId.equals(routeDto.fromId) : routeDto.fromId != null) return false;
        if (toId != null ? !toId.equals(routeDto.toId) : routeDto.toId != null) return false;
        if (to != null ? !to.equals(routeDto.to) : routeDto.to != null) return false;
        return date != null ? date.equals(routeDto.date) : routeDto.date == null;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (fromId != null ? fromId.hashCode() : 0);
        result = 31 * result + (toId != null ? toId.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RouteDto{");
        sb.append("from='").append(from).append('\'');
        sb.append(", fromId=").append(fromId);
        sb.append(", toId=").append(toId);
        sb.append(", to='").append(to).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

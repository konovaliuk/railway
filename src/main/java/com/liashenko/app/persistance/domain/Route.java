package com.liashenko.app.persistance.domain;

import com.liashenko.app.persistance.dao.Identified;

import java.io.Serializable;

public class Route implements Serializable, Identified {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long stationsOrder;
    private Long stationId;
    private Long routeNumberId;
    private Float distance;

    public Route() {
    }

    public Route(Long id, Long stationsOrder, Long stationId, Long routeNumberId, Float distance) {
        this.id = id;
        this.stationsOrder = stationsOrder;
        this.stationId = stationId;
        this.routeNumberId = routeNumberId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStationsOrder() {
        return stationsOrder;
    }

    public void setStationsOrder(Long stationsOrder) {
        this.stationsOrder = stationsOrder;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getRouteNumberId() {
        return routeNumberId;
    }

    public void setRouteNumberId(Long routeNumberId) {
        this.routeNumberId = routeNumberId;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;

        Route route = (Route) o;

        if (id != null ? !id.equals(route.id) : route.id != null) return false;
        if (stationsOrder != null ? !stationsOrder.equals(route.stationsOrder) : route.stationsOrder != null)
            return false;
        if (stationId != null ? !stationId.equals(route.stationId) : route.stationId != null) return false;
        if (routeNumberId != null ? !routeNumberId.equals(route.routeNumberId) : route.routeNumberId != null)
            return false;
        return distance != null ? distance.equals(route.distance) : route.distance == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (stationsOrder != null ? stationsOrder.hashCode() : 0);
        result = 31 * result + (stationId != null ? stationId.hashCode() : 0);
        result = 31 * result + (routeNumberId != null ? routeNumberId.hashCode() : 0);
        result = 31 * result + (distance != null ? distance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Route{");
        sb.append("id=").append(id);
        sb.append(", stationsOrder=").append(stationsOrder);
        sb.append(", stationId=").append(stationId);
        sb.append(", routeNumberId=").append(routeNumberId);
        sb.append(", distance=").append(distance);
        sb.append('}');
        return sb.toString();
    }
}

package com.liashenko.app.persistance.domain;

import com.liashenko.app.persistance.dao.Identified;

import java.io.Serializable;

public class TrainRate implements Serializable, Identified {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Float rate;
    private Long  trainId;

    public TrainRate() {
    }

    public TrainRate(Long id, Float rate, Long trainId) {
        this.id = id;
        this.rate = rate;
        this.trainId = trainId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainRate)) return false;

        TrainRate trainRate = (TrainRate) o;

        if (id != null ? !id.equals(trainRate.id) : trainRate.id != null) return false;
        if (rate != null ? !rate.equals(trainRate.rate) : trainRate.rate != null) return false;
        return trainId != null ? trainId.equals(trainRate.trainId) : trainRate.trainId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (rate != null ? rate.hashCode() : 0);
        result = 31 * result + (trainId != null ? trainId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TrainRate{");
        sb.append("id=").append(id);
        sb.append(", rate=").append(rate);
        sb.append(", trainId=").append(trainId);
        sb.append('}');
        return sb.toString();
    }
}

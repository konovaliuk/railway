package com.liashenko.app.service;

import com.liashenko.app.service.dto.FullRouteDto;
import com.liashenko.app.service.dto.PriceForVagonDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Optional<String> getTrainNameById(Long trainId);

    Optional<FullRouteDto> getFullTrainRoute(Long routeId);

    Optional<List<PriceForVagonDto>> getPricesForVagons(Long fromStationId, Long toStationId, Long routeId);

    Optional<String> getStationNameById(Long stationId);
}

package com.liashenko.app.service;

import com.liashenko.app.service.dto.BillDto;

import java.util.Optional;

public interface BillService {

    Optional<BillDto> showBill(Long routeId, Long trainId, Long fromStationId, Long toStationId, String trainName,
                               String firstName, String lastName, Integer vagonTypeId, String date);
}

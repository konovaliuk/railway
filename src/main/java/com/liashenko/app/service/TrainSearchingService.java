package com.liashenko.app.service;

import com.liashenko.app.service.dto.AutocompleteDto;
import com.liashenko.app.service.dto.TrainDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainSearchingService {

    Optional<List<AutocompleteDto>> getStationAutocomplete(String stationLike);

    Optional<List<TrainDto>> getTrainsForTheRouteOnDate(Long stationFrom, String stationFromName,  Long stationTo,
                                                        String stationToName, LocalDate date);
}

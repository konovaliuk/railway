package com.liashenko.app.service.implementation;

import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.persistance.dao.*;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.Route;
import com.liashenko.app.persistance.domain.Station;
import com.liashenko.app.persistance.domain.TimeTable;
import com.liashenko.app.persistance.domain.Train;
import com.liashenko.app.service.TrainSearchingService;
import com.liashenko.app.service.data_source.DbConnectService;
import com.liashenko.app.service.dto.AutocompleteDto;
import com.liashenko.app.service.dto.TrainDto;
import com.liashenko.app.service.exceptions.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TrainSearchingServiceImpl implements TrainSearchingService {

    private static final Logger classLogger = LogManager.getLogger(UserProfileServiceImpl.class);

    private static final DbConnectService dbConnectService = DbConnectService.getInstance();
    private static final DaoFactory daoFactory = MySQLDaoFactory.getInstance();

    private ResourceBundle localeQueries;

    public TrainSearchingServiceImpl(ResourceBundle localeQueries) {
        this.localeQueries = localeQueries;
    }

    @Override
    public Optional<List<AutocompleteDto>> getStationAutocomplete(String stationLike) {
        List<AutocompleteDto> autocompleteWordList = new ArrayList<>();
        Connection conn = dbConnectService.getConnection();
        try {
//            conn.setReadOnly(true);
            Optional<GenericJDBCDao> stationDaoOpt = daoFactory.getDao(conn, Station.class, localeQueries);
            StationDao stationDao = (StationDao) stationDaoOpt.orElseThrow(() -> new ServiceException("StationDao is null"));

            stationDao.getStationsLike(stationLike).ifPresent(stations
                    -> stations.forEach(station
                    -> autocompleteWordList.add(new AutocompleteDto(station.getId(), station.getName()))));

        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
        return Optional.of(autocompleteWordList);
    }

    @Override
    public Optional<List<TrainDto>> getTrainsForTheRouteOnDate(Long stationFromId, String stationFromName, Long stationToId,
                                                               String stationToName, LocalDate date) {
        Connection conn = dbConnectService.getConnection();
        List<TrainDto> trainDtoList = null;
        try {
//            conn.setReadOnly(true);
            Optional<GenericJDBCDao> routeDaoOpt = daoFactory.getDao(conn, Route.class, localeQueries);
            RouteDao routeDao = (RouteDao) routeDaoOpt.orElseThrow(() -> new ServiceException("RouteDao is null"));
            Optional<List<Route>> routesOpt = routeDao.getRoutesByDepartureAndArrivalStationsId(stationFromId, stationToId);
            if (routesOpt.isPresent()) {
                trainDtoList = new ArrayList<>();
                List<Route> routs = routesOpt.get();
                for (Route route : routs) {
                    TrainDto trainDto = new TrainDto();
                    setRoutWithFromAndToStationsForTrain(stationFromId, stationFromName, stationToId, stationToName, route, trainDto);
                    setTrainIdAndNumber(conn, route, trainDto);
                    setLeavingAndArrivalDatesForTrain(stationFromId, stationToId, date, conn,
                            trainDtoList, route, trainDto);
                }
            }
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }

        return Optional.ofNullable(trainDtoList);
    }

    private void setTrainIdAndNumber(Connection conn, Route route, TrainDto trainDto) {
        Optional<GenericJDBCDao> trainDaoOpt = daoFactory.getDao(conn, Train.class, localeQueries);
        TrainDao trainDao = (TrainDao) trainDaoOpt.orElseThrow(() -> new ServiceException("TrainDao is null"));
        trainDao.getByRoute(route.getRouteNumberId())
                .ifPresent(train -> {
                    trainDto.setTrainNumber(train.getNumber());
                    trainDto.setTrainId(train.getId());
                });
    }

    private void setRoutWithFromAndToStationsForTrain(Long stationFromId, String stationFromName, Long stationToId, String stationToName, Route route, TrainDto trainDto) {
        trainDto.setStationFromId(stationFromId);
        trainDto.setStationToId(stationToId);
        trainDto.setFromStation(stationFromName);
        trainDto.setToStation(stationToName);
        trainDto.setRouteId(route.getRouteNumberId());
    }

    private void setLeavingAndArrivalDatesForTrain(Long stationFromId, Long stationToId, LocalDate date,
                                                   Connection conn, List<TrainDto> trainDtoList,
                                                   Route route, TrainDto trainDto) {

        Optional<GenericJDBCDao> timeTableDaoOpt = daoFactory.getDao(conn, TimeTable.class, localeQueries);
        TimeTableDao timeTableDao = (TimeTableDao) timeTableDaoOpt
                .orElseThrow(() -> new ServiceException("TimeTableDao is null"));

        timeTableDao.getTimeTableForStationByDataAndRoute(stationFromId, route.getRouteNumberId(), date)
                .ifPresent(stationFromTimeTable
                        -> trainDto.setLeavingDate(
                        HttpParser.convertDateTimeToHumanReadableString(stationFromTimeTable.getDeparture())));

        timeTableDao.getTimeTableForStationByDataAndRoute(stationToId, route.getRouteNumberId(), date)
                .ifPresent(stationToTimeTable
                        -> trainDto.setArrivalDate(
                        HttpParser.convertDateTimeToHumanReadableString(stationToTimeTable.getArrival())));
        trainDtoList.add(trainDto);
    }
}

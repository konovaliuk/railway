package com.liashenko.app.service.implementation;

import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.persistance.dao.*;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.Route;
import com.liashenko.app.persistance.domain.Station;
import com.liashenko.app.persistance.domain.TimeTable;
import com.liashenko.app.persistance.domain.Train;
import com.liashenko.app.service.TrainSearchingService;
import com.liashenko.app.service.dto.AutocompleteDto;
import com.liashenko.app.service.dto.TrainDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.storage_connection.DBConnectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;

public class TrainSearchingServiceImpl implements TrainSearchingService {

    private static final Logger classLogger = LogManager.getLogger(UserProfileServiceImpl.class);

    private DaoFactory daoFactory;
    private com.liashenko.app.service.storage_connection.DBConnectService DBConnectService;
    private ResourceBundle localeQueries;

    public TrainSearchingServiceImpl(ResourceBundle localeQueries) {
        this.localeQueries = localeQueries;
        this.DBConnectService = new DBConnectService();
        this.daoFactory = new MySQLDaoFactory();
    }

    @Override
    public Optional<List<AutocompleteDto>> getStationAutocomplete(String stationLike) {
        List<AutocompleteDto> autocompleteWordList = new ArrayList<>();
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
//            connection.setReadOnly(true);
            Optional<GenericJDBCDao> stationDaoOpt = daoFactory.getDao(connection, Station.class, localeQueries);
            StationDao stationDao = (StationDao) stationDaoOpt.orElseThrow(ServiceException::new);

            Optional<List<Station>> stationsOpt = stationDao.getStationsLike(stationLike);
            stationsOpt.ifPresent(stations -> stations.forEach(station -> autocompleteWordList
                    .add(new AutocompleteDto(station.getId(), station.getName()))));

        } catch (ServiceException | DAOException e) {
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            DBConnectService.close(connection);
        }
        return Optional.ofNullable(autocompleteWordList);
    }

    @Override
    public Optional<List<TrainDto>> getTrainsForTheRouteOnDate(Long stationFromId, String stationFromName,  Long stationToId,
                                                               String stationToName, LocalDate date){
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        List<TrainDto> trainDtoList = null;
        try {
//            connection.setReadOnly(true);
            Optional<GenericJDBCDao> routeDaoOpt = daoFactory.getDao(connection, Route.class, localeQueries);
            RouteDao routeDao = (RouteDao) routeDaoOpt.orElseThrow(ServiceException::new);

            Optional<GenericJDBCDao> timeTableDaoOpt = daoFactory.getDao(connection, TimeTable.class, localeQueries);
            TimeTableDao timeTableDao = (TimeTableDao) timeTableDaoOpt.orElseThrow(ServiceException::new);

            Optional<GenericJDBCDao> trainDaoOpt = daoFactory.getDao(connection, Train.class, localeQueries);
            TrainDao trainDao = (TrainDao) trainDaoOpt.orElseThrow(ServiceException::new);

            Optional<List<Route>> routesOpt = routeDao.getRoutesByDepartureAndArrivalStationsId(stationFromId, stationToId);
            if (routesOpt.isPresent()) {
                trainDtoList = new ArrayList<>();
                List<Route> routs = routesOpt.get();
                for (Route route : routs) {
                    TrainDto trainDto = new TrainDto();
                    trainDto.setStationFromId(stationFromId);
                    trainDto.setStationToId(stationToId);
                    trainDto.setFromStation(stationFromName);
                    trainDto.setToStation(stationToName);
                    trainDto.setRouteId(route.getRouteNumberId());

                    trainDao.getByRoute(route.getRouteNumberId())
                            .ifPresent(train -> {
                                trainDto.setTrainNumber(train.getNumber());
                                trainDto.setTrainId(train.getId());
                            });

                    timeTableDao.getTimeTableForStationByDataAndRoute(stationFromId, route.getRouteNumberId(), date)
                            .ifPresent(stationFromTimeTable
                                    -> trainDto.setLeavingDate(
                                            HttpParser.convertDateTimeToString(stationFromTimeTable.getDeparture())));

                    timeTableDao.getTimeTableForStationByDataAndRoute(stationToId, route.getRouteNumberId(), date)
                            .ifPresent(stationToTimeTable
                                    -> trainDto.setArrivalDate(
                                            HttpParser.convertDateTimeToString(stationToTimeTable.getArrival())));
                    System.out.println("TrainSearchingServiceImpl.getTrainsForTheRouteOnDate " + trainDao);
                    trainDtoList.add(trainDto);
                }
            }
        } catch (ServiceException | DAOException e) {
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            DBConnectService.close(connection);
        }


        return Optional.ofNullable(trainDtoList);
    }
}

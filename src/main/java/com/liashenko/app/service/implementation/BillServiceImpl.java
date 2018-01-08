package com.liashenko.app.service.implementation;

import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.persistance.dao.*;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.*;
import com.liashenko.app.service.BillService;
import com.liashenko.app.service.data_source.DbConnectService;
import com.liashenko.app.service.dto.BillDto;
import com.liashenko.app.service.exceptions.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class BillServiceImpl implements BillService {

    private static final Logger classLogger = LogManager.getLogger(BillServiceImpl.class);

    private static final DbConnectService dbConnectService = DbConnectService.getInstance();
    private static final DaoFactory daoFactory = MySQLDaoFactory.getInstance();

    //make it in AppSettings
    private static final int MAX_TICKET_NUM = 100_000;
    private static final float DEFAULT_ROUTE_RATE = 1.0F;
    private static final float DEFAULT_PRICE_FOR_VAGON_PER_KM = 1.0F;

    private ResourceBundle localeQueries;

    public BillServiceImpl(ResourceBundle localeQueries) {
        this.localeQueries = localeQueries;
    }

    @Override
    public Optional<BillDto> showBill(Long routeId, Long trainId, Long fromStationId, Long toStationId, String trainName,
                                      String firstName, String lastName, Integer vagonTypeId, String date) {
        BillDto billDto = new BillDto();
        Connection conn = dbConnectService.getConnection();
        try {
//          conn.setReadOnly(true);
            setFirstAndLastName(billDto, firstName, lastName);
            setStationsAndCity(billDto, fromStationId, toStationId, conn);
            setTrainNameAndVagonNumber(billDto, trainId, trainName, conn);
            setVagonTypeAndPlace(billDto, vagonTypeId, conn);
            setTicketNumberAndDate(billDto);
            setLeavingAndArrivalDate(billDto, fromStationId, toStationId, date, routeId, conn);
            setPrice(billDto, routeId, conn, fromStationId, toStationId);
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
        return Optional.of(billDto);
    }

    private void setFirstAndLastName(BillDto billDto, String firstName, String lastName) {
        billDto.setFirstName(firstName);
        billDto.setLastName(lastName);
    }

    private void setStationsAndCity(BillDto billDto, Long fromStationId, Long toStationId, Connection connection) {
        billDto.setFromStationId(fromStationId);
        billDto.setToStationId(toStationId);
        Optional<GenericJDBCDao> stationDaoOpt = daoFactory.getDao(connection, Station.class, localeQueries);
        StationDao stationDao = (StationDao) stationDaoOpt.orElseThrow(() -> new ServiceException("StationDao is null"));

        stationDao.getByPK(fromStationId).ifPresent(station -> {
            billDto.setFromCityName(station.getCity());
            billDto.setFromStationName(station.getName());
        });

        stationDao.getByPK(toStationId).ifPresent(station
                -> billDto.setToStationName(station.getName()));
    }

    private void setTrainNameAndVagonNumber(BillDto billDto, Long trainId, String trainName, Connection conn) {
        billDto.setTrainNumber(trainName);
        Optional<GenericJDBCDao> trainDaoOpt = daoFactory.getDao(conn, Train.class, localeQueries);
        TrainDao trainDao = (TrainDao) trainDaoOpt.orElseThrow(() -> new ServiceException("TrainDao is null"));
        trainDao.getByPK(trainId).ifPresent(train -> billDto.setVagonNumber(CalculatorUtil.generateValue(train.getVagonCount())));
    }

    private void setVagonTypeAndPlace(BillDto billDto, Integer vagonId, Connection connection) {
        Optional<GenericJDBCDao> vagonTypeDaoOpt = daoFactory.getDao(connection, VagonType.class, localeQueries);
        VagonTypeDao vagonTypeDao = (VagonTypeDao) vagonTypeDaoOpt
                .orElseThrow(() -> new ServiceException("VagonTypeDao is null"));

        vagonTypeDao.getByPK(vagonId).ifPresent(vagonType -> {
            billDto.setVagonTypeName(vagonType.getTypeName());
            billDto.setVagonTypeId(vagonType.getId());
            billDto.setPlaceNumber(CalculatorUtil.generateValue(vagonType.getPlacesCount()));
        });
    }

    private void setTicketNumberAndDate(BillDto billDto) {
        billDto.setTicketNumber(String.format("%06d", CalculatorUtil.generateValue(MAX_TICKET_NUM)));
        billDto.setTicketDate(HttpParser.convertDateToHumanReadableString(LocalDateTime.now().toLocalDate()));
    }

    private void setLeavingAndArrivalDate(BillDto billDto, Long fromStationId, Long toStationId, String date, Long routeId,
                                          Connection conn) {

        Optional<GenericJDBCDao> timeTableDaoOpt = daoFactory.getDao(conn, TimeTable.class, localeQueries);
        TimeTableDao timeTableDao = (TimeTableDao) timeTableDaoOpt.orElseThrow(()
                -> new ServiceException("TimeTableDao is null"));

        timeTableDao.getTimeTableForStationByDataAndRoute(fromStationId, routeId, date)
                .ifPresent(stationFromTimeTable
                        -> billDto.setFromStationLeavingDate(
                        HttpParser.convertDateTimeToHumanReadableString (stationFromTimeTable.getDeparture())));

        timeTableDao.getTimeTableForStationByDataAndRoute(toStationId, routeId, date)
                .ifPresent(stationToTimeTable
                        -> billDto.setToStationArrivalDate(
                        HttpParser.convertDateTimeToHumanReadableString(stationToTimeTable.getArrival())));
    }

    private void setPrice(BillDto billDto, Long routeId, Connection conn, Long fromStationId, Long toStationId) {
        Optional<GenericJDBCDao> routeDaoOpt = daoFactory.getDao(conn, Route.class, localeQueries);
        RouteDao routeDao = (RouteDao) routeDaoOpt.orElseThrow(() -> new ServiceException("RouteDao is null"));

        Float distance = CalculatorUtil.calculateDistanceFromStationToStationOnTheRoute(routeDao, routeId,
                fromStationId, toStationId);

        Optional<GenericJDBCDao> routeRateDaoOpt = daoFactory.getDao(conn, RouteRate.class, localeQueries);
        RouteRateDao routeRateDao = (RouteRateDao) routeRateDaoOpt.orElseThrow(() -> new ServiceException("RouteRateDao is null"));

        Optional<RouteRate> routeRateOpt = routeRateDao.getByRouteId(routeId);
        Float routeRateFloat = routeRateOpt.isPresent() ? routeRateOpt.get().getRate() : DEFAULT_ROUTE_RATE;

        Optional<GenericJDBCDao> vagonTypeDaoOpt = daoFactory.getDao(conn, VagonType.class, localeQueries);
        VagonTypeDao vagonTypeDao = (VagonTypeDao) vagonTypeDaoOpt.orElseThrow(() -> new ServiceException("VagonTypeDao is null"));
        Integer placesCount = vagonTypeDao.getByPK(billDto.getVagonTypeId())
                .orElseThrow(() -> new ServiceException("VagonType is null")).getPlacesCount();

        Optional<GenericJDBCDao> pricePerKmForVagonDaoOpt = daoFactory.getDao(conn, PricePerKmForVagon.class, localeQueries);
        PricePerKmForVagonDao pricePerKmForVagonDao = (PricePerKmForVagonDao) pricePerKmForVagonDaoOpt
                .orElseThrow(() -> new ServiceException("PricePerKmForVagonDao is null"));

        Optional<PricePerKmForVagon> pricePerKmForVagonOpt = pricePerKmForVagonDao.getPricePerKmForVagon(billDto.getVagonTypeId());

        Double pricePerKmForVagonDouble = pricePerKmForVagonOpt.isPresent() ? pricePerKmForVagonOpt.get().getPrice()
                : DEFAULT_PRICE_FOR_VAGON_PER_KM;
        Float price = CalculatorUtil.calculateTicketPrice(routeRateFloat, distance, pricePerKmForVagonDouble, placesCount);
        billDto.setTicketPrice(price);
    }
}

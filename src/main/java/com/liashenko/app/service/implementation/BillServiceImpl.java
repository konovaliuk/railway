package com.liashenko.app.service.implementation;

import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.persistance.dao.*;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.*;
import com.liashenko.app.service.BillService;
import com.liashenko.app.service.dto.BillDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.storage_connection.DBConnectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class BillServiceImpl implements BillService {

    private static final Logger classLogger = LogManager.getLogger(BillServiceImpl.class);

    //make it in AppSettings
    private static final int SIGNS_AFTER_ZERO = 2;
    private static final float DEFAULT_ROUTE_RATE = 1.0F;
    private static final float DEFAULT_PRICE_FOR_VAGON_PER_KM = 1.0F;

    private DaoFactory daoFactory;
    private DBConnectService DBConnectService;
    private ResourceBundle localeQueries;
    private static final Random RANDOM = new Random();

    public BillServiceImpl(ResourceBundle localeQueries) {
        this.localeQueries = localeQueries;
        this.DBConnectService = new DBConnectService();
        this.daoFactory = new MySQLDaoFactory();
    }

    @Override
    public Optional<BillDto> showBill(Long routeId, Long trainId, Long fromStationId, Long toStationId, String trainName,
                                      String firstName, String lastName, Integer vagonTypeId, String date) {
        BillDto billDto = new BillDto();
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
//          connection.setReadOnly(true);
        setFirstAndLastName(billDto, firstName, lastName);
        setStationsAndCity(billDto, fromStationId, toStationId, connection);
        setTrainNameAndVagonNumber(billDto, trainId, trainName, connection);
        setVagonTypeAndPlace(billDto, vagonTypeId, connection);
        setTicketNumberAndDate(billDto);
        setLeavingAndArrivalDate(billDto, fromStationId, toStationId, date, routeId,  connection);
        setPrice(billDto, routeId, connection);

    } catch (ServiceException | DAOException e) {
        classLogger.error("Operation wasn't successful", e);
        throw new ServiceException("Operation wasn't successful");
    } finally {
        DBConnectService.close(connection);
    }
        return Optional.of(billDto);
    }

    private void setFirstAndLastName(BillDto billDto, String firstName, String lastName){
        billDto.setFirstName(firstName);
        billDto.setLastName(lastName);
    }

    private void setStationsAndCity(BillDto billDto, Long fromStationId, Long toStationId, Connection connection){

        billDto.setFromStationId(fromStationId);
        billDto.setToStationId(toStationId);
        Optional<GenericJDBCDao> stationDaoOpt = daoFactory.getDao(connection, Station.class, localeQueries);
        StationDao stationDao = (StationDao) stationDaoOpt.orElseThrow(ServiceException::new);

        stationDao.getByPK(fromStationId).ifPresent(station -> {
            billDto.setFromCityName(station.getCity());
            billDto.setFromStationName(station.getName());
        });

        stationDao.getByPK(toStationId).ifPresent(station
                -> billDto.setToStationName(station.getName()));
    }

    private void setTrainNameAndVagonNumber(BillDto billDto, Long trainId, String trainName, Connection connection){
        billDto.setTrainNumber(trainName);
        Optional<GenericJDBCDao> trainDaoOpt = daoFactory.getDao(connection, Train.class, localeQueries);
        TrainDao trainDao = (TrainDao) trainDaoOpt.orElseThrow(ServiceException::new);

        trainDao.getByPK(trainId).ifPresent(train -> billDto.setVagonNumber(generateValue(train.getVagonCount())));
    }

    private void setVagonTypeAndPlace(BillDto billDto, Integer vagonId, Connection connection){
        Optional<GenericJDBCDao> vagonTypeDaoOpt = daoFactory.getDao(connection, VagonType.class, localeQueries);
        VagonTypeDao vagonTypeDao = (VagonTypeDao) vagonTypeDaoOpt.orElseThrow(ServiceException::new);

        vagonTypeDao.getByPK(vagonId).ifPresent(vagonType -> {
            billDto.setVagonTypeName(vagonType.getTypeName());
            billDto.setVagonTypeId(vagonType.getId());
            billDto.setPlaceNumber(generateValue(vagonType.getPlacesCount()));
        });
    }

    private void setTicketNumberAndDate(BillDto billDto){
        billDto.setTicketNumber(String.valueOf(generateValue(100_000)));
        billDto.setTicketDate(LocalDateTime.now());
    }

    private void setLeavingAndArrivalDate(BillDto billDto, Long fromStationId, Long toStationId, String date, Long routeId, Connection connection){

        Optional<GenericJDBCDao> timeTableDaoOpt = daoFactory.getDao(connection, TimeTable.class, localeQueries);
        TimeTableDao timeTableDao = (TimeTableDao) timeTableDaoOpt.orElseThrow(ServiceException::new);

        timeTableDao.getTimeTableForStationByDataAndRoute(fromStationId, routeId, date)
                .ifPresent(stationFromTimeTable
                        -> billDto.setFromStationLeavingDate(HttpParser.convertDateTimeToString(stationFromTimeTable.getDeparture())));

        timeTableDao.getTimeTableForStationByDataAndRoute(toStationId, routeId, date)
                .ifPresent(stationToTimeTable
                        -> billDto.setToStationArrivalDate(HttpParser.convertDateTimeToString(stationToTimeTable.getArrival())));
    }

    private void setPrice(BillDto billDto, Long routeId, Connection connection){
        Optional<GenericJDBCDao> routeDaoOpt = daoFactory.getDao(connection, Route.class, localeQueries);
        RouteDao routeDao = (RouteDao) routeDaoOpt.orElseThrow(ServiceException::new);

        Optional<Route> fromStation = routeDao.getStationOnRoute(billDto.getFromStationId(), routeId);
        Optional<Route> toStation = routeDao.getStationOnRoute(billDto.getToStationId(), routeId);

        Float distance;
        if (fromStation.isPresent() && toStation.isPresent()){
            distance = toStation.get().getDistance() - fromStation.get().getDistance();
            if (distance <= 0){
                throw new ServiceException();
            }
        } else {
            throw new ServiceException();
        }
        Optional<GenericJDBCDao> routeRateDaoOpt = daoFactory.getDao(connection, RouteRate.class, localeQueries);
        RouteRateDao routeRateDao = (RouteRateDao) routeRateDaoOpt.orElseThrow(ServiceException::new);

        Optional<RouteRate> routeRateOpt =  routeRateDao.getByRouteId(routeId);
        Float routeRateFloat = routeRateOpt.isPresent() ? routeRateOpt.get().getRate() : DEFAULT_ROUTE_RATE;

        Optional<GenericJDBCDao> vagonTypeDaoOpt = daoFactory.getDao(connection, VagonType.class, localeQueries);
        VagonTypeDao vagonTypeDao = (VagonTypeDao) vagonTypeDaoOpt.orElseThrow(ServiceException::new);
        Integer placesCount = vagonTypeDao.getByPK(billDto.getVagonTypeId())
                .orElseThrow(() -> new ServiceException())
                .getPlacesCount();

//            MathContext mathCtxt = new MathContext(2, RoundingMode.HALF_UP);

        Optional<GenericJDBCDao> pricePerKmForVagonDaoOpt = daoFactory.getDao(connection, PricePerKmForVagon.class, localeQueries);
        PricePerKmForVagonDao pricePerKmForVagonDao = (PricePerKmForVagonDao) pricePerKmForVagonDaoOpt.orElseThrow(ServiceException::new);

        Optional<PricePerKmForVagon> pricePerKmForVagonOpt = pricePerKmForVagonDao.getPricePerKmForVagon(billDto.getVagonTypeId());

        Double pricePerKmForVagonDouble = pricePerKmForVagonOpt.isPresent() ? pricePerKmForVagonOpt.get().getPrice() : DEFAULT_PRICE_FOR_VAGON_PER_KM;

        Float price = BigDecimal.valueOf(routeRateFloat)
                .multiply(BigDecimal.valueOf(distance))
                .multiply(BigDecimal.valueOf(pricePerKmForVagonDouble))
                .divide(BigDecimal.valueOf(placesCount), SIGNS_AFTER_ZERO, BigDecimal.ROUND_HALF_UP)
//                        .round(mathCtxt)
                .floatValue();
        billDto.setTicketPrice(price);
    }

    private static int generateValue(int maxValueCount){
        int res = 0;
        do {
            res = RANDOM.nextInt(maxValueCount);
        }
        while (res < 1);
        return res;
    }
}

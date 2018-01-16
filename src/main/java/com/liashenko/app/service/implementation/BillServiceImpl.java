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
import com.liashenko.app.utils.AppProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;
import java.util.ResourceBundle;

public class BillServiceImpl implements BillService {

    private static final Logger classLogger = LogManager.getLogger(BillServiceImpl.class);

    private static final DbConnectService dbConnectService = DbConnectService.getInstance();
    private static final DaoFactory daoFactory = MySQLDaoFactory.getInstance();

    private ResourceBundle localeQueries;

    public BillServiceImpl(ResourceBundle localeQueries) {
        this.localeQueries = localeQueries;
    }

    @Override
    public Optional<BillDto> showBill(Long routeId, Long trainId, Long fromStationId, Long toStationId, String trainName,
                                      String firstName, String lastName, Integer vagonTypeId, String date) {
        LocalDate localDate = HttpParser.convertStringToDate(date);
        BillDto billDto = new BillDto();
        Connection conn = dbConnectService.getConnection();
        try {
//          conn.setReadOnly(true);
            setFirstAndLastName(billDto, firstName, lastName);
            setStationsAndCity(billDto, fromStationId, toStationId, conn);
            setTrainNameAndVagonNumber(billDto, trainId, trainName, conn);
            setVagonTypeAndPlace(billDto, vagonTypeId, conn);
            setTicketNumberAndDate(billDto);
            setLeavingAndArrivalDate(billDto, fromStationId, toStationId, localDate, routeId, conn);
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
        billDto.setTicketNumber(String.format("%06d", CalculatorUtil.generateValue(AppProperties.getMaxTicketNumber())));
        billDto.setTicketDate(HttpParser.convertDateToHumanReadableString(LocalDateTime.now().toLocalDate()));
    }

    private void setLeavingAndArrivalDate(BillDto billDto, Long fromStationId, Long toStationId, LocalDate requiredDate,
                                          Long routeId, Connection conn) {

        Optional<GenericJDBCDao> timeTableDaoOpt = daoFactory.getDao(conn, TimeTable.class, localeQueries);
        TimeTableDao timeTableDao = (TimeTableDao) timeTableDaoOpt.orElseThrow(()
                -> new ServiceException("TimeTableDao is null"));

        Optional<TimeTable> leavingTimeTableOpt = timeTableDao
                .getTimeTableForStationByDataAndRoute(fromStationId, routeId, requiredDate);
        Optional<TimeTable> arrivalTimeTableOpt = timeTableDao
                .getTimeTableForStationByDataAndRoute(toStationId, routeId, requiredDate);

        if (arrivalTimeTableOpt.isPresent() && leavingTimeTableOpt.isPresent()) {
            LocalDateTime leavingTime = leavingTimeTableOpt.get().getDeparture();
            LocalDateTime arrivalTime = arrivalTimeTableOpt.get().getArrival();

            LocalDateTime leavingTimeForRequiredDate = leavingTime
                    .withDayOfMonth(requiredDate.getDayOfMonth())
                    .withMonth(requiredDate.getMonthValue())
                    .withYear(requiredDate.getYear());

            LocalDateTime arrivalTimeForRequiredDate = leavingTimeForRequiredDate
                    .plusDays(Period.between(leavingTime.toLocalDate(), arrivalTime.toLocalDate()).getDays())
                    .withHour(arrivalTime.getHour())
                    .withMinute(arrivalTime.getMinute())
                    .withSecond(arrivalTime.getSecond());

            billDto.setFromStationLeavingDate(HttpParser.convertDateTimeToHumanReadableString(leavingTimeForRequiredDate));
            billDto.setToStationArrivalDate(HttpParser.convertDateTimeToHumanReadableString(arrivalTimeForRequiredDate));
        }
    }

    private void setPrice(BillDto billDto, Long routeId, Connection conn, Long fromStationId, Long toStationId) {
        Optional<GenericJDBCDao> routeDaoOpt = daoFactory.getDao(conn, Route.class, localeQueries);
        RouteDao routeDao = (RouteDao) routeDaoOpt.orElseThrow(() -> new ServiceException("RouteDao is null"));

        Float distance = CalculatorUtil.calculateDistanceFromStationToStationOnTheRoute(routeDao, routeId,
                fromStationId, toStationId);

        Optional<GenericJDBCDao> routeRateDaoOpt = daoFactory.getDao(conn, RouteRate.class, localeQueries);
        RouteRateDao routeRateDao = (RouteRateDao) routeRateDaoOpt.orElseThrow(() -> new ServiceException("RouteRateDao is null"));

        Optional<RouteRate> routeRateOpt = routeRateDao.getByRouteId(routeId);
        Float routeRateFloat = routeRateOpt.isPresent() ? routeRateOpt.get().getRate() : AppProperties.getDefRouteRate();

        Optional<GenericJDBCDao> vagonTypeDaoOpt = daoFactory.getDao(conn, VagonType.class, localeQueries);
        VagonTypeDao vagonTypeDao = (VagonTypeDao) vagonTypeDaoOpt.orElseThrow(() -> new ServiceException("VagonTypeDao is null"));
        Integer placesCount = vagonTypeDao.getByPK(billDto.getVagonTypeId())
                .orElseThrow(() -> new ServiceException("VagonType is null")).getPlacesCount();

        Optional<GenericJDBCDao> pricePerKmForVagonDaoOpt = daoFactory.getDao(conn, PricePerKmForVagon.class, localeQueries);
        PricePerKmForVagonDao pricePerKmForVagonDao = (PricePerKmForVagonDao) pricePerKmForVagonDaoOpt
                .orElseThrow(() -> new ServiceException("PricePerKmForVagonDao is null"));

        Optional<PricePerKmForVagon> pricePerKmForVagonOpt = pricePerKmForVagonDao.getPricePerKmForVagon(billDto.getVagonTypeId());

        Double pricePerKmForVagonDouble = pricePerKmForVagonOpt.isPresent() ? pricePerKmForVagonOpt.get().getPrice()
                : AppProperties.getDefPriceForVagonKm();
        Float price = CalculatorUtil.calculateTicketPrice(routeRateFloat, distance, pricePerKmForVagonDouble, placesCount);
        billDto.setTicketPrice(price);
    }
}

package com.liashenko.app.service.implementation;

import com.liashenko.app.persistance.dao.*;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.*;
import com.liashenko.app.service.OrderService;
import com.liashenko.app.service.dto.FullRouteDto;
import com.liashenko.app.service.dto.PriceForVagonDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.storage_connection.DBConnectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderServiceImpl implements OrderService {

    private static final Logger classLogger = LogManager.getLogger(OrderServiceImpl.class);

    //make it in AppSettings
    private static final int SIGNS_AFTER_ZERO = 2;
    private static final float DEFAULT_ROUTE_RATE = 1.0F;
    private static final float DEFAULT_PRICE_FOR_VAGON_PER_KM = 1.0F;

    private DaoFactory daoFactory;
    private DBConnectService DBConnectService;
    private ResourceBundle localeQueries;

    public OrderServiceImpl(ResourceBundle localeQueries) {
        this.localeQueries = localeQueries;
        this.DBConnectService = new DBConnectService();
        this.daoFactory = new MySQLDaoFactory();
    }

    @Override
    public Optional<FullRouteDto> getFullTrainRoute(Long routeId) {
        FullRouteDto fullRouteDto = new FullRouteDto();
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
//            connection.setReadOnly(true);
            Optional<GenericJDBCDao> routeDaoOpt = daoFactory.getDao(connection, Route.class, localeQueries);
            RouteDao routeDao = (RouteDao) routeDaoOpt.orElseThrow(ServiceException::new);

            Optional<Route> routeFirstStationOpt = routeDao.getFirstTerminalStationOnRoute(routeId);
            Optional<Route> routeLastStationOpt = routeDao.getLastTerminalStationOnRoute(routeId);
            Long routeFirstStationId = routeFirstStationOpt.orElseThrow(ServiceException::new).getStationId();
            Long routeLastStationId = routeLastStationOpt.orElseThrow(ServiceException::new).getStationId();

            Optional<GenericJDBCDao> stationDaoOpt = daoFactory.getDao(connection, Station.class, localeQueries);
            StationDao stationDao = (StationDao) stationDaoOpt.orElseThrow(ServiceException::new);

            stationDao.getByPK(routeFirstStationId).ifPresent(station
                    -> fullRouteDto.setFirstTerminalStation(station.getCity()));

            stationDao.getByPK(routeLastStationId).ifPresent(station
                    -> fullRouteDto.setLastTerminalStation(station.getCity()));
        } catch (ServiceException | DAOException e) {
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            DBConnectService.close(connection);
        }
        return Optional.of(fullRouteDto);
    }

    @Override
    public Optional<List<PriceForVagonDto>> getPrices(Long fromStationId, Long toStationId, Long routeId) {
        List<PriceForVagonDto> pricesForVagonList = new ArrayList<>();

        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
            Optional<GenericJDBCDao> routeDaoOpt = daoFactory.getDao(connection, Route.class, localeQueries);
            RouteDao routeDao = (RouteDao) routeDaoOpt.orElseThrow(ServiceException::new);

            Optional<Route> fromStation = routeDao.getStationOnRoute(fromStationId, routeId);
            Optional<Route> toStation = routeDao.getStationOnRoute(toStationId, routeId);

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

            Optional<List<VagonType>> vagonTypeList = vagonTypeDao.getAll();
//            MathContext mathCtxt = new MathContext(2, RoundingMode.HALF_UP);
            vagonTypeList.ifPresent(vagonTypes -> vagonTypes.forEach(vagonType -> {
                PriceForVagonDto priceForVagonDto = new PriceForVagonDto();

                Optional<GenericJDBCDao> pricePerKmForVagonDaoOpt = daoFactory.getDao(connection, PricePerKmForVagon.class, localeQueries);
                PricePerKmForVagonDao pricePerKmForVagonDao = (PricePerKmForVagonDao) pricePerKmForVagonDaoOpt.orElseThrow(ServiceException::new);

                Optional<PricePerKmForVagon> pricePerKmForVagonOpt = pricePerKmForVagonDao.getPricePerKmForVagon(vagonType.getId());

                Double pricePerKmForVagonDouble = pricePerKmForVagonOpt.isPresent() ? pricePerKmForVagonOpt.get().getPrice() : DEFAULT_PRICE_FOR_VAGON_PER_KM;

                Float price = BigDecimal.valueOf(routeRateFloat)
                        .multiply(BigDecimal.valueOf(distance))
                        .multiply(BigDecimal.valueOf(pricePerKmForVagonDouble))
                        .divide(BigDecimal.valueOf(vagonType.getPlacesCount()), SIGNS_AFTER_ZERO, BigDecimal.ROUND_HALF_UP)
//                        .round(mathCtxt)
                        .floatValue();

                priceForVagonDto.setVagonTypeId(vagonType.getId());
                priceForVagonDto.setVagonTypeName(vagonType.getTypeName());
                priceForVagonDto.setDistance(distance);
                priceForVagonDto.setTicketPrice(price);
                pricesForVagonList.add(priceForVagonDto);
            }));

        } catch (ServiceException | DAOException e) {
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            DBConnectService.close(connection);
        }
        return Optional.of(pricesForVagonList);
    }

    @Override
    public Optional<String> getTrainNameById(Long trainId) {
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
//            connection.setReadOnly(true);
            Optional<GenericJDBCDao> trainDaoOpt = daoFactory.getDao(connection, Train.class, localeQueries);
            TrainDao trainDao = (TrainDao) trainDaoOpt.orElseThrow(ServiceException::new);
            Optional<Train> trainOpt = trainDao.getByPK(trainId);
            if (trainOpt.isPresent()){
                return Optional.ofNullable(trainOpt.get().getNumber());
            }
        } catch (ServiceException | DAOException e) {
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            DBConnectService.close(connection);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getStationNameById(Long stationId) {
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
//            connection.setReadOnly(true);
            Optional<GenericJDBCDao> stationDaoOpt = daoFactory.getDao(connection, Station.class, localeQueries);
            StationDao stationDao = (StationDao) stationDaoOpt.orElseThrow(ServiceException::new);
            Optional<Station> stationOpt = stationDao.getByPK(stationId);
            if (stationOpt.isPresent()){
                return Optional.ofNullable(stationOpt.get().getName());
            }
        } catch (ServiceException | DAOException e) {
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            DBConnectService.close(connection);
        }
        return Optional.empty();
    }
}

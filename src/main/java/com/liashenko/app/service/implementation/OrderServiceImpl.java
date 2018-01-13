package com.liashenko.app.service.implementation;

import com.liashenko.app.persistance.dao.*;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.*;
import com.liashenko.app.service.OrderService;
import com.liashenko.app.service.data_source.DbConnectService;
import com.liashenko.app.service.dto.FullRouteDto;
import com.liashenko.app.service.dto.PriceForVagonDto;
import com.liashenko.app.service.exceptions.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderServiceImpl implements OrderService {

    private static final Logger classLogger = LogManager.getLogger(OrderServiceImpl.class);

    //make it in AppSettings
    private static final float DEFAULT_ROUTE_RATE = 1.0F;
    private static final float DEFAULT_PRICE_FOR_VAGON_PER_KM = 1.0F;

    private static final DbConnectService dbConnectService = DbConnectService.getInstance();
    private static final DaoFactory daoFactory = MySQLDaoFactory.getInstance();

    private ResourceBundle localeQueries;

    public OrderServiceImpl(ResourceBundle localeQueries) {
        this.localeQueries = localeQueries;
    }

    @Override
    public Optional<FullRouteDto> getFullTrainRoute(Long routeId) {
        Connection conn = dbConnectService.getConnection();
        try {
//            conn.setReadOnly(true);
            Optional<GenericJDBCDao> routeDaoOpt = daoFactory.getDao(conn, Route.class, localeQueries);
            RouteDao routeDao = (RouteDao) routeDaoOpt
                    .orElseThrow(() -> new ServiceException("RoutDao is null"));

            Optional<Route> routeFirstStationOpt = routeDao.getFirstTerminalStationOnRoute(routeId);
            Optional<Route> routeLastStationOpt = routeDao.getLastTerminalStationOnRoute(routeId);

            if (routeFirstStationOpt.isPresent() && routeLastStationOpt.isPresent()) {
                Long routeFirstStationId = routeFirstStationOpt.get().getStationId();
                Long routeLastStationId = routeLastStationOpt.get().getStationId();
                return Optional.ofNullable(getRouteWithTerminalStations(conn, routeFirstStationId, routeLastStationId));
            } else {
                throw new ServiceException("Couldn't find terminal station id for the route ");
            }
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
    }

    private FullRouteDto getRouteWithTerminalStations(Connection connection, Long firstStationId, Long lastStationId) {
        FullRouteDto fullRouteDto = null;
        Optional<GenericJDBCDao> stationDaoOpt = daoFactory.getDao(connection, Station.class, localeQueries);
        StationDao stationDao = (StationDao) stationDaoOpt
                .orElseThrow(() -> new ServiceException("StationDao is null"));
        Optional<Station> firstStationOpt = stationDao.getByPK(firstStationId);
        Optional<Station> lastStationOpt = stationDao.getByPK(lastStationId);
        if (firstStationOpt.isPresent() && lastStationOpt.isPresent()) {
            fullRouteDto = new FullRouteDto();
            fullRouteDto.setFirstTerminalStation(firstStationOpt.get().getCity());
            fullRouteDto.setLastTerminalStation(lastStationOpt.get().getCity());
            return fullRouteDto;
        } else {
            throw new ServiceException("Couldn't find terminal station's city for the route ");
        }
    }

    @Override
    public Optional<List<PriceForVagonDto>> getPricesForVagons(Long fromStationId, Long toStationId, Long routeId) {
        List<PriceForVagonDto> pricesForVagonList = new ArrayList<>();
        Connection conn = dbConnectService.getConnection();
        try {
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

            Optional<List<VagonType>> vagonTypeListOpt = vagonTypeDao.getAll();
            vagonTypeListOpt.ifPresent(vagonTypes
                    -> vagonTypes.forEach(vagonType
                    -> calculateAndFillListOfPricesForVagons(conn, pricesForVagonList, vagonType,
                    distance, routeRateFloat)));

        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
        return Optional.of(pricesForVagonList);
    }

    private void calculateAndFillListOfPricesForVagons(Connection connection, List<PriceForVagonDto> pricesForVagonList,
                                                       VagonType vagonType, Float distance, Float routeRateFloat) {
        PriceForVagonDto priceForVagonDto = new PriceForVagonDto();

        Optional<GenericJDBCDao> pricePerKmForVagonDaoOpt = daoFactory.getDao(connection, PricePerKmForVagon.class, localeQueries);
        PricePerKmForVagonDao pricePerKmForVagonDao = (PricePerKmForVagonDao) pricePerKmForVagonDaoOpt
                .orElseThrow(() -> new ServiceException("PricePerKmForVagonDao is null"));

        Optional<PricePerKmForVagon> pricePerKmForVagonOpt = pricePerKmForVagonDao.getPricePerKmForVagon(vagonType.getId());

        Double pricePerKmForVagonDouble = pricePerKmForVagonOpt.isPresent()
                ? pricePerKmForVagonOpt.get().getPrice()
                : DEFAULT_PRICE_FOR_VAGON_PER_KM;

        Float price = CalculatorUtil.calculateTicketPrice(routeRateFloat, distance, pricePerKmForVagonDouble, vagonType.getPlacesCount());

        priceForVagonDto.setVagonTypeId(vagonType.getId());
        priceForVagonDto.setVagonTypeName(vagonType.getTypeName());
        priceForVagonDto.setDistance(distance);
        priceForVagonDto.setTicketPrice(price);
        pricesForVagonList.add(priceForVagonDto);
    }

    @Override
    public Optional<String> getTrainNameById(Long trainId) {
        Connection conn = dbConnectService.getConnection();
        try {
//            conn.setReadOnly(true);
            Optional<GenericJDBCDao> trainDaoOpt = daoFactory.getDao(conn, Train.class, localeQueries);
            TrainDao trainDao = (TrainDao) trainDaoOpt.orElseThrow(() -> new ServiceException("TrainDao is null"));
            Optional<Train> trainOpt = trainDao.getByPK(trainId);
            return trainOpt.isPresent() ? Optional.ofNullable(trainOpt.get().getNumber()) : Optional.empty();
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
    }

    @Override
    public Optional<String> getStationNameById(Long stationId) {
        Connection conn = dbConnectService.getConnection();
        try {
//            conn.setReadOnly(true);
            Optional<GenericJDBCDao> stationDaoOpt = daoFactory.getDao(conn, Station.class, localeQueries);
            StationDao stationDao = (StationDao) stationDaoOpt
                    .orElseThrow(() -> new ServiceException("StationDao is null"));
            Optional<Station> stationOpt = stationDao.getByPK(stationId);
            return stationOpt.isPresent() ? Optional.ofNullable(stationOpt.get().getName()) : Optional.empty();
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
    }
}

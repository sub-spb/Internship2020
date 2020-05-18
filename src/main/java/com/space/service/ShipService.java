package com.space.service;

import com.space.exceptions.BadRequestException;
import com.space.exceptions.NotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShipService {
    private final ShipRepository shipRepository;

    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public List<Ship> getAllShips(Pageable pageable) {
        if (pageable == null) {
            return shipRepository.getAll();
        } else {
            return shipRepository.findAll(pageable).getContent();
        }
    }

    public Ship createShip(Ship ship) {
        if (isValidByParams(ship)) {
            throw new BadRequestException();
        } else if (ship.getUsed() == null) {
            ship.setUsed(false);
        }
        ship.setSpeed(Math.round(ship.getSpeed() * 100) / 100.0);
        ship.setRating(computeRating(ship));
        return shipRepository.save(ship);
    }

    public Ship updateShip(Ship ship, Long id) {
        Ship modernizedShip = getShipById(id);

        if (ship.getName() != null) {
            if (ship.getName().length() > 50 || ship.getName().isEmpty()) {
                throw new BadRequestException();
            }
            modernizedShip.setName(ship.getName());
        }
        if (ship.getPlanet() != null) {
            if (ship.getPlanet().length() > 50 || ship.getPlanet().isEmpty()) {
                throw new BadRequestException();
            }
            modernizedShip.setPlanet(ship.getPlanet());
        }
        if (ship.getShipType() != null) {
            modernizedShip.setShipType(ship.getShipType());
        }
        if (ship.getProdDate() != null) {
            if (ship.getProdDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear() < 2800
                    || ship.getProdDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear() > 3019) {
                throw new BadRequestException();
            }
            modernizedShip.setProdDate(ship.getProdDate());
        }
        if (ship.getUsed() != null) {
            modernizedShip.setUsed(ship.getUsed());
        }
        if (ship.getSpeed() != null) {
            if (ship.getSpeed() < 0.01d || ship.getSpeed() > 0.99d) {
                throw new BadRequestException();
            }
            modernizedShip.setSpeed(ship.getSpeed());
        }
        if (ship.getCrewSize() != null) {
            if (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999) {
                throw new BadRequestException();
            }
            modernizedShip.setCrewSize(ship.getCrewSize());
        }

        modernizedShip.setRating(computeRating(modernizedShip));
        return shipRepository.save(modernizedShip);
    }

    public void deleteShip(Long id){
        if(id < 0 ){
            throw new BadRequestException();
        }
        if(!shipRepository.existsById(id)){
            throw new NotFoundException();
        }
        shipRepository.deleteById(id);
    }

    public Ship getShipById(Long id){
        if(id < 0){
            throw new BadRequestException();
        }

        if(!shipRepository.existsById(id)){
            throw new NotFoundException();
        }

        return shipRepository.findById(id).orElse(null);
    }

    public List<Ship> getShipByNamePageNumber(String name, Pageable pageable){
        return shipRepository.filterByNamePageNumber(name, pageable);
    }

    public List<Ship> getShipByPlanetPageSize(String planet, Pageable pageable){
        return shipRepository.filterByPlanetPageSize(planet, pageable);
    }

    public List<Ship> getShipByAfterBeforeShipType(Date after, Date before, ShipType shipType, Pageable pageable){
        return shipRepository.filterByAfterBeforeShipType(after, before, shipType, pageable);
    }

    public List<Ship> getShipByMinMaxSpeedShipType(double minSpeed, double maxSpeed, ShipType shipType, Pageable pageable){
        return shipRepository.filterByMinMaxSpeedShipType(minSpeed, maxSpeed, shipType, pageable);
    }

    public List<Ship> getShipByMinMaxCrewSizeShipType(int minCrewSize, int maxCrewSize, ShipType shipType, Pageable pageable){
        return shipRepository.filterByMinMaxCrewSizeShipType(minCrewSize, maxCrewSize, shipType, pageable);
    }

    public List<Ship> getShipByMinMaxRatingIsUsed(double minRating, double maxRating, boolean isUsed, Pageable pageable){
        return shipRepository.filterByMinMaxRatingIsUsed(minRating, maxRating, isUsed, pageable);
    }

    public List<Ship> getShipByMaxSpeedMaxRatingIsUsed(double maxSpeed, double maxRating, boolean isUsed, Pageable pageable){
        return shipRepository.filterByMaxSpeedMaxRatingIsUsed(maxSpeed, maxRating, isUsed, pageable);
    }

    public List<Ship> getShipByAfterBeforeMinMaxCrewSize(Date after, Date before, int minCrewSize, int maxCrewSize, Pageable pageable){
        return shipRepository.filterByAfterBeforeMinMaxCrewSize(after, before, minCrewSize, maxCrewSize, pageable);
    }

    public List<Ship> getShipMinCrewSizeMinSpeedMinRating(int minCrewSize, double minSpeed, double minRating, Pageable pageable){
        return shipRepository.filterByMinCrewSizeMinSpeedMinRating(minCrewSize, minSpeed, minRating , pageable);
    }

    public List<Ship> getShipByNameAfterMaxRating(String name, Date after, double maxRating, Pageable pageable){
        return shipRepository.filterByNameAfterMaxRating(name, after, maxRating, pageable);
    }

    public List<Ship> getShipByShipTypeIsUsed(ShipType shipType, boolean isUsed, Pageable pageable){
        return shipRepository.filterByShipTypeIsUsed(shipType, isUsed, pageable);
    }

    public List<Ship> getShipByMaxCrewSizeShipType(int maxCrewSize, ShipType shipType, Pageable pageable){
        return shipRepository.filterByMaxCrewSizeShipType(maxCrewSize, shipType, pageable);
    }

    public List<Ship> getShipByBeforeMaxSpeedShipType(Date before, double maxSpeed, ShipType shipType, Pageable pageable){
        return shipRepository.filterByBeforeMaxSpeedShipType(before, maxSpeed, shipType, pageable);
    }

    public List<Ship> getShipByMinMaxSpeedIsUsed(double minSpeed, double maxSpeed, boolean isUsed, Pageable pageable){
        return shipRepository.filterByMinMaxSpeedIsUsed(minSpeed, maxSpeed, isUsed, pageable);
    }

    public List<Ship> getShipsByFilters(Optional<String> name, Optional<String>  planet,
                                        Optional<ShipType> shipType, Optional<Long> after,
                                        Optional<Long>  before, Optional<Boolean> isUsed,
                                        Optional<Double> minSpeed, Optional<Double>  maxSpeed,
                                        Optional<Integer> minCrewSize, Optional<Integer> maxCrewSize,
                                        Optional<Double>  minRating, Optional<Double>  maxRating,
                                        Pageable pageable){
        List<Ship> shipList;;
        if(planet.isPresent()) {
            shipList = getShipByPlanetPageSize(planet.get(), pageable);
        } else if(after.isPresent() && before.isPresent() && shipType.isPresent()) {
            shipList = getShipByAfterBeforeShipType(new Date(after.get()), new Date(before.get()), shipType.get(), pageable);
        } else if(minSpeed.isPresent() && maxSpeed.isPresent() && shipType.isPresent()) {
            shipList = getShipByMinMaxSpeedShipType(minSpeed.get(), maxSpeed.get(), shipType.get(), pageable);
        } else if(minCrewSize.isPresent() && maxCrewSize.isPresent() && shipType.isPresent()) {
            shipList = getShipByMinMaxCrewSizeShipType(minCrewSize.get(), maxCrewSize.get(), shipType.get(), pageable);
        } else if(minRating.isPresent() && maxRating.isPresent() & isUsed.isPresent()) {
            shipList = getShipByMinMaxRatingIsUsed(minRating.get(), maxRating.get(), isUsed.get(), pageable);
        } else if(maxSpeed.isPresent() && maxRating.isPresent() && isUsed.isPresent()) {
            shipList = getShipByMaxSpeedMaxRatingIsUsed(maxSpeed.get(), maxRating.get(), isUsed.get(), pageable);
        } else if(after.isPresent() && before.isPresent() && minCrewSize.isPresent() && maxCrewSize.isPresent()) {
            shipList = getShipByAfterBeforeMinMaxCrewSize(new Date(after.get()), new Date(before.get()), minCrewSize.get(), maxCrewSize.get(), pageable);
        } else if(minCrewSize.isPresent() && minSpeed.isPresent() && minRating.isPresent()) {
            shipList = getShipMinCrewSizeMinSpeedMinRating(minCrewSize.get(), minSpeed.get(), minRating.get(), pageable);
        } else if(name.isPresent() && after.isPresent() && maxRating.isPresent()) {
            shipList = getShipByNameAfterMaxRating(name.get(), new Date(after.get()), maxRating.get(), pageable);
        } else if(shipType.isPresent() && isUsed.isPresent()) {
            shipList = getShipByShipTypeIsUsed(shipType.get(), isUsed.get(), pageable);
        } else if(maxCrewSize.isPresent() && shipType.isPresent()){
            shipList = getShipByMaxCrewSizeShipType(maxCrewSize.get(), shipType.get(), pageable);
        } else if(before.isPresent() && maxSpeed.isPresent() && shipType.isPresent()){
            shipList = getShipByBeforeMaxSpeedShipType(new Date(before.get()), maxSpeed.get(), shipType.get(), pageable);
        } else if(minSpeed.isPresent() && maxSpeed.isPresent() && isUsed.isPresent()){
            shipList = getShipByMinMaxSpeedIsUsed(minSpeed.get(), maxSpeed.get(), isUsed.get(), pageable);
        } else if(name.isPresent()) {
            shipList = getShipByNamePageNumber(name.get(), pageable);
        } else {
            shipList = getAllShips(pageable);
        }
        return shipList;
    }

    public boolean isValidByParams(Ship ship){
        return ship.getName() == null
                || ship.getName().isEmpty()
                || ship.getName().length() > 50
                || (ship.getPlanet() == null
                || ship.getPlanet().isEmpty()
                || ship.getPlanet().length() > 50)
                || ship.getShipType() == null
                || (ship.getProdDate() == null
                || ship.getProdDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear() < 2800
                || ship.getProdDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear() > 3019)
                || ship.getSpeed() == null
                || ship.getSpeed() < 0.01d
                || ship.getSpeed() > 0.99d
                || ship.getCrewSize() == null
                || ship.getCrewSize() < 1
                || ship.getCrewSize() > 9999;
    }

    private Double computeRating(Ship ship) {
        double k = 1.0;
        if(ship.getUsed()){
            k = 0.5;
        }
        int currentYear = 3019;
        int productionDate = ship.getProdDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        double rating = (80 * ship.getSpeed() * k) / (double) (currentYear - productionDate + 1);
        return Math.round(rating * 100) / 100.0;
    }
}

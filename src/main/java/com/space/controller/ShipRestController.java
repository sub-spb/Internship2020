package com.space.controller;

import com.space.exceptions.BadRequestException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("rest/ships")
public class ShipRestController {
    private final ShipService shipService;

    @Autowired
    public ShipRestController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping
    public List<Ship> getShipsList(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<String> planet,
            @RequestParam(required = false) Optional <ShipType> shipType,
            @RequestParam(required = false) Optional<Long> after,
            @RequestParam(required = false) Optional<Long>  before,
            @RequestParam(required = false) Optional<Boolean> isUsed,
            @RequestParam(required = false) Optional<Double> minSpeed,
            @RequestParam(required = false) Optional<Double>  maxSpeed,
            @RequestParam(required = false) Optional<Integer> minCrewSize,
            @RequestParam(required = false) Optional<Integer> maxCrewSize,
            @RequestParam(required = false) Optional<Double>  minRating,
            @RequestParam(required = false) Optional<Double>  maxRating,
            @RequestParam(required = false) Optional<ShipOrder> order,
            @RequestParam(required = false) Optional<Integer> pageNumber,
            @RequestParam(required = false) Optional<Integer> pageSize
            ){

        String sort = "id";
        if(order.isPresent()){
            sort = order.get().getFieldName();
        }

        Pageable pageable = PageRequest.of(pageNumber.orElse(0), pageSize.orElse(3), Sort.by(sort));

        return shipService.getShipsByFilters(name, planet, shipType, after,
                before, isUsed,minSpeed, maxSpeed, minCrewSize,
                maxCrewSize, minRating, maxRating, pageable);
    }

    @GetMapping("/count")
    public long getShipsCount(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<String> planet,
            @RequestParam(required = false) Optional <ShipType> shipType,
            @RequestParam(required = false) Optional<Long> after,
            @RequestParam(required = false) Optional<Long>  before,
            @RequestParam(required = false) Optional<Boolean> isUsed,
            @RequestParam(required = false) Optional<Double> minSpeed,
            @RequestParam(required = false) Optional<Double>  maxSpeed,
            @RequestParam(required = false) Optional<Integer> minCrewSize,
            @RequestParam(required = false) Optional<Integer> maxCrewSize,
            @RequestParam(required = false) Optional<Double>  minRating,
            @RequestParam(required = false) Optional<Double>  maxRating,
            @RequestParam(required = false) Optional<ShipOrder> order,
            @RequestParam(required = false) Optional<Integer> pageNumber,
            @RequestParam(required = false) Optional<Integer> pageSize
    ){
        return shipService.getShipsByFilters(name, planet, shipType, after,
                before, isUsed,minSpeed, maxSpeed, minCrewSize,
                maxCrewSize, minRating, maxRating, null).size();
    }

    @GetMapping("/{id}")
    public Ship getShipById(@PathVariable Long id){
        if(!isValidId(id)){
            throw new BadRequestException();
        }
        return shipService.getShipById(id);
    }

    @PostMapping
    public Ship createShip(@RequestBody Ship ship){

        return shipService.createShip(ship);
    }

    @PostMapping("/{id}")
    public Ship updateShip(@RequestBody Ship ship, @PathVariable long id){
        if(!isValidId(id)){
            throw new BadRequestException();
        }
        return shipService.updateShip(ship, id);
    }

    @DeleteMapping("/{id}")
    public void deleteShip(@PathVariable long id){
        if(!isValidId(id)){
            throw new BadRequestException();
        }
        shipService.deleteShip(id);
    }

    public boolean isValidId(Long id){
        return id != null && id > 0 && id % 1 == 0;
    }
}

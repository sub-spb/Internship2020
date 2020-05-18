package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {
    Page<Ship> findAll(Pageable pageable);

    @Query(value = "select * from ship", nativeQuery = true)
    List<Ship> getAll();

    @Query("from Ship ship where ship.name like %:name%")
    List<Ship> filterByNamePageNumber(@Param("name") String name, Pageable pageable);

    @Query("from Ship ship where ship.planet like %:planet%")
    List<Ship> filterByPlanetPageSize(@Param("planet") String planet, Pageable pageable);

    @Query("from Ship ship where ship.prodDate >= :after and ship.prodDate <= :before and ship.shipType = :shipType")
    List<Ship> filterByAfterBeforeShipType(@Param("after") Date after, @Param("before") Date before, @Param("shipType") ShipType shipType, Pageable pageable);

    @Query("from Ship ship where ship.speed >= :minSpeed and ship.speed <= :maxSpeed and ship.shipType = :shipType")
    List<Ship> filterByMinMaxSpeedShipType(@Param("minSpeed") double minSpeed, @Param("maxSpeed") double maxSpeed, @Param("shipType") ShipType shipType, Pageable pageable);

    @Query("from Ship ship where ship.crewSize >= :minCrewSize and ship.crewSize <= :maxCrewSize and ship.shipType = :shipType")
    List<Ship> filterByMinMaxCrewSizeShipType(@Param("minCrewSize") int minCrewSize, @Param("maxCrewSize") int maxCrewSize, @Param("shipType") ShipType shipType, Pageable pageable);

    @Query("from Ship ship where ship.rating >= :minRating and ship.rating <= :maxRating and ship.isUsed = :isUsed")
    List<Ship> filterByMinMaxRatingIsUsed(@Param("minRating") double minRating, @Param("maxRating") double maxRating, @Param("isUsed") boolean isUsed, Pageable pageable);

    @Query("from Ship ship where ship.speed <= :maxSpeed and ship.rating <= :maxRating and ship.isUsed = :isUsed")
    List<Ship> filterByMaxSpeedMaxRatingIsUsed(@Param("maxSpeed") double maxSpeed, @Param("maxRating") double maxRating, @Param("isUsed") boolean isUsed, Pageable pageable);

    @Query("from Ship ship where ship.prodDate >= :after and ship.prodDate <= :before and ship.crewSize >= :minCrewSize and ship.crewSize <= :maxCrewSize")
    List<Ship> filterByAfterBeforeMinMaxCrewSize(@Param("after") Date after, @Param("before") Date before, @Param("minCrewSize") int minCrewSize, @Param("maxCrewSize") int maxCrewSize, Pageable pageable);

    @Query("from Ship ship where ship.crewSize >= :minCrewSize and ship.speed >= :minSpeed and ship.rating >= :minRating")
    List<Ship> filterByMinCrewSizeMinSpeedMinRating(@Param("minCrewSize") int minCrewSize, @Param("minSpeed") double minSpeed, @Param("minRating") double minRating, Pageable pageable);

    @Query("from Ship ship where ship.name like %:name% and ship.prodDate >= :after and ship.rating <= :maxRating")
    List<Ship> filterByNameAfterMaxRating(@Param("name") String name, @Param("after") Date after, @Param("maxRating") double maxRating, Pageable pageable);

    /*@Query("SELECT ship FROM Ship ship WHERE ship.name LIKE %:name% AND ship.prodDate >= :after AND ship.rating <= :maxRating")
    List<Ship> getAllWithFiltersNameAfterMaxRating(@Param("name") String name, @Param("after") Date after, @Param("maxRating") double maxRating, Pageable pageable);*/

    @Query("from Ship ship where ship.shipType = :shipType and ship.isUsed = :isUsed")
    List<Ship> filterByShipTypeIsUsed(@Param("shipType") ShipType shipType, @Param("isUsed") boolean isUsed, Pageable pageable);

    @Query("from Ship ship where ship.crewSize <= :maxCrewSize and ship.shipType = :shipType")
    List<Ship> filterByMaxCrewSizeShipType(@Param("maxCrewSize") int maxCrewSize, @Param("shipType") ShipType shipType, Pageable pageable);

    @Query("from Ship ship where ship.prodDate <= :before and ship.speed <= :maxSpeed and ship.shipType = :shipType")
    List<Ship> filterByBeforeMaxSpeedShipType(@Param("before") Date before, @Param("maxSpeed") double maxSpeed, @Param("shipType") ShipType shipType, Pageable pageable);

    @Query("from Ship ship where ship.speed >= :minSpeed and ship.speed <= :maxSpeed and ship.isUsed = :isUsed")
    List<Ship> filterByMinMaxSpeedIsUsed(@Param("minSpeed") double minSpeed, @Param("maxSpeed") double maxSpeed, @Param("isUsed") boolean isUsed, Pageable pageable);
}

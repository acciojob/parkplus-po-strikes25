package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot newParkingLot = new ParkingLot();
        newParkingLot.setAddress(address);
        newParkingLot.setName(name);

        parkingLotRepository1.save(newParkingLot);

        return newParkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot newSpot = new Spot();
        if(numberOfWheels == 2)
            newSpot.setSpotType(SpotType.TWO_WHEELER);
        else if(numberOfWheels == 4)
            newSpot.setSpotType(SpotType.FOUR_WHEELER);
        else
            newSpot.setSpotType(SpotType.OTHERS);

        newSpot.setPricePerHour(pricePerHour);
        parkingLot.getSpotList().add(newSpot);
        newSpot.setParkingLot(parkingLot);

        parkingLotRepository1.save(parkingLot);

        return newSpot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spot = spotRepository1.findById(spotId).get();
        ParkingLot parkingLot = spot.getParkingLot();
        parkingLot.getSpotList().remove(spot);

        spotRepository1.deleteById(spotId);
        parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot updatedSpot = spotRepository1.findById(spotId).get();
        updatedSpot.setPricePerHour(pricePerHour);

        parkingLotRepository1.save(updatedSpot.getParkingLot());

        return updatedSpot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}

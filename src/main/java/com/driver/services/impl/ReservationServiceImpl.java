package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        // Reserve a spot in the given parkingLot such that the total price is minimum.
        // Note that the price per hour for each spot is different
        // Note that the vehicle can only be parked in a spot having a type equal to or
        // larger than given vehicle
        // If parkingLot is not found, user is not found, or no spot is available,
        // throw "Cannot make reservation" exception.
        Reservation newReservation = new Reservation();
        // 1. If ParkingLotNotFound, UserNotFound, NoSpotAvailable --> throw exception ;
        if(!parkingLotRepository3.existsById(parkingLotId) || !userRepository3.existsById(userId))
            throw new RuntimeException("Cannot make reservation");

        Spot reservedSpot = new Spot();
        List<Spot> spotList = parkingLotRepository3.findById(parkingLotId).get().getSpotList();
        boolean spotCheck = false;
        for(Spot spot : spotList) {
            // 2. Traverse Spot List and check if any/all spots are occupied ;
            if(!spot.getOccupied()) {
                int minimumTotalPrice = Integer.MAX_VALUE;
                // 3. If spot are available then check if the spotType >= vehicleType ;
                if(numberOfWheels == 2 && spot.getSpotType().equals(SpotType.TWO_WHEELER) || spot.getSpotType().equals(SpotType.FOUR_WHEELER) ||
                        numberOfWheels == 4 && spot.getSpotType().equals(SpotType.FOUR_WHEELER) || spot.getSpotType().equals(SpotType.OTHERS)) {
                    // 4. Calculate the totalMinimumPrice for each available spot and
                    // reserve the spot with the minimum totalPrice ;
                    int totalPriceOfThisSpot = timeInHours * spot.getPricePerHour();
                    if(totalPriceOfThisSpot < minimumTotalPrice) {
                        minimumTotalPrice = totalPriceOfThisSpot;
                        reservedSpot = spot;
                    }
                }
                spotCheck = true;
            }
        }
        if(!spotCheck)
            throw new RuntimeException("Cannot make reservation");

        newReservation.setSpot(reservedSpot);
        newReservation.setNumberOfHours(timeInHours);
        newReservation.setUser(userRepository3.findById(userId).get());

        reservedSpot.getReservationList().add(newReservation);
        return newReservation;
    }
}

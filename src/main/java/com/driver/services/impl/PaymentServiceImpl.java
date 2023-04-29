package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        // 1. If the amountSent is less than bill, throw "Insufficient Amount" exception,
        // otherwise update payment attributes ;
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        int billCalculated = reservation.getSpot().getPricePerHour() * reservation.getNumberOfHours();
        if(amountSent < billCalculated)
            throw new RuntimeException("Insufficient Amount");
        // 2. Create a newPayment ;
        Payment newPayment = new Payment();
        // 3. If the mode contains a string other than "cash", "card", or "upi"
        // (any character in uppercase or lowercase), throw "Payment mode not detected" exception ;
        if(mode.equalsIgnoreCase("CASH"))
            newPayment.setPaymentMode(PaymentMode.CASH);
        else if(mode.equalsIgnoreCase("CARD"))
            newPayment.setPaymentMode(PaymentMode.CARD);
        else if(mode.equalsIgnoreCase("UPI"))
            newPayment.setPaymentMode(PaymentMode.UPI);
        else
            throw new RuntimeException("Payment mode not detected");
        // 4. Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
        newPayment.setReservation(reservation);
        newPayment.setPaymentCompleted(true);

        reservation.setPayment(newPayment);
        reservationRepository2.save(reservation);

        return newPayment;
    }
}

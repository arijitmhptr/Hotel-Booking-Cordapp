package com.template.states;

import com.template.contracts.BookingContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(BookingContract.class)
public class BookingState implements ContractState {
    private final String customerName;
    private final int customerAge;
    private final Instant checkInDate;
    private final Instant checkOutDate;
    private final String roomType;
    private final int roomRate;
    private final double bookingAmount;
    private final Party makeMyTrip;
    private final Party lemonTree;

    public BookingState(String customerName, int customerAge, Instant checkInDate, Instant checkOutDate, String roomType, int roomRate, double bookingAmount, Party makeMyTrip, Party lemonTree) {
        this.customerName = customerName;
        this.customerAge = customerAge;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.roomRate = roomRate;
        this.bookingAmount = bookingAmount;
        this.makeMyTrip = makeMyTrip;
        this.lemonTree = lemonTree;
    }

    public double getBookingAmount() {
        return bookingAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerAge() {
        return customerAge;
    }

    public Instant getCheckInDate() {
        return checkInDate;
    }

    public Instant getCheckOutDate() {
        return checkOutDate;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getRoomRate() {
        return roomRate;
    }

    public Party getMakeMyTrip() {
        return makeMyTrip;
    }

    public Party getLemonTree() {
        return lemonTree;
    }

    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(makeMyTrip,lemonTree);
    }
}
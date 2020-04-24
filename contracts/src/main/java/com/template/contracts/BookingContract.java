package com.template.contracts;

import com.template.states.BookingState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;

import java.security.PublicKey;
import java.time.Instant;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class BookingContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.template.contracts.BookingContract";

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(LedgerTransaction tx) {
        if (tx.getCommands().size() != 1) throw new IllegalArgumentException("Command input can be only one");

        Command command = tx.getCommand(0);
        CommandData commandData = command.getValue();
        List<PublicKey> allSigners = command.getSigners();

        if (commandData instanceof Commands.Booking){
            System.out.println("Inside Booking state");
            if (tx.getInputStates().size() != 0){
                throw new IllegalArgumentException("There should not be any input state");
            }
            if (tx.getOutputStates().size() != 1){
                throw new IllegalArgumentException("There must be one output state");
            }
            ContractState outputState = tx.getOutput(0);
            if (!(outputState instanceof BookingState)){
                throw new IllegalArgumentException("Output state has to be of BookingState");
            }
            BookingState bookState = (BookingState) outputState;
            requireThat(require -> {
                require.using("Customer age should be more than 18", bookState.getCustomerAge() >= 18);
                require.using("Check out date should be greater than Check In date", bookState.getCheckOutDate().isAfter(bookState.getCheckInDate()));
                require.using("Check In date should be future dated", bookState.getCheckInDate().isAfter(Instant.now()));
                require.using("Check Out date should be future dated", bookState.getCheckOutDate().isAfter(Instant.now()));
                require.using("Room typer must be Standard or Delux", bookState.getRoomType().equals("Standard") || bookState.getRoomType().equals("Delux"));
                require.using("Booking amount must be 85% of original amount", bookState.getBookingAmount() == (bookState.getRoomRate())*0.85);
            return null;
            });
            PublicKey makemyTripKey = bookState.getMakeMyTrip().getOwningKey();
            if (!(allSigners.contains(makemyTripKey))){
                throw new IllegalArgumentException("MakeMyTrip must sign this transaction");
            }
        }
    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        class Booking implements Commands {}
    }
}
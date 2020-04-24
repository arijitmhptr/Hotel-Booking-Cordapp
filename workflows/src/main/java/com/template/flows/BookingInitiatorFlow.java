package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.BookingContract;
import com.template.states.BookingState;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.template.contracts.BookingContract.ID;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class BookingInitiatorFlow extends FlowLogic<SignedTransaction> {
    private final String customerName;
    private final int customerAge;
    private final Instant checkInDate;
    private final Instant checkOutDate;
    private final String roomType;
    private final int roomRate;
    private final double bookingAmount;
    private final Party lemonTree;
    //private final ProgressTracker progressTracker = new ProgressTracker();
    private final ProgressTracker.Step GENERATING_TRANSACTION = new ProgressTracker.Step("Generating transaction based on IOU");
    private final ProgressTracker.Step VERIFYING_TRANSACTION = new ProgressTracker.Step("Verifying Contract constrain");
    private final ProgressTracker.Step SIGNING_TRANSACTION = new ProgressTracker.Step("Signing transaction with Private key");
    private final ProgressTracker.Step GATHERING_SIGNATURE = new ProgressTracker.Step("Gathering the counterparty's signature"){
        @Override
        public ProgressTracker childProgressTracker() {
            return CollectSignaturesFlow.Companion.tracker();
        }
    };
    private final ProgressTracker.Step FINALISING_TRANSACTION = new ProgressTracker.Step("Obtaining Notary signature and recording transaction"){
        @Override
        public ProgressTracker childProgressTracker() {
            return FinalityFlow.Companion.tracker();
        }
    };
    private final ProgressTracker progressTracker = new ProgressTracker(
            GENERATING_TRANSACTION,
            VERIFYING_TRANSACTION,
            SIGNING_TRANSACTION,
            GATHERING_SIGNATURE,
            FINALISING_TRANSACTION
    );

    public BookingInitiatorFlow(String customerName, int customerAge, Instant checkInDate, Instant checkOutDate, String roomType, int roomRate, double bookingAmount, Party LemonTree) {
        this.customerName = customerName;
        this.customerAge = customerAge;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.roomRate = roomRate;
        this.bookingAmount = bookingAmount;
        this.lemonTree = LemonTree;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // Initiator flow logic goes here.
        //check the correct node is initiating the transaction
        if (getOurIdentity().getName().getOrganisation().equals("MakeMyTrip")){
            System.out.println("Identity verified");
        }
        else { throw new FlowException("Booking request is not initiated by MakeMyTrip"); }

        //Assign Notary
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        BookingState outputState = new BookingState(customerName, customerAge,checkInDate,checkOutDate,roomType,roomRate,bookingAmount,getOurIdentity(),lemonTree);

        progressTracker.setCurrentStep(GENERATING_TRANSACTION);

        TransactionBuilder txbuilder = new TransactionBuilder(notary).addOutputState(outputState,BookingContract.ID).addCommand(new BookingContract.Commands.Booking(),getOurIdentity().getOwningKey());

        progressTracker.setCurrentStep(VERIFYING_TRANSACTION);
        //txbuilder.verify(getServiceHub());

        progressTracker.setCurrentStep(SIGNING_TRANSACTION);
        SignedTransaction ptx = getServiceHub().signInitialTransaction(txbuilder);

        progressTracker.setCurrentStep(GATHERING_SIGNATURE);
        FlowSession session = initiateFlow(lemonTree);
        //SignedTransaction stx = subFlow(new CollectSignaturesFlow(ptx, Collections.singleton(session)));

        progressTracker.setCurrentStep(FINALISING_TRANSACTION);
        return subFlow(new FinalityFlow(ptx,session));
    }
}

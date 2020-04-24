<p align="center">
  <img src="https://www.corda.net/wp-content/uploads/2016/11/fg005_corda_b.png" alt="Corda" width="500">
</p>

# Hotel Booking Cordapp Template - Java

Our use-case:

MakeMyTrip is online hotel booking services which allow its customer to book rooms online on LemonTree’s hotel chain. As per the agreement, MakeMyTrip collects certain information from its customer and send to LemonTree, LemonTree in return sends an acknowledgement to MakeMyTrip. So, here we are building an application HotelBookingCorDapp for MakeMyTrip.

There are two parties in HotelBookingCorDapp.

MakeMyTrip

LemonTree

MakeMyTrip will send the following details to LemonTree to book a stay as requested by their customer.

Customer name, Customer Age, Check In Date, Check out Date, Room Type, Original Room Rate, Booking Amount (After 15% commission deduction on room rate)

5 constraints should be meet before sending booking detail to HotelHeaven:

Customer Age should be greater than 18.

Check in and Check Out date Should be Future Date.

Check Out date should be greater than Check in date.

Room Type format is from this only: Standard or Delux

After commission price should be 85% of Original room price.

Run at terminal:

flow start BookingInitiatorFlow customerName: Arijit,customerAge: 30,checkInDate: "2020-10-23T10:12:35Z",checkOutDate: "2020-10-24T10:12:35Z",roomType: Delux,roomRate: 4000,bookingAmount: 3400,LemonTree: "O=LemonTree,L=New York,C=US"

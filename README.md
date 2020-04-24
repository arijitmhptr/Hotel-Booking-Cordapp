<p align="center">
  <img src="https://www.corda.net/wp-content/uploads/2016/11/fg005_corda_b.png" alt="Corda" width="500">
</p>

# Hotel Booking Cordapp Template - Java

Our use-case:

BookMyStay is online hotel booking services which allow its customer to book rooms online on HotelHeaven’s hotel chain. As per the agreement, BookMyStay collects certain information from its customer and send to HotelHeaven, HotelHeaven in return sends an acknowledgement to BookMyStay. So, here we are building an application BookingCorDapp for BookMyStay.

There are two parties in BookingCorDapp.

BookMyStay

HotelHeaven

BookMyStay will send the following details to HotelHeaven to book a stay as requested by their customer.

Customer name, Customer Age Check In Date, Check out Date Room Type, Original Room Rate Merchant Credit Card Number, Credit Card exp date. Credit Card Amount (After 15% commission deduction on room rate)

7 constraints should be meet before sending booking detail to HotelHeaven:

Customer Age should be greater than 18.

Check in and Check Out date Should be Future Date.

Check Out date should be greater than Check in date.

Room Type format is from this only: K, NK, DD, NDD

After commission price should 85% of Original room price.

Credit Card number length should be 16.

Credit Card Exp date should not be in past.

Run at terminal:

flow start BookingInitiatorFlow custName: Sonal,custAge: 27,checkInDate: "2020-10-23T10:12:35Z",checkOutDate: "2020-10-24T10:12:35Z",roomType: NK,roomRate: 100,creditCardNumber: 1234567890123456,creditCardExpDate: "2020-10-23T10:12:35Z",creditCardAmount: 85,hotelHeaven: "O=HotelHeaven,L=New York,C=US"

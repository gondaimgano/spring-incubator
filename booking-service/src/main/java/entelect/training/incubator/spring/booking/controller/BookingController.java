package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingRequest;
import entelect.training.incubator.spring.booking.model.Customer;
import entelect.training.incubator.spring.booking.model.Flight;
import entelect.training.incubator.spring.booking.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping("bookings")
public class BookingController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    private final BookingRepository bookingRepository;

    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @PostMapping
    ResponseEntity<?> saveBooking(
            @RequestBody
            BookingRequest bookingRequest
    ){
        LOGGER.info("performing a booking creation");
        RestTemplate restTemplate = new RestTemplate();
        String customerResourceUrl
                = "http://localhost:8201/customers/";
        String flightResourceUrl
                = "http://localhost:8202/flights/";
        LOGGER.trace("Fetching customer Id "+bookingRequest.getCustomerId());
        ResponseEntity<?> customerResponseEntity
                = restTemplate.getForEntity(customerResourceUrl + bookingRequest.getCustomerId(), Customer.class);

        if( customerResponseEntity.getStatusCode()== HttpStatus.OK) {
            LOGGER.trace("Fetching flightId for customerId "+bookingRequest.getFlightId());
            ResponseEntity<?> flightResponseEntity
                    = restTemplate.getForEntity(flightResourceUrl + bookingRequest.getFlightId(), Flight.class);

            if(flightResponseEntity.getStatusCode()==HttpStatus.OK)
            {
                LOGGER.trace("Saving Booking");
                Booking booking=new Booking();
                booking.setCustomerId(bookingRequest.getCustomerId());
                booking.setFlightId(bookingRequest.getFlightId());
                booking.setReferenceNumber(UUID.randomUUID().toString().substring(0,8).toUpperCase());
               final Booking item= bookingRepository.save(booking);
               return ResponseEntity.ok(item);
            }

        }

        LOGGER.trace("Not Found");

        return ResponseEntity.notFound().build();
    }

}

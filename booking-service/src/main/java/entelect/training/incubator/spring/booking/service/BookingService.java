package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking getBookingByReference(String id) {
        Optional<Booking> bookingOptional = bookingRepository.findByReferenceNumber(id);
        return bookingOptional.orElse(null);
    }


    public Optional<List<Booking>> getBookingsByCustomerId(Integer id) {
        return bookingRepository.findByCustomerId(id);
    }

    public Booking getBooking(Integer id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        return bookingOptional.orElse(null);
    }
}

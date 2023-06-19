package edu.ozyegin.cs.controller;

import java.util.*;

import edu.ozyegin.cs.entity.booking;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

public class bookingControllerTest extends IntegrationTestSuite {

    private List<booking> generateBooking(int size) {
        ArrayList<booking> booking = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            booking.add(new booking()
                    .room_id(random(2))
                    .user_id(random(2))
                    .guest_id(random(2))
                    .guest_count(random(5))
                    .is_paid(random(2))
                    .price(random(5))
                    .confirmation_status(random(3))
                    .is_booked(random(2))
                    .checkin_date(RandomStringUtils.random(random(10), true, true))
                    .checkout_date(RandomStringUtils.random(random(10), true, true)));
        }
        return booking;
    }

    @Test
    public void create1() throws Exception {
        List<booking> bookings = generateBooking(1);


        postMethod("/booking/make_booking", String.class, bookings);

        List<booking> data = Objects.requireNonNull(jdbcTemplate)
                .query("SELECT * FROM booking", new BeanPropertyRowMapper<>(booking.class));

        assertTwoListEqual(bookings, data);
    }



}
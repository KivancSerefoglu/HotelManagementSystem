package edu.ozyegin.cs.controller;

import edu.ozyegin.cs.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.*;

@RestController
@RequestMapping
@CrossOrigin
public class statisticsController {
    @Autowired
    private PlatformTransactionManager transactionManager;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    final int batchSize = 10;


    final String countPS = "SELECT COUNT(*) FROM booking WHERE room_id = ?";




    @RequestMapping(value = "/statistics/room/bookings", method = RequestMethod.POST, produces = "application/json")
    public int statisticsRoomBookings(@RequestBody Map<String, Object>[] payload) {
        // prepare data for usage
        List<booking> bookings = new ArrayList<>();
        for (Map<String, Object> entity : payload) {
            booking booking1 = new booking();
            booking1.setRoom_id((int) entity.get("RoomId"));

            bookings.add(booking1);
        }


        int count = 0;
        for (booking b : bookings) {
            count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM booking WHERE room_id = ?", Integer.class, b.getRoom_id());
        }
        return count;
    }


    @RequestMapping(value = "/statistics/room/bookings/conversion_rate", method = RequestMethod.POST, produces = "application/json")
    public int bookingsConversion_rate(@RequestBody Map<String, Object>[] payload) {

        List<booking> bookings = new ArrayList<>();
        for (Map<String, Object> entity : payload) {
            booking booking1 = new booking();
            booking1.setRoom_id((int) entity.get("RoomId"));
            bookings.add(booking1);
        }
        int totalBookings = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM booking WHERE room_id = ?",
                new Object[]{bookings.get(0).getRoom_id()}, Integer.class);

        // get cancelled bookings
        int cancelledBookings = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM booking WHERE room_id = ? AND status = 0",
                new Object[]{bookings.get(0).getRoom_id()}, Integer.class);

        // calculate and return conversion rate
        return (totalBookings - cancelledBookings) / totalBookings;
    }

    @RequestMapping(value = "/statistics/room/average_price", method = RequestMethod.POST, produces = "application/json")
    public int bookingsAveragePrice(@RequestBody Map<String, Object>[] payload) {
        List<booking> bookings = new ArrayList<>();
        for (Map<String, Object> entity : payload) {
            booking booking1 = new booking();
            booking1.setRoom_id((int) entity.get("RoomId"));
            bookings.add(booking1);
        }

        // get average price of bookings
        int avgPrice = jdbcTemplate.queryForObject("SELECT AVG(price) FROM booking WHERE room_id = ?",
                new Object[]{bookings.get(0).getRoom_id()}, Integer.class);

        return avgPrice;
    }


    @RequestMapping(value = "/statistics/room/max_user_bookings", method = RequestMethod.POST, produces = "application/json")
    public int bookingsMaxUserBookings(@RequestBody Map<String, Object>[] payload) {
        List<booking> bookings = new ArrayList<>();
        for (Map<String, Object> entity : payload) {
            booking booking1 = new booking();
            booking1.setRoom_id((int) entity.get("RoomId"));
            bookings.add(booking1);
        }

        // get user_id of user who stayed in the given room the most
        int maxUserId = jdbcTemplate.queryForObject("SELECT user_id FROM booking WHERE room_id = ? GROUP BY user_id ORDER BY COUNT(*) DESC LIMIT 1",
                new Object[]{bookings.get(0).getRoom_id()}, Integer.class);

        return maxUserId;
    }

    @RequestMapping(value = "/statistics/room/max_user_duration", method = RequestMethod.POST, produces = "application/json")
    public int maxUserDuration(@RequestBody Map<String, Object>[] payload) {
        List<booking> bookings = new ArrayList<>();
        for (Map<String, Object> entity : payload) {
            booking booking1 = new booking();
            booking1.setRoom_id((int) entity.get("RoomId"));
            bookings.add(booking1);
        }

        // get user_id of user who stayed in the given room the most
        int maxUserId = jdbcTemplate.queryForObject("SELECT user_id, SUM(DATEDIFF(checkout_date, checkin_date)) as duration FROM bookings WHERE room_id = ? GROUP BY user_id ORDER BY duration DESC LIMIT 1",
                new Object[]{bookings.get(0).getRoom_id()}, Integer.class);

        return maxUserId;
    }

    @RequestMapping(value = "/statistics/room/amenity_count", method = RequestMethod.POST, produces = "application/json")
    public List<Map<String, Object>> amenityCount(@RequestBody Map<String, Object> payload) {
        int k = (int) payload.get("K");
        String query = "SELECT * FROM room JOIN inventory ON room.room_id = inventory.room_id GROUP BY inventory.room_id HAVING COUNT(inventory.amenities_id) >= ?";
        return jdbcTemplate.queryForList(query, k);
    }

    @RequestMapping(value = "/statistics/housekeeping/count_clean", method = RequestMethod.POST, produces = "application/json")
    public int finishedTasks() {
        int finishedTasks = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM housekeepingSchedule WHERE is_cleaned = 1",
                Integer.class);

        return finishedTasks;
    }

    @RequestMapping(value = "/statistics/housekeeping/max_unique", method = RequestMethod.POST, produces = "application/json")
    public int mostCleanedRooms() {
        int housekeeperId = jdbcTemplate.queryForObject("SELECT user_id FROM housekeepingSchedule WHERE is_cleaned = 1 GROUP BY user_id ORDER BY COUNT(*) DESC LIMIT 1",
                Integer.class);

        return housekeeperId;
    }

    @RequestMapping(value = "/statistics/housekeeping/max_unfinished", method = RequestMethod.POST, produces = "application/json")
    public int maxUnfinished() {
// get user_id of housekeeper with most unfinished tasks
        int housekeeperId = jdbcTemplate.queryForObject("SELECT user_id FROM housekeepingSchedule WHERE is_cleaned = 0 GROUP BY user_id ORDER BY COUNT(*) DESC LIMIT 1",
                Integer.class);

        return housekeeperId;
    }

    @RequestMapping(value = "/statistics/housekeeping/with_most_housekeepers", method = RequestMethod.POST, produces = "application/json")
    public List<Integer> withMostHousekeepers() {
// get room ids of rooms with highest number of different housekeepers
        List<Integer> roomIds = jdbcTemplate.queryForList("SELECT room_id FROM housekeepingSchedule GROUP BY room_id, user_id ORDER BY COUNT(DISTINCT user_id) DESC", Integer.class);
        return roomIds;
    }


    @RequestMapping(value = "/statistics/bookings/price/amenity", method = RequestMethod.POST, produces = "application/json")
    public double averagePriceWithAmenities(@RequestBody Map<String, Object> payload) {
        int k = (int) payload.get("K");
        String query = "SELECT AVG(booking.price) FROM booking JOIN inventory ON booking.room_id = inventory.room_id GROUP BY booking.room_id HAVING COUNT(inventory.amenities_id) >= ?";
        return jdbcTemplate.queryForObject(query, new Object[]{k}, Double.class);
    }



    @RequestMapping(value = "/statistics/bookings/max_room/price", method = RequestMethod.POST, produces = "application/json")
    public double averagePriceOfMaxBookedRooms(@RequestBody Map<String, Object> payload) {
        int t = (int) payload.get("T");
        String query = "SELECT AVG(booking.price) FROM booking JOIN (SELECT room_id, COUNT(*) as count FROM booking WHERE DATEDIFF(checkout_date, checkin_date) >= ? GROUP BY room_id ORDER BY count DESC LIMIT 1) as max_booked_rooms ON booking.room_id = max_booked_rooms.room_id";
        return jdbcTemplate.queryForObject(query, new Object[]{t}, Double.class);
    }

    @RequestMapping(value = "/statistics/amenity/max", method = RequestMethod.POST, produces = "application/json")
    public List<Integer> mostCommonAmenities() {
        List<Integer> amenityIds = jdbcTemplate.queryForList("SELECT amenities_id FROM inventory GROUP BY amenities_id ORDER BY COUNT(*) DESC", Integer.class);
        return amenityIds;
    }


    @RequestMapping(value = "/statistics/amenity/difference", method = RequestMethod.POST, produces = "application/json")
    public double priceDifference() {
        int mostCommonAmenity = jdbcTemplate.queryForObject("SELECT amenities_id FROM inventory GROUP BY amenities_id ORDER BY COUNT(*) DESC LIMIT 1", Integer.class);

        // get the second most common amenity
        int secondMostCommonAmenity = jdbcTemplate.queryForObject("SELECT amenities_id FROM inventory GROUP BY amenities_id ORDER BY COUNT(*) DESC LIMIT 1,1", Integer.class);

        // get the total price of rooms which contain the most common amenity but not the second most common amenity
        double totalPrice1 = jdbcTemplate.queryForObject("SELECT SUM(price) FROM booking JOIN inventory ON booking.room_id = inventory.room_id WHERE inventory.amenities_id = ? AND inventory.amenities_id != ?", new Object[]{mostCommonAmenity, secondMostCommonAmenity}, Double.class);

        // get the total price of rooms which does not contain the the most common amenity but contains the second most common amenity
        double totalPrice2 = jdbcTemplate.queryForObject("SELECT SUM(price) FROM booking JOIN inventory ON booking.room_id = inventory.room_id WHERE inventory.amenities_id != ? AND inventory.amenities_id = ?", new Object[]{mostCommonAmenity, secondMostCommonAmenity}, Double.class);

        // calculate the difference
        double priceDifference = totalPrice1 - totalPrice2;

        return priceDifference;
    }








}


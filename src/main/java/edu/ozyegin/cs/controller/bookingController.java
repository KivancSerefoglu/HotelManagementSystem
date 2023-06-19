package edu.ozyegin.cs.controller;

import edu.ozyegin.cs.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping
@CrossOrigin
public class bookingController {

    @Autowired
    private PlatformTransactionManager transactionManager;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    final int batchSize = 10;

//
//    INSERT INTO booking (room_id, start_date, end_date, status)
//    SELECT ?, ?, ?, 'Confirmed'
//    FROM room
//    WHERE room.id = ? AND room.id NOT IN (
//            SELECT booking.room_id
//            FROM booking
//            WHERE (? BETWEEN start_date AND end_date OR ? BETWEEN start_date AND end_date) AND status = 'Confirmed'
//            ) LIMIT 1;


    final String makingPS="INSERT INTO booking (room_id, user_id, checkin_date, checkout_date,price,guest_count) VALUES(?,?,?,?,?,?)";
    final String cancelPS="DELETE FROM booking WHERE book_id= ?, is_paid=0";
    final String changeDatePS="UPDATE booking SET start_date = '?', end_date = '?' WHERE booking_id = ?";
    final String changePricePS="UPDATE booking SET price = ? WHERE booking_id = ?";
    final String getAllPS="SELECT * FROM booking";
    final String payPS="UPDATE booking SET is_paid = 1 WHERE booking_id = ?";
    final String checkInPS="UPDATE booking SET confirmation_status = 1, checkin_date='?'  WHERE booking_id = ?";
    final String checkOutPS="UPDATE booking SET confirmation_status = 2, checkout_date='?' WHERE booking_id = ?";




    @RequestMapping(value = "/booking/make_booking", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity make_booking(@RequestBody Map<String, Object>[] payload) {

        // prepare data for usage
        List<booking> bookings = new ArrayList<>();
        for (Map<String, Object> entity : payload) {
            //int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));

           // if (RoleId==1 || RoleId==2){
                booking booking1 = new booking();
                booking1.setUser_id((int) entity.get("UserId"));
                booking1.setRoom_id((int) entity.get("RoomId"));
                booking1.setCheckin_date((String) entity.get("DayStart"));
                booking1.setCheckout_date((String) entity.get("DayEnd"));
                booking1.setPrice((double) entity.get("Price"));
                booking1.setGuest_count((int) entity.get("guestCount"));
                bookings.add(booking1);
          //  }

        }

        // init Transaction Manager
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);

        // create response's structure
        Map<String, Object> response = new HashMap<>();

        try {
            // INSERT INTO Samples using a PREPARED STATEMENT
            Objects.requireNonNull(jdbcTemplate).batchUpdate(makingPS, bookings, batchSize,
                    (ps, booking1) -> {
                        ps.setInt(1, booking1.getRoom_id());
                        ps.setInt(2, booking1.getUser_id());
                        ps.setString(3, booking1.getCheckin_date());
                        ps.setString(4, booking1.getCheckout_date());
                        ps.setDouble(5, booking1.getPrice());
                        ps.setInt(6, booking1.getGuest_count());
                    });

            // commit changes to database
            transactionManager.commit(txStatus);

            response.put("success", true);  // prepare data to respond with
        } catch (Exception exception) {
            // revert changes planned
            transactionManager.rollback(txStatus);

            // prepare data to respond with
            response.put("success", false);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/booking/cancel_booking", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity cancel(@RequestBody Map<String, Object>[] payload) {

        List<booking> bookings = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (RoleId==1 || RoleId==2){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                // create response's structure

                try {
                    int rowsAffected = jdbcTemplate.update(cancelPS, (int) entity.get("BookingId"));

                    if (rowsAffected == 0) {
                        response.put("success", false);
                    } else {
                        // commit changes to database
                        transactionManager.commit(txStatus);
                        response.put("success", true);
                    }
                } catch (Exception exception) {
                    // revert changes planned
                    transactionManager.rollback(txStatus);
                    response.put("success", false);
                }
            }else {
                response.put("success", false);
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @RequestMapping(value = "/booking/modify/change_date", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity change_date(@RequestBody Map<String, Object>[] payload) {

        List<booking> bookings = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (RoleId==1 || RoleId==2){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                // create response's structure

                try {
                    // UPDATE amenities using a PREPARED STATEMENT
                    int rowsAffected = jdbcTemplate.update(changeDatePS, (String) entity.get("DayStart"), (String) entity.get("DayEnd"),(int) entity.get("BookingId"));

                    if (rowsAffected == 0) {
                        response.put("success", false);
                    } else {
                        // commit changes to database
                        transactionManager.commit(txStatus);
                        response.put("success", true);
                    }
                } catch (Exception exception) {
                    // revert changes planned
                    transactionManager.rollback(txStatus);
                    response.put("success", false);
                }
            }else {
                response.put("success", false);
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @RequestMapping(value = "/booking/modify/change_price", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity change_price(@RequestBody Map<String, Object>[] payload) {

        List<booking> bookings = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (RoleId==1 || RoleId==2){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                // create response's structure

                try {
                    // UPDATE amenities using a PREPARED STATEMENT
                    int rowsAffected = jdbcTemplate.update(changePricePS,(double) entity.get("NewPrice"),(int) entity.get("BookingId"));

                    if (rowsAffected == 0) {
                        response.put("success", false);
                    } else {
                        // commit changes to database
                        transactionManager.commit(txStatus);
                        response.put("success", true);
                    }
                } catch (Exception exception) {
                    // revert changes planned
                    transactionManager.rollback(txStatus);
                    response.put("success", false);
                }
            }else {
                response.put("success", false);
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/booking/get", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity get(@RequestBody Map<String, Object>[] payload) {

        List<booking> bookings = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (RoleId==1 || RoleId==2){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                String getPS="SELECT * FROM booking WHERE booking_id='"+entity.get("BookingId")+"'";

                try {
                    List<booking> data = Objects.requireNonNull(jdbcTemplate).query(getPS, new BeanPropertyRowMapper<>(booking.class));

                    response.put("booking", data);
                    response.put("status", true);

                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } catch (Exception ex) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
                }
            }else {
                response.put("success", false);
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/booking/get_all", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity get_all() {
        try {
            List<booking> data = Objects.requireNonNull(jdbcTemplate).query(getAllPS, new BeanPropertyRowMapper<>(booking.class));

            Map<String, Object> response = new HashMap<>();
            response.put("booking", data);
            response.put("status", true);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
    }

    @RequestMapping(value = "/booking/pay", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity pay (@RequestBody Map<String, Object>[] payload) {

        List<booking> bookings = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (RoleId==1 || RoleId==2){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                // create response's structure

                try {
                    // UPDATE amenities using a PREPARED STATEMENT
                    int rowsAffected = jdbcTemplate.update(payPS,(int) entity.get("BookingId"));

                    if (rowsAffected == 0) {
                        response.put("success", false);
                    } else {
                        // commit changes to database
                        transactionManager.commit(txStatus);
                        response.put("success", true);
                    }
                } catch (Exception exception) {
                    // revert changes planned
                    transactionManager.rollback(txStatus);
                    response.put("success", false);
                }
            }else {
                response.put("success", false);
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/booking/check_in", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity check_in (@RequestBody Map<String, Object>[] payload) {

        List<booking> bookings = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (RoleId==1 || RoleId==2){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                // create response's structure

                try {
                    // UPDATE amenities using a PREPARED STATEMENT
                    int rowsAffected = jdbcTemplate.update(checkInPS,(String)entity.get("Date") ,(int) entity.get("BookingId"));

                    if (rowsAffected == 0) {
                        response.put("success", false);
                    } else {
                        // commit changes to database
                        transactionManager.commit(txStatus);
                        response.put("success", true);
                    }
                } catch (Exception exception) {
                    // revert changes planned
                    transactionManager.rollback(txStatus);
                    response.put("success", false);
                }
            }else {
                response.put("success", false);
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @RequestMapping(value = "/booking/check_out", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity check_out (@RequestBody Map<String, Object>[] payload) {

        List<booking> bookings = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (RoleId==1 || RoleId==2){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                // create response's structure

                try {
                    // UPDATE amenities using a PREPARED STATEMENT
                    int rowsAffected = jdbcTemplate.update(checkOutPS,(String)entity.get("Date") ,(int) entity.get("BookingId"));

                    if (rowsAffected == 0) {
                        response.put("success", false);
                    } else {
                        // commit changes to database
                        transactionManager.commit(txStatus);
                        response.put("success", true);
                    }
                } catch (Exception exception) {
                    // revert changes planned
                    transactionManager.rollback(txStatus);
                    response.put("success", false);
                }
            }else {
                response.put("success", false);
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }




    public int getRoleId(int userID){
        return jdbcTemplate.queryForObject("SELECT role_id FROM user WHERE user_id = ?", new Object[]{userID},int.class);
    }

}

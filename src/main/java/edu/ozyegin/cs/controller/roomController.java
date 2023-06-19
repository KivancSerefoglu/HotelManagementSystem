package edu.ozyegin.cs.controller;

import edu.ozyegin.cs.entity.inventory;
import edu.ozyegin.cs.entity.room;
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
public class roomController {
    @Autowired
    private PlatformTransactionManager transactionManager;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    final int batchSize = 10;

    final String createPS="INSERT INTO room (room_name, room_type) VALUES(?,?)";
    final String updateNamePS="UPDATE room SET room_name = ? WHERE room_id = ?";
    final String updateTypePS="UPDATE room SET room_type = ? WHERE room_id = ?";
    final String deletePS="DELETE FROM room = WHERE room_id = ?";
    final String selectPS = "SELECT * FROM room";
    final String addPS ="UPDATE inventory" +
            "SET quantity = quantity + 1" +
            "WHERE amenities_id = ? AND room_id = ?;" +
            "" +
            "IF ROW_COUNT() = 0 THEN" +
            "   INSERT INTO inventory (amenities_name, room_id, quantity)" +
            "   VALUES (?, ?, 1);" +
            "END IF;";
    final String removePS="UPDATE inventory" +
            "SET quantity = quantity - 1" +
            "WHERE amenities_id = ? AND room_id = ? AND quantity > 0;" +
            "" +
            "DELETE FROM inventory " +
            "WHERE amenities_id = ? AND room_id = ? AND quantity <= 0;";




    @RequestMapping(value = "/room/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity create(@RequestBody Map<String, Object>[] payload) {

        // prepare data for usage
        List<room> rooms = new ArrayList<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));

            if (RoleId==1){
                room room1 = new room();
                room1.setRoom_type((int) entity.get("RoomTypeId"));
                room1.setRoom_name((String) entity.get("RoomName"));
                rooms.add(room1);
            }

        }

        // init Transaction Manager
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);

        // create response's structure
        Map<String, Object> response = new HashMap<>();

        try {
            // INSERT INTO Samples using a PREPARED STATEMENT
            Objects.requireNonNull(jdbcTemplate).batchUpdate(createPS, rooms, batchSize,
                    (ps, room1) -> {
                        ps.setString(1, room1.getRoom_name());
                        ps.setInt(2, room1.getRoom_type());
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

    @RequestMapping(value = "/room/modify/rename", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity modifyName(@RequestBody Map<String, Object>[] payload) {

        List<room> rooms = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (RoleId==1){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                // create response's structure

                try {
                    // UPDATE amenities using a PREPARED STATEMENT
                    int rowsAffected = jdbcTemplate.update(updateNamePS, (String) entity.get("NewName"), (int) entity.get("RoomId"));

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

    @RequestMapping(value = "/room/modify/change_type", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity modifyType(@RequestBody Map<String, Object>[] payload) {

        List<room> rooms = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (RoleId==1){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                // create response's structure

                try {
                    // UPDATE amenities using a PREPARED STATEMENT
                    int rowsAffected = jdbcTemplate.update(updateTypePS, (int) entity.get("RoomTypeId"), (int) entity.get("RoomId"));

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

    @RequestMapping(value = "/room/modify/delete", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity delete(@RequestBody Map<String, Object>[] payload) {

        List<room> rooms = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (RoleId==1){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                try {
                    int rowsAffected = jdbcTemplate.update(deletePS, (int) entity.get("RoomId"));

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
            }else{
                response.put("success", false);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/room/amenity/add", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity add(@RequestBody Map<String, Object>[] payload) {

        List<inventory> inventories = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));



            if (RoleId==1){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                // create response's structure

                try {
                    // UPDATE amenities using a PREPARED STATEMENT
                    int rowsAffected = jdbcTemplate.update(addPS, (int) entity.get("AmenityId"), (int) entity.get("RoomId"), (int) entity.get("AmenityId"), (int) entity.get("RoomId"));

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

    @RequestMapping(value = "/room/amenity/remove", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity remove(@RequestBody Map<String, Object>[] payload) {

        List<inventory> inventories = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));



            if (RoleId==1){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                // create response's structure

                try {
                    int rowsAffected = jdbcTemplate.update(removePS, (int) entity.get("AmenityId"), (int) entity.get("RoomId"), (int) entity.get("AmenityId"), (int) entity.get("RoomId"));

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

    @RequestMapping(value = "/room/get_all", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity get_all() {
        try {
            List<room> data = Objects.requireNonNull(jdbcTemplate).query(selectPS, new BeanPropertyRowMapper<>(room.class));

            Map<String, Object> response = new HashMap<>();
            response.put("room", data);
            response.put("status", true);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
    }


    @RequestMapping(value = "/room/get_available_for_date", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity getAvailableRoom(@RequestBody Map<String, Object>[] payload) {


        List<room> rooms = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));
            String startDate = (String) entity.get("StartDate");
            String endDate = (String) entity.get("EndDate");

            String selectAvailableRoomPS ="SELECT * " +
                    "FROM room" +
                    "LEFT JOIN booking" +
                    "ON room.room_id = booking.room_id" +
                    "AND (" +
                    "    (booking.checkin_date BETWEEN '"+startDate+"'AND'"+endDate+"')" +
                    "    OR" +
                    "    (booking.checkout_date BETWEEN '"+startDate+"'AND'"+endDate+"')" +
                    "    OR" +
                    "    (booking.checkin_date < '"+startDate+"' AND booking.checkout_date > '"+endDate+"')" +
                    ")" +
                    "WHERE booking.room_id IS NULL;";




            if (RoleId==1 || RoleId==2){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                try {
                    List<room> data = Objects.requireNonNull(jdbcTemplate).query(selectAvailableRoomPS,new BeanPropertyRowMapper<>(room.class));

                    response.put("room", data);
                    response.put("status", true);

                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } catch (Exception ex) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
                }
            }else{
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }




    public int getRoleId(int userID){
        return jdbcTemplate.queryForObject("SELECT role_id FROM user WHERE user_id = ?", new Object[]{userID},int.class);
    }


}

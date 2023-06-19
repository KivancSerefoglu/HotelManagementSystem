package edu.ozyegin.cs.controller;

import edu.ozyegin.cs.entity.amenities;
import edu.ozyegin.cs.entity.booking;
import edu.ozyegin.cs.entity.housekeepingSchedule;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping
@CrossOrigin
public class housekeepingController {

    @Autowired
    private PlatformTransactionManager transactionManager;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    final int batchSize = 10;

    final String addSchedulePS="INSERT INTO housekeepingSchedule (user_id,room_id,is_cleaned,cleaning_date) VALUES(?,?,?,?)";
    final String updatePS="UPDATE housekeepingSchedule SET is_cleaned = ?, cleaning_date = ? WHERE room_id = ?";
    final String getNotCleanPS ="SELECT room_id FROM housekeepingSchedule WHERE is_cleaned = ?";

    @RequestMapping(value = "/housekeeping/schedule", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity create(@RequestBody Map<String, Object>[] payload) {

        // prepare data for usage
        List<housekeepingSchedule> schedules = new ArrayList<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));

            if (RoleId==1 || RoleId==2){
                housekeepingSchedule schedule = new housekeepingSchedule();
                schedule.setUser_id((int) entity.get("UserId"));
                schedule.setRoom_id((int) entity.get("RoomId"));
            }
        }

        // init Transaction Manager
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);

        // create response's structure
        Map<String, Object> response = new HashMap<>();

        try {
            // INSERT INTO Samples using a PREPARED STATEMENT
            Objects.requireNonNull(jdbcTemplate).batchUpdate(addSchedulePS, schedules, batchSize,
                    (ps, schedule) -> {
                        ps.setInt(1, schedule.getUser_id());
                        ps.setInt(2, schedule.getRoom_id());
                        ps.setInt(3, 0);
                        ps.setString(4, "");

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

    @RequestMapping(value = "/housekeeping/clean", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity modifyClean(@RequestBody Map<String, Object>[] payload) {

        List<housekeepingSchedule> schedules = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {

            // init Transaction Manager
            TransactionDefinition txDef = new DefaultTransactionDefinition();
            TransactionStatus txStatus = transactionManager.getTransaction(txDef);

            // create response's structure

            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String checkout_date = dtf.format(now);

                // UPDATE amenities using a PREPARED STATEMENT
                int rowsAffected = jdbcTemplate.update(updatePS, 1,checkout_date,(int) entity.get("RoomId"));

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

        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @RequestMapping(value = "/housekeeping/schedule/get", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody int[] getRoomList(@RequestBody Map<String, Object>[] payload) {
        List<Integer> rooms = jdbcTemplate.queryForList(getNotCleanPS, new Object[] {false}, Integer.class);
        return rooms.stream().mapToInt(i -> i).toArray();
    }

    public int getRoleId(int userID){
        return jdbcTemplate.queryForObject("SELECT role_id FROM user WHERE user_id = ?", new Object[]{userID},int.class);
    }
}

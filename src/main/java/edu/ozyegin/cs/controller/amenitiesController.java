package edu.ozyegin.cs.controller;

import edu.ozyegin.cs.entity.amenities;
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;
import java.util.*;

@RestController
@RequestMapping
@CrossOrigin
public class amenitiesController {

    @Autowired
    private PlatformTransactionManager transactionManager;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    final int batchSize = 10;

    final String createPS = "INSERT INTO amenities (amenities_name) VALUES(?)";
    final String selectPS = "SELECT * FROM amenities";
    final String deletePS=  "DELETE FROM amenities WHERE amenities_id = ?";
    final String updatePS = "UPDATE amenities SET amenities_name = ? WHERE amenities_id = ?";


    @RequestMapping(value = "/amenity/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity create(@RequestBody Map<String, Object>[] payload) {


        // prepare data for usage
        List<amenities> amenities = new ArrayList<>();
        for (Map<String, Object> entity : payload) {
                //int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));
            System.out.println("AAAAAAAAAAAAAAA");



            if (1==1){
                System.out.println("bbbbbbbbbbbbbbbbbbbbbb");

                    amenities amenities1 = new amenities();
                    amenities1.setAmenities_name((String) entity.get("AmenityName"));
                    amenities.add(amenities1);
                }

        }

        // init Transaction Manager
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);

        // create response's structure
        Map<String, Object> response = new HashMap<>();

        try {

            System.out.println("cccccccccccccccccccc");

            // INSERT INTO Samples using a PREPARED STATEMENT
            Objects.requireNonNull(jdbcTemplate).batchUpdate(createPS, amenities, batchSize,
                    (ps, sample) -> {
                        System.out.println("dddddddddddddddddddddddddddddd");
                        ps.setString(1, sample.getAmenities_name());
                    });




            // commit changes to database
            transactionManager.commit(txStatus);

            response.put("success", true);  // prepare data to respond with
        } catch (Exception exception) {
            // revert changes planned
            transactionManager.rollback(txStatus);

            // prepare data to respond with
            response.put("success", false);
            response.put("message", "Failed inserting Samples");

        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/amenity/modify", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity modify(@RequestBody Map<String, Object>[] payload) {

        List<amenities> amenities = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            //int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (1==1){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                // create response's structure

                try {
                    // UPDATE amenities using a PREPARED STATEMENT
                    int rowsAffected = jdbcTemplate.update(updatePS, (String) entity.get("AmenityName"), (int) entity.get("AmenityId"));

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

    @RequestMapping(value = "/amenity/delete", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity delete(@RequestBody Map<String, Object>[] payload) {

        List<amenities> amenities = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (Map<String, Object> entity : payload) {
            //int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));


            if (1==1){
                // init Transaction Manager
                TransactionDefinition txDef = new DefaultTransactionDefinition();
                TransactionStatus txStatus = transactionManager.getTransaction(txDef);

                try {
                    // UPDATE amenities using a PREPARED STATEMENT
                    int rowsAffected = jdbcTemplate.update(deletePS, (int) entity.get("AmenityId"));

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

    @RequestMapping(value = "/amenity/get_all", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity get_all() {
        try {
            List<amenities> data = Objects.requireNonNull(jdbcTemplate).query(selectPS, new BeanPropertyRowMapper<>(amenities.class));

            Map<String, Object> response = new HashMap<>();
            response.put("amenities", data);
            response.put("status", true);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
    }

//    public int getRoleId(int userID){
//        return jdbcTemplate.queryForObject("SELECT role_id FROM user WHERE user_id = ?", new Object[]{userID},int.class);
//    }
}

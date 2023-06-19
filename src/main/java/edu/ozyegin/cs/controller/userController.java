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
public class userController {
    @Autowired
    private PlatformTransactionManager transactionManager;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    final int batchSize = 10;

    final String createPS="INSERT INTO user (role_id, user_name, user_phone, user_mail) values  (?,?,?,?)";
    final String updatePS="UPDATE user SET user_name = ? WHERE user_id = ?";


    @RequestMapping(value = "/user/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity create(@RequestBody Map<String, Object>[] payload) {

        // prepare data for usage
        List<user> users = new ArrayList<>();
        for (Map<String, Object> entity : payload) {
            int RoleId =  getRoleId((Integer) entity.get("CallerUserId"));

            if (RoleId==1){
                user user1 = new user();
                user1.setUser_name((String) entity.get("UserName"));
                user1.setRole_id((int) entity.get("UserTypeId"));
                users.add(user1);
            }

        }

        // init Transaction Manager
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);

        // create response's structure
        Map<String, Object> response = new HashMap<>();

        try {
            // INSERT INTO Samples using a PREPARED STATEMENT
            Objects.requireNonNull(jdbcTemplate).batchUpdate(createPS, users, batchSize,
                    (ps, user1) -> {
                        ps.setString(1, user1.getUser_name());
                        ps.setInt(2, user1.getRole_id());
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


    @RequestMapping(value = "/user/modify/rename", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity rename(@RequestBody Map<String, Object>[] payload) {

        List<user> users = new ArrayList<>();
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
                    int rowsAffected = jdbcTemplate.update(updatePS, (String) entity.get("NewName"), (int) entity.get("UserId"));

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
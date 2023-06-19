package edu.ozyegin.cs.controller;

import java.util.*;

import edu.ozyegin.cs.entity.amenities;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

public class amenitiesControllerTest extends IntegrationTestSuite {

    private List<amenities> generateAmenities(int size) {
        ArrayList<amenities> amenities1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            amenities1.add(new amenities()
                    .amenities_name(RandomStringUtils.random(random(10), true, true)));
        }
        return amenities1;
    }


    @Test
    public void helloWorld() throws Exception {
        HashMap response = getMethod("/amenity", HashMap.class);
        Assert.assertEquals("Hello World", response.get("message"));
    }

    @Test
    public void create1() throws Exception {
        List<amenities> amenities1 = generateAmenities(1);

        postMethod("/amenity/create", String.class, amenities1);


        List<amenities> data = Objects.requireNonNull(jdbcTemplate)
                .query("SELECT * FROM amenities", new BeanPropertyRowMapper<>(amenities.class));

        assertTwoListEqual(amenities1, data);
    }



}


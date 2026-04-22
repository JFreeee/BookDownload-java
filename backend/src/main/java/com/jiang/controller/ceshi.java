package com.jiang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ceshi {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-db")
    public String test() {
        Integer count = jdbcTemplate.queryForObject(
            "select count(*) from ebooks",
            Integer.class
        );
        return "ebooks数量: " + count;
    }
}

package com.rishabh.RecipeService.controller;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class ContainerBaseTest {

    @Container
    public static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest").withDatabaseName("recipe_db");

        MY_SQL_CONTAINER.start();
        System.setProperty("DB_URL", MY_SQL_CONTAINER.getJdbcUrl());
        System.setProperty("DB_USERNAME", MY_SQL_CONTAINER.getUsername());
        System.setProperty("DB_PASSWORD", MY_SQL_CONTAINER.getPassword());
        System.setProperty("DB_DRIVER", MY_SQL_CONTAINER.getDriverClassName());
    }
}

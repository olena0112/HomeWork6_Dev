package org.example;
import org.flywaydb.core.Flyway;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FlywayMigration {
    public static void main(String[] args) {

        String jdbcUrl = "jdbc:h2:./megasoft";
        String jdbcUser = "sa";
        String jdbcPassword ="";
            Flyway flyway = Flyway.configure().dataSource(jdbcUrl, jdbcUser, jdbcPassword).load();

            flyway.migrate();

            System.out.println("Flyway migration completed.");

    }
}



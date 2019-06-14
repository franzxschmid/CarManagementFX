package de.gfn.carmanagement.mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbFactory {

    private DbFactory() {
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/carmanagement?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
    }
}

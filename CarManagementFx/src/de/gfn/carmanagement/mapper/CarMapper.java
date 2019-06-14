package de.gfn.carmanagement.mapper;

import de.gfn.carmanagement.entity.Car;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author tlubowiecki
 */
public class CarMapper extends AbstractMapper<Car>{

    public CarMapper() {
        super("cars");
    }

    @Override
    public Car create(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getInt("id"));
        car.setRegistration(rs.getString("registration"));
        car.setBrand(rs.getString("brand"));
        return car;
    }
}

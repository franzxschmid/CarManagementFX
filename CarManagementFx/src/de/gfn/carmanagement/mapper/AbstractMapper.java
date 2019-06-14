/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gfn.carmanagement.mapper;

import de.gfn.carmanagement.entity.AbstractEntity;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tlubowiecki
 */
public abstract class AbstractMapper<T extends AbstractEntity> implements Mapper<T> {

    public final String TABLE;

    public AbstractMapper(String table) {
        this.TABLE = table;
    }
    
    @Override
    public List<T> find() throws SQLException {
        try(Connection dbh = DbFactory.getConnection(); Statement stmt = dbh.createStatement()) {
            stmt.execute("SELECT * FROM " + TABLE + " ORDER BY id");
            ResultSet results = stmt.getResultSet();
            List<T> list = new ArrayList<>();
            while(results.next()) {
                list.add(create(results));
            }
            return list;
        }
    }

    @Override
    public T find(int id) throws SQLException {
        try(Connection dbh = DbFactory.getConnection(); Statement stmt = dbh.createStatement()) {
            stmt.execute("SELECT * FROM " + TABLE + " WHERE id = " + id);
            ResultSet results = stmt.getResultSet();
            results.next();
            return create(results);
        }
    }
    
    private boolean execute(String sql) throws SQLException {
        try(Connection dbh = DbFactory.getConnection(); Statement stmt = dbh.createStatement()) {
            stmt.execute(sql);
            return stmt.getUpdateCount() > 0;
        }
    }
    
    private boolean executePrepared(String sql, T t) throws SQLException, RuntimeException {
        try(Connection dbh = DbFactory.getConnection(); PreparedStatement stmt = dbh.prepareStatement(sql)) {
            int i = 0;
            for(Field f : t.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                try {
                    stmt.setObject(++i, f.get(t));
                }
                catch(IllegalAccessException e) {
                    throw new RuntimeException("Not Accessible", e);
                }
            }
            stmt.execute();
            return stmt.getUpdateCount() > 0;
        }
    }
    
    public boolean save(T t) throws SQLException {
        
        if(t.getId() > 0) {
            return update(t);
        }
        else {
            return insert(t);
        }
    }
    
    public boolean update(T t) throws SQLException {
        
        StringBuilder fields = new StringBuilder();
        for(Field f : t.getClass().getDeclaredFields()) {
            if(fields.length() > 1) {
                fields.append(",");
            }
            fields.append(f.getName()).append(" = ?");
        }
        
        String sql = "UPDATE " + TABLE + " SET " + fields.toString() + " WHERE id = " + t.getId();
        return executePrepared(sql, t);
    }
    
    public boolean insert(T t) throws SQLException {
        
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        
        // Eigenschaften der Elternklasse
        //Field[] superFields = t.getClass().getSuperclass().getDeclaredFields();
        
        for(Field f : t.getClass().getDeclaredFields()) {
            if(fields.length() > 1) {
                fields.append(",");
                values.append(",");
            }
            fields.append(f.getName());
            values.append("?");
        }
        String sql = "INSERT INTO " + TABLE + " (" + fields.toString() + ") VALUES(" + values.toString() + ")";
        return executePrepared(sql, t);
    }
    
    

    @Override
    public boolean delete(T t) throws SQLException {
        return execute("DELETE FROM " + TABLE + " WHERE id = " + t.getId());
    }
}

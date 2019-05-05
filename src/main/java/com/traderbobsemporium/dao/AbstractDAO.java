package main.java.com.traderbobsemporium.dao;



import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

public interface AbstractDAO<T> {
    T get(long id) throws SQLException;
    List<T> getAll() throws SQLException;
    void update(T t, String[] params) throws SQLException, MalformedURLException;
    void add(T t) throws SQLException;
    void delete(long id) throws SQLException;

}

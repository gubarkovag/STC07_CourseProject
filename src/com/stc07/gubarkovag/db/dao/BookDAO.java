package com.stc07.gubarkovag.db.dao;

import com.stc07.gubarkovag.db.ConnectionManagerPostgreSQL;
import com.stc07.gubarkovag.db.IConnectionManager;
import com.stc07.gubarkovag.pojo.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookDAO {
    public static class BookDAOException extends Exception{}

    private static IConnectionManager manager;

    static {
        manager = ConnectionManagerPostgreSQL.getInstance();
    }

    public static List<Book> getAll() throws BookDAOException {
        List<Book> bookList = new ArrayList<>();
        try {
            Statement statement = manager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from public.\"book\"");
            while(resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("genre"));

                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BookDAOException();
        }

        return bookList;
    }

    public static Book getById(Integer id) throws BookDAOException {
        Book book = null;
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("select * from public.\"book\" where id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            book = new Book(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("genre"));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new BookDAOException();
        }

        return book;
    }

    public static boolean deleteById(Integer id) throws BookDAOException {
        PreparedStatement statement = null;
        try {
            statement = manager.getConnection()
                    .prepareStatement("delete from public.\"book\" where id = ?");
            statement.setInt(1, id);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BookDAOException();
        }
    }

    public static boolean deleteAll() throws BookDAOException {
        try {
            Statement statement = manager.getConnection().createStatement();
            return statement.execute("delete from public.\"book\"");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BookDAOException();
        }
    }

    public static int insertOne(Book book) throws BookDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("INSERT INTO public.\"book\"(id, name, genre)" +
                            " VALUES(?, ?, ?)");
            statement.setInt(1,
                    book.getId());
            statement.setString(2,
                    book.getName());
            statement.setString(3,
                    book.getGenre());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BookDAOException();
        }
    }

    public static int[] insertAll(List<Book> books) throws BookDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("INSERT INTO public.\"book\"(id, name, genre)" +
                            " VALUES(?, ?, ?)");
            for (Book book : books) {
                statement.setInt(1,
                        book.getId());
                statement.setString(2,
                        book.getName());
                statement.setString(3,
                        book.getGenre());

                statement.addBatch();
            }

            return statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BookDAOException();
        }
    }

    public static int update(Book changeData, Book target) throws BookDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("update public.\"book\" set id = ?, name = ?, genre = ?" +
                            " where id = ?");
            statement.setInt(1, changeData.getId());
            statement.setString(2, changeData.getName());
            statement.setString(3, changeData.getGenre());
            statement.setInt(4, target.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BookDAOException();
        }
    }

    public static int[] updateAll(Map<Book, Book> updateData) throws BookDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("update public.\"book\" set id = ?, name = ?, genre = ?" +
                            " where id = ?");

            for (Map.Entry<Book, Book> entry : updateData.entrySet()) {
                Book changeData = entry.getKey();
                Book target = entry.getValue();
                statement.setInt(1, changeData.getId());
                statement.setString(2, changeData.getName());
                statement.setString(3, changeData.getGenre());
                statement.setInt(4, target.getId());

                statement.addBatch();
            }

            return statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BookDAOException();
        }
    }
}

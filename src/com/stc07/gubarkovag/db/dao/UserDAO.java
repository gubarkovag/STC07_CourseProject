package com.stc07.gubarkovag.db.dao;

import com.stc07.gubarkovag.db.ConnectionManagerPostgreSQL;
import com.stc07.gubarkovag.db.IConnectionManager;
import com.stc07.gubarkovag.pojo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDAO {
    public static class UserDAOException extends Exception{}

    private static IConnectionManager manager;

    static {
        manager = ConnectionManagerPostgreSQL.getInstance();
    }

    public static List<User> getAll() throws UserDAOException {
        List<User> userList = new ArrayList<>();
        try {
            Statement statement = manager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from public.\"user\"");
            while(resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        User.Role.valueOf(resultSet.getString("role")));

                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserDAOException();
        }

        return userList;
    }

    public static User getById(Integer id) throws UserDAOException {
        User user = null;
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("select * from public.\"user\" where id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("login"),
                    resultSet.getString("password"),
                    User.Role.valueOf(resultSet.getString("role")));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserDAOException();
        }

        return user;
    }

    public static boolean deleteById(Integer id) throws UserDAOException {
        PreparedStatement statement = null;
        try {
            statement = manager.getConnection()
                    .prepareStatement("delete from public.\"user\" where id = ?");
            statement.setInt(1, id);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserDAOException();
        }
    }

    public static boolean deleteAll() throws UserDAOException {
        try {
            Statement statement = manager.getConnection().createStatement();
            return statement.execute("delete from public.\"user\"");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserDAOException();
        }
    }

    public static int insertOne(User user) throws UserDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("INSERT INTO public.\"user\"(id, login, password" +
                            ", role) VALUES(?, ?, ?, ?)");
            statement.setInt(1,
                    user.getId());
            statement.setString(2,
                    user.getLogin());
            statement.setString(3,
                    user.getPassword());
            statement.setString(4,
                    user.getRole().toString());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserDAOException();
        }
    }

    public static int[] insertAll(List<User> users) throws UserDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("INSERT INTO public.\"user\"(id, login, password" +
                            ", role) VALUES(?, ?, ?, ?)");
            for (User user : users) {
                statement.setInt(1,
                        user.getId());
                statement.setString(2,
                        user.getLogin());
                statement.setString(3,
                        user.getPassword());
                statement.setString(4,
                        user.getRole().toString());

                statement.addBatch();
            }

            return statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserDAOException();
        }
    }

    public static int update(User changeData, User target) throws UserDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("update public.\"user\" set id = ?, login = ?, password = ?" +
                            ", role = ? where id = ?");
            statement.setInt(1, changeData.getId());
            statement.setString(2, changeData.getLogin());
            statement.setString(3, changeData.getPassword());
            statement.setString(4, changeData.getRole().toString());
            statement.setInt(5, target.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserDAOException();
        }
    }

    public static int[] updateAll(Map<User, User> updateData) throws UserDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("update public.\"user\" set id = ?, login = ?, password = ?" +
                            ", role = ? where id = ?");

            for (Map.Entry<User, User> entry : updateData.entrySet()) {
                User changeData = entry.getKey();
                User target = entry.getValue();
                statement.setInt(1, changeData.getId());
                statement.setString(2, changeData.getLogin());
                statement.setString(3, changeData.getPassword());
                statement.setString(4, changeData.getRole().toString());
                statement.setInt(5, target.getId());

                statement.addBatch();
            }

            return statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserDAOException();
        }
    }
}

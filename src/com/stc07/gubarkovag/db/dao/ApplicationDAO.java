package com.stc07.gubarkovag.db.dao;

import com.stc07.gubarkovag.db.ConnectionManagerPostgreSQL;
import com.stc07.gubarkovag.db.IConnectionManager;
import com.stc07.gubarkovag.pojo.Application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApplicationDAO {
    public static class ApplicationDAOException extends Exception{}

    private static IConnectionManager manager;

    static {
        manager = ConnectionManagerPostgreSQL.getInstance();
    }

    public static List<Application> getAll() throws ApplicationDAOException {
        List<Application> applicationList = new ArrayList<>();
        try {
            Statement statement = manager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from public.\"application\"");
            while(resultSet.next()) {
                Application application = new Application(
                        resultSet.getInt("user_id"),
                        resultSet.getInt("book_id"),
                        Application.Status.valueOf(resultSet.getString("status")));

                applicationList.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationDAOException();
        }

        return applicationList;
    }

    public static Application getById(Integer id) throws ApplicationDAOException {
        Application application = null;
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("select * from public.\"application\" where id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            application = new Application(
                    resultSet.getInt("book_id"),
                    resultSet.getInt("user_id"),
                    Application.Status.valueOf(resultSet.getString("status")));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationDAOException();
        }

        return application;
    }

    public static boolean deleteById(Integer id) throws ApplicationDAOException {
        PreparedStatement statement = null;
        try {
            statement = manager.getConnection()
                    .prepareStatement("delete from public.\"application\" where id = ?");
            statement.setInt(1, id);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationDAOException();
        }
    }

    public static boolean deleteAll() throws ApplicationDAOException {
        try {
            Statement statement = manager.getConnection().createStatement();
            return statement.execute("delete from public.\"application\"");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationDAOException();
        }
    }

    public static int insertOne(Application application) throws ApplicationDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("INSERT INTO public.\"application\"(user_id, book_id, status)" +
                            " VALUES(?, ?, ?)");
            statement.setInt(1,
                    application.getUser_id());
            statement.setInt(2,
                    application.getBook_id());
            statement.setString(2,
                    application.getStatus().toString());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationDAOException();
        }
    }

    public static int[] insertAll(List<Application> applications) throws ApplicationDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("INSERT INTO public.\"application\"(user_id, book_id, status)" +
                            " VALUES(?, ?, ?)");
            for (Application application : applications) {
                statement.setInt(1,
                        application.getUser_id());
                statement.setInt(2,
                        application.getBook_id());
                statement.setString(3,
                        application.getStatus().toString());

                statement.addBatch();
            }

            return statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationDAOException();
        }
    }

    public static int update(Application changeData, Application target) throws ApplicationDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("update public.\"application\" set status = ?" +
                            " where user_id = ? and book_id = ?");
            statement.setString(1, changeData.getStatus().toString());
            statement.setInt(2, target.getUser_id());
            statement.setInt(3, target.getBook_id());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationDAOException();
        }
    }

    public static int[] updateAll(Map<Application, Application> updateData) throws ApplicationDAOException {
        try {
            PreparedStatement statement = manager.getConnection()
                    .prepareStatement("update public.\"application\" set status = ?" +
                            " where user_id = ? and book_id = ?");

            for (Map.Entry<Application, Application> entry : updateData.entrySet()) {
                Application changeData = entry.getKey();
                Application target = entry.getValue();
                statement.setString(1, changeData.getStatus().toString());
                statement.setInt(2, target.getUser_id());
                statement.setInt(3, target.getBook_id());

                statement.addBatch();
            }

            return statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationDAOException();
        }
    }
}

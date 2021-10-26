package com.anet.qtr4tdm.uebki;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.anet.qtr4tdm.TdmMod;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import scala.util.control.Exception.Catch;

public class SqlHelper {

    private static String user = "user";
    private static String password = "pass";
    private static String url = "google.com";

    public static Connection connection;
    public static Statement statement;
    public static ResultSet resSet;

    public static SqlHelper instance;
    public static boolean nosqlMode = true;

    public SqlHelper(String path) {
        instance = this;
        LoadDataBaseConfig(path);
        try {
            setConnection();
        }
        catch (SQLException e) {
            
        }
    }

    public void LoadDataBaseConfig (String path) {
        File f = new File(path);
        if (!f.exists()) return;
        try {
            TdmMod.logger.info(path);
            String[] values = new String(Files.readAllBytes(Paths.get(path))).split(" ");
            user = values[0];
            password = values[1];
            url = values[2];
        }
        catch (IOException e) {
            TdmMod.logger.info(e.toString());
            TdmMod.logger.info("failed to read database config!");
        }

    }

    public void setConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver"); //Настраиваем драйвер JDBC
            System.out.println("Driver was loaded");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not Found!!!");
            System.out.println(e.toString());
        }

        try {
            connection = (Connection) DriverManager.getConnection(url, user, password);
            connection.setAutoReconnect(true);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Connection can't be estabilished!!!"); //В случае если не сможет подключиться
        }

        if (connection != null) {
            System.out.println("Connection with database was created!!!"); //Если все-таки соединение установлено, выведется это сообщение
        } else System.out.println("There's fail to create connection!");
    }

    public int GetPlayerId (String name) {
        if (nosqlMode) {
            return 0;
        }
        try {
            statement = (Statement) connection.createStatement();
            resSet = statement.executeQuery("SELECT id FROM users WHERE name='" + name + "'");
            resSet.first();
            return resSet.getInt("id");
        }
        catch (SQLException e) {
            System.out.println(e.toString());
            return -1;
        }
    }

    public String GetPlayerName (int id) {
        try {
            statement = (Statement) connection.createStatement();
            resSet = statement.executeQuery("SELECT name FROM users WHERE id='" + id + "'");
            resSet.first();
            return resSet.getString("name");
        }
        catch (SQLException e) {
            System.out.println(e.toString());
            return "sqlError";
        }
    }

    public void registerPlayer(String name) throws SQLException { //Функция регистрация игрока в базе данных на основе запроса БД
        statement.executeUpdate("INSERT INTO players (name, money) VALUES ('" + name + "', '0',)");
        System.out.println("Register player");
    }

    public boolean playerRegistered(String name) throws SQLException { //Возвращает true, если игрок уже зарегистрирован в системе
        statement = (Statement) connection.createStatement();
        resSet = statement.executeQuery("SELECT EXISTS(SELECT * FROM players WHERE name='" + name + "')");
        resSet.next();
        if (resSet.getInt(1) == 1) {
            System.out.println("Player registered");
            return true;
        } else {
            System.out.println("Player not registered");
            return false;
        }

    }

    public static void CloseConnection () {
        if (connection != null) try { connection.close();} catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}

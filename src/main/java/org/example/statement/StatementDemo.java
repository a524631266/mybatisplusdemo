package org.example.statement;

import org.example.plus.domain.Employee;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class StatementDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Connection connection = initConnection();
        System.out.println("1、 start simple statement");
        simpleStatement(connection);
        System.out.println("2、 start prepare statement");
        prepareStatement(connection);

        System.out.println("3、 call back prepare statement");
        callBackStatement(connection);
        connection.close();
    }


    private static void simpleStatement(Connection connection) throws SQLException {
        // 得到Statement
        Statement statement = connection.createStatement();

        // 3. 设置语句sql语句  【SQL注入攻击】
        String sql = "select * from tbl_employee";
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {

            Employee temp = new Employee();

            temp.setId(rs.getInt("id"));

            temp.setLastName(rs.getString("last_name"));

            temp.setEmail(rs.getString("email"));

            temp.setGender(rs.getString("gender"));

            temp.setAge(rs.getInt("age"));
            System.out.println(temp);
        }

        //关闭流
        rs.close();
        statement.close();
    }


    private static void prepareStatement(Connection connection) throws SQLException {
        // 得到Statement
        String sql = "select * from tbl_employee where id = ?";

        // 词法、语义解析、语句优化【查询重写 以及 根据io、cpu成本选择不同的执行方案】
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 设置 多次传递. 可以公用一个缓存
        for (int i = 1; i < 3; i++) {
            preparedStatement.setInt(1, i);

            // 3. 设置语句sql语句
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                Employee temp = new Employee();

                temp.setId(rs.getInt("id"));

                temp.setLastName(rs.getString("last_name"));

                temp.setEmail(rs.getString("email"));

                temp.setGender(rs.getString("gender"));

                temp.setAge(rs.getInt("age"));
                System.out.println(temp);
            }
            rs.close();
        }

        //关闭流
        preparedStatement.close();
    }

    private static void callBackStatement(Connection connection) throws SQLException {
        // 得到Statement
        String sql = "{call test1(?)}";

        // 词法、语义解析、语句优化【查询重写 以及 根据io、cpu成本选择不同的执行方案】
        CallableStatement callableStatement = connection.prepareCall(sql);

        // 设置 多次传递. 可以公用一个缓存
        callableStatement.setInt(1, 1);

        // 3. 设置语句sql语句
        ResultSet rs = callableStatement.executeQuery();
        while (rs.next()) {

            Employee temp = new Employee();

            temp.setId(rs.getInt("id"));

            temp.setLastName(rs.getString("last_name"));

            temp.setEmail(rs.getString("email"));

            temp.setGender(rs.getString("gender"));

            temp.setAge(rs.getInt("age"));
            System.out.println(temp);
        }
        rs.close();


        //关闭流
        callableStatement.close();
    }

    /**
     * 初始化connection
     *
     * @return connection
     */
    private static Connection initConnection() throws IOException, ClassNotFoundException, SQLException {
        //用户输入管理员的姓名和密码
        // 1. 获取配置文件
        Properties properties = new Properties();
        properties.load(StatementDemo.class.getResourceAsStream("/jdbc.properties"));
        //获取到相关值
        String user = properties.getProperty("username");
        String password = properties.getProperty("password");
        String driver = properties.getProperty("driver");
        String url = properties.getProperty("url");
        Class.forName(driver);
        // 2、 获取connection链接。
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}

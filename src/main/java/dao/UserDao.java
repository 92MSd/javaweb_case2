package dao;

import com.example.demo2.DBConnection;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private User toUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setSex(rs.getString("sex"));
        u.setAge(rs.getInt("age"));
        u.setBirthday(rs.getString("birthday"));
        u.setType(rs.getString("type"));
        return u;
    }

    public void insert(User u) throws SQLException{
        String sql = "INSERT INTO `user` (username,password,sex,age,birthday,type) VALUES (?,?,?,?,?,?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getSex());
            ps.setInt(4, u.getAge());
            ps.setString(5, u.getBirthday());
            ps.setString(6, u.getType());
            ps.executeUpdate();
        }
    }

    public void update(User u) throws SQLException{
        String sql = "UPDATE `user` SET username=?,password=?,sex=?,age=?,birthday=?,type=? WHERE id=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getSex());
            ps.setInt(4, u.getAge());
            ps.setString(5, u.getBirthday());
            ps.setString(6, u.getType());
            ps.setInt(7, u.getId());
            ps.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM `user` WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM `user` WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? toUser(rs) : null;
            }
        }
    }

    public User validLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM `user` WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? toUser(rs) : null;
            }
        }
    }

    public List<User> findAll(String username, String usertype) throws SQLException {
        String name = username == null ? "" : username;
        String type = usertype == null ? "" : usertype;
        String sql = "SELECT * FROM `user` WHERE username LIKE ? AND type LIKE ?";
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ps.setString(2, "%" + type + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(toUser(rs));
                }
            }
        }
        return list;
    }




}

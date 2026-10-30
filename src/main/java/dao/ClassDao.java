package dao;

import com.example.demo2.DBConnection;
import entity.Classinfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDao {

    private Classinfo toClassinfo(ResultSet rs) throws SQLException {
        Classinfo c = new Classinfo();
        c.setClassname(rs.getString("classname"));
        c.setSpecno(rs.getString("specno"));
        return c;
    }

    public void insert(Classinfo classinfo) throws SQLException {
        String sql = "INSERT INTO classinfo (classname, specno) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, classinfo.getClassname());
            ps.setString(2, classinfo.getSpecno());
            ps.executeUpdate();
        }
    }

    public void deleteByName(String classname) throws SQLException {
        String sql = "DELETE FROM classinfo WHERE classname=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, classname);
            ps.executeUpdate();
        }
    }

    public Classinfo findByName(String classname) throws SQLException {
        String sql = "SELECT * FROM classinfo WHERE classname=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, classname);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? toClassinfo(rs) : null;
            }
        }
    }

    public List<Classinfo> findAll(String classname) throws SQLException {
        String name = classname == null ? "" : classname;
        String sql = "SELECT * FROM classinfo WHERE classname LIKE ?";
        List<Classinfo> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(toClassinfo(rs));
                }
            }
        }
        return list;
    }
}

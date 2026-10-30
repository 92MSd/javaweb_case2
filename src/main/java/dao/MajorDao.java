package dao;

import com.example.demo2.DBConnection;
import entity.Major;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MajorDao {

    private Major toMajor(ResultSet rs) throws SQLException {
        Major m = new Major();
        m.setSpecno(rs.getString("specno"));
        m.setSpecname(rs.getString("specname"));
        m.setCreateyear(rs.getString("createyear"));
        return m;
    }

    public void insert(Major major) throws SQLException {
        String sql = "INSERT INTO major (specno, specname, createyear) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, major.getSpecno());
            ps.setString(2, major.getSpecname());
            ps.setString(3, major.getCreateyear());
            ps.executeUpdate();
        }
    }

    public void update(Major major) throws SQLException {
        String sql = "UPDATE major SET specname=?, createyear=? WHERE specno=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, major.getSpecname());
            ps.setString(2, major.getCreateyear());
            ps.setString(3, major.getSpecno());
            ps.executeUpdate();
        }
    }

    public void deleteBySpecno(String specno) throws SQLException {
        String sql = "{call sp_delete_major(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, specno);
            cs.execute();
        }
    }

    public Major findBySpecno(String specno) throws SQLException {
        String sql = "SELECT * FROM major WHERE specno=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, specno);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? toMajor(rs) : null;
            }
        }
    }

    public List<Major> findAll(String specname, String createyear) throws SQLException {
        String name = specname == null ? "" : specname;
        String year = createyear == null ? "" : createyear;
        String sql = "SELECT * FROM major WHERE specname LIKE ? AND createyear LIKE ?";
        List<Major> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ps.setString(2, "%" + year + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(toMajor(rs));
                }
            }
        }
        return list;
    }

    public List<String> findClassinfoBySpec(String specno) throws SQLException {
        String sql = "SELECT classname FROM classinfo WHERE specno=?";
        List<String> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, specno);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("classname"));
                }
            }
        }
        return list;
    }
}

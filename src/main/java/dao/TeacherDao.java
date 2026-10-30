package dao;

import com.example.demo2.DBConnection;
import entity.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDao {

    private Teacher toTeacher(ResultSet rs) throws SQLException {
        Teacher t = new Teacher();
        t.setTno(rs.getString("tno"));
        t.setTname(rs.getString("tname"));
        t.setTdept(rs.getString("tdept"));
        t.setTschool(rs.getString("tschool"));
        return t;
    }

    public void insert(Teacher teacher) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sql1 = "INSERT INTO `user` (username, password, type) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql1)) {
                ps.setString(1, teacher.getTno());
                ps.setString(2, "123");
                ps.setString(3, "教师");
                ps.executeUpdate();
            }

            String sql2 = "INSERT INTO teacher (tno, tname, tdept, tschool) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql2)) {
                ps.setString(1, teacher.getTno());
                ps.setString(2, teacher.getTname());
                ps.setString(3, teacher.getTdept());
                ps.setString(4, teacher.getTschool());
                ps.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void update(Teacher teacher) throws SQLException {
        String sql = "UPDATE teacher SET tname=?, tdept=?, tschool=? WHERE tno=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, teacher.getTname());
            ps.setString(2, teacher.getTdept());
            ps.setString(3, teacher.getTschool());
            ps.setString(4, teacher.getTno());
            ps.executeUpdate();
        }
    }

    public void deleteByTno(String tno) throws SQLException {
        String sql = "DELETE FROM teacher WHERE tno=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tno);
            ps.executeUpdate();
        }
    }

    public Teacher findByTno(String tno) throws SQLException {
        String sql = "SELECT * FROM teacher WHERE tno=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tno);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? toTeacher(rs) : null;
            }
        }
    }

    public List<Teacher> findAll(String tname, String tdept, String tschool) throws SQLException {
        String name = tname == null ? "" : tname;
        String dept = tdept == null ? "" : tdept;
        String school = tschool == null ? "" : tschool;
        String sql = "SELECT * FROM teacher WHERE tname LIKE ? AND tdept LIKE ? AND tschool LIKE ?";
        List<Teacher> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ps.setString(2, "%" + dept + "%");
            ps.setString(3, "%" + school + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(toTeacher(rs));
                }
            }
        }
        return list;
    }
}

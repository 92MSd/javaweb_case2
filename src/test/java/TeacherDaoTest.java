import com.example.demo2.DBConnection;
import dao.TeacherDao;
import entity.Teacher;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherDaoTest {

    private final TeacherDao dao = new TeacherDao();
    private static int seq = 0;

    private Teacher newTeacher() {
        Teacher t = new Teacher();
        t.setTno("T" + (System.currentTimeMillis() % 10000000) + (seq++ % 10));
        t.setTname("测试教师");
        t.setTdept("软件工程系");
        t.setTschool("山东建筑大学");
        return t;
    }

    private String[] findUser(String username) throws SQLException {
        String sql = "SELECT password, type FROM `user` WHERE username=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new String[]{rs.getString(1), rs.getString(2)} : null;
            }
        }
    }

    private void deleteUser(String username) throws SQLException {
        String sql = "DELETE FROM `user` WHERE username=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        }
    }

    @Test
    public void testInsertAndFindByTno() throws SQLException {
        Teacher t = newTeacher();
        dao.insert(t);
        Teacher found = dao.findByTno(t.getTno());
        assertNotNull(found);
        assertEquals(t.getTname(), found.getTname());
        assertEquals(t.getTdept(), found.getTdept());

        String[] u = findUser(t.getTno());
        assertNotNull(u);
        assertEquals("123", u[0]);
        assertEquals("教师", u[1]);

        deleteUser(t.getTno());
        dao.deleteByTno(t.getTno());
    }

    @Test
    public void testUpdate() throws SQLException {
        Teacher t = newTeacher();
        dao.insert(t);
        t.setTname("改过的教师名");
        dao.update(t);
        assertEquals("改过的教师名", dao.findByTno(t.getTno()).getTname());
        deleteUser(t.getTno());
        dao.deleteByTno(t.getTno());
    }

    @Test
    public void testDeleteByTno() throws SQLException {
        Teacher t = newTeacher();
        dao.insert(t);
        dao.deleteByTno(t.getTno());
        assertNull(dao.findByTno(t.getTno()));
        deleteUser(t.getTno());
    }

    @Test
    public void testFindAllFuzzy() throws SQLException {
        Teacher t1 = newTeacher();
        Teacher t2 = newTeacher();
        dao.insert(t1);
        dao.insert(t2);
        List<Teacher> list = dao.findAll("测试", "软件", "");
        assertTrue(list.size() >= 2);
        deleteUser(t1.getTno());
        deleteUser(t2.getTno());
        dao.deleteByTno(t1.getTno());
        dao.deleteByTno(t2.getTno());
    }

    @Test
    public void testInsertRollbackWhenTeacherDuplicate() throws SQLException {
        Teacher t = newTeacher();
        dao.insert(t);
        deleteUser(t.getTno());
        assertThrows(SQLException.class, () -> dao.insert(t));
        assertNull(findUser(t.getTno()));
        dao.deleteByTno(t.getTno());
    }
}

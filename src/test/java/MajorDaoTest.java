import com.example.demo2.DBConnection;
import dao.MajorDao;
import entity.Major;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MajorDaoTest {

    private final MajorDao dao = new MajorDao();

    private static int seq = 0;   // 计数器：保证同一次运行里每次调用的专业号都不同

    // 造一个"专业号不重复且不超过10位"的测试专业
    //（major.specno 是 varchar(10)，太长会报 Data too long）
    private Major newMajor() {
        Major m = new Major();
        m.setSpecno("M" + (System.currentTimeMillis() % 10000000) + (seq++ % 10));
        m.setSpecname("测试专业");
        m.setCreateyear("2024");
        return m;
    }

    private void insertClass(String classname, String specno) throws SQLException {
        String sql = "INSERT INTO classinfo (classname, specno) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, classname);
            ps.setString(2, specno);
            ps.executeUpdate();
        }
    }

    private void deleteClass(String classname) throws SQLException {
        String sql = "DELETE FROM classinfo WHERE classname=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, classname);
            ps.executeUpdate();
        }
    }

    @Test
    public void testInsertAndFindBySpecno() throws SQLException {
        Major m = newMajor();
        dao.insert(m);
        Major found = dao.findBySpecno(m.getSpecno());
        assertNotNull(found);
        assertEquals(m.getSpecname(), found.getSpecname());
        assertEquals(m.getCreateyear(), found.getCreateyear());
        dao.deleteBySpecno(m.getSpecno());
    }

    @Test
    public void testUpdate() throws SQLException {
        Major m = newMajor();
        dao.insert(m);
        m.setSpecname("改过的专业名");
        dao.update(m);
        assertEquals("改过的专业名", dao.findBySpecno(m.getSpecno()).getSpecname());
        dao.deleteBySpecno(m.getSpecno());
    }

    @Test
    public void testDeleteBySpecno() throws SQLException {
        Major m = newMajor();
        dao.insert(m);
        dao.deleteBySpecno(m.getSpecno());
        assertNull(dao.findBySpecno(m.getSpecno()));
    }

    @Test
    public void testDeleteWhenHasClassThrows() throws SQLException {
        Major m = newMajor();
        dao.insert(m);
        String cn = "临时班" + System.currentTimeMillis();
        insertClass(cn, m.getSpecno());
        assertThrows(SQLException.class, () -> dao.deleteBySpecno(m.getSpecno()));
        deleteClass(cn);
        dao.deleteBySpecno(m.getSpecno());
    }

    @Test
    public void testFindAllFuzzy() throws SQLException {
        Major m1 = newMajor();
        Major m2 = newMajor();
        dao.insert(m1);
        dao.insert(m2);
        List<Major> list = dao.findAll("测试", "2024");
        assertTrue(list.size() >= 2);
        dao.deleteBySpecno(m1.getSpecno());
        dao.deleteBySpecno(m2.getSpecno());
    }

    @Test
    public void testFindClassinfoBySpec() throws SQLException {
        Major m = newMajor();
        dao.insert(m);
        String cn = "临时班" + System.currentTimeMillis();
        insertClass(cn, m.getSpecno());
        List<String> names = dao.findClassinfoBySpec(m.getSpecno());
        assertTrue(names.contains(cn));
        deleteClass(cn);
        dao.deleteBySpecno(m.getSpecno());
    }
}

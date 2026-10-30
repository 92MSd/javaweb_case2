import dao.ClassDao;
import entity.Classinfo;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClassDaoTest {

    private final ClassDao dao = new ClassDao();

    private Classinfo newClass() {
        Classinfo c = new Classinfo();
        c.setClassname("C" + System.currentTimeMillis());
        c.setSpecno("001");
        return c;
    }

    @Test
    public void testInsertAndFindByName() throws SQLException {
        Classinfo c = newClass();
        dao.insert(c);
        Classinfo found = dao.findByName(c.getClassname());
        assertNotNull(found);
        assertEquals(c.getClassname(), found.getClassname());
        assertEquals(c.getSpecno(), found.getSpecno());
        dao.deleteByName(c.getClassname());
    }

    @Test
    public void testDeleteByName() throws SQLException {
        Classinfo c = newClass();
        dao.insert(c);
        dao.deleteByName(c.getClassname());
        assertNull(dao.findByName(c.getClassname()));
    }

    @Test
    public void testFindAllFuzzy() throws SQLException {
        Classinfo c1 = newClass();
        Classinfo c2 = newClass();
        dao.insert(c1);
        dao.insert(c2);
        List<Classinfo> list = dao.findAll("C");
        assertTrue(list.size() >= 2);
        dao.deleteByName(c1.getClassname());
        dao.deleteByName(c2.getClassname());
    }
}

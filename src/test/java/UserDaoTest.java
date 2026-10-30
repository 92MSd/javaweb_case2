import dao.UserDao;
import entity.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {
    private final UserDao dao = new UserDao();

    private User newUser() {
        User u = new User();
        u.setUsername("student02" + System.currentTimeMillis());
        u.setPassword("123456");
        u.setSex("女");
        u.setAge(20);
        u.setBirthday("2006-11-15");
        u.setType("学生");
        return u;
    }

    private int findId(String username) throws SQLException {
        return dao.findAll(username, "").get(0).getId();
    }

    @Test
    public void testInsertAndFindById() throws SQLException {
        User u = newUser();
        dao.insert(u);
        int id = findId(u.getUsername());
        User found = dao.findById(id);
        assertNotNull(found);
        assertEquals(u.getUsername(), found.getUsername());
        assertEquals(u.getType(), found.getType());
        dao.deleteById(id);
    }

    @Test
    public void testValidLogin() throws SQLException {
        User u = newUser();
        dao.insert(u);
        int id = findId(u.getUsername());
        assertNotNull(dao.validLogin(u.getUsername(), "123456"));
        assertNull(dao.validLogin(u.getUsername(), "错误密码"));
        dao.deleteById(id);
    }

    @Test
    public void testUpdate() throws SQLException {
        User u = newUser();
        dao.insert(u);
        int id = findId(u.getUsername());
        User found = dao.findById(id);
        found.setAge(99);
        dao.update(found);
        assertEquals(99, dao.findById(id).getAge());
        dao.deleteById(id);
    }

    @Test
    public void testDeleteById() throws SQLException {
        User u = newUser();
        dao.insert(u);
        int id = findId(u.getUsername());
        dao.deleteById(id);
        assertNull(dao.findById(id));
    }

    @Test
    public void testFindAllFuzzy() throws SQLException {
        dao.insert(newUser());
        dao.insert(newUser());
        List<User> list = dao.findAll("stu", "学生");
        assertTrue(list.size() >= 2);
    }
}

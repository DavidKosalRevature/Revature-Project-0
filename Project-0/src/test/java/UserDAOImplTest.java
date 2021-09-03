import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOImplTest {

    UserDAO dao = UserDAOFactory.getUserDao();

    @Test
    void checkLogin() throws SQLException {

        assertTrue(dao.checkLogin("david", "12345"));

    }
}
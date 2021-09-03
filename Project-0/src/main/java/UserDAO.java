import java.sql.SQLException;

public interface UserDAO {
    void addUser(User user) throws SQLException;

    void addAccount(int requestId) throws SQLException;

    void requestAccount(int customerId, String accountType, double amount) throws SQLException;

    boolean viewRequest() throws SQLException;

    void acceptRequest(int requestId) throws SQLException;

    boolean checkLogin(String username, String password) throws SQLException;

    void customerAccount(User user) throws SQLException;

    void customerAccount(int id) throws SQLException;

    void deposit(int id, double amount) throws SQLException;

    void withdraw(int id, double amount) throws SQLException;

    void sendTransfer(int sendId, int receiveId, double amount) throws SQLException;

    boolean viewTransfer(int receiveId) throws SQLException;

    void acceptTransfer(int transferId) throws SQLException;

    void getUser() throws SQLException;

    User userByUsername(String username) throws SQLException;
}

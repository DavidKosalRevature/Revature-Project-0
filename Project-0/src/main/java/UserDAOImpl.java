import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
    Connection connection = null;

    public UserDAOImpl() {
        try {
            this.connection = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addUser(User user) throws SQLException {
        String sql = "insert into user (accountType, firstName, lastName, email, username, password) " +
                "values (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getAccountType());
        preparedStatement.setString(2, user.getFirstName());
        preparedStatement.setString(3, user.getLastName());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setString(5, user.getUsername());
        preparedStatement.setString(6, user.getPassword());
        int count = preparedStatement.executeUpdate();
        if (count > 0)
            System.out.println("You have successfully registered for a " +
                    user.getAccountType() + " account\n");
        else
            System.out.println("something went wrong");
    }

    @Override
    public void addAccount(int requestId) throws SQLException {
        String sql = "insert into bankaccount (customerId, accountType, balance) values (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int count = preparedStatement.executeUpdate();
        if (count > 0)
            System.out.println("\n");
        else
            System.out.println("something went wrong");
    }

    @Override
    public void requestAccount(int customerId, String accountType, double amount) throws SQLException {
        String sql = "insert into request (customerId, accountType, amount) values (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, customerId);
        preparedStatement.setString(2, accountType);
        preparedStatement.setDouble(3, amount);
        int count = preparedStatement.executeUpdate();
        if (count > 0)
            System.out.println("You have sent a request\n");
        else
            System.out.println("something went wrong");
    }

    @Override
    public boolean viewRequest() throws SQLException {
        String sql = "call getAllRequests()";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            System.out.println("There are no pending transfers\n");
            return false;
        } else {
            do {
                System.out.println("Request ID: " + resultSet.getInt("requestId"));
                System.out.println("Account Type: " + resultSet.getString("AccountType"));
                System.out.println("Customer ID: " + resultSet.getInt("customerId"));
                System.out.println("Starting balance: " + resultSet.getDouble("amount"));
                System.out.println();
            } while (resultSet.next());

            return true;
        }

    }

    @Override
    public void acceptRequest(int requestId) throws SQLException {
        String sql = "select * from request where requestId = " + requestId;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String accountType = resultSet.getString("accountType");
            int customerId = resultSet.getInt("customerId");
            double amount = resultSet.getDouble("amount");
            System.out.println(accountType + " " + customerId + " " + amount);

            String sql2 = "insert into bankaccount (customerId, accountType, balance) values (?,?,?)";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setInt(1, customerId);
            preparedStatement2.setString(2, accountType);
            preparedStatement2.setDouble(3, amount);
            int count = preparedStatement2.executeUpdate();
            if (count > 0) {
                System.out.println("You have approved a bank account\n");
                String sql3 = "delete from request where requestId = " + requestId;
                PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
                int count2 = preparedStatement3.executeUpdate();
                if (count2 > 0)
                    System.out.println("The account request has been removed from the table");
                else
                    System.out.println("something went wrong");
            } else
                System.out.println("something went wrong");
        }


    }


    @Override
    public boolean checkLogin(String username, String password) throws SQLException {
        String usernameCheck = "select * from user where username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(usernameCheck);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String passwordCheck = "select * from user where password = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(passwordCheck);
            preparedStatement2.setString(1, password);
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            if (resultSet2.next()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void customerAccount(User user) throws SQLException {
        String sql = "select * from bankaccount where customerId = " + user.getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("Account Id: " + resultSet.getString("accountId") +
                    " " + resultSet.getString("accountType") + " " +
                    resultSet.getString("balance") + "\n");
        }
    }

    @Override
    public void customerAccount(int id) throws SQLException {
        String sql = "select * from bankaccount where customerId = " + id;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("Bank Account Id: " + resultSet.getString("accountId") +
                    ", " + resultSet.getString("accountType") + "= " +
                    resultSet.getString("balance") + "\n");
        }
    }


    @Override
    public void deposit(int id, double amount) throws SQLException {
        String sql = "select * from bankaccount where accountId  = " + id;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String sql2 = "update bankaccount set balance = ? where accountId = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);

            double newBalance = amount + resultSet.getDouble("balance");
            preparedStatement2.setDouble(1, newBalance);
            preparedStatement2.setInt(2, id);
            int count = preparedStatement2.executeUpdate();
            if (count > 0) {
                System.out.println(amount + " has been deposited in your account");
                System.out.println(newBalance + " is your updated balance\n");
            } else
                System.out.println("something went wrong");

        }

    }

    @Override
    public void withdraw(int id, double amount) throws SQLException {
        String sql = "select * from bankaccount where accountId  = " + id;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String sql2 = "update bankaccount set balance = ? where accountId = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);

            double newBalance = resultSet.getDouble("balance");
            if ((newBalance - amount) > 0) {
                newBalance -= amount;
                preparedStatement2.setDouble(1, newBalance);
                preparedStatement2.setInt(2, id);
                int count = preparedStatement2.executeUpdate();
                if (count > 0) {
                    System.out.println(amount + " has been withdrawed from your account");
                    System.out.println(newBalance + " is your updated balance\n");
                } else
                    System.out.println("something went wrong");

            }else {
                System.out.println("You cannot withdraw more than what is available in your account\n");
            }

        }

    }

    @Override
    public void sendTransfer(int sendId, int receiveId, double amount) throws SQLException {
        String sql2 = "select customerId from bankaccount where accountId = " + sendId;
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
        ResultSet resultSet = preparedStatement2.executeQuery();

        if (resultSet.next()) {
            int customerId = resultSet.getInt("customerId");
            System.out.println(customerId);

            String sql = "insert into transfer (sendId, receiveId, customerId, amount) values (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, sendId);
            preparedStatement.setInt(2, receiveId);
            preparedStatement.setInt(3, customerId);
            preparedStatement.setDouble(4, amount);
            int count = preparedStatement.executeUpdate();
            if (count > 0)
                System.out.println("Your transfer has been sent\n");
            else
                System.out.println("something went wrong");
        }

    }

    @Override
    public boolean viewTransfer(int customerId) throws SQLException {
        String sql = "select * from transfer where customerId = " + customerId;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            System.out.println("There are no pending transfers\n");
            return false;
        } else {
            do {
                System.out.println("Transfer Request ID: " + resultSet.getInt("transferId"));
                System.out.println(resultSet.getDouble("amount") + " from Bank Account ID:" +
                        resultSet.getInt("receiveId"));
                System.out.println("Pending transfer to Bank Account ID: " + resultSet.getInt("sendId"));
                System.out.println();
            } while (resultSet.next());

            return true;
        }

    }

    @Override
    public void acceptTransfer(int transferId) throws SQLException {
        String sql = "select * from transfer where transferId = " + transferId;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int customerId = resultSet.getInt("customerId");
            double amount = resultSet.getDouble("amount");

            String sql2 = "select accountId from bankaccount where customerId = " + customerId;
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            if (resultSet2.next()) {
                int accountId = resultSet2.getInt("accountId");
                deposit(accountId, amount);

                String sql3 = "delete from transfer where transferId = " + transferId;
                PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
                int count = preparedStatement3.executeUpdate();
                if (count > 0)
                    System.out.println("Your transfer has been completed\n");
                else
                    System.out.println("something went wrong");
            }
        }

    }

    @Override
    public void getUser() throws SQLException {
        String sql = "call getAllUsers()";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("Account Id: " + resultSet.getInt("id"));
            System.out.println(resultSet.getString("firstName"));
            System.out.println(resultSet.getString("lastName"));
            System.out.println(resultSet.getString("email"));
            System.out.println();

        }

    }

    @Override
    public User userByUsername(String username) throws SQLException {
        User user = new User();
        String usernameCheck = "select * from user where username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(usernameCheck);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            user.setId(resultSet.getInt(1));
            user.setAccountType(resultSet.getString(2));
            user.setFirstName(resultSet.getString(3));
            user.setLastName(resultSet.getString(4));
            user.setEmail(resultSet.getString(5));
            user.setUsername(resultSet.getString(6));
            user.setPassword(resultSet.getString(7));
        }
        return user;

    }
}

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class Account {
    Scanner scan = new Scanner(System.in);
    UserDAO userDao = UserDAOFactory.getUserDao();
    //private static final Logger logger = LogManager.getLogger(Account.class);


    public void loginMenu() throws SQLException {

        String choice;
        do {
            System.out.println("What would you like to do?");
            System.out.println("Login");
            System.out.println("Register");
            System.out.println("Exit");

            choice = scan.next().toLowerCase();

            switch (choice) {
                case "login":
                    login();
                    break;
                case "register":
                    register();
                    break;
                case "exit":
                    exit();
                    break;
                default:
                    System.out.println("Please make valid choice");
                    loginMenu();
                    break;
            }
        } while (!choice.equals("exit"));
    }

    private void login() throws SQLException {


        Customer customer = new Customer();
        Employee employee = new Employee();

        System.out.println("Please enter your username: ");
        String username = scan.next();

        System.out.println("Please enter your password: ");
        String password = scan.next();

        if (userDao.checkLogin(username, password)) {
            User user = new User();
            user = userDao.userByUsername(username);

            if (Objects.equals(user.getAccountType(), "customer")) {
                customer.customerBankMenu(user);
                //logger.info(username + "has logged on");
            } else {
                employee.employeeBankMenu(user);
                //logger.info(username + "has logged on");
            }
        }

    }

    private void register() throws SQLException {


        System.out.println("Please choose an account type: ");
        System.out.println("Customer");
        System.out.println("Employee");
        String accountType = scan.next().toLowerCase();

        accountInput(accountType);


    }

    public void accountInput(String accountType) throws SQLException {
        User user = new User();
        System.out.println("Please enter your first name");
        String firstName = scan.next();
        user.setFirstName(firstName);

        System.out.println("Please enter your last name");
        String lastName = scan.next();
        user.setLastName(lastName);

        System.out.println("Please enter your email");
        String email = scan.next();
        user.setEmail(email);

        System.out.println("Please enter your username: ");
        String username = scan.next();
        user.setUsername(username);

        System.out.println("Please enter your password: ");
        String password = scan.next();
        user.setPassword(password);

        user.setAccountType(accountType);
        userDao.addUser(user);
        //logger.info("An account has been registered");


    }

    private void exit() {
        System.exit(0);
    }


}

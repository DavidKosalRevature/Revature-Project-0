import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.SQLException;
import java.util.Scanner;

public class Employee {
    Customer customer;
    Scanner scan = new Scanner(System.in);
    String choice;
    User user;
    UserDAO dao = UserDAOFactory.getUserDao();
    //private static final Logger logger = LogManager.getLogger(Employee.class);

    public void employeeBankMenu(User user) throws SQLException {
        this.user = user;
        System.out.println("Current user logged in: " + user.getUsername() + "\n");

        do {
            System.out.println("Please choose a bank option\n");
            System.out.println("account:\t Approve or Reject Account Requests");
            System.out.println("customer:\t View A Customer's Account");
//            System.out.println("log:\t View A Log of All Transactions");
            System.out.println("logout:\t Log out and go back to Account Menu");

            choice = scan.next().toLowerCase();

            switch (choice) {
                case "account":
                    accountRequest();
                    break;
                case "customer":
                    viewCustomer();
                    break;
//                case "log":
//                    viewLog();
//                    break;
                case "logout":
                    logout();
                    break;
                default:
                    System.out.println("Please choose a valid option");
                    break;
            }
        } while (!choice.equals("logout"));
    }

    private void accountRequest() throws SQLException {
        System.out.println("Here is a list of pending account requests");
        if (dao.viewRequest()) {

            System.out.println("Would you like to approve any request?");
            System.out.println("Enter yes to accept");
            String accept = scan.next().toLowerCase();
            if (accept.equals("yes")) {
                System.out.println("Enter the Account Request ID you would like to approve");
                int requestId = scan.nextInt();
                dao.acceptRequest(requestId);
            }
        }

        //logger.info("Approved or Rejected Account Request");


    }

    private void viewCustomer() throws SQLException {
        System.out.println("Here is a list of all customers");
        dao.getUser();

        System.out.println("Please choose the user account ID that you would like to view");
        int accountID = scan.nextInt();

        dao.customerAccount(accountID);
        //logger.info("viewed customer account info");

    }

    private void viewLog() {

        //PropertyConfigurator.configure("src/log4j.properties");
    }

    private void logout() throws SQLException {
        user = null;
        Account account = new Account();
        account.loginMenu();
    }
}

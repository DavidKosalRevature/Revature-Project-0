import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.Scanner;

public class Customer {
    Scanner scan = new Scanner(System.in);
    String choice;
    UserDAO dao = UserDAOFactory.getUserDao();
    User user;
    //private static final Logger logger = LogManager.getLogger(Customer.class);


    public void customerBankMenu(User user) throws SQLException {
        this.user = user;
        System.out.println("Current user logged in: " + user.getUsername() + "\n");

        do {
            System.out.println("Please choose a bank option\n");
            System.out.println("apply:\t Apply for a new bank account");
            System.out.println("view:\t View Account Balance");
            System.out.println("deposit:\t Deposit funds into account");
            System.out.println("withdraw:\t Withdraw funds from account");
            System.out.println("post: \tPost A Money Transfer");
            System.out.println("accept:\t Accept A Money Transfer");
            System.out.println("logout:\t Log out and go back to Account Menu");

            choice = scan.next().toLowerCase();

            switch (choice) {
                case "apply":
                    apply(user);
                    break;
                case "view":
                    view(user);
                    break;
                case "deposit":
                    deposit(user);
                    break;
                case "withdraw":
                    withdraw(user);
                    break;
                case "post":
                    postMoney(user);
                    break;
                case "accept":
                    acceptMoneyTransfer(user);
                    break;
                case "logout":
                    logout();
                    break;
                default:
                    System.out.println("Please make a valid choice");
                    customerBankMenu(user);
                    break;
            }
        } while (!choice.equals("logout"));
    }

    private void apply(User user) throws SQLException {
        System.out.println("Which bank account would you like?");
        System.out.println("Checking");
        System.out.println("Savings");

        String bankAccount = scan.next();

        System.out.println("Please Enter starting balance");
        double startingAmount = scan.nextDouble();

        dao.requestAccount(user.getId(), bankAccount, startingAmount);
        //logger.info("Customer has applied for a bank account");
    }

    private void view(User user) throws SQLException {
        System.out.println("Here are all your accounts");
        dao.customerAccount(user);
        //logger.info("account viewed");
    }

    private void deposit(User user) throws SQLException {
        view(user);

        System.out.println("Select the account ID you like to deposit to");
        int bankAccount = scan.nextInt();

        System.out.println("Select the amount you would like to deposit");
        double amount = scan.nextDouble();

        dao.deposit(bankAccount, amount);
        //logger.info("deposited " + amount + " into " + bankAccount);
    }

    private void withdraw(User user) throws SQLException {
        view(user);

        System.out.println("Which account would you like to withdraw from?");
        int bankAccount = scan.nextInt();

        System.out.println("Select the amount you would like to withdraw");
        double amount = scan.nextDouble();

        dao.withdraw(bankAccount, amount);
        //logger.info("withdrew " + amount + " from " + bankAccount);
    }

    private void postMoney(User user) throws SQLException {
        System.out.println("Please enter the account number you would like to send money to");
        int sendAccount = scan.nextInt();

        System.out.println("Please enter your account ID that will send the transfer");
        dao.customerAccount(user);
        int customerAccount = scan.nextInt();

        System.out.println("Please enter the amount you would like to send");
        double amount = scan.nextDouble();

        dao.sendTransfer(sendAccount, customerAccount, amount);
        dao.withdraw(customerAccount, amount);
        //logger.info("post money transfer");

    }

    private void acceptMoneyTransfer(User user) throws SQLException {
        System.out.println("Here are your pending money transfer");

        if (dao.viewTransfer(user.getId())) {

            System.out.println("Would you like to accept any transfer?");
            System.out.println("Enter yes to accept");
            String accept = scan.next().toLowerCase();
            if (accept.equals("yes")) {
                System.out.println("Enter the Transfer Request ID you would like to accept");
                int transferID = scan.nextInt();
                dao.acceptTransfer(transferID);

            }
        }

        //logger.info("accepted money transfer");
    }

    private void logout() throws SQLException {
        user = null;
        Account account = new Account();
        account.loginMenu();
    }
}

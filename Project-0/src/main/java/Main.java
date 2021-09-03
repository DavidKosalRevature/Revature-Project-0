

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        //BasicConfigurator.configure();
        Account account = new Account();

        account.loginMenu();
    }
}

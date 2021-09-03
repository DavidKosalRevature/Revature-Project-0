public class UserDAOFactory {
    private UserDAOFactory(){

    }

    private static UserDAO dao;

    public static UserDAO getUserDao(){
        if(dao == null){
            dao = new UserDAOImpl();
        }
        return dao;
    }
}

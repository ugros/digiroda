package digiroda;

import static digiroda.DigiController.LOGGER;
import java.util.HashMap;
import javafx.util.Pair;
import ussoft.USDialogs;
import static digiroda.DigiController.language;
import static digiroda.DigiController.config;
import java.util.logging.Level;

public class DigiUser extends ussoft.User {

    private static DigiDB CONNECT;
    private static String DBHOST;
    private static String DBSCHEMA;
    private String LOGINTEXT;
    private String LOGINTITLE;

    public DigiUser() {
        super();
    }

    public DigiUser(String userName, String password) {
        super(userName, password);
    }

    @Override
    protected boolean checkUser() {
        if ((getUserName() == null) || (getUserName().equals(""))) {
            Pair p = USDialogs.login(LOGINTITLE, LOGINTEXT);
            this.userName = p.getKey().toString();
            this.password = p.getValue().toString();
        }
        CONNECT = new DigiDB(DBHOST, DBSCHEMA, this.userName, this.password);
        LOGGER.log(Level.INFO,this.userName + " logged in.");
        //USDialogs.error(language.getProperty("LOGINERROR"), e.getMessage());
        return (CONNECT != null);
    }

    @Override
    protected HashMap<String, Boolean> getRights() {
        String sql = "select "
                + "`right` "
                + "from "
                + "rights "
                + "where "
                + "rights.id in ("
                + "select "
                + "rightid "
                + "from "
                + "userrights "
                + "where "
                + "userid=(select id "
                + "from users "
                + "where username='" + this.userName + "'))";
       /*getCONNECT().executeSqlToDataResult(sql);
            //Logger.getLogger(DigiUser.class.getName()).log(Level.SEVERE, "Error while getting user " + userName + "'s rights");
            LOGGER.log(Level.SEVERE,"Error while getting user " + userName + "'s rights"+"</br>"+ex.getLocalizedMessage());  
            if (LOGGER.getLevel().equals(Level.FINEST)) LOGGER.log(Level.INFO,sql);
        }  */
        return null;
    }

    @Override
    protected void initialize() {
        // setting up database connection
        DBHOST = config.getProperty("DBHOST");
        DBSCHEMA = config.getProperty("DBSCHEMA");

        // setting up texts
        LOGINTEXT = language.getProperty("LOGINTEXT");
        LOGINTITLE = language.getProperty("LOGINTITLE");

        // be careful, it isn't safe
        this.userName = config.getProperty("DBUSER");
        this.password = config.getProperty("DBPASSWORD");

    }

    public static DigiDB getCONNECT() {
        return CONNECT;
    }

    public static String getDBHOST() {
        return DBHOST;
    }

    public static String getDBSCHEMA() {
        return DBSCHEMA;
    }

}

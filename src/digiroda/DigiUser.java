//<editor-fold defaultstate="collapsed" desc="license">
/*
 * Copyright 2018 ugros.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//</editor-fold>

package digiroda;

import static digiroda.DigiController.LOGGER;
import javafx.util.Pair;
import ussoft.USDialogs;
import static digiroda.DigiController.language;
import static digiroda.DigiController.config;
import java.util.HashSet;
import java.util.logging.Level;

public class DigiUser extends ussoft.User {

    private final DigiDB CONNECTS;
    private String LOGINTEXT;
    private String LOGINTITLE;
    
    private void login() {
        
    }
    
    public DigiUser(String userName, String password) {
        super(userName, password);
        if ((getUserName() == null) || (getUserName().equals(""))) {
            Pair p = USDialogs.login(LOGINTITLE, LOGINTEXT);
            this.userName = p.getKey().toString();
            this.password = p.getValue().toString();
        }
        CONNECTS = new DigiDB(this.userName, this.password);
        this.checked=checkUser();
        LOGGER.log(Level.INFO, "User "+this.userName + " logged in."); 
        this.rights=getRights();
    }
    
    public boolean checkRight(String right) {
        return rights.contains(right);
    }

    @Override
    protected boolean checkUser() {
        return (CONNECTS != null);
    }

    @Override
    protected HashSet<String> getRights() {  
        HashSet<String> s = CONNECTS.getUserRights(this.userName);
        return s;
    }

    @Override
    protected void initialize() {
        // setting up texts
        LOGINTEXT = language.getProperty("LOGINTEXT");
        LOGINTITLE = language.getProperty("LOGINTITLE");

        // be careful, it isn't safe
        this.userName = config.getProperty("DBUSER");
        this.password = config.getProperty("DBPASSWORD");

    }

    public DigiDB getConnects() {
        return CONNECTS;
    }
}

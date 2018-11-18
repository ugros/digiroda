/**
 * 
 */
package ussoft;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author USSoft - 2018.
 *
 */
public abstract class User {
	
	protected String userName;
	protected String password;
	protected Set<String> rights= new HashSet<>();
	protected boolean checked=false;

	protected User() {
            initialize();
            if (checkUser()) {
			checked=true;
			rights=getRights();
		}
        }
	protected User(String userName, String password) {
		initialize();
                if (userName!=null) {
                    if (!userName.equals("")) {
                        this.userName=userName;
                        this.password=password;
                    }
                }
		if (checkUser()) {
			checked=true;
			rights=getRights();
		}
	}
	
	protected abstract boolean checkUser();
	protected abstract Set<String> getRights();
        protected abstract void initialize();
	
	public String getUserName() {
		return userName;
	}	

	public boolean getRight(String right){
		return rights.contains(right);		
	}
        
        public boolean getChecked() {
            return checked;
        }	
	
	@Override
	public String toString() {
		if (checked) return userName; else return "unchecked: "+userName;
	}
	
}

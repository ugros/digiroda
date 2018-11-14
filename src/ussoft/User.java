/**
 * 
 */
package ussoft;

import java.util.HashMap;

/**
 * @author USSoft - 2018.
 *
 */
public abstract class User {
	
	protected String userName;
	protected String password;
	private HashMap<String,Boolean> rights= new HashMap<String,Boolean>();
	private boolean checked=false;

	protected User() {
            initialize();
            if (checkUser()) {
			checked=true;
			rights=getRights();
		}
        }
	protected User(String userName, String password) {
		initialize();
                this.userName=userName;
		this.password=password;	                
		if (checkUser()) {
			checked=true;
			rights=getRights();
		}
	}
	
	protected abstract boolean checkUser();
	protected abstract HashMap<String,Boolean> getRights();
        protected abstract void initialize();
	
	public String getUserName() {
		return userName;
	}	

	public boolean getRight(String right){
		return rights.get(right);		
	}
        
        public boolean getChecked() {
            return checked;
        }	
	
	@Override
	public String toString() {
		if (checked) return userName; else return "unchecked: "+userName;
	}
	
}

package at.ac.tuwien.software.architectures.ws2012.server.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.NamedEvent;

import at.ac.tuwien.software.architectures.ws2012.server.domain.User;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable{

    /**
     * Default constructor. 
     */
    public LoginBean() {
        // TODO Auto-generated constructor stub
    }
    
    private User user = null;
    private String username;
    private String password;
	public User getUser() {
		return user;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
    public String login(){
    	user = null; // get the User if there is one in the DB
    	if(user != null){
    		user = null;
    		return "Welcome " + user.getName();
    	}else{
    		user = null;
    		return "Sorry. Your username or password are wrong!";
    	}
    }

}

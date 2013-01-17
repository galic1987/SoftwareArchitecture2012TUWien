package at.ac.tuwien.software.architectures.ws2012.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="USER")
public class User {

	Long id;
	String username;
	String password;
	String Name;
	String Surname;
	String email;
	int account;
	List<RequestHistory> requestHistories = new ArrayList<RequestHistory>();
	
	public User(){}

	public User(String username, String password, String name, String surname,
			String email, int account) {
		super();
		this.username = username;
		this.password = password;
		Name = name;
		Surname = surname;
		this.email = email;
		this.account = account;
	}

	@Id
	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return Name;
	}

	public String getSurname() {
		return Surname;
	}

	public String getEmail() {
		return email;
	}

	public int getAccount() {
		return account;
	}
	
	@OneToMany()
	public List<RequestHistory> getRequestHistories() {
		return requestHistories;
	}

	public void setRequestHistories(List<RequestHistory> requestHistories) {
		this.requestHistories = requestHistories;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setSurname(String surname) {
		Surname = surname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAccount(int account) {
		this.account = account;
	}
	
	

}

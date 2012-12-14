package at.ac.tuwien.software.architectures.ws2012.server.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="REQUEST_HISTORY")
public class RequestHistory {

	
	Long id;
	Date date;
	User user;
	SearchFind searchFind;
	int accountStatus;
	RequestHistory foundBy;
	
	public RequestHistory() {
		super();
	}
	
	public RequestHistory(Date date, User user, SearchFind searchFind,
			int accountStatus, RequestHistory foundBy) {
		super();
		this.date = date;
		this.user = user;
		this.searchFind = searchFind;
		this.accountStatus = accountStatus;
		this.foundBy = foundBy;
	}

	@Id
	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	@ManyToOne
	@JoinColumn(name="USER_ID")
	public User getUser() {
		return user;
	}

	public SearchFind getSearchFind() {
		return searchFind;
	}

	public int getAccountStatus() {
		return accountStatus;
	}

	@ManyToOne
	@JoinColumn(name="FOUND_BY")
	public RequestHistory getFoundBy() {
		return foundBy;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setSearchFind(SearchFind searchFind) {
		this.searchFind = searchFind;
	}

	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}

	public void setFoundBy(RequestHistory foundBy) {
		this.foundBy = foundBy;
	}
	
}

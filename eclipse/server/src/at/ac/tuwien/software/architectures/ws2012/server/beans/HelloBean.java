package at.ac.tuwien.software.architectures.ws2012.server.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import at.ac.tuwien.software.architectures.ws2012.server.domain.User;

import java.io.Serializable;

@ManagedBean
@SessionScoped
public class HelloBean implements Serializable{

	private static final long serialVersionUID = 1L;
	 
	private String name;
 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@PostConstruct
	public void init(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("software-architectures");
		EntityManager em = emf.createEntityManager();
		User user = new User("vilo", "vilo", "Viliam", "Benuska", "vilino@yahoo.de", 50);
		user.setId(Long.parseLong("0"));
		
		em.persist(user);
	}
	

}

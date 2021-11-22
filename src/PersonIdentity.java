import java.util.ArrayList;
import java.util.List;

public class PersonIdentity {
	private int personId;
	private String name;
	private List<PersonIdentity> parents = new ArrayList<>();
	private PersonIdentity partner;
	private List<PersonIdentity> children = new ArrayList<>();
	
	public PersonIdentity getPartner() {
		return partner;
	}

	public PersonIdentity(int personId, String name) {
		this.personId = personId;
		this.name = name;
	}

	public int getPersonId() {
		return personId;
	}

	public String getName() {
		return name;
	}

	public void setPartner(PersonIdentity partner) {
		this.partner = partner;
	}
	
	public boolean hasPartner() {
		return partner != null;
	}

	public List<PersonIdentity> getChildren() {
		return children;
	}

	public void addChild(PersonIdentity child) {
		children.add(child);
	}

	public List<PersonIdentity> getParents() {
		return parents;
	}
	
	public void addParent(PersonIdentity parent) {
		parents.add(parent);
	}
}

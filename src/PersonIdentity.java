
public class PersonIdentity {
	private int personId;
	private String name;
	private PersonIdentity partner;
	
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
}

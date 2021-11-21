import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FamilyTreeManagement {
	
	// Maximum string length of the tag name
	private static final int MAX_PERSON_NAME_LENGTH = 255;
	// Maximum string length of the reference
	private static final int MAX_REFERENCE_LENGTH = 500;
	// Maximum string length of the note
	private static final int MAX_NOTE_LENGTH = 500;
	
	// Map for storing name and its corresponding PersonIdentity
	private Map<String, PersonIdentity> persons = new HashMap<>();
	// Map for storing id and its corresponding PersonIdentity
	private Map<Integer, PersonIdentity> personIds = new HashMap<>();
	
	// Set for storing roots of the family tree
	private Set<PersonIdentity> roots = new HashSet<>();
	// Set for storing partners
	private Set<PersonIdentity> partnered = new HashSet<>();
	
	
	// Object for accessing family tree related database tables
	private FamilyTreeDatabaseAccess familyTreeAccess = new FamilyTreeDatabaseAccess();
	
	public FamilyTreeManagement() {
		try {
			familyTreeAccess.loadPersons(persons, personIds, roots);
			familyTreeAccess.loadPartneringRelationships(partnered, personIds);
		} catch (SQLException e) {
			throw new IllegalStateException();
		}
	}
	
	public PersonIdentity addPerson(String name) {
		// Handle illegal arguments
		if (name == null || name.equals("") || name.length() > MAX_PERSON_NAME_LENGTH) {
			throw new IllegalArgumentException();
		}
		
		// If the name already exists, then throw an exception
		if (persons.containsKey(name)) {
			throw new IllegalArgumentException("Name already exists");
		}
		
		// Create a PersonIdentity variable
		PersonIdentity personIdentity;
		try {
			
			// Insert the person into the person table
			int id = familyTreeAccess.insertPerson(name);
			
			// Instantiate the PersonIdentity class
			personIdentity = new PersonIdentity(id, name);
			
			// Add the name and person identity object to the persons map
			persons.put(name, personIdentity);
			personIds.put(id, personIdentity);
			roots.add(personIdentity);
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		return personIdentity;
	}
	
	Boolean recordAttributes(PersonIdentity person, Map<String, String> attributes) {
		return true;
	}
	
	Boolean recordReference(PersonIdentity person, String reference) {
		// Handle invalid inputs
		if (person == null || reference == null || reference == "" || reference.length() > MAX_REFERENCE_LENGTH) {
			throw new IllegalArgumentException();
		}
		
		try {
			
			// Insert reference for the given person
			familyTreeAccess.insertReference(person.getPersonId(), reference);
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		return true;
	}
	
	Boolean recordNote(PersonIdentity person, String note) {
		// Handle invalid inputs
		if (person == null || note == null || note == "" || note.length() > MAX_NOTE_LENGTH) {
			throw new IllegalArgumentException();
		}
		
		try {
			
			// Insert note for the given person
			familyTreeAccess.insertNote(person.getPersonId(), note);
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		return true;
	}
	
	Boolean recordChild(PersonIdentity parent, PersonIdentity child) {
		return true;
	}
	
	Boolean recordPartnering(PersonIdentity partner1, PersonIdentity partner2) {
		if (partner1 == null || partner2 == null) {
			throw new IllegalArgumentException();
		}
		
		if (partner1 == partner2) {
			throw new IllegalArgumentException("Cannot partner the same person");
		}
		
		if (partnered.contains(partner1) || partnered.contains(partner2)) {
			throw new IllegalArgumentException("Partnering already exists for one of the persons/both the persons");
		}
		
		try {
			// Add partnering relationship in the database
			familyTreeAccess.insertPartneringRelation(partner1.getPersonId(), partner2.getPersonId());
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		// Add both the partners in the partnered set
		partnered.add(partner1);
		partnered.add(partner2);
		
		// Set partners for both persons in the PersonIdentity object
		partner1.setPartner(partner2);
		partner2.setPartner(partner1);
		
		return true;
	}
	
	Boolean recordDissolution(PersonIdentity partner1, PersonIdentity partner2) {
		return true;
	}
}

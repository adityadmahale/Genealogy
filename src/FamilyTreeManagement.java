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
	// Set for storing children
	private Set<PersonIdentity> children = new HashSet<>();
	
	// Object for accessing family tree related database tables
	private FamilyTreeDatabaseAccess familyTreeAccess = new FamilyTreeDatabaseAccess();
	
	public FamilyTreeManagement() {
		try {
			familyTreeAccess.loadPersons(persons, personIds, roots);
			familyTreeAccess.loadPartneringRelationships(partnered, personIds);
			familyTreeAccess.loadParentChildRelationships(children, roots, personIds);
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
	
	public Boolean recordAttributes(PersonIdentity person, Map<String, String> attributes) {
		return true;
	}
	
	public Boolean recordReference(PersonIdentity person, String reference) {
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
	
	public Boolean recordNote(PersonIdentity person, String note) {
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
	
	public Boolean recordChild(PersonIdentity parent, PersonIdentity child) {
		if (parent == null || child == null) {
			throw new IllegalArgumentException();
		}
		
		if (parent == child) {
			throw new IllegalArgumentException("Cannot record child for the same person");
		}
		
		if (children.contains(child) && !roots.contains(child)) {
			throw new IllegalArgumentException("Cannot record the child twice");
		}
		
		try {
			
			// Insert parent child relation in the database
			familyTreeAccess.insertParentChild(parent.getPersonId(), child.getPersonId());
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		// Remove child from root and add to children
		roots.remove(child);
		children.add(child);
		
		// Add child for the individual and the partner
		parent.addChild(child);
		if (parent.hasPartner()) {
			parent.getPartner().addChild(child);
		}
		
		return true;
	}
	
	public Boolean recordPartnering(PersonIdentity partner1, PersonIdentity partner2) {
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
	
	public Boolean recordDissolution(PersonIdentity partner1, PersonIdentity partner2) {
		if (partner1 == null || partner2 == null) {
			throw new IllegalArgumentException();
		}
		
		if (partner1 == partner2) {
			throw new IllegalArgumentException("Cannot perform dissolution between the same person");
		}
		
		if (!arePartners(partner1, partner2)) {
			throw new IllegalArgumentException("Partnering does not exist between individuals");
		}
		
		try {
			// Add partnering relationship in the database
			familyTreeAccess.dissolvePartneringRelation(partner1.getPersonId(), partner2.getPersonId());
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		// Remove both the persons from the partnered set
		partnered.remove(partner1);
		partnered.remove(partner2);
		
		// Set partners as null for both the persons in the PersonIdentity object
		partner1.setPartner(null);
		partner2.setPartner(null);
		
		return true;
	}
	
	private boolean arePartners(PersonIdentity person1, PersonIdentity person2) {
		if (person1.getPartner() == null || person2.getPartner() == null) {
			return false;
		}
		return person1.getPartner() == person2 && person2.getPartner() == person1;
	}
}

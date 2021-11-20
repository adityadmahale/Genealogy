import java.sql.SQLException;
import java.util.Map;

public class FamilyTreeManagement {
	
	// Maximum string length of the tag name
	private static final int MAX_PERSON_NAME_LENGTH = 255;
	// Maximum string length of the reference
	private static final int MAX_REFERENCE_LENGTH = 500;
	// Maximum string length of the note
	private static final int MAX_NOTE_LENGTH = 500;
	
	// Map for storing file location and its corresponding FileIdentifier
	private Map<String, PersonIdentity> persons;
	
	// Object for accessing family tree related database tables
	private FamilyTreeDatabaseAccess familyTreeAccess = new FamilyTreeDatabaseAccess();
	
	public FamilyTreeManagement() {
		try {
			persons = familyTreeAccess.getPersons();
		} catch (SQLException e) {
			throw new IllegalStateException();
		}
	}
	
	public PersonIdentity addPerson(String name) {
		// Handle illegal arguments
		if (name == null || name.equals("") || name.length() > MAX_PERSON_NAME_LENGTH) {
			throw new IllegalArgumentException();
		}
		
		// If the file location already exists, then throw an exception
		if (persons.containsKey(name)) {
			throw new IllegalArgumentException("Name already exists");
		}
		
		// Create a FileIdentifier variable
		PersonIdentity personIdentity;
		try {
			
			// Insert the media into the media table
			int id = familyTreeAccess.insertPerson(name);
			
			// Instantiate the file identifier class
			personIdentity = new PersonIdentity(id, name);
			
			// Add the file location and identifier to the files map
			persons.put(name, personIdentity);
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
		return true;
	}
	
	Boolean recordDissolution(PersonIdentity partner1, PersonIdentity partner2) {
		return true;
	}
}

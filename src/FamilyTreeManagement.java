import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

class FamilyTreeManagement {
	
	// Attribute keys
	private static final String DOB_KEY = "date_of_birth";
	private static final String DOD_KEY = "date_of_death";
	private static final String GENDER_KEY = "gender";
	private static final String LOCATION_OF_BIRTH_KEY = "location_of_birth";
	private static final String LOCATION_OF_DEATH_KEY = "location_of_death";
	private static final String OCCUPATION_KEY = "occupation";
	
	// Maximum string length of the tag name
	private static final int MAX_PERSON_NAME_LENGTH = 255;
	// Maximum string length of the reference
	private static final int MAX_REFERENCE_LENGTH = 500;
	// Maximum string length of the note
	private static final int MAX_NOTE_LENGTH = 500;
	// Maximum string length of the gender
	private static final int MAX_GENDER_LENGTH = 1;
	// Maximum string length of the location
	private static final int MAX_LOCATION_LENGTH = 255;
	// Maximum string length of the occupation
	private static final int MAX_OCCUPATION_LENGTH = 255;
	
	// Map for storing name and its corresponding PersonIdentity
	private Map<String, PersonIdentity> persons;
	// Map for storing id and its corresponding PersonIdentity
	private Map<Integer, PersonIdentity> personIds;
	
	// Set for storing roots of the family tree
	private Set<PersonIdentity> roots;
	// Set for storing partners
	private Set<PersonIdentity> partnered;
	
	// Map for storing location and its corresponding location id
	private Map<String, Integer> locations;
	
	// Map for storing occupation and its corresponding occupation id
	private Map<String, Integer> occupations;
	
	// Object for accessing family tree related database tables
	private FamilyTreeDatabaseAccess familyTreeAccess = new FamilyTreeDatabaseAccess();
	
	public FamilyTreeManagement() {
		try {
			PersistentState.initializeFamilyTreeState();
			persons = PersistentState.getPersons();
			personIds = PersistentState.getPersonIds();
			roots = PersistentState.getRoots();
			partnered = PersistentState.getPartners();
			locations = PersistentState.getLocations();
			occupations = PersistentState.getOccupations();
		} catch (SQLException e) {
			throw new IllegalStateException();
		}
	}
	
	PersonIdentity addPerson(String name) {
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
		// Handle invalid input for the fileIdentifier
		if (person == null || attributes == null) {
			throw new IllegalArgumentException();
		}
		
		// If no attributes are passed, then return false
		if (attributes.size() == 0) {
			return false;
		}
		
		boolean isAttributePresent;
		// Check if the attribute is already present
		try {
			isAttributePresent = familyTreeAccess.isAttributePresent(person.getPersonId());
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		// If the attribute is not present, then insert a row
		// Otherwise, update the existing attributes
		
		recordAttributes(person, attributes, isAttributePresent);
	
		return true;
	}
	
	private void recordAttributes(PersonIdentity person, Map<String, String> attributes, boolean isAttributePresent) {
		// Check the date format
		String dateOfBirth = null;
		if (attributes.containsKey(DOB_KEY)) {
			dateOfBirth = attributes.get(DOB_KEY);
			if (dateOfBirth == null || dateOfBirth == "" || !Utility.isDateValid(dateOfBirth)) {
				throw new IllegalArgumentException("Date format is incorrect");
			}
		}
		
		// Check the date format
		String dateOfDeath = null;
		if (attributes.containsKey(DOD_KEY)) {
			dateOfDeath = attributes.get(DOD_KEY);
			if (dateOfDeath == null || dateOfDeath == "" || !Utility.isDateValid(dateOfDeath)) {
				throw new IllegalArgumentException("Date format is incorrect");
			}
		}
		
		// Check the gender
		String gender = null;
		if (attributes.containsKey(GENDER_KEY)) {
			gender = attributes.get(GENDER_KEY);
			if (gender == null || gender == "" || gender.length() != MAX_GENDER_LENGTH) {
				throw new IllegalArgumentException("Gender format is incorrect");
			}
		}
		
		// Check if the location of birth value is empty
		String locationOfBirth = null;
		if (attributes.containsKey(LOCATION_OF_BIRTH_KEY)) {
			locationOfBirth = attributes.get(LOCATION_OF_BIRTH_KEY);
			if (locationOfBirth == null || locationOfBirth == "" || locationOfBirth.length() > MAX_LOCATION_LENGTH) {
				throw new IllegalArgumentException("The location value cannot be null or empty or exceed max length");
			}
		}
		
		// Check if the location of birth value is empty
		String locationOfDeath = null;
		if (attributes.containsKey(LOCATION_OF_DEATH_KEY)) {
			locationOfDeath = attributes.get(LOCATION_OF_DEATH_KEY);
			if (locationOfDeath == null || locationOfDeath == "" || locationOfDeath.length() > MAX_LOCATION_LENGTH) {
				throw new IllegalArgumentException("The location value cannot be null or empty or exceed max length");
			}
		}
		
		// Check if the location of birth value is empty
		String occupation = null;
		if (attributes.containsKey(OCCUPATION_KEY)) {
			occupation = attributes.get(OCCUPATION_KEY);
			if (occupation == null || occupation == "" || occupation.length() > MAX_OCCUPATION_LENGTH) {
				throw new IllegalArgumentException("The occupation value cannot be null or empty or exceed max length");
			}
		}
		
		if (dateOfBirth == null && dateOfDeath == null && gender == null && occupation == null && locationOfBirth == null && locationOfDeath == null) {
			return;
		}
		
		int locationOfBirthId = getLocationId(locationOfBirth);
		int locationOfDeathId = getLocationId(locationOfDeath);
		int occupationId = getOccupationId(occupation);
		
		try {
			if (isAttributePresent) {
				// Update person attribute
				familyTreeAccess.updatePersonAttributes(person.getPersonId(), dateOfBirth, dateOfDeath, gender, locationOfBirthId, locationOfBirth, locationOfDeathId, locationOfDeath, occupationId, occupation, locations, occupations);
			} else {
				familyTreeAccess.insertPersonAttributes(person.getPersonId(), dateOfBirth, dateOfDeath, gender, locationOfBirthId, locationOfBirth, locationOfDeathId, locationOfDeath, occupationId, occupation, locations, occupations);
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
	}

	//Get the location id corresponding to a location name
	private int getLocationId(String location) {
		int locationId = 0;
		
		// If the location is already present then get that locationId
		if (locations.containsKey(location)) {
			locationId = locations.get(location);
		}
		return locationId;
	}
	
	//Get the occupation id corresponding to a occupation name
	private int getOccupationId(String occupation) {
		int occupationId = 0;
		
		// If the occupation is already present then get that occupationId
		if (occupations.containsKey(occupation)) {
			occupationId = occupations.get(occupation);
		}
		return occupationId;
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
		// Check if the inputs are invalid
		if (Utility.isInvalid(parent, child)) {
			throw new IllegalArgumentException();
		}
		
		// Check if both the input objects are same
		if (Utility.isSamePerson(parent, child)) {
			throw new IllegalArgumentException("Cannot record child for the same person");
		}
		
		if (child.hasParent(parent)) {
			throw new IllegalArgumentException("The given parent is already linked to the child");
		}
		
		if (!child.isParentAdditionAllowed()) {
			throw new IllegalArgumentException("A child cannot have more than two parents");
		}
		
		try {
			
			// Insert parent child relation in the database
			familyTreeAccess.insertParentChild(parent.getPersonId(), child.getPersonId());
			
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		// Remove child from root and add to children
		roots.remove(child);
		
		// Update relationships in the PersonIdentity objects
		Utility.updateParentChildRelationship(parent, child);
		
		// Update root ancestors
		child.updateRootAncestors(parent);

		return true;
	}
	
	Boolean recordPartnering(PersonIdentity partner1, PersonIdentity partner2) {
		// Check if the inputs are invalid
		if (Utility.isInvalid(partner1, partner2)) {
			throw new IllegalArgumentException();
		}
		
		// Check if both the input objects are same
		if (Utility.isSamePerson(partner1, partner2)) {
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
		// Check if the inputs are invalid
		if (Utility.isInvalid(partner1, partner2)) {
			throw new IllegalArgumentException();
		}
		
		// Check if both the input objects are same
		if (Utility.isSamePerson(partner1, partner2)) {
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
	
	// Checks if the persons are partners
	private boolean arePartners(PersonIdentity person1, PersonIdentity person2) {
		// Check if the partner is null
		if (person1.getPartner() == null || person2.getPartner() == null) {
			return false;
		}
		return person1.getPartner() == person2 && person2.getPartner() == person1;
	}
}

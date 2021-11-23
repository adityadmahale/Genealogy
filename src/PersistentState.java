import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PersistentState {
	
	// Family tree related table names
	private static final String TABLE_PERSON = "person";
	private static final String TABLE_PARTNER = "partner";
	private static final String TABLE_CHILD = "child";
	
	// Family tree related column names
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PERSON_ID = "person_id";
	private static final String COLUMN_PARTNER_ID = "partner_id";
	private static final String COLUMN_CHILD_ID = "child_id";
	
	// Map for storing name and its corresponding PersonIdentity
	private static Map<String, PersonIdentity> persons;
	// Map for storing id and its corresponding PersonIdentity
	private static Map<Integer, PersonIdentity> personIds;
	
	// Set for storing roots of the family tree
	private static Set<PersonIdentity> roots;
	// Set for storing partners
	private static Set<PersonIdentity> partnered;
	// Set for storing children
	private static Set<PersonIdentity> children;
	
	public static void initializeFamilyTreeState() throws SQLException {
		loadPersons();
		loadPartneringRelationships();
		loadParentChildRelationships();
		updateRootAncestors();
	}
	
	public static Map<String, PersonIdentity> getPersons() throws SQLException {
		return persons;
	}
	
	public static Map<Integer, PersonIdentity> getPersonIds() throws SQLException {
		return personIds;
	}
	
	public static Set<PersonIdentity> getRoots() throws SQLException {
		return roots;
	}
	
	public static Set<PersonIdentity> getPartners() throws SQLException {
		return partnered;
	}
	
	public static Set<PersonIdentity> getChildren() throws SQLException {
		return children;
	}
	
	// Returns all the persons present in the database
	private static void loadPersons() throws SQLException {
		persons = new HashMap<>();
		personIds = new HashMap<>();
		roots = new HashSet<>();
		// Get the result set
	    ResultSet rs = QueryUtility.getAllColumnsAndRows(TABLE_PERSON);
	    
	    // Iterate over the result set and store the values in the map
	    // with the person name as the key and person identity object as the value
	    while(rs.next()) {
	    	String name = rs.getString(COLUMN_NAME);
	    	int person_id = rs.getInt(COLUMN_PERSON_ID);
	    	PersonIdentity personIdentity = new PersonIdentity(person_id, name);
	    	persons.put(name, personIdentity);
	    	personIds.put(person_id, personIdentity);
	    	roots.add(personIdentity);
	    }
	}
	
	// Load partnering relationships
	private static void loadPartneringRelationships() throws SQLException {
		partnered = new HashSet<>();
		// Get the result set
	    ResultSet rs = QueryUtility.getAllColumnsAndRows(TABLE_PARTNER);
	    
	    // Iterate over the result set and store the values in the partnered set
	    while(rs.next()) {
	    	// Get row values
	    	int person_id = rs.getInt(COLUMN_PERSON_ID);
	    	int partner_id = rs.getInt(COLUMN_PARTNER_ID);
	    	
	    	// Get the PersonIdentity objects for partners
	    	PersonIdentity personIdentity1 = personIds.get(person_id);
	    	PersonIdentity personIdentity2 = personIds.get(partner_id);
	    	
	    	// Add the person to the partnered set
	    	partnered.add(personIdentity1);
	    	
	    	// Set the partner in the PersonIdentity object
	    	personIdentity1.setPartner(personIdentity2);
	    }
	}
	
	// Load parent-child relationships
	private static void loadParentChildRelationships() throws SQLException {
		children = new HashSet<>();
		// Get the result set
	    ResultSet rs = QueryUtility.getAllColumnsAndRows(TABLE_CHILD);
	    
	    // Iterate over the result set and store the values in the children set
	    while(rs.next()) {
	    	// Get row values
	    	int parentId = rs.getInt(COLUMN_PERSON_ID);
	    	int childId = rs.getInt(COLUMN_CHILD_ID);
	    	
	    	// Get the PersonIdentity objects for parent and child
	    	PersonIdentity parentIdentity = personIds.get(parentId);
	    	PersonIdentity childIdentity = personIds.get(childId);
	    	
	    	// Remove child from root and add to children
	    	roots.remove(childIdentity);
			children.add(childIdentity);
			
			// Update relationships in the PersonIdentity objects
			Utility.updateParentChildRelationship(parentIdentity, childIdentity);
	    }
	}
	
	// Update root ancestors set for all individuals
	private static void updateRootAncestors() {
		// Trigger recursion from all roots
		for (var root: roots) {
			updateRootAncestors(root, root);
		}
	}
	
	// Recursively call for every descendant
	private static void updateRootAncestors(PersonIdentity current, PersonIdentity root) {
		if (current != root) {
			current.addRootAncestor(root);
		}
		
		for (var child: current.getChildren()) {
			updateRootAncestors(child, root);
		}
	}
}

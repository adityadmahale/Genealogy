import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Reporting {
	// Map for storing name and its corresponding PersonIdentity
	private Map<String, PersonIdentity> persons;
	// Map for storing id and its corresponding PersonIdentity
	private Map<Integer, PersonIdentity> personIds;
	// Set for storing roots of the family tree
	private Set<PersonIdentity> roots;
	// Set for storing partners
	private Set<PersonIdentity> partnered;
	
	// Map for storing tag and its corresponding tag id
	private Map<String, Integer> tags;
	// Map for storing file location and its corresponding FileIdentifier
	private Map<String, FileIdentifier> files;
	// Map for storing city and its corresponding id
	private Map<String, Integer> cities;
	
	// Object for accessing reporting related database tables
	private ReportingDatabaseAccess reportingAccess = new ReportingDatabaseAccess();
	
	public Reporting() {
		try {
			// Load all the states from the family tree and media related classes
			persons = PersistentState.getPersons();
			personIds = PersistentState.getPersonIds();
			roots = PersistentState.getRoots();
			partnered = PersistentState.getPartners();
			files = PersistentState.getFiles();
			tags = PersistentState.getTags();
			cities = PersistentState.getCities();
		} catch (SQLException e) {
			throw new IllegalStateException();
		}
	}
	
	// Returns the PersonIdentity object for the given name
	PersonIdentity findPerson(String name) {
		// Handle invalid input
		if (name == null) {
			throw new IllegalArgumentException();
		}
		// If the person name is not present, then throw an exception
		if (!persons.containsKey(name)) {
			throw new IllegalArgumentException("Person with the name " + name + " does not exist" );
		}
		return persons.get(name);
	}
	
	// Returns the FileIdentifier object for the given file location
	FileIdentifier findMediaFile(String name) {
		// Handle invalid input
		if (name == null) {
			throw new IllegalArgumentException();
		}
		// If the file location is not present, then throw an exception
		if (!files.containsKey(name)) {
			throw new IllegalArgumentException("File with the location " + name + " does not exist" );
		}
		return files.get(name);
	}
	
	// Returns the name of the person for the given PersonIdentity object
	String findName(PersonIdentity id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		return id.getName();
	}
	
	// Returns the file location for the given FileIdentifier object
	String findMediaFile(FileIdentifier fileId) {
		if (fileId == null) {
			throw new IllegalArgumentException();
		}
		return fileId.getFileLocation();
	}
	
	BiologicalRelation findRelation(PersonIdentity person1,PersonIdentity person2) {
		// Handle invalid inputs
		if (Utility.isInvalid(person1, person2)) {
			throw new IllegalArgumentException();
		}
		
		// Handle when the same person is passed
		if (Utility.isSamePerson(person1, person2)) {
			throw new IllegalArgumentException("Cannot find relation between the same person");
		}
		
		// Find a common root ancestor between two persons
		PersonIdentity rootAncestor = getRootAncestor(person1, person2);
		if (rootAncestor == null) {
			return null;
		}
		
		// Create two queues for storing paths from the person to the root ancestor
		Deque<PersonIdentity> path1 = new LinkedList<>();
		Deque<PersonIdentity> path2 = new LinkedList<>();
		// Find paths between the person and the common root ancestor
		findPath(person1, rootAncestor, path1);
		findPath(person2, rootAncestor, path2);
		
		// Remove all the common ancestor from both the paths including lowest common ancestor
		while (path1.size() != 0 && path2.size() != 0) {
			// If the ancestors are different, then break
			if (path1.getLast() != path2.getLast()) {
				break;
			}
			
			path1.removeLast();
			path2.removeLast();
		}
		
		// Get generation gap for both the persons from the lowest common ancestor
		int nPerson1 = path1.size();
		int nPerson2 = path2.size();
		
		// Calculate counsinship and removal
		int cousinship = Math.min(nPerson1, nPerson2) - 1;
		int removal = Math.abs(nPerson1 - nPerson2);
		return new BiologicalRelation(cousinship, removal);
	}
	
	// Find the path recursively from the person to the root ancestor
	// Traverse the tree from bottom to top
	private boolean findPath(PersonIdentity current, PersonIdentity rootAncestor, Deque<PersonIdentity> path) {
		// If null then return
		if (current == null) {
			return false;
		}
		
		// Add the node to the path
		path.addLast(current);
		
		// If found, the return true
		if (current == rootAncestor) {
			return true;
		}
		
		// Define parent variables for the current person
		PersonIdentity firstParent;
		PersonIdentity secondParent;
		List<PersonIdentity> parents = current.getParents();
		
		// If both the parents are found, then set the variables
		// If one parent is found then set one variable to that parent and other to null
		// If the current person has no parents, then st both the parents to null
		if (parents.size() == 2) {
			firstParent = parents.get(0);
			secondParent = parents.get(1);
		} else if (parents.size() == 1) {
			firstParent = parents.get(0);
			secondParent = null;
		} else {
			firstParent = null;
			secondParent = null;
		}
		
		// Recursively find path for both the parents
		if (findPath(firstParent, rootAncestor, path) || findPath(secondParent, rootAncestor, path)) {
			return true;
		}
	    
		// If the path is not present, then remove current node and return false
		path.removeLast();
		
		return false;
	}
	
	// Returns a common root ancestor for the given two person
	// If a common root ancestor is not present, then return null
	private PersonIdentity getRootAncestor(PersonIdentity person1,PersonIdentity person2) {
		// Check if person2 is the root ancestor of person1
		if (person1.isRootAncestorPresent(person2)) {
			return person2;
		}
		// Check if person1 is the root ancestor of person2
		if (person2.isRootAncestorPresent(person1)) {
			return person1;
		}
		
		// Find the intersection between two person's root ancestors
		Set<PersonIdentity> commonRootAncestors = new HashSet<>(person1.getRootAncestors());
		commonRootAncestors.retainAll(person2.getRootAncestors());
		
		// If the intersection is found, then return one of the common root ancestor
		// Otherwise, return null
		if (commonRootAncestors.size() != 0) {
			return commonRootAncestors.iterator().next();
		}
		
		return null;
	}
	
	// Find the descendants of the given person
	Set<PersonIdentity> descendents(PersonIdentity person, Integer generations) {
		// Handle invalid inputs
		if (person == null || generations < 0) {
			throw new IllegalArgumentException();
		}
		// Recursively call private descendants method to find the descendants of a given person
		Set<PersonIdentity> descendants = new HashSet<>();
		descendants(person, person, generations, descendants, 0);
		
		return descendants;
	}
	
	private void descendants(PersonIdentity root, PersonIdentity current, Integer generations, Set<PersonIdentity> descendants, Integer currentGeneration) {
		// Add current node to the set
		if (root != current) {
			descendants.add(current);
		}
		// If the generation matches with the passed generation, then return
		if (generations == currentGeneration) {
			return;
		}
		
		// Recursively find descendants for all the children of the current person
		for (var child: current.getChildren()) {
			descendants(root, child, generations, descendants, currentGeneration + 1);
		}
	}
	
	// Find the ancestors of the given person
	Set<PersonIdentity> ancestors(PersonIdentity person, Integer generations) {
		// Handle invalid inputs
		if (person == null || generations < 0) {
			throw new IllegalArgumentException();
		}
		// Recursively call private ancestors method to find the ancestors of a given person
		Set<PersonIdentity> ancestors = new HashSet<>();
		ancestors(person, person, generations, ancestors, 0);
		
		return ancestors;
	}
	
	private void ancestors(PersonIdentity root, PersonIdentity current, Integer generations, Set<PersonIdentity> ancestors, Integer currentGeneration) {
		// Add current node to the set
		if (root != current) {
			ancestors.add(current);
		}
		// If the generation matches with the passed generation, then return
		if (generations == currentGeneration) {
			return;
		}
		
		// Recursively find ancestors for all the parents of the current person
		for (var parent: current.getParents()) {
			ancestors(root, parent, generations, ancestors, currentGeneration + 1);
		}
	}
	
	List<String> notesAndReferences(PersonIdentity person) {
		if (person == null) {
			throw new IllegalArgumentException();
		}
		List<String> result = new ArrayList<>();
		try {
			// Get notes and references
			reportingAccess.getNotesAndReferences(person, result);
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		return result;
	}
	
	Set<FileIdentifier> findMediaByTag(String tag, String startDate, String endDate) {
		// Handle invalid inputs
		if (tag == null || startDate == null || endDate == null || tag == "" || startDate == "" || endDate == "") {
			throw new IllegalArgumentException();
		}
		
		// Check if the date format is incorrect
		if (!Utility.isDateValid(startDate) || !Utility.isDateValid(endDate)) {
			throw new IllegalArgumentException("Date format is not valid");
		}
		
		// Check the dates
		if (!Utility.isDateRangeCorrect(startDate, endDate)) {
			throw new IllegalArgumentException("Start date is later than the end date");
		}
		
		// Check if the tag exists
		if (!tags.containsKey(tag)) {
			throw new IllegalArgumentException("The tag is not available");
		}
		
		// Get tag id
		int tagId = tags.get(tag);
		
		Set<FileIdentifier> result = new HashSet<>();
		try {
			// Get media by tags from the database
			reportingAccess.getMediaByTag(tagId, startDate, endDate, result, files);
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		return result;
	}
	
	Set<FileIdentifier> findMediaByLocation(String location, String startDate, String endDate) {
		// Handle invalid inputs
		if (location == null || startDate == null || endDate == null || location == "" || startDate == "" || endDate == "") {
			throw new IllegalArgumentException();
		}
		
		// Check if the date format is incorrect
		if (!Utility.isDateValid(startDate) || !Utility.isDateValid(endDate)) {
			throw new IllegalArgumentException("Date format is not valid");
		}
		
		// Check the dates
		if (!Utility.isDateRangeCorrect(startDate, endDate)) {
			throw new IllegalArgumentException("Start date is later than the end date");
		}
		
		// Check if the location exists
		if (!cities.containsKey(location)) {
			throw new IllegalArgumentException("Location is not available");
		}
		
		// Get city id
		int cityId = cities.get(location);
		
		Set<FileIdentifier> result = new HashSet<>();
		try {
			// Get media by location from the database
			reportingAccess.getMediaByLocation(cityId, startDate, endDate, result, files);
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		return result;
	}
	
	List<FileIdentifier> findIndividualsMedia(Set<PersonIdentity> people, String startDate, String endDate) {
		// Handle invalid inputs
		if (people == null) {
			throw new IllegalArgumentException();
		}
		
		// Check if the date format is incorrect
		if (!Utility.isDateValid(startDate) || !Utility.isDateValid(endDate)) {
			throw new IllegalArgumentException("Date format is not valid");
		}
		
		// Check the dates
		if (!Utility.isDateRangeCorrect(startDate, endDate)) {
			throw new IllegalArgumentException("Start date is later than the end date");
		}
		
		// Handle when the set size is zero
		List<FileIdentifier> result = new ArrayList<>();
		if (people.size() == 0) {
			return result;
		}
		
		try {
			// Get media by individuals
			reportingAccess.getMediaByIndividuals(people, startDate, endDate, result, files);
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		return result;
		
	}
	
	List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person) {
		// Handle invalid inputs
		if (person == null) {
			throw new IllegalArgumentException();
		}
		
		// Handle when the set size is zero
		List<FileIdentifier> result = new ArrayList<>();
		
		// If no children, then return empty list
		if (!person.hasChildren()) {
			return result;
		}
		
		try {
			// Get media by biological children
			reportingAccess.getMediaByBiologicalChildren(person, result, files);
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		
		return result;
	}
	
}

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Reporting {
	// Map for storing name and its corresponding PersonIdentity
	private Map<String, PersonIdentity> persons;
	// Map for storing id and its corresponding PersonIdentity
	private Map<Integer, PersonIdentity> personIds;
	// Set for storing roots of the family tree
	private Set<PersonIdentity> roots;
	// Set for storing partners
	private Set<PersonIdentity> partnered;
	// Set for storing children
	private Set<PersonIdentity> children;
	
	// Map for storing tag and its corresponding tag id
	private Map<String, Integer> tags;
	// Map for storing file location and its corresponding FileIdentifier
	private Map<String, FileIdentifier> files;
	
	public Reporting() {
		try {
			persons = PersistentState.getPersons();
			personIds = PersistentState.getPersonIds();
			roots = PersistentState.getRoots();
			partnered = PersistentState.getPartners();
			children = PersistentState.getChildren();
			files = PersistentState.getFiles();
			tags = PersistentState.getTags();
		} catch (SQLException e) {
			throw new IllegalStateException();
		}
	}

	public PersonIdentity findPerson(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		if (!persons.containsKey(name)) {
			throw new IllegalArgumentException("Person with the name " + name + " does not exist" );
		}
		return persons.get(name);
	}
	
	public FileIdentifier findMediaFile(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		if (!files.containsKey(name)) {
			throw new IllegalArgumentException("File with the location " + name + " does not exist" );
		}
		return files.get(name);
	}
	
	public String findName(PersonIdentity id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		return id.getName();
	}
	
	public String findMediaFile(FileIdentifier fileId) {
		if (fileId == null) {
			throw new IllegalArgumentException();
		}
		return fileId.getFileLocation();
	}
	
	public BiologicalRelation findRelation(PersonIdentity person1,PersonIdentity person2) {
		return null;
	}
	
	public Set<PersonIdentity> descendents(PersonIdentity person, Integer generations) {
		if (person == null || generations < 0) {
			throw new IllegalArgumentException();
		}
		Set<PersonIdentity> descendants = new HashSet<>();
		descendants(person, person, generations, descendants, 0);
		
		return descendants;
	}
	
	private void descendants(PersonIdentity root, PersonIdentity current, Integer generations, Set<PersonIdentity> descendants, Integer currentGeneration) {
		if (root != current) {
			descendants.add(current);
		}
		if (generations == currentGeneration) {
			return;
		}
		
		for (var child: current.getChildren()) {
			descendants(root, child, generations, descendants, currentGeneration + 1);
		}
	}
	
	public Set<PersonIdentity> ancestors(PersonIdentity person, Integer generations) {
		if (person == null || generations < 0) {
			throw new IllegalArgumentException();
		}
		Set<PersonIdentity> ancestors = new HashSet<>();
		ancestors(person, person, generations, ancestors, 0);
		
		return ancestors;
	}
	
	private void ancestors(PersonIdentity root, PersonIdentity current, Integer generations, Set<PersonIdentity> ancestors, Integer currentGeneration) {
		if (root != current) {
			ancestors.add(current);
		}
		if (generations == currentGeneration) {
			return;
		}
		
		for (var parent: current.getParents()) {
			ancestors(root, parent, generations, ancestors, currentGeneration + 1);
		}
	}
	
	public List<String> notesAndReferences(PersonIdentity person) {
		return null;
	}
	
	public Set<FileIdentifier> findMediaByTag(String tag , String startDate, String endDate) {
		return null;
	}
	
	public Set<FileIdentifier> findMediaByLocation(String location, String startDate, String endDate) {
		return null;
	}
	
	public List<FileIdentifier> findIndividualsMedia(Set<PersonIdentity> people, String startDate, String endDate) {
		return null;
	}
	
	public List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person) {
		return null;
	}
	
}

import java.sql.SQLException;
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
		return null;
	}
	
	public Set<PersonIdentity> ancestors(PersonIdentity person, Integer generations) {
		return null;
	}
	
	public List<String> notesAndReferences(PersonIdentity person) {
		return null;
	}
	
	public Set<FileIdentifier> findMediaByTag( String tag , String startDate, String endDate) {
		return null;
	}
	
	public Set<FileIdentifier> findMediaByLocation( String location, String startDate, String endDate) {
		return null;
	}
	
	public List<FileIdentifier> findIndividualsMedia( Set<PersonIdentity> people, String startDate, String endDate) {
		return null;
	}
	
	public List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person) {
		return null;
	}
	
}

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonIdentity {
	// Unique identifier for a person
	private int personId;
	// Name of the person
	private String name;
	// Parents of the person
	private List<PersonIdentity> parents = new ArrayList<>();
	// Partner of the person
	private PersonIdentity partner;
	// Children of the person
	private List<PersonIdentity> children = new ArrayList<>();
	// Root ancestors of the person
	private Set<PersonIdentity> rootAncestors = new HashSet<>();
	
	// Returns partner of the person
	PersonIdentity getPartner() {
		return partner;
	}
	
	boolean hasParent(PersonIdentity parent) {
		return parents.contains(parent);
	}
	
	public PersonIdentity(int personId, String name) {
		this.personId = personId;
		this.name = name;
	}
	
	// Returns Id associated with the person
	int getPersonId() {
		return personId;
	}
	
	boolean hasChildren() {
		return children.size() > 0;
	}
	
	// Returns name of the person
	String getName() {
		return name;
	}
	
	// Sets partner for the person
	void setPartner(PersonIdentity partner) {
		this.partner = partner;
	}
	
	// Checks if the partner exists for the person
	boolean hasPartner() {
		return partner != null;
	}
	
	// Returns list of children of the person
	List<PersonIdentity> getChildren() {
		return children;
	}
	
	// Adds a child to the person
	void addChild(PersonIdentity child) {
		children.add(child);
	}
	
	// Returns parents of the person
	List<PersonIdentity> getParents() {
		return parents;
	}
	
	// Adds a parent to the person
	void addParent(PersonIdentity parent) {
		parents.add(parent);
	}
	
	// Checks if the root ancestor is present for the person
	boolean isRootAncestorPresent() {
		return rootAncestors.size() > 0;
	}
	
	// Add a root ancestor for the person
	void addRootAncestor(PersonIdentity person) {
		rootAncestors.add(person);
	}
	
	boolean isParentAdditionAllowed() {
		return parents.size() < 2;
	}
	
	Set<PersonIdentity> getRootAncestors() {
		return rootAncestors;
	}
	
	boolean isRootAncestorPresent(PersonIdentity person) {
		return rootAncestors.contains(person);
	}
	
	// Updates rootAncestors for the child and descendants recursively
	void updateRootAncestors(PersonIdentity parent) {
		// Holds new root ancestors for the child
		Set<PersonIdentity> childRootAncestors = new HashSet<>();
		childRootAncestors.addAll(rootAncestors);
		
		// If the parent does not have root ancestors, then add parent as the root ancestor
		// Otherwise, add root ancestors of the parent 
		if (!parent.isRootAncestorPresent()) {
			childRootAncestors.add(parent);
		} else {
			childRootAncestors.addAll(parent.rootAncestors);
		}
		
		// Start recursion
		updateRootAncestors(this, this, childRootAncestors);
	}
	
	private void updateRootAncestors(PersonIdentity root, PersonIdentity previousRoot,Set<PersonIdentity> newRootAncestors) {
		// Remove old ancestor
		if (root != previousRoot) {
			root.rootAncestors.remove(previousRoot);
		}
		
		// Add new ancestors
		root.rootAncestors.addAll(newRootAncestors);
		
		// Recursively update root ancestors for all descendants
		for (var child: root.children) {
			updateRootAncestors(child, previousRoot, newRootAncestors);
		}
	}
}

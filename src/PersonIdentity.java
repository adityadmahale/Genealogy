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
	public PersonIdentity getPartner() {
		return partner;
	}
	
	public PersonIdentity(int personId, String name) {
		this.personId = personId;
		this.name = name;
	}
	
	// Returns Id associated with the person
	public int getPersonId() {
		return personId;
	}
	
	// Returns name of the person
	public String getName() {
		return name;
	}
	
	// Sets partner for the person
	public void setPartner(PersonIdentity partner) {
		this.partner = partner;
	}
	
	// Checks if the partner exists for the person
	public boolean hasPartner() {
		return partner != null;
	}
	
	// Returns list of children of the person
	public List<PersonIdentity> getChildren() {
		return children;
	}
	
	// Adds a child to the person
	public void addChild(PersonIdentity child) {
		children.add(child);
	}
	
	// Returns parents of the person
	public List<PersonIdentity> getParents() {
		return parents;
	}
	
	// Adds a parent to the person
	public void addParent(PersonIdentity parent) {
		parents.add(parent);
	}
	
	// Checks if the root ancestor is present for the person
	public boolean isRootAncestorPresent() {
		return rootAncestors.size() > 0;
	}
	
	// Add a root ancestor for the person
	public void addRootAncestor(PersonIdentity person) {
		rootAncestors.add(person);
	}
	
	// Updates rootAncestors for the child and descendants recursively
	public void updateRootAncestors(PersonIdentity parent) {
		// Holds new root ancestors for the child
		Set<PersonIdentity> childRootAncestors = new HashSet<>();
		
		// If the parent does not have root ancestors, then add parent as the root ancestor
		// Otherwise, add root ancestors of the parent 
		if (!parent.isRootAncestorPresent()) {
			childRootAncestors.add(parent);
		} else {
			childRootAncestors.addAll(parent.rootAncestors);
		}
		
		// If the partner does not have root ancestors, then add partner as the root ancestor
		// Otherwise, add root ancestors of the partner
		if (parent.hasPartner()) {
			PersonIdentity partner = parent.getPartner();
			if (!partner.isRootAncestorPresent()) {
				childRootAncestors.add(partner);
			} else {
				childRootAncestors.addAll(partner.rootAncestors);
			}
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

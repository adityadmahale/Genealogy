
public class Utility {
	public static void updateParentChildRelationship(PersonIdentity parent, PersonIdentity child) {
		// Add child for the individual and the partner
		parent.addChild(child);
		child.addParent(parent);
		
		// If a partner is present, the update the parent-child relationship for the partner
		if (parent.hasPartner()) {
			PersonIdentity partner = parent.getPartner();
			partner.addChild(child);
			child.addParent(partner);
		}
	}
	
	// Checks if its the same person
	public static boolean isSamePerson(PersonIdentity person1, PersonIdentity person2) {
		return person1 == person2;
	}
	
	// Checks if the inputs are invalid
	public static boolean isInvalid(PersonIdentity person1, PersonIdentity person2) {
		return person1 == null || person2 == null;
	}
}

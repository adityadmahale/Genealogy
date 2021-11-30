import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class Utility {
	static void updateParentChildRelationship(PersonIdentity parent, PersonIdentity child) {
		// Add child for the individual and the partner
		parent.addChild(child);
		child.addParent(parent);
	}
	
	// Checks if its the same person
	static boolean isSamePerson(PersonIdentity person1, PersonIdentity person2) {
		return person1 == person2;
	}
	
	// Checks if the inputs are invalid
	static boolean isInvalid(PersonIdentity person1, PersonIdentity person2) {
		return person1 == null || person2 == null;
	}
	
	// Checks if the date format is correct
	static boolean isDateValid(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        try {
        	format.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
	
	// Checks if the date format is correct
	static boolean isYearValid(String date) {
        DateFormat format = new SimpleDateFormat("yyyy");
        format.setLenient(false);
        try {
        	format.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}

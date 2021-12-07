import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

class Utility {
	private static final String propertiesFilePath = "database.properties";
	
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
	
	// Check if the date range is correct
	static boolean isDateRangeCorrect(String start, String end) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date startDate = format.parse(start);
			Date endDate = format.parse(end);
			
			// Check if start date is after the end date
			if (startDate.compareTo(endDate) > 0) {
				return false;
			} else {
				return true;
			}
			
		} catch (ParseException e) {
			return false;
		}
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
	
	// Load database inputs from the properties file
	static Properties loadDatabaseInputs() {
		Properties properties = new Properties();
		try {
			FileReader read = new FileReader(propertiesFilePath);
			properties.load(read);
			
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e.getMessage());
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		return properties;
	}
}

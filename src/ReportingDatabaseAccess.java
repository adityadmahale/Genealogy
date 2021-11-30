import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReportingDatabaseAccess {
	// Union query between notes and references
	private static final String notesAndReferencesQuery = 
			"select * from ( "
			+ "select source, record_time, person_id "
			+ "from reference "
			+ "union "
			+ "select text, record_time, person_id "
			+ "from note) a "
			+ "where person_id = %d "
			+ "order by record_time";
	
	// Database connection object
	private static Connection connection = DatabaseConnection.getConnection();
	
	void getNotesAndReferences(PersonIdentity person, List<String> result) throws SQLException {
		// Parse final query
		String query = String.format(notesAndReferencesQuery, person.getPersonId());
	    PreparedStatement ps = connection.prepareStatement(query);
	    // Execute query
	    ResultSet rs = ps.executeQuery();
	    
	    // Add the text to the list
	    while (rs.next()) {
	    	result.add(rs.getString("source"));
	    }
	}
}

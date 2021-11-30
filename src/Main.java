import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args)  {
		Genealogy  genealogy = new Genealogy();
		FileIdentifier fileId = genealogy.addMediaFile("abc9");
		Map<String, String> mp = new HashMap<>();
		mp.put("year", "2005");
		mp.put("date", "2006-01-01");
		mp.put("city", "Toronto");
		genealogy.recordMediaAttributes(fileId, mp);
	}
}



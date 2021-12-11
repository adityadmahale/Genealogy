import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

	public static void main(String[] args)  {
		
		Genealogy  genealogy = new Genealogy();
		
		
//		var E = genealogy.findPerson("E");
//		var F = genealogy.findPerson("F");
//		var G = genealogy.findPerson("G");
//		var H = genealogy.findPerson("H");
//		
//		var A = genealogy.findMediaFile("A");
//		var B = genealogy.findMediaFile("B");
//		var C = genealogy.findMediaFile("C");
//		var D = genealogy.findMediaFile("D");
//		
//		genealogy.peopleInMedia(A, Arrays.asList(E, G, H));
//		genealogy.peopleInMedia(B, Arrays.asList(H));
//		genealogy.peopleInMedia(D, Arrays.asList(G));
		
//		var a = genealogy.findMediaByLocation("Toronto", "2006-01-01", "2006-12-31");
//		System.out.print(a);
//		var media1 = genealogy.addMediaFile("A");
//		var media2 = genealogy.addMediaFile("B");
//		var media3 = genealogy.addMediaFile("C");
//		var media4 = genealogy.addMediaFile("D");
//		
//		Map<String, String> attr3 = new HashMap<>();
//		attr3.put("city", "Pune");
//		attr3.put("date", "2005-01-03");
//		Map<String, String> attr2 = new HashMap<>();
//		attr2.put("city", "Toronto");
//		attr2.put("date", "2004-01-03");
//		genealogy.recordMediaAttributes(media3, attr3);
//		genealogy.recordMediaAttributes(media2, attr2);
//		
//		genealogy.tagMedia(media1, "Tag2");
//		genealogy.tagMedia(media2, "Tag1");
//		genealogy.tagMedia(media3, "Tag1");
//		genealogy.tagMedia(media4, "Tag1");
		
		var p = genealogy.findPerson("A");
		Set<PersonIdentity> s = new HashSet<>();
		s.add(p);
		var result = genealogy.findIndividualsMedia(s, null, null);
		
//		var A = genealogy.addPerson("A");
//		var B = genealogy.addPerson("B");
//		var C = genealogy.addPerson("C");
//		var D = genealogy.addPerson("D");
//		var E = genealogy.addPerson("E");
//		var F = genealogy.addPerson("F");
//		var G = genealogy.addPerson("G");
//		var H = genealogy.addPerson("H");
//		var I = genealogy.addPerson("I");
//		var J = genealogy.addPerson("J");
//		var K = genealogy.addPerson("K");
//		var L = genealogy.addPerson("L");
//		var M = genealogy.addPerson("M");
//		
//		genealogy.recordChild(D, A);
//		genealogy.recordChild(D, B);
//		genealogy.recordChild(D, C);
//		genealogy.recordChild(J, D);
//		
//		genealogy.recordChild(I, G);
//		genealogy.recordChild(I, H);
//		
//		genealogy.recordChild(G, E);
//		genealogy.recordChild(G, F);
//		
//		genealogy.recordChild(J, I);
//		genealogy.recordChild(M, I);
//		
//		genealogy.recordChild(M, L);
//		genealogy.recordChild(L, K);
		
//		var A = genealogy.findPerson("A");
//		var B = genealogy.findPerson("B");
//		var C = genealogy.findPerson("C");
//		var D = genealogy.findPerson("D");
//		var E = genealogy.findPerson("E");
//		var F = genealogy.findPerson("F");
//		var G = genealogy.findPerson("G");
//		var H = genealogy.findPerson("H");
//		var I = genealogy.findPerson("I");
//		var J = genealogy.findPerson("J");
//		var K = genealogy.findPerson("K");
//		var L = genealogy.findPerson("L");
//		var M = genealogy.findPerson("M");
//		
//		var relation1 = genealogy.findRelation(A, B);
//		var relation2 = genealogy.findRelation(A, E);
//		var relation3 = genealogy.findRelation(E, J);
//		var relation4 = genealogy.findRelation(F, H);
//		var relation5 = genealogy.findRelation(C, K);
//		
//		var ancestors = genealogy.ancestors(G, 4);
//		var descendants = genealogy.descendents(M, 2);
	}
}



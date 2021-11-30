import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args)  {
		Genealogy  genealogy = new Genealogy();
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
		
		var A = genealogy.findPerson("A");
		var B = genealogy.findPerson("B");
		var C = genealogy.findPerson("C");
		var D = genealogy.findPerson("D");
		var E = genealogy.findPerson("E");
		var F = genealogy.findPerson("F");
		var G = genealogy.findPerson("G");
		var H = genealogy.findPerson("H");
		var I = genealogy.findPerson("I");
		var J = genealogy.findPerson("J");
		var K = genealogy.findPerson("K");
		var L = genealogy.findPerson("L");
		var M = genealogy.findPerson("M");
		
		var relation1 = genealogy.findRelation(A, B);
		var relation2 = genealogy.findRelation(A, E);
		var relation3 = genealogy.findRelation(E, J);
		var relation4 = genealogy.findRelation(F, H);
		var relation5 = genealogy.findRelation(C, K);
		
		var ancestors = genealogy.ancestors(G, 4);
		var descendants = genealogy.descendents(M, 2);
	}
}



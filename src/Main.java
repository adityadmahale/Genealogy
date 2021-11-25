import java.util.Arrays;

public class Main {

	public static void main(String[] args)  {
		Genealogy  genealogy = new Genealogy();
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
		
		var rel = genealogy.findRelation(E, J);
		System.out.println(rel.getDegreeOfCounsinship());
		System.out.println(rel.getLevelOfRemoval());
	}
}




public class BiologicalRelation {
	private int degreeOfCounsinship;
	private int levelOfRemoval;
	
	public BiologicalRelation(int degreeOfCounsinship, int levelOfRemoval) {
		this.degreeOfCounsinship = degreeOfCounsinship;
		this.levelOfRemoval = levelOfRemoval;
	}

	public int getDegreeOfCounsinship() {
		return degreeOfCounsinship;
	}

	public int getLevelOfRemoval() {
		return levelOfRemoval;
	}
}

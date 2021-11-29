
public class BiologicalRelation {
	private int degreeOfCounsinship;
	private int levelOfRemoval;
	
	public BiologicalRelation(int degreeOfCounsinship, int levelOfRemoval) {
		this.degreeOfCounsinship = degreeOfCounsinship;
		this.levelOfRemoval = levelOfRemoval;
	}

	public int getCounsinship() {
		return degreeOfCounsinship;
	}

	public int getRemoval() {
		return levelOfRemoval;
	}
}

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Genealogy {
	// Initialize family tree, media and reporting objects
	FamilyTreeManagement familyTreeManagement = new FamilyTreeManagement();
	MediaManagement mediaManagement = new MediaManagement();
	Reporting reporting = new Reporting();
	
	public PersonIdentity addPerson(String name) {
		return familyTreeManagement.addPerson(name);
	}
	
	public Boolean recordAttributes(PersonIdentity person, Map<String, String> attributes) {
		return familyTreeManagement.recordAttributes(person, attributes);
	}
	
	public Boolean recordReference(PersonIdentity person, String reference) {
		return familyTreeManagement.recordReference(person, reference);
	}
	
	public Boolean recordNote(PersonIdentity person, String note) {
		return familyTreeManagement.recordNote(person, note);
	}
	
	public Boolean recordChild(PersonIdentity parent, PersonIdentity child) {
		return familyTreeManagement.recordChild(parent, child);
	}
	
	public Boolean recordPartnering(PersonIdentity partner1, PersonIdentity partner2) {
		return familyTreeManagement.recordPartnering(partner1, partner2);
	}
	
	public Boolean recordDissolution(PersonIdentity partner1, PersonIdentity partner2) {
		return familyTreeManagement.recordDissolution(partner1, partner2);
	}
	
	public FileIdentifier addMediaFile(String fileLocation) {
		return mediaManagement.addMediaFile(fileLocation);
	}
	
	public Boolean recordMediaAttributes(FileIdentifier fileIdentifier, Map<String, String> attributes) {
		return mediaManagement.recordMediaAttributes(fileIdentifier, attributes);
	}
	
	public Boolean peopleInMedia(FileIdentifier fileIdentifier, List<PersonIdentity> people) {
		return mediaManagement.peopleInMedia(fileIdentifier, people);
	}
	
	public Boolean tagMedia(FileIdentifier fileIdentifier, String tag) {
		return mediaManagement.tagMedia(fileIdentifier, tag);
	}
	
	public PersonIdentity findPerson(String name) {
		return reporting.findPerson(name);
	}
	
	public FileIdentifier findMediaFile(String name) {
		return reporting.findMediaFile(name);
	}
	
	public String findName(PersonIdentity id) {
		return reporting.findName(id);
	}
	
	public String findMediaFile(FileIdentifier fileId) {
		return reporting.findMediaFile(fileId);
	}
	
	public BiologicalRelation findRelation(PersonIdentity person1,PersonIdentity person2) {
		return reporting.findRelation(person1, person2);
	}
	
	public Set<PersonIdentity> descendents(PersonIdentity person, Integer generations) {
		return reporting.descendents(person, generations);
	}
	
	public Set<PersonIdentity> ancestors(PersonIdentity person, Integer generations) {
		return reporting.ancestors(person, generations);
	}
	
	public List<String> notesAndReferences(PersonIdentity person) {
		return reporting.notesAndReferences(person);
	}
	
	public Set<FileIdentifier> findMediaByTag(String tag , String startDate, String endDate) {
		return reporting.findMediaByTag(tag, startDate, endDate);
	}
	
	public Set<FileIdentifier> findMediaByLocation(String location, String startDate, String endDate) {
		return reporting.findMediaByLocation(location, startDate, endDate);
	}
	
	public List<FileIdentifier> findIndividualsMedia(Set<PersonIdentity> people, String startDate, String endDate) {
		return reporting.findIndividualsMedia(people, startDate, endDate);
	}
	
	public List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person) {
		return reporting.findBiologicalFamilyMedia(person);
	}
}

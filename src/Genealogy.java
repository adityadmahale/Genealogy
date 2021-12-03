import java.util.List;
import java.util.Map;
import java.util.Set;

public class Genealogy {
	// Initialize family tree, media and reporting objects
	FamilyTreeManagement familyTreeManagement = new FamilyTreeManagement();
	MediaManagement mediaManagement = new MediaManagement();
	Reporting reporting = new Reporting();
	
	// Add a person to the database
	public PersonIdentity addPerson(String name) {
		return familyTreeManagement.addPerson(name);
	}
	
	// Records attributes for a person
	public Boolean recordAttributes(PersonIdentity person, Map<String, String> attributes) {
		return familyTreeManagement.recordAttributes(person, attributes);
	}
	
	// Records a reference for an individual
	public Boolean recordReference(PersonIdentity person, String reference) {
		return familyTreeManagement.recordReference(person, reference);
	}
	
	// Records a note for an individual
	public Boolean recordNote(PersonIdentity person, String note) {
		return familyTreeManagement.recordNote(person, note);
	}
	
	// Records a child for an individual
	public Boolean recordChild(PersonIdentity parent, PersonIdentity child) {
		return familyTreeManagement.recordChild(parent, child);
	}
	
	// Records a partner for an individual
	public Boolean recordPartnering(PersonIdentity partner1, PersonIdentity partner2) {
		return familyTreeManagement.recordPartnering(partner1, partner2);
	}
	
	// Records dissolution
	public Boolean recordDissolution(PersonIdentity partner1, PersonIdentity partner2) {
		return familyTreeManagement.recordDissolution(partner1, partner2);
	}
	
	// Adds a media file to the database
	public FileIdentifier addMediaFile(String fileLocation) {
		return mediaManagement.addMediaFile(fileLocation);
	}
	
	// Records media attributes
	public Boolean recordMediaAttributes(FileIdentifier fileIdentifier, Map<String, String> attributes) {
		return mediaManagement.recordMediaAttributes(fileIdentifier, attributes);
	}
	
	// Records people who are present in a media file
	public Boolean peopleInMedia(FileIdentifier fileIdentifier, List<PersonIdentity> people) {
		return mediaManagement.peopleInMedia(fileIdentifier, people);
	}
	
	// Tags a media
	public Boolean tagMedia(FileIdentifier fileIdentifier, String tag) {
		return mediaManagement.tagMedia(fileIdentifier, tag);
	}
	
	// Find a person based on the given name
	public PersonIdentity findPerson(String name) {
		return reporting.findPerson(name);
	}
	
	// Finds a media base on the given name
	public FileIdentifier findMediaFile(String name) {
		return reporting.findMediaFile(name);
	}
	
	// Finds a name based on given object
	public String findName(PersonIdentity id) {
		return reporting.findName(id);
	}
	
	// Returns the name of the media file based on the given object
	public String findMediaFile(FileIdentifier fileId) {
		return reporting.findMediaFile(fileId);
	}
	
	// Finds the relation between two people in terms of cousinship and removal
	public BiologicalRelation findRelation(PersonIdentity person1,PersonIdentity person2) {
		return reporting.findRelation(person1, person2);
	}
	
	// Finds the descendants of a given individual up to given generations
	public Set<PersonIdentity> descendents(PersonIdentity person, Integer generations) {
		return reporting.descendents(person, generations);
	}
	
	// Finds the ancestors of a given individual up to given generations
	public Set<PersonIdentity> ancestors(PersonIdentity person, Integer generations) {
		return reporting.ancestors(person, generations);
	}
	
	// Finds the notes and references of a given individual
	public List<String> notesAndReferences(PersonIdentity person) {
		return reporting.notesAndReferences(person);
	}
	
	// Finds a set of medias by given tag
	public Set<FileIdentifier> findMediaByTag(String tag , String startDate, String endDate) {
		return reporting.findMediaByTag(tag, startDate, endDate);
	}
	
	// Finds a set of medias by given location
	public Set<FileIdentifier> findMediaByLocation(String location, String startDate, String endDate) {
		return reporting.findMediaByLocation(location, startDate, endDate);
	}
	
	// Finds medias based on individuals present in it
	public List<FileIdentifier> findIndividualsMedia(Set<PersonIdentity> people, String startDate, String endDate) {
		return reporting.findIndividualsMedia(people, startDate, endDate);
	}
	
	// Finds medias in which the children of a given person are present
	public List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person) {
		return reporting.findBiologicalFamilyMedia(person);
	}
}

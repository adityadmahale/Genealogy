# Approach:

1. Create two databases
  a. FamilyTree: Storing family relationships, Individual’s information
  b. MediaManagement: Storing media metadata, tags
2. Create tables in the FamilyTree database:
a. Person(person_id, name)
b. PersonAttribute(person_id, date_of_birth, gender, occupation)
c. Note(person_id, note)
d. Reference(person_id, reference)
e. Partner(person_id_1, person_id_2)
f. ParentChild(parent_id, child_id)
3. Create tables in the MediaManagement database:
a. Media(media_id, fileLocation)
b. MediaAttribute(media_id, year, date, city)
c. PeopleMedia(media_id, person_id)
d. Tag(media_id, tag)
4. Create a tree data structure by querying the database to build a family tree.
5. Maintain individuals’ information in a map for quick access.
6. Navigate the tree to do different operations such as finding the lowest common ancestor, finding ancestors/descendants.

# Data Structures

## A) PersonIdentity:

The PersonIdentity object stores the following relations:
1. parents: Stores the parent/parents related to the person in a list(List<PersonIdentity>)
2. partner: Stores the partner(PersonIdentity)
3. children: Stores all the children in a list(List<PersonIdentity>)
4. rootAncestors: Stores root ancestors of the person in a set(Set<PersonIdentity>)
  
## B) rootAncestors:
Root ancestors represent the root in the Genealogy tree. There can be multiple root ancestors in the tree. Each individual in the tree stores the corresponding root ancestors.

## C) FamilyManagement:

The FamilyManagement class stores the following data structures:
1. roots: Represents the root individuals in the genealogy tree(Set<PersonIdentity>)
2. partnered: Represents all the individuals who have a partner(Set< PersonIdentity >)
3. children: Represents all the individuals who are a child of a person(Set<PersonIdentity>)
4. personIds: A map to store the personId and PersonIdentity object(Map<Integer, PersonIdentity>)
## D) MediaManagement:
The media management class stores the following objects:
1. tags: Stores tag name as the key and tagId as the value(Map<String, Integer>)
2. files: Stores fileLocation as the key and FileIdentifier object as the value(Map<String, FileIdentifier>)
## E) Genealogy Tree:
The fields of the PersonIdentity class discussed above automatically creates a tree like data structure. The tree can be thought of as top to bottom – In this case, the tree will be a multi-node tree. It can also be considered from bottom to top – In this case, the tree will be a binary tree(Since a person can have a maximum of two parents)
  
# Key Algorithms
 
1. Identifying root ancestors:
It is important to find the root ancestor in the genealogy tree.
a) Whenever a new person gets added to the tree, the person automatically becomes one of the roots.
b) When a person is recorded as the child of a parent, the person loses the root status.
2. Updating root ancestors:
The genealogy tree is a multi-root tree. To find the relationship between two person, it is necessary to narrow down a common root ancestor between two persons. The easiest way to do this is to maintain a rootAncestor set in the PersonIdentity class. The rootAncestors should be updated in two different cases.
Scenario 1: While loading the data from the database tables
a) Iterate over all the roots in the tree.
b) In each iteration, recursively update the root node as the root ancestor for the descendant nodes.
Scenario 2: While adding a child
a) Check the root ancestors of the parent nodes.
b) Add the root ancestors of both the parents as the root ancestors of the child node.
c) Recursively traverse all the descendants of the child node -> remove the child node from the root ancestors of the descendants -> And add the new root ancestors of the child node to all the descendants.
3. Sequence of loading family tree information from the database:
a) Get all individuals
b) Get all roots
c) Get partnering relationships
d) Get all children
4. Finding ancestors:
Recursively traverse all the parents of the current node till the given number of generations to find all the ancestors.
5. Finding descendants:
Recursively traverse all the children of the current node till the given number of generations to find all the descendants.
6. Finding relation:
a) Before finding relation, check if the two persons have any common root ancestors.
b) If common root ancestors are not present, then they are not related.
c) Otherwise, pick any one of the common root ancestors.
d) Create two double ended queues for each person to find the path from the person node to the picked root ancestor node.
e) Traverse recursively to add the nodes between the person node and the root node.
f) After adding nodes to both the queues, remove common nodes between two queues.
g) The size of the queues represents nX and nY.
h) Calculate degree of cousinship and level of removal between using nX and nY.   

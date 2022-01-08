package tree;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class Person extends Label{
	private String firstName, lastName, fullName, initial;
	private Person left;
	private Person right;
	private Person personParent;
	private char firstNameLetter,lastNameLetter;
	private boolean isFullNameDisplay = false;
	
	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = firstName + " " + lastName;
		this.firstNameLetter = firstName.charAt(0);
		this.lastNameLetter = lastName.charAt(0);
		this.initial = String.valueOf(this.firstNameLetter) + String.valueOf(this.lastNameLetter);
		this.setText(this.getInitial());
		this.setTooltip(new Tooltip(this.getFullName()));
	}
	
	public Person(String fullName) {
		this.setPrefSize(40, 40);
		this.fullName = fullName;
		this.firstNameLetter = firstName.charAt(0);
		this.lastNameLetter = lastName.charAt(0);
		this.initial = String.valueOf(this.firstNameLetter) + String.valueOf(this.lastNameLetter);
		this.setText(initial);
		this.setTooltip(new Tooltip(this.getFullName()));
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Person getLeft() {
		return left;
	}

	public void setLeft(Person left) {
		this.left = left;
	}

	public Person getRight() {
		return right;
	}

	public void setRight(Person right) {
		this.right = right;
	}

	public char getFirstNameLetter() {
		return firstNameLetter;
	}

	public void setFirstNameLetter(char firstNameLetter) {
		this.firstNameLetter = firstNameLetter;
	}

	public char getLastNameLetter() {
		return lastNameLetter;
	}

	public void setLastNameLetter(char lastNameLetter) {
		this.lastNameLetter = lastNameLetter;
	}

	public String getInitial() {
		return initial;
	}

	public Person getPersonParent() {
		return personParent;
	}

	public void setPersonParent(Person parent) {
		this.personParent = parent;
	}
	
	public void toggleText() {
		if (!isFullNameDisplay) {
			isFullNameDisplay = true;
			setText(this.getFirstName()+"\n"+this.getLastName());
		} else {
			isFullNameDisplay = false;
			setText(this.getInitial());
		}
	}
}

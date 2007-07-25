package demo;

public class User {
	private String id;
	private String prefix;
	private String firstName;
	private String lastName;
	private String address;
	private String jobTitle;
	private String phone;
	private String mobile;
	
	
	
	public User(String id,String prefix, String firstName, String lastName, String address, String jobTitle, String phone, String mobile) {
		this.id = id;
		this.prefix = prefix;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.jobTitle = jobTitle;
		this.phone = phone;
		this.mobile = mobile;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

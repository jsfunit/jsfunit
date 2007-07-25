package demo;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.faces.model.SelectItem;

public class UserList {
	private ArrayList users;
	private User selectedUser;


	public User getCurrentUser() {
		if (selectedUser == null) {
			 selectedUser = (User)this.getUsers().get(0);
		}
		return selectedUser;
	}

	public void setCurrentUser(User currentUser) {
		this.selectedUser = currentUser;
	}

	public ArrayList getUsers() {
		if (users == null)
			initUserList();
		return users;
	}

	public void setUsers(ArrayList users) {
		this.users = users;
	}
	
	public void selectUserById(String selectedId) {
		ListIterator i = this.getUsers().listIterator();
		while (i.hasNext()) {
			User u = (User) i.next();
			if (u.getId().equals(selectedId)) {
				selectedUser = u;
				return;
			}
		}
		// TODO exceptional case
	}

	
	
	private void initUserList() {
		users = new ArrayList();
		users.add(new User(
				"Holloman, Debbie",
				"Mrs.", "Debbie","Holloman",
				"2243 Fallenwood Street Dallas, TX 75555-3483",
				"Administrative Assistant",
				"(214) 555-2343", "(214) 555-2144"
		));
		users.add(new User(
				"Barnes, Pat",
				"Mr.", "Pat","Barnes",
				"743 1st Avenue Boston, MA 71204-2345",
				"Executive Vice President",
				"(972) 555-029", "(972) 555-0295"
		));
		users.add(new User(
				"Dampier, Joan",
				"Mrs.", "Joan","Dampier",
				"535 Market Street Chicago, IL 76933-2359",
				"Chief Information Officer",
				"(318) 555-3424", "(318) 555-3326"
		));
		users.add(new User(
				"Alvarez, Randy",
				"Dr.", "Randy","Alvarez",
				"15 Magnolia Drive Los Angeles, CA 79333-2323",
				"Design Consultant",
				"(233) 555-3920", "(233) 555-3427"
		));
		users.add(new User(
				"Neil, William",
				"Sir", "William","Neil",
				"234 Forsythe Avenue San Fransisco, CA 74234-3090",
				"Design Consultant",
				"(789) 555-2349", "(789) 555-2548"
		));
		users.add(new User(
				"Hardoway, Kimber",
				"Miss", "Kimber","Hardoway",
				"32 Wells Road New York, NY 78334-3973",
				"Chief Technology Officer",
				"(743) 555-3245", "(743) 555-3649"
		));
		users.add(new User(
				"Story, Leslie",
				"Mrs.", "Leslie","Story",
				"834 Thomas Road Atlanta, GA 72890-3423",
				"Ajax Evangelist",
				"(817) 555-2349", "(817) 555-2740"
		));
		users.add(new User(
				"Lott, Charlie",
				"Mr.", "Charlie","Lott",
				"8888 Spartan Rd. Washington D.C., VA 70938-3445",
				"Talk Radio Host",
				"(404) 555-9843", "(404) 555-9841"
		));
		users.add(new User(
				"Patton, Sabrina",
				"Mrs.", "Sabrina","Patton",
				"69 Stewart Street Seattle, WA 74905-3286",
				"Self Employeed",
				"(489) 555-4395", "(489) 555-4992"
		));
		users.add(new User(
				"Lopez, Juan",
				"Mr.", "Juan","Lopez",
				"8992 Nondescript Road Las Vegas, NV 70923-4032",
				"Tax Attorney",
				"(484) 555-0002", "(484) 555-1003"
		));
	}
}

package demo;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.faces.model.SelectItem;


public class BackingBean {
	private UserList userlist;
	private ArrayList selectList;
	private String selectedId;



	public String getSelectedId() {
		if (selectedId == null)
			selectedId = this.userlist.getCurrentUser().getId();
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}
	

	public ArrayList getSelectList() {
		if (selectList == null) {
			selectList = new ArrayList();
			ListIterator i = userlist.getUsers().listIterator();
			while (i.hasNext()) {
				User u = (User) i.next();
				String title = u.getLastName() + ", "+u.getFirstName();
				selectList.add(new SelectItem(title,title));
			}
		}
		return selectList;
	}

	
	
	
	public void setSelectList(ArrayList selectList) {
		this.selectList = selectList;
	}

	public demo.UserList getUserlist() {
		return userlist;
	}

	public void setUserlist(demo.UserList userlist) {
		this.userlist = userlist;
	}

	public String changeCurrent() {
		this.getUserlist().selectUserById(selectedId);
		return null;
	}
}

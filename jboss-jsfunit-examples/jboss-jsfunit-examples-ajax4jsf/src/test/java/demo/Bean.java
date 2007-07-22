/**
 * 
 */
package demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ias
 *
 */
public class Bean {

	private static final int SIZE = 10; 
	private int requestCounter;
	private List<Integer> collection = null;
	private void initCollection() {
		collection = new ArrayList<Integer>();
		for (int i=0; i<SIZE; i++){
			collection.add(new Integer(0));
		}
	}
	
	public synchronized List<Integer> getCollection() {
		if (collection!=null && collection.size()==SIZE) {
			return collection;
		} else {
			initCollection();
			return collection;
		}
	}

	public synchronized void setCollection(List<Integer> collection) {
		this.collection = collection;
	}

	public synchronized String up() {
		for (int index=0; index<getCollection().size(); index++) {
			Integer current = getCollection().get(index);
			current++;
			getCollection().set(index,current);
		}
		requestCounter++;
		return null;
	}

	public synchronized String down() {
		requestCounter++;
		for (int index=0; index<getCollection().size(); index++) {
			Integer current = getCollection().get(index);
			current--;
			getCollection().set(index,current);
		}
		return null;
	}

	public synchronized int getRequestCounter() {
		return requestCounter;
	}

	public synchronized void setRequestCounter(int requestCounter) {
		this.requestCounter = requestCounter;
	}
}

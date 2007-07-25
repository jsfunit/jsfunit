/**
 * 
 */
package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

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
                int index = findTargetIndex("command_link_up");
		Integer current = getCollection().get(index);
		current++;
		getCollection().set(index,current);
		requestCounter++;
		return null;
	}

	public synchronized String down() {
		requestCounter++;
                int index = findTargetIndex("command_link_down");
		Integer current = getCollection().get(index);
		current--;
		getCollection().set(index,current);
		return null;
	}

        private int findTargetIndex(String target)
        {
           ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();   
           for (Iterator i = extCtx.getRequestParameterNames(); i.hasNext();)
           {
              String param = (String)i.next();
              if (param.endsWith(target))
              {
                 int lastColon = param.lastIndexOf(":");
                 int nextToLastColon = param.lastIndexOf(":", lastColon - 1);
                 String index = param.substring(nextToLastColon + 1, lastColon);
                 return Integer.parseInt(index);
              }
            } 

            throw new IllegalStateException("Could not find target index.");
        }

	public synchronized int getRequestCounter() {
		return requestCounter;
	}

	public synchronized void setRequestCounter(int requestCounter) {
		this.requestCounter = requestCounter;
	}
}

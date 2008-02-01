//$Id: HotelSearching.java,v 1.12 2007/06/27 00:06:49 gavin Exp $
package org.jboss.seam.example.booking;

import javax.ejb.Local;

@Local
public interface HotelSearching
{
   public int getPageSize();
   public void setPageSize(int pageSize);
   
   public String getSearchString();
   public void setSearchString(String searchString);
   
   public String getSearchPattern();
   
   public void find();
   public void nextPage();
   public boolean isNextPageAvailable();

   public void destroy();
   
}
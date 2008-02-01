//$Id: Register.java,v 1.4 2006/09/28 00:36:56 gavin Exp $
package org.jboss.seam.example.registration;

import javax.ejb.Local;

@Local
public interface Register
{
   public String register();
}

package org.jboss.jsfunit.example.ajax4jsf;

import java.io.InputStream;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 */
public class TextBean
{
   private String text = "";
   
   public TextBean()
   {
   }
   
   public String getText()
   {
      return this.text;
   }
   
   public void setText(String text)
   {
      this.text = text;
   }
   
   private String getSource(String file) throws Exception
   {
      ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
      InputStream in = extCtx.getResourceAsStream(file);
      byte[] bytes = new byte[in.available()];
      in.read(bytes);
      return new String(bytes);
   }
   
   public String getTestSource() throws Exception
   {
      return getSource("/A4JTest.java");
   }
   
   public String getEchoSource() throws Exception
   {
      return getSource("/pages/echo.xhtml");
   }
   
   public String getListSource() throws Exception
   {
      return getSource("/list.xhtml");
   }
   
   public String getRepeatRerenderSource() throws Exception
   {
      return getSource("/pages/a4j-repeat-rerender.xhtml");
   }
}

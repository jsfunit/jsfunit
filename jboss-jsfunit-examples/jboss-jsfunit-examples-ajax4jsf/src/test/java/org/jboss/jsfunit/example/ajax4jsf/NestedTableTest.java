/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.jsfunit.example.ajax4jsf;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * Test getComponentValue() on nested DataTables.
 *
 * @author Stan Silvert
 */
public class NestedTableTest extends ServletTestCase
{
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( NestedTableTest.class  );
   }

   // JSFUNIT-186
   public void testGetComponentValuesInNestedDatatable() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/pages/nestedtables.jsf");
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      // assert on values from the middle of the table
      assertEquals(  "1_1", server.getComponentValue("form:table:1:column1"));
      assertEquals("1_2_1", server.getComponentValue("form:table:2:nestedtable:2:column1"));
      assertEquals("2_1_1", server.getComponentValue("form:table:0:nestedtable:0:nestednestedtable:1:column1"));
      
      // assert on all values
      for (int i=0; i < 3; i++)
      {
         //table.setRowIndex(i);
         
         for (int j=0; j < 3; j++)
         {
            //nestedTable.setRowIndex(j);
            
            String id = "form:table:" + i + ":column" + j;
            assertEquals(i + "_" + j, server.getComponentValue(id));
            System.out.println(id + "=" + server.getComponentValue(id));
            
            for (int k=0; k < 3; k++)
            {  // 1 is nesting depth
               String nestedId = "form:table:" + i + ":nestedtable:" + j + ":column" + k;
               assertEquals("1_" + j + "_" + k, server.getComponentValue(nestedId));  
               System.out.println(nestedId + "=" + server.getComponentValue(nestedId));
               
               for (int l=0; l < 3; l++)
               {  // 2 is nesting depth
                  String nestednestedId = "form:table:" + i + ":nestedtable:" + j + ":nestednestedtable:" + k + ":column" + l;
                  assertEquals("2_" + k + "_" + l, server.getComponentValue(nestednestedId));  
                  System.out.println(nestednestedId + "=" + server.getComponentValue(nestednestedId));
               }
            }
         }
         
      }
   }
}

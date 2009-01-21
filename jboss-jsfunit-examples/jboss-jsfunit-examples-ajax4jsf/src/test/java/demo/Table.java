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

package demo;

/**
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class Table 
{
   private String parentKey;
   private int row;
   
   public Table()
   {
      parentKey = "";
      row = -1;
   }
   
   public Table(String parent, int row)
   {
      this.parentKey = parent;
      this.row = row;
   }

   private String makeColumnName(String column)
   {
      String colName = "";
      if (parentKey.length() > 0) colName = parentKey + "_";
      
      return colName + row + "_" + column;
   }
   
   public String getColumn0()
   {
      return makeColumnName("0");
   }
   
   public String getColumn1()
   {
      return makeColumnName("1");
   }
   
   public String getColumn2()
   {
      return makeColumnName("2");
   }
   
   public Table[] getSubTable()
   {
      String myNestLevel = "";
      //Append parent nest level:
      if (this.parentKey.length() > 0 && this.row != -1)
      {
        myNestLevel = parentKey + "_";
      }
      //Dont' add row on top level table:
      if (this.row != -1)
      {
        myNestLevel += this.row;
      }
      return new Table[] { new Table(myNestLevel, 0),
                           new Table(myNestLevel, 1),
                           new Table(myNestLevel, 2) };
   }
}

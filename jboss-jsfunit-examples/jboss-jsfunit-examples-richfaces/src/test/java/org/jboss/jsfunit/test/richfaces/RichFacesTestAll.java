/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.test.richfaces;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;

/**
 * Peform all JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichFacesTestAll extends ServletTestCase
{
   public static Test suite()
   {
      TestSuite suite = new TestSuite();
      suite.addTestSuite(ActionParamTest.class);
      suite.addTestSuite(AjaxFormTest.class);
      suite.addTestSuite(AjaxRegionValidationErrorTest.class);
      suite.addTestSuite(AjaxRegionSelfRenderTest.class);
      suite.addTestSuite(AjaxSupportTest.class);
      suite.addTestSuite(AjaxCommandButtonTest.class);
      suite.addTestSuite(AjaxCommandLinkTest.class);
      suite.addTestSuite(AjaxJsFunctionTest.class);
      suite.addTestSuite(AjaxKeepaliveTest.class);
      //suite.addTestSuite(AjaxIncludeTest.class);
      suite.addTestSuite(AjaxOutputPanelTest.class);
      suite.addTestSuite(AjaxRepeaterTest.class);
      suite.addTestSuite(RichCalendarTest.class);
      suite.addTestSuite(RichDataFilterSliderTest.class);
      //suite.addTestSuite(RichDataTableScrollerTest.class);
      //suite.addTestSuite(RichDragAndDropTest.class);
      //suite.addTestSuite(RichDropDownMenuTest.class);
      suite.addTestSuite(RichInplaceInputTest.class);
      //suite.addTestSuite(RichInputNumberSliderTest.class);
      //suite.addTestSuite(RichInputNumberSpinnerTest.class);
      suite.addTestSuite(RichTabPanelTest.class);
      //suite.addTestSuite(RichPanelMenuTest.class);
      return suite;
   }

}

/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <joepeer@excite.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */
package org.dbforms.event;
import java.util.*;
import javax.servlet.http.*;

import org.CVS.*;
import org.apache.log4j.Category;
import org.dbforms.*;
import org.dbforms.util.*;
import org.dbforms.config.*;
import org.dbforms.event.eventtype.EventType;
import org.dbforms.event.eventtype.EventTypeUtil;



/**
 * This class is invoked by the Controller-Servlet. It parses a request to find out
 * which Event(s) need to be instanciated.
 * The fine-grained parsing (parsing of additional data, etc) is done
 * by the WebEvent-Object itself (in order to hide complexity from this
 * class and to keep the framework open for implementations of new Event-classes)
 *
 * @author  Joe Peer <j.peer@gmx.net>
 * @created  30 novembre 2002
 */
public class EventEngine
{
   /** logging category for this class */
   static Category logCat = Category.getInstance(EventEngine.class.getName());

   /** instance of DatabaseEventFactory */
   private DatabaseEventFactory dbEventFactory = DatabaseEventFactoryImpl
      .instance();

   /** instance of NavigationEventFactory */
   private NavEventFactory    navEventFactory = NavEventFactoryImpl.instance();
   private HttpServletRequest request;
   private DbFormsConfig      config;
   Vector                     involvedTables;

   /**
    * @param  request Description of the Parameter
    * @param  config Description of the Parameter
    */
   public EventEngine(HttpServletRequest request, DbFormsConfig config)
   {
      this.request      = request;
      this.config       = config;
      involvedTables    = parseInvolvedTables();
   }

   /**
    * Find out which tables where on the jsp
    * (one jsp file may contain multiple dbforms, and each forms could contain many
    * subforms nested inside!)
    *
    * @return  Description of the Return Value
    */
   private Vector parseInvolvedTables()
   {
      String[] invTables = ParseUtil.getParameterValues(request, "invtable");

      // in empty forms, for example, we don't have any involved tables..!
      if (invTables == null)
      {
         return null;
      }

      Vector result = new Vector();

      for (int i = 0; i < invTables.length; i++)
      {
         int   tableIndex = Integer.parseInt(invTables[i]);
         Table t = config.getTable(tableIndex);

         result.addElement(t);
      }

      return result;
   }


   /**
    * Get the involvedTables attribute of the EventEngine class.
    *
    * @return  the involvedTables attribute of the EventEngine class
    */
   public Vector getInvolvedTables()
   {
      return involvedTables;
   }


   /**
    * result of this method depends on which Database- or Navigation - button was clicked
    * and should reflect the users' intention (is that to pathetic? ;=)
    * <br>
    * this is alpha code. the goal for version 1.0 is to make it more flexible and open it
    * for new code - it would be great to add new Web-Events without even compiling this class.
    *
    * if you have a hint for me on this topics please mail me: <j.peer@gmx.net>
    *
    * @return  Description of the Return Value
    */
   public WebEvent generatePrimaryEvent()
   {
      WebEvent e           = null;
      String   action      = ParseUtil.getFirstParameterStartingWith(request,
            "ac_");
      String   customEvent = request.getParameter("customEvent");

      if ((action == null) && (customEvent != null))
      {
         action = customEvent;
      }

      // NOOP EVENT
      //
      // family: web event
      if (action.equals(""))
      {
         logCat.info("##### N O O P   ELEMENT ######");
         e = new NoopEvent(-1, request, config);
         initializeWebEvent(e);

         return e;
      }

      // RELOAD EVENT
      //
      // family: web event
      //
      // ReloadEvent use to refresh field values from request object
      // and to allow server side manipulation for these fields
      // Ex: Select parent -> Select child
      if (action.startsWith("re_"))
      {
         logCat.info("##### RELOAD  EVENT ######");
         e = new ReloadEvent(ParseUtil.getEmbeddedStringAsInteger(action, 2, '_'),
               request, config);
         initializeWebEvent(e);

         return e;
      }

      // make the image button data (if any) look like a submit button;
      action = getImageButtonAction(action);

      // get the EventType class and identify the event type
      // and use the related factory class to create the event;
      EventType eventType = EventTypeUtil.getEventType(action);

      switch (eventType.getEventGroup())
      {
         case EventType.EVENT_GROUP_DATABASE:
         {
            logCat.info("::generatePrimaryEvent - generating a database event");
            e = dbEventFactory.createEvent(action, request, config);

            break;
         }

         case EventType.EVENT_GROUP_NAVIGATION:
         {
            logCat.info(
               "::generatePrimaryEvent - generating a navigation event");
            e = navEventFactory.createEvent(action, request, config);

            break;
         }

         default:
         {
            logCat.error(
               "::generatePrimaryEvent - WARNING: generating NO event. Why ?");

            break;
         }
      }

      // setting the followUp attributes for the generated event
      setEventFollowUp(e, action);

      return e;
   }


   /**
    * DOCUMENT ME!
    *
    * @param  exclude DOCUMENT ME!
    * @return  DOCUMENT ME!
    */
   public Enumeration generateSecundaryEvents(WebEvent exclude)
   {
      Vector  result           = new Vector();
      Vector  vAc              = ParseUtil.getParametersStartingWith(request,
            "autoupdate_");
      int     excludeTableId   = -1;
      String  excludeKeyId     = null;
      boolean collissionDanger = false;

      // first of all, we check if there is some real potential for collisions in the "to exclude"-event
      if (exclude instanceof DatabaseEvent)
      {
         collissionDanger    = true;
         excludeTableId      = exclude.getTableId();
         excludeKeyId        = ((DatabaseEvent) exclude).getKeyId();
      }

      for (int i = 0; i < vAc.size(); i++)
      {
         String param = (String) vAc.elementAt(i);

         // auto-updating may be disabled, so we have to check:
         if (ParseUtil.getParameter(request, param).equalsIgnoreCase("true"))
         {
            // let's find the id of the next table we may update
            int tableId = Integer.parseInt(param.substring(
                     "autoupdate_".length()));

            // we can only update existing rowsets. so we just look for key-values
            String      paramStub          = "k_" + tableId + "_";
            Enumeration keysOfCurrentTable = ParseUtil.getParametersStartingWith(request,
                  paramStub).elements();

            while (keysOfCurrentTable.hasMoreElements())
            {
               String aKeyParam = (String) keysOfCurrentTable.nextElement();
               String keyId = aKeyParam.substring(paramStub.length());

               logCat.info("autoaupdate debug info: keyId=" + keyId
                  + " excludeKeyId=" + excludeKeyId);

               if (!collissionDanger || (excludeTableId != tableId)
                        || !keyId.equals(excludeKeyId))
               {
                  DatabaseEvent e = dbEventFactory.createUpdateEvent(tableId,
                        keyId, request, config);
                  result.addElement(e);
               }
            }
         }
      }

      return result.elements();
   }


   /**
    * PRIVATE METHODS here
    */
   /**
    *  Sets the sourcePath attribute of the EventEngine object
    *
    * @param  contextPath The new sourcePath value
    * @param  sourcePath The new sourcePath value
    * @return  Description of the Return Value
    */
   private String setSourcePath(String contextPath, String sourcePath)
   {
      if ((contextPath.length() > 0) && sourcePath.startsWith(contextPath))
      {
         // shouldn't! just make sure!
         if (contextPath.endsWith("/"))
         {
            sourcePath = sourcePath.substring(contextPath.length() - 1);
         }
         else
         {
            sourcePath = sourcePath.substring(contextPath.length());
         }
      }

      return sourcePath;
   }


   /**
    *  Make the image button data look like a submit button.
    *  <br>
    *  Image buttons submit different parameters than submit buttons
    *  for submit the browser sends one value
    *  parameter:ac_insert_0_root=Submit this bug!
    *  for image buttons, the browser returns two values, showing the x and y
    *  position of the mouse
    *    parameter ac_insert_0_root.y=24
    *    parameter ac_insert_0_root.x=34
    *
    * @param  action Description of the Parameter
    * @return  The imageButtonAction value
    */
   private String getImageButtonAction(String action)
   {
      if (action.endsWith(".y") || action.endsWith(".x"))
      {
         action = action.substring(0, action.length() - 2);
      }

      logCat.info("::getImageButtonAction - action = [" + action + "]");

      return action;
   }


   /**
    *  Initialize the input web event.
    *
    * @param  e the web event to initialize
    */
   private void initializeWebEvent(WebEvent e)
   {
      String contextPath = request.getContextPath();
      String sourcePath = ParseUtil.getParameter(request, "source");

      logCat.info("sourcePath = " + sourcePath);
      sourcePath = setSourcePath(contextPath, sourcePath);

      e.setFollowUp(sourcePath);
      logCat.info("followup=" + e.getFollowUp());
   }


   /**
    *  Sets the eventFollowUp and followUpOnError attributes
    *  of the input Event object
    *
    * @param  e the event object
    * @param  action the action string
    */
   private void setEventFollowUp(WebEvent e, String action)
   {
      // now we have to find the followup-site the app-developer wants us to display.
      String followUp = ParseUtil.getParameter(request, "data" + action + "_fu");

      // if not...
      // ...then check if §2-followup exists (should always exist!)
      if (followUp == null)
      {
         followUp = ParseUtil.getParameter(request, "fu_" + e.getTableId());
      }

      logCat.info("setting follow up to:" + followUp);
      e.setFollowUp(followUp);

      String followUpOnError = ParseUtil.getParameter(request,
            "data" + action + "_fue");

      // if not...
      // ...then check if §2-followup exists
      if (followUpOnError == null)
      {
         followUpOnError = ParseUtil.getParameter(request,
               "fue_" + e.getTableId());
      }

      // Still no followup on error - use general followup
      if (followUpOnError == null)
      {
         followUpOnError = followUp;
      }

      logCat.info("setting follow up on Error to:" + followUpOnError);
      e.setFollowUpOnError(followUpOnError);
   }
}

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.Constants;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Table;

import org.dbforms.event.eventtype.EventType;
import org.dbforms.event.eventtype.EventTypeUtil;

import org.dbforms.util.ParseUtil;
import org.dbforms.util.StringUtil;
import org.dbforms.util.Util;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;



/**
 * This class is invoked by the Controller-Servlet. It parses a request to find
 * out which Event(s) need to be instanciated. The fine-grained parsing
 * (parsing of additional data, etc) is done by the WebEvent-Object itself (in
 * order to hide complexity from this class and to keep the framework open for
 * implementations of new Event-classes)
 *
 * @author Joe Peer
 *
 */
public class EventEngine {
   /** logging category for this class */
   private static Log logCat = LogFactory.getLog(EventEngine.class.getName());

   /** instance of DatabaseEventFactory */
   private AbstractDatabaseEventFactory dbEventFactory = DatabaseEventFactoryImpl
                                                 .instance();
   private DbFormsConfig        config;
   private HttpServletRequest   request;

   /** instance of NavigationEventFactory */
   private AbstractNavEventFactory navEventFactory = NavEventFactoryImpl.instance();

   /**
    * this vector will contain which tables where on the jsp page: one jsp file
    * may contain multiple dbforms, and each forms could contain many subforms
    * nested inside
    */
   private Vector involvedTables;

   /**
    * Constructor.
    *
    * @param request the request object
    * @param config the configuration object
    */
   public EventEngine(HttpServletRequest request,
                      DbFormsConfig      config) {
      this.request = request;
      this.config  = config;

      // find out which tables where on the jsp
      involvedTables = parseInvolvedTables();
   }

   /**
    * Get the involvedTables attribute of the EventEngine class.
    *
    * @return the involvedTables attribute of the EventEngine class
    */
   public Vector getInvolvedTables() {
      return involvedTables;
   }


   /**
    * Generate the primary event object, depending on the data contained into
    * the incoming http request object.
    *
    * @return a new WebEvent object
    */
   public AbstractWebEvent generatePrimaryEvent() {
      AbstractWebEvent e      = null;
      String   action = ParseUtil.getFirstParameterStartingWith(request, "ac_");
      String   customEvent = ParseUtil.getParameter(request, "customEvent");

      if (Util.isNull(action) && !Util.isNull(customEvent)) {
         // 2003-07-21-HKK: fixing NS 4.79 bug! (From bug list)
         action = customEvent.trim();
      }

      // NOOP EVENT
      //
      // family: web event
      if (Util.isNull(action)) {
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
      //
      // This event is created if customEvent is set to re_x_x!!
      //
      if (action.startsWith("re_")) {
         logCat.info("##### RELOAD  EVENT ######");
         e = new PageReloadEvent(StringUtil.getEmbeddedStringAsInteger(action,
                                                                      2, '_'),
                                 request, config);
         e.setType(EventType.EVENT_NAVIGATION_RELOAD);
         initializeWebEvent(e);

         return e;
      }

      // make the image button data (if any) look like a submit button;
      action = getImageButtonAction(action);

      // get the EventType class and identify the event type
      // and use the related factory class to create the event;
      EventType eventType = EventTypeUtil.getEventType(action);

      switch (eventType.getEventGroup()) {
         case EventType.EVENT_GROUP_DATABASE: {
            logCat.info("::generatePrimaryEvent - generating a database event");
            e = dbEventFactory.createEvent(action, request, config);

            break;
         }

         case EventType.EVENT_GROUP_NAVIGATION: {
            logCat.info("::generatePrimaryEvent - generating a navigation event");
            e = navEventFactory.createEvent(action, request, config);

            break;
         }

         default: {
            logCat.error("::generatePrimaryEvent - WARNING: generating NO event. Why ?");

            break;
         }
      }

      // setting the followUp attributes for the generated event
      setEventFollowUp(e, action);

      return e;
   }


   /**
    * Generate secundary events (update events)
    *
    * @param actTable DOCUMENT ME!
    * @param exclude the parent web event (related to the main form)
    *
    * @return DOCUMENT ME!
    */
   public Enumeration generateSecundaryEvents(Table    actTable,
                                              AbstractWebEvent exclude) {
      Vector  result           = new Vector();
      int     excludeTableId   = -1;
      String  excludeKeyId     = null;
      boolean collissionDanger = false;

      // first of all, we check if there is some real potential for collisions
      // in the "to exclude"-event
      if (exclude instanceof AbstractDatabaseEvent) {
         collissionDanger = true;
         excludeTableId   = exclude.getTable()
                                   .getId();
         excludeKeyId = ((AbstractDatabaseEvent) exclude).getKeyId();
      }

      String param = "autoupdate_" + String.valueOf(actTable.getId());
      String res = ParseUtil.getParameter(request, param);

      // auto-updating may be disabled, so we have to check:
      if (res.equalsIgnoreCase("true")
                || (res.equalsIgnoreCase("OnUpdateOnly")
                && exclude.getType()
                                .equals("update"))) {
         // we can only update existing rowsets. so we just look for
         // key-values
         String      paramStub          = "k_" + actTable.getId() + "_";
         Enumeration keysOfCurrentTable = ParseUtil.getParametersStartingWith(request,
                                                                              paramStub)
                                                   .elements();

         while (keysOfCurrentTable.hasMoreElements()) {
            String aKeyParam = (String) keysOfCurrentTable.nextElement();
            String keyId = aKeyParam.substring(paramStub.length());

            logCat.info("autoaupdate debug info: keyId=" + keyId
                        + " excludeKeyId=" + excludeKeyId);

            if (!collissionDanger
                      || (excludeTableId != actTable.getId())
                      || !keyId.equals(excludeKeyId)) {
               AbstractDatabaseEvent e = dbEventFactory.createUpdateEvent(actTable
                                                                  .getId(),
                                                                  keyId,
                                                                  request,
                                                                  config);
               result.addElement(e);
            }
         }

         // now try the same with insert records
         // but only if
         if (collissionDanger && (excludeTableId != actTable.getId())) {
            paramStub = "f_" + actTable.getId() + "_"
                        + Constants.FIELDNAME_INSERTPREFIX;

            Vector v = ParseUtil.getParametersStartingWith(request, paramStub);

            if (v.size() > 0) {
               String aKeyParam = (String) v.firstElement();
               String keyId = aKeyParam.substring(paramStub.length());
               keyId = StringUtil.getEmbeddedString(keyId, 0, '_');

               AbstractDatabaseEvent e = dbEventFactory.createInsertEvent(actTable
                                                                  .getId(),
                                                                  keyId,
                                                                  request,
                                                                  config);
               result.addElement(e);
            }
         }
      }

      return result.elements();
   }


   /**
    * Sets the eventFollowUp and followUpOnError attributes of the input Event
    * object
    *
    * @param e the event object
    * @param action the action string
    */
   private void setEventFollowUp(AbstractWebEvent e,
                                 String   action) {
      // now we have to find the followup-site the app-developer wants us to
      // display.
      String followUp = ParseUtil.getParameter(request, "data" + action + "_fu");

      // if not...
      // ...then check if §2-followup exists (should always exist!)
      if (followUp == null) {
         followUp = ParseUtil.getParameter(request,
                                           "fu_"
                                           + (((e == null)
                                              || (e.getTable() == null)) ? (-1)
                                                                         : e.getTable().getId()));
      }

      logCat.info("setting follow up to:" + followUp);

      if (e != null) {
         e.setFollowUp(followUp);
      }

      String followUpOnError = ParseUtil.getParameter(request,
                                                      "data" + action + "_fue");

      // if not...
      // ...then check if §2-followup exists
      if (followUpOnError == null) {
         followUpOnError = ParseUtil.getParameter(request,
                                                  "fue_"
                                                  + (((e == null)
                                                     || (e.getTable() == null))
                                                     ? (-1)
                                                     : e.getTable().getId()));
      }

      // Still no followup on error - use general followup
      if (followUpOnError == null) {
         followUpOnError = followUp;
      }

      logCat.info("setting follow up on Error to:" + followUpOnError);

      if (e != null) {
         e.setFollowUpOnError(followUpOnError);
      }
   }


   /**
    * Make the image button data look like a submit button. <br>
    * Image buttons submit different parameters than submit buttons for submit
    * the browser sends one value parameter:ac_insert_0_root=Submit this bug!
    * for image buttons, the browser returns two values, showing the x and y
    * position of the mouse parameter ac_insert_0_root.y=24 parameter
    * ac_insert_0_root.x=34
    *
    * @param action Description of the Parameter
    *
    * @return The imageButtonAction value
    */
   private String getImageButtonAction(String action) {
      if (action.endsWith(".y") || action.endsWith(".x")) {
         action = action.substring(0, action.length() - 2);
      }

      logCat.info("::getImageButtonAction - action = [" + action + "]");

      return action;
   }


   /**
    * PRIVATE METHODS here
    *
    * @param contextPath DOCUMENT ME!
    * @param sourcePath DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   /**
    * Sets the sourcePath attribute of the EventEngine object
    *
    * @param contextPath The new sourcePath value
    * @param sourcePath The new sourcePath value
    *
    * @return Description of the Return Value
    */
   private String setSourcePath(String contextPath,
                                String sourcePath) {
      if (!Util.isNull(contextPath)
                && !Util.isNull(sourcePath)
                && sourcePath.startsWith(contextPath)) {
         // shouldn't! just make sure!
         if (contextPath.endsWith("/")) {
            sourcePath = sourcePath.substring(contextPath.length() - 1);
         } else {
            sourcePath = sourcePath.substring(contextPath.length());
         }
      }

      return sourcePath;
   }


   /**
    * Initialize the input web event.
    *
    * @param e the web event to initialize
    */
   private void initializeWebEvent(AbstractWebEvent e) {
      String contextPath = request.getContextPath();
      String sourcePath = ParseUtil.getParameter(request, "source");

      logCat.info("sourcePath = " + sourcePath);
      sourcePath = setSourcePath(contextPath, sourcePath);

      e.setFollowUp(sourcePath);
      logCat.info("followup=" + e.getFollowUp());
   }


   /**
    * Find out which tables where on the jsp (one jsp file may contain multiple
    * dbforms, and each forms could contain many subforms nested inside!)
    *
    * @return the vector object containing the Table objects involved with the
    *         main table
    */
   private Vector parseInvolvedTables() {
      String[] invTables = ParseUtil.getParameterValues(request, "invtable");

      // in empty forms, for example, we don't have any involved tables..!
      if (invTables == null) {
         return null;
      }

      Vector result = new Vector();

      for (int i = 0; i < invTables.length; i++) {
         int   tableIndex = Integer.parseInt(invTables[i]);
         Table t = config.getTable(tableIndex);

         result.addElement(t);
      }

      return result;
   }
}

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

package org.dbforms.taglib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;

import org.dbforms.util.Util;

import javax.servlet.jsp.tagext.BodyTagSupport;



/**
 * <p>
 * Base class for TagSupport that render form data-elements capable of
 * including JavaScript event handlers and/or CSS Style attributes.
 * </p>
 *
 * <p>
 * the html/css releated properties and methods where originally done by Don
 * Clasen for Apache Groups's Jakarta-Struts project.
 * </p>
 */
public abstract class TagSupportWithScriptHandler extends BodyTagSupport {
   private static Log logCat = LogFactory.getLog(DbBaseHandlerTag.class.getName());

   /** DOCUMENT ME! */
   private String accessKey = null;

   // Focus Events

   /** Component lost focus event. */
   private String onBlur = null;

   /** Content changed after component lost focus event. */
   private String onChange = null;

   //  Mouse Events

   /** Mouse click event. */
   private String onClick = null;

   /** Mouse double click event. */
   private String onDblClick = null;

   /** Component has received focus event. */
   private String onFocus = null;

   //  Keyboard Events

   /** Key down in component event. */
   private String onKeyDown = null;

   /** Key down and up together in component event. */
   private String onKeyPress = null;

   /** Key released in component event. */
   private String onKeyUp = null;

   /** Mouse pressed on component event. */
   private String onMouseDown = null;

   /** Mouse moved over component event. */
   private String onMouseMove = null;

   /** Mouse exit component event. */
   private String onMouseOut = null;

   /** Mouse over component event. */
   private String onMouseOver = null;

   /** Mouse released on component event. */
   private String onMouseUp = null;

   // Text Events

   /** Text selected in component event. */
   private String onSelect = null;

   // CSS Style Support

   /** Style attribute associated with component. */
   private String style = null;

   /** Named Style class associated with component. */
   private String styleClass = null;

   /** Tab index value. */
   private String tabIndex = null;

   /** Title (Tool Tip/Hint) attribute */
   private String title = null;

   //  Navigation Management

   /**
    * Sets the accessKey character.
    *
    * @param accessKey DOCUMENT ME!
    */
   public void setAccessKey(String accessKey) {
      this.accessKey = accessKey;
   }


   /**
    * Returns the accessKey character.
    *
    * @return DOCUMENT ME!
    */
   public String getAccessKey() {
      return (this.accessKey);
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public DbFormsConfig getConfig() {
      try {
         return DbFormsConfigRegistry.instance()
                                     .lookup();
      } catch (Exception e) {
         logCat.error("getConfig", e);

         return null;
      }
   }


   // Focus Events

   /**
    * Sets the onBlur event handler.
    *
    * @param onBlur DOCUMENT ME!
    */
   public void setOnBlur(String onBlur) {
      this.onBlur = onBlur;
   }


   /**
    * Returns the onBlur event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnBlur() {
      return onBlur;
   }


   // Text Events

   /**
    * Sets the onChange event handler.
    *
    * @param onChange DOCUMENT ME!
    */
   public void setOnChange(String onChange) {
      this.onChange = onChange;
   }


   /**
    * Returns the onChange event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnChange() {
      return onChange;
   }


   // Mouse Events

   /**
    * Sets the onClick event handler.
    *
    * @param onClick DOCUMENT ME!
    */
   public void setOnClick(String onClick) {
      this.onClick = onClick;
   }


   /**
    * Returns the onClick event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnClick() {
      return onClick;
   }


   /**
    * Sets the onDblClick event handler.
    *
    * @param onDblClick DOCUMENT ME!
    */
   public void setOnDblClick(String onDblClick) {
      this.onDblClick = onDblClick;
   }


   /**
    * Returns the onDblClick event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnDblClick() {
      return onDblClick;
   }


   /**
    * Sets the onFocus event handler.
    *
    * @param onFocus DOCUMENT ME!
    */
   public void setOnFocus(String onFocus) {
      this.onFocus = onFocus;
   }


   /**
    * Returns the onFocus event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnFocus() {
      return onFocus;
   }


   // Keyboard Events

   /**
    * Sets the onKeyDown event handler.
    *
    * @param onKeyDown DOCUMENT ME!
    */
   public void setOnKeyDown(String onKeyDown) {
      this.onKeyDown = onKeyDown;
   }


   /**
    * Returns the onKeyDown event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnKeyDown() {
      return onKeyDown;
   }


   /**
    * Sets the onKeyPress event handler.
    *
    * @param onKeyPress DOCUMENT ME!
    */
   public void setOnKeyPress(String onKeyPress) {
      this.onKeyPress = onKeyPress;
   }


   /**
    * Returns the onKeyPress event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnKeyPress() {
      return onKeyPress;
   }


   /**
    * Sets the onKeyUp event handler.
    *
    * @param onKeyUp DOCUMENT ME!
    */
   public void setOnKeyUp(String onKeyUp) {
      this.onKeyUp = onKeyUp;
   }


   /**
    * Returns the onKeyUp event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnKeyUp() {
      return onKeyUp;
   }


   /**
    * Sets the onMouseDown event handler.
    *
    * @param onMouseDown DOCUMENT ME!
    */
   public void setOnMouseDown(String onMouseDown) {
      this.onMouseDown = onMouseDown;
   }


   /**
    * Returns the onMouseDown event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnMouseDown() {
      return onMouseDown;
   }


   /**
    * Sets the onMouseMove event handler.
    *
    * @param onMouseMove DOCUMENT ME!
    */
   public void setOnMouseMove(String onMouseMove) {
      this.onMouseMove = onMouseMove;
   }


   /**
    * Returns the onMouseMove event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnMouseMove() {
      return onMouseMove;
   }


   /**
    * Sets the onMouseOut event handler.
    *
    * @param onMouseOut DOCUMENT ME!
    */
   public void setOnMouseOut(String onMouseOut) {
      this.onMouseOut = onMouseOut;
   }


   /**
    * Returns the onMouseOut event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnMouseOut() {
      return onMouseOut;
   }


   /**
    * Sets the onMouseOver event handler.
    *
    * @param onMouseOver DOCUMENT ME!
    */
   public void setOnMouseOver(String onMouseOver) {
      this.onMouseOver = onMouseOver;
   }


   /**
    * Returns the onMouseOver event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnMouseOver() {
      return onMouseOver;
   }


   /**
    * Sets the onMouseUp event handler.
    *
    * @param onMouseUp DOCUMENT ME!
    */
   public void setOnMouseUp(String onMouseUp) {
      this.onMouseUp = onMouseUp;
   }


   /**
    * Returns the onMouseUp event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnMouseUp() {
      return onMouseUp;
   }


   /**
    * Sets the onSelect event handler.
    *
    * @param onSelect DOCUMENT ME!
    */
   public void setOnSelect(String onSelect) {
      this.onSelect = onSelect;
   }


   /**
    * Returns the onSelect event handler.
    *
    * @return DOCUMENT ME!
    */
   public String getOnSelect() {
      return onSelect;
   }


   // CSS Style Support

   /**
    * Sets the style attribute.
    *
    * @param style DOCUMENT ME!
    */
   public void setStyle(String style) {
      this.style = style;
   }


   /**
    * Returns the style attribute.
    *
    * @return DOCUMENT ME!
    */
   public String getStyle() {
      return style;
   }


   /**
    * Sets the style class attribute.
    *
    * @param styleClass DOCUMENT ME!
    */
   public void setStyleClass(String styleClass) {
      this.styleClass = styleClass;
   }


   /**
    * Returns the style class attribute.
    *
    * @return DOCUMENT ME!
    */
   public String getStyleClass() {
      return styleClass;
   }


   /**
    * Sets the tabIndex value.
    *
    * @param tabIndex DOCUMENT ME!
    */
   public void setTabIndex(String tabIndex) {
      this.tabIndex = tabIndex;
   }


   /**
    * Returns the tabIndex value.
    *
    * @return DOCUMENT ME!
    */
   public String getTabIndex() {
      return (this.tabIndex);
   }


   /**
    * Sets the title attribute.
    *
    * @param title DOCUMENT ME!
    */
   public void setTitle(String title) {
      this.title = title;
   }


   /**
    * Returns the title attribute.
    *
    * @return DOCUMENT ME!
    */
   public String getTitle() {
      return title;
   }


   /**
    * reset tag state
    *
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally()
    */
   public void doFinally() {
      accessKey   = null;
      tabIndex    = null;
      onClick     = null;
      onDblClick  = null;
      onMouseOver = null;
      onMouseOut  = null;
      onMouseMove = null;
      onMouseDown = null;
      onMouseUp   = null;
      onKeyDown   = null;
      onKeyUp     = null;
      onKeyPress  = null;
      onSelect    = null;
      onChange    = null;
      onBlur      = null;
      onFocus     = null;
      style       = null;
      styleClass  = null;
      title       = null;
   }


   /**
    * Prepares the event handlers for inclusion in the component's HTML tag.
    *
    * @return The prepared String for inclusion in the HTML tag.
    */
   protected String prepareEventHandlers() {
      StringBuffer handlers = new StringBuffer();
      prepareIdEvents(handlers); // Fossato, 20011008		
      prepareMouseEvents(handlers);
      prepareKeyEvents(handlers);
      prepareTextEvents(handlers);
      prepareFocusEvents(handlers);

      return handlers.toString();
   }


   /**
    * Prepares the style attributes for inclusion in the component's HTML tag.
    *
    * @return The prepared String for inclusion in the HTML tag.
    */
   protected String prepareStyles() {
      StringBuffer styles = new StringBuffer();

      if (!Util.isNull(getStyleClass())) {
         styles.append(" class=\"");
         styles.append(getStyleClass());
         styles.append("\"");
      }

      if (!Util.isNull(getStyle())) {
         styles.append(" style=\"");
         styles.append(getStyle());
         styles.append("\"");
      }

      if (!Util.isNull(getTitle())) {
         styles.append(" title=\"");
         styles.append(getTitle());
         styles.append("\"");
      }

      return styles.toString();
   }


   /**
    * Prepares the focus event handlers, appending them to the the given
    * StringBuffer.
    *
    * @param handlers The StringBuffer that output will be appended to.
    */
   private void prepareFocusEvents(StringBuffer handlers) {
      if (onBlur != null) {
         handlers.append(" onBlur=\"");
         handlers.append(onBlur);
         handlers.append("\"");
      }

      if (onFocus != null) {
         handlers.append(" onFocus=\"");
         handlers.append(onFocus);
         handlers.append("\"");
      }
   }


   // -------------------------------------------------------- Private Methods

   /**
    * Prepares the id handlers, appending them to the the given StringBuffer.
    *
    * @param handlers The StringBuffer that output will be appended to.
    */
   private void prepareIdEvents(StringBuffer handlers) { // Fossato, 20011008

      if (id != null) {
         handlers.append(" id=\"")
                 .append(id)
                 .append("\"");
      }
   }


   /**
    * Prepares the keyboard event handlers, appending them to the the given
    * StringBuffer.
    *
    * @param handlers The StringBuffer that output will be appended to.
    */
   private void prepareKeyEvents(StringBuffer handlers) {
      if (onKeyDown != null) {
         handlers.append(" onKeyDown=\"");
         handlers.append(onKeyDown);
         handlers.append("\"");
      }

      if (onKeyUp != null) {
         handlers.append(" onKeyUp=\"");
         handlers.append(onKeyUp);
         handlers.append("\"");
      }

      if (onKeyPress != null) {
         handlers.append(" onKeyPress=\"");
         handlers.append(onKeyPress);
         handlers.append("\"");
      }
   }


   /**
    * Prepares the mouse event handlers, appending them to the the given
    * StringBuffer.
    *
    * @param handlers The StringBuffer that output will be appended to.
    */
   private void prepareMouseEvents(StringBuffer handlers) {
      if (onClick != null) {
         handlers.append(" onClick=\"");
         handlers.append(onClick);
         handlers.append("\"");
      }

      if (onDblClick != null) {
         handlers.append(" onDblClick=\"");
         handlers.append(onDblClick);
         handlers.append("\"");
      }

      if (onMouseOver != null) {
         handlers.append(" onMouseOver=\"");
         handlers.append(onMouseOver);
         handlers.append("\"");
      }

      if (onMouseOut != null) {
         handlers.append(" onMouseOut=\"");
         handlers.append(onMouseOut);
         handlers.append("\"");
      }

      if (onMouseMove != null) {
         handlers.append(" onMouseMove=\"");
         handlers.append(onMouseMove);
         handlers.append("\"");
      }

      if (onMouseDown != null) {
         handlers.append(" onMouseDown=\"");
         handlers.append(onMouseDown);
         handlers.append("\"");
      }

      if (onMouseUp != null) {
         handlers.append(" onMouseUp=\"");
         handlers.append(onMouseUp);
         handlers.append("\"");
      }
   }


   /**
    * Prepares the text event handlers, appending them to the the given
    * StringBuffer.
    *
    * @param handlers The StringBuffer that output will be appended to.
    */
   private void prepareTextEvents(StringBuffer handlers) {
      if (onSelect != null) {
         handlers.append(" onSelect=\"");
         handlers.append(onSelect);
         handlers.append("\"");
      }

      if (onChange != null) {
         handlers.append(" onChange=\"");
         handlers.append(onChange);
         handlers.append("\"");
      }
   }
}

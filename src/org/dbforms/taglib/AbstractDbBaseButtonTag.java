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

import org.dbforms.config.Table;

import org.dbforms.util.MessageResources;
import org.dbforms.util.Util;

import org.dbforms.validation.ValidatorConstants;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * abstract base class for buttons supports 3 types of Buttons: #fixme - docu
 * 
 * @author Joachim Peer
 */
public abstract class AbstractDbBaseButtonTag extends AbstractDbBaseHandlerTag {

    /** DOCUMENT ME! */
    protected static final int FLAVOR_STANDARD = 0;

    /** DOCUMENT ME! */
    protected static final int FLAVOR_IMAGE = 1;

    /** DOCUMENT ME! */
    protected static final int FLAVOR_MODERN = 2;

    private static int uniqueID = 0;

    private String alt; // used if flavor is "image"

    private String border; // used to set html border attribute"

    private String caption; // used if flavor is "standard"

    //20040225 JFM
    private String disabledBehaviour;

    private String disabledImageAlt;

    private String disabledImageHeight;

    private String disabledImageSrc;

    private String disabledImageWidth;

    private String followUp;

    private String followUpOnError;

    private String src; // used if flavor is "image"

    private Table table;

    private int choosenFlavor = 0; // default = standard

    /**
     * DOCUMENT ME!
     * 
     * @param src
     *            DOCUMENT ME!
     */
    public void setAlt(String src) {
        this.alt = src;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getAlt() {
        return alt;
    }

    /**
     * Sets the border
     * 
     * @param border
     *            The border to set
     */
    public void setBorder(String border) {
        this.border = border;
    }

    /**
     * Gets the border
     * 
     * @return Returns a String
     */
    public String getBorder() {
        return border;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param caption
     *            DOCUMENT ME!
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getCaption() {
        return caption;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public int getChoosenFlavor() {
        return choosenFlavor;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param disabledBehaviour -
     *            possible values: "nohtml", "altimage", "disabled"
     */
    public void setDisabledBehaviour(String disabledBehaviour) {
        this.disabledBehaviour = disabledBehaviour;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getDisabledBehaviour() {
        return disabledBehaviour;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param disabledImageAlt -
     *            alternative image if button is disabled and flavor 'image'
     */
    public void setDisabledImageAlt(String disabledImageAlt) {
        this.disabledImageAlt = disabledImageAlt;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return alternative image alt text if button is disabled and flavor
     *         'image'
     */
    public String getDisabledImageAlt() {
        return disabledImageAlt;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param disabledImageHeight -
     *            the height of disabledImageSrc
     */
    public void setDisabledImageHeight(String disabledImageHeight) {
        this.disabledImageHeight = disabledImageHeight;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getDisabledImageHeight() {
        return disabledImageHeight;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param disabledImageSrc -
     *            alternative image alt text if button is disabled and flavor
     *            'image'
     */
    public void setDisabledImageSrc(String disabledImageSrc) {
        this.disabledImageSrc = disabledImageSrc;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return alternative image src if button is disabled and flavor 'image'
     */
    public String getDisabledImageSrc() {
        return disabledImageSrc;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param disabledImageWidth -
     *            the width of disabledImageSrc
     */
    public void setDisabledImageWidth(String disabledImageWidth) {
        this.disabledImageWidth = disabledImageWidth;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getDisabledImageWidth() {
        return disabledImageWidth;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param flavor
     *            DOCUMENT ME!
     */
    public void setFlavor(String flavor) {
        if ("image".equals(flavor)) {
            choosenFlavor = FLAVOR_IMAGE;
        } else if ("modern".equals(flavor)) {
            choosenFlavor = FLAVOR_MODERN;
        } else {
            choosenFlavor = FLAVOR_STANDARD;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param followUp
     *            DOCUMENT ME!
     */
    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getFollowUp() {
        return followUp;
    }

    /**
     * Sets the followUpOnError
     * 
     * @param followUpOnError
     *            The followUpOnError to set
     */
    public void setFollowUpOnError(String followUpOnError) {
        this.followUpOnError = followUpOnError;
    }

    /**
     * Gets the followUpOnError
     * 
     * @return Returns a String
     */
    public String getFollowUpOnError() {
        return followUpOnError;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public void setParent(final javax.servlet.jsp.tagext.Tag parent) {
        super.setParent(parent);
        table = getParentForm().getTable();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param src
     *            DOCUMENT ME!
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getSrc() {
        return src;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public Table getTable() {
        return table;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws javax.servlet.jsp.JspException
     *             DOCUMENT ME!
     * @throws JspException
     *             DOCUMENT ME!
     */
    public int doEndTag() throws javax.servlet.jsp.JspException {
        if (choosenFlavor == FLAVOR_MODERN) {
            try {
                if (bodyContent != null) {
                    bodyContent.writeOut(bodyContent.getEnclosingWriter());
                }

                pageContext.getOut().write("</button>");
            } catch (java.io.IOException ioe) {
                throw new JspException("IO Error: " + ioe.getMessage());
            }
        }

        return EVAL_PAGE;
    }

    /**
     * DOCUMENT ME!
     */
    public void doFinally() {
        followUp = null;
        followUpOnError = null;
        table = null;
        choosenFlavor = 0;
        caption = null;
        src = null;
        alt = null;
        border = null;
        super.doFinally();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws javax.servlet.jsp.JspException
     *             DOCUMENT ME!
     */
    public int doStartTag() throws javax.servlet.jsp.JspException {
        if ((getParentForm().getFormValidatorName() != null)
                && (getParentForm().getFormValidatorName().length() > 0)
                && getParentForm().hasJavascriptValidationSet()) {
            String onclick = (getOnClick() != null) ? getOnClick() : "";

            if (onclick.lastIndexOf(";") != (onclick.length() - 1)) {
                onclick += ";"; // be sure javascript end with ";"
            }

            setOnClick(onclick + JsValidation());
        }

        return SKIP_BODY;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected static synchronized int getUniqueID() {
        uniqueID++;

        return uniqueID;
    }

    /**
     * returns beginnings of tags with attributes defining type/value/[src/alt -
     * if image]
     * 
     * @return DOCUMENT ME!
     */
    protected String getButtonBegin() {
        StringBuffer buf = new StringBuffer();
        Locale locale = MessageResources
                .getLocale((HttpServletRequest) pageContext.getRequest());

        String privCaption = getCaption();
        // If the caption is not null and the resources="true" attribut
        if ((privCaption != null) && getParentForm().hasCaptionResourceSet()) {
            privCaption = MessageResources.getMessage(privCaption, locale);
        }

        String privAlt = getAlt();
        // If the caption is not null and the resources="true" attribut
        if ((privAlt != null) && getParentForm().hasCaptionResourceSet()) {
            privAlt = MessageResources.getMessage(privAlt, locale);
        }

        switch (choosenFlavor) {
            case FLAVOR_IMAGE:
                buf.append("<input type=\"image\" ");

                if (src != null) {
                    buf.append(" src=\"");
                    buf.append(src);
                    buf.append("\" ");
                }

                if (privAlt != null) {
                    buf.append(" alt=\"");
                    buf.append(privAlt);
                    buf.append("\" ");
                }

                break;

            case FLAVOR_MODERN:
                buf.append("<button type=\"submit\" ");

                if (privCaption != null) {
                    buf.append(" value=\""); // not visible - neither on ie nor
                    // netscape (?) [only tags embedded in
                    // this tags get rendered !]
                    buf.append(privCaption);
                    buf.append("\" ");
                }

                break;

            default: // FLAVOR_STANDARD
                buf.append("<input type=\"submit\" ");

                if (privCaption != null) {
                    buf.append(" value=\""); // not very useful: this is _not_
                    // visible!
                    buf.append(privCaption);
                    buf.append("\" ");
                }
        }

        buf.append(prepareStyles());
        buf.append(prepareEventHandlers());

        return buf.toString();
    }

    /**
     * returns beginnings of tags with attributes defining type/value/[src/alt -
     * if image]
     * 
     * @return DOCUMENT ME!
     */
    protected String getButtonEnd() {
        switch (choosenFlavor) {
            case FLAVOR_IMAGE:
                return "\"/>";

            case FLAVOR_MODERN:
                return "\">";

            default: // FLAVOR_STANDARD

                return "\"/>";
        }
    }

    /**
     * renders tag containing additional information about that button: ie
     * followUp, associatedRadio, etc.
     * 
     * @param primaryTagName
     *            DOCUMENT ME!
     * @param dataKey
     *            DOCUMENT ME!
     * @param dataValue
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected String getDataTag(String primaryTagName, String dataKey,
            String dataValue) {
        String s = "";

        if (!Util.isNull(dataValue)) {
            StringBuffer tagBuf = new StringBuffer();
            tagBuf.append("<input type=\"hidden\" name=\"data");
            tagBuf.append(primaryTagName);
            tagBuf.append("_");
            tagBuf.append(dataKey);
            tagBuf.append("\" value =\"");
            tagBuf.append(dataValue);
            tagBuf.append("\"/>");
            s = tagBuf.toString();
        }

        return s;
    }

    //20040225 JFM: added

    /**
     * DOCUMENT ME!
     * 
     * @return HTML code for the disabled Image
     */
    protected String getDisabledImage() {
        StringBuffer imgBuf = new StringBuffer();

        // image src
        imgBuf.append("<img src=\"");
        imgBuf.append(getDisabledImageSrc());
        imgBuf.append("\"");

        // image alt
        imgBuf.append(" alt=\"");
        imgBuf.append(getDisabledImageAlt());
        imgBuf.append("\"");

        // image style
        imgBuf.append(prepareStyles());

        // image events:
        //imgBuf.append(prepareEventHandlers());
        // image width and height
        if (!Util.isNull(getDisabledImageWidth())) {
            imgBuf.append(" width=\"");
            imgBuf.append(getDisabledImageWidth());
            imgBuf.append("\"");
        }

        if (!Util.isNull(getDisabledImageHeight())) {
            imgBuf.append(" height=\"");
            imgBuf.append(getDisabledImageHeight());
            imgBuf.append("\"");
        }

        imgBuf.append(" />");

        return imgBuf.toString();
    }

    /**
     * returns the JavaScript validation flags. Will be put into the onClick
     * event of the main form Must be overloaded by update and delete button
     * 
     * @return the java script validation vars.
     */
    protected String JsValidation() {
        return ValidatorConstants.JS_CANCEL_VALIDATION + "=false;";
    }

    /**
     * Prepares the style attributes for inclusion in the component's HTML tag.
     * 
     * @return The prepared String for inclusion in the HTML tag.
     */
    protected String prepareStyles() {
        StringBuffer styles = new StringBuffer(super.prepareStyles());

        if (border != null) {
            styles.append(" border=\"");
            styles.append(border);
            styles.append("\"");
        }

        return styles.toString();
    }
}

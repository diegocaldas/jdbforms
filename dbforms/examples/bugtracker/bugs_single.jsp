<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@  page language="java" import="org.dbforms.util.MessageResources,java.util.Locale" %>

<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/request.tld" prefix="request" %>


<html>
<head>
<db:base/>
<link rel="stylesheet" href="dbforms.css"/>
<META name="GENERATOR" content="IBM WebSphere Studio">
</head>

<body bgcolor="#ccffff" >


<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
    <tr>
        <td><img src="templates/sourceforge/images/bt_logo.jpg" border="0"></td>
        <td align="right"><img src="templates/sourceforge/images/pfeilchen.jpg"/> <a href="bugs_list.jsp">[<db:message key="menu.list.displayname"/>]</a>&nbsp; 
 
        	<request:isuserinrole role="bug_admin" value="true"> 
	        	<img src="templates/sourceforge/images/pfeilchen.jpg"/>
        		<a href="admin_menu.jsp">[<db:message key="menu.admin.displayname"/>]</a> 
        	</request:isuserinrole>
 
         	<request:isuserinrole role="bug_admin" value="false"> 
	        	<img src="templates/sourceforge/images/pfeilchen.jpg"/>
        		<a href="admin_menu.jsp">[<db:message key="menu.login.displayname"/>]</a> 
        	</request:isuserinrole>
 
        	<request:isuserinrole role="bug_admin" value="true"> 
        		<img src="templates/sourceforge/images/pfeilchen.jpg"/> 
        	 	<a href="logout.jsp">[<db:message key="menu.logout.displayname"/>]</a> 
        	</request:isuserinrole> 
 
        </td>
    </tr>
</table>

<%  String readonly = request.getParameter("readonly");
	readonly = (readonly!=null && readonly.equals("true"))? readonly:"false";
	
	String class1 = (readonly.equals("true"))? "readonly1":"normal1";
	String class2 = (readonly.equals("true"))? "readonly2":"normal2";
%>
	
<db:dbform 	tableName="bugs" 
			maxRows="1" 
			followUp="/bugs_single.jsp" 
			redisplayFieldsOnError="true" 
			autoUpdate="false"
			captionResource="true"
			formValidatorName="Bugs"
			javascriptValidation="true"
			javascriptValidationSrcFile="validation.js"			
			readOnly="<%=readonly%>"> 
			


<db:body>

<db:header>
	<h1><db:message key="bugsForm.title.modify"/></h1>
</db:header> 

<db:xmlErrors/> 

<table border="0" cellspacing="1" align="center" width="500">

    <tr class="<%=class1%>">
        <td align=right>ID </td>
        <td>#<db:label fieldName="id"/></td>
    </tr>


    <tr class="<%=class2%>" >
        <td><db:message key="bugsForm.category.displayname"/> </td>
        <td><db:select fieldName="category" size="0" > 
        		<db:tableData 	name="data_category" 
        						foreignTable="category" 
        						visibleFields="title" 
        						storeField="id"/> 
        	</db:select>
        </td>
    </tr>
    <tr class="<%=class1%>" >
        <td><db:message key="bugsForm.priority.displayname"/> </td>
        <td><db:radio fieldName="priority"  growDirection="vertical" > 
        		<db:tableData name="data_priority" foreignTable="priority" visibleFields="title" storeField="id" /> 
        	</db:radio>
        </td>
    </tr>
    <tr class="<%=class2%>" >
        <td><db:message key="bugsForm.title.displayname"/> </td>
        <td><db:textField fieldName="title" size="40"   style="FONT-COLOR:red"/></td>
    </tr>
    <tr class="<%=class1%>" >
        <td><db:message key="bugsForm.description.displayname"/> </td>
        <td><db:textArea fieldName="description" cols="40" rows="3" wrap="virtual" readOnlyStyleClass="readonly1"/></td>
    </tr>


    <tr class="<%=class2%>" >
        <td><db:message key="bugsForm.indate.displayname"/></td>
        <td><% java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd"); %> 
		<db:dateField fieldName="indate" formatter="<%=sdf%>" size="12" />&nbsp;&nbsp;&nbsp;
		<db:dateLabel fieldName="indate" formatter="<%=sdf%>"/>
		</td>
    </tr>


    <tr class="<%=class1%>" >
        <td><db:message key="bugsForm.reporter.displayname"/></td>
        <td><db:textField fieldName="reporter" size="40" /></td>
    </tr>

    <tr class="<%=class2%>" >
        <td><db:message key="bugsForm.phone.displayname"/></td>
        <td><db:textField fieldName="phone" size="15" /></td>
    </tr>

    <tr class="<%=class1%>" >
        <td><db:message key="bugsForm.bugstate.displayname"/></td>
        <td>

        	<db:select fieldName="bugstate" size="3"> 
        		<db:staticData name="data_bugstate"> 
        			<db:staticDataItem key="0" value="bugsForm.status.0"/> 
        			<db:staticDataItem key="1" value="bugsForm.status.1"/> 
        			<db:staticDataItem key="2" value="bugsForm.status.2"/> 
        		</db:staticData> 
        	</db:select>
        
        </td>
    </tr>
  
    <tr class="<%=class2%>" >
        <td><db:message key="bugsForm.contactFirst.displayname"/></td>
        <td>
 	       <db:checkbox fieldName="contactFirst" value="1"/>
		</td>
	</tr>


</table>

<center>

<input 	type=checkbox 
		name=readonly 
		value="true"  <%=(readonly.equals("true"))? "checked":""%>
		onClick="document.dbform.submit();"	>Read-only Form : <%=readonly%><BR>
<BR>

<% if(!readonly.equals("true")) { %>
	<table>
		<tr align=center>
			<td>
				<db:updateButton  caption="button.save" /> 
			</td>
			
			<td>
				<db:deleteButton  caption="button.delete" followUp="/bugs_list.jsp" confirmMessage="bugsForm.delete.confirm" /> 
			</td>
		</tr>
	</table>
<% } %>

<table align="center" border="0">


    <tr>
        
        <db:isWebEvent event="navFirst" value="false"> 
 	       <td><db:navFirstButton caption="button.nav.first" style="width:90"/></td>
	    </db:isWebEvent>
	        
        <td><db:navPrevButton caption="button.nav.previous" style="width:90"/></td>
        <td><db:navNextButton caption="button.nav.next" style="width:90"/></td>

        <db:isWebEvent event="navLast" value="false"> 
	        <td><db:navLastButton caption="button.nav.last" style="width:90"/></td>
	    </db:isWebEvent>
	    
    </tr>
</table>
</center>
</db:body> <db:footer>
<table align="center" border="0">


</table>

</db:footer> 


<db:isWebEvent event="navfirst" value="false"> 
	<h4>Not navFirst event</h4>
	<db:isWebEvent event="navlast" value="false"> 
		<h4>All event except navFirst and navLast</h4>
	</db:isWebEvent>
</db:isWebEvent>

<db:isWebEvent event="update" value="true"> <h4>Update event throwed</h4> </db:isWebEvent>
<db:isWebEvent event="delete" value="true"> <h4>Delete event throwed</h4> </db:isWebEvent>
<db:isWebEvent event="noop" value="true"> <h4>Noop event throwed</h4> </db:isWebEvent>

</db:dbform>

<h5 style="color:#ff3333">using I18N for error messages </h5>
</body>
</html>




<!--

  This page do the same thing of bugs_single.jsp except we have add some exemples
  of using javascript functionnalities. The javascript code can be modified as you
  wish, because DbForms give a tools to work with data, but the rest it's in charge 
  of the javascript developpers ...
  
  Don't forget to modify your database structure accordingly with the new fields
  in Bugs Table.
  
--> 



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

<body bgcolor="#ccffff" onLoad="Initialize();">


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
			followUp="/bugs_single_js.jsp" 
			redisplayFieldsOnError="true" 
			autoUpdate="false"
			captionResource="true"
			formValidatorName="Bugs"
			javascriptValidation="true"
			javascriptFieldsArray="true"
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
		<db:dateField fieldName="indate" format="<%=sdf%>" size="12" />&nbsp;&nbsp;&nbsp;
		<db:dateLabel fieldName="indate" format="<%=sdf%>"/>
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
        	<!-- See BugInterceptor for update value 
        		Due to the limitation of POST/GET, never send object when unchecked
        	-->
	       <db:checkbox fieldName="contactFirst" value="1"/>
		</td>
	</tr>

   <tr class="<%=class1%>" >
        <td><db:message key="bugsForm.platform.displayname"/></td>
        <td>
	       <db:select fieldName="server_cat_id" onChange="updateSelectModels(this.value,0);" >
				<db:queryData name="server_categories" 
					  query="SELECT id,name FROM server_categories"/>
			</db:select>

			<db:select fieldName="server_model_id">
				<db:queryData name="server_models" 
						  query="SELECT id,name FROM server_models"/>
			</db:select>

		</td>
	</tr>

  	
	

</table>

<SCRIPT language=javascript>
var selCatSelected = '<%=rsv_bugs.getField("server_cat_id").trim()%>';
var selModelSelected = '<%=rsv_bugs.getField("server_model_id").trim()%>';
</SCRIPT>

<db:javascriptArray name="arrServerMod">
	<db:queryData name="server_models_js" 
				  query="SELECT id, cat_id, name FROM server_models"/>
</db:javascriptArray>

<br/>
<center>

<input 	type=checkbox 
		name=readonly 
		value="true"  <%=(readonly.equals("true"))? "checked":""%>
		onClick="document.dbform.submit();"	>Read-only Form : <%=readonly%><BR>
<BR>

<% if(!readonly.equals("true")) { %>
<table>
	<tr align=center>
		<td><db:updateButton  caption="button.save" /> </td>
		<td><db:deleteButton  caption="button.delete" followUp="/bugs_list.jsp" /> </td>
	</tr>
</table>

<% } %>

<table align="center" border="0">


    <tr>
        <td><db:navFirstButton caption="button.nav.first" style="width:90"/></td>
        <td><db:navPrevButton caption="button.nav.previous" style="width:90"/></td>
        <td><db:navNextButton caption="button.nav.next" style="width:90"/></td>
        <td><db:navLastButton caption="button.nav.last" style="width:90"/></td>
    </tr>
</table>
</center>
</db:body> <db:footer>
<table align="center" border="0">


</table>

</db:footer> 
</db:dbform>

<db:isWebEvent event="navfirst" value="true"> <h4>navFirst event throwed</h4> </db:isWebEvent>
<db:isWebEvent event="navlast" value="true"> <h4>navLast event throwed</h4> </db:isWebEvent>
<db:isWebEvent event="navNext" value="true"> <h4>navNext event throwed</h4> </db:isWebEvent>
<db:isWebEvent event="navPrev" value="true"> <h4>navPrevious event throwed</h4> </db:isWebEvent>
<db:isWebEvent event="navNew" value="true"> <h4>navNew event throwed</h4> </db:isWebEvent>
<db:isWebEvent event="goto" value="true"> <h4>Goto event throwed</h4> </db:isWebEvent>
<db:isWebEvent event="update" value="true"> <h4>Update event throwed</h4> </db:isWebEvent>
<db:isWebEvent event="delete" value="true"> <h4>Delete event throwed</h4> </db:isWebEvent>
<db:isWebEvent event="noop" value="true"> <h4>Noop event throwed</h4> </db:isWebEvent>

<h5 style="color:#ff3333">using I18N for error messages </h5>
</body>
</html>

<SCRIPT language=javascript>

function Initialize(){	
	updateSelectModels(selCatSelected, selModelSelected);
}

function updateSelectModels(cat_id, selected){
	
	if('<%=readonly%>'=='true') return;
		
	var selectName = getDbFormFieldName("server_model_id");
	var select = document.dbform[selectName];
	select.options.length = 0;
	var tmp;
	var selectedIndex = 0;
	var countElement=0;
	for(i=0;i<arrServerMod.length;i++){
		tmp = arrServerMod[i];
		if(tmp[1]==cat_id) {
			select.options[select.length] = new Option(tmp[2],tmp[0]);
			if(tmp[0]==selected) selectedIndex =countElement;
			countElement++;
		}
		
	}
	select.selectedIndex=selectedIndex;
}
</SCRIPT>


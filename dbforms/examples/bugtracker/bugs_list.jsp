<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/request.tld" prefix="request" %>


<html>
<head>
<db:base/>
<title></title>
<link rel="stylesheet" href="dbforms.css">
<META name="GENERATOR" content="IBM WebSphere Studio">
</head>

<body bgcolor="#ccffff">


<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
    <tr>
        <td><img src="templates/sourceforge/images/bt_logo.jpg" border="0"></td>
        <td align="right">
        		<img src="templates/sourceforge/images/pfeilchen.jpg"/> 
        		<a href="bugs_single_readonly.jsp">[<db:message key="menu.list.readonly.displayname"/>]</a> 
 
        		<request:isuserinrole role="bug_admin" value="true"> 
        			<img src="templates/sourceforge/images/pfeilchen.jpg"/>
        			<a href="admin_menu.jsp">[<db:message key="menu.admin.displayname"/>]</a>&nbsp; 
        		</request:isuserinrole> 
         		<request:isuserinrole role="bug_admin" value="true"> 
        			<img src="templates/sourceforge/images/pfeilchen.jpg"/> 
        			<a href="logout.jsp">[<db:message key="menu.logout.displayname"/>]</a> 	
        		</request:isuserinrole> 
        		
        		<request:isuserinrole role="bug_admin" value="false"> 
        			<img src="templates/sourceforge/images/pfeilchen.jpg"/> 
        			<a href="admin_menu.jsp">[<db:message key="menu.login.displayname"/>]</a> 
        		</request:isuserinrole></td>
    </tr>
</table>


<% int i=0; %>
<BR>
<db:dbform 		tableName="bugs" 
				maxRows="*" 
				followUp="/bugs_list.jsp" 
				autoUpdate="false"
				captionResource="true"> 
<db:header> 

<db:xmlErrors/>

<table align="center" cellspacing=1 cellpadding=2 bgcolor="#ffffff" width="100%">
    <tr bgcolor="#ccffff" valign="top">



        <td>id</td>
        <td><db:message key="bugsForm.category.displayname"/><br>
        <db:sort fieldName="category"/></td>
        <td><db:message key="bugsForm.priority.displayname"/><br>
        <db:sort fieldName="priority"/></td>
        <td><db:message key="bugsForm.title.displayname"/></td>

        <td><db:message key="bugsForm.indate.displayname"/><br>
        <db:sort fieldName="indate"/></td>
        <td><db:message key="bugsForm.outdate.displayname"/><br>
        <db:sort fieldName="outdate"/></td>
        <td><db:message key="bugsForm.reporter.displayname"/><br>
        <db:sort fieldName="reporter"/></td>
        <td><db:message key="bugsForm.bugstate.displayname"/><br>
        <db:sort fieldName="bugstate"/></td>

    </tr>
    </db:header>

    <db:body allowNew="false"> <% String bgcolor = (i % 2 == 0) ? "#FEE9CE" : "#FEE9AA"; i++; 
          
          if( "1".equals( (String) currentRow_bugs.get("priority")) ) {
            bgcolor = "#FF9999";
          }
          
          %>
    <tr bgcolor="<%= bgcolor %>">
        <td><a href="<db:linkURL href="/bugs_single_readonly.jsp" tableName="bugs" position="<%= position_bugs %>"/>"><%= currentRow_bugs.get("id") %></a>&nbsp;</td>
        <td><db:dataLabel fieldName="category"> <db:tableData name="data_category" foreignTable="category" visibleFields="title" storeField="id"/> </db:dataLabel></td>

        <td><img src="templates/sourceforge/images/<db:dataLabel fieldName="priority">                  
            <db:tableData 
               name="data_priority" 
               foreignTable="priority"
               visibleFields="title"
               storeField="id"
            />
          </db:dataLabel>.jpg"> <db:dataLabel fieldName="priority"> <db:tableData name="data_priority" foreignTable="priority" visibleFields="title" storeField="id"/> </db:dataLabel></td>

        <td><%= currentRow_bugs.get("title") %>&nbsp;</td>

        <td><%= currentRow_bugs.get("indate") %>&nbsp;</td>
        <td><%= currentRow_bugs.get("outdate") %>&nbsp;</td>
        <td><a href="mailto:<%= currentRow_bugs.get("reporter") %>?subject=<%= currentRow_bugs.get("title") %>"><%= currentRow_bugs.get("reporter") %></a>&nbsp;</td>
        <td><img src="templates/sourceforge/images/<db:dataLabel fieldName="bugstate">
            <db:staticData name="data_bugstate">
              <db:staticDataItem key="0" value="open"/>
              <db:staticDataItem key="1" value="progress"/>            
              <db:staticDataItem key="2" value="closed"/>            
            </db:staticData>
          </db:dataLabel>.jpg"> 
          
          <db:dataLabel fieldName="bugstate"> 
          	<db:staticData name="data_bugstate_show"> 
          		<db:staticDataItem key="0" value="Open"/> 
          		<db:staticDataItem key="1" value="Progress"/> 
          		<db:staticDataItem key="2" value="Closed"/>
          	</db:staticData> 
          </db:dataLabel>
         </td>
    </tr>
    </db:body>
    <db:footer>
</table>
<p>
<center>
	<db:navNewButton caption="button.addnew" followUp="/bugs_single_addnew.jsp"/>
</center>
</p>
</db:footer></db:dbform>
</body>
</html>




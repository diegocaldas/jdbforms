<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/request.tld" prefix="request" %>


    <html>
      <head>
	<db:base/>
	<title></title>
	<link rel="stylesheet" href="dbforms.css">
      </head>

      <body bgcolor="#ccffff">

<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
<tr>
<td>
<img src="img/bt_logo.gif" border="0">
</td>
<td align="right">


<request:isuserinrole role="bug_admin"value="true">
<img src="img/pfeilchen.gif"/><a href="admin_menu.jsp">[Menu]</a>&nbsp;
</request:isuserinrole>


<img src="img/pfeilchen.gif"/>
<request:isuserinrole role="bug_admin"value="true">
<a href="logout.jsp">[Log out]</a>
</request:isuserinrole>
<request:isuserinrole role="bug_admin" value="false">
<a href="admin_menu.jsp">[Log in]</a>
</request:isuserinrole>

</td>
</tr>
</table>


      <% int i=0; %>			

      <db:dbform tableName="bugs" maxRows="*" followUp="/bugs_list.jsp" autoUpdate="false">

        <db:header>
          <db:errors/>			

          <table align="center" cellspacing=1 cellpadding=2 bgcolor="#ffffff" width="100%">
          <tr bgcolor="#ccffff" valign="top">



           <td>id</td>
             <td>category<br><db:sort fieldName="category" /></td>
             <td>priority<br><db:sort fieldName="priority" /></td>
             <td>title</td>
             
             <td>indate<br><db:sort fieldName="indate" /></td>
             <td>outdate</td>
             <td>reporter<br><db:sort fieldName="reporter" /></td>
             <td>bugstate<br><db:sort fieldName="bugstate" /></td>
     	
          </tr>
        </db:header>		

        <db:body allowNew="false">
          <% String bgcolor = (i % 2 == 0) ? "#FEE9CE" : "#FEE9AA"; i++; 
          
          if( "1".equals( (String) currentRow_bugs.get("priority")) ) {
            bgcolor = "#FF0000";
          }
          
          %>		
          <tr bgcolor="<%= bgcolor %>">
    <td><a href="<db:linkURL href="/bugs_single_readonly.jsp" tableName="bugs" position="<%= position_bugs %>"/>" ><%= currentRow_bugs.get("id") %></a>&nbsp;</td>
        <td>        
          <db:dataLabel fieldName="category">
            <db:tableData 
               name="data_category" 
               foreignTable="category"
               visibleFields="title"
               storeField="id"
            />
          </db:dataLabel>        
        </td>
        
        <td>
        
          <img src="img/<db:dataLabel fieldName="priority">                  
            <db:tableData 
               name="data_priority" 
               foreignTable="priority"
               visibleFields="title"
               storeField="id"
            />
          </db:dataLabel>.gif">       
        
          <db:dataLabel fieldName="priority">
            <db:tableData 
               name="data_priority" 
               foreignTable="priority"
               visibleFields="title"
               storeField="id"
            />
          </db:dataLabel>           
        </td>
        
        <td><%= currentRow_bugs.get("title") %>&nbsp;</td>
        
        <td><%= currentRow_bugs.get("indate") %>&nbsp;</td>
        <td><%= currentRow_bugs.get("outdate") %>&nbsp;</td>
        <td><%= rsv_bugs.getField("reporter") %>  <a href="mailto:<%= currentRow_bugs.get("reporter") %>"><%= currentRow_bugs.get("reporter") %></a>&nbsp;</td>
        <td>
                
                  
          <img src="img/<db:dataLabel fieldName="bugstate">
            <db:staticData name="data_bugstate">
              <db:staticDataItem key="0" value="open"/>
              <db:staticDataItem key="1" value="progress"/>            
              <db:staticDataItem key="2" value="closed"/>            
            </db:staticData>
          </db:dataLabel>.gif">                        

          <db:dataLabel fieldName="bugstate">
            <db:staticData name="data_bugstate">
              <db:staticDataItem key="0" value="open"/>
              <db:staticDataItem key="1" value="progress"/>            
              <db:staticDataItem key="2" value="closed"/>            
            </db:staticData>
          </db:dataLabel>     
        
        </td>
        
          </tr>			              		 				
        </db:body>
        <db:footer>
          </table>
          <p><center><db:navNewButton caption="--&gt; Report a bug!" followUp="/bugs_single_addnew.jsp" /></center></p>			
        </db:footer>
       </db:dbform>
      </body>
     </html>
   

	

<%@ page import = "org.dbforms.util.Util"						         %>
<%@ page import = "com.pow2.webgui.tabbedselector.TabbedSelectorUtil"    %>
<html>
	<head>
		<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
		<%@ taglib uri="/WEB-INF/pow2webgui.tld" prefix="gui" %>
		<db:base/>
	   <link   media="all"    href="../css/tabbedselector_amazon.css"  type="text/css" rel="stylesheet" >
	   <link   media="all"    href="../css/tabbedselector_backoffice.css"  type="text/css" rel="stylesheet" >
	</head>
    <% 
   		// get the tabbedSelector followUp parameter value:
	  	String selectedTab = TabbedSelectorUtil.getSelectedTabParamValue(request, "ts1");
    %>
    <body>
	    <h1>
		Shows howto use Luca Fossato's tabselector together with dbforms
	    </h1>
		  <!-- use gotoPrefix to move to the same record after changing the tab -->
	      <db:dbform  
          	    tableName="AUTHOR" 
	      		bypassNavigation="true" 
	         	followUp="/howto/howtoUseTabSelector.jsp" 
    	     	maxRows="1" 
        	 	autoUpdate="false" 
	         	gotoPrefix="fv_"
	      		>
         <db:header>
            <db:errors/>			
			<!-- To show first page if we have a new record -->
			<% 
			 if (Util.isNull(selectedTab) || (rsv_AUTHOR == null))
			   selectedTab = "tab1";
			%>
         </db:header>
         <db:body allowNew="true" >
            <table>
               <tr>
                     <td>ID</td>
			         <td>
			         	<db:label fieldName="AUTHOR_ID"/>&nbsp;
			         </td>
               </tr>
			</table>            
			<gui:tabbedSelector name        = "ts1"
			                    drawer      = "backoffice"
			                    color       = "#f0f0f0"
			                    followUp    = "<%= "howtoUseTabSelector.jsp?" + 
													((rsv_AUTHOR == null)?"":"fv_AUTHOR_ID=" + rsv_AUTHOR.getField("AUTHOR_ID"))
												%>"
			                    selectedTab = "<%=selectedTab  %>"
			                    width       ="400px" 
			>
			  <gui:tab 	drawer="backoffice" name="tab1" title="tab1">
		    	 <!-- This hidden input fields should remember the last used tab page if you move to the next record -->
		    	 <input 
 		    	 		type="hidden" 
 		    	  		name="<%=TabbedSelectorUtil.getSelectedTabParamName("ts1")%>"
 		    	  		value="tab1"
 		    	 />
                 <!-- contents of first tab --> 
				 <table>
	               <tr>
                    	<td>NAME</td>
                    	<td>
                    		<db:textField size="25" fieldName="NAME"/>
                    	</td>
	               </tr>
				</table>
			  </gui:tab>
		      <% if (rsv_AUTHOR != null) {%>
  			     <gui:tab 	drawer="backoffice" name="tab2" title="tab2">
	 		         <!-- This hidden input fields should remember the last used tab page if you move to the next record -->
		    		 <input 
 		    	 			type="hidden" 
 		    	  			name="<%=TabbedSelectorUtil.getSelectedTabParamName("ts1")%>"
 		    	  			value="tab2"
	 		    	 />
	                 <!-- contents of second tab --> 
					 <table>
	            		<tr>
	    	                <td>ORGANISATION</td>
    	    	            <td>
        		            	<db:textField size="25" fieldName="ORGANISATION"/>
            		        </td>
						</tr>
					</table>
				  </gui:tab>     
			  <% } %>
			</gui:tabbedSelector>
			<p/>
         </db:body>
	     <db:footer>
            <table>
               <tr>
                  <td>
                    <db:updateButton caption="update"/>
                    <db:deleteButton caption="delete"/>
	                <db:insertButton caption="insert" showAlways="false" />
                  </td>
               </tr>
               <tr>
                  <td>
                	 <db:navFirstButton caption="first"/>
            	     <db:navPrevButton  caption="previous"/>
        	         <db:navNextButton  caption="next"/>
    	             <db:navLastButton  caption="last"/>
	                 <db:navNewButton showAlwaysInFooter="false" caption="new"/>
                  </td>
               </tr>
			</table>
         </db:footer>
		</db:dbform>
   </body>
</html>

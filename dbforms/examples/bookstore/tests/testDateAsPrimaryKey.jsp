	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	</head>
    <body >
       <db:dbform autoUpdate="false"
                  followUp="/tests/testDateAsPrimaryKey.jsp"
                  maxRows="1"
                  tableName="TIMEPLAN"

       >
          <db:header>
	         <h1 align="center">Timestamp as Primary Key</h1>
          </db:header>
          <db:errors/>
          <db:body>
             <table class="fixed" align="center">
                <tr class="even">
                      <td style="width:300px">TIME</td>
			         <td style="width:100px"><db:textField fieldName="TIME" /></td>
                </tr>
                <tr class="odd">
                     <td style="width:300px">REMARK</td>
                     <td style="width:300px"><db:textField size="25" fieldName="REMARK"/></td>
                </tr>

                 <db:dbform autoUpdate="true"
	                  followUp="/tests/testDateAsPrimaryKey.jsp"
    	              maxRows="*"
        	          tableName="TIMEPLAN1"
            	      parentField="TIME"
                	  childField="TIME"
			       >
	    		      <db:header/>
		    	      <db:body>
        			        <tr class="even">
			                     <td style="width:300px">REMARK in subform</td>
            			         <td style="width:300px"><db:textField size="25" fieldName="REMARK"/></td>
			                </tr>
			          </db:body>
			          <db:footer>
            			 <tr class="button">
			                <td colspan="2" style="text-align:center">
            			      <db:updateButton style="width:100" caption="Save in subform"/>
			                  <db:deleteButton style="width:100" caption="Delete in subform"/>
            			      <db:insertButton style="width:100" caption="Insert in subform" showAlways="false" />
			                </td>
            			 </tr>
			             <tr class="button">
            			    <td colspan="2" style="text-align:center">
			                  <db:navFirstButton style="width:100" caption="<< First in subform"/>
            			      <db:navPrevButton  style="width:100" caption="< Previous in subform"/>
			                  <db:navNextButton  style="width:100" caption=">  Next in subform"/>
            			      <db:navLastButton  style="width:100" caption=">> Last in subform"/>
			                  <db:navNewButton   style="width:100" caption="New in subform"/>
            			   </td>
			             </tr>
			          </db:footer>
			       </db:dbform>
          </db:body>
          <db:footer>
             <tr class="button">
                <td colspan="2" style="text-align:center">
                  <db:updateButton style="width:100" caption="Save"/>
                  <db:deleteButton style="width:100" caption="Delete"/>
                  <db:insertButton style="width:100" caption="Insert" showAlways="false" />
                </td>
             </tr>
             <tr class="button">
                <td colspan="2" style="text-align:center">
                  <db:navFirstButton style="width:100" caption="<< First"/>
                  <db:navPrevButton  style="width:100" caption="< Previous"/>
                  <db:navNextButton  style="width:100" caption=">  Next"/>
                  <db:navLastButton  style="width:100" caption=">> Last"/>
                  <db:navNewButton   style="width:100" caption="New"/>
                  &nbsp;
                </td>
             </tr>
             </table>
          </db:footer>
       </db:dbform>

<%@ include file="httpSnooper.jsp" %>

    </body>
</html>
Prerequisites for the XSL-templates "generate_struts_lists.xsl",
"generate_struts_menu.xsl" and "generate_struts_singleforms.xsl" in this folder.

1.) dbforms-config.xml
----------------------

The xsl-stylesheets, contained in this folder,
need following elements in the dbforms-config.xml file.

* date-format
  Defines the format, date values should be displayed in,
  on the generated jsps. Also used for jcal.
  
* number-format
  Defines the format, number values should be displayed in,
  on the generated jsps.

* module-prefix
  DbForms-tags aren't module-aware like Struts-tags. 
  If you want to use DbForms inside a Struts-module, 
  you have to specify the module-path in this element. 
  If your DbForms-pages aren't inside a Struts-module 
  leave this element empty.

* url-prefix
  In order to work in Struts , the DbForms jsp pages have 
  to be delivered to the client by a kind of forward-action 
  which is part of this release (ForwardToUrlAction.java).
  This action forwards to the URL provided by the request-parameter 
  "forward". Links to generated DbForms-jsps will be prefixed 
  with this elements value.

**** Example: ****

<date-format>dd.MM.yyyy</date-format>
<number-format>#.#</number-format>
<module-prefix>/my-module</module-prefix>
<url-prefix>/showDbFormsPage.do?forward=/dbformspages/</url-prefix>



2.) ApplicationResources.properties (Struts)
--------------------------------------------

Following properties have to exist in Struts' ApplicationResources.properties

**** Example: ****

dbforms.navigation.link.list=[List]
dbforms.navigation.link.menu=[Menu]
dbforms.detailpage.buttontext.save=save
dbforms.detailpage.buttontext.first=<< first
dbforms.detailpage.buttontext.previous=< previous
dbforms.detailpage.buttontext.next=next >
dbforms.detailpage.buttontext.last=last >>
dbforms.detailpage.buttontext.delete=delete
dbforms.detailpage.buttontext.new=new
dbforms.listpage.buttontext.new=new entry

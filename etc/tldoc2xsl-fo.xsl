<?xml version="1.0"?>

<!-- Convert Tag Library Documentation into xsl-fo document                -->
<!-- First version 2002-10-15 (dikr@users.sourceforge.net):
        - not perfect, but already usable
-->

   <xsl:stylesheet version="1.0"
           xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
     xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:output indent="yes"/>

    <xsl:attribute-set name="thin-borders">
       <xsl:attribute name="border-before-width">0.2pt</xsl:attribute>
       <xsl:attribute name="border-after-width">0.2pt</xsl:attribute>
       <xsl:attribute name="border-start-width">0.2pt</xsl:attribute>
       <xsl:attribute name="border-end-width">0.2pt</xsl:attribute>
       <xsl:attribute name="border-before-style">solid</xsl:attribute>
       <xsl:attribute name="border-after-style">solid</xsl:attribute>
       <xsl:attribute name="border-start-style">solid</xsl:attribute>
       <xsl:attribute name="border-end-style">solid</xsl:attribute>
    </xsl:attribute-set>

    <xsl:template match="/" >                               
       <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
         <fo:layout-master-set>

         <fo:simple-page-master  page-height="11in" page-width="8.5in" 
            margin-left="22mm" margin-top="5mm" margin-bottom="10mm" margin-right="22mm"
            master-name="standard">
           <fo:region-body 
              region-name="xsl-region-body" 
              margin-top="10mm" margin-bottom="20mm" margin-left="0mm" margin-right="0mm"
              padding="6pt" />
           <fo:region-before 
              region-name="xsl-region-before" extent="10mm" 
               display-align="after" padding="6pt 0.7in"/>
           <fo:region-after 
              region-name="xsl-region-after" 
              extent="10mm" 
              padding="6pt"/>
         </fo:simple-page-master>

      <fo:simple-page-master master-name="twocolumns"
                  page-height="11in" page-width="8.5in"
            margin-left="22mm" margin-top="5mm" margin-bottom="10mm" margin-right="22mm" >
      <fo:region-body
                margin-top="20mm" margin-bottom="20mm"
                column-count="2" column-gap="10mm"/>
      <fo:region-before extent="10mm"/>
      <fo:region-after extent="10mm"/>
      </fo:simple-page-master>


        <fo:page-sequence-master master-reference="standard2"> 
          <fo:repeatable-page-master-alternatives>
            <fo:conditional-page-master-reference master-name="standard" odd-or-even="any"/>
          </fo:repeatable-page-master-alternatives>
        </fo:page-sequence-master>

         </fo:layout-master-set>

         <fo:page-sequence master-reference="standard">
           <fo:static-content flow-name="xsl-region-after">
             <fo:block text-align="center" font-size="10pt" 
                font-family="serif" line-height="14pt"> - <fo:page-number/> -
             </fo:block>
          </fo:static-content>

           <fo:static-content flow-name="xsl-region-before">
             <fo:block text-align="center" font-size="10pt"
                font-family="serif" line-height="14pt"> DbForms Tag Library 
             </fo:block>
          </fo:static-content>

           <fo:flow 
              flow-name="xsl-region-body">
              <xsl:apply-templates />                    
           </fo:flow>
         </fo:page-sequence>

         <fo:page-sequence master-reference="twocolumns">

           <fo:static-content flow-name="xsl-region-after">
             <fo:block text-align="center" font-size="10pt"
                font-family="serif" line-height="14pt"> - <fo:page-number/> -
             </fo:block>
          </fo:static-content>

           <fo:static-content flow-name="xsl-region-before">
             <fo:block text-align="center" font-size="10pt"
                font-family="serif" line-height="14pt"> DbForms Tag Library
             </fo:block>
          </fo:static-content>

           <fo:flow
              flow-name="xsl-region-body">
              <xsl:apply-templates  mode="tagindex"/>
              <xsl:apply-templates  mode="attrindex"/>              
           </fo:flow>
         </fo:page-sequence>


       </fo:root>
    </xsl:template> 

  <!-- =========================================================== -->
  <!-- template for taglib                                         -->
  <!-- =========================================================== -->
  <xsl:template match="taglib">

  <!-- Title -->

  <fo:block font-family="Times" font-size="18pt" font-weight="bold" space-before="18pt"
         space-after="12pt" text-align="center">

  <xsl:text>DbForms Construction Tags</xsl:text>
  </fo:block>

  <xsl:if test="tlibversion">
  <fo:block>
    Tag library version: <xsl:value-of select="tlibversion"/>
  </fo:block>
  </xsl:if>

  <!-- Create table with table of contents  -->

    <fo:table table-layout="fixed" padding="6pt" start-indent="0.5em" end-indent="0.5em">
    <fo:table-column column-width="45mm"  xsl:use-attribute-sets="thin-borders"/>
    <fo:table-column column-width="105mm" xsl:use-attribute-sets="thin-borders"/>
    <fo:table-column column-width="15mm"  xsl:use-attribute-sets="thin-borders"/>

    <fo:table-header>
         <fo:table-row xsl:use-attribute-sets="thin-borders">
           <fo:table-cell padding="0.3em" xsl:use-attribute-sets="thin-borders">
              <fo:block text-align="center" font-weight="bold">Tag name</fo:block>
           </fo:table-cell>
           <fo:table-cell padding="0.3em" xsl:use-attribute-sets="thin-borders">
              <fo:block text-align="center" font-weight="bold">Description</fo:block>
           </fo:table-cell>
           <fo:table-cell padding="0.3em" xsl:use-attribute-sets="thin-borders">
              <fo:block text-align="center" font-weight="bold">Page</fo:block>
           </fo:table-cell>
         </fo:table-row>
    </fo:table-header>

    <fo:table-body>
      <xsl:for-each select="tag">
        <fo:table-row xsl:use-attribute-sets="thin-borders">
          <fo:table-cell padding="0.2em" xsl:use-attribute-sets="thin-borders">
             <fo:block><xsl:value-of select="name"/></fo:block>
          </fo:table-cell>
          <fo:table-cell padding="0.2em" xsl:use-attribute-sets="thin-borders">
            <xsl:if test="summary">
              <fo:block font-size="11pt"><xsl:value-of select="summary"/></fo:block>
            </xsl:if>
          </fo:table-cell>
          <fo:table-cell padding="0.2em" xsl:use-attribute-sets="thin-borders">
            <fo:block>
              <fo:page-number-citation  ref-id="{generate-id(name)}">
              </fo:page-number-citation>
            </fo:block>
          </fo:table-cell>
       </fo:table-row>
     </xsl:for-each>
   </fo:table-body>

   <fo:table-footer></fo:table-footer>
   </fo:table>

   <fo:block break-before="page"/>

   <xsl:apply-templates select="tag"/>

  </xsl:template>

  <!-- =========================================================== -->
  <!-- template for tag                                            -->
  <!-- =========================================================== -->
  <xsl:template match="tag">
    <fo:block start-indent="0em" space-before="2em"> 

    <fo:block keep-with-next="always" id="{generate-id(name)}">
       <fo:inline font-weight="bold" font-size="14pt">
         <xsl:value-of select="name"/>
       </fo:inline>
       <xsl:if test="summary">
         <fo:inline font-style="italic">
         <xsl:text> - </xsl:text>
         <xsl:value-of select="summary"/> 
         </fo:inline>
       </xsl:if>
    </fo:block>

    <fo:block>
    <xsl:if test="info">
      <fo:block space-before="1mm"><xsl:apply-templates select="info"/></fo:block> 
    </xsl:if>

    <xsl:choose>
      <xsl:when test="count(attribute)>0">
      <fo:block>
         <fo:table  
                table-layout="fixed" space-before="0.5em" 
                table-omit-header-at-break="true">
         <fo:table-column column-width="45mm" />
         <fo:table-column column-width="125mm" />
         <fo:table-header>
           <fo:table-row >
             <fo:table-cell  padding="0.3em" xsl:use-attribute-sets="thin-borders">
                <fo:block font-weight="bold">Attribute Name</fo:block>
             </fo:table-cell>
             <fo:table-cell  padding="0.3em" xsl:use-attribute-sets="thin-borders">
                <fo:block font-weight="bold">Description</fo:block>
             </fo:table-cell>
          </fo:table-row>
         </fo:table-header>
         <fo:table-body>
           <xsl:apply-templates select="attribute"/>
         </fo:table-body>
         <fo:table-footer></fo:table-footer>
         </fo:table>
     </fo:block>
    </xsl:when>
      <xsl:otherwise><fo:block>This tag has no attributes.</fo:block></xsl:otherwise>
    </xsl:choose>

    </fo:block>
   </fo:block>

  </xsl:template>

  <!-- =========================================================== -->
  <!-- template for attribute                                      -->
  <!-- =========================================================== -->
  <!-- Process an individual tag attribute -->
  <xsl:template match="attribute">
    <fo:table-row xsl:use-attribute-sets="thin-borders" >
       <fo:table-cell padding="0.2em" xsl:use-attribute-sets="thin-borders">
           <xsl:if test="name"> 
              <fo:block font-size="10pt" id="{generate-id(name)}"><xsl:value-of select="name" />
              </fo:block>
           </xsl:if>
       </fo:table-cell>

       <fo:table-cell padding="0.2em" xsl:use-attribute-sets="thin-borders">
            <xsl:choose>
             <xsl:when test="info"><fo:block keep-together="always" font-size="10pt">
                  <xsl:apply-templates/></fo:block>
            </xsl:when>
             <xsl:otherwise><xsl:text disable-output-escaping="yes"> </xsl:text></xsl:otherwise>
            </xsl:choose>

            <xsl:if test="required = 'true'">
               <fo:block font-size="10pt">
                 <xsl:text disable-output-escaping="yes">[Required]</xsl:text>
               </fo:block>
            </xsl:if>

            <xsl:if test="rtexprvalue = 'true'">
               <fo:block font-size="10pt">
                 <xsl:text disable-output-escaping="yes">[RTExpValue]</xsl:text>
               </fo:block>
            </xsl:if>

        </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <!-- =========================================================== -->
  <!-- template for several html tags like p, ul,li etc            -->
  <!--   it is certainly better to include here a more general     -->
  <!--   free stylesheet to generate fo out of html, but for the   -->
  <!--   moment...                                                 -->
  <!-- =========================================================== -->

  <!-- p  -->
  <xsl:template match="p"><fo:block><xsl:apply-templates/></fo:block></xsl:template>

  <!-- span -->
  <xsl:template match="span"><fo:block><xsl:apply-templates/></fo:block></xsl:template>

  <!-- pre -->  
  <xsl:template match="pre">
    <fo:block  wrap-option="wrap" font-size="9pt" linefeed-treatment="preserve"
         font-family="monospace">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <!-- ul  -->
  <xsl:template match="ul">
        <fo:list-block space-before="0.25em" space-after="0.25em">
           <xsl:apply-templates/>
        </fo:list-block>
  </xsl:template>

  <!-- li  -->
  <xsl:template match="li">
    <fo:list-item space-after="0.5em">
        <fo:list-item-label start-indent="1em">
           <fo:block>&#x2022;</fo:block>
        </fo:list-item-label>
        <fo:list-item-body start-indent="2em">
           <fo:block><xsl:apply-templates/></fo:block>
        </fo:list-item-body>
    </fo:list-item>
  </xsl:template>

  <!-- br  -->
  <xsl:template match="br"><fo:block><xsl:apply-templates/></fo:block></xsl:template>

  <!-- b   -->
  <xsl:template match="b">
      <fo:inline font-weight="bold"><xsl:apply-templates/></fo:inline>
  </xsl:template>

  <!-- u   -->
  <xsl:template match="u">
      <fo:inline text-decoration="underline"><xsl:apply-templates/></fo:inline>
  </xsl:template>

  <!-- i   -->
  <xsl:template match="i">
      <fo:inline font-style="italic"><xsl:apply-templates/></fo:inline>
  </xsl:template>

  <!-- =========================================================== -->
  <!-- following tags are currently ignored:                       -->
  <!-- =========================================================== -->
   <xsl:template match="properties"/>
   <xsl:template match="jspversion"></xsl:template>
   <xsl:template match="shortname"></xsl:template>
   <xsl:template match="shortname"></xsl:template>
   <xsl:template match="display-name"></xsl:template>
   <xsl:template match="uri"></xsl:template>
   <xsl:template match="body/taglib/info"></xsl:template>
   <xsl:template match="attribute/name"/>
   <xsl:template match="attribute/required"/>
   <xsl:template match="attribute/rtexprvalue"/>

  <!-- =========================================================== -->
  <!-- template for taglib, mode "tagindex"                        -->
  <!-- =========================================================== -->
  <xsl:template match="taglib" mode="tagindex">

    <fo:block font-family="Times" font-size="15pt" font-weight="bold" space-before="18pt"
         space-after="12pt">
      <xsl:text>Tag Index</xsl:text>
    </fo:block>

    <fo:table table-layout="fixed">
    <fo:table-column column-width="72mm"/>
    <fo:table-column column-width="7mm"/>

    <fo:table-header/>

    <fo:table-body>

      <xsl:for-each select="tag" >
        <xsl:sort select="name"/>
          <fo:table-row >
              <fo:table-cell>
                 <fo:block  font-size="10pt">
                    <xsl:value-of select="name"/>
                    <fo:leader leader-pattern="dots"/>
                 </fo:block>
              </fo:table-cell>
              <fo:table-cell>
                 <fo:block  font-size="10pt" text-align="right">
                    <fo:page-number-citation ref-id="{generate-id(name)}"/>
                 </fo:block>
              </fo:table-cell>
          </fo:table-row >
     </xsl:for-each>

   </fo:table-body>

   <fo:table-footer/>

   </fo:table>

  </xsl:template>

  <!-- =========================================================== -->
  <!-- template for taglib, mode "attrindex"                       -->
  <!-- =========================================================== -->
  <xsl:template match="taglib" mode="attrindex">

    <fo:block font-family="Times" font-size="15pt" font-weight="bold" space-before="18pt"
         space-after="12pt">
      <xsl:text>Attribute Index</xsl:text>
    </fo:block>


    <fo:table table-layout="fixed">
    <fo:table-column column-width="72mm"/>
    <fo:table-column column-width="7mm"/>

    <fo:table-header/>

    <fo:table-body>

      <xsl:for-each select="tag/attribute" >
        <xsl:sort select="name"/>
        <xsl:sort select="../name"/>
          <fo:table-row >
              <fo:table-cell>
                 <fo:block font-size="10pt"><xsl:value-of select="name"/>
                           <xsl:text> (</xsl:text>
                           <xsl:value-of select="../name"/>)
                           <fo:leader leader-pattern="dots"/>
                 </fo:block>
              </fo:table-cell>
              <fo:table-cell>
                <fo:block text-align="right" font-size="10pt">
                  <fo:page-number-citation ref-id="{generate-id(name)}"/>
                </fo:block>
              </fo:table-cell>
          </fo:table-row >
     </xsl:for-each>

   </fo:table-body>

   <fo:table-footer/>

   </fo:table>

  </xsl:template>

</xsl:stylesheet>

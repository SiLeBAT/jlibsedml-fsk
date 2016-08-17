<?xml version="1.0" encoding="UTF-8"?>


<!-- 

Version 1.0.0
This style-sheet transforms SED-ML level 1 version 1 XML files to an HTML representation.

Features:
.........

1) Source attributes that are URLs are transformed to hyperlinks
2) Source attributes that are MIRIAM URNs to the BioModels database are converted to hyperlinks
3) KISAO algorithm identifiers are resolved into hyperlinks

4) Elements with notes are styled as 'hotspot' and notes appear in mouseovers.
5) If running this stylesheet through Java with jlibsedml in your classpath, MathML is converted to C-style text.
6) Internal hyperlinks between cross-references.


Options: 
........

There are two global parameters  that configure this stylesheet:

1) jlibsedml ( default = true ). Can be set as as the string 'true' or 'false'. If set to false, MathML
  will appear as unaltered XML; if true, will be converted to string format.
  
2) div  ( default = 'false' ). If set to true, will return content wrapped in a div element with id of 'sedml2html'
 for inclusion in another web page; if 'false' will generate a completed HTML page with default styling
   applied by an internal stylesheet. 

 -->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:math="http://www.w3.org/1998/Math/MathML"
	xmlns:sed="http://sed-ml.org/" xmlns:jmath="org.jlibsedml.Libsedml"
	xmlns:html="http://www.w3.org/1999/xhtml">
	<xsl:output method="html"/>

    <!-- The global parameters -->
    <xsl:param name="jlibsedml">true</xsl:param>
	<xsl:param name="div">false</xsl:param>
	
	<xsl:key name="modelid" match="sed:model[@id]" use="@id"/> 
    <xsl:key name="simid" match="sed:uniformTimeCourse[@id]" use="@id"/> 
    <xsl:key name="dgid" match="sed:dataGenerator[@id]" use="@id"/> 
    <xsl:key name="taskid" match="sed:task[@id]" use="@id"/>   
    <xsl:key name="allIds" match="sed:*[@id]" use="@id"></xsl:key>
	

     <!-- Starting template, choose between whole HTML or a div-->
	<xsl:template match="/">
	   
	    <xsl:choose>
	    	<xsl:when test="$div = 'false'">
	    	
	    		<html>
					<head>
						<style type="text/css">

	body {
	  background-color: #D5EDEC;
	  font-family: Georgia, serif;
	  font-size: 10pt;
	}
  
     h2.sedml,h3.sedml {
     color: navy;
     }
     
     table.sedml {
       border: 1;
       padding: 5px;
       border-collapse: collapse;
       width: 400;
       
     }
     th,td {
       border: 1px solid black;
       padding: 5px;
       text-align: center;
       font-size: 10pt;
     }
        
     .tblColTitle {
       background-color: #9acd32;
     }
     
    </style>
		
</head>
				<body style="background-color: #D5EDEC; font-family: Georgia, serif; font-size: 10pt;">
             
					<xsl:call-template name="notesJS"></xsl:call-template>
		
					<xsl:apply-templates select="sed:sedML"></xsl:apply-templates>
						 
				</body>
				</html>
	    	</xsl:when>
	    	
	    	<xsl:otherwise>
	    	  <div id="sedml2html">
	    	  	<xsl:call-template name="notesJS"></xsl:call-template>
		
					<xsl:apply-templates select="sed:sedML"></xsl:apply-templates>
	    	  </div>	    	
	    	</xsl:otherwise>
	    
	    
	    </xsl:choose>
	    <!-- Return error message HTML if is non-SEDML document -->
		<xsl:if test="count(sed:sedML) = 0">
	    	<p> 
	    	There was no root <b>sedML</b> element in the namespace
	    	http://sed-ml.org/ in this document. Although the document does not 
	    	have to be perfectly valid SED-ML, it must contain the <b>sedML</b>
	    	root element in the http://sed-ml.org/ namespace, to be read. 
	    	</p>
	    </xsl:if>

	</xsl:template>
	<!--  This JS runs the notes mouseover pop-up -->
	<xsl:template name="notesJS">
				<script type="text/javascript" language="javascript">
					<xsl:text>
				var tooltip=function(){
	var id = 'tt';
	var top = 3;
	var left = 3;
	var maxw = 300;
	var speed = 10;
	var timer = 20;
	var endalpha = 95;
	var alpha = 0;
	var tt,t,c,b,h;
	var ie = document.all ? true : false;
	return{
		show:function(v,w){
			if(tt == null){
				tt = document.createElement('div');
				tt.setAttribute('id',id);
				tt.setAttribute('style','position:absolute; display:block;')
				t = document.createElement('div');
				t.setAttribute('id',id + 'top');
				c = document.createElement('div');
				c.setAttribute('id',id + 'cont');
				c.setAttribute('style','display:block; padding:2px 12px 3px 7px; margin-left:5px; background:#666; color:#FFF');
				b = document.createElement('div');
				b.setAttribute('id',id + 'bot');
				tt.appendChild(t);
				tt.appendChild(c);
				tt.appendChild(b);
				document.body.appendChild(tt);
				tt.style.opacity = 0;
				tt.style.filter = 'alpha(opacity=0)';
				document.onmousemove = this.pos;
			}
			tt.style.display = 'block';
			c.innerHTML = v;
			tt.style.width = w ? w + 'px' : 'auto';
			if(!w &amp;&amp; ie){
				t.style.display = 'none';
				b.style.display = 'none';
				tt.style.width = tt.offsetWidth;
				t.style.display = 'block';
				b.style.display = 'block';
			}
			if(tt.offsetWidth &gt; maxw){tt.style.width = maxw + 'px'}
			h = parseInt(tt.offsetHeight) + top;
			clearInterval(tt.timer);
			tt.timer = setInterval(function(){tooltip.fade(1)},timer);
		},
		pos:function(e){
			var u = ie ? event.clientY + document.documentElement.scrollTop : e.pageY;
			var l = ie ? event.clientX + document.documentElement.scrollLeft : e.pageX;
			tt.style.top = (u - h) + 'px';
			tt.style.left = (l + left) + 'px';
		},
		fade:function(d){
			var a = alpha;
			if((a != endalpha &amp;&amp; d == 1) || (a != 0 &amp;&amp; d == -1)){
				var i = speed;
				if(endalpha - a &lt; speed &amp;&amp; d == 1){
					i = endalpha - a;
				}else if(alpha &lt; speed &amp;&amp; d == -1){
					i = a;
				}
				alpha = a + (i * d);
				tt.style.opacity = alpha * .01;
				tt.style.filter = 'alpha(opacity=' + alpha + ')';
			}else{
				clearInterval(tt.timer);
				if(d == -1){tt.style.display = 'none'}
			}
		},
		hide:function(){
			clearInterval(tt.timer);
			tt.timer = setInterval(function(){tooltip.fade(-1)},timer);
		}
	};
}();

</xsl:text>
</script>
	
	</xsl:template>

	<!-- Converts a MathML node to a string representation of that node. We 
		use local name to replace the namespace and prefix in a uniform way. -->
	<xsl:template match="*" mode="escape">
		<xsl:variable name="xmlnsDec">
			xmlns:math="http://www.w3.org/1998/Math/MathML"
		</xsl:variable>
		<xsl:text>&lt;math:</xsl:text>
		<xsl:choose>
			<xsl:when test="local-name() = 'math'">
				<xsl:value-of select="concat(local-name(), ' ',  $xmlnsDec)" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="local-name()" />
			</xsl:otherwise>
		</xsl:choose>

		<xsl:apply-templates mode="escape" select="@*" />
		<xsl:text>&gt;</xsl:text>
		<xsl:apply-templates mode="escape" />
		<xsl:text>&lt;/math:</xsl:text>
		<xsl:value-of select="local-name()" />
		<xsl:text>&gt;</xsl:text>
	</xsl:template>

    <!-- General way to rewrite escaped attributes -->
	<xsl:template match="@*" mode="escape">
		<xsl:text> </xsl:text>
		<xsl:value-of select="name()" />
		<xsl:text>="</xsl:text>
		<xsl:value-of select="." />
		<xsl:text>"</xsl:text>
	</xsl:template>
	
	<!-- General way to re-write HTML nodes and remove spurious xml-ns attributes -->
	<xsl:template match="*" mode="html">
		<xsl:text>&lt;</xsl:text>
		<xsl:value-of select="local-name()" />
		<xsl:apply-templates  select="@*" mode="escape" />
		<xsl:text>&gt;</xsl:text>
		<xsl:apply-templates mode="html" />
		<xsl:text>&lt;/</xsl:text>
		<xsl:value-of select="local-name()" />
		<xsl:text>&gt;</xsl:text>
	</xsl:template>

	


	<xsl:template match="sed:sedML">
		<!-- Just copy top-level notes, these don't need to be in mouseover as they
		 are 'outside' the main content. -->
	    <xsl:copy-of select="sed:notes/html:html/html:body/html:* |  sed:notes/html:*[not(local-name()='html') and not(local-name()='body')]"
		 ></xsl:copy-of>
		<xsl:apply-templates select="sed:listOfModels"></xsl:apply-templates>
		
		<xsl:apply-templates select="sed:listOfSimulations"></xsl:apply-templates>
		<xsl:apply-templates select="sed:listOfTasks"></xsl:apply-templates>
		<xsl:apply-templates select="sed:listOfDataGenerators"></xsl:apply-templates>
		<xsl:apply-templates select="sed:listOfOutputs"></xsl:apply-templates>
		
	</xsl:template>
	


	<!-- Sub-routine template to generate mouseover, if text has a notes subelement.
	 Takes a single parameter, 'text' which is the text to marked-up in 'hotspot' text 
	 if it has a note, else the text is just output. -->
	<xsl:template name="noteMouseOver">
	<xsl:param name="text"></xsl:param>
	<xsl:choose>
		<xsl:when test="count(sed:notes) &gt; 0">
		
		<xsl:variable name="htmlnotes">
		<xsl:apply-templates select="sed:notes/html:html/html:body/html:* |  sed:notes/html:*[not(local-name()='html') and not(local-name()='body')]"
		 mode="html"></xsl:apply-templates>	
	</xsl:variable>
	   <xsl:element name = "span">
	     <xsl:attribute name="style"> color:#900; padding-bottom:1px; border-bottom:1px dotted #900; cursor:pointer</xsl:attribute>
	     <xsl:attribute name="onmouseout">tooltip.hide();</xsl:attribute>
	     
	     <!-- We have to remove newlines from the HTML string that is passed into the
	     tooltip.show method, else continuation lines are ignored by the browser -->
	     <xsl:attribute name="onmouseover">tooltip.show(' <xsl:value-of select="translate($htmlnotes,'&#13;&#10;',' ')"></xsl:value-of>  ');</xsl:attribute>
	     <xsl:value-of select="$text"></xsl:value-of>
	   </xsl:element>			
		</xsl:when>
		
		<xsl:otherwise>
			<xsl:value-of select="$text"></xsl:value-of>
		</xsl:otherwise>
	</xsl:choose>
	
	</xsl:template>
	
	    <!-- This template inserts either a name or the id (if no name is available).  -->
		<xsl:template name="nameOrId">
					  <xsl:variable name="nameOrID">
			     <xsl:choose>
			     	<xsl:when test="string-length(@name) > 0">
			     	 <xsl:value-of select="@name" ></xsl:value-of>
			     	</xsl:when>
			     	<xsl:otherwise>
			     		 [<xsl:value-of select="@id" ></xsl:value-of>]
			     	</xsl:otherwise>
			     </xsl:choose>
			    </xsl:variable>
				<xsl:call-template name="noteMouseOver">
				 <xsl:with-param name="text"><xsl:value-of select="$nameOrID"></xsl:value-of></xsl:with-param>
				</xsl:call-template>
	
	</xsl:template>
	
	<!--  The following templates are pretty standard templates to
	 generate the element content. -->

	<xsl:template match="sed:plot2D">
		<tr>
			<td>
		
				<xsl:call-template name="nameOrId"></xsl:call-template>
				
			</td>
			<td>
				<table class="sedml">
					<tr class="tblColTitle">
						<th>Curve name [ID]</th>
						<td>X-axis</td>
						<td>X-axis log-scale?</td>
						<th>Y-axis</th>
						<td>Y-axis log-scale?</td>
					</tr>
					<xsl:for-each select="sed:listOfCurves/sed:curve">
						<tr>
							<td>
								<xsl:call-template name="nameOrId"></xsl:call-template>
							</td>
							<td>
								<a href="#{@xDataReference}">
										<xsl:for-each select="key('dgid', @xDataReference)[1]">
						<xsl:choose>
						 <xsl:when test="@name"><xsl:value-of select="@name"></xsl:value-of> </xsl:when>
						 <xsl:otherwise><xsl:value-of select="@id"></xsl:value-of></xsl:otherwise>
						</xsl:choose>
						</xsl:for-each>
								</a>
							</td>
							<td>
								<xsl:value-of select="@logX"></xsl:value-of>
							</td>

							<td>
								<a href="#{@yDataReference}">
												<xsl:for-each select="key('dgid', @yDataReference)[1]">
						<xsl:choose>
						 <xsl:when test="@name"><xsl:value-of select="@name"></xsl:value-of> </xsl:when>
						 <xsl:otherwise><xsl:value-of select="@id"></xsl:value-of></xsl:otherwise>
						</xsl:choose>
						</xsl:for-each>
								</a>
							</td>
							<td>
								<xsl:value-of select="@logY"></xsl:value-of>
							</td>
						</tr>

					</xsl:for-each>
				</table>

			</td>

		</tr>

	</xsl:template>
	<xsl:template match="plot3D">
		<tr>
			<td>
				<xsl:call-template name="nameOrId"></xsl:call-template>
			</td>
			<td>
				<table class="sedml">
					<tr class="tblColTitle">
						<th>Curve name [ID]</th>
						<th>X-axis</th>
						<th>X-axis log-scale?</th>
						<th>Y-axis</th>
						<th>Y-axis log-scale?</th>
						<th>Z-axis</th>
						<th>Z-axis log-scale?</th>
					</tr>
					<xsl:for-each select="sed:listOfSurfaces/sed:surface">
						<tr>
							<td>
						<xsl:call-template name="nameOrId"></xsl:call-template>
							</td>
							<td>
								<a href="#{@xDataReference}">
									<xsl:value-of select="@xDataReference"></xsl:value-of>
								</a>
							</td>
							<td>
								<xsl:value-of select="@logX"></xsl:value-of>
							</td>

							<td>
								<a href="#{@yDataReference}">
									<xsl:value-of select="@yDataReference"></xsl:value-of>
								</a>
							</td>
							<td>
								<xsl:value-of select="@logY"></xsl:value-of>
							</td>
							<td>
								<a href="#{@zDataReference}">
									<xsl:value-of select="@zDataReference"></xsl:value-of>
								</a>
							</td>
							<td>
								<xsl:value-of select="@logZ"></xsl:value-of>
							</td>
						</tr>

					</xsl:for-each>
				</table>

			</td>

		</tr>
	</xsl:template>
	<xsl:template match="sed:report">
		<tr>
			<td>
				<xsl:call-template name="nameOrId"></xsl:call-template>
			</td>
			<td>
				<table class="sedml">
					<tr class="tblColTitle">
						<th>Dataset name [ID]</th>
						<td>Datagenerator</td>
					</tr>
					<xsl:for-each select="sed:listOfDataSets/sed:dataSet">
						<tr>
							<td>
					<xsl:call-template name="nameOrId"></xsl:call-template>
							</td>
							<td>
								<a href="#{@dataReference}">
									<xsl:value-of select="@dataReference"></xsl:value-of>
								</a>
							</td>
						</tr>

					</xsl:for-each>
				</table>

			</td>

		</tr>
	</xsl:template>

	<xsl:template match="sed:listOfOutputs">
		<h3 class="sedml">Outputs</h3>
		<xsl:if test="count(sed:plot2D) > 0">
			<h4>2D Plots</h4>

			<table class="sedml">
				<tr class="tblColTitle">
					<th>Plot name [id]</th>
					<th>Curve</th>
				</tr>
				<xsl:apply-templates select="sed:plot2D"></xsl:apply-templates>
			</table>
		</xsl:if>
		<xsl:if test="count(sed:plot3D) > 0">
			<h4>3D Plots</h4>

			<table class="sedml">
				<tr class="tblColTitle">
					<th>Plot name [id]</th>
					<th>Curve</th>
				</tr>

				<xsl:apply-templates select="sed:plot3D"></xsl:apply-templates>


			</table>
		</xsl:if>

		<xsl:if test="count(sed:report) > 0">
			<h4>Reports</h4>
			<table class="sedml">
				<tr class="tblColTitle">
					<th>Report name [id]</th>
					<th>Curve</th>
				</tr>
				<xsl:apply-templates select="sed:report"></xsl:apply-templates>

			</table>
		</xsl:if>


	</xsl:template>

	<xsl:template match="sed:listOfDataGenerators">
		<h3 class="sedml">Data Generators</h3>
		<table class="sedml">
			<tr class="tblColTitle">
				<th>Name [Id]</th>
				<th>Referred variable</th>
				<th>Post-processing</th>
			</tr>
			<xsl:for-each select="sed:dataGenerator">
				<tr>
					<td>
						<a name="{@id}" />
						<xsl:call-template name="nameOrId"></xsl:call-template>
					</td>

					<td>
						<table class="sedml">
							<tr class="tblColTitle">
								<th>Variable Name [Id]</th>
								<th>Model reference (task)</th>
							</tr>
							<xsl:apply-templates select="sed:listOfVariables" />

						</table>
					</td>
					<td>
						<xsl:variable name="maths">
							<xsl:apply-templates select="math:math"
								mode="escape" />
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$jlibsedml='true'">
								<xsl:value-of select="jmath:MathMLXMLToText($maths)" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="translate($maths, '&#x9;','')" />
							</xsl:otherwise>
						</xsl:choose>

					</td>

				</tr>
			</xsl:for-each>
		</table>

	</xsl:template>

	<xsl:template match="sed:listOfVariables">

		<xsl:for-each select="sed:variable">
			<tr>
				<td>
			<xsl:call-template name="nameOrId"></xsl:call-template>
				</td>
				<td>
					<xsl:value-of select="@target"></xsl:value-of>
					<xsl:value-of select="@symbol"></xsl:value-of>
					<xsl:text>, task [</xsl:text>
					<a href="#{@taskReference}">
						<xsl:for-each select="key('taskid', @taskReference)">
						<xsl:choose>
						 <xsl:when test="@name"><xsl:value-of select="@name"></xsl:value-of> </xsl:when>
						 <xsl:otherwise><xsl:value-of select="@id"></xsl:value-of></xsl:otherwise>
						</xsl:choose>
						</xsl:for-each>
						
						
					</a>]
					
				</td>
			</tr>

		</xsl:for-each>

	</xsl:template>

	<xsl:template match="sed:listOfTasks">
		<h3 class="sedml">Tasks</h3>
		<table class="sedml">
			<tr class="tblColTitle">
				<th>Name [Id]</th>
				<th>Model reference</th>
				<th>Simulation reference</th>
			</tr>
			<xsl:for-each select="sed:task">
				<tr>
					<td>
						<a name="{@id}" />
						<xsl:call-template name="nameOrId"></xsl:call-template>
					</td>
					<td>
						<a href="#{@modelReference}">
						<xsl:for-each select="key('modelid', @modelReference)">
						<xsl:choose>
						 <xsl:when test="@name"><xsl:value-of select="@name"></xsl:value-of> </xsl:when>
						 <xsl:otherwise><xsl:value-of select="@id"></xsl:value-of></xsl:otherwise>
						</xsl:choose>
						</xsl:for-each>

						</a>
					</td>
					<td>
						<a href="#{@simulationReference}">
						<xsl:for-each select="key('simid', @simulationReference)">
						<xsl:choose>
						 <xsl:when test="@name"><xsl:value-of select="@name"></xsl:value-of> </xsl:when>
						 <xsl:otherwise><xsl:value-of select="@id"></xsl:value-of></xsl:otherwise>
						</xsl:choose>
						</xsl:for-each>
						</a>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
	<xsl:template match="sed:listOfSimulations">
		<h3 class="sedml">Simulations</h3>
		<table class="sedml">
			<tr class="tblColTitle">
				<th>Simulation Name [ID]</th>
				<th>Algorithm</th>
				<th>Start time</th>
				<th>Output start</th>
				<th>Output end</th>
				<th>Number of data points</th>
			</tr>
			<xsl:for-each select="sed:uniformTimeCourse">
				<tr>
					<td>
					
						<a name="{@id}" />
						<xsl:call-template name="nameOrId"></xsl:call-template>
					</td>
					<td>
						<xsl:apply-templates select="sed:algorithm"></xsl:apply-templates>
						
					</td>
					<td>
						<xsl:value-of select="@initialTime"></xsl:value-of>
					</td>
					<td>
						<xsl:value-of select="@outputStartTime"></xsl:value-of>
					</td>
					<td>
						<xsl:value-of select="@outputEndTime"></xsl:value-of>
					</td>
					<td>
						<xsl:value-of select="@numberOfPoints"></xsl:value-of>
					</td>
				</tr>
			</xsl:for-each>
		</table>

	</xsl:template>
	
	<xsl:template match="sed:algorithm">
	<xsl:choose>
		<!-- If is correct syntax, then format into hyperlink -->
		<xsl:when
			test="starts-with(@kisaoID, 'KISAO:')
							     and string-length(substring-after(@kisaoID,':')) =7">
			<xsl:variable name="kisaoID" select="substring-after(@kisaoID,':')" />
			<xsl:element name="a">
				<xsl:attribute name="href">
					<xsl:value-of
					select="concat('http://bioportal.bioontology.org/ontologies/46039/?p=terms&amp;conceptid=kisao%3AKISAO_',
					$kisaoID)">
					</xsl:value-of>
			   </xsl:attribute>
				<xsl:value-of select="@kisaoID"></xsl:value-of>
			</xsl:element>

		</xsl:when>
		<!-- Else just output the text -->
		<xsl:otherwise>
			<xsl:value-of select="@kisaoID"></xsl:value-of>
		</xsl:otherwise>
	</xsl:choose>

</xsl:template>

	<xsl:template match="sed:listOfModels">
		<h3 class="sedml">Models</h3>
		
			<xsl:for-each select="sed:model">
			    <h4>  <a name="{@id}"/>
			    <xsl:call-template name="nameOrId"></xsl:call-template>
					</h4>
			    <table class="sedml">
			      <tr class="tblColTitle">
			       <th>Attribute</th>
			       <th>Value</th>
			      </tr>
				
				<tr>
					<td>Name</td>
					<td>
						<xsl:value-of select="@name"></xsl:value-of>
					</td>
			    </tr>
			    <tr>
			    	<td>Source</td>
					<xsl:variable name="src">
						<xsl:value-of select="@source"></xsl:value-of>
					</xsl:variable>
					<xsl:choose>
					    <!-- Linkify an HTML source -->
						<xsl:when test="starts-with($src, 'http://')">
							<td>
								<a href="{$src}">
									<xsl:value-of select="$src"></xsl:value-of>
								</a>
							</td>
						</xsl:when>
 						<!-- special case for urnBIomodels DB -->
						<xsl:when test="starts-with($src, 'urn:miriam:biomodels.db:')">
							<td>
								<xsl:variable name="biomodelsURL">
									<xsl:value-of
										select="concat('http://www.ebi.ac.uk/biomodels-main/',
									                   substring-after($src, 'urn:miriam:biomodels.db:'))"></xsl:value-of>
								</xsl:variable>
								<a href="{$biomodelsURL}">
									<xsl:value-of select="$src"></xsl:value-of>
								</a>
							</td>
						</xsl:when>
						<!-- Else just output plain text. -->
						<xsl:otherwise>
							<td>
								<xsl:value-of select="$src"></xsl:value-of>
							</td>
						</xsl:otherwise>
					</xsl:choose>
					</tr>
					<tr>
					<td>Language</td>
					<td>
						<xsl:value-of select="@language"></xsl:value-of>
					</td>
					</tr>
					<tr>
					 <td>Changes</td>
					<td>
						<xsl:if test="count(sed:listOfChanges/sed:changeAttribute) > 0">
							<p> Attribute changes</p>
							<table class="sedml">
								<tr>
									<th> Variable</th>
									<th> New value</th>
								</tr>
								<xsl:for-each select="sed:listOfChanges/sed:changeAttribute">
									<tr>
										<td>
										 <xsl:call-template name="noteMouseOver">
				 								<xsl:with-param name="text">
													 <xsl:value-of select="@target">
				 							</xsl:value-of></xsl:with-param>
											</xsl:call-template>
											
										</td>
										<td>
											<xsl:value-of select="@newValue"></xsl:value-of>
										</td>

									</tr>


								</xsl:for-each>
							</table>
						</xsl:if>

					</td>
				</tr>
				</table>
				<!-- Leave a gap between models. -->
				<div style="margin-bottom: 20px;"></div>
			</xsl:for-each>
		
	</xsl:template>

</xsl:stylesheet>
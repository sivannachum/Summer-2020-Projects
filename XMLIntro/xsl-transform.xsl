<?xml version="1.0"?>
<!--This specifies the namespace for the style sheet and declares its prefix (xsl)-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--Specify that the output should be HTML!-->
<xsl:output method="html"/>

<!--
This defines the root template; every XSLT style sheet must have one.
This is the template that defines the set of rules to apply to the source XML document's root node,
i.e. it describes how to transform the content from the root node into some new output.
The forward slash matches the root node of the XML source document.
-->
  <xsl:template match="/">
    <html>
      <head><title>Wonders of the World</title></head>
      <body>
       <h1 align="center">Seven Wonders of the Ancient World</h1>
       <p align="center">
					<img src="herodotus.jpg" width="120" height="171"/>
       </p>
				<p>The famous Greek historian Herodotus wrote of seven great architectural achievements.  And although his writings did not survive, he planted seeds for what has become the list of the <strong>Seven Wonders of the Ancient World</strong>.</p>

      <!--List the great wonders professionally-->
      <p>These ancient wonders are 
        <!--Do this for every node that matches-->
        <xsl:for-each select="ancient_wonders/wonder/name[@language='English']">
          the 
          <xsl:value-of select="."/>
            <xsl:choose>
            <!--If this is the last wonder, put a period at the end-->
              <xsl:when test="position()=last()">.</xsl:when>
              <!--If it's the second-to-last wonder, add an and-->
              <xsl:when test="position()=last()-1">, and </xsl:when>
              <!--Otherwise, just put a comma-->
              <xsl:otherwise>, </xsl:otherwise>
            </xsl:choose>
          </xsl:for-each>
          
          <!--Count how many wonders were destroyed in the various ways of destruction, as well as how many are still standing.-->
          Of these wonders,
          <xsl:value-of select="count(ancient_wonders/wonder/history/how_destroyed[. = 'earthquake'])" /> were destroyed by earthquake, 
          <xsl:value-of select="count(//how_destroyed[. = 'fire'])" /> were destroyed by fire, and
          <xsl:value-of select="count(//wonder) - count(//how_destroyed)" /> is still standing.
        
        </p>
      
      <hr/>
      <h2 align="center">Overview</h2>
        <table border="1" align="center"><tr><th>Wonder Name</th><th>Location</th><th>Years<br />Standing</th><th>Height</th></tr>
          <xsl:apply-templates select="ancient_wonders/wonder">
            <!--Sort the nodes according to height, from tallest to lowest.-->
            <xsl:sort select="height" order="descending" data-type="number" />
          </xsl:apply-templates>
          
          <!--Calculate the average height of the ancient wonders-->
          <tr>
            <td valign="top" align="right" colspan="3">Average Height: </td>
            <td valign="top"><xsl:value-of select="format-number(sum(/ancient_wonders/wonder/height) div count(ancient_wonders/wonder/height[.!=0]),'##0.0')" /> ft <br/>
             (<em><xsl:value-of select="format-number((sum(/ancient_wonders/wonder/height) div count(ancient_wonders/wonder/height[.!=0])) * 0.3048, '##0.0')"/> m</em>) 
            </td>
          </tr>
        
        </table>
        
			<br/>
			<hr/>
			<h2 align="center">History</h2>
      <xsl:apply-templates select="//history">
        <!--Sort the nodes according to height, from tallest to lowest.-->
        <xsl:sort select="name" order="ascending" data-type="text" />
      </xsl:apply-templates>

      </body>
    </html>
  </xsl:template>
  
  <!--
  This is another template that we can use and call multiple times.
  We must explicitly call it for it to be used
  -->
  <!--
If the select expression matches a node, it outputs the string value of that node.
If the node has child elements, the output includes the text contained in those child elements as well
-->
  <xsl:template match="name[@language!='English']">
  <!--Select the current node-->
    (<em><xsl:value-of select="."/></em>)
  </xsl:template>  

<xsl:template match="wonder">
 <tr>
 
  <td valign="top">
    <a><xsl:attribute name="href">#<xsl:value-of select="name[@language='English']"/></xsl:attribute>
    <strong><xsl:value-of select="name[@language='English']"/></strong></a><br/>
    <xsl:apply-templates select="name[@language!='English']"/>
  </td>

  <td valign="top"><xsl:value-of select="location"/></td>

  <td valign="top">
  <!--Determine how long the ancient wonder stood / has been standing-->
  <xsl:choose>
  <!--If it's no longer standing:-->
    <xsl:when test="history/year_destroyed != 0">
     <xsl:choose>
     <!--
     All wonders were constructed in the BC years, if it was destroyed in those years,
     all we have to do is subtract to find out how many years it stood
     -->
      <xsl:when test="history/year_destroyed/@era = 'BC'">
       <xsl:value-of select="history/year_built - history/year_destroyed"/>
      </xsl:when>
      <!--
      If it was destroyed in AD, we have to add to find out how many years it stood
      We subtract 1 because there was no year 0
      -->
      <xsl:otherwise>
       <xsl:value-of select="history/year_built + history/year_destroyed - 1"/>
      </xsl:otherwise>
     </xsl:choose>
   </xsl:when>
   <!--If the wonder is still standing, we add the year it was built to the current year-->
    <xsl:otherwise>
     <xsl:value-of select="history/year_built + 2020 - 1"/>
    </xsl:otherwise>
   </xsl:choose>
  </td>

  <td valign="top">
   <xsl:choose>
    <xsl:when test="height != 0">
     <xsl:value-of select="height"/> ft <br/>
     <!--Convert the feet to meters-->
     <!--
     Use format number to limit the decimal places.
     # means include the number if it's nonzero, 0 means include the number no matter what;
     also indicating how many numbers to put after the decimal place; rounds automatically-->
     (<em><xsl:value-of select="format-number(height * 0.3048, '##0.0')"/> m</em>) 
    </xsl:when>
    <xsl:otherwise>
     unknown
    </xsl:otherwise>
   </xsl:choose>
  </td>

 </tr>
</xsl:template>

<xsl:template match="//history">
<!--Make a line across the page to separate sections-->
  <xsl:if test="position()!=1">
    <hr/>
  </xsl:if>
  <a><xsl:attribute name="name"><xsl:value-of select="../name[@language='English']"/></xsl:attribute></a>
  <!--
  Translates each letter to its uppercase counterpart.
  Can also translate characters generally to other characters through this operation.
  XML version 2.0 has operations specifically for sending letters to upper/lower case
  -->
  <center>
  <strong><xsl:value-of select="translate(../name[@language='English'],'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/></strong><br />
  </center>
  <p align="center"><xsl:apply-templates select="../main_image"/></p>
  <p>
  <!--Specifies we want the name in English-->
  The <xsl:value-of select="../name[@language='English']"/>
  <xsl:apply-templates select="../name[@language!='English']"/>
  <!--This is an if-else statement-->
  <xsl:choose> 
    <xsl:when test="year_destroyed != 0">
      was 
    </xsl:when>
    <xsl:otherwise>
      is 
    </xsl:otherwise>
  </xsl:choose>
  <!--
  Just get the country the wonder was in.
  Can also do substring-before and substring(txt, start, end)
  -->
  located in <xsl:value-of select="substring-after(../location, ',')"/>.
  <!--Select a child of the parent node, i.e. a sibling of the current node-->
  <!--Can use xsl:text to insert text inbetween xsl commands; necessary for spaces--> 
  The <xsl:value-of select="../name[@language='English']"/> was built in <xsl:value-of select="year_built"/> <xsl:text> </xsl:text>
  <xsl:value-of select="year_built/@era"/>, and it 
  <xsl:choose>
    <xsl:when test="year_destroyed != 0">
      was destroyed in <xsl:value-of select="year_destroyed"/> <xsl:text> </xsl:text> 
      <xsl:value-of select="year_destroyed/@era"/> by <xsl:value-of select="how_destroyed"/>. 
    </xsl:when>
    <xsl:otherwise>
      is still standing today.
    </xsl:otherwise>
  </xsl:choose>
  <xsl:value-of select="story"/>
  </p>
</xsl:template>

<!--Add the image of a wonder-->
<xsl:template match="main_image">

 <img>
  <xsl:attribute name="src"><xsl:value-of select="./@file"/></xsl:attribute>
  <!--
  Need to use "div" for division because / already has meaning with meaning;
  otherwise can use +, -, *.
  Need to round because this size needs to be an integer
  -->
  <!--Could also do "floor" do round down, or "round" to round to the nearest int-->
  <xsl:attribute name="width"><xsl:value-of select="ceiling(./@w div 2)"/></xsl:attribute>
  <xsl:attribute name="height"><xsl:value-of select="ceiling(./@h div 2)"/></xsl:attribute>
 </img>

</xsl:template>  
  
</xsl:stylesheet>

<!--
select="//*/@file"
// goes through all the descendants of the root node, * looks at all the elements at the given level, @file finds the file attributes of those elements
-->

<!--
Can do comparisons with &gt;, &lt;, = (just one!), !=
Can concatenate boolean expressions with "and" and "or"
Can use not() operation (this is not an expression!)
-->
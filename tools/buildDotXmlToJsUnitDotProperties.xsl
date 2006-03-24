<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="text"/>
    <xsl:template match="/project">
        <xsl:text>
            <![CDATA[
            #Using jsunit.properties is one way to specify the various properties used by the JsUnitServer.
            #It is deprecated in favor of using ant build files. See build.xml.
            #To use this file, rename it to "jsunit.properties". You need to provide values for the mandatory properties.
            #See the documentation at http://www.jsunit.net for more information.
            ]]>
        </xsl:text>
        <xsl:for-each select="property[@id!='']">
            <xsl:text>
            </xsl:text>
            <xsl:text>#</xsl:text>
            <xsl:value-of select="@description"/>
            <xsl:text>
            </xsl:text>
            <xsl:value-of select="@name"/>
            <xsl:text>=
            </xsl:text>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
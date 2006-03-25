<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="text"/>
    <xsl:template match="/">
        JsUnit Test Results
        The overall result was
        <i>
            <xsl:value-of select="@type"/>
        </i>
        .
        <br/>
    </xsl:template>
</xsl:stylesheet>
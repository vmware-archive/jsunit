<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>JsUnit Test Results</title>
                <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css"/>
            </head>
            <body>
                <h2>JsUnit Test Results</h2>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="testRunResult">
        <hr/>
        <xsl:apply-templates select="properties"/>
        The overall result was
        <i>
            <xsl:value-of select="@type"/>
        </i>
        .
        <br/>
        <xsl:apply-templates select="browserResult"/>
        <hr/>
        <br/>
    </xsl:template>

    <xsl:template match="properties">
        <h4>
            <xsl:value-of select="property[@name='hostname']/@value"/>
            -
            <xsl:value-of select="property[@name='ipAddress']/@value"/>
            (
            <xsl:value-of select="property[@name='os']/@value"/>
            )
        </h4>
        <br/>
    </xsl:template>

    <xsl:template match="browserResult">
        <b>Browser
            <xsl:value-of select="properties/property[@name='browserFileName']/@value"/>
        </b>
        (ID
        <xsl:value-of select="properties/property[@name='browserId']/@value"/>
        )
        <br/>
        URL
        <xsl:value-of select="properties/property[@name='url']/@value"/>
        <br/>
    </xsl:template>

</xsl:stylesheet>
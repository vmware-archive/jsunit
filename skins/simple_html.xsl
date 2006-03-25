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
        ,
        ID
        <xsl:value-of select="properties/property[@name='browserId']/@value"/>
        <xsl:if test="@time">
            (
            <xsl:value-of select="@time"/>
            seconds)
        </xsl:if>
        <xsl:if test="properties/property[@name='url']">
            on URL
            <i>
                <xsl:value-of select="properties/property[@name='url']/@value"/>
            </i>
        </xsl:if>
        <br/>
        <xsl:apply-templates select="testCases"/>
    </xsl:template>

    <xsl:template match="testCases">
        <xsl:value-of select="count(testCase)"/>
        tests run:
        <br/>
        <ul>
            <xsl:for-each select="testCase">
                <li>
                    <i>
                        <xsl:value-of select="@name"/>
                        (
                        <xsl:value-of select="@time"/>
                        seconds):
                    </i>
                    <xsl:if test="./failure">
                        <font color="red">FAILED</font>
                        <br/>
                        <pre>
                            <xsl:value-of select="./failure"/>
                        </pre>
                    </xsl:if>
                    <xsl:if test="./error">
                        <font color="red">ERROR</font>
                        <br/>
                        <pre>
                            <xsl:value-of select="./error"/>
                        </pre>
                    </xsl:if>
                    <xsl:if test="not(./failure) and not(./error)">SUCCESS
                        <br/>
                    </xsl:if>
                </li>
            </xsl:for-each>
        </ul>
    </xsl:template>

</xsl:stylesheet>
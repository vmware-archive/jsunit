<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>JsUnit Test Results</title>
                <link rel="stylesheet" type="text/css" href="/jsunit/css/jsUnitStyle.css"/>
            </head>
            <body>
                <h4>JsUnit Test Results</h4>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="testRunResult">
        <table>
            <tr>
                <td>
                    <xsl:if test="@type='SUCCESS'">
                        <img src="/jsunit/images/green_tick.gif" alt="SUCCESS" title="SUCCESS"/>
                    </xsl:if>
                    <xsl:if test="@type!='SUCCESS'">
                        <img src="/jsunit/images/red_x.gif">
                            <xsl:attribute name="alt" value="@type"/>
                            <xsl:attribute name="title" value="@type"/>
                        </img>
                    </xsl:if>
                </td>
                <td>
                    <xsl:apply-templates select="properties"/>
                </td>
                <td>
                    <xsl:text>(</xsl:text>
                    <xsl:value-of select="@type"/>
                    <xsl:text>)</xsl:text>
                </td>
            </tr>
        </table>
        <br/>
        <xsl:apply-templates select="browserResult"/>
        <hr/>
        <br/>
    </xsl:template>

    <xsl:template match="properties">
        <b>
            <xsl:value-of select="property[@name='hostname']/@value"/>
            <xsl:text>-</xsl:text>
            <xsl:value-of select="property[@name='ipAddress']/@value"/>
            <xsl:text> (</xsl:text>
            <xsl:value-of select="property[@name='os']/@value"/>
            <xsl:text>)</xsl:text>
        </b>
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
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
                    <xsl:text>&#160;-&#160;</xsl:text>
                    <font>
                        <xsl:attribute name="color">
                            <xsl:if test="@type='SUCCESS'">green</xsl:if>
                            <xsl:if test="@type!='SUCCESS'">red</xsl:if>
                        </xsl:attribute>
                        <xsl:value-of select="@type"/>
                    </font>
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
            <xsl:text>&#160;-&#160;</xsl:text>
            <xsl:value-of select="property[@name='ipAddress']/@value"/>
            <xsl:text>&#160;(</xsl:text>
            <xsl:value-of select="property[@name='os']/@value"/>
            <xsl:text>)</xsl:text>
        </b>
    </xsl:template>

    <xsl:template match="browserResult">
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
                    <b>Browser
                        <xsl:value-of select="properties/property[@name='browserFileName']/@value"/>
                    </b>
                    <xsl:text>,&#160;ID&#160;</xsl:text>
                    <xsl:value-of select="properties/property[@name='browserId']/@value"/>
                    <xsl:if test="@time">
                        <xsl:text>(</xsl:text>
                        <xsl:value-of select="@time"/>
                        <xsl:text>seconds)</xsl:text>
                    </xsl:if>
                    <xsl:if test="properties/property[@name='url']">
                        <xsl:text>&#160;on URL&#160;</xsl:text>
                        <xsl:value-of select="properties/property[@name='url']/@value"/>
                    </xsl:if>
                    <xsl:text>&#160;-&#160;</xsl:text>
                    <font>
                        <xsl:attribute name="color">
                            <xsl:if test="@type='SUCCESS'">green</xsl:if>
                            <xsl:if test="@type!='SUCCESS'">red</xsl:if>
                        </xsl:attribute>
                        <xsl:value-of select="@type"/>
                    </font>
                </td>
            </tr>
        </table>
        <br/>
        <xsl:apply-templates select="testCaseResults"/>
    </xsl:template>

    <xsl:template match="testCaseResults">
        <xsl:value-of select="count(testCaseResult)"/>
        <xsl:text>&#160;tests run:</xsl:text>
        <br/>
        <xsl:for-each select="testCaseResult">
            <xsl:apply-templates select="."/>
            <br/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="testCaseResult">
        <xsl:if test="@type='SUCCESS'">
            <img src="/jsunit/images/green_tick.gif" alt="SUCCESS" title="SUCCESS"/>
        </xsl:if>
        <xsl:if test="@type!='SUCCESS'">
            <img src="/jsunit/images/red_x.gif">
                <xsl:attribute name="alt" value="@type"/>
                <xsl:attribute name="title" value="@type"/>
            </img>
        </xsl:if>
        <xsl:value-of select="@name"/>
        <xsl:text>&#160;(</xsl:text>
        <xsl:value-of select="@time"/>
        <xsl:text>&#160;seconds)</xsl:text>
        <xsl:text>&#160;-&#160;</xsl:text>
        <font>
            <xsl:attribute name="color">
                <xsl:if test="@type='SUCCESS'">green</xsl:if>
                <xsl:if test="@type!='SUCCESS'">red</xsl:if>
            </xsl:attribute>
            <xsl:value-of select="@type"/>
        </font>
        <xsl:if test="@type='FAILURE'">
            <pre>
                <xsl:value-of select="failure"/>
            </pre>
        </xsl:if>
        <xsl:if test="@type='ERROR'">
            <pre>
                <xsl:value-of select="error"/>
            </pre>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
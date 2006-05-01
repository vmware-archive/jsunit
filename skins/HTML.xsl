<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>JsUnit Test Results</title>
                <link rel="stylesheet" type="text/css" href="/jsunit/css/jsUnitStyle.css"/>
                <script language="javascript" src="/jsunit/app/server/jsUnitServerUtilities.js"></script>
            </head>
            <body>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="testRunResult">
        <table>
            <tr>
                <td valign="top" rowspan="2" nowrap="nowrap">
                    <img>
                        <xsl:attribute name="src">
                            <xsl:value-of select="platform/logoPath"/>
                        </xsl:attribute>
                        <xsl:attribute name="alt">
                            <xsl:value-of select="platform/name"/>
                        </xsl:attribute>
                        <xsl:attribute name="title">
                            <xsl:value-of select="platform/name"/>
                        </xsl:attribute>
                    </img>
                </td>
                <td>
                    <xsl:apply-templates select="properties"/>
                </td>
            </tr>
            <tr>
                <td>
                    <xsl:apply-templates select="browserResult"/>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="properties">
        <xsl:value-of select="property[@name='hostname']/@value"/>
        <xsl:text>&#160;-&#160;</xsl:text>
        <xsl:value-of select="property[@name='ipAddress']/@value"/>
        <xsl:text>&#160;(</xsl:text>
        <xsl:value-of select="property[@name='os']/@value"/>
        <xsl:text>)</xsl:text>
    </xsl:template>

    <xsl:template match="browserResult">
        <table>
            <tr>
                <td colspan="2" nowrap="nowrap">
                    <table>
                        <tr>
                            <td valign="center">
                                <xsl:if test="@type='SUCCESS'">
                                    <img src="/jsunit/images/green_tick.gif" alt="SUCCESS" title="SUCCESS"/>
                                </xsl:if>
                                <xsl:if test="@type!='SUCCESS'">
                                    <img src="/jsunit/images/red_x.gif">
                                        <xsl:attribute name="alt">
                                            <xsl:value-of select="@type"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="title">
                                            <xsl:value-of select="@type"/>
                                        </xsl:attribute>
                                    </img>
                                </xsl:if>
                            </td>
                            <xsl:apply-templates select="browser"/>
                            <td valign="center">
                                <xsl:text>-&#160;</xsl:text>
                                <font>
                                    <xsl:attribute name="color">
                                        <xsl:if test="@type='SUCCESS'">green</xsl:if>
                                        <xsl:if test="@type!='SUCCESS'">red</xsl:if>
                                    </xsl:attribute>
                                    <xsl:value-of select="@type"/>
                                </font>
                            </td>
                            <xsl:if test="testCaseResults">
                                <td></td>
                                <td valign="center">
                                    <font size="-2">
                                        <a>
                                            <xsl:attribute name="href">
                                                <xsl:text>javascript:toggleVisibility("</xsl:text>
                                                <xsl:value-of
                                                        select="concat(../properties/property[@name='hostname']/@value, '_', browser/id)"/>
                                                <xsl:text>","</xsl:text>
                                                <xsl:value-of
                                                        select="concat(../properties/property[@name='hostname']/@value, '_', browser/id, '_link')"/>
                                                <xsl:text>", "show details")</xsl:text>
                                            </xsl:attribute>
                                            <div style="text-decoration:underline;valign:center">
                                                <xsl:attribute name="id">
                                                    <xsl:value-of
                                                            select="concat(../properties/property[@name='hostname']/@value, '_', browser/id, '_link')"/>
                                                </xsl:attribute>
                                                <xsl:text>show details</xsl:text>
                                            </div>
                                        </a>
                                    </font>
                                </td>
                            </xsl:if>
                        </tr>
                    </table>
                </td>
            </tr>
            <xsl:if test="testCaseResults">
                <tr>
                    <td>
                        <div style="display:none">
                            <xsl:attribute name="id">
                                <xsl:value-of
                                        select="concat(../properties/property[@name='hostname']/@value, '_', browser/id)"/>
                            </xsl:attribute>
                            <table>
                                <tr>
                                    <td colspan="2" nowrap="nowrap">
                                        <xsl:value-of select="count(./testCaseResults/testCaseResult)"/>
                                        <xsl:text>&#160;test(s) run</xsl:text>
                                        <xsl:if test="@time">
                                            <xsl:text>&#160;in&#160;</xsl:text>
                                            <xsl:value-of select="@time"/>
                                            <xsl:text>&#160;seconds</xsl:text>
                                        </xsl:if>
                                        <xsl:if test="properties/property[@name='url']">
                                            <xsl:text>&#160;on URL&#160;</xsl:text>
                                            <xsl:value-of select="properties/property[@name='url']/@value"/>
                                        </xsl:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td>
                                        <xsl:apply-templates select="testCaseResults"/>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
            </xsl:if>
        </table>
    </xsl:template>

    <xsl:template match="testCaseResults">
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
                <xsl:attribute name="alt">
                    <xsl:value-of select="@type"/>
                </xsl:attribute>
                <xsl:attribute name="title">
                    <xsl:value-of select="@type"/>
                </xsl:attribute>
            </img>
        </xsl:if>
        <xsl:value-of select="substring-after(@name, '.html:')"/>
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

    <xsl:template match="browser">
        <td valign="center">
            <img>
                <xsl:attribute name="src">
                    <xsl:value-of select="logoPath"/>
                </xsl:attribute>
                <xsl:attribute name="alt">
                    <xsl:value-of select="displayName"/>
                </xsl:attribute>
                <xsl:attribute name="title">
                    <xsl:value-of select="displayName"/>
                </xsl:attribute>
            </img>
        </td>
        <td valign="center">
            <xsl:value-of select="displayName"/>
        </td>
    </xsl:template>

</xsl:stylesheet>
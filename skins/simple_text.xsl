<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="text"/>
    <xsl:template match="/">
        JsUnit Test Results
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="testRunResult">

        <xsl:apply-templates select="properties"/>
        The overall result was
        <xsl:value-of select="@type"/>
        .
        <xsl:apply-templates select="browserResult"/>
    </xsl:template>

    <xsl:template match="properties">
        <xsl:value-of select="property[@name='hostname']/@value"/>
        -
        <xsl:value-of select="property[@name='ipAddress']/@value"/>
        (
        <xsl:value-of select="property[@name='os']/@value"/>
        )
    </xsl:template>

    <xsl:template match="browserResult">
        Browser
        <xsl:value-of select="properties/property[@name='browserFileName']/@value"/>
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
            <xsl:value-of select="properties/property[@name='url']/@value"/>
        </xsl:if>

        <xsl:apply-templates select="testCases"/>
    </xsl:template>

    <xsl:template match="testCases">
        <xsl:value-of select="count(testCase)"/>
        tests run:
        <xsl:for-each select="testCase">
            <xsl:value-of select="@name"/>
            (
            <xsl:value-of select="@time"/>
            seconds):
            <xsl:if test="./failure">
                <font color="red">FAILED</font>
                <xsl:value-of select="./failure"/>
            </xsl:if>
            <xsl:if test="./error">
                <font color="red">ERROR</font>
                <xsl:value-of select="./error"/>
            </xsl:if>
            <xsl:if test="not(./failure) and not(./error)">SUCCESS</xsl:if>

        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
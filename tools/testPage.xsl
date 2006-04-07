<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>Generated JsUnit Test Page</title>
                <script language="javascript" src="../app/jsUnitCore.js"></script>
                <script language="javascript">
                    <xsl:value-of select="."/>
                </script>
            </head>
            <body></body>
        </html>
    </xsl:template>
</xsl:stylesheet>
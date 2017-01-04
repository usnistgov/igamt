<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="MetaData">
        <xsl:element name="div">
            <xsl:attribute name="class">
                <xsl:text>metadata</xsl:text>
            </xsl:attribute>
            <xsl:element name="img">
                <xsl:attribute name="src">
                    <xsl:value-of select="$imageLogoSrc"/>
                </xsl:attribute>
            </xsl:element>

            <xsl:element name="p">
                <xsl:attribute name="style">
                    <xsl:text>font-size:250%;</xsl:text>
                </xsl:attribute>
                <xsl:element name="strong">
                    <xsl:value-of select="@Name"></xsl:value-of>
                </xsl:element>
            </xsl:element>
            <xsl:element name="br"/>
            <xsl:element name="p">
                <xsl:attribute name="style">
                    <xsl:text>font-size:200%;</xsl:text>
                </xsl:attribute>
                <xsl:value-of select="@Subtitle"></xsl:value-of>
            </xsl:element>
            <xsl:element name="br"/>
            <xsl:element name="p">
                <xsl:attribute name="style">
                    <xsl:text>font-size:100%;</xsl:text>
                </xsl:attribute>
                <xsl:value-of select="@Date"></xsl:value-of>
            </xsl:element>
            <xsl:element name="br"/>
            <xsl:element name="p">
                <xsl:attribute name="style">
                    <xsl:text>font-size:100%;</xsl:text>
                </xsl:attribute>
                <xsl:text>HL7 version </xsl:text>
                <xsl:value-of select="@HL7Version"></xsl:value-of>
            </xsl:element>
            <xsl:element name="br"/>
            <xsl:element name="p">
                <xsl:attribute name="style">
                    <xsl:text>font-size:80%;</xsl:text>
                </xsl:attribute>
                <xsl:text>Document version </xsl:text>
                <xsl:value-of select="@DocumentVersion"></xsl:value-of>
            </xsl:element>
            <xsl:element name="br"/>
            <xsl:element name="p">
                <xsl:attribute name="style">
                    <xsl:text>font-size:80%;</xsl:text>
                </xsl:attribute>
                <xsl:value-of select="@OrgName"></xsl:value-of>
            </xsl:element>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>

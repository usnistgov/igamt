<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="SegmentField">
        <xsl:param name="inlineConstraint"/>
        <xsl:param name="showConfLength"/>
        <xsl:element name="tr">
            <xsl:attribute name="class">
                <xsl:text>contentTr</xsl:text>
            </xsl:attribute>
            <xsl:element name="td">
                <xsl:value-of select="format-number(@Position, '0')" />
            </xsl:element>
            <xsl:element name="td">
                <xsl:value-of select="@Name" />
            </xsl:element>
            <xsl:element name="td">
                <xsl:value-of select="@Datatype" />
            </xsl:element>
            <xsl:element name="td">
                <xsl:value-of select="@Usage" />
            </xsl:element>
            <xsl:element name="td">
                <xsl:if test="(normalize-space(@Min)!='') and (normalize-space(@Max)!='') and ((normalize-space(@Min)!='0') or (normalize-space(@Max)!='0'))">
                    <xsl:value-of select="concat('[',@Min,'..',@Max,']')"/>
                </xsl:if>
            </xsl:element>
            <xsl:element name="td">
                <xsl:choose>
                    <xsl:when test="@complex='true'">
                        <xsl:attribute name="class"><xsl:text>greyCell</xsl:text></xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test="(normalize-space(@MinLength)!='') and (normalize-space(@MaxLength)!='') and ((normalize-space(@MinLength)!='0') or (normalize-space(@MaxLength)!='0'))">
                            <xsl:value-of select="concat('[',@MinLength,'..',@MaxLength,']')"/>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:element>
            <xsl:if test="$showConfLength='true'">
                <xsl:element name="td">
                    <xsl:if test="(normalize-space(@ConfLength)!='') and (normalize-space(@ConfLength)!='0')">
                        <xsl:value-of select="@ConfLength"/>
                    </xsl:if>
                </xsl:element>
            </xsl:if>
            <xsl:element name="td">
                <xsl:value-of select="@Binding" />
            </xsl:element>
            <xsl:element name="td">
                <xsl:value-of select="@Comment" />
            </xsl:element>
        </xsl:element>
        <xsl:if test="normalize-space($inlineConstraint) = 'true'">
            <xsl:if test="count(Constraint) &gt; 0">
                <!--TODO see with Olivier what's this doing here-->
                <xsl:apply-templates select="." mode="inlineSgt"/>
            </xsl:if>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>

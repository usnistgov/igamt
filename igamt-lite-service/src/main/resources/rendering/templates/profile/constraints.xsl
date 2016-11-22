<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="Constraints">
        <xsl:if test="count(./Constraint) &gt; 0">
            <xsl:element name="p">
                <xsl:element name="strong">
                    <xsl:element name="u">
                        <xsl:value-of
                                select="@title"/>
                    </xsl:element>
                </xsl:element>

                <xsl:element name="table">
                    <xsl:attribute name="class">
                        <xsl:text>contentTable</xsl:text>
                    </xsl:attribute>
                    <xsl:choose>
                        <xsl:when test="@Type='ConformanceStatement'">
                            <xsl:call-template name="conformanceStatementHeader"/>
                        </xsl:when>
                        <xsl:when test="@Type='ConditionPredicate'">
                            <xsl:call-template name="predicateHeader"/>
                        </xsl:when>
                    </xsl:choose>
                    <xsl:element name="tbody">
                        <xsl:for-each select="./Constraint">
                            <xsl:sort select="@Position" data-type="number"></xsl:sort>
                            <xsl:call-template name="ConstraintContent">
                                <xsl:with-param name="mode">
                                    <xsl:text>standalone</xsl:text>
                                </xsl:with-param>
                                <xsl:with-param name="type" select="@Type"/>
                            </xsl:call-template>
                        </xsl:for-each>
                    </xsl:element>
                </xsl:element>
            </xsl:element>
        </xsl:if>

    </xsl:template>

</xsl:stylesheet>

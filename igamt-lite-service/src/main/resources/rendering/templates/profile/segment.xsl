<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="/rendering/templates/profile/constraint.xsl"/>
    <xsl:import href="/rendering/templates/profile/segmentField.xsl"/>

    <xsl:template match="Segment" mode="toc">
        <xsl:element name="a">
            <xsl:attribute name="href">
                <xsl:value-of select="concat('#{',@id,'}')"/>
            </xsl:attribute>
            <xsl:element name="br"/>
            <xsl:value-of select="concat(@Name,' - ',@Description)"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="Segment">
        <xsl:param name="inlineConstraint"/>
        <!--xsl:value-of select="@Comment"/-->
        <xsl:if test="count(./Text[@Type='DefPreText']) &gt; 0">
            <xsl:call-template name="definitionText">
                <xsl:with-param name="type">
                    <xsl:text>pre</xsl:text>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:element name="p">
            <xsl:element name="table">
                <xsl:attribute name="class">
                    <xsl:text>contentTable</xsl:text>
                </xsl:attribute>
                <xsl:attribute name="summary">
                    <xsl:value-of select="@Description"></xsl:value-of>
                </xsl:attribute>
                <xsl:element name="col">
                    <xsl:attribute name="width">
                        <xsl:text>5%</xsl:text>
                    </xsl:attribute>
                </xsl:element>
                <xsl:element name="col">
                    <xsl:attribute name="width">
                        <xsl:text>15%</xsl:text>
                    </xsl:attribute>
                </xsl:element>
                <xsl:element name="col">
                    <xsl:attribute name="width">
                        <xsl:text>10%</xsl:text>
                    </xsl:attribute>
                </xsl:element>
                <xsl:element name="col">
                    <xsl:attribute name="width">
                        <xsl:text>10%</xsl:text>
                    </xsl:attribute>
                </xsl:element>
                <xsl:element name="col">
                    <xsl:attribute name="width">
                        <xsl:text>10%</xsl:text>
                    </xsl:attribute>
                </xsl:element>
                <xsl:element name="col">
                    <xsl:attribute name="width">
                        <xsl:text>10%</xsl:text>
                    </xsl:attribute>
                </xsl:element>
                <xsl:element name="col">
                    <xsl:attribute name="width">
                        <xsl:text>10%</xsl:text>
                    </xsl:attribute>
                </xsl:element>
                <xsl:if test="@ShowConfLength='true'">
                    <xsl:element name="col">
                        <xsl:attribute name="width">
                            <xsl:text>10%</xsl:text>
                        </xsl:attribute>
                    </xsl:element>
                    <xsl:element name="col">
                        <xsl:attribute name="width">
                            <xsl:text>20%</xsl:text>
                        </xsl:attribute>
                    </xsl:element>
                </xsl:if>
                <xsl:if test="@ShowConfLength='false'">
                    <xsl:element name="col">
                        <xsl:attribute name="width">
                            <xsl:text>30%</xsl:text>
                        </xsl:attribute>
                    </xsl:element>
                </xsl:if>
                <xsl:element name="thead">
                    <xsl:attribute name="class">
                        <xsl:text>contentThead</xsl:text>
                    </xsl:attribute>
                    <xsl:element name="tr">
                        <xsl:element name="th">
                            <xsl:text>Seq</xsl:text>
                        </xsl:element>
                        <xsl:if test="$columnDisplay.segment.name = 'true'">
                            <xsl:element name="th">
                                <xsl:text>Element name</xsl:text>
                            </xsl:element>
                        </xsl:if>
                        <xsl:if test="$columnDisplay.segment.dataType = 'true'">
                            <xsl:element name="th">
                                <xsl:text>Data type</xsl:text>
                            </xsl:element>
                        </xsl:if>
                        <xsl:if test="$columnDisplay.segment.usage = 'true'">
                            <xsl:element name="th">
                                <xsl:text>Usage</xsl:text>
                            </xsl:element>
                        </xsl:if>
                        <xsl:if test="$columnDisplay.segment.cardinality = 'true'">
                            <xsl:element name="th">
                                <xsl:text>Cardinality</xsl:text>
                            </xsl:element>
                        </xsl:if>
                        <xsl:if test="$columnDisplay.segment.length = 'true'">
                            <xsl:element name="th">
                                <xsl:text>Length</xsl:text>
                            </xsl:element>
                        </xsl:if>
                        <xsl:if test="$columnDisplay.segment.conformanceLength = 'true'">
                            <xsl:if test="@ShowConfLength='true'">
                                <xsl:element name="th">
                                    <xsl:text>ConfLength</xsl:text>
                                </xsl:element>
                            </xsl:if>
                        </xsl:if>
                        <xsl:if test="$columnDisplay.segment.valueSet = 'true'">
                            <xsl:element name="th">
                                <xsl:text>Value Set</xsl:text>
                            </xsl:element>
                        </xsl:if>
                        <xsl:if test="$columnDisplay.segment.comment = 'true'">
                            <xsl:element name="th">
                                <xsl:text>Comment</xsl:text>
                            </xsl:element>
                        </xsl:if>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="tbody">
                    <xsl:for-each select="Field">
                        <xsl:sort select="@Position" data-type="number"></xsl:sort>
                        <xsl:call-template name="SegmentField">
                            <xsl:with-param name="inlineConstraint" select="$inlineConstraint"/>
                            <xsl:with-param name="showConfLength" select="../@ShowConfLength"/>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:element>
            </xsl:element>
        </xsl:element>
        <xsl:if test="count(Constraint) &gt; 0">
            <xsl:if test="count(Constraint[@Type='cs']) &gt; 0">
                <xsl:element name="p">
                    <xsl:element name="strong">
                        <xsl:element name="u">
                            <xsl:text>Conformance Statements</xsl:text>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="table">
                    <xsl:attribute name="class">
                        <xsl:text>contentTable</xsl:text>
                    </xsl:attribute>
                    <xsl:call-template name="conformanceStatementHeader"/>
                    <xsl:element name="tbody">
                        <xsl:for-each select="./Constraint[@Type='cs']">
                            <xsl:sort select="@Position" data-type="number"></xsl:sort>
                            <xsl:call-template name="ConstraintContent">
                                <xsl:with-param name="mode">
                                    <xsl:text>standalone</xsl:text>
                                </xsl:with-param>
                                <xsl:with-param name="type">
                                    <xsl:text>cs</xsl:text>
                                </xsl:with-param>
                                <xsl:with-param name="displayPeriod">
                                    <xsl:text>false</xsl:text>
                                </xsl:with-param>
                            </xsl:call-template>
                        </xsl:for-each>
                    </xsl:element>
                </xsl:element>
            </xsl:if>
            <xsl:if test="count(Constraint[@Type='pre']) &gt; 0">
                <xsl:element name="p">
                    <xsl:element name="strong">
                        <xsl:element name="u">
                            <xsl:text>Conditional Predicates</xsl:text>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="table">
                    <xsl:attribute name="class">
                        <xsl:text>contentTable</xsl:text>
                    </xsl:attribute>
                    <xsl:call-template name="predicateHeader"/>
                    <xsl:element name="tbody">
                        <xsl:for-each select="./Constraint[@Type='pre']">
                            <xsl:sort select="@Position" data-type="number"></xsl:sort>
                            <xsl:call-template name="ConstraintContent">
                                <xsl:with-param name="mode">
                                    <xsl:text>standalone</xsl:text>
                                </xsl:with-param>
                                <xsl:with-param name="type">
                                    <xsl:text>pre</xsl:text>
                                </xsl:with-param>
                                <xsl:with-param name="displayPeriod">
                                    <xsl:text>false</xsl:text>
                                </xsl:with-param>
                            </xsl:call-template>
                        </xsl:for-each>
                    </xsl:element>
                </xsl:element>
            </xsl:if>
        </xsl:if>
        
        <xsl:apply-templates select="./coconstraints"></xsl:apply-templates>

        <xsl:if test="count(./Text[@Type='DefPostText']) &gt; 0">
            <xsl:element name="p">
                <xsl:call-template name="definitionText">
                    <xsl:with-param name="type">
                        <xsl:text>post</xsl:text>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:element>
        </xsl:if>

        <xsl:for-each select="Field">
            <xsl:sort select="@Position" data-type="number"></xsl:sort>
            <xsl:if test="count(Text) &gt; 0">
	            <xsl:element name="p">
                    <xsl:element name="b">
                        <xsl:value-of select="concat(../@Name,'-',./@Position,' : ',./@Name,' (',./@Datatype,')')" />
                    </xsl:element>
                    <xsl:value-of disable-output-escaping="yes" select="./Text[@Type='Text']" />
                </xsl:element>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="coconstraints">
        <xsl:element name="p">
            <xsl:element name="strong">
                <xsl:element name="u">
                    <xsl:text>Co-Constraints</xsl:text>
                </xsl:element>
            </xsl:element>
        </xsl:element>
        <xsl:copy-of select="table"/>
    </xsl:template>

</xsl:stylesheet>

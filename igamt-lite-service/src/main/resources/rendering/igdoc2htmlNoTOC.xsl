<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE xsl:stylesheet [
  <!ENTITY laquo "&#171;">
  <!ENTITY raquo "&#187;">
  <!ENTITY nbsp "&#160;">
  <!ENTITY Acirc "&#194;">
  <!ENTITY hellip "&#8230;">
]>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html"/>
	<xsl:param name="inlineConstraints" select="'false'"></xsl:param>
	<xsl:param name="includeTOC" select="'true'"></xsl:param>

	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<title>Implementation guide</title>
				<!--<link rel="stylesheet" href="froala_style.min.edited.css" type="text/css" ></link>-->
				<style type="text/css">
					
					/*!
					* froala_editor v2.2.1 (https://www.froala.com/wysiwyg-editor)
					*
					License https://froala.com/wysiwyg-editor/terms/
					* Copyright
					2014-2016 Froala Labs
					*/
					.clearfix::after{clear:both;display:block;content:""}.fr-view
					table{border:0;border-collapse:collapse;empty-cells:show;max-width:100%}.fr-view
					table.fr-dashed-borders td,.fr-view table.fr-dashed-borders
					th{border-style:dashed}.fr-view table.fr-alternate-rows tbody
					tr:nth-child(2n){background:#f5f5f5}.fr-view table td,.fr-view
					table th{border:1px solid #ddd}.fr-view table td:empty,.fr-view
					table th:empty{height:20px}.fr-view table
					td.fr-highlighted,.fr-view table th.fr-highlighted{border:1px
					double red}.fr-view table td.fr-thick,.fr-view table
					th.fr-thick{border-width:2px}.fr-view table
					th{background:#e6e6e6}.fr-view
					hr{clear:both;user-select:none;-o-user-select:none;-moz-user-select:none;-khtml-user-select:none;-webkit-user-select:none;-ms-user-select:none;page-break-after:always}.fr-view
					.fr-file{position:relative}.fr-view
					.fr-file::after{position:relative;content:"\1F4CE";font-weight:400}.fr-view
					pre{white-space:pre-wrap;word-wrap:break-word}.fr-view
					blockquote{border-left:solid 2px
					#5e35b1;margin-left:0;padding-left:5px;color:#5e35b1}.fr-view
					blockquote blockquote{border-color:#00bcd4;color:#00bcd4}.fr-view
					blockquote blockquote
					blockquote{border-color:#43a047;color:#43a047}.fr-view
					span.fr-emoticon{font-weight:400;font-family:"Apple Color
					Emoji","Segoe UI Emoji",NotoColorEmoji,"Segoe UI Symbol","Android
					Emoji",EmojiSymbols;display:inline;line-height:0}.fr-view
					span.fr-emoticon.fr-emoticon-img{font-size:inherit;height:1em;width:1em;min-height:20px;min-width:20px;display:inline-block;margin:-.2em
					.15em .2em;line-height:normal;vertical-align:middle}.fr-view
					.fr-text-gray{color:#AAA!important}.fr-view
					.fr-text-bordered{border-top:solid 1px #222;border-bottom:solid 1px
					#222;padding:10px 0}.fr-view
					.fr-text-spaced{letter-spacing:1px}.fr-view
					.fr-text-uppercase{text-transform:uppercase}.fr-view
					img{z-index:3;position:relative;overflow:auto;cursor:pointer}.fr-view
					img.fr-dib{margin:auto;display:block;float:none;vertical-align:top;margin-top:5px;margin-bottom:5px}.fr-view
					img.fr-dib.fr-fil{margin:5px auto 5px 0;left:5px}.fr-view
					img.fr-dib.fr-fir{margin:5px 0 5px auto;right:5px}.fr-view
					img.fr-dii{margin:auto;display:inline-block;float:none;margin-top:5px;margin-bottom:5px}.fr-view
					img.fr-dii.fr-fil{margin:5px 10px 5px
					0;left:5px;float:left}.fr-view img.fr-dii.fr-fir{margin:5px 0 5px
					10px;right:5px;float:right}.fr-view
					img.fr-rounded{border-radius:100%;-moz-border-radius:100%;-webkit-border-radius:100%;-moz-background-clip:padding;-webkit-background-clip:padding-box;background-clip:padding-box}.fr-view
					img.fr-bordered{border:solid 10px
					#CCC;-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box}.fr-view
					.fr-video{text-align:center;position:relative}.fr-view
					.fr-video>*{-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;max-width:100%;border:0}.fr-view
					.fr-video.fr-dvb{display:block;clear:both}.fr-view
					.fr-video.fr-dvb.fr-fvl{text-align:left}.fr-view
					.fr-video.fr-dvb.fr-fvr{text-align:right}.fr-view
					.fr-video.fr-dvi{display:inline-block}.fr-view
					.fr-video.fr-dvi.fr-fvl{float:left}.fr-view
					.fr-video.fr-dvi.fr-fvr{float:right}.fr-view
					a.fr-strong{font-weight:700}.fr-view
					a.fr-green{color:green}a.fr-view.fr-strong{font-weight:700}a.fr-view.fr-green{color:green}img.fr-view{z-index:3;position:relative;overflow:auto;cursor:pointer}img.fr-view.fr-dib{margin:auto;display:block;float:none;vertical-align:top;margin-top:5px;margin-bottom:5px}img.fr-view.fr-dib.fr-fil{margin:5px
					auto 5px 0;left:5px}img.fr-view.fr-dib.fr-fir{margin:5px 0 5px
					auto;right:5px}img.fr-view.fr-dii{margin:auto;display:inline-block;float:none;margin-top:5px;margin-bottom:5px}img.fr-view.fr-dii.fr-fil{margin:5px
					10px 5px 0;left:5px;float:left}img.fr-view.fr-dii.fr-fir{margin:5px
					0 5px
					10px;right:5px;float:right}img.fr-view.fr-rounded{border-radius:100%;-moz-border-radius:100%;-webkit-border-radius:100%;-moz-background-clip:padding;-webkit-background-clip:padding-box;background-clip:padding-box}img.fr-view.fr-bordered{border:solid
					10px
					#CCC;-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box}
				</style>
				<style type="text/css">
					body,
					html {
					font-family: 'Arial Narrow',
					sans-serif;
					}
				</style>
			</head>

			<body style="font-family: Arial Narrow, Arial, sans-serif;">
				<xsl:if test="$includeTOC='true'">
						<xsl:apply-templates select="ConformanceProfile/MetaData" />
						<hr></hr>
						<xsl:call-template name="dispSect" />
				</xsl:if>
			</body>
		</html>
	</xsl:template>

	<xsl:template name="dispInfoSect" mode="disp">
		<xsl:if test="name() = 'Section'">
				<u>
					<xsl:choose>
						<xsl:when test="@h &lt; 7 and normalize-space($includeTOC) = 'true'">
							<xsl:element name="{concat('h', @h)}">
								<xsl:value-of select="@title" />
							</xsl:element>
						</xsl:when>
						<xsl:when test="@h &gt; 7 and normalize-space($includeTOC) = 'true'">
							<xsl:element name="h6">
								<xsl:value-of select="@title" />
							</xsl:element>
						</xsl:when>
						<xsl:when test="@h &lt; 7 and normalize-space($includeTOC) = 'false'">
							<xsl:element name="{concat('h', @h)}">
								<xsl:value-of select="@title" />
							</xsl:element>
						</xsl:when>
						<xsl:when test="@h &gt; 7 and normalize-space($includeTOC) = 'true'">
							<xsl:element name="h6">
								<xsl:value-of select="@title" />
							</xsl:element>
						</xsl:when>
					</xsl:choose>
				</u>
			<br />
			<xsl:call-template name="dispSectContent" />
			<xsl:call-template name="dispProfileContent" />

		</xsl:if>
	</xsl:template>

	<xsl:template match="MetaData">
		<p style="text-align:center">
			<xsl:element name="img">
				<xsl:attribute name="src">
					<xsl:text>http://hit-2015.nist.gov/docs/hl7Logo.png</xsl:text>
				</xsl:attribute>
			</xsl:element>
		</p>
		<p style="font-size: 250%; text-align: center">
			<strong>
				<xsl:value-of select="@Name"></xsl:value-of>
			</strong>
		</p>
		<br></br>
		<p style="font-size: 200%; text-align: center">
			<xsl:value-of select="@Subtitle"></xsl:value-of>
		</p>
		<br></br>
		<p style="font-size: 100%; text-align: center">
			<xsl:value-of select="@Date"></xsl:value-of>
		</p>
		<br></br>
		<br></br>
		<p style="font-size: 100%; text-align: center">
			<xsl:text>HL7 version </xsl:text>
			<xsl:value-of select="@HL7Version"></xsl:value-of>
		</p>
		<br></br>
		<p style="font-size: 80%; text-align: center">
			<xsl:text>Document version </xsl:text>
			<xsl:value-of select="@DocumentVersion"></xsl:value-of>
		</p>
		<br></br>
		<p style="font-size: 80%; text-align: center">
			<xsl:value-of select="@OrgName"></xsl:value-of>
		</p>
		<p style="font-size: 62.5; text-align: center">
		</p>
	</xsl:template>

	<xsl:template name="dispSectContent">
		<xsl:value-of disable-output-escaping="yes" select="SectionContent" />
	</xsl:template>

	<xsl:template name="dispProfileContent">
		<xsl:choose>
			<xsl:when test="count(MessageDisplay) &gt; 0">
				<xsl:apply-templates select="MessageDisplay">
					<xsl:sort select="@position" data-type="number"></xsl:sort>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="count(Segment) &gt; 0">
				<xsl:apply-templates select="Segment">
					<xsl:sort select="@position" data-type="number"></xsl:sort>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="count(Datatype) &gt; 0">
				<xsl:apply-templates select="Datatype">
					<xsl:sort select="@position" data-type="number"></xsl:sort>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="count(ValueSetDefinition) &gt; 0">
				<xsl:apply-templates select="ValueSetDefinition">
					<xsl:sort select="@position" data-type="number"></xsl:sort>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="count(Constraints) &gt; 0">
				<xsl:apply-templates select="Constraints">
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
			</xsl:choose>
</xsl:template>

	<xsl:template name="dispSect">
		<!-- &#xA0; -->
		<xsl:call-template name="dispInfoSect" />
		<xsl:for-each select="*">
			<xsl:sort select="@position" data-type="number"></xsl:sort>
			<xsl:call-template name="dispSect" />
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="tocInfoSect">
		<xsl:if test="name() = 'Section'">
				<xsl:attribute name="class"><xsl:value-of select="concat('divh', @h)" /></xsl:attribute>
				<xsl:value-of select="@prefix" />
				-
				<xsl:value-of select="@title" />
				<xsl:choose>
					<xsl:when test="count(Section) &gt; 0">
						<xsl:element name="a">
							<xsl:attribute name="href">javascript:unhide('<xsl:value-of
								select="concat(@id, '_toc')" />', '<xsl:value-of
								select="concat(@id, '_btn')" />');</xsl:attribute>
							<xsl:element name="div">
								<xsl:attribute name="id"><xsl:value-of
									select="concat(@id, '_btn')" /></xsl:attribute>
								<xsl:attribute name="class">unhidden btn</xsl:attribute>
								[Hide]
							</xsl:element>
						</xsl:element>
					</xsl:when>
				</xsl:choose>
			<br></br>
		</xsl:if>
	</xsl:template>

	<xsl:template match="MessageDisplay">
		<xsl:value-of select="@Comment" />
		<p>
			<!-- <table width="100%" border="1" cellspacing="0" cellpadding="1"> -->
			<table style="color: #000000; background-color: transparent; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; 
				border-collapse: collapse; -fs-border-spacing-horizontal: 2px; -fs-border-spacing-vertical: 2px; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: relative; right: auto; src: none;table-layout: fixed; text-align: left; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: baseline; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: solid; border-right-style: solid; border-bottom-style: solid; border-left-style: solid; border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0in; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
				<thead style="background: #F0F0F0; color: #B21A1C; text-align: center">					
					<tr style="color: #B21A1C; background-color: #F0F0F0; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: center; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: top; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
						<th>
							Segment
						</th>
						<th>
							Flavor
						</th>
						<th>
							Element name
						</th>
						<th>
							Cardinality
						</th>
						<th>
							Usage
						</th>
						<th>
							Description/Comments
						</th>
					</tr>
				</thead>
				<tbody style="color: #000000; background-color: transparent; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row-group; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: left; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: middle; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
					<xsl:for-each select="Elt">
						<xsl:call-template name="elt">
							<xsl:with-param name="style"
								select="'background-color:white;text-decoration:normal'">
							</xsl:with-param>
						</xsl:call-template>

					</xsl:for-each>
				</tbody>
			</table>
		</p>
		<xsl:value-of disable-output-escaping="yes"
			select="./Text[@Type='UsageNote']" />
		<br></br>
	</xsl:template>

	<xsl:template name="elt">
		<xsl:param name="style" />
		<tr style="{$style}">
			<td style="width:10%">
				<xsl:value-of select="@Ref" />
			</td>
			<td style="width:20%">
				<xsl:value-of select="@Label" />
			</td>
			<td style="width:20%">
				<xsl:value-of select="@Description" />
			</td>
			<td style="width:10%">
				[
				<xsl:value-of select="@Min" />
				..
				<xsl:value-of select="@Max" />
				]
			</td>
			<td style="width:10%">
				<xsl:value-of select="@Usage" />
			</td>
			<td style="width:30%">
				<xsl:value-of select="@Comment" />
			</td>
		</tr>
	</xsl:template>


	<xsl:template match="Segment" mode="toc">
		<a href="#{@ID}">
			<br></br>
			<xsl:value-of select="@Name" />
			-
			<xsl:value-of select="@Description" />
		</a>
	</xsl:template>

	<xsl:template match="Segment">
		<xsl:value-of select="@Comment"></xsl:value-of>
		<xsl:if test="count(./Text[@Type='Text1']) &gt; 0">
			<p>
				<xsl:value-of disable-output-escaping="yes"
					select="./Text[@Type='Text1']" />
			</p>
		</xsl:if>
		<p>
			<table width="100%" border="1" cellspacing="0" cellpadding="1">
				<thead style="background: #F0F0F0; color: #B21A1C; text-align: center">
					<tr>
						<th>
							Seq
						</th>
						<th>
							Element name
						</th>
						<th>
							Data type
						</th>
						<th>
							Usage
						</th>
						<th>
							Cardinality
						</th>
						<th>
							Length
						</th>
						<th>
							Value Set
						</th>
						<th>
							Comment
						</th>
					</tr>
				</thead>
				<tbody  style="color: #000000; background-color: transparent; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row-group; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: left; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: middle; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
					<xsl:for-each select="Field">
						<xsl:sort select="@Position" data-type="number"></xsl:sort>
						<xsl:call-template name="field">
							<xsl:with-param name="style"
								select="'background-color:white;text-decoration:normal'">
							</xsl:with-param>
						</xsl:call-template>

					</xsl:for-each>
				</tbody>
			</table>
		</p>

		<xsl:choose>
			<xsl:when test="normalize-space($inlineConstraints) = 'false'">
				<xsl:if test="count(Field//Constraint[@Type='cs']) &gt; 0">
					<p>
						<strong>
							<u>Conformance statements</u>
						</strong>
						<table width="100%" border="1" cellspacing="0" cellpadding="1">
							<xsl:call-template name="csheader"></xsl:call-template>
							<tbody  style="color: #000000; background-color: transparent; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row-group; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: left; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: middle; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
								<xsl:for-each select="Field/Constraint[@Type='cs']">
									<xsl:sort select="@Position" data-type="number"></xsl:sort>
									<xsl:apply-templates select="." mode="standalone"></xsl:apply-templates>
								</xsl:for-each>
							</tbody>
						</table>
					</p>
				</xsl:if>
				<xsl:if test="count(Field//Constraint[@Type='pre']) &gt; 0">
					<p>
						<strong>
							<u>Conditional predicates</u>
						</strong>
						<table width="100%" border="1" cellspacing="0" cellpadding="1">
							<xsl:call-template name="preheader"></xsl:call-template>
							<tbody  style="color: #000000; background-color: transparent; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row-group; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: left; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: middle; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
								<xsl:for-each select="Field/Constraint[@Type='pre']">
									<xsl:sort select="@Position" data-type="number"></xsl:sort>
									<xsl:apply-templates select="." mode="standalone"></xsl:apply-templates>
								</xsl:for-each>
							</tbody>
						</table>
					</p>
				</xsl:if>
			</xsl:when>
		</xsl:choose>

		<xsl:value-of disable-output-escaping="yes"
			select="./Text[@Type='Text2']" />

		<xsl:for-each select="Field">
			<xsl:sort select="@Position" data-type="number"></xsl:sort>
			<xsl:if test="count(Text) &gt; 0">
				<p>
					<b>
						<xsl:value-of select="../@Name" />
						-
						<xsl:value-of select="./@Position" />
						&#160;
						<xsl:value-of select="./@Name" />
						(
						<xsl:value-of select="./@Datatype" />
						)
					</b>
					<xsl:value-of disable-output-escaping="yes"
						select="./Text[@Type='Text']" />
				</p>
			</xsl:if>
		</xsl:for-each>
		<br></br>
	</xsl:template>

	<xsl:template name="field">
		<xsl:param name="style" />
		<tr style="{$style}">
			<td>
				<xsl:value-of select="format-number(@Position, '0')" />
			</td>
			<td>
				<xsl:value-of select="@Name" />
			</td>
			<td>
				<xsl:value-of select="@Datatype" />
			</td>
			<td>
				<xsl:value-of select="@Usage" />
			</td>
			<td>
				[
				<xsl:value-of select="@Min" />
				..
				<xsl:value-of select="@Max" />
				]
			</td>
			<td>
				[
				<xsl:value-of select="@MinLength" />
				..
				<xsl:value-of select="@MaxLength" />
				]
			</td>
			<td>
				<xsl:value-of select="@Binding" />
			</td>
			<td>
				<xsl:value-of select="@Comment" />
			</td>
		</tr>
		<xsl:if test="normalize-space($inlineConstraints) = 'true'">
			<xsl:if test="count(Constraint) &gt; 0">
				<xsl:apply-templates select="." mode="inlineSgt"></xsl:apply-templates>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template match="Datatype" mode="toc">
		<a href="#{@ID}">
			<br></br>
			<xsl:value-of select="@Label" />
			-
			<xsl:value-of select="@Description" />
		</a>
	</xsl:template>

	<xsl:template match="Datatype">
		<xsl:value-of select="@Comment"></xsl:value-of>
		<xsl:if test="count(Text[@Type='UsageNote']) &gt; 0">
			<p>
				<xsl:value-of disable-output-escaping="yes"
					select="Text[@Type='UsageNote']" />
			</p>
		</xsl:if>
		<p>
			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<thead style="background: #F0F0F0; color: #B21A1C; text-align: center">
					<tr>
						<th>
							Seq
						</th>
						<th>
							Element name
						</th>
						<th>
							Conf length
						</th>
						<th>
							Data type
						</th>
						<th>
							Usage
						</th>
						<th>
							Length
						</th>
						<th>
							Value set
						</th>
						<th>
							Comment
						</th>
					</tr>
				</thead>
				<tbody  style="color: #000000; background-color: transparent; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row-group; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: left; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: middle; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
					<xsl:for-each select="Component">
						<xsl:sort select="@Position" data-type="number"></xsl:sort>
						<xsl:call-template name="component">
							<xsl:with-param name="style"
								select="'background-color:white;text-decoration:normal'">
							</xsl:with-param>
						</xsl:call-template>
					</xsl:for-each>
				</tbody>
			</table>
		</p>

		<xsl:if test="count(Component//Constraint) &gt; 0">
			<xsl:choose>
				<xsl:when test="normalize-space($inlineConstraints) = 'false'">
					<xsl:if test="count(Component//Constraint[@Type='cs']) &gt; 0">
						<p>
							<strong>
								<u>Conformance statements</u>
							</strong>
							<table width="100%" border="1" cellspacing="0" cellpadding="1">
								<xsl:call-template name="csheader"></xsl:call-template>
								<tbody  style="color: #000000; background-color: transparent; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row-group; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: left; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: middle; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
									<xsl:for-each select="Component/Constraint[@Type='cs']">
										<xsl:sort select="@Position" data-type="number"></xsl:sort>
										<xsl:apply-templates select="." mode="standalone"></xsl:apply-templates>
									</xsl:for-each>
								</tbody>
							</table>
						</p>
					</xsl:if>
					<xsl:if test="count(Component//Constraint[@Type='pre']) &gt; 0">
						<p>
							<strong>
								<u>Conditional predicates</u>
							</strong>
							<table width="100%" border="1" cellspacing="0" cellpadding="1">
								<xsl:call-template name="preheader"></xsl:call-template>
								<tbody  style="color: #000000; background-color: transparent; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row-group; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: left; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: middle; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
									<xsl:for-each select="Component/Constraint[@Type='pre']">
										<xsl:sort select="@Position" data-type="number"></xsl:sort>
										<xsl:apply-templates select="." mode="standalone"></xsl:apply-templates>
									</xsl:for-each>
								</tbody>
							</table>
						</p>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</xsl:if>

		<xsl:for-each select="Component">
			<xsl:sort select="@Position" data-type="number"></xsl:sort>
			<p>
				<xsl:value-of disable-output-escaping="yes"
					select="./Text[@Type='Text']" />
			</p>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="component">
		<xsl:param name="style" />
		<tr style="{$style}">

			<td style="width: 5%">
				<xsl:value-of select="format-number(@Position, '0')" />
			</td>
			<td style="width: 15%">
				<xsl:value-of select="@Name" />
			</td>
			<td style="width: 10%">
				<xsl:value-of select="@ConfLength" />
			</td>
			<td style="width: 10%">
				<xsl:value-of select="@Datatype" />
			</td>
			<td style="width: 10%">
				<xsl:value-of select="@Usage" />
			</td>
			<td style="width: 10%">
				[
				<xsl:value-of select="@MinLength" />
				..
				<xsl:value-of select="@MaxLength" />
				]
			</td>
			<td style="width: 10%">
				<xsl:value-of select="@Binding" />
			</td>
			<td style="width: 30%">
				<xsl:value-of select="@Comment" />
			</td>
		</tr>

		<xsl:if test="normalize-space($inlineConstraints) = 'true'">
			<xsl:if test="count(Constraint) &gt; 0">
				<xsl:apply-templates select="." mode="inlineDt"></xsl:apply-templates>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ValueSetDefinition" mode="toc">
			<br></br>
			<xsl:value-of select="@BindingIdentifier" />
			-
			<xsl:value-of select="@Description" />
		
	</xsl:template>

	<xsl:template match="ValueSetDefinition">
		<xsl:if test="@Stability != ''">
			<p><xsl:text>Stability: </xsl:text>
			<xsl:value-of select="@Stability"></xsl:value-of>
			</p>
		</xsl:if>
		<xsl:if test="@Extensibility != ''">
			<p><xsl:text>Extensibility: </xsl:text>
			<xsl:value-of select="@Extensibility"></xsl:value-of></p>
		</xsl:if>
		<xsl:if test="@ContentDefinition != ''">
			<p><xsl:text>Content Definition: </xsl:text>
			<xsl:value-of select="@ContentDefinition"></xsl:value-of></p>
		</xsl:if>
		<xsl:if test="@Oid != ''">
			<p><xsl:text>Oid: </xsl:text>
			<xsl:value-of select="@Oid"></xsl:value-of></p>
		</xsl:if>
		<table width="100%" border="1" cellspacing="0" cellpadding="0">
			<thead style="background: #F0F0F0; color: #B21A1C; text-align: center">
				<tr>
					<th>
						Value
					</th>
					<th>
						Code System
					</th>
					<th>
						Usage
					</th>
					<th>
						Description
					</th>
				</tr>
			</thead>
			<tbody  style="color: #000000; background-color: transparent; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row-group; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: left; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: middle; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
				<xsl:for-each select="ValueElement">
					<xsl:sort select="@Value" />
					<xsl:call-template name="ValueElement">
						<xsl:with-param name="style" select="'background-color: white;'">
						</xsl:with-param>
					</xsl:call-template>
				</xsl:for-each>
			</tbody>
		</table>
		<!-- <br></br> -->
	</xsl:template>

	<xsl:template name="ValueElement">
		<xsl:param name="style" />
		<tr style="{$style}">
			<td style="width: 15%" >
				<xsl:value-of select="@Value" />
			</td>
			<td style="width: 15%">
				<xsl:value-of select="@CodeSystem" />
			</td>
			<td style="width: 10%">
				<xsl:value-of select="@Usage" />
			</td>
			<td style="width: 60%">
				<xsl:value-of select="@Label" />
			</td>
		</tr>
	</xsl:template>


	<xsl:template match="Constraints">
		<xsl:if test="count(./Constraint) &gt; 0">
			<b>
				<xsl:value-of select="@title" />
			</b>
			<br></br>
			<p>
				<xsl:if test="./@Type='ConditionPredicate'">
					<table width="100%" border="1" cellspacing="0" cellpadding="0">
						<xsl:call-template name="csheader"></xsl:call-template>
						<tbody  style="color: #000000; background-color: transparent; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row-group; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: left; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: middle; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
							<xsl:for-each select="./Constraint">
								<xsl:sort select="@Position" data-type="number"></xsl:sort>
								<xsl:apply-templates select="." mode="standalone"></xsl:apply-templates>
							</xsl:for-each>
						</tbody>
					</table>
					<br></br>
				</xsl:if>
				<xsl:if test="./@Type='ConformanceStatement'">
					<table width="100%" border="1" cellspacing="0" cellpadding="0">
						<xsl:call-template name="preheader"></xsl:call-template>
						<tbody  style="color: #000000; background-color: transparent; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row-group; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: left; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: middle; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
							<xsl:for-each select="./Constraint">
								<xsl:sort select="@Position" data-type="number"></xsl:sort>
								<xsl:apply-templates select="." mode="standalone"></xsl:apply-templates>
							</xsl:for-each>
						</tbody>
					</table>
					<br />
				</xsl:if>
			</p>
		</xsl:if>
	</xsl:template>

	<!-- Conformance statement header -->
	<xsl:template name="csheader">
		<col style="width: 10%"> </col>
		<col style="width: 10%"> </col>
		<col style="width: 80%"> </col>
		<thead>
			<!--<tr style="background: #F0F0F0; color: #B21A1C; text-align: center">-->
			<tr style="color: #B21A1C; background-color: #F0F0F0; background-image: none; background-repeat: repeat; background-attachment: scroll; background-position: [0%, 0%]; background-size: [auto, auto]; border-collapse: collapse; -fs-border-spacing-horizontal: 0; -fs-border-spacing-vertical: 0; -fs-font-metric-src: none; -fs-keep-with-inline: auto; -fs-page-width: auto; -fs-page-height: auto; -fs-page-sequence: auto; -fs-pdf-font-embed: auto; -fs-pdf-font-encoding: Cp1252; -fs-page-orientation: auto; -fs-table-paginate: auto; -fs-text-decoration-extent: line; bottom: auto; caption-side: top; clear: none; ; content: normal; counter-increment: none; counter-reset: none; cursor: auto; ; display: table-row; empty-cells: show; float: none; font-style: normal; font-variant: normal; font-weight: normal; font-size: medium; line-height: normal; font-family: serif; -fs-table-cell-colspan: 1; -fs-table-cell-rowspan: 1; height: auto; left: auto; letter-spacing: normal; list-style-type: disc; list-style-position: outside; list-style-image: none; max-height: none; max-width: none; min-height: 0; min-width: 0; orphans: 2; ; ; ; overflow: visible; page: auto; page-break-after: auto; page-break-before: auto; page-break-inside: auto; position: static; ; right: auto; src: none; table-layout: auto; text-align: center; text-decoration: none; text-indent: 0; text-transform: none; top: auto; ; vertical-align: top; visibility: visible; white-space: normal; word-wrap: normal; widows: 2; width: auto; word-spacing: normal; z-index: auto; border-top-color: #000000; border-right-color: #000000; border-bottom-color: #000000; border-left-color: #000000; border-top-style: none; border-right-style: none; border-bottom-style: none; border-left-style: none; border-top-width: 2px; border-right-width: 2px; border-bottom-width: 2px; border-left-width: 2px; margin-top: 0; margin-right: 0; margin-bottom: 0; margin-left: 0; padding-top: 0; padding-right: 0; padding-bottom: 0; padding-left: 0;">
				<th>
					Id
				</th>
				<th>
					Location
				</th>
				<th>
					Description
				</th>
			</tr>
		</thead>

	</xsl:template>

	<!-- Predicate header -->
	<xsl:template name="preheader">
		<col style="width:10%"> </col>
		<col style="width:10%"> </col>
		<col style="width:80%"> </col>
		<thead style="background: #F0F0F0; color: #B21A1C; text-align: center">
			<tr>
				<th>
					Location
				</th>
				<th>
					Usage
				</th>
				<th>
					Description
				</th>
			</tr>
		</thead>
	</xsl:template>

	<!-- Parse constraint for inline mode segment -->
	<xsl:template match="Constraint" mode="inlineSgt">
		<xsl:variable name="precolspan" select="4"></xsl:variable>
		<xsl:variable name="cscolspan" select="4"></xsl:variable>
		<xsl:if test="./@Type='pre'">
			<tr style="background-color: #E8E8E8; text-decoration: normal">
				<td>
				</td>
				<td>
				</td>
				<td>
				</td>
				<td>
					<xsl:value-of select="@Usage" />
				</td>
				<xsl:element name="td">
					<xsl:attribute name="colspan">
				<xsl:value-of select="$precolspan" />
				</xsl:attribute>
					<xsl:value-of select="." />
				</xsl:element>
			</tr>
		</xsl:if>
		<xsl:if test="./@Type='cs'">
			<tr style="background-color: #E8E8E8; text-decoration: normal">
				<td>
				</td>
				<td>
				</td>
				<td>
				</td>
				<xsl:element name="td">
					<xsl:attribute name="colspan">
				<xsl:value-of select="number($cscolspan)" />	
				</xsl:attribute>
					<xsl:value-of select="@Id" />
					:
					<xsl:value-of select="." />
				</xsl:element>
			</tr>
		</xsl:if>
	</xsl:template>

	<!-- Parse constraint for inline mode datatype -->
	<xsl:template match="Constraint" mode="inlineDt">
		<xsl:variable name="precolspan" select="4"></xsl:variable>
		<xsl:variable name="cscolspan" select="5"></xsl:variable>
		<xsl:if test="./@Type='pre'">
			<tr style="background-color: #E8E8E8; text-decoration: normal">
				<td>
				</td>
				<td>
				</td>
				<td>
				</td>
				<td>
					<xsl:value-of select="@Usage" />
				</td>
				<xsl:element name="td">
					<xsl:attribute name="colspan">
				<xsl:value-of select="$precolspan" />
				</xsl:attribute>
					<xsl:value-of select="." />
				</xsl:element>
			</tr>
		</xsl:if>
		<xsl:if test="./@Type='cs'">
			<tr style="background-color: #E8E8E8; text-decoration: normal">
				<td>
				</td>
				<td>
				</td>
				<td>
				</td>
				<xsl:element name="td">
					<xsl:attribute name="colspan">
				<xsl:value-of select="number($cscolspan)" />	
				</xsl:attribute>
					<xsl:value-of select="@Id" />
					:
					<xsl:value-of select="." />
				</xsl:element>
			</tr>
		</xsl:if>
	</xsl:template>

	<!-- Parse constraint for standalone mode -->
	<xsl:template match="Constraint" mode="standalone">
		<xsl:if test="./@Type='pre'">
			<tr style="background-color: white; text-decoration: normal">
				<td>
					<xsl:value-of select="concat(@LocationName, @Location)" />
				</td>
				<td>
					<xsl:value-of select="@Usage" />
				</td>
				<td>
					<xsl:value-of select="." />
				</td>
			</tr>

		</xsl:if>
		<xsl:if test="./@Type='cs'">
			<tr style="background-color: white; text-decoration: normal">
				<td>
					<xsl:value-of select="@Id" />
				</td>
				<td>
					<xsl:value-of select="concat(@LocationName, @Location)" />
				</td>
				<td>
					<xsl:value-of select="." />
				</td>
			</tr>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>



package com.dangdang.unit.keeper.util.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class CDataXMLStreamWriter extends DelegatingXMLStreamWriter {

	private static final String defaultVersion = "1.0";

	private final String encoding;

	public CDataXMLStreamWriter(XMLStreamWriter writer, String encoding) {
		super(writer);
		this.encoding = encoding;
	}

	@Override
	public void writeStartDocument() throws XMLStreamException {
		super.writeStartDocument(encoding, defaultVersion);
	}

	@Override
	public void writeCharacters(String text) throws XMLStreamException {
		super.writeCData(text);
	}
}

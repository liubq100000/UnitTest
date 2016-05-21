
package com.dangdang.unit.keeper.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class UkOutputStream extends ByteArrayOutputStream {
	public UkOutputStream() {
		super(2048);
	}

	public UkOutputStream(int size) {
		super(size);
	}

	public synchronized InputStream asInputStream() {
		return new ByteArrayInputStream(buf, 0, count);
	}

	public synchronized byte[] getBuf() {
		return buf;
	}
}

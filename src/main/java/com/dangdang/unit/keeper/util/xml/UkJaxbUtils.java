
package com.dangdang.unit.keeper.util.xml;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UkJaxbUtils {

	private static final Logger logger = LoggerFactory.getLogger(UkJaxbUtils.class);

	private static Map<String, JAXBContext> contextMap = new HashMap<String, JAXBContext>();

	private static Lock lock = new ReentrantLock();

	@SuppressWarnings("rawtypes")
	private static JAXBContext getJAXBContext(Class clazz) throws JAXBException {

		JAXBContext context = contextMap.get(clazz.getName());
		if (context != null) {
			return context;
		}
		lock.lock();
		try {
			context = contextMap.get(clazz.getName());
			if (context == null) {
				context = JAXBContext.newInstance(clazz);
				contextMap.put(clazz.getName(), context);
			}
		}
		finally {
			lock.unlock();
		}
		return context;
	}

	private UkJaxbUtils() {
	}

	/**
	 * 将对象转换成xml字符串（使用默认编码）
	 * 
	 * @param obj 对象
	 * @return
	 */
	public static String object2Xml(Object obj) {
		return object2Xml(obj, DEFAULT_ENCODE);
	}

	/**
	 * 将对象转换成xml字符串
	 * 
	 * @param obj 对象
	 * @param encode 编码
	 * @return
	 */
	public static String object2Xml(Object obj, String encode) {
		StringWriter writer = null;
		try {
			JAXBContext ctx = getJAXBContext(obj.getClass());
			Marshaller marshaller = ctx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 是否格式化XML
			marshaller.setProperty(Marshaller.JAXB_ENCODING, isEmpty(encode) ? DEFAULT_ENCODE : encode); // xml编码
			writer = new StringWriter();
			marshaller.marshal(obj, writer);
			return writer.getBuffer().toString();
		}
		catch (JAXBException e) {
			logger.error("", e);
		}
		finally {
			if (writer != null) {
				try {
					writer.close();
				}
				catch (Exception e1) {
				}
			}
		}
		return null;
	}

	/**
	 * 将对象转换成带cdata标签的xml字符串（使用默认编码）
	 * 
	 * @param obj 对象
	 * @return
	 */
	public static String object2CDATAXml(Object obj) {
		CDataXMLStreamWriter cdataStreamWriter = null;
		try {
			JAXBContext ctx = getJAXBContext(obj.getClass());
			Marshaller marshaller = ctx.createMarshaller();
			XMLOutputFactory xof = XMLOutputFactory.newInstance();
			OutputStream outputStream = new ByteArrayOutputStream();
			XMLStreamWriter streamWriter = xof.createXMLStreamWriter(outputStream, DEFAULT_ENCODE);
			cdataStreamWriter = new CDataXMLStreamWriter(streamWriter, DEFAULT_ENCODE);
			marshaller.marshal(obj, cdataStreamWriter);
			return outputStream.toString();
		}
		catch (JAXBException e) {
			//			 e.printStackTrace();
		}
		catch (XMLStreamException e) {
			//			 e.printStackTrace();
		}
		finally {
			if (cdataStreamWriter != null) {
				try {
					cdataStreamWriter.close();
				}
				catch (Exception e) {
				}
			}
		}
		return null;
	}

	/**
	 * 将XML字符串根据其指定类型转换成相应的对象
	 * 
	 * @param xml xml字符串
	 * @param clazz 需要转换的java对象类型
	 * @return clazz
	 */
	public static Object xml2Object(String xml, Class<?> clazz) {
		try {
			JAXBContext ctx = getJAXBContext(clazz);
			Unmarshaller umr = ctx.createUnmarshaller();
			Object obj = umr.unmarshal(new StreamSource(new StringReader(xml)));
			return obj;
		}
		catch (JAXBException e) {
			//e.printStackTrace();
			try {
				return clazz.newInstance();
			}
			catch (InstantiationException e1) {
				//e1.printStackTrace();
			}
			catch (IllegalAccessException e1) {
				//e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将对象转换成xml字符串 提供泛型支持
	 * 
	 * @param obj 对象
	 * @param encode 编码
	 * @return
	 */
	public static String object2Xml(Object obj, String encode, Class<?>... clazz) {
		StringWriter writer = null;
		try {
			JAXBContext ctx = JAXBContext.newInstance(clazz);
			Marshaller marshaller = ctx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 是否格式化XML
			marshaller.setProperty(Marshaller.JAXB_ENCODING, isEmpty(encode) ? DEFAULT_ENCODE : encode); // xml编码
			writer = new StringWriter();
			marshaller.marshal(obj, writer);
			return writer.getBuffer().toString();
		}
		catch (JAXBException e) {
			return null;
		}
		finally {
			if (writer != null) {
				try {
					writer.close();
				}
				catch (Exception e) {
					//					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将XML字符串根据其指定类型转换成相应的对象
	 * 
	 * @param xml xml字符串
	 * @param clazz 需要转换的java对象类型
	 * @return clazz(异常返回空)
	 */
	public static <T> T xml2Object2(String xml, Class<T> clazz) {
		try {
			JAXBContext ctx = getJAXBContext(clazz);
			Unmarshaller umr = ctx.createUnmarshaller();
			@SuppressWarnings("unchecked")
			T t = (T) umr.unmarshal(new StreamSource(new StringReader(xml)));
			return t;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//默认字符集
	public static final String DEFAULT_ENCODE = "UTF-8";

	//判空
	public static boolean isEmpty(String value) {
		if (value == null || value.length() <= 0) {
			return true;
		}
		return false;
	}
}
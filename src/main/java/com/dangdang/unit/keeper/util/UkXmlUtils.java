
package com.dangdang.unit.keeper.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

/**
 * Xml文件生成
 * 
 * @author liubq
 */
public class UkXmlUtils {
	/**
	 * 格式化字符串
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public static String formatXmlStr(String xmlStr) throws Exception {
		if (xmlStr == null || xmlStr.length() <= 0) {
			return null;
		}
		java.io.Reader in = new StringReader(xmlStr);
		Document doc = (new SAXBuilder()).build(in);
		XMLOutputter xmlout = new XMLOutputter(formatXML());
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		xmlout.output(doc, bo);
		return bo.toString().trim();
	}

	/**
	 * 文件格式 格式化生成的xml文件，如果不进行格式化的话，生成的xml文件将会是很长的一行...
	 * 
	 * @return
	 */
	public static Format formatXML() {
		Format format = Format.getCompactFormat();
		format.setEncoding("utf-8");
		format.setIndent(" ");
		return format;
	}

	/**
	 * DOCUMENT格式化输出保存为XML
	 * 
	 * @param doc JDOM的Document
	 * @param filePath 输出文件路径
	 * @throws Exception
	 */
	public static void doc2XML(Document doc, String filePath) throws Exception {
		Format format = Format.getCompactFormat();
		format.setEncoding("UTF-8"); //设置XML文件的字符为UTF-8    
		format.setIndent("     ");//设置缩进     

		XMLOutputter outputter = new XMLOutputter(format);//定义输出 ,在元素后换行，每一层元素缩排四格     
		FileWriter writer = new FileWriter(filePath);//输出流    
		outputter.output(doc, writer);
		writer.close();
	}

	/**
	 * 字符串转换为DOCUMENT
	 * 
	 * @param xmlStr 字符串
	 * @return doc JDOM的Document
	 * @throws Exception
	 */
	public static Document string2Doc(String xmlStr) throws Exception {
		java.io.Reader in = new StringReader(xmlStr);
		Document doc = (new SAXBuilder()).build(in);
		return doc;
	}

	/**
	 * Document转换为字符串
	 * 
	 * @param xmlFilePath XML文件路径
	 * @return xmlStr 字符串
	 * @throws Exception
	 */
	public static String doc2String(Document doc) throws Exception {
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");// 设置xml文件的字符为UTF-8，解决中文问题    
		XMLOutputter xmlout = new XMLOutputter(format);
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		xmlout.output(doc, bo);
		return bo.toString();
	}

	/**
	 * XML文件转换为Document
	 * 
	 * @param xmlFilePath XML文件路径
	 * @return doc Document对象
	 * @throws Exception
	 */
	public static Document xml2Doc(String xmlFilePath) throws Exception {
		File file = new File(xmlFilePath);
		return (new SAXBuilder()).build(file);
	}

	/**
	 * XML格式字符串转换成JDOM的Document对象 方 法 名:string2xml
	 * 
	 * @author jeffsui
	 * @time 2014-11-14 下午2:25:40
	 * @param xmlStr
	 * @return
	 */
	public static Document xml2Doc2(String xmlStr) {
		SAXBuilder builder = new SAXBuilder();
		StringReader sr = new StringReader(xmlStr);
		InputSource is = new InputSource(sr);
		Document document = null;
		try {
			document = builder.build(is);
		}
		catch (JDOMException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}
}

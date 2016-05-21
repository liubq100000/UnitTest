
package com.dangdang.unit.keeper.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.dangdang.unit.keeper.dto.UnitAttribute;
import com.dangdang.unit.keeper.dto.UnitData;
import com.dangdang.unit.keeper.dto.UnitRow;
import com.dangdang.unit.keeper.dto.UnitTable;
import com.dangdang.unit.keeper.util.UkXmlUtils;

public class UkDataSetBuilder {

	/**
	 * 创建文件
	 * 
	 * @param data
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static File buildDatasetFile(UnitData data) throws Exception {
		XMLOutputter XMLOut = new XMLOutputter(UkXmlUtils.formatXML());
		File file = createTempFile(data.getStoreKey());
		XMLOut.output(buildDatasetDoc(data), new FileOutputStream(file));
		return file;
	}

	/**
	 * 创建文件流
	 * 
	 * @param data
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static InputStream buildDatasetStream(UnitData data) throws Exception {
		XMLOutputter XMLOut = new XMLOutputter(UkXmlUtils.formatXML());
		UkOutputStream out = new UkOutputStream();
		XMLOut.output(buildDatasetDoc(data), out);
		return out.asInputStream();
	}

	/**
	 * 转化为对象
	 * 
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public static String elementToXml(Element element) throws Exception {
		if (element == null) {
			return null;
		}
		Element root = new Element("root");
		//复制所有属性
		copyElement(element, root);
		//格式化输出
		String value = UkXmlUtils.formatXmlStr(UkXmlUtils.doc2String(new Document(root)));
		return value;

	}

	@SuppressWarnings("rawtypes")
	private static void copyElement(Element src, Element destParent) {
		Element destXml = new Element(src.getName());
		//属性
		List attributeXmls = src.getAttributes();
		Attribute newAttribute;
		for (Object attributeObj : attributeXmls) {
			Attribute attributeXml = (Attribute) attributeObj;
			newAttribute = new Attribute(attributeXml.getName(), attributeXml.getValue(), attributeXml.getNamespace());
			destXml.setAttribute(newAttribute);
		}
		//值
		destXml.setText(src.getText());
		List elementXmls = src.getChildren();
		for (Object elementObj : elementXmls) {
			Element childXml = (Element) elementObj;
			copyElement(childXml, destXml);
		}
		destParent.addContent(destXml);
	}

	/**
	 * 创建文件流
	 * 
	 * @param data
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static Document buildDatasetDoc(UnitData data) throws Exception {
		//创建根节点...  
		Element root = new Element("dataset");
		//将根节点添加到文档中...  
		Document doc = new Document(root);
		for (UnitTable table : data.getDataSet()) {
			List<UnitRow> rows = table.getRows();
			if (rows == null || rows.size() == 0) {
				//哈哈，宝权给你加点料
				Element element = new Element(table.getTableName());
				root.addContent(element);
			}
			else {
				for (UnitRow row : rows) {
					//哈哈，宝权给你加点料
					Element element = new Element(table.getTableName());
					for (UnitAttribute attribute : row.getAttributes()) {
						element.setAttribute(attribute.getName(), attribute.getValue());
					}
					root.addContent(element);
				}
			}

		}
		return doc;
	}

	//临时目录
	private static String TMP_ROOT_URL = System.getProperty("user.dir") + "/target/unit_test/";

	/**
	 * 临时目录赋值
	 * 
	 * @param tmpDir
	 */
	public static void setTempRootUrl(String tmpDir) {
		TMP_ROOT_URL = tmpDir;
		TMP_ROOT_URL = TMP_ROOT_URL.trim();
		if (!TMP_ROOT_URL.endsWith("/") && !TMP_ROOT_URL.endsWith("\\")) {
			TMP_ROOT_URL = TMP_ROOT_URL + File.separator;
		}
	}

	/**
	 * 临时目录赋值
	 * 
	 * @param tmpDir
	 */
	public static void setTempRootEndDir(String tmpDir) {
		TMP_ROOT_URL = TMP_ROOT_URL + tmpDir + File.separator;
	}

	/**
	 * 清理临时文件目录
	 */
	public static void clear() {
		File tempFile = new File(TMP_ROOT_URL + "temp");
		File[] subFiles = tempFile.listFiles();
		if (subFiles != null) {
			for (File file : subFiles) {
				file.delete();
			}
		}

	}

	/**
	 * 临时文件目录
	 * 
	 * @return
	 */
	public static File createTempFile(String name) {
		File tempFile = new File(TMP_ROOT_URL + "temp/" + name + System.currentTimeMillis() + ".xml");
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs();
		}
		return tempFile;
	}

}

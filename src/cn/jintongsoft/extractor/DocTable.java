package cn.jintongsoft.extractor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;


public class DocTable {

	private static final Logger logger = LogManager.getLogger(DocTable.class);

	public static void removeAll(String path) {
		XWPFDocument doc;
		try {
			doc = new XWPFDocument(new FileInputStream(path));
			List<XWPFTable> tables = doc.getTables();
			int num_table = tables.size();

			for (int i = 0; i < num_table; i++) {
				XWPFTable table = doc.getTables().get(0);
				int bodyElement = doc.getPosOfTable(table);
				doc.removeBodyElement(bodyElement);
			}

			doc.write(new FileOutputStream(path));
		} catch (FileNotFoundException e) {
			logger.error(path + "文件未找到");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public static Map<String, String> getTblQA(String type, InputStream inputStream)
			throws Exception, IOException, ExcelException {
		Map<String, String> result = null;
		if (type.equals("docx")) {
			XWPFDocument doc;
			doc = new XWPFDocument(inputStream);
			result = new LinkedHashMap<String, String>();
			List<XWPFTable> tables = doc.getTables();

			for (XWPFTable table : tables) {
				Map<String, String> content = getDocxTblContent(table);
				if (content != null) {
					result.putAll(content);
				}
			}
			inputStream.close();
		} else if (type.equals("doc")) {
			HWPFDocument doc;
			doc = new HWPFDocument(inputStream);
			result = new HashMap<String, String>();
			Range range = doc.getRange();
			TableIterator itr = new TableIterator(range);
			while (itr.hasNext()) {
				Table table = itr.next();
				Map<String, String> content = getDocTblContent(table);
				if (content != null) {
					result.putAll(content);
				}
			}
			inputStream.close();
		}
		if (result == null) {
			logger.debug("doc does not have table");
		}
		return result;
	}

	public static Map<String, String> getTblQA(String path) throws ExcelException {
		XWPFDocument doc;
		Map<String, String> result = null;
		try {
			doc = new XWPFDocument(new FileInputStream(path));
			result = new LinkedHashMap<String, String>();
			List<XWPFTable> tables = doc.getTables();

			for (XWPFTable table : tables) {
				Map<String, String> content = getDocxTblContent(table);
				if (content != null) {
					result.putAll(content);
				}
			}
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return result;
	}

	public static Map<String, String> getDocxTblContent(XWPFTable xwpfTable) throws ExcelException {
		int rowCount = xwpfTable.getNumberOfRows();
		String[][] table = new String[rowCount][];

		for (int i = 0; i < rowCount; i++) {
			int columnCount = 0;//xwpfTable.getRow(i).getTableCells().size();
			String[] row = new String[columnCount];
			for (int j = 0; j < columnCount; j++) {
				String cell = "";//xwpfTable.getRow(i).getCell(j).getText();
				if (cell.equals("")) {
					row[j] = null;
				} else {
					row[j] = cell;
				}
			}
			table[i] = row;
		}

		return ExcelHandler.extractTbl(table);
	}

	public static Map<String, String> getDocTblContent(Table hwpfTable) throws ExcelException {
		int rowCount = hwpfTable.numRows();
		String[][] table = new String[rowCount][];

		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			TableRow row = hwpfTable.getRow(rowIndex);
			String[] rowContent = new String[row.numCells()];
			for (int colIndex = 0; colIndex < row.numCells(); colIndex++) {
				TableCell cell = row.getCell(colIndex);
				String cellContent = cell.getParagraph(0).text();
				if (cellContent.equals("")) {
					rowContent[colIndex] = null;
				} else {
					rowContent[colIndex] = cellContent.substring(0, cellContent.length() - 1);
				}
			}
			table[rowIndex] = rowContent;
		}
		return ExcelHandler.extractTbl(table);
	}


}

package cn.jintongsoft.extractor;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.jintongsoft.nlp.parser.DNode;
import cn.jintongsoft.nlp.parser.Lexeme;
import cn.jintongsoft.nlp.parser.Token;
import cn.jintongsoft.nlp.parser.document.DocumentBean;

import cn.jintongsoft.utils.PoiUtils;

public class ExcelHandler {

	private static Logger logger = LogManager.getLogger(ExcelHandler.class);

	public static Map<String, String> getQAFromTxt(String filePath) {

		if (filePath.endsWith(".txt")) {
			byte[] b;
			String txt = null;
			try {
				b = PoiUtils.getText(new File(filePath), "txt", true);
				txt = new String(b, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}

			Lexeme lex = new Lexeme();
			lex.init(txt, Lex4Excel.COLLECTION);
			lex.run();
			ArrayList<Token> tokens = lex.getTokens();

		} else {
			return null;
		}
		return null;
	}

	public static Map<String, String> getQA(String type, InputStream inputstream) throws IOException, ExcelException {
		Sheet datatypeSheet = null;

		if (type.equalsIgnoreCase("xls")) {
			HSSFWorkbook workbook = new HSSFWorkbook(inputstream);
			datatypeSheet = workbook.getSheetAt(0);
		} else if (type.equalsIgnoreCase("xlsx")) {
			Workbook workbook = new XSSFWorkbook(inputstream);
			datatypeSheet = workbook.getSheetAt(0);

		} else {
			throw new ExcelException(1, "not a excel file");
		}
		int rowCount = datatypeSheet.getLastRowNum();
		String[][] table = new String[rowCount][];
		Iterator<Row> iterator = datatypeSheet.iterator();

		while (iterator.hasNext()) {

			Row currentRow = iterator.next();
			Iterator<Cell> cellIterator = currentRow.iterator();
			int rowNum = currentRow.getRowNum();
			int columnCount = currentRow.getLastCellNum();

			if (columnCount <= 0) {
				continue;
			}
			String[] row = new String[columnCount];

			while (cellIterator.hasNext()) {
				String content = null;
				Cell currentCell = cellIterator.next();

				int y = currentCell.getColumnIndex();

				if (currentCell.getCellTypeEnum() == CellType.STRING) {
					content = currentCell.getStringCellValue();
				} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
					content = String.valueOf(currentCell.getNumericCellValue());
					double doubleNum = Double.valueOf(content);
					if (Math.abs(doubleNum - (int) doubleNum) == 0) {
						content = String.valueOf((int) doubleNum);
					}
				}

				row[y] = content;
			}
			if (rowNum >= rowCount) {
				break;
			}
			table[rowNum] = row;
		}
		return extractTbl(table);
	}

	public static Map<String, String> getQA(String type, byte[] b) throws ExcelException, IOException {
		return getQA(type, new ByteArrayInputStream(b));
	}

	public static Map<String, String> getQA(String filePath) throws IOException, ExcelException {
		String type = null;
		if (filePath.endsWith(".xls")) {
			type = "xls";
		} else if (filePath.endsWith(".xlsx")) {
			type = "xlsx";
		} else {
			type = "";
		}
		try {
			return getQA(type, new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> extractTbl(String[][] rawTable) throws ExcelException {
		String[][] table = fixTable(rawTable);
		Map<String, String> result = null;
		Map<String, String> tirmResult = null;
		int type;
		type = checkTableType(table);
		logger.debug("type = " + type);

		if (type == 1) {
			result = generateType1(table);
		} else if (type == 2) {
			result = generateType2(table);
		} else {
			throw new ExcelException(2, "not a format table");
		}
		tirmResult = new LinkedHashMap<String, String>();
		for (Entry<String, String> en : result.entrySet()) {
			String value = en.getValue();
			String key = en.getKey();
			if (key == null || key.equals("")) {
				continue;
			}
			String newKey = tirmCNChar(key);
			tirmResult.put(newKey, value);

		}
		return tirmResult;
	}

	public static String tirmCNChar(String str) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ' ') {
				boolean frontEn = true;
				boolean behindEn = true;
				if (i > 0) {
					frontEn = str.substring(i - 1, i).matches("[a-zA-Z0-9]");
				}
				if (i < str.length() - 1) {
					behindEn = str.substring(i + 1, i + 2).matches("[a-zA-Z0-9]");
				}
				if (frontEn && behindEn) {
					result.append(str.charAt(i));
				}
			} else {
				result.append(str.charAt(i));
			}
		}
		return result.toString();
	}

	public static String[][] fixTable(String[][] table) {
		String[][] newT = null;
		// 去除无用行
		List<Integer> noEmptyLine = new ArrayList<Integer>();
		for (int i = 0; i < table.length; i++) {
			boolean empty = true;
			if (table[i] != null) {
				for (int j = 0; j < table[i].length; j++) {
					if (table[i][j] != null) {
						empty = false;
					}
				}
			}
			if (!empty) {

				noEmptyLine.add(i);
			}
		}
		int end = noEmptyLine.size();

		// 去除末尾null
		int end2 = Integer.MIN_VALUE;
		for (int i = 0; i < end; i++) {
			if (table[i] != null) {
				for (int j = table[i].length - 1; j >= 0; j--) {
					if (table[i][j] != null) {
						if (j > end2) {
							end2 = j;
						}
					}
				}
			}
		}

		newT = new String[end][end2 + 1];

		for (int i = 0; i < end; i++) {
			String[] row = table[noEmptyLine.get(i)];
			for (int j = 0; j < newT[i].length; j++) {
				if (j >= row.length) {
					newT[i][j] = null;
				} else {
					newT[i][j] = row[j];
				}
			}
		}
		return newT;
	}

	public static int checkTableType(String[][] table) throws ExcelException {
		// 检查表格的列是否一致
		for (int i = 0; i < table.length - 1; i++) {
			if (table[i].length != table[i + 1].length) {
				throw new ExcelException(2, "table column number disagree");
			}
		}

		int type = -1;
		// type1
		boolean isType1 = true;
		int needFullColumn = 2;
		for (int i = 0; i < table.length; i++) {

			// check multiple title
			boolean isTitle = true;
			if (table[i][0] != null) {
				for (int j = 1; j < table[0].length; j++) {
					if (table[i][j] != null) {
						isTitle = false;
					}
				}
			} else {
				for (int j = 1; j < table[0].length; j++) {
					if (table[i][j] == null) {
						isType1 = false;
					}
				}
			}

			if (!isType1) {
				break;
			}

			// check full column
			if (!isTitle) {
				for (int j = 1; j < table[0].length; j++) {
					if (table[i][j] == null && needFullColumn > 0) {
						logger.debug("not type1: mismatch at row " + i + " and column " + j + " " + table[i]);
						isType1 = false;
						break;
					}
				}
				if (!isType1) {
					break;
				}
				needFullColumn--;
			}

			if (isTitle) {
				needFullColumn = 2;
			}
		}

		if (isType1) {
			type = 1;
			return type;
		}

		// type2
		boolean isType2 = true;
		for (int i = 0; i < table.length; i++) {
			boolean isTitle = true;
			if (table[i][0] == null) {
				isTitle = false;
			}

			// check multiple title
			for (int j = 1; j < table[0].length; j++) {
				if (table[i][j] != null) {
					isTitle = false;
				}
			}

			// no null in the middle or at the end
			if (!isTitle) {
				int start = -1;
				for (int j = 0; j < table[0].length; j++) {
					if (table[i][j] == null && start >= 0) {
						logger.debug("not type2	: mismatch at row " + i + " and column " + j + " " + table[i]);
						isType2 = false;
						break;
					} else if (table[i][j] != null && start < 0) {
						start = j;
					}
				}
				if (start < 0 && !isType2) {
					break;
				}
			}
		}

		if (isType2) {
			type = 2;
			return type;
		}

		// type3

		if (type == -1) {
			throw new ExcelException(2, "can not handle such type of table");
		}
		return type;
	}

	public static Map<String, String> generateType1(String[][] table) throws ExcelException {
		Map<String, String> result = new LinkedHashMap<String, String>();
		List<Object[][]> result1 = new ArrayList<Object[][]>();
		List<Object[][]> result2 = new ArrayList<Object[][]>();

		if (table == null || table.length == 0) {
			logger.warn("Empty excel");
		} else {
			int row_num = table.length;
			int column_num = table[0].length;
			int x_offset = 0;

			// 判断首段是否为标题
			for (int i = 1; i < column_num; i++) {
				if (table[0][i] == null) {
					x_offset = 1;
					break;
				}
			}
			result.put(table[0][0], getHTMLContent(table, 20));
			if (x_offset == 1) {
				result.put(table[1][0], getHTMLContent(table, 20));
			}

			// 默认行为一例子
			if (table[x_offset][0] != null) {
				String[][] t = new String[2][];
				t[0] = new String[] {};
				t[1] = new String[] {};
			}

			// 以表格一行提问
			String[][] a1 = new String[2][column_num];
			String[] a01 = new String[column_num - 1];
			for (int j = 1; j < column_num; j++) {
				a01[j - 1] = table[x_offset][j];
				a1[0][j] = table[x_offset][j];
			}

			for (int i = x_offset + 1; i < row_num; i++) {
				String a00 = table[x_offset][0];
				String a10 = table[i][0];
				a1[0][0] = table[x_offset][0];
				a1[1][0] = table[i][0];

				String[] a11 = new String[column_num - 1];

				for (int j = 1; j < column_num; j++) {
					if (table[i][j] == null) {
						for (int k = i; k >= 0; k--) {
							if (table[k][j] != null) {
								a11[j - 1] = table[k][j];
								a1[1][j] = table[i][j];
								break;
							}
						}
					} else {
						a11[j - 1] = table[i][j];
						a1[1][j] = table[i][j];
					}
				}

				Object[][] a = new Object[2][];
				a[0] = new Object[] { a00, null };
				a[1] = new Object[] { a10, a11 };
				result1.add(a);

				if (a00 == null) {
					result.put(a10, getHTMLContent(a1, 20));
				} else {
					result.put(a10, getHTMLContent(a1, 20));
					result.put(a10 + " " + a00, getHTMLContent(a1, 20));
				}
			}
			result.putAll(combine2Questions(result1));

			// 以表格一列提问
			for (int i = 1; i < column_num; i++) {
				String q = table[x_offset][i];
				String[][] a = new String[row_num - x_offset][];
				a[0] = new String[] { table[x_offset][0], q };

				for (int j = x_offset + 1; j < row_num; j++) {
					if (table[j][i] == null) {
						for (int k = j; k >= 0; k--) {
							if (table[k][i] != null) {
								a[j - x_offset] = new String[] { table[j][0], table[k][i] };
								break;
							}
						}
					} else {
						a[j - x_offset] = new String[] { table[j][0], table[j][i] };
					}
				}
				result.put(q, getHTMLContent(a, 20));
			}

			// 以具体的单元格提问
			for (int i = x_offset + 1; i < row_num; i++) {
				for (int j = 1; j < column_num; j++) {
					if (table[i][j] == null) {
						for (int k = i; k >= 0; k--) {
							if (table[k][j] != null) {
								String[][] t = new String[2][];
								t[0] = new String[] { table[x_offset][0], table[x_offset][j] };
								t[1] = new String[] { table[i][0], table[k][j] };
								result2.add(t);

								result.put(table[i][0] + " " + table[x_offset][0] + " " + table[x_offset][j],
										table[k][j]);
								result.put(
										table[i][0] + " " + table[x_offset][0] + " " + table[x_offset][j] + ":"
												+ table[k][j],
										table[i][0] + " " + table[x_offset][0] + " " + table[x_offset][j] + ":"
												+ table[k][j]);
								break;
							}
						}
					} else {
						String[][] t = new String[2][];
						t[0] = new String[] { table[x_offset][0], table[x_offset][j] };
						t[1] = new String[] { table[i][0], table[i][j] };
						result2.add(t);

						result.put(table[i][0] + " " + table[x_offset][0] + " " + table[x_offset][j], table[i][j]);
						result.put(
								table[i][0] + " " + table[x_offset][0] + " " + table[x_offset][j] + ":" + table[i][j],
								table[i][0] + " " + table[x_offset][0] + " " + table[x_offset][j] + ":" + table[i][j]);

					}
				}
			}
		}
		result.putAll(combine2Questions(result2));
		return result;
	}

	public static Map<String, String> generateType2(String[][] table) throws ExcelException {
		Map<String, String> result = new LinkedHashMap<String, String>();
		List<Object[][]> result1 = new ArrayList<Object[][]>();
		List<Object[][]> result2 = new ArrayList<Object[][]>();

		List<Node> roots = new ArrayList<Node>();
		List<List<Node>> trees = new ArrayList<List<Node>>();
		Node root = new Node();
		List<Node> tree = new ArrayList<Node>();

		for (int i = 0; i < table.length; i++) {
			String content = table[i][0];
			boolean isTitle = true;
			if (content == null) {
				isTitle = false;
			}
			// check title
			for (int j = 1; j < table[0].length; j++) {
				if (table[i][j] != null) {
					isTitle = false;
				}
			}
			if (isTitle) {
				root = new Node();
				root.setContent(content);
				root.setLevel(0);
				trees.add(tree);
				tree = new ArrayList<Node>();
				tree.add(root);
				roots.add(root);
			} else {
				// j is the level of tree
				int parent = 0;
				Node first = null;
				Node pervious = null;
				for (int j = 0; j < table[0].length; j++) {
					if (table[i][j] != null && first == null) {
						parent = j;
						first = new Node();
						first.setLevel(j + 1);
						first.setContent(table[i][j]);
						pervious = first;
						tree.add(first);
					} else if (table[i][j] != null && first != null) {
						Node node = new Node();
						node.setLevel(j + 1);
						node.setContent(table[i][j]);
						pervious.addChild(node);
						pervious = node;
						tree.add(node);
					}
				}
				// set the first node parent
				for (int j = tree.size() - 1; j >= 0; j--) {
					if (tree.get(j).getLevel() == parent) {
						tree.get(j).addChild(first);
						break;
					}
				}
			}
		}

		for (Node r : roots) {
			Object[][] t = new Object[2][];
			t[0] = new String[] { null, null };
			t[1] = new Object[] { r.getContent(), rebuildTable(r) };
			result1.add(t);

			result.put(r.getContent(), getHTMLContent(rebuildTable(r), 20));
			result2.addAll(traverseAndBuild(r.getContent(), r));
		}
		for (Object[][] t : result2) {
			result.put(t[1][0].toString() + " " + t[0][1].toString(), t[1][1].toString());
		}
		
		result.putAll(combine2Questions(result1));
		result.putAll(combine2Questions(result2));

		return result;
	}

	public static Map<String, String> combine2Questions(List<Object[][]> l) throws ExcelException {
		Map<String, String> result = new HashMap<String, String>();
		for (int i = 0; i < l.size(); i++) {
			for (int j = i + 1; j < l.size(); j++) {
				result.putAll(combine2Questions(l.get(i), l.get(j)));
			}
		}
		return result;
	}

	// a size = b size = 2 * 2
	// Object can be String, String[] and String[][]
	public static Map<String, String> combine2Questions(Object[][] a, Object[][] b) throws ExcelException {
		Map<String, String> result = new HashMap<String, String>();
		if ((a[0][0] == null && b[0][0] == null) || a[0][0].equals(b[0][0])) {
			Object a00 = a[0][0];
			String subfix = a00 == null ? "" : a00.toString();
			

			if (a[1][0] == null) {
				return result;
			}

			if (a[0][1] == null && b[0][1] == null) {
				a[0][1] = "";
				b[0][1] = "";
			}

			if (a[0][1].equals(b[0][1]) && !a[1][0].equals(b[1][0])) {
				String qestion = a[1][0] + subfix + " " + b[1][0] + subfix + " " + a[0][1];
				String qestion1 = a[1][0] + " " + b[1][0] + " " + a[0][1];
                
				if(subfix.equals("") && a[0][1].equals("")){
					Object[][] answer = new Object[2][2];
					answer[0][0] = a[1][0];
					answer[0][1] = a[1][1];
					answer[1][0] = b[1][0];
					answer[1][1] = b[1][1];

					result.put(qestion, getHTMLContent(answer, 20));
					result.put(qestion1, getHTMLContent(answer, 20));
				}else{
					Object[][] answer = new Object[3][2];
					answer[0][0] = subfix;
					answer[0][1] = a[0][1];
					answer[1][0] = a[1][0];
					answer[1][1] = a[1][1];
					answer[2][0] = b[1][0];
					answer[2][1] = b[1][1];

					result.put(qestion, getHTMLContent(answer, 20));
					result.put(qestion1, getHTMLContent(answer, 20));
				}
				

				return result;
			}

			if (a[1][0].equals(b[1][0]) && !a[0][1].equals(b[0][1])) {
				String qestion = a[1][0] + subfix + " " + a[0][1] + " " + b[0][1];
				String qestion1 = a[1][0] + " " + a[0][1] + " " + b[0][1];

				Object[][] answer = new Object[2][3];
				answer[0][0] = subfix;
				answer[0][1] = a[0][1];
				answer[0][2] = b[0][1];
				answer[1][0] = a[1][0];
				answer[1][1] = a[1][1];
				answer[1][2] = b[1][1];

				result.put(qestion, getHTMLContent(answer, 20));
				result.put(qestion1, getHTMLContent(answer, 20));
				return result;
			}
		}
		return result;
	}

	private static String getHTMLContent(Object[][] answer, int widthLimit) throws ExcelException {
		String[][] trueTable = reveal(answer);
		return getHTMLContent(trueTable, 20);
	}

	private static String[][] reveal(Object[][] table) throws ExcelException {
		if (table instanceof String[][]) {
			return (String[][]) table;
		}

		Map<Integer, Integer> rowspanMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> colspanMap = new HashMap<Integer, Integer>();

		for (int i = 0; i < table.length; i++) {
			int rowspanMax = 1;
			for (int j = 0; j < table[i].length; j++) {
				int rowspan = 1;
				if (table[i][j] instanceof String[][]) {
					rowspan = ((String[][]) table[i][j]).length;
				}
				if (rowspan > rowspanMax) {
					rowspanMax = rowspan;

				}
			}
			rowspanMap.put(i, rowspanMax);
		}

		for (int i = 0; i < table[0].length; i++) {
			int colspanMax = 1;
			for (int j = 0; j < table.length; j++) {
				int colspan = 1;
				if (table[j][i] instanceof String[][]) {
					colspan = ((String[][]) table[j][i])[0].length;
				} else if (table[j][i] instanceof String[]) {
					colspan = ((String[]) table[j][i]).length;
				}
				if (colspan > colspanMax) {
					colspanMax = colspan;

				}
			}
			colspanMap.put(i, colspanMax);
		}

		int[] i_s = new int[table.length + 1];
		int[] j_s = new int[table[0].length + 1];
		i_s[0] = 0;
		j_s[0] = 0;

		for (int i = 1; i < table.length + 1; i++) {
			i_s[i] = rowspanMap.get(i - 1) + i_s[i - 1];
		}

		for (int i = 1; i < table[0].length + 1; i++) {
			j_s[i] = colspanMap.get(i - 1) + j_s[i - 1];
		}

		int tolRow = i_s[table.length];
		int tolCol = j_s[table[0].length];

		String[][] newT = new String[tolRow][tolCol];

		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				int rowCount = rowspanMap.get(i);
				int colCount = colspanMap.get(j);

				if (table[i][j] instanceof String) {
					if (rowCount != 1 && colCount != 1) {
						throw new ExcelException(3, "");
					}
					newT[i_s[i]][j_s[j]] = (String) table[i][j];
					
					if (rowCount != 1) {
						for (int k = i_s[i] + 1; k < i_s[i + 1]; k++) {
							newT[k][j_s[j]] = null;
						}
					} else if (colCount != 1) {
						for (int k = j_s[j] + 1; k < j_s[j + 1]; k++) {
							newT[i_s[i]][k] = null;
						}
					}
				} else if (table[i][j] instanceof String[]) {
					String[] cell = (String[]) table[i][j];
					if (colCount != cell.length) {
						throw new ExcelException(3, "");
					}

					for (int k = j_s[j]; k < j_s[j + 1]; k++) {
						newT[i_s[i]][k]= cell[k - j_s[j]];
					}

					if (j < table[i].length - 1) {
						for (int l = j_s[j] + 1; l < j_s[j + 1]; j++) {
							for (int k = i_s[i]; k < i_s[i]; k++) {
								newT[k][l] = null;
							}
						}
					}

				} else if (table[i][j] instanceof String[][]) {
					String[][] cell = (String[][]) table[i][j];
					if (rowCount != cell.length && colCount != cell[0].length) {
						throw new ExcelException(3, "");
					}

					for (int l = j_s[j]; l < j_s[j + 1]; l++) {
						for (int k = i_s[i]; k < i_s[i + 1]; k++) {
							if (k - i_s[i] >= cell.length || l - j_s[j] >= cell[0].length) {
								newT[k][l] = null;
							} else {
								newT[k][l] = cell[k - i_s[i]][l - j_s[j]];
							}
						}
					}
				}
			}
		}
		return newT;
	}

	public static List<Object[][]> traverseAndBuild(String rootInfo, Node node) {
		List<Object[][]> l = new ArrayList<Object[][]>();

		for (Node c : node.getChildren()) {
			if (c.isLeaf()) {
				return l;
			}

			Object[][] t = new Object[2][];
			t[0] = new String[] { null, c.getContent() };
			String[][] content = rebuildTable(c);
			if(content.length == 1 && content[0].length == 1){
				t[1] = new Object[] { rootInfo, content[0][0] };
			}else{
				t[1] = new Object[] { rootInfo, content };
			}
			
			l.add(t);

			l.addAll(traverseAndBuild(rootInfo, c));
		}
		return l;
	}

	public static String[][] rebuildTable(Node node) {
		int colNum = nodeDeepth(node, 1);
		int rowNum = leafCount(node);

		String[][] content = new String[rowNum][colNum];
		fillTable(node, content, 0, 0);
		String[][] contentInNeed = new String[rowNum][colNum - 1];

		for (int i = 0; i < contentInNeed.length; i++) {
			for (int j = 0; j < contentInNeed[i].length; j++) {
				contentInNeed[i][j] = content[i][j + 1];
			}
		}
		return contentInNeed;
	}

	public static void fillTable(Node node, String[][] table, int colAxis, int rowAxis) {
		int colNum = leafCount(node);
		table[rowAxis][colAxis] = node.getContent();
		for (int i = rowAxis + 1; i < rowAxis + colNum; i++) {
			table[i][colAxis] = null;
		}

		int rowStart = rowAxis;
		colAxis++;
		for (Node c : node.getChildren()) {
			fillTable(c, table, colAxis, rowStart);
			rowStart += colNum = leafCount(c);
		}
	}

	public static int nodeDeepth(Node node, int level) {
		if (node.isLeaf()) {
			return level;
		}
		int maxDeepth = Integer.MIN_VALUE;
		for (Node c : node.getChildren()) {
			int deepth = nodeDeepth(c, level + 1);
			if (maxDeepth < deepth) {
				maxDeepth = deepth;
			}
		}
		return maxDeepth;
	}

	public static int leafCount(Node node) {
		if (node.isLeaf()) {
			return 1;
		} else {
			int count = 0;
			for (Node c : node.getChildren()) {
				count += leafCount(c);
			}
			return count;
		}
	}

	public static String getHTMLContent(String[][] table, int widthLimit) {
		StringBuffer buffer = new StringBuffer();
		if (table.length == 1 && table[0].length == 0) {
			return "";
		}

		if (table.length == 1 && table[0].length == 1) {
			return table[0][0];
		}

		if (table[0][0] == null) {
			table[0][0] = "";
		}

		int[] width = new int[table[0].length];
		int[] height = new int[table.length];

		// init
		for (int w : width) {
			w = 0;
		}
		for (int h : height) {
			h = 0;
		}

		// determine the width of each column
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				int cellHeight = 1;
				if (table[i][j] == null) {
					continue;
				}
				int cellWidth = table[i][j].length();
				if (cellWidth > widthLimit) {
					cellHeight = cellWidth / widthLimit + 1;
					cellWidth = widthLimit;
				}
				if (width[j] < cellWidth) {
					width[j] = cellWidth;
				}
				if (height[i] < cellHeight) {
					height[i] = cellHeight;
				}
			}
		}

		Pattern p = Pattern.compile("<p hidden='hidden'>.+</p>");

		buffer.append("<table border='1' padding='0' margin='0' cellspacing='0' cellpadding='0'>");
		Set<String> spaned = new HashSet<String>();
		List<int[]> blocks = new ArrayList<int[]>();

		// detect empty block
		for (int i = 0; i < table.length; i++) {
			
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j] == null && !spaned.contains("" + i + "," + j)) {
					int rowStart = i;
					int colStart = j;
					int rowEnd = table.length;
					int colEnd = table[j].length;

					for (int k = i; k < rowEnd; k++) {
						if (k < table.length - 1 && rowEnd > k + 1) {
							if (table[k + 1][j] != null || spaned.contains(("" + k + "," + i))) {
								rowEnd = k;
							}
						}
						for (int l = j; l < colEnd; l++) {
							if (i == k && l == j) {
								continue;
							}
							if (spaned.contains(("" + k + "," + l))) {
								if (colEnd > l) {
									colEnd = l;
									colEnd = l;
								}
								break;
							}else if(table[i][l] != null){
								colEnd = l - 1;
							} else {
								if (l + 1 == colEnd || table[k][l + 1] != null) {
									if (colEnd > l) {
										colEnd = l;
										colEnd = l;
									}
									break;
								}
							}
						}
					}
					int[] block = null;
					if (rowStart == rowEnd) {
						block = new int[] { rowStart, colStart - 1, 0, colEnd - colStart + 2 };
						blocks.add(block);
					} else if (colStart == colEnd) {
					
						block = new int[] { rowStart - 1, colStart, 1, rowEnd - rowStart + 2 };
						blocks.add(block);
					} else {
						for (int k = rowStart; k < rowEnd; k++) {
							block = new int[] { k, colStart - 1, 0, colEnd - colStart + 2 };
							blocks.add(block);
						}
					}

					for (int k = rowStart; k <= rowEnd; k++) {
						for (int l = colStart; l <= colEnd; l++) {
							spaned.add("" + k + "," + l);
						}
					}
				}
			}
		}

		for (int i = 0; i < table.length; i++) {
			buffer.append("<tr>");
			for (int j = 0; j < table[i].length; j++) {
				int spanCol = 0;
				int spanRow = 0;

				for (int[] block : blocks) {
					if (block[0] == i && block[1] == j) {
						if (block[2] == 0) {
							spanCol = block[3];
						}
						if (block[2] == 1) {
							spanRow = block[3];
						}
					}
				}

				if (table[i][j] != null) {
					if (spanRow != 0 || spanCol != 0) {
						buffer.append("<td " + "colspan='" + spanCol + "' rowspan='" + spanRow + "'>");
					} else {
						buffer.append("<td>");
					}
					String tmp = table[i][j];
					Matcher m = p.matcher(tmp);
					if (m.find()) {
						String s = m.group();
						tmp = tmp.replace(s, "");
						buffer.append(tmp);
					} else {
						buffer.append(tmp);
					}
					buffer.append("</td>");
				}
			}
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		buffer.append("<p hidden='hidden'>");
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j] == null) {
					buffer.append("\t");
				} else {
					Matcher m = p.matcher(table[i][j]);
					if (m.find()) {
						String s = m.group();
						String tmp = s.replace("<p hidden='hidden'>", "");
						tmp = s.replace("</p>", "");

						buffer.append(tmp);
					} else {
						buffer.append(table[i][j]);
					}
				}
				buffer.append(";");
			}
		}
		buffer.append("</p>");
		return buffer.toString();
	}

	public static void main(String[] args) throws Exception, IOException, ExcelException {
		Map<String, String> tblqa = getQA("d:/resource/raw/手机对比.xls");

	    //Map<String, String> tblqa = DocTable.getTblQA("docx", new FileInputStream("D:/resource/raw/测试文档.docx"));
		
		
		for (Entry<String, String> en : tblqa.entrySet()) {
			System.out.println(en.getKey());
			System.out.println("________________________");
			System.out.println(en.getValue());
			System.out.println("=================================================");
		}
		

		StringBuffer buffer = new StringBuffer();
		
		for (Entry<String, String> en : tblqa.entrySet()) {
			buffer.append(en.getKey() + "\r\n");
			buffer.append("________________________" + "\r\n");
			buffer.append(en.getValue() + "\r\n");
			buffer.append("=================================================" + "\r\n");
		}
		
		writeTxt("d:/a.html", buffer.toString());
	}
	
	private static void writeTxt(String path, String content){
		BufferedWriter writer = null;
		try {
            File f = new File(path);
            if(!f.exists()){
            	f.createNewFile();
            }
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path),"UTF-8"));

			writer.write(content); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

class Node {

	private int level;

	private List<Node> children;

	private String content;

	public Node() {
		children = new ArrayList<Node>();
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void addChild(Node node) {
		children.add(node);
	}

	public String toString() {
		return content;
	}

	public boolean isLeaf() {
		if (children.isEmpty()) {
			return true;
		}
		return false;
	}

	public Node getInternalNode(String str) {
		if (isLeaf()) {
			return null;
		}
		if (content.equals(str) && !isLeaf()) {
			return this;
		}
		Node tmp = null;
		for (Node n : this.getChildren()) {
			if ((tmp = n.getInternalNode(str)) != null) {
				return tmp;
			}
		}
		return null;
	}

}

class Lex4Excel {

	public static Pattern ESCAPE = Pattern.compile("^(\n|\\r\\n)$");

	public static Pattern TAB = Pattern.compile("^\t$");

	public static Pattern STRING = Pattern.compile("^((?!\t|\n)[\\w\\W])+$");

	public static LinkedHashMap<Pattern, DNode> COLLECTION = new LinkedHashMap<Pattern, DNode>();

	static {
		COLLECTION.put(ESCAPE, DocumentBean.ESCAPE);
		COLLECTION.put(TAB, DocumentBean.TAB);
		COLLECTION.put(STRING, DocumentBean.STRING);
	}

}

package cn.jintongsoft.extractor;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;


import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import com.aspose.words.Document;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;

import org.apache.any23.encoding.TikaEncodingDetector;

public class QAExtractor  {

	private static final Logger logger = LogManager.getLogger(DocTable.class);

	public Map<String, String> getQAFromDocument(byte[] b, String fileName, String type) {
		Map<String, String> tblqa = null;
		String content = null;

		if (type.equals("xls") || type.equals("xlsx")) {
			try {
				tblqa = ExcelHandler.getQA(type, b);
			} catch (IOException e) {
				logger.catching(e);
			} catch (ExcelException e) {
				logger.catching(e);
			}finally {
				return tblqa;
			}
		} else {
			content = getContent(type, b);
			if (type.equals("doc") || type.equals("docx")) {
				try {
					tblqa = DocTable.getTblQA(type, new ByteArrayInputStream(b));
				} catch (IOException e) {
					logger.catching(e);
				} catch (ExcelException e) {
					logger.catching(e);
				}catch (Exception e){
					logger.catching(e);
				}
			}
		}
		Map<String, String> qaPairs = new HashMap<String, String>();
		if (content != null) {
			try {
				cn.jintongsoft.utils.Paragraph paragraph = FormattedDocumentStructureAnalysis.generate(content);
				qaPairs = new ExtractQuestionAnswerPair().extractQAPair(paragraph);
				List<String> atomSentences = new ExtractAtomSentences().generateAtomSentences(paragraph);
				if (tblqa != null) {
					qaPairs.putAll(tblqa);
				}
				for (String sentence : atomSentences) {
					qaPairs.put(sentence, sentence);
				}
				qaPairs.remove("");
				qaPairs.remove(null);
				
			} catch (Exception ex) {
				logger.catching(ex);
			}
		} else if (content == null && tblqa != null) {
			tblqa.remove("");
			tblqa.remove(null);
			qaPairs.putAll(tblqa);
		} else{
			return qaPairs;
		}

        return qaPairs;
	}

	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static String getContent(String type, byte[] b) {
		InputStream is = new ByteArrayInputStream(b);
		if (type.equalsIgnoreCase("txt")) {
			try {
				String encoding = Charset.forName(new TikaEncodingDetector().guessEncoding(is)).name();
				String str = new String(b, encoding);
				return str;
			} catch (IOException e) {
				logger.catching(e);
				logger.debug("读取错误");
			}
		}
		return getContent(type, is);
	}

	public static String getContent(String type, InputStream is) {
		if (type.equalsIgnoreCase("pdf")) {
			try {
				PDDocument document = PDDocument.load(is);
				PDFTextStripper stripper = new PDFTextStripper();
				String data = stripper.getText(document);
				is.close();
				document.close();
				return data;
			} catch (InvalidPasswordException e) {
				logger.catching(e);
				logger.debug("密码错误");
			} catch (IOException e) {
				logger.catching(e);
				logger.debug("读取错误");
			}
		} else if (type.equalsIgnoreCase("doc") || type.equalsIgnoreCase("docx")) {
			try {
				Document doc = new Document(is);
				File file = File.createTempFile("tmp-", ".docx");
				String tmpPath = file.getAbsolutePath();
				doc.save(tmpPath);
				DocTable.removeAll(tmpPath);
				is.close();

				StringBuffer buffer = new StringBuffer();
				InputStream inputStream = new FileInputStream(tmpPath);
				Document document = new Document(inputStream);
				document.updateListLabels();

				NodeCollection<?> nodeCollection = document.getChildNodes(NodeType.PARAGRAPH, true);
				for (Object object : nodeCollection) {
					if (object instanceof Paragraph) {
						Paragraph paragraph = (Paragraph) object;

						String paragraphText = paragraph.getText().trim().replaceAll("　", "");
						if (!paragraphText.startsWith(
								"Evaluation Only. Created with Aspose.Words. Copyright 2003-2017 Aspose Pty Ltd.")) {
							buffer.append(paragraphText).append("\n");
						}
					}
				}
				inputStream.close();
				return buffer.toString();
			} catch (Exception e) {
				logger.catching(e);
				logger.debug("读取错误");
			}
		}
		return null;
	}
}

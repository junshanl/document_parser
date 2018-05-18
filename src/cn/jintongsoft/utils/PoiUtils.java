package cn.jintongsoft.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hslf.usermodel.HSLFSlideShowImpl;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class PoiUtils {
    public static byte[] getText(File sourcefile, String suffix, boolean encoding) throws Exception, IOException {
        if (sourcefile == null)
            throw new Exception("File对象不能为空");
        if (sourcefile.isDirectory())
            throw new Exception("File对象不能为路径");
        switch (suffix) {
            case "txt":
                return analyzeTxt(sourcefile, encoding);
            case "pdf":
                return analyzePDF(sourcefile);
            case "docx":
                return analyzeDocx(sourcefile);
            case "doc":
                return analyzeDoc(sourcefile);
            case "pptx":
                return analyzePptx(sourcefile);
            case "ppt":
                return analyzePpt(sourcefile);
            case "xlsx":
                return analyzeXlsx(sourcefile);
            case "xls":
                return analyzeXls(sourcefile);
            default:
                throw new Exception("不在解析范围内的文件格式");
        }
    }

    private static byte[] analyzeTxt(File sourcefile, boolean encoding) throws IOException, Exception {
        byte[] data = null;
        BufferedReader reader = null;
        try {
            StringBuilder source = new StringBuilder("");
            String line = "";
            String encode = "UTF-8";
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourcefile), encode));
            while ((line = reader.readLine()) != null) {
                source.append(line).append("\n");
            }

            data = source.toString().getBytes("utf-8");
            // data = FileUtils.readFileToByteArray(sourcefile);
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null)
                reader.close();
        }
        return data;
    }

    private static byte[] analyzePDF(File sourcefile) throws IOException {
        byte[] data = null;
        PDDocument pdfdoc = null;
        try {

            RandomAccessBufferedFileInputStream ra = new RandomAccessBufferedFileInputStream(sourcefile);
            PDFParser parser = new PDFParser(ra);
            parser.parse();
            PDFTextStripper stripper = new PDFTextStripper();
            String s = stripper.getText(parser.getPDDocument());

            data = s.getBytes();
        } catch (IOException e) {
            throw e;
        } finally {
            if (pdfdoc != null)
                pdfdoc.close();
        }
        return data;
    }

    private static byte[] analyzeDocx(File sourcefile) throws IOException {
        byte[] data = null;
        XWPFWordExtractor extractor = null;
        try {
            extractor = new XWPFWordExtractor(new XWPFDocument(new FileInputStream(sourcefile)));
            data = extractor.getText().getBytes("utf-8");
        } catch (IOException e) {
            throw e;
        } finally {
            if (extractor != null){

            }
        }
        return data;
    }

    private static byte[] analyzeDoc(File sourcefile) throws IOException {
        byte[] data = null;
        WordExtractor extractor = null;
        try {
            extractor = new WordExtractor(new HWPFDocument(new FileInputStream(sourcefile)));
            data = extractor.getText().getBytes("utf-8");
        } catch (IOException e) {
            throw e;
        } finally {
            if (extractor != null){

            }

        }
        return data;
    }

    private static byte[] analyzeXlsx(File sourcefile) throws IOException {
        byte[] data = null;
        XSSFExcelExtractor extractor = null;
        try {
            extractor = new XSSFExcelExtractor(new XSSFWorkbook(new FileInputStream(sourcefile)));
            data = extractor.getText().getBytes("utf-8");
        } catch (IOException e) {
            throw e;
        } finally {
            if (extractor != null){

            }

        }
        return data;
    }

    private static byte[] analyzeXls(File sourcefile) throws IOException {
        byte[] data = null;
        ExcelExtractor extractor = null;
        try {
            extractor = new ExcelExtractor(new HSSFWorkbook(new FileInputStream(sourcefile)));
            data = extractor.getText().getBytes("utf-8");
        } catch (IOException e) {
            throw e;
        } finally {
            if (extractor != null)
              {

            }
        }
        return data;
    }

    private static byte[] analyzePptx(File sourcefile) throws IOException {
        byte[] data = null;
        XSLFPowerPointExtractor extractor = null;
        try {
            extractor = new XSLFPowerPointExtractor(new XMLSlideShow(new FileInputStream(sourcefile)));
            data = extractor.getText().getBytes("utf-8");
        } catch (IOException e) {
            throw e;
        } finally {
            if (extractor != null){}

        }
        return data;
    }

    private static byte[] analyzePpt(File sourcefile) throws IOException {
        byte[] data = null;
        PowerPointExtractor extractor = null;
        try {
            extractor = new PowerPointExtractor(new HSLFSlideShowImpl(new FileInputStream(sourcefile)));
            data = extractor.getText().getBytes("utf-8");
        } catch (IOException e) {
            throw e;
        } finally {
            if (extractor != null){

            }
        }
        return data;
    }

	/*public static void copyTxtEncodeUTF8(MultipartFile fileUpload, File file) throws IOException {
		InputStream input = fileUpload.getInputStream();
		BufferedInputStream in = new BufferedInputStream(input);
		Charset result = getFileEncode(in);
		if (result != null) {
			StringBuilder sb = new StringBuilder("");
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, result));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			writer.write(sb.toString());
			reader.close();
			writer.flush();
			writer.close();
		} else {
			FileUtils.writeByteArrayToFile(file, fileUpload.getBytes());
		}
		in.close();
	}*/

    @SuppressWarnings("unused")
    private static Charset getFileEncode(InputStream in) throws IllegalArgumentException, IOException {
        return Charset.defaultCharset();
        // return getDetector().detectCodepage(in, in.available());
    }

    @SuppressWarnings("unused")
    private static Charset getFileEncode(File file) throws IllegalArgumentException, IOException {
        return Charset.defaultCharset();
        // return getDetector().detectCodepage(file.toURI().toURL());
    }

}

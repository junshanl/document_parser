package cn.jintongsoft.extractor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.jintongsoft.utils.Paragraph;

public class ExtractQuestionAnswerPair {

    // private static final Logger LOGGER =
    // LogManager.getLogger(ExtractQuestionAnswerPair.class);
    private Map<String, String> qaPairs = new LinkedHashMap<>();

    public Map<String, String> extractQAPair(Paragraph paragraph) {
        extract(paragraph);
        return qaPairs;
    }

    /**
     * 从给定的paragraph中获取问答对
     * 当前paragraph的标题作为问题，其内容和其子paragraph的内容联合起来作为答案，并递归抽取
     *
     * @param paragraph
     */
    private void extract(Paragraph paragraph) {
        String title = paragraph.getTitle();
        List<String> content = paragraph.getContent();
        List<Paragraph> subPara = paragraph.getSubParagraph();
        if (title != null && !title.isEmpty() && (!content.isEmpty() || !subPara.isEmpty())) {
            String string = generateContent(paragraph);
            if (!string.isEmpty()) {
                qaPairs.put(title, string);
            }
        }
        if (!subPara.isEmpty()) {
            for (Paragraph sub : subPara) {
                extract(sub);
            }
        }
    }

    /**
     * 从paragraph中将标题和内容合并
     *
     * @param paragraph
     * @return
     */
    private String generateContent(Paragraph paragraph) {
        String contentStr = paragraph.getTitle();
        List<String> content = paragraph.getContent();
        contentStr = contentStr + "\n" + concat(content);
        List<Paragraph> subParas = paragraph.getSubParagraph();
        if (!subParas.isEmpty()) {
            for (Paragraph sub : subParas) {
                contentStr = contentStr + "\n" + generateContent(sub);
            }
        }
        return contentStr;
    }

    /**
     * 对于给定的列表合并其中的句子为单个字符串
     *
     * @param content
     * @return
     */
    private String concat(List<String> content) {
        StringBuffer buffer = new StringBuffer();
        for (String string : content) {
            buffer.append(string);
            buffer.append("\n");
        }
        return buffer.toString().trim();
    }
}
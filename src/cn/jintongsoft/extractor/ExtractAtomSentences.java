/**
 * 从给定的Paragraph中获取原子句
 */
package cn.jintongsoft.extractor;

import java.util.ArrayList;
import java.util.List;

import cn.jintongsoft.utils.Paragraph;

public class ExtractAtomSentences {
	private List<String> atomSentences = new ArrayList<>();

	public void generate(Paragraph paragraph) {
		for (String string : paragraph.getContent()) {
			String[] array = string.split("[。？！；]");
			int index = -1;
			for (String arrayString : array) {
				index = index + arrayString.length() + 1;
				if (index < string.length()) {
					arrayString = arrayString + string.substring(index, index + 1);
				}
				atomSentences.add(arrayString);
			}
		}
		for (Paragraph subParagraph : paragraph.getSubParagraph()) {
			generate(subParagraph);
		}
	}

	public List<String> generateAtomSentences(Paragraph paragraph) {
		generate(paragraph);
		return atomSentences;
	}
}

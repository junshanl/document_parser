package cn.jintongsoft.nlp.parser.listitem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.jintongsoft.nlp.parser.Lexeme;
import cn.jintongsoft.nlp.parser.SyntaxTree;
import cn.jintongsoft.nlp.parser.Token;
import cn.jintongsoft.nlp.parser.TreeNode;
import cn.jintongsoft.nlp.parser.document.Lex4List;
import cn.jintongsoft.nlp.parser.document.Syntax4Document;

public class ListItemWarper {

	public static List<RawListItem> transform(List<TreeNode> nodes) {
		List<RawListItem> items = warp(nodes);
		fillPattern(items);
		fillLevel(items);
		return items;
	}

	public static List<RawListItem> warp(List<TreeNode> nodes) {
		List<RawListItem> items = new ArrayList<RawListItem>();
		SyntaxTree st = new SyntaxTree();

		for (TreeNode treeNode : nodes) {
			RawListItem item = new RawListItem();
			item.listInfo = st.getContent(st.getAllTreeNodes(treeNode, Syntax4Document.LIST).get(0));

			List<TreeNode> paragraphs = st.getAllTreeNodes(treeNode, Syntax4Document.PARAGRAPH);
			List<TreeNode> paragraphBlock = st.getAllTreeNodes(treeNode, Syntax4Document.PARAGRAPH_BLOCK);
			List<TreeNode> titleNode = st.getAllTreeNodes(treeNode, Syntax4Document.TITLE);
			

			String firstParagraph = null;
			String title = null;
			
			String first = null;
			
			if (!paragraphs.isEmpty()) {
				firstParagraph = st.getContent(paragraphs.get(0));
			}
			if (!titleNode.isEmpty()) {
				title = st.getContent(titleNode.get(0));
			}

			if (firstParagraph != null && title != null) {
				first = title + firstParagraph;
			} else if (firstParagraph != null && title == null) {
				first = firstParagraph;
			}else if (firstParagraph == null && title != null) {
				first = title;
			}

			if (first != null) {
				if (isTitle(first)) {
					item.title = first;
				} else {
					if (title != null) {
						item.title = title;
					}
					firstParagraph = null;
				}
			}
			
			NodePara tmp = new NodePara();
			
			if(!paragraphBlock.isEmpty()){
				tmp.title = item.title;
				if(firstParagraph == null){
					tmp.content.add(st.getContent(paragraphs.get(0)));
				}
				for(int i = 1; i < paragraphs.size(); i++){
					String para = st.getContent(paragraphs.get(i));
					
					String[] parts = para.split("\n");
					if(parts.length < 1){
						tmp.content.add(para);
						continue;
					}
					String firstLine = parts[0];
					
					if(isTitle(firstLine) && isTitleLength(firstLine, parts) ){
						item.content.add(tmp);
						tmp = new NodePara();
						tmp.title = firstLine;
						para = para.replace(firstLine + "\n", "");
					}
					tmp.content.add(para);
				}
			}
			item.content.add(tmp);
			items.add(item);
		}
		return items;
	}

	public static boolean isTitleLength(String title ,String[] parts){
		double max = 0.;
		for(String part : parts){
			max = Math.max(max, part.length());
		}
		if(title.length() < max){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isTitle(String str){
		Pattern p = Pattern.compile("^[【《]?[a-zA-Z0-9\\u4e00-\\u9fa5，：.、\\n]+[】》？：]?$");
		return p.matcher(str).find();
	}

	public static void fillPattern(List<RawListItem> list) {
		for (RawListItem item : list) {
			Lexeme lex = new Lexeme();
			lex.init(item.listInfo, Lex4List.COLLECTION);
			lex.run();

			List<Token> tokens = lex.getTokens();
			item.pattern = tokens;
			item.index = getLastNumbering(tokens);
		}
	}

	public static int getLastNumbering(List<Token> tokens) {
		for (int i = tokens.size() - 1; i >= 0; i--) {
			if (Lex4List.ORDER_COLLECTION.contains(tokens.get(i).getPattern())) {
				return (int) NumberingUtil.getNumbering(tokens.get(i));
			}
		}
		return -1;
	}

	public static void fillLevel(List<RawListItem> list) {
		List<List<Token>> s = new ArrayList<List<Token>>();

		for (RawListItem item : list) {
			int index = -1;
			for (List<Token> previous : s) {
				if (ListLexCompartor.compare(previous, item.pattern)) {
					index = s.indexOf(previous);
					break;
				}
			}
			if (index == -1) {
				s.add(item.pattern);
			} else {
				for (int i = s.size() - 1; i > index; i--) {
					s.remove(i);
				}
			}
			item.level = s.size();
		}
	}

}

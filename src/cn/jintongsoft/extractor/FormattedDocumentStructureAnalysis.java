package cn.jintongsoft.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.jintongsoft.nlp.parser.CFG;
import cn.jintongsoft.nlp.parser.LLParser;
import cn.jintongsoft.nlp.parser.Lexeme;
import cn.jintongsoft.nlp.parser.SyntaxTree;
import cn.jintongsoft.nlp.parser.Token;
import cn.jintongsoft.nlp.parser.TreeNode;
import cn.jintongsoft.nlp.parser.catalog.CatalogHandler;
import cn.jintongsoft.nlp.parser.document.Lex4Document;
import cn.jintongsoft.nlp.parser.document.Syntax4Document;
import cn.jintongsoft.nlp.parser.listitem.ListItemReg;
import cn.jintongsoft.nlp.parser.listitem.ListItemWarper;
import cn.jintongsoft.nlp.parser.listitem.RawListItem;
import cn.jintongsoft.utils.Paragraph;

public class FormattedDocumentStructureAnalysis {
	
	private static final Logger logger = LogManager.getLogger(FormattedDocumentStructureAnalysis.class);
	
	public static Paragraph generate(String content) {
		content = content.replaceAll("\r\n", "\n");
		content = content.replaceAll("\\u0020", "");
		content = content.replaceAll("\\u3000", "");

		content = CatalogHandler.remove(content);
		Lexeme lex = new Lexeme();
		lex.init(content, Lex4Document.COLLECTION);
		lex.run();
		ArrayList<Token> tokens = lex.getTokens();
		LLParser llparser = new LLParser();
		llparser.showLog = false;
		CFG cfg = Syntax4Document.getFullCFG();

		SyntaxTree st = llparser.parse(cfg, tokens);
		// System.out.println(llparser.count);
		List<TreeNode> list = st.getAllTreeNodes(Syntax4Document.LISTITEM);
		String introduction = null;
		if (st.getAllTreeNodes(Syntax4Document.INTRODUCTION).size() > 0) {
			TreeNode intro = st.getAllTreeNodes(Syntax4Document.INTRODUCTION).get(0);
			introduction = st.getContent(st.getAllTreeNodes(intro, Syntax4Document.INTRODUCTION).get(0));
		}

		// 转换到 Paragraph
		List<RawListItem> items = ListItemWarper.transform(list);
		items = ListItemReg.correct2(items);
		// 根段落为:没有标号的头段
		Paragraph p = ListItemReg.toParaprah(introduction, items);
		logger.debug(ListItemReg.printParagraphStruct(p));;
		return p;
	}
}

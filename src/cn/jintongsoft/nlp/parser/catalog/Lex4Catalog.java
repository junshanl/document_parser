package cn.jintongsoft.nlp.parser.catalog;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import cn.jintongsoft.nlp.parser.DNode;
import cn.jintongsoft.nlp.parser.document.DocumentBean;
import cn.jintongsoft.nlp.parser.document.Lex4Document;

public class Lex4Catalog extends Lex4Document{

	public static Pattern KEYWORD_CATALOG = Pattern.compile("^目录$");
	
	public static Pattern KEYWORD_APPANDIX = Pattern.compile("^附录$");

	public static LinkedHashMap<Pattern, DNode> COLLECTION = new LinkedHashMap<Pattern, DNode>();

	static {
		COLLECTION.put(KEYWORD_CATALOG, DocumentBean.KEYWORD_CATALOG);
		COLLECTION.put(KEYWORD_APPANDIX, DocumentBean.KEYWORD_APPENDIX);
		
		COLLECTION.put(ESCAPE, DocumentBean.ESCAPE);
		COLLECTION.put(TAB, DocumentBean.TAB);

		COLLECTION.put(LIST_SPERATOR, DocumentBean.LIST_SPERATOR);
		COLLECTION.put(PAIR_LEFT, DocumentBean.PAIR_LEFT);
		COLLECTION.put(PAIR_RIGHT, DocumentBean.PAIR_RIGHT);

		COLLECTION.put(BALL_NUMBERING, DocumentBean.BALL_NUMBERING);
		COLLECTION.put(CN_ORDERING, DocumentBean.CN_ORDERING);
		COLLECTION.put(NUMBERING, DocumentBean.NUMBERING);
		COLLECTION.put(CN_NUMBERING, DocumentBean.CN_NUMBERING);
		COLLECTION.put(ROMEN_NUMBERING, DocumentBean.ROMEN_NUMBERING);
		COLLECTION.put(EN_NUMBERING, DocumentBean.EN_NUMBERING);

		COLLECTION.put(STRING, DocumentBean.STRING);
		COLLECTION.put(CHARACTER, DocumentBean.STRING);
		COLLECTION.put(UNKNOWN, DocumentBean.UNKNOWN);
	}

}

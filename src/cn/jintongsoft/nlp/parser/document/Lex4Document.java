package cn.jintongsoft.nlp.parser.document;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import cn.jintongsoft.nlp.parser.DNode;

public class Lex4Document {
	
	public static Pattern NUMBERING = Pattern.compile("^[\\d]+(?!\n)$");
	
	public static Pattern EN_NUMBERING = Pattern.compile("^[a-z](?!\n)$");
	
	public static Pattern BALL_NUMBERING = Pattern.compile("^[①②③④⑤⑥⑦⑧⑨]$");
	
	public static Pattern CN_NUMBERING = Pattern.compile("^[一二三四五六七八九十壹贰叁肆伍陆柒捌玖拾]+(?!\n)$");
	
	public static Pattern ROMEN_NUMBERING = Pattern.compile("^(l?x{0,3}|x[lc])(v?i{0,3}|i[vx])(?!\n)$");
	
	public static Pattern CN_ORDERING = Pattern.compile("^第[\\w\\W]+[章节条款(部分)]$");
	
	public static Pattern PAIR_RIGHT = Pattern.compile("^[\\]>)】）”](?!\n)$");
	
	public static Pattern PAIR_LEFT = Pattern.compile("^[\\[<(【（“](?!\n)$");
	
	public static Pattern LIST_SPERATOR = Pattern.compile("^[.、．](?!\n)$");
	
	public static Pattern PUNCUATION1 = Pattern.compile("^[:\\+\\-·=/：、](?!\n)$");
	
	public static Pattern PUNCUATION2 = Pattern.compile("^[.?!:;,'\"@#$%^&*\\/\\+\\-·=/。？！：；，、＝×÷±φ](?!\n)$");
	
	public static Pattern STOP = Pattern.compile("^[。？！；](?!\n)$");
	
	public static Pattern ESCAPE = Pattern.compile("^(\n|\\r\\n)$");
	
	public static Pattern TAB = Pattern.compile("^\t$");
	
	public static Pattern STRING = Pattern.compile("^[a-zA-Z0-9\\u4e00-\\u9fa5]{2,}(?!\n)$");
	
	public static Pattern CHARACTER = Pattern.compile("^[a-zA-Z0-9\\u4e00-\\u9fa5](?!\n)$");
	
	public static Pattern UNKNOWN = Pattern.compile("^[\\w\\W]$");
	
	public static LinkedHashMap<Pattern, DNode> COLLECTION = new LinkedHashMap<Pattern, DNode>();
	
	static {
	
		COLLECTION.put( ESCAPE, DocumentBean.ESCAPE);
		COLLECTION.put( TAB, DocumentBean.TAB);
		
		COLLECTION.put( LIST_SPERATOR , DocumentBean.LIST_SPERATOR);
		
		COLLECTION.put( STOP, DocumentBean.STOP);
		COLLECTION.put( PAIR_LEFT, DocumentBean.PAIR_LEFT);
		COLLECTION.put( PAIR_RIGHT, DocumentBean.PAIR_RIGHT);
		COLLECTION.put( PUNCUATION1, DocumentBean.PUNCTUACTION1);
		COLLECTION.put( PUNCUATION2, DocumentBean.PUNCTUACTION2);
		
		COLLECTION.put( BALL_NUMBERING, DocumentBean.BALL_NUMBERING);
		COLLECTION.put( CN_ORDERING, DocumentBean.CN_ORDERING);
		COLLECTION.put( NUMBERING, DocumentBean.NUMBERING);
		COLLECTION.put( CN_NUMBERING, DocumentBean.CN_NUMBERING);
		COLLECTION.put( ROMEN_NUMBERING, DocumentBean.ROMEN_NUMBERING);
		COLLECTION.put( EN_NUMBERING, DocumentBean.EN_NUMBERING);
		
		COLLECTION.put( STRING, DocumentBean.STRING);
		COLLECTION.put( CHARACTER, DocumentBean.STRING);
		COLLECTION.put( UNKNOWN, DocumentBean.UNKNOWN);
	}
	
}


package cn.jintongsoft.nlp.parser.document;

import cn.jintongsoft.nlp.parser.Bean;
import cn.jintongsoft.nlp.parser.DNode;

public class DocumentBean extends Bean{
	
	public static final DNode NUMBERING = new DNode("NUMBERING");
	
	public static final DNode PUNCTUACTION1 = new DNode("PUNCTUACTION1");
	
	public static final DNode PUNCTUACTION2 = new DNode("PUNCTUACTION2");
	
	public static final DNode PAIR_LEFT= new DNode("PAIR_LEFT");
	
	public static final DNode PAIR_RIGHT = new DNode("PAIR_RIGHT");
	
	public static final DNode ESCAPE = new DNode("ESCAPE");
	
	public static final DNode TAB = new DNode("TAB");
	
	public static final DNode STRING = new DNode("STRING");
	 
	public static final DNode CHARACTER = new DNode("CHARACTER");

	public static final DNode CN_NUMBERING = new DNode("CN_NUMBERING");

	public static final DNode ROMEN_NUMBERING = new DNode("ROMEN_NUMBERING");

	public static final DNode CN_ORDERING = new DNode("CN_ORDERING");

	public static final DNode EN_NUMBERING = new DNode("EN_NUMBERING");

	public static final DNode UNKNOWN = new DNode("UNKNOWN");

	public static final DNode LIST_SPERATOR = new DNode("LIST_SPERATOR");

	public static final DNode BALL_NUMBERING = new DNode("BALL_NUMBERING");

	public static final DNode STOP = new DNode("STOP");
	
	public static final DNode KEYWORD_CATALOG = new DNode("KEYWORD_CATALOG");
	
	public static final DNode KEYWORD_APPENDIX = new DNode("KEYWORD_APPENDIX");

}

package cn.jintongsoft.nlp.parser.listitem;

import java.util.ArrayList;
import java.util.List;

import cn.jintongsoft.nlp.parser.Token;

public class RawListItem {
	
	public String listInfo;
	
	public int index;
	
	public List<Token> pattern;
	
	public int level;
	
	public String title;
	
	public List<NodePara> content = new ArrayList<NodePara>();
	
	public List<NodePara> errorCorrect = new ArrayList<NodePara>();
	
	public String toString(){
		return listInfo;
	}

}

class NodePara{
	
	public String title;
	
	public List<String> content = new ArrayList<String>();
	
}

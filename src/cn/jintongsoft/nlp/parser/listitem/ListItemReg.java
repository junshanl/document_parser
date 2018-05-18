package cn.jintongsoft.nlp.parser.listitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jintongsoft.utils.Paragraph;

public class ListItemReg {

	public static List<RawListItem> correct2(List<RawListItem> list) {
		List<RawListItem> correct = new ArrayList<RawListItem>();
		Map<Integer, Integer> map = new HashMap<>();

		RawListItem offset = new RawListItem();
		offset.level = -1;
		correct.add(offset);

		for (RawListItem li : list) {
			int index = li.index;
			int level = li.level;

			if (map.get(level) == null && index == 1) {
				map.put(level, index);
				correct.add(li);
			} else if (map.get(level) != null && index - map.get(level) == 1) {
				map.put(level, index);
				correct.add(li);
			} else if (index == 1) {
				map.put(level, index);
				correct.add(li);
			} else {
				RawListItem lastOne = correct.get(correct.size() - 1);
				NodePara node = li.content.get(0);
				node.title = li.listInfo + node.title;
				lastOne.errorCorrect.add(node);
				lastOne.errorCorrect.addAll(li.content.subList(1, li.content.size()));
			}
		}
		return correct;
	}

	public static Paragraph toParaprah(String intro, List<RawListItem> list) {
		Paragraph root = new Paragraph();
		root.setTitle(removeEnter(intro));
		
		List<Paragraph> rootParas = generateParaInSameLevel(list.get(0));
		
		for(Paragraph para  : rootParas){
			root.add(para);
		}
		
		
		Map<Integer, Paragraph> map = new HashMap<>();
		map.put(0, root);

		for (int i = 1; i < list.size(); i++) {
			RawListItem li = list.get(i);

			List<Paragraph> paras = generateParaInSameLevel(li);
			Paragraph represent = paras.get(0);
			
			
			represent.setLevel(li.level);
			if (li.title != null) {
				represent.setTitle(removeEnter(li.listInfo + " " + li.title));
			} else {
				represent.setTitle(removeEnter(li.listInfo));
			}

			int tmp = li.level - 1;
			Paragraph parent = map.get(tmp);
			while (parent == null) {
				parent = map.get(--tmp);
			}
			
			for(Paragraph para : paras){
				parent.add(para);
			}
			
			map.put(li.level, represent);

		}

		return root;
	}
	
	
	public static List<Paragraph> generateParaInSameLevel(RawListItem li){
		List<Paragraph> paras = new ArrayList<Paragraph>();
		
		for( NodePara node : li.content){
			Paragraph para = new Paragraph();
			para.setTitle(removeEnter(node.title));
			para.setContent(removeEnter(node.content));
			
			paras.add(para);
		}
		
		for( NodePara node : li.errorCorrect){
			Paragraph para = new Paragraph();
			para.setTitle(removeEnter(node.title));
			para.setContent(removeEnter(node.content));
			
			paras.add(para);
		}
		return paras;
	}

	public static String printParagraphStruct(Paragraph p) {
		String temp = "     " + p.getLevel() + "     " + p.getTitle() + "    " + p.getContent().toString();
		temp = temp.replaceAll("\n", "") + "\n";
		for (Paragraph sub_p : p.getSubParagraph()) {
			temp += printParagraphStruct(sub_p);
		}
		return temp;
	}
	
	public static String removeEnter(String str){
		if(str != null){
			return str.replace("\n", "");
		}else{
			return null;
		}
	}
	
	public static List<String> removeEnter(List<String> list){
		List<String> result = new ArrayList<String>();
		for(String str : list){
			result.add(str.replace("\n", ""));
		}
		return result; 
	}

}

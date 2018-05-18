package cn.jintongsoft.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Paragraph implements Serializable {

	private static final long serialVersionUID = 3656780872065214041L;

	private String title = null;
	private List<String> content = new ArrayList<>();
	private List<ListItem> items = new ArrayList<>();
	private List<Paragraph> list = new ArrayList<>();
	private int level = 0;
	private String documentTitle = null;
	private String titleWithSuperTitle = null;
	private String titleFromRoot = null;

	public Paragraph() {
	}

	public Paragraph(String title, List<String> content) {
		this.title = title;
		this.content = new ArrayList<String>(content);
	}

	public Paragraph(String title, List<String> content, int level) {
		this(title, content);
		this.level = level;
	}

	public Paragraph(String title, List<String> content, int level, String documentTitle) {
		this(title, content, level);
		this.documentTitle = documentTitle;
	}

	public String getTitleWithSuperTitle() {
		return titleWithSuperTitle;
	}

	public void setTitleWithSuperTitle(String titleWithSuperTitle) {
		this.titleWithSuperTitle = titleWithSuperTitle;
	}

	public String getTitleFromRoot() {
		return titleFromRoot;
	}

	public void setTitleFromRoot(String titleFromRoot) {
		this.titleFromRoot = titleFromRoot;
	}

	public void add(Paragraph paragraph) {
		this.list.add(paragraph);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(List<String> content) {
		this.content = new ArrayList<String>(content);
	}

	public void setListItem(List<ListItem> items) {
		this.items = new ArrayList<ListItem>(items);
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}

	public String getTitle() {
		return this.title;
	}

	public List<String> getContent() {
		return this.content;
	}

	public List<Paragraph> getSubParagraph() {
		return this.list;
	}

	public int getLevel() {
		return level;
	}

	public List<ListItem> getListItems() {
		return this.items;
	}

	public String getDocumentTitle() {
		return documentTitle;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("level:" + level + "\n");
		buffer.append("Title:" + title + "\n");
		buffer.append("TitleWithSuper:" + titleWithSuperTitle + "\n");
		buffer.append("TitleFromRoot:" + titleFromRoot + "\n");
		buffer.append("content:\n");
		if (content.isEmpty()) {
			buffer.append("empty\n");
		} else {
			for (String con : content) {
				buffer.append(con);
				buffer.append("\n");
			}
		}
		buffer.append("ListItem:\n");
		if (items.isEmpty()) {
			buffer.append("empty\n");
		}
		for (ListItem item : items) {
			buffer.append("\t");
			buffer.append(item.getTitle());
			buffer.append("\n");
		}
		buffer.append("sub_paragraph:\n");
		if (list.isEmpty()) {
			buffer.append("null\n");
		}
		for (Paragraph paragraph : list) {
			buffer.append("\t");
			buffer.append(paragraph.getTitle());
			buffer.append("\n");
		}
		buffer.append("\n");
		for (Paragraph paragraph : list)
			buffer.append(paragraph.toString());
		return buffer.toString();
	}
}

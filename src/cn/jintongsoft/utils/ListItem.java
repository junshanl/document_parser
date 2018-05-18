package cn.jintongsoft.utils;

import java.util.ArrayList;
import java.util.List;

public class ListItem {
	private String title = null;
	private List<String> content = new ArrayList<>();
	private List<ListItem> subItems = new ArrayList<ListItem>();

	public ListItem(String title, List<String> content) {
		this.title = title;
		this.content = new ArrayList<>(content);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = new ArrayList<String>(content);
	}

	public List<ListItem> getSubItems() {
		return subItems;
	}

	public void setSubItems(List<ListItem> subItems) {
		this.subItems = new ArrayList<ListItem>(subItems);
	}

	public void addSubItem(ListItem item) {
		this.subItems.add(item);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Title:");
		buffer.append(title);
		buffer.append("\n");
		buffer.append("content:\n");
		if (content.isEmpty()) {
			buffer.append("\t");
			buffer.append("empty\n");
		}
		for (String string : content) {
			buffer.append("\t");
			buffer.append(string);
			buffer.append("\n");
		}
		buffer.append("subList:\n");
		if (subItems.isEmpty()) {
			buffer.append("\t");
			buffer.append("empty\n");
		}
		for (ListItem item : subItems) {
			buffer.append("\t");
			buffer.append(item.toString());
			buffer.append("\n");
		}
		return buffer.toString();
	}
}

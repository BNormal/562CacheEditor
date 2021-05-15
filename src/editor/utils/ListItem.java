package editor.utils;

public class ListItem {
	private String title;
	private String data;

	ListItem(String title) {
		this.title = title;
		data = null;
	}

	public ListItem(String title, String data) {
		this.title = title;
		this.data = data;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}
}

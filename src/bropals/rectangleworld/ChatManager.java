package bropals.rectangleworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Graphics;

import java.util.ArrayList;

/**
	A chat box that can hold the last few lines of line it got.
*/
public class ChatManager {
	
	private List<String[]> messages;
	private char[] currentLine;
	private boolean chatting;
	private int maxLines;
	private int onChar;
	
	public ChatManager(int lines, int characterLimit) {
		chatting = false;
		maxLines = lines;
		messages = Collections.synchronizedList(new ArrayList<String[]>());
		currentLine = new char[characterLimit];
		onChar = 0;
	}
	
	public void drawChatManager(Graphics g) {
		
	}
	
	public boolean emptyLine() {
		return onChar==0;
	}
	
	/**
		Add a chat message to the list, storing the name of the one who said it
	*/
	public void getChatMessage(String name, String line) {
		messages.add(new String[] { name, line });
		if (messages.size() > maxLines) {
			messages.remove(messages.size()-1);
		}
	}
	
	public String[] getMessage(int index) {
		return messages.get(index);
	}
	
	private void clearCharacterArray() {
		onChar = 0;
	}
	
	public void startChatting() {
		chatting = true;
		onChar = 0;
	}
	
	public void backspace() {
		onChar--;
		if (onChar < 0) {
			cancelChat();
		}
	}
	
	public void typeKey(char key) {
		if (isChatting()) {
			if (onChar < currentLine.length) {
				currentLine[onChar] = key;
				onChar++;
			}
		}
	}
	
	public boolean isChatting() {
		return chatting;
	}
	
	public String getTypedLine() {
		String str = "";
		for (int i=0; i<onChar; i++) {
			str += currentLine[i];
		}
		return str;
	}
	
	private void cancelChat() {
		chatting = false;
		onChar = 0;
	}
}
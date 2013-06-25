package chatsys.client.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.border.Border;

public class ToolsPanel extends JPanel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6291849779610341292L;
	private JButton emoji;
	private JButton font;
	private JButton color;

	public ToolsPanel(){
		this.setLayout(new FlowLayout(FlowLayout.LEADING,20,2));
		this.setBackground(new Color(244, 249, 254));
		Border line = BorderFactory.createLineBorder(new Color(74,133, 213));
		this.setBorder(line);
		ImageIcon icon=new ImageIcon("/images/image/font.gif");
		font=new JButton(icon);
		font.setBorderPainted(false);
		font.setFocusPainted(false);
		font.setMargin(new Insets(0,0,0,0));
		font.setActionCommand("Font");
		
		icon=new ImageIcon("/images/image/color.gif");
		color=new JButton(icon);
		color.setBorderPainted(false);
		color.setFocusPainted(false);
		color.setMargin(new Insets(0,0,0,0));
		color.setActionCommand("Color");
		
		icon=new ImageIcon("/images/image/emoji.gif");
		emoji=new JButton(icon);
		emoji=new JButton(icon);
		emoji.setBorderPainted(false);
		emoji.setFocusPainted(false);
		emoji.setMargin(new Insets(0,0,0,0));
		emoji.setActionCommand("Emoji");
		
		this.add(font);
		this.add(color);
		this.add(emoji);
		this.setSize(new Dimension(5,5));
	}

	public JButton getColorButton() {
		return color;
	}

	public JButton getEmojiButton() {
		return emoji;
	}

	public JButton getFontButton() {
		return font;
	}
	
}

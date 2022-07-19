package main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame
{
	private static final long serialVersionUID = 1L;

	private SplitterPan splitterPan = new SplitterPan();
	
	public Window(int w, int h)
	{
		super();
		this.setPreferredSize(new Dimension(w, h));
		this.setTitle("Image splitter");
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(w, h));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(splitterPan);
		
		this.setVisible(true);
	}
	
}

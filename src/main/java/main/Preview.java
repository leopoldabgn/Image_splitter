package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Preview extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private Image img;
	private int c, l;
	private SplitterPan splitterPan;
	
	private Timer timer = new Timer(100, this);
	
	public Preview(SplitterPan splitterPan, String imgPath, int w, int h)
	{
		this.splitterPan = splitterPan;
		this.setPreferredSize(new Dimension(w, h+50));
		Image img = new ImageIcon(imgPath).getImage();
		this.img = img;
	
	    timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		repaint();
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(img == null)
			return;
		Dimension dim = resize(img, this.getWidth(), this.getHeight());
		g.drawImage(img, 0, 0, (int)dim.getWidth(), (int)dim.getHeight(), null);
		
		int[] val = splitterPan.getValues();
		Point[] lines = getLines(dim, val[0], val[1]);
		if(lines == null)
			return;
		int size = 10;
		g.setColor(Color.RED);
		//System.out.println("IMG (w : "+dim.getWidth()+" h : "+dim.getHeight()+") :\nLines -> ");
		for(Point p : lines)
		{
			if(p.getX() == 0)
				g.fillRect((int)p.getX(), (int)p.getY(), (int)dim.getWidth(), size);
			else
				g.fillRect((int)p.getX(), (int)p.getY(), size, (int)dim.getHeight());
			
			//System.out.println("\tx : "+p.getX()+" y : "+p.getY());
		}
	}
	
	public Point[] getLines(Dimension dim, int c, int l)
	{
		if(c <= 1 || l <= 1)
			return null;
		Point[] lines = new Point[(c-1)+(l-1)];
		int w = (int) Math.floor((float)dim.getWidth()/c);
		int h = (int) Math.floor((float)dim.getHeight()/l);
		int index = 0;
		for(int j=0;j<(c-1);j++)
		{
			index += w;
			lines[j] = new Point(index, 0);
		}
		index = 0;
		
		for(int i=0;i<(l-1);i++)
		{
			index += h;
			lines[(c-1)+i] = new Point(0, index);
		}
		
		return lines;
	}
	
	public static Dimension resize(Image pic, int w_max, int h_max)
	{
		int w, h;
		if(pic.getWidth(null) > w_max)
			w = w_max;
		else
			w = pic.getWidth(null);
		
		h = pic.getHeight(null);
		float coeff = (float)w / (float)pic.getWidth(null);
		h *= coeff;
		
		if(h > h_max)
		{
			coeff = (float)h_max / (float)h;
			h = h_max;
			w *= coeff;
		}	
		return new Dimension(w, h);
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}
	
	public void setImg(String path) {
		this.img = new ImageIcon(path).getImage();
	}

}

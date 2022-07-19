package main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SplitterPan extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JPanel pan1 = new JPanel(),
				   pan2 = new JPanel(),
				   pan3 = new JPanel();
	
	private JButton parcourir = new JButton("Parcourir..."),
					validate = new JButton("Validate");
	
	private JLabel path = new JLabel();
	
	private JTextPane edit1 = new JTextPane(),
					  edit2 = new JTextPane();
	
	private JSlider slider1 = new JSlider(),
					slider2 = new JSlider();
	
	private Preview previewPan;
	
	public SplitterPan()
	{
		super();
		
		this.setLayout(new GridLayout(2,1));
		
		pan1.add(new JLabel("Select an image :"));
		pan1.add(path);
		pan1.add(parcourir);
		
		edit1.setPreferredSize(new Dimension(35,22));
		edit1.setText("3");
		edit2.setPreferredSize(new Dimension(35,22));
		edit2.setText("3");
	    slider1.setMaximum(9);
	    slider1.setMinimum(1);
	    slider1.setValue(3);
	    slider1.setPaintTicks(true);
	    slider1.setPaintLabels(true);
	    slider1.setMinorTickSpacing(1);
	    slider1.setMajorTickSpacing(2);
	    slider2.setMaximum(9);
	    slider2.setMinimum(1);
	    slider2.setValue(3);
	    slider2.setPaintTicks(true);
	    slider2.setPaintLabels(true);
	    slider2.setMinorTickSpacing(1);
	    slider2.setMajorTickSpacing(2);
		pan2.add(edit1);
		pan2.add(new JLabel("x"));
		pan2.add(edit2);
		pan2.add(slider1);
		pan2.add(slider2);
		
		pan3.add(validate);		
		
		this.add(pan1);
		this.add(pan2);
		this.add(pan3);
		
		SplitterPan self = this;
		
		parcourir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				File f = new File(getImgPath());

				if(!isImg(f))
					return;
				
				path.setText(f.getAbsolutePath());
				
				if(containsComponent(self, previewPan))
					self.remove(previewPan);
				
				previewPan = new Preview(self, f.getAbsolutePath(), 350, 350);
				self.add(previewPan);
			}
		});
		
		validate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!path.getText().equals(""))
					saveImages(cutImg(path.getText(), Integer.parseInt(edit1.getText()), 
							   Integer.parseInt(edit2.getText())));
			}
		});
		
		slider1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e){
				edit1.setText(""+((JSlider)e.getSource()).getValue());
			}
		});
		
		slider2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e){
				edit2.setText(""+((JSlider)e.getSource()).getValue());
			}
		});
		
	}
	
	public static String getFileExtension(File f)
	{
		if(f.isFile())
		{
			String ext = f.getName().substring(f.getName().lastIndexOf(".")+1).toUpperCase();
			return ext;
		}
		return null;
	}
	
	public static boolean isImg(File f)
	{
		if(f.isFile())
		{
			String ext = getFileExtension(f);
			if(ext.equals("JPG") || ext.equals("PNG")) // || ext.equals("ICO"))
					return true;
		}
		return false;
	}
	
	public String getImgPath()
	{
		JFileChooser choice = new JFileChooser();
		String path = "";
		int var = choice.showOpenDialog(this);
		if(var==JFileChooser.APPROVE_OPTION)
		{
		   //choice.getSelectedFile().getName();
		   path = choice.getSelectedFile().getAbsolutePath();
		   if(!path.contains("."))
			   path += ".jpg";
		}
		
		return path;
	}
	
	public boolean containsComponent(Container container, Component component) 
	{
		for (Component containedComponent : container.getComponents()) 
			if (containedComponent == component) 
				return true;

		return false;
	}
	
	public int[][] getMatrixOfImage(String imgPath)
	{
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(imgPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(img == null)
			return null;
		
		int w = img.getWidth();
		int h = img.getHeight();
		int[][] matrix = new int[w][h];
		for(int j=0;j<w;j++)
			for(int i=0;i<h;i++)
				matrix[j][i] = img.getRGB(j, i);
				
		return matrix;
	}
	
	public BufferedImage getImageByMatrix(int[][] matrix, int c, int l, int w, int h)
	{
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		
		
		for(int j=0;j<w;j++)
			for(int i=0;i<h;i++)
				img.setRGB(j, i, matrix[(c*(w-1))+j][(l*(h-1))+i]);
		
		return img;
	}
	
	public BufferedImage[] cutImg(String imgPath, int c, int l)
	{
		int[][] matrix = getMatrixOfImage(imgPath);
		if(matrix == null || (c <= 0 || l <= 0))
			return null;
		BufferedImage[] images = new BufferedImage[c*l];
		Image img = new ImageIcon(imgPath).getImage();
		int w = (int) Math.floor((float)img.getWidth(null)/c);
		int h = (int) Math.floor((float)img.getHeight(null)/l);
		
		System.out.println("w: "+w+" h: "+h);
		
		/*for(int j=0;j<c;j++)
		{
			for(int i=0;i<l;i++)
			{
				images[(j*l)+i] = getImageByMatrix(matrix, j, i, w, h);
			}
		}*/
		
		for(int j=0;j<l;j++)
		{
			for(int i=0;i<c;i++)
			{
				images[(j*c)+i] = getImageByMatrix(matrix, i, j, w, h);
			}
		}
		
		
		return images;
	}
	
	public void saveImages(BufferedImage[] images)
	{
		if(images == null)
			return;
		File folder = new File(getFolderPath("images/img"));
		if(!folder.exists() || folder.isFile())
			folder.mkdirs();
			
		for(int i=0;i<images.length;i++)
		{
			try {
				ImageIO.write(images[i], "jpg", new File(folder.getAbsolutePath()+"\\"+(i+1)+".jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void deleteFiles(File folder)
	{
		File[] list = folder.listFiles();
		if(list == null)
			return;
		
		for(File f : list)
		{
			if(f.isDirectory())
				deleteFiles(f);
			else
				f.delete();
		}
		
	}
	
	public String getFolderPath(String path)
	{
		File folder = new File(path), folder2;
		int index = 0;
		if(!folder.exists() || folder.isFile())
			return folder.getAbsolutePath();
		
		do
		{
			index++;
			folder2 = new File(folder.getAbsolutePath()+index);
		}while(folder2.isDirectory());
		
		System.out.println(folder2);
		
		return folder2.getAbsolutePath();
	}
	
	public int[] getValues()
	{
		int[] val = new int[2];
		try {
			val[0] = Integer.parseInt(edit1.getText());
			val[1] = Integer.parseInt(edit2.getText());
		}
		catch(Exception e) {
			val[0] = 3;
			val[1] = 3;
		}
		return val;
	}
	
}

import java.awt.image.WritableRaster;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;


public class Flowfields{
	public static void main(String[] args) throws IOException {
		drawfield(600, 800);
	}
	
	public static void drawfield(int w, int h) {

		Frame f = new Frame();
		f.setBounds(0, 0, w, h);
		f.setBackground(Color.white);
		Canvas test = new Canvas();
		test.setBounds(0, 0, w, h);
		test.setBackground(Color.black);
		test.setVisible(true);
		f.add(test);
		f.setVisible(true);
		BufferedImage us=new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g=test.getGraphics();
		g.drawImage(us, 0, 0, null);
		g.setColor(Color.cyan);
		double[][] d=new double[100][75];//75x100
		//lots of turning in the 24x24 box near origin
		double pi=Math.PI;
		for (int i=0; i<24; i++) {
		//d[i]=[];
		  for (int j=0; j<24; j++) {
		    d[i][j]=(pi/12-(i*(pi/288))+(j*(pi/288)))/4;
		  }
		}
		for (int i=0; i<24; i++) {
		  for (int j=24; j<75; j++) {
		    d[i][j]=pi/80-(i/1920);
		  }
		}
		for (int i=24; i<62; i++) {
		   // d[i]=[];
		  for (int j=0; j<75; j++) {
		    d[i][j]=-pi/48;
		  }
		}
		for (int i=62; i<90; i++) {
		   // d[i]=[];
		  for (int j=0; j<75; j++) {
		    d[i][j]=(pi/70)*(j/18);
		  }
		}
		for (int i=90; i<100; i++) {
		//d[i]=[];
		  for (int j=0; j<75; j++) {
		    d[i][j]=(-pi/70)*((24-j)/18);
		  }
		}
		double [][] e= new double[100][75];
		for (int i=0; i<100; i++) {
			for (int j=0; j<75; j++) {
				double dist=Math.sqrt((37-i)*(37-i)+(63-j)*(62-j));
				dist/=900;
				e[i][j]=-0.10+dist;
				//center roughly 63 37
				if (i<30 && j<40) e[i][j]*=1.5;
				else if (i<30) e[i][j]*=-1;
			}
		}
		double[][] r = new double[100][75];
		for (int i=0; i<100; i++) {
			for (int j=0; j<75; j++) {
				r[i][j]=(Math.random()-0.3)/5;
			}}
		//double[] lines= {4.73, 4.8, 5, 4.852, 4.853, 4.88, 4.871, 4.91};
		double[] lines=new double[20];
		for (int i=0; i<lines.length; i++) {
			double b=Math.random()*6.27;
			//while (b>1.5 && b<5.7) b=Math.random()*6.27;
			lines[i]=b;
		}
		BufferedImage bu = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		for (int i=0; i<w; i++) {
			for (int j=0; j<h; j++) {
				int[] al=new int[4];
				al=bu.getRaster().getPixel(i, j, al);
				al[3]=0;
				bu.getRaster().setPixel(i, j, al);
				us.getRaster().setPixel(i, j, al);
			}
		}
		for (int l=0; l<lines.length; l++) {
		  double x=400;
		  double y=400;
		  if (l>0) bu=us;
		  int iter=0;
		  while (x<w && y<h && x>0 && y>0) {
		    int rx=(int)x; int ry=(int)y;
		    double theta=d[(int)(y/8)][(int)(x/8)];
		    lines[l]+=theta;
		    if (lines[l]>=Math.PI*2) lines[l]=0;
		    x+=12*Math.cos(lines[l]);
		    y-=12*Math.sin(lines[l]);
		    Point[] p=line2(rx, ry, (int)x, (int)y);
		    
		    for (Point px : p) {
		    	int[] color = {180-rx/4, 0, 255-ry/4, 255};
		    	if (check(px.x, px.y, us)) us.getRaster().setPixel(px.x, px.y, color);
		    	for (int i=-40; i<=40; i++) {
		    		int[] colt={180-rx/4, 0, 255-ry/4, 500/(Math.abs(i)+1)+25};
		    		if (colt[3]>255) colt[3]=255;
		    		if (check(px.x+i, px.y, us)) {
		    			int[] oc=new int[4];
		    			oc=bu.getRaster().getPixel(px.x+i, px.y, oc);
		    			if (oc[0]==0 && oc[1]==0 && oc[2]==0) oc[3]=0;
		    			oc[3]=(colt[3]+oc[3])/2;
		    			if (oc[3]>255) oc[3]=255;
		    			oc[0]=(oc[0]+colt[0])/2;
		    			oc[1]=(oc[1]+colt[1])/2;
		    			oc[2]=(oc[2]+colt[2])/2;
		    			us.getRaster().setPixel(px.x+i, px.y, oc);
		    			
		    		}
		    		if (check(px.x, px.y+i, us)) {
		    			int[] oc=new int[4];
		    			oc=bu.getRaster().getPixel(px.x, px.y+i, oc);
		    			if (oc[0]==0 && oc[1]==0 && oc[2]==0) oc[3]=0;
		    			//oc[3]=oc[3]>colt[3]?oc[3]:colt[3];
		    			oc[3]=(colt[3]+oc[3])/2;
		    			if (oc[3]>255) oc[3]=255;
		    			oc[0]=(oc[0]+colt[0])/2;
		    			oc[1]=(oc[1]+colt[1])/2;
		    			oc[2]=(oc[2]+colt[2])/2;
		    			us.getRaster().setPixel(px.x, px.y+i, oc);
		    		}
		    	}
		    }
		   g.setColor(new Color(180-rx/4, 0, 255-ry/4));
		   g.drawLine(rx, ry, (int)x, (int)y);
		  }
		 
		}
		
		g.drawImage(us, 0, 0, null);
	
	}
	
	public static boolean check(int x, int y, BufferedImage img) {
		return (x >= 0 && y >= 0 && x < img.getWidth() && y < img.getHeight());
	}

	public static Point[] line2(int startx, int starty, int endx, int endy) {
		Point start=new Point(startx, starty);
		Point end=new Point(endx, endy);
		return line(start, end);
	}
	
	public static Point[] line(Point start, Point end) {
		int size=Math.abs(start.x-end.x)>Math.abs(start.y-end.y) ? Math.abs(start.x-end.x): Math.abs(start.y-end.y);
		Point[] line = new Point[size];
		if (end.x==start.x) {
			int starty=start.y<end.y? start.y:end.y;
			for (int i=0; i<size; i++) {
				line[i]=new Point(start.x, starty+i);
			}
			return line;
		}
		for (int i=0; i<size; i++) {
			double m=(end.x-start.x)*1.0;
			double n=(end.y-start.y)*1.0;
			m/=size; n/=size;
			line[i]=new Point(start.x+(int)(i*m), start.y+(int)(i*n));
		}
		return line;
		
	}
	
}
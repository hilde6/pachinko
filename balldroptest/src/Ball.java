import java.awt.*;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Random;

import javax.swing.*;

@SuppressWarnings("serial")
public class Ball extends JPanel{

	JFrame f;
	double x=500;
	double y=0;
	double x_vel=0;
	double y_vel=0;
	
	static double grav=0.008;
	static double cor=0.8;
	static int balld=30;
	static int fheight=1000;
	static int fwidth=965;
	static int n_pegs_x=18;
	static int n_pegs_y=15;
	static int space=55;
	
	static int pegd=10;
	static Cord[][] peg = new Cord[n_pegs_x][n_pegs_y];
	int[] bin = new int[n_pegs_x-1];
	int hcount=0;
	int tcount=0;
	
	public static void main(String [] args)
	{
		Ball b = new Ball();
		JFrame f=new JFrame();
		f.add(b);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(fwidth,fheight);
		f.setVisible(true);
		showOnScreen(1,f);
		f.getContentPane().addMouseListener(new ballML(b));
		
		//set up pegs
		for(int i=0; i<n_pegs_x; i++) {
			for(int j=0; j<n_pegs_y; j++) {
				if(j%2==1) {
					peg[i][j] = new Cord(i*space-25+space,j*space+space);
				} else {
					peg[i][j] = new Cord(i*space-25+space/2,j*space+space);
				}
			}
		}

		b.randx();
		
		while(true)
		{
			try{
			Thread.sleep(1);
			} catch(Exception e){
			}
			b.move_it();	
			f.repaint();
		}
	}
	
	public void randx() {
		Random rand = new Random();
		Random flip = new Random();
//		x=rand.nextInt(fwidth-15-balld);
		x=(fwidth-15)/2-balld/2;
		//add small random x velocity
		x_vel=0.01/rand.nextInt(100)+0.01;
		if(flip.nextInt(2)==0) {
			x_vel*=-1;
		}
	}

	void move_it()
	{
		if( y+balld<fheight-40 ) //gravity
		{
			y_vel+=grav;
		} else if( y_vel>0 ) // floor collision
		{
//			y_vel*=-cor;
			
			findbin(x);
			System.out.println(Arrays.toString(bin));

			y=0;
			y_vel=0;
			randx();
		}

		if( (x+balld>=fwidth-15 && x_vel>0) || (x<=0 && x_vel<0) ) //wall collision
		{ 
			x_vel*=-cor;
		}
		
		for(int i=0;i<n_pegs_x;i++) {
			if(y+balld/2>peg[i][n_pegs_y-1].getY()+pegd/2 && x<peg[i][n_pegs_y-1].getX() && x+balld>=peg[i][n_pegs_y-1].getX() && x_vel>0) {
				x_vel*=-cor;
			} else if (y+balld/2>peg[i][n_pegs_y-1].getY()+pegd/2 && x>=peg[i][n_pegs_y-1].getX() && x<=peg[i][n_pegs_y-1].getX()+pegd && x_vel<0) {
				x_vel*=-cor;
			}
		}	

		for(int i=0;i<n_pegs_x;i++) {
			for(int j=0;j<n_pegs_y;j++) {
				if( Math.sqrt(Math.pow(y+balld/2-(peg[i][j].getY()+pegd/2),2)+Math.pow(x+balld/2-(peg[i][j].getX()+pegd/2), 2)) < (balld+pegd)/2 ) {
					double v_tot=Math.sqrt(Math.pow(y_vel, 2)+Math.pow(x_vel, 2));
					y_vel=v_tot*cor*(y+balld/2-peg[i][j].getY()-pegd/2)/((balld+pegd)/2);
					x_vel=v_tot*cor*(x+balld/2-peg[i][j].getX()-pegd/2)/((balld+pegd)/2);
				}
			}
		}
	
		y+=y_vel;
		x+=x_vel;
	}
	
	public void findbin(double x)
	{
		double dist = Math.abs(peg[0][n_pegs_y-1].getX()-x);
		int idx=0;
		for(int i=0;i<n_pegs_x-1;i++) {
			double tmpdist = Math.abs(peg[i][n_pegs_y-1].getX()-x);
			if(tmpdist<dist) {
				idx=i;
				dist=tmpdist;
			}
		}
		
		if(x>peg[idx][n_pegs_y-1].getX()) {
			bin[idx]++;
		} else {
			bin[idx+1]++;
		}
	}

	public void paintComponent(Graphics g)
	{
		int xint=(int)Math.round(x);
		int yint=(int)Math.round(y);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		//color ball
		g.setColor(Color.GREEN);
		g.fillOval(xint, yint, balld, balld);
	
		//color pegs
		g.setColor(Color.GRAY);
		for(int i=0;i<n_pegs_x;i++) {
			for(int j=0;j<n_pegs_y;j++) {
				g.fillOval(peg[i][j].getX(),peg[i][j].getY(),pegd,pegd);
			}
		}
		
		//color bins
		for(int i=0;i<n_pegs_x;i++) {
			g.fillRect(peg[i][n_pegs_y-1].getX(),peg[i][n_pegs_y-1].getY()+pegd/2,pegd,fheight-40-(peg[i][n_pegs_y-1].getY()+pegd/2));
		}
	}

	public static void showOnScreen( int screen, JFrame frame ) {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice[] gd = ge.getScreenDevices();
	    if( screen > -1 && screen < gd.length ) {
	        frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, frame.getY());
	    } else if( gd.length > 0 ) {
	        frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
	    } else {
	        throw new RuntimeException( "No Screens Found" );
	    }
	}
}
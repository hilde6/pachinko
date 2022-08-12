import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ballML implements MouseListener {

	private Ball b;
	
	public ballML(Ball b) {
		super();
		this.b=b;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		b.randx();
		b.x=e.getX()-b.balld/2;
		b.x_vel=0;
		//b.y=0;
		b.y=e.getY()-b.balld/2;
		b.y_vel=0;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

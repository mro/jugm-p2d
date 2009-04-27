/**
 * 
 */
package mobi.alsw.jugm.hampel;

import java.awt.geom.Point2D;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PImage;

/**
 * Demo for the talk http://alsw.mobi/go/jugm
 * 
 * @author mro@alsw.mobi
 */
public class Driver {

	private static class Controller extends PBasicInputEventHandler {
		private final int dy = 300;

		private final PNode linker_arm, linkes_bein, linker_fuss;
		private final Point2D p0 = new Point2D.Double();
		private double pull = -1;
		private double pull0 = 0;
		private final PNode rechter_arm, rechtes_bein, rechter_fuss;
		private final PNode torso, kopf, hand;

		public Controller(final PNode view) {
			torso = add(view, 0, "/torso.png");
			torso.translate(200, 200);
			hand = add(view, 1, "/hand.png");
			kopf = add(torso, 0, "/kopf.png");
			linker_arm = add(torso, 1, "/linker_arm.png");
			linkes_bein = add(torso, 2, "/linkes_bein.png");
			linker_fuss = add(linkes_bein, 0, "/linker_fuss.png");
			rechter_arm = add(torso, 3, "/rechter_arm.png");
			rechtes_bein = add(torso, 4, "/rechtes_bein.png");
			rechter_fuss = add(rechtes_bein, 0, "/rechter_fuss.png");
			setPull(0);
		}

		public double getPull() {
			return pull;
		}

		public void mouseDragged(final PInputEvent arg0) {
			arg0.setHandled(true);
			final double _dy = arg0.getPosition().getY() - p0.getY();
			setPull(pull0 + _dy / dy);
		}

		public void mousePressed(final PInputEvent arg0) {
			p0.setLocation(arg0.getPosition());
			pull0 = pull;
			// super.mousePressed(arg0);
		}

		public void setPull(double pull) {
			if (pull < 0)
				pull = 0;
			if (pull > 1)
				pull = 1;
			if (Math.abs(this.pull - pull) < 1e-3)
				return;
			this.pull = pull;
			// System.out.println(pull);
			// do the mechanics:
			hand.setOffset(250, 700 + this.pull * dy - dy);
			kopf.setOffset(25, -100);

			double alpha = pull * Math.PI * 0.66;
			linker_arm.setOffset(30, 10);
			linker_arm.setRotation(alpha);
			linker_arm.translate(-150, -10);

			rechter_arm.setOffset(120, 10);
			rechter_arm.setRotation(-alpha);
			rechter_arm.translate(-0, -10);

			alpha = pull * Math.PI * 0.66;
			linkes_bein.setOffset(20, 160);
			linkes_bein.setRotation(alpha);
			linkes_bein.translate(-50, 10);

			linker_fuss.setOffset(20, 60);
			linker_fuss.setRotation(-alpha);
			linker_fuss.translate(-60, 20);

			rechtes_bein.setOffset(120, 175);
			rechtes_bein.setRotation(-alpha);
			rechtes_bein.translate(-30, -10);

			rechter_fuss.setOffset(50, 60);
			rechter_fuss.setRotation(alpha);
			rechter_fuss.translate(-20, 20);
		}
	}

	private static class MyException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public MyException(final String name) {
			super("Trouble wiring up \"" + name + "\"");
		}
	}

	private static final PNode add(final PNode parent, final int index,
			final String file) {
		final PNode ret = parent.getChild(index);
		if (!ret.getAttribute("file").equals(file))
			throw new MyException(file);
		return ret;
	}

	private static final PNode createScene() {
		final PNode torso;
		PNode tmp;
		final PNode universe = new PNode();
		universe.addChild(torso = load("/torso.png"));
		universe.addChild(load("/hand.png"));
		torso.addChild(load("/kopf.png"));
		torso.addChild(load("/linker_arm.png"));
		torso.addChild(tmp = load("/linkes_bein.png"));
		tmp.addChild(load("/linker_fuss.png"));
		torso.addChild(load("/rechter_arm.png"));
		torso.addChild(tmp = load("/rechtes_bein.png"));
		tmp.addChild(load("/rechter_fuss.png"));
		return universe;
	}

	private static final PImage load(final String name) {
		try {
			final PImage p = new PImage(ImageIO.read(Driver.class
					.getResourceAsStream(name)));
			p.addAttribute("file", name);
			return p;
		} catch (final Exception e) {
			throw new RuntimeException("Failed to load \"" + name + "\"", e);
		}
	}

	/**
	 * Wire up the ZUI and launch.
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final PCanvas canvas = new PCanvas();
		frame.add(canvas);
		final PNode world = createScene();
		canvas.getLayer().addChild(world);
		canvas.addInputEventListener(new Controller(world));

		frame.setSize(600, 700);
		frame.setVisible(true);
	}
}

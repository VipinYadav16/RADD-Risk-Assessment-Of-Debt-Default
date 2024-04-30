import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public class Frame1 extends JFrame {

    private Ellipse2D.Double circle;
    private Color circleColor = new Color(0, 0, 0, 0); // Initial transparent color

    public Frame1() {
        setTitle("Risk Assessment Of Debt Default");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load the background image
        ImageIcon backgroundIcon = new ImageIcon("img.png");
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(screenSize.width, screenSize.height,
                Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundIcon = new ImageIcon(backgroundImage);
        JLabel backgroundLabel = new JLabel(scaledBackgroundIcon) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(circleColor);
                g2d.fill(circle);
            }
        };
        add(backgroundLabel);

        // Define the invisible circle's position and size
        int circleX = 750; // X coordinate of the circle's center
        int circleY = 350; // Y coordinate of the circle's center
        int circleRadius = 180; // Radius of the circle

        // Create the circle shape
        circle = new Ellipse2D.Double(circleX - circleRadius, circleY - circleRadius, circleRadius * 2,
                circleRadius * 2);
        backgroundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (circle.contains(e.getPoint())) {
                    System.out.println("Invisible Circle Clicked! Go to Next Page.");
                    // Create and show Frame2
                    SwingUtilities.invokeLater(() -> new Frame2().setVisible(true));
                    // Hide Frame1 if needed
                    setVisible(false);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Frame1().setVisible(true));
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class Frame2 extends JFrame {

    private Ellipse2D.Double smallCircle;
    private RoundRectangle2D.Double textBox;
    private Color boxColor = new Color(0, 0, 0, 100); // Semi-transparent color for the box
    private Color circleColor = new Color(0, 0, 0, 100); // Semi-transparent color for the circle

    public Frame2() {
        setTitle("About");
        // Set the size of the frame to the screen size
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load the background image
        ImageIcon backgroundIcon = new ImageIcon("img.png");
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundIcon = new ImageIcon(backgroundImage);
        JLabel backgroundLabel = new JLabel(scaledBackgroundIcon) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Draw the semi-transparent box
                g2d.setColor(boxColor);
                g2d.fill(textBox);

                // Draw the heading "About Us" with underline
                g2d.setColor(Color.WHITE);
                Font headingFont = new Font("Arial", Font.PLAIN, 34);
                FontMetrics fm = g2d.getFontMetrics(headingFont);
                g2d.setFont(headingFont);
                String heading = "About Us";
                int headingWidth = fm.stringWidth(heading);
                int headingX = (int) textBox.getX() + ((int) textBox.getWidth() - headingWidth) / 2;
                int headingY = (int) textBox.getY() + 40;
                g2d.drawString(heading, headingX, headingY);

                // Draw underline manually
                int underlineY = headingY + fm.getDescent() + 5; // Adjust the position as needed
                int underlineX1 = headingX - 5; // Adjust the start position as needed
                int underlineX2 = headingX + headingWidth + 5; // Adjust the end position as needed
                g2d.drawLine(underlineX1, underlineY, underlineX2, underlineY);

                // Draw the text on the box
                g2d.setFont(new Font("Arial", Font.PLAIN, 22));
                String paragraph = "Our project, Risk Assessment of Debt Default (RADD), is a testament to our commitment to leveraging advanced machine learning techniques for practical, real-world applications.\n" +
                        "\n" +
                        "Our team believes in the power of data-driven decision making. Our objective is to develop a predictive model using logistic regression that accurately assesses the risk of debt default.\n" +
                        "\n" +
                        "We understand the critical role that credit risk management plays in the financial sector. Our model, RADD, is designed to enhance the ability of banks, credit agencies, and independent financial entities to manage credit risk effectively. By providing a more accurate prediction of loan defaults, RADD serves as a reliable decision support tool, minimizing the risk of bad debt and contributing to the overall stability of the financial system.\n" +
                        "\n" +
                        "Our approach is rooted in the core assumptions of logistic regression, ensuring that our model is robust, reliable, and accurate. We utilize training datasets to train our model, implement a loss function to quantify inaccuracies, and allow for iterative optimization to improve prediction accuracy over time.\n" +
                        "\n" +
                        "Join us on our journey to transform credit risk management with RADD, where data meets decision-making.";

                drawWrappedText(g2d, paragraph, (int) textBox.getX() + 20, (int) textBox.getY() + 80, (int) textBox.getWidth() - 40, (int) textBox.getHeight() - 80);

                // Draw "Next" inside the small semi-transparent circle
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.PLAIN, 22));
                FontMetrics fmNext = g2d.getFontMetrics();
                String nextText = "Next";
                int nextTextWidth = fmNext.stringWidth(nextText);
                int nextTextX = (int) (smallCircle.getCenterX() - nextTextWidth / 2);
                int nextTextY = (int) (smallCircle.getCenterY() + fmNext.getAscent() / 2);
                g2d.drawString(nextText, nextTextX, nextTextY);

                // Draw the small semi-transparent circle
                g2d.setColor(circleColor);
                g2d.fill(smallCircle);
            }
        };
        add(backgroundLabel);

        // Define the semi-transparent box's size and border radius
        int boxWidth = 1000; // Width of the box
        int boxHeight = 600; // Height of the box
        int borderRadius = 80; // Border radius of the box

        // Calculate the position of the box to center it
        int boxX = (getWidth() - boxWidth) / 2; // X coordinate of the box's top-left corner
        int boxY = (getHeight() - boxHeight) / 2; // Y coordinate of the box's top-left corner

        // Create the box shape
        textBox = new RoundRectangle2D.Double(boxX, boxY, boxWidth, boxHeight, borderRadius, borderRadius);

        // Define the small circle's position and size
        int circleX = boxX + boxWidth - 60; // X coordinate of the circle's center
        int circleY = boxY + boxHeight - 60; // Y coordinate of the circle's center
        int circleRadius = 40; // Radius of the circle

        // Create the small circle shape
        smallCircle = new Ellipse2D.Double(circleX - circleRadius, circleY - circleRadius, circleRadius * 2, circleRadius * 2);

        // Add a mouse listener to the background label for the small circle
        backgroundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (smallCircle.contains(e.getPoint())) {
                    System.out.println("Small Circle Clicked! Go to Next Page.");

                    // Create and show Frame4
                    SwingUtilities.invokeLater(() -> {
                        Frame3 frame4 = new Frame3();
                        frame4.setVisible(true);
                    });

                    // Hide Frame3
                    setVisible(false);
                }
            }
        });
    }

    // Function to draw wrapped text
    private void drawWrappedText(Graphics2D g2d, String text, int x, int y, int maxWidth, int maxHeight) {
        FontMetrics fm = g2d.getFontMetrics();
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder(words[0]);
        int lineHeight = fm.getHeight();
        int currentY = y + lineHeight;
        for (int i = 1; i < words.length; i++) {
            if (fm.stringWidth(currentLine.toString() + " " + words[i]) <= maxWidth) {
                currentLine.append(" ").append(words[i]);
            } else {
                if (currentY + lineHeight <= y + maxHeight) {
                    g2d.drawString(currentLine.toString(), x, currentY);
                    currentLine = new StringBuilder(words[i]);
                    currentY += lineHeight;
                } else {
                    break; // Stop drawing if maxHeight is reached
                }
            }
        }
        // Draw the last line
        if (currentY + lineHeight <= y + maxHeight) {
            g2d.drawString(currentLine.toString(), x, currentY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Frame2().setVisible(true));
    }
}

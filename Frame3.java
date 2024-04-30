import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class Frame3 extends JFrame {

    private Ellipse2D.Double smallCircle;
    private RoundRectangle2D.Double topRectangle;
    private RoundRectangle2D.Double[] textBoxes = new RoundRectangle2D.Double[5];
    private Color boxColor = new Color(0, 0, 0, 100); // Semi-transparent color for the boxes
    private Color circleColor = new Color(0, 0, 0, 100); // Semi-transparent color for the circle

    public Frame3() {
        setTitle("Semi-Transparent Boxes with Text");
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

                // Draw the top center semi-transparent box
                g2d.setColor(boxColor);
                g2d.fill(topRectangle);

                // Draw the text on the top box
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.PLAIN, 28));
                FontMetrics fm = g2d.getFontMetrics();
                String heading = "Information About Parameters";
                int headingWidth = fm.stringWidth(heading);
                int headingX = (int) ((getWidth() - headingWidth) / 2); // Center the heading
                int headingY = (int) topRectangle.getY() + 40;
                g2d.drawString(heading, headingX, headingY);
                // Draw underline manually
                int underlineY = headingY + fm.getDescent() + 5; // Adjust the position as needed
                int underlineX1 = (int) topRectangle.getX(); // Adjust the start position as needed
                int underlineX2 = (int) topRectangle.getX() + topRectangle.getBounds().width; // Adjust the end position as needed
                g2d.drawLine(underlineX1, underlineY, underlineX2, underlineY);

                // Draw the 5 vertical semi-transparent boxes with text
                for (int i = 0; i < 5; i++) {
                    g2d.setColor(boxColor);
                    g2d.fill(textBoxes[i]);

                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.PLAIN, 18));
                    String paragraph = "";
                    switch (i) {
                        case 0:
                            paragraph = "Credit line outstanding-       The amount of money borrowed from a credit line that has not yet been repaid to the lender.       Credit line outstanding reflects the amount of borrowed funds that remain unpaid and serves as a crucial metric for both borrowers and lenders in managing credit usage effectively.";
                            break;
                        case 1:
                            paragraph = "Debt to Income Ratio- A personal finance measure that compares the percentage of a person's gross monthly income (before taxes) that goes towards payments for rent, mortgage, credit cards, or other debt.";
                            break;
                        case 2:
                            paragraph = "Payment-to-income (PTI) ratio- A financial term that lenders use to assess your ability to manage debt. It focuses specifically on the proportion of your monthly income that goes toward your monthly debt payments.";
                            break;
                        case 3:
                            paragraph = "FICO score- A three-digit number that helps lenders assess your creditworthiness, or how likely you are to repay borrowed money. It's one of the most widely used credit scoring models in the United States.";
                            break;
                        case 4:
                            paragraph = "Years employed- The duration an individual has been engaged in formal employment. It serves as a critical factor in assessing financial stability and repayment capacity for loan evaluations. A longer duration of employment typically indicates a steady income stream and can positively influence loan approval decisions.";
                            break;
                    }
                    drawWrappedText(g2d, paragraph, (int) textBoxes[i].getX() + 20, (int) textBoxes[i].getY() + 40, (int) textBoxes[i].getWidth() - 40, (int) textBoxes[i].getHeight() - 40);
                }

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

        // Define the top center semi-transparent box's size and position
        int topBoxWidth = 600; // Width of the box
        int topBoxHeight = 80; // Height of the box
        int borderRadius = 80; // Border radius of the box
        int topBoxX = (int) ((getWidth() - topBoxWidth) / 2); // X coordinate of the box's top-left corner
        int topBoxY = 50; // Y coordinate of the box's top-left corner

        // Create the top box shape
        topRectangle = new RoundRectangle2D.Double(topBoxX, topBoxY, topBoxWidth, topBoxHeight, borderRadius, borderRadius);

        // Define the 5 vertical semi-transparent boxes' size
        int boxWidth = 200; // Width of the box
        int boxHeight = 540; // Height of the box

        // Create the 5 vertical boxes
        for (int i = 0; i < 5; i++) {
            int boxX = 50 + i * (boxWidth + 50); // X coordinate of the box's top-left corner
            int boxY = topBoxY + topBoxHeight + 50; // Y coordinate of the box's top-left corner

            textBoxes[i] = new RoundRectangle2D.Double(boxX, boxY, boxWidth, boxHeight, borderRadius, borderRadius);
        }

        // Define the small circle's position and size
        int circleX = getWidth() - 150; // X coordinate of the circle's center
        int circleY = getHeight() - 180; // Y coordinate of the circle's center
        int circleRadius = 40; // Radius of the circle

        // Create the small circle shape
        smallCircle = new Ellipse2D.Double(circleX - circleRadius, circleY - circleRadius, circleRadius * 2, circleRadius * 2);

        // Add a mouse listener to the background label for the small circle
        backgroundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (smallCircle.contains(e.getPoint())) {
                    System.out.println("Small Circle Clicked! Go to Next Frame.");
                    // Create and show Frame5
                    SwingUtilities.invokeLater(() -> {
                        Frame4 frame4 = new Frame4();
                        frame4.setVisible(true);
                    });
                    // Hide Frame4
                    setVisible(false);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Frame3().setVisible(true));
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
}

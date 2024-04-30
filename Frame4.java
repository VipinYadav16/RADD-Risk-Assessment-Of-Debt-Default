import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.functions.SMOreg;

class RoundedCornerTextField extends JTextField {

    private static final long serialVersionUID = 1L;
    private String watermark;
    private boolean isWatermarkVisible = true;
    private Font watermarkFont = new Font("Arial", Font.PLAIN, 22); // Font for watermark text
    private Color watermarkColor = Color.BLACK; // Color for watermark text

    public RoundedCornerTextField(String watermark) {
        this.watermark = watermark;
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isWatermarkVisible) {
                    setText("");
                    isWatermarkVisible = false;
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(watermark);
                    isWatermarkVisible = true;
                }
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(255, 255, 255, 128)); // Semi-transparent white color
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 80, 80); // Draw rounded rectangle
        super.paintComponent(g);

        // Draw watermark text if visible and text field is empty
        if (isWatermarkVisible && getText().isEmpty()) {
            g2.setColor(watermarkColor);
            g2.setFont(watermarkFont);
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(watermark)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(watermark, x, y);
        }

        g2.dispose();
    }
}

class CircularButton extends JButton {

    private static final long serialVersionUID = 1L;

    public CircularButton(String text, Color bgColor) {
        super(text);
        setContentAreaFilled(false);
        setBorder(BorderFactory.createEmptyBorder());
        setFocusPainted(false);
        setBackground(bgColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        if (getModel().isArmed()) {
            g2.setColor(new Color(0, 0, 0, 128)); // Semi-transparent black color when pressed
        } else {
            g2.setColor(getBackground()); // Use button background color
        }
        g2.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    }
}

public class Frame4 extends JFrame {

    private JTextField[] textFields = new JTextField[5]; // Array to hold text fields
    private RoundRectangle2D.Double[] inputBoxes = new RoundRectangle2D.Double[5];
    private Color boxColor = new Color(0, 0, 0, 128); // Semi-transparent color for the boxes

    public Frame4() {
        setTitle("Input Boxes");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null); // Using null layout for absolute positioning

        // Define the size and position of the input boxes
        int boxWidth = 600; // Width of the box
        int boxHeight = 80; // Height of the box
        int borderRadius = 80; // Border radius of the box
        int boxX = (getWidth() - boxWidth) / 2 - 25; // X coordinate of the box's top-left corner
        int boxY = 120; // Initial Y coordinate of the first box's top-left corner

        // Headings for each box
        String[] headings = {
                "Credit Line Outstanding",
                "Payment to Income Ratio",
                "Debt to Income Ratio",
                "Years Employed",
                "FICO Score"
        };

        // Create and position the text fields, labels, and input boxes
        for (int i = 0; i < 5; i++) {
            inputBoxes[i] = new RoundRectangle2D.Double(boxX, boxY, boxWidth, boxHeight, borderRadius, borderRadius);
            textFields[i] = new RoundedCornerTextField(headings[i]); // Create custom text field with watermark
            textFields[i].setOpaque(false); // Make text field transparent
            textFields[i].setBorder(BorderFactory.createEmptyBorder()); // Remove border
            textFields[i].setHorizontalAlignment(JTextField.CENTER); // Center align text
            textFields[i].setBounds(boxX + 10, boxY + 10, boxWidth - 20, boxHeight - 20); // Position text field within the box
            add(textFields[i]); // Add text field to the frame

            boxY += boxHeight + 20; // Increment Y coordinate for the next box
        }

        // Create and position the circular buttons with the same color as the rectangle
        Color buttonColor = new Color(255, 255, 255, 128); // Use the same color as the rectangle
        CircularButton analyzeButton1 = new CircularButton("Upload Data", buttonColor);
        analyzeButton1.setBounds((getWidth() - 280) / 2 - 14, boxY + 20, 120, 120);
        add(analyzeButton1);

        CircularButton analyzeButton2 = new CircularButton("Let's Analyze", buttonColor);
        analyzeButton2.setBounds((getWidth() - 280) / 2 + 140, boxY + 20, 120, 120);
        add(analyzeButton2);

        // Load the background image
        ImageIcon backgroundIcon = new ImageIcon("img.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        add(backgroundLabel);

        // Add mouse listener to handle clicks
        analyzeButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Write parameters to file when Upload Data button is clicked
                writeParametersToFile("TestDataSet.arff");
            }
        });

        // Add action listener to "Let's Analyze" button
        analyzeButton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    classifyInstances();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Set focus to the frame to prevent cursor from focusing on the first text box
        requestFocusInWindow();
    }

    private void writeParametersToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write ARFF file header
            writer.write("@relation TestDataSet\n\n");
            writer.write("@ATTRIBUTE credit_lines_outstanding REAL\n");
            writer.write("@ATTRIBUTE payment_to_incomeratio REAL\n");
            writer.write("@ATTRIBUTE debt_to_incomeratio REAL\n");
            writer.write("@ATTRIBUTE years_employed REAL\n");
            writer.write("@ATTRIBUTE fico_score REAL\n");
            writer.write("@ATTRIBUTE default NUMERIC\n\n");
            writer.write("@DATA\n");

            // Write ARFF file data
            for (int i = 0; i < textFields.length; i++) {
                String data = textFields[i].getText();
                writer.write(data);
                if (i < textFields.length - 1) {
                    writer.write(",");
                }
            }
            writer.write(",?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void classifyInstances() throws Exception {
        // Load training dataset
        DataSource source = new DataSource("csvToArff.arff");
        Instances trainDataset = source.getDataSet();

        // Set class index to the last attribute
        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);

        // Build model
        SMOreg smo = new SMOreg();
        smo.buildClassifier(trainDataset);

        // Load new dataset
        DataSource source1 = new DataSource("TestDataSet.arff");
        Instances testDataset = source1.getDataSet();

        // Set class index to the last attribute
        testDataset.setClassIndex(testDataset.numAttributes() - 1);

        // Prepare the output string
        StringBuilder output = new StringBuilder();
        // output.append("==========\n");
        // output.append("Actual Class, NB Predicted\n");
        for (int i = 0; i < testDataset.numInstances(); i++) {
            // double actualValue = testDataset.instance(i).classValue();
            Instance newInst = testDataset.instance(i);
            double predSMO = smo.classifyInstance(newInst);
            double prob;
            if(predSMO<0)
            prob=(predSMO*-1)*100;
            else
            prob=predSMO*100;
            
            if (predSMO < 0.5) {
                output.append("The Probability of Defaulting is: ").append(prob).append("% \n");
                output.append("Loan should not Default\n");
            } else {
                output.append("The Probability of Defaulting is: ").append(prob).append("% \n");
                output.append("Loan can Default\n");
            }
        }

        // Open a new frame (Frame6) to display the output
        Frame5 outputFrame = new Frame5(output.toString());
        outputFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Frame4().setVisible(true));
    }
}

class Frame5 extends JFrame {

    public Frame5(String outputText) {
        setTitle("Analysis Result");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Load background image
        ImageIcon backgroundImage = new ImageIcon("Frame.png");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        setContentPane(backgroundLabel);
        setLayout(new BorderLayout());

        // Create semi-transparent rectangular box
        JPanel outputPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 200)); // Semi-transparent white color
                int borderRadius = 80;
                int padding = 40;
                int x = padding;
                int y = padding;
                int width = getWidth() - 2 * padding;
                int height = getHeight() - 2 * padding;
                g2.fillRoundRect(x, y, width, height, borderRadius, borderRadius);
                g2.dispose();
            }
        };
        outputPanel.setLayout(new BorderLayout());
        outputPanel.setOpaque(false);

        JTextArea outputTextArea = new JTextArea(outputText);
        outputTextArea.setEditable(false);
        outputTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        add(outputPanel, BorderLayout.CENTER);
    }
}

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

public class GUI {

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Boids Simulation");
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

            // Create an instance of the BoidsSimulation class
            BoidsSimulation simulation = GUIStyler_handler.getSimulation();

            JPanel rightPanel = GUIStyler_handler.rightPanel(screen);
            JPanel leftPanel;

            try {
                leftPanel = GUIStyler_handler.leftPanel(screen);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            JPanel bottomPanel = GUIStyler_handler.bottomPanel(screen);

            // Create a container panel for rightPanel with a FlowLayout
            JPanel rightContainerPanel = new JPanel(new FlowLayout());
            JPanel leftContainerPanel = new JPanel(new FlowLayout());
            JPanel bottomContainerPanel = new JPanel(new FlowLayout());
            rightContainerPanel.add(rightPanel);
            leftContainerPanel.add(leftPanel);
            bottomContainerPanel.add(bottomPanel);

            // Create a mainPanel to hold both rightContainerPanel and simulation
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(rightContainerPanel, BorderLayout.EAST);
            mainPanel.add(leftContainerPanel, BorderLayout.WEST);
            mainPanel.add(bottomContainerPanel, BorderLayout.SOUTH);
            mainPanel.add(simulation, BorderLayout.CENTER);

            frame.getContentPane().add(mainPanel);
            frame.setSize(screen.width, screen.height);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });

    }
}
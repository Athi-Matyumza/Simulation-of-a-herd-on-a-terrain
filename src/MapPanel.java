
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class MapPanel extends JPanel{
    // Define canvas dimensions and simulation parameters
    private BoidsSimulation parentPanel;
    public HeightMaps heightMaps;
    private boolean zoomSet = false;

    public MapPanel() {
        // Initialize simulation and event listeners\
        this.setBackground(Color.GRAY);
    }
    public MapPanel(HeightMaps heightMaps) {
        // Initialize simulation and event listeners\
        this.setBackground(Color.GRAY);
        this.heightMaps = heightMaps;
        // sizeCanvas();
    }
    public MapPanel(HeightMaps heightMaps, BoidsSimulation parentPanel) {
        // Initialize simulation and event listeners\
        this.setBackground(Color.GRAY);
        this.heightMaps = heightMaps;
        this.parentPanel = parentPanel;
        // sizeCanvas();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if(heightMaps.loaded){
            if(!zoomSet){
                float maxZoomX = (float)this.getSize().width/(float)heightMaps.GetWidth();
                float maxZoomY = (float)this.getSize().height/(float)heightMaps.GetHeight();
                
                parentPanel.zoom = Math.max(maxZoomX, maxZoomY);
                parentPanel.maxZoom = Math.min(maxZoomX, maxZoomY)-0.1f;
                zoomSet=true;
            }
            
            // this.setSize((int)Math.ceil(heightMaps.GetWidth()*parentPanel.zoom), (int)Math.ceil(heightMaps.GetHeight()*parentPanel.zoom));
            // Draw obstacles
            for(int x=0; x<heightMaps.width; x++){
                for(int y=0; y<heightMaps.height; y++){
                    int colGrey = heightMaps.GetHeightColor(x,y);
                    // new Color(colGrey, colGrey, y, colGrey)
                    graphics.setColor(new Color(colGrey, colGrey, colGrey, 255));
                    graphics.fillRect((int)Math.ceil(x*parentPanel.zoom - parentPanel.xOffset), (int)Math.ceil(y*parentPanel.zoom - parentPanel.yOffset), (int)Math.ceil(parentPanel.zoom), (int)Math.ceil(parentPanel.zoom));
                }
            }
        }
        
        // graphics.clearRect(0, 0, heightMaps.GetWidth(), heightMaps.GetHeight());
        // this.repaint();
    }
}

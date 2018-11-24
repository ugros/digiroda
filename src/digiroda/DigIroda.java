
package digiroda;

import static digiroda.DigiController.LOGGER;
import static digiroda.DigiController.user;
import java.util.logging.Level;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class DigIroda extends Application {
static Parent root ; 

    @Override
    public void start(Stage stage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image("resources/images/office.png"));
        stage.setMaximized(true);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        
        stage.show();
    }

    public static void main(String[] args) {
      try {
        launch(args);
      }
      finally {
          if (user!=null) if (user.getConnects()!=null) user.getConnects().close();
          LOGGER.log(Level.INFO,"Program terminated.");
      }
    }
    
}

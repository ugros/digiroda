
package digiroda;

import static digiroda.DigiController.LOGGER;
import java.util.logging.Level;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class DigIroda extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image("resources/images/office.png"));
        stage.show();
    }

    public static void main(String[] args) {
      try {
        launch(args);
      }
      finally {
          LOGGER.log(Level.INFO,"Program terminated.");
      }
    }
    
}

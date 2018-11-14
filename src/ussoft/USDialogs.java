package ussoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Pair;





/**  
 * @author USSoft - 2018.
 * @version 1.0
 *
 * Az osztály különdözõ dialógusablakok egyszerû megvalósítására ad lehetõséget az osztály 
 * metódusainak meghívásával. Részletek a metódusoknál
 *
 */
public class USDialogs {
		
	private static Pair<String,String> p;
	
	/**
	 * Konstruktor, használata nem kötelezõ, az osztály metódusainak meghívásához nem szükséges a példányosítás.
	 * 
	 */
	public USDialogs() {
	}

	/** Egyszerû, <u>osztott</u> információs ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	header	Az információs szöveg fejléce
	 * 	@param	content	Az információs szöveg törzse
	 */
	public static void information(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (title.equals(""))
			title = "Információs üzenet";
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/** Egyszerû, <u>nem osztott</u> információs ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	content	Az információs szöveg törzse
	 */
	public static void information(String title, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (title.equals(""))
			title = "Információs üzenet";
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/** <u>Hosszú</u> információs ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	header	Az információs szöveg fejléce
	 * 	@param	content	Az információs szöveg törzse
	 */
	public static void longInformation(String title, String header, String content, String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (title.equals(""))
			title = "Információs üzenet";
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		TextArea textArea = new TextArea(text);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);

		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(new Label("További részletek:"), 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}

	/** <u>Osztott</u>, figyelmeztetõ üzenetet tartalmazó ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	header	Az információs szöveg fejléce
	 * 	@param	content	Az információs szöveg törzse
	 */
	public static void warning(String title, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		if (title.equals(""))
			title = "Figyelemfelhívó üzenet";
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/** <u>Nem osztott</u>, figyelmeztetõ üzenetet tartalmazó ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	content	Az információs szöveg törzse
	 */
	public static void warning(String title, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		if (title.equals(""))
			title = "Figyelemfelhívó üzenet";
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/** <u>Osztott</u>, hibaüzenetet tartalmazó ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	header	Az információs szöveg fejléce
	 * 	@param	content	Az információs szöveg törzse
	 */
	public static void error(String title, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		if (title.equals(""))
			title = "Hibaüzenet";
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/** <u>Nem osztott</u>, hibaüzenetet tartalmazó ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	content	Az információs szöveg törzse
	 */
	public static void error(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		if (title.equals(""))
			title = "Hibaüzenet";
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/** <u>Osztott</u>, megerõsítést kérõ ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	header	Az információs szöveg fejléce
	 * 	@param	content	Az információs szöveg törzse
	 */
	public static ButtonType confirmation(String title, String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		if (title.equals(""))
			title = "Megerõsítés";
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		Optional<ButtonType> result = alert.showAndWait();
		return result.get();
	}

	/** <u>Nem osztott</u>, megerõsítést kérõ ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	content	Az információs szöveg törzse
	 */
	public static ButtonType confirmation(String title, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		if (title.equals(""))
			title = "Megerõsítés";
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);

		Optional<ButtonType> result = alert.showAndWait();
		return result.get();
	}

	/** <u>Osztott</u>, megerõsítést kérõ ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	header	Az információs szöveg fejléce
	 * 	@param	content	Az információs szöveg törzse
	 * 	@param	buttons	List&lt;ButtonType&gt; - Az ablak alján elhelyezkedõ gombokat tartalmazó lista	 * 
	 * 	@return	ButtonType - a kiválasztott gomb
	 */
	public static ButtonType confirmation(String title, String header, String content, List<ButtonType> buttons) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		if (title.equals(""))
			title = "Megerõsítés";
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.getButtonTypes().setAll(buttons);

		Optional<ButtonType> result = alert.showAndWait();
		return result.get();
	}

	/** Egysoros adatot bekérõ ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	header	Az információs szöveg fejléce
	 * 	@param	content	Az információs szöveg törzse
	 * 	@return	String - A begépelt szöveg, vagy null
	 */
	public static String input(String title, String header, String content) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			return result.get();
		} else
			return null;
	}

	/** Egysoros adatot bekérõ ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	header	Az információs szöveg fejléce
	 * 	@param	content	Az információs szöveg törzse
	 * 	@param	defaulttext	Alapértelmezett szöveg
	 * 	@return	String - A begépelt szöveg, vagy null
	 */
	public static String input(String title, String header, String content, String defaultText) {
		TextInputDialog dialog = new TextInputDialog(defaultText);
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			return result.get();
		} else
			return null;
	}

	/** Többsoros adatot bekérõ ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	header	Az információs szöveg fejléce
	 * 	@param 	List&lt;String&gt; items
	 * 	@return HashMap&lt;String, String&gt;(); - .getKey()= melyik elem / .getValue()=begépelt szöveg
	 */
	
	public static Map<String, String> input(String title, String header, List<String> items) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		List<TextField> questions = new ArrayList<TextField>();
		int max = items.size();
		for (int i = 1; i <= max; i++) {
			grid.add(new Label(items.get(i - 1)), 0, i);
			questions.add(new TextField());
			grid.add(questions.get(i - 1), 1, i);
		}

		dialog.getDialogPane().setContent(grid);

		Optional<String> result = dialog.showAndWait();
		Map<String, String> answers = new HashMap<String, String>();
		if (result.isPresent()) {
			for (int i = 1; i <= max; i++) {
				answers.put(items.get(i - 1), questions.get(i - 1).getText());
			}
			return answers;
		} else
			return null;
	}
	
	/** Bejelentkezõ ablak megjelenítése.
	 * 	@param	title	Az ablak címsora
	 * 	@param	header	Az információs szöveg fejléce
	 * 	@return Pair&lt;String, String&gt;(); 
	 * </br>.getKey()= felhasználónév 
	 * </br>.getValue()=jelszó
	 */
	public static Pair<String,String> login(String title, String header)
	{
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		
		Image image = new Image("resources/images/login.png");
		dialog.setGraphic(new ImageView(image));
	
		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Felhasználónév");
		PasswordField password = new PasswordField();
		password.setPromptText("Jelszó");

		grid.add(new Label("Felhasználónév:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Jelszó:"), 0, 1);
		grid.add(password, 1, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());
		

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(username.getText(), password.getText());
		    }
		    return null;
		});
		
		p=null;

		Optional<Pair<String, String>> result = dialog.showAndWait();		

		
		result.ifPresent(usernamePassword -> {
			p = new Pair<String,String>(usernamePassword.getKey(), usernamePassword.getValue());			
		});
		return p;
	}
	
	

}

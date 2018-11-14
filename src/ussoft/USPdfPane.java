package ussoft;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class USPdfPane extends ScrollPane {
	
	public USPdfPane(File file) {
		super();
		GridPane grid = new GridPane();

		try {
			PDDocument document = PDDocument.load(file);
			System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
			PDFRenderer renderer = new PDFRenderer(document);
			int max = document.getNumberOfPages();

			ImageView imageView = new ImageView();

			// Oldalak száma max 20 !!!!!!!!!!!!!!!!!!!!!!!!!!
			int c = 20;

			if (max > c) {
				USDialogs.error("Hibaüzenet", "Túl hosszú .pdf dokumentum",
						"A .pdf dokumneum oldalainak száma meghaladja a " + c + "-t!");
				max = c;
			}

			for (int i = 0; i < max; i++) {
				BufferedImage bufferedImge = renderer.renderImage(i);				
				Image image = SwingFXUtils.toFXImage(bufferedImge, null);
				imageView = new ImageView();
				imageView.setImage(image);

				grid.add(imageView, 0, i);
			}
			document.close();
			this.setContent(grid);

		} catch (IOException e) {
			System.out.println("hiba: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

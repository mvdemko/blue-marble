package hellofx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.curiousworks.BlueMarble;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BlueMarbleController {	
	private boolean isEnhanced;
	
	@FXML
	private ImageView image;

	@FXML
	private DatePicker datePicker;
	
	@FXML
	private DialogPane feedbackPane;
	
	@FXML
	private Button enhancedButton;
	
	@FXML
	private Button bWButton;
	
	@FXML
	void updateDate(ActionEvent event) {
		isEnhanced = false;
		populateImage(false);
	}
	
	@FXML
	void enhanceImage(ActionEvent event) {
		isEnhanced = true;
		populateImage(false);
	}
	
	@FXML
	void makeBlackAndWhite(ActionEvent event) {
		populateImage(true);
	}
	
	void populateImage(boolean isBlackAndWhite) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		BlueMarble blueMarble = new BlueMarble();
		// get the date the user specified
		String selectedDate = datePicker.getValue().toString();
		LocalDate selectedLocalDate = LocalDate.parse(selectedDate, formatter);
		
		// get the most recent date for which natural image is available
		String mostRecentNaturalImageDate = BlueMarble.getMostRecentImageDate("natural");
		LocalDate mostRecentNaturalImageLocalDate = LocalDate.parse(mostRecentNaturalImageDate, formatter);
		
		// get the most recent date for which enhanced image is available
		String mostRecentEnhancedImageDate = BlueMarble.getMostRecentImageDate("enhanced");
		LocalDate mostRecentEnhancedImageLocalDate = LocalDate.parse(mostRecentEnhancedImageDate, formatter);
		
		if (selectedLocalDate.isAfter(mostRecentNaturalImageLocalDate)) {
			feedbackPane.setContentText("Please select a date \n"
					+ "on or before " + mostRecentNaturalImageLocalDate);
	    } else if (selectedLocalDate.isAfter(mostRecentEnhancedImageLocalDate)) {
	    	enhancedButton.setDisable(true);
	    } else {
	    	feedbackPane.setContentText("");
			blueMarble.setDate(selectedDate);
			blueMarble.setEnhanced(isEnhanced);
			image.setImage(new Image(blueMarble.getImage()));
			if (isBlackAndWhite) {
				ColorAdjust colorAdjust = new ColorAdjust();
		        colorAdjust.setSaturation(-1);
		        image.setEffect(colorAdjust);
			}
		}
	}


}

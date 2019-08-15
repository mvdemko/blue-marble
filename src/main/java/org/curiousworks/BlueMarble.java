package org.curiousworks;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class BlueMarble {
	
	public static String API_KEY = "7u1nv3v73ROS0u2F65J7w14pnGpjzwCv6cruBzes";
	private String dateAsString;
	private String quality = "natural";
	private String caption;
	private String nasaImageName;

	public static InputStream getMostRecentImage() {
		BlueMarble blueMarble = new BlueMarble();
		blueMarble.setDate(LocalDate.now().minusDays(1).toString());
		return blueMarble.getImage();
	}
	
	public static String getMostRecentImageDate(String imageType) {
		String lastImageDate = "2019-06-27";
		try {
			if (imageType.toLowerCase() != "natural"
					&& imageType.toLowerCase() != "enhanced") {
				throw new IllegalArgumentException("Invalid image type specified");
			}
			URL url = new URL("https://api.nasa.gov/EPIC/api/" + 
								imageType + "/all?api_key=" + API_KEY);
			InputStream metaInfoStream = url.openStream();
			lastImageDate = IOUtils.toString(metaInfoStream, "UTF-8").substring(10, 20);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lastImageDate;
		
	}
	
	public void setDate(String date) {
		this.dateAsString = date;
	}
	
	public InputStream getImage() {
		try {
			getMetaData();

			URL url = new URL("https://api.nasa.gov/EPIC/archive/" + quality + "/" + dateAsString.replace('-', '/')
					+ "/png/" + this.nasaImageName + ".png?api_key=" + API_KEY);
			return url.openStream();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void getMetaData() throws IOException, MalformedURLException {
		String metaQueryURL = "https://epic.gsfc.nasa.gov/api/" + quality + "/date/" + dateAsString;
		InputStream metaInfoStream = new URL(metaQueryURL).openStream();
		String metaInfoJSON = IOUtils.toString(metaInfoStream, "UTF-8").replace("[", "");
		metaInfoStream.close();
		JSONObject json = new JSONObject(metaInfoJSON);
		this.nasaImageName = (String) json.get("image");
		this.caption = (String) json.get("caption");
	}

	public String getCaption() {
		return this.caption;
	}

	public void setEnhanced(boolean b) {
		if (b) {
			this.quality = "enhanced";
		} else {
			this.quality = "natural";
		}
	}
}

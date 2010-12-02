package viewer;

import javax.swing.*;
import java.awt.*;

import javax.imageio.ImageIO;
import java.net.*;

public class ImageLoader {

	/** Method loadIcon: <br />
	 * @param iconName icon in the folder /images/ relative to the class path.
	 * @return ImageIcon
	 */
	public static final ImageIcon loadIcon(String iconName) {
		try {
			URL url = GameFrame.class.getResource("/images/" + iconName);
			if (url != null) {
				Image img = ImageIO.read(url);
				return new ImageIcon(img);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}
	
}
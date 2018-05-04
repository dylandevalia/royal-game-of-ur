package game.dylandevalia.royal_game_of_ur.objects.base;

import game.dylandevalia.royal_game_of_ur.gui.ColorMaterial;
import game.dylandevalia.royal_game_of_ur.gui.Window;
import game.dylandevalia.royal_game_of_ur.utility.ICallback;
import game.dylandevalia.royal_game_of_ur.utility.Utility;
import java.awt.Color;
import java.awt.Graphics2D;

public class Fade {
	
	private Color inColour, outColor;
	private FadeState state;
	private ICallback callback;
	private int ratio;
	private int speed;
	
	public Fade(Color inColour, Color outColor, int speed, boolean fadeIn) {
		this.inColour = inColour;
		this.outColor = outColor;
		this.speed = speed;
		
		if (fadeIn) {
			ratio = 255;
			state = FadeState.IN;
		} else {
			ratio = 0;
			state = FadeState.NONE;
		}
	}
	
	public void draw(Graphics2D g) {
		switch (state) {
			case IN:
				ratio = Utility.clamp(ratio -= speed, 0, 255);
				g.setColor(ColorMaterial.withAlpha(inColour, ratio));
				g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
				if (ratio == 0) {
					state = FadeState.NONE;
					if (callback != null) {
						callback.callback();
					}
				}
				break;
			case OUT:
				ratio = Utility.clamp(ratio += speed, 0, 255);
				g.setColor(ColorMaterial.withAlpha(outColor, ratio));
				g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
				if (ratio == 255) {
					state = FadeState.NONE;
					if (callback != null) {
						callback.callback();
					}
				}
				break;
			case NONE:
				break;
		}
	}
	
	public void setCallback(ICallback callback) {
		this.callback = callback;
	}
	
	public void out() {
		state = FadeState.OUT;
	}
	
	private enum FadeState {
		IN, OUT, NONE
	}
}

package cs4620.common.texture;

import egl.math.Color;
import egl.math.MathHelper;
import egl.math.Vector2i;

public class TexGenUVGrid extends ACTextureGeneratorTwoColor {
	public TexGenUVGrid() {
		setSize(new Vector2i(128, 128));
		setColor1(Color.Red);
		setColor2(Color.Green);
	}
	
	@Override
	public void getColor(float u, float v, Color outColor) {
		outColor.set(100, 100, 100);
	}
}

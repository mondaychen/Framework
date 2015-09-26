package cs4620.common.texture;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import egl.math.Color;
import egl.math.MathHelper;
import egl.math.Vector2i;

public class TexGenCheckerBoard extends ACTextureGeneratorTwoColor {
	public final Vector2i tiles = new Vector2i();
	
	public TexGenCheckerBoard() {
		setTiles(8, 8);
		setColor1(Color.Black);
		setColor2(Color.White);
	}
	
	public void setTiles(int x, int y) {
		tiles.set(x, y);
	}
	public void setTiles(Vector2i t) {
		setTiles(t.x, t.y);
	}

	
	@Override
	public void getColor(float u, float v, Color outColor) {
		outColor.set(100, 100, 100);
	}
	
	@Override
	public void saveData(Document doc, Element eData) {
		super.saveData(doc, eData);
		
		Element e = doc.createElement("tiles");
		e.appendChild(doc.createTextNode(tiles.x + " " + tiles.y));
		eData.appendChild(e);
	}
}

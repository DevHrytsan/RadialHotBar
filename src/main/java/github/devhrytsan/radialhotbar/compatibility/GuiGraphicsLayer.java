package github.devhrytsan.radialhotbar.compatibility;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

//? if >= 26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
*///? } else {
import net.minecraft.client.gui.GuiGraphics;
 //? }

public class GuiGraphicsLayer { //Backward compatibility hell

	//? if >= 26.1 {
	/*private GuiGraphicsExtractor instance;
	 *///? } else {
	private GuiGraphics instance;
	//? }

	public GuiGraphicsLayer(){

	}

	public GuiGraphicsLayer(Object graphicsObject) {
		//? if >= 26.1 {
		/*this.instance = (GuiGraphicsExtractor) graphicsObject;
		 *///? } else {
		this.instance = (GuiGraphics) graphicsObject;
		//? }
	}

	public void setContext(Object graphicsObject){
		//? if >= 26.1 {
		/*this.instance = (GuiGraphicsExtractor) graphicsObject;
		 *///? } else {
		this.instance = (GuiGraphics) graphicsObject;
		//? }
	}

	public void pushMatrix() {
		//? if >=1.21.5 {
		this.instance.pose().pushMatrix();
		//? } else {
		/*this.instance.pose().pushPose();
		 *///? }
	}

	public void popMatrix() {
		//? if >=1.21.5 {
		this.instance.pose().popMatrix();
		//? } else {
		/*this.instance.pose().popPose();
		 *///? }
	}

	public void scaleMatrix(float x, float y, float i) {
		//? if >=1.21.5 {
		this.instance.pose().scale(x, y);
		//? } else {
		/*this.instance.pose().scale(x,y,i);
		 *///? }
	}

	public void translate(float x, float y, float z) {
		//? if >=1.21.5 {
		this.instance.pose().translate(x, y);
		//? } else {
		/*this.instance.pose().translate(x, y, z);
		 *///? }
	}

	public void fill(int minX, int minY, int maxX, int maxY, int color) {
		this.instance.fill(minX, minY, maxX, maxY, color);
	}

	public void drawString(Font font, String text, int x, int y, int colorCode, boolean dropShadow) {
		//? if >= 26.1 {
		/*this.instance.text(font, Component.literal(text), x, y, colorCode, dropShadow);
		*///? } else {
		this.instance.drawString(font, text, x, y, colorCode, dropShadow);
		 //? }
	}

	public void drawString(Font font, Component component, int x, int y, int colorCode, boolean dropShadow) {
		//? if >= 26.1 {
		/*this.instance.text(font, component, x, y, colorCode, dropShadow);
		*///? } else {
		this.instance.drawString(font, component, x, y, colorCode, dropShadow);
		 //? }
	}

	public void renderItem(ItemStack itemStack, int x, int y) {
		//? if >= 26.1 {
		/*this.instance.item(itemStack, x, y);
		*///? } else {
		this.instance.renderItem(itemStack, x, y);
		 //? }

	}

	public void renderItemDecoration(Font font, ItemStack itemStack, int x, int y) {
		//? if >= 26.1 {
		/*this.instance.itemDecorations(font, itemStack, x, y);
		*///? } else {
		this.instance.renderItemDecorations(font, itemStack, x, y);
		 //? }
	}

	//? if >= 26.1 {
	/*public GuiGraphicsExtractor unwrap() {
		return this.instance;
	}
	*///? } else {
	public GuiGraphics unwrap() { return this.instance; }
	 //? }
}

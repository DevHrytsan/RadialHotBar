package github.devhrytsan.radialhotbar.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class GuiGraphicsUtils {

	public static void PopMatrix(GuiGraphics context) {

		//? if >=1.21.5 {
		context.pose().popMatrix();
		//? } else {
		/*
		context.pose().popPose();
		*/
		//? }
	}

	public static void PushMatrix(GuiGraphics context) {
		//? if >=1.21.5 {
		context.pose().pushMatrix();
		//? } else {
		/*
		context.pose().pushPose();
		*/
		//? }
	}

	public static void ScaleMatrix(GuiGraphics context, float x, float y, float i) {
		//? if >=1.21.5 {
		context.pose().scale(x,y);
		//? } else {
		/*
		context.pose().scale(x,y,i);
		*/
		//? }
	}

	public static void TranslateMatrix(GuiGraphics context, float x, float y, float i) {
		//? if >=1.21.5 {
		context.pose().translate(x, y);
		//? } else {
		/*
		context.pose().translate(x, y, i);
		*/
		//? }
	}

	public static void RenderItem(GuiGraphics context, ItemStack itemStack, int x, int y) {
		context.renderItem(itemStack, x, y);
	}

	public static void RenderItemDecoration(GuiGraphics context, Font font, ItemStack itemStack, int x, int y) {
		context.renderItemDecorations(font, itemStack, x, y);
	}

	public static void DrawString(GuiGraphics context, Font font, String text, int x, int y, int colorCode, boolean dropShadow){
		context.drawString(
				font,
				text,
				x,
				y,
				colorCode,
				dropShadow
		);
	}

}

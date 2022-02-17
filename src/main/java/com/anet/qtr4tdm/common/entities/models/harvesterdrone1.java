package com.anet.qtr4tdm.common.entities.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

// Made with Blockbench 4.0.5
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


public class harvesterdrone1 extends ModelBase {
	private final ModelRenderer rotor;
	private final ModelRenderer rotor2;
	private final ModelRenderer rotor3;
	private final ModelRenderer rotor4;
	private final ModelRenderer bb_main;

	public harvesterdrone1() {
		textureWidth = 128;
		textureHeight = 128;

		rotor = new ModelRenderer(this);
		rotor.setRotationPoint(6.5F, 12.0F, 6.5F);
		rotor.cubeList.add(new ModelBox(rotor, 40, 20, -5.0F, 0.0F, -1.0F, 10, 1, 2, 0.0F, false));
		rotor.cubeList.add(new ModelBox(rotor, 20, 30, -1.0F, 0.0F, 1.0F, 2, 1, 4, 0.0F, false));
		rotor.cubeList.add(new ModelBox(rotor, 0, 30, -1.0F, 0.0F, -5.0F, 2, 1, 4, 0.0F, false));

		rotor2 = new ModelRenderer(this);
		rotor2.setRotationPoint(6.5F, 12.0F, -6.5F);
		rotor2.cubeList.add(new ModelBox(rotor2, 24, 38, -5.0F, 0.0F, -1.0F, 10, 1, 2, 0.0F, false));
		rotor2.cubeList.add(new ModelBox(rotor2, 20, 25, -1.0F, 0.0F, 1.0F, 2, 1, 4, 0.0F, false));
		rotor2.cubeList.add(new ModelBox(rotor2, 0, 25, -1.0F, 0.0F, -5.0F, 2, 1, 4, 0.0F, false));

		rotor3 = new ModelRenderer(this);
		rotor3.setRotationPoint(-6.5F, 12.0F, 6.5F);
		rotor3.cubeList.add(new ModelBox(rotor3, 0, 38, -5.0F, 0.0F, -1.0F, 10, 1, 2, 0.0F, false));
		rotor3.cubeList.add(new ModelBox(rotor3, 20, 20, -1.0F, 0.0F, 1.0F, 2, 1, 4, 0.0F, false));
		rotor3.cubeList.add(new ModelBox(rotor3, 0, 20, -1.0F, 0.0F, -5.0F, 2, 1, 4, 0.0F, false));

		rotor4 = new ModelRenderer(this);
		rotor4.setRotationPoint(-6.5F, 12.0F, -6.5F);
		rotor4.cubeList.add(new ModelBox(rotor4, 36, 8, -5.0F, 0.0F, -1.0F, 10, 1, 2, 0.0F, false));
		rotor4.cubeList.add(new ModelBox(rotor4, 0, 5, -1.0F, 0.0F, 1.0F, 2, 1, 4, 0.0F, false));
		rotor4.cubeList.add(new ModelBox(rotor4, 0, 0, -1.0F, 0.0F, -5.0F, 2, 1, 4, 0.0F, false));

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -6.0F, -8.0F, -6.0F, 12, 8, 12, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 20, 21, -5.0F, -9.0F, -8.0F, 2, 1, 16, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 20, 3.0F, -9.0F, -8.0F, 2, 1, 16, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 36, 4, -8.0F, -10.0F, -8.0F, 16, 1, 3, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 36, 0, -8.0F, -10.0F, 5.0F, 16, 1, 3, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 41, -8.0F, -11.0F, 5.0F, 3, 1, 3, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 40, 31, -8.0F, -11.0F, -8.0F, 3, 1, 3, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 40, 27, 5.0F, -11.0F, -8.0F, 3, 1, 3, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 40, 23, 5.0F, -11.0F, 5.0F, 3, 1, 3, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		rotor.render(f5);
		rotor2.render(f5);
		rotor3.render(f5);
		rotor4.render(f5);
		bb_main.render(f5);
	}

	public void setVaintsRotation (float rotation) {
		setRotationAngle(rotor, 0, rotation, 0);
		setRotationAngle(rotor2, 0, -rotation, 0);
		setRotationAngle(rotor3, 0, -rotation, 0);
		setRotationAngle(rotor4, 0, rotation, 0);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
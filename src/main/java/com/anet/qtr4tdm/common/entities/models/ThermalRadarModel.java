package com.anet.qtr4tdm.common.entities.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


public class ThermalRadarModel extends ModelBase {
	public final ModelRenderer rotatin;
	public final ModelRenderer bone;

	public ThermalRadarModel() {
		textureWidth = 128;
		textureHeight = 128;

		rotatin = new ModelRenderer(this);
		rotatin.setRotationPoint(0.0F, 24.0F, 0.0F);
		rotatin.cubeList.add(new ModelBox(rotatin, 42, 18, -8.0F, -6.0F, -5.0F, 16, 2, 10, 0.0F, false));
		rotatin.cubeList.add(new ModelBox(rotatin, 48, 0, -8.0F, -9.0F, -4.0F, 1, 3, 9, 0.0F, false));
		rotatin.cubeList.add(new ModelBox(rotatin, 23, 45, 7.0F, -9.0F, -4.0F, 1, 3, 9, 0.0F, false));
		rotatin.cubeList.add(new ModelBox(rotatin, 0, 33, -8.0F, -11.0F, -5.0F, 16, 2, 10, 0.0F, false));
		rotatin.cubeList.add(new ModelBox(rotatin, 41, 34, -7.0F, -9.0F, -6.0F, 14, 3, 11, 0.0F, false));
		rotatin.cubeList.add(new ModelBox(rotatin, 0, 0, -6.0F, -12.0F, -5.0F, 4, 1, 4, 0.0F, false));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 24.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 0, 18, -7.0F, -3.0F, -7.0F, 14, 1, 14, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 45, -4.0F, -4.0F, -4.0F, 8, 1, 8, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 0, -8.0F, -2.0F, -8.0F, 16, 2, 16, 0.0F, false));
	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		rotatin.render(f5);
		bone.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void SetTopRotation (float y) {
		setRotationAngle(rotatin, 0, y, 0);
	}
}
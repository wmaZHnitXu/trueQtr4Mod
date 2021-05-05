package com.anet.qtr4tdm.common.entities.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


public class Radar2Model extends ModelBase {
	private final ModelRenderer crutilki;
	private final ModelRenderer crutilka3_r1;
	private final ModelRenderer crutilka3_r2;
	private final ModelRenderer crutilka3_r3;
	private final ModelRenderer crutilka2_r1;
	private final ModelRenderer crutilka2_r2;
	private final ModelRenderer bb_main;

	public Radar2Model() {
		textureWidth = 64;
		textureHeight = 64;

		crutilki = new ModelRenderer(this);
		crutilki.setRotationPoint(0.0F, 24.0F, 0.0F);
		crutilki.cubeList.add(new ModelBox(crutilki, 0, 35, -3.0F, -6.0F, -3.0F, 6, 2, 6, 0.0F, false));
		crutilki.cubeList.add(new ModelBox(crutilki, 40, 55, -3.0F, -22.0F, 13.0F, 6, 3, 6, 0.0F, false));

		crutilka3_r1 = new ModelRenderer(this);
		crutilka3_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		crutilki.addChild(crutilka3_r1);
		setRotationAngle(crutilka3_r1, -1.5708F, -0.5672F, 0.0F);
		crutilka3_r1.cubeList.add(new ModelBox(crutilka3_r1, 0, 0, 1.0F, 14.0F, -32.0F, 26, 1, 19, 0.0F, false));

		crutilka3_r2 = new ModelRenderer(this);
		crutilka3_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
		crutilki.addChild(crutilka3_r2);
		setRotationAngle(crutilka3_r2, -1.5708F, 0.5672F, 0.0F);
		crutilka3_r2.cubeList.add(new ModelBox(crutilka3_r2, 0, 0, -28.0F, 15.0F, -32.0F, 26, 1, 19, 0.0F, false));

		crutilka3_r3 = new ModelRenderer(this);
		crutilka3_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
		crutilki.addChild(crutilka3_r3);
		setRotationAngle(crutilka3_r3, -1.5708F, 0.0F, 0.0F);
		crutilka3_r3.cubeList.add(new ModelBox(crutilka3_r3, 0, 0, -13.0F, 10.0F, -32.0F, 26, 1, 19, 0.0F, false));
		crutilka3_r3.cubeList.add(new ModelBox(crutilka3_r3, 0, 37, -2.0F, 11.0F, -24.0F, 4, 1, 11, 0.0F, false));

		crutilka2_r1 = new ModelRenderer(this);
		crutilka2_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		crutilki.addChild(crutilka2_r1);
		setRotationAngle(crutilka2_r1, 0.2182F, 0.0F, 0.0F);
		crutilka2_r1.cubeList.add(new ModelBox(crutilka2_r1, 0, 20, -2.0F, -16.0F, -7.0F, 4, 1, 28, 0.0F, false));

		crutilka2_r2 = new ModelRenderer(this);
		crutilka2_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
		crutilki.addChild(crutilka2_r2);
		setRotationAngle(crutilka2_r2, -0.7854F, 0.0F, 0.0F);
		crutilka2_r2.cubeList.add(new ModelBox(crutilka2_r2, 30, 29, -2.0F, -2.0F, -18.0F, 4, 1, 13, 0.0F, false));

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 39, -12.0F, -1.0F, -12.0F, 24, 1, 24, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 47, -8.0F, -2.0F, -8.0F, 16, 1, 16, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 34, -3.0F, -4.0F, -3.0F, 6, 2, 6, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {

		crutilki.render(f5);
		bb_main.render(f5);
		
	}

	public void SetCrutilkiRotation (float y) {
		setRotationAngle(crutilki, 0, y, 0);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
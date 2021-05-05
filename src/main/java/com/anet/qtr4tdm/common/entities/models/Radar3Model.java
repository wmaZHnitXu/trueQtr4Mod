package com.anet.qtr4tdm.common.entities.models;


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports
import net.minecraft.entity.Entity;


public class Radar3Model extends ModelBase {
	private final ModelRenderer crutilki;
	private final ModelRenderer crutilka3_r1;
	private final ModelRenderer crutilka3_r2;
	private final ModelRenderer crutilka3_r3;
	private final ModelRenderer crutilka3_r4;
	private final ModelRenderer crutilka2_r1;
	private final ModelRenderer crutilka2_r2;
	private final ModelRenderer crutilka2_r3;
	private final ModelRenderer bb_main;

	public Radar3Model() {
		textureWidth = 256;
		textureHeight = 256;

		crutilki = new ModelRenderer(this);
		crutilki.setRotationPoint(0.0F, 22.0F, 0.0F);
		crutilki.cubeList.add(new ModelBox(crutilki, 84, 103, -3.0F, -9.0F, -3.0F, 6, 5, 6, 0.0F, false));
		crutilki.cubeList.add(new ModelBox(crutilki, 40, 55, -9.0F, -35.0F, 26.0F, 17, 6, 7, 0.0F, false));

		crutilka3_r1 = new ModelRenderer(this);
		crutilka3_r1.setRotationPoint(0.0F, -5.0F, -5.0F);
		crutilki.addChild(crutilka3_r1);
		setRotationAngle(crutilka3_r1, -1.5708F, -0.5672F, 0.0F);
		crutilka3_r1.cubeList.add(new ModelBox(crutilka3_r1, 0, 0, 11.0F, 17.0F, -44.0F, 39, 2, 34, 0.0F, false));

		crutilka3_r2 = new ModelRenderer(this);
		crutilka3_r2.setRotationPoint(0.0F, -5.0F, -5.0F);
		crutilki.addChild(crutilka3_r2);
		setRotationAngle(crutilka3_r2, -1.5708F, 0.5672F, 0.0F);
		crutilka3_r2.cubeList.add(new ModelBox(crutilka3_r2, 0, 0, -50.0F, 16.0F, -44.0F, 39, 2, 34, 0.0F, false));

		crutilka3_r3 = new ModelRenderer(this);
		crutilka3_r3.setRotationPoint(0.0F, -5.0F, -5.0F);
		crutilki.addChild(crutilka3_r3);
		setRotationAngle(crutilka3_r3, -1.5708F, 0.0F, 0.0F);
		crutilka3_r3.cubeList.add(new ModelBox(crutilka3_r3, 0, 0, -20.0F, 7.0F, -44.0F, 42, 2, 34, 0.0F, false));

		crutilka3_r4 = new ModelRenderer(this);
		crutilka3_r4.setRotationPoint(0.0F, -2.0F, -5.0F);
		crutilki.addChild(crutilka3_r4);
		setRotationAngle(crutilka3_r4, -1.5708F, 0.0F, 0.0F);
		crutilka3_r4.cubeList.add(new ModelBox(crutilka3_r4, 93, 188, -2.0F, 8.0F, -24.0F, 4, 4, 14, 0.0F, false));

		crutilka2_r1 = new ModelRenderer(this);
		crutilka2_r1.setRotationPoint(0.0F, -1.0F, 0.0F);
		crutilki.addChild(crutilka2_r1);
		setRotationAngle(crutilka2_r1, 1.1345F, 0.0F, 0.0F);
		crutilka2_r1.cubeList.add(new ModelBox(crutilka2_r1, 78, 222, -3.0F, 14.0F, 32.0F, 6, 3, 8, 0.0F, false));

		crutilka2_r2 = new ModelRenderer(this);
		crutilka2_r2.setRotationPoint(0.0F, -1.0F, -5.0F);
		crutilki.addChild(crutilka2_r2);
		setRotationAngle(crutilka2_r2, 0.2182F, 0.0F, 0.0F);
		crutilka2_r2.cubeList.add(new ModelBox(crutilka2_r2, 78, 222, -3.0F, -17.0F, -7.0F, 6, 2, 44, 0.0F, false));

		crutilka2_r3 = new ModelRenderer(this);
		crutilka2_r3.setRotationPoint(0.0F, -2.0F, -5.0F);
		crutilki.addChild(crutilka2_r3);
		setRotationAngle(crutilka2_r3, -0.48F, 0.0F, 0.0F);
		crutilka2_r3.cubeList.add(new ModelBox(crutilka2_r3, 174, 209, -2.0F, -6.0F, -15.0F, 4, 2, 17, 0.0F, false));

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.cubeList.add(new ModelBox(bb_main, 6, 191, -12.0F, -2.0F, -20.0F, 24, 2, 39, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 8, 142, -8.0F, -4.0F, -7.0F, 16, 2, 14, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 90, 102, -3.0F, -6.0F, -3.0F, 6, 5, 9, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 8, 142, -28.0F, -2.0F, -7.0F, 16, 2, 14, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 6, 142, 12.0F, -2.0F, -7.0F, 16, 2, 14, 0.0F, false));
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
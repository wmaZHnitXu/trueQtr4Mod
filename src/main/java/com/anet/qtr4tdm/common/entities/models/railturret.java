package com.anet.qtr4tdm.common.entities.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


public class railturret extends ModelBase {
	private final ModelRenderer base;
	private final ModelRenderer cube_r1;
	private final ModelRenderer turret;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer gun;
	private final ModelRenderer cube_r6;
	private final ModelRenderer cube_r7;
	private final ModelRenderer kazennik;
	private final ModelRenderer arrayRight;
	private final ModelRenderer bone11;
	private final ModelRenderer bone12;
	private final ModelRenderer bone13;
	private final ModelRenderer bone14;
	private final ModelRenderer bone15;
	private final ModelRenderer bone16;
	private final ModelRenderer bone17;
	private final ModelRenderer bone18;
	private final ModelRenderer bone19;
	private final ModelRenderer bone20;
	private final ModelRenderer arrayLeft;
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;
	private final ModelRenderer bone4;
	private final ModelRenderer bone5;
	private final ModelRenderer bone6;
	private final ModelRenderer bone7;
	private final ModelRenderer bone8;
	private final ModelRenderer bone9;
	private final ModelRenderer bone10;

	public railturret() {
		textureWidth = 256;
		textureHeight = 256;

		base = new ModelRenderer(this);
		base.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		base.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 52, 63, 17.0F, -2.0F, -12.0F, 7, 2, 24, 0.0F, false));
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 87, -24.0F, -2.0F, -12.0F, 7, 2, 24, 0.0F, false));
		cube_r1.cubeList.add(new ModelBox(cube_r1, 90, 66, -12.0F, -2.0F, 17.0F, 24, 2, 7, 0.0F, false));
		cube_r1.cubeList.add(new ModelBox(cube_r1, 90, 75, -12.0F, -2.0F, -24.0F, 24, 2, 7, 0.0F, false));
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, -17.0F, -2.0F, -17.0F, 34, 2, 34, 0.0F, false));

		turret = new ModelRenderer(this);
		turret.setRotationPoint(-0.0714F, 17.0582F, 0.0F);
		setRotationAngle(turret, 0.0F, 0.0F, 0.0F);
		

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
		turret.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 184, 69, -25.0F, -24.0F, -3.0F, 32, 4, 6, 0.0F, false));
		cube_r2.cubeList.add(new ModelBox(cube_r2, 97, 157, -29.0F, -20.0F, -11.0F, 29, 11, 22, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
		turret.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.0F, -0.2182F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 186, 131, -1.0F, -20.0F, -15.0F, 20, 16, 12, 0.0F, false));
		cube_r3.cubeList.add(new ModelBox(cube_r3, 182, 29, -1.0F, -20.0F, 3.0F, 20, 16, 12, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(-2.9286F, 6.9418F, 0.0F);
		turret.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.0F, 0.5672F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 57, 149, -19.0F, -4.0F, -8.0F, 12, 4, 16, 0.0F, false));

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(-2.9286F, 6.9418F, 0.0F);
		turret.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 0.0F);
		cube_r5.cubeList.add(new ModelBox(cube_r5, 108, 109, -19.0F, -16.0F, -9.0F, 12, 8, 18, 0.0F, false));
		cube_r5.cubeList.add(new ModelBox(cube_r5, 104, 196, -7.0F, -16.0F, -9.0F, 24, 13, 18, 0.0F, false));
		cube_r5.cubeList.add(new ModelBox(cube_r5, 0, 36, -7.0F, -3.0F, -13.0F, 20, 1, 26, 0.0F, false));

		gun = new ModelRenderer(this);
		gun.setRotationPoint(0.0714F, -14.0582F, 0.0F);
		turret.addChild(gun);
		

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(-0.0714F, 14.0582F, 0.0F);
		gun.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, 0.0F, 0.0F);
		cube_r6.cubeList.add(new ModelBox(cube_r6, 103, 112, -2.0F, -19.0F, -2.0F, 12, 10, 4, 0.0F, false));

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(0.0F, 14.0F, 0.0F);
		gun.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.0F, 0.0F, 0.0F);
		cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 248, 10.0F, -19.0F, -2.0F, 52, 4, 4, 0.0F, false));
		cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 90, -1.0F, -16.0F, -1.0F, 17, 4, 2, 0.0F, false));
		cube_r7.cubeList.add(new ModelBox(cube_r7, 146, 231, 10.0F, -15.0F, -1.0F, 51, 2, 2, 0.0F, false));
		cube_r7.cubeList.add(new ModelBox(cube_r7, 142, 236, 10.0F, -13.0F, -2.0F, 52, 4, 4, 0.0F, false));

		kazennik = new ModelRenderer(this);
		kazennik.setRotationPoint(7.9286F, -1.9418F, 0.0F);
		gun.addChild(kazennik);
		

		arrayRight = new ModelRenderer(this);
		arrayRight.setRotationPoint(20.0F, 0.0F, -1.0F);
		gun.addChild(arrayRight);
		setRotationAngle(arrayRight, 3.1416F, 0.0F, 0.0F);
		

		bone11 = new ModelRenderer(this);
		bone11.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayRight.addChild(bone11);
		bone11.cubeList.add(new ModelBox(bone11, 228, 188, 0.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone12 = new ModelRenderer(this);
		bone12.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayRight.addChild(bone12);
		bone12.cubeList.add(new ModelBox(bone12, 228, 188, 4.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone13 = new ModelRenderer(this);
		bone13.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayRight.addChild(bone13);
		bone13.cubeList.add(new ModelBox(bone13, 228, 188, 8.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone14 = new ModelRenderer(this);
		bone14.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayRight.addChild(bone14);
		bone14.cubeList.add(new ModelBox(bone14, 228, 188, 12.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone15 = new ModelRenderer(this);
		bone15.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayRight.addChild(bone15);
		bone15.cubeList.add(new ModelBox(bone15, 228, 188, 16.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone16 = new ModelRenderer(this);
		bone16.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayRight.addChild(bone16);
		bone16.cubeList.add(new ModelBox(bone16, 228, 188, 20.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone17 = new ModelRenderer(this);
		bone17.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayRight.addChild(bone17);
		bone17.cubeList.add(new ModelBox(bone17, 228, 188, 24.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone18 = new ModelRenderer(this);
		bone18.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayRight.addChild(bone18);
		bone18.cubeList.add(new ModelBox(bone18, 228, 188, 28.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone19 = new ModelRenderer(this);
		bone19.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayRight.addChild(bone19);
		bone19.cubeList.add(new ModelBox(bone19, 228, 188, 32.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone20 = new ModelRenderer(this);
		bone20.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayRight.addChild(bone20);
		bone20.cubeList.add(new ModelBox(bone20, 228, 188, 36.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		arrayLeft = new ModelRenderer(this);
		arrayLeft.setRotationPoint(20.0F, 0.0F, 1.0F);
		gun.addChild(arrayLeft);
		

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayLeft.addChild(bone);
		bone.cubeList.add(new ModelBox(bone, 228, 188, 0.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayLeft.addChild(bone2);
		bone2.cubeList.add(new ModelBox(bone2, 228, 188, 4.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayLeft.addChild(bone3);
		bone3.cubeList.add(new ModelBox(bone3, 228, 188, 8.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayLeft.addChild(bone4);
		bone4.cubeList.add(new ModelBox(bone4, 228, 188, 12.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayLeft.addChild(bone5);
		bone5.cubeList.add(new ModelBox(bone5, 228, 188, 16.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayLeft.addChild(bone6);
		bone6.cubeList.add(new ModelBox(bone6, 228, 188, 20.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone7 = new ModelRenderer(this);
		bone7.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayLeft.addChild(bone7);
		bone7.cubeList.add(new ModelBox(bone7, 228, 188, 24.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone8 = new ModelRenderer(this);
		bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayLeft.addChild(bone8);
		bone8.cubeList.add(new ModelBox(bone8, 228, 188, 28.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone9 = new ModelRenderer(this);
		bone9.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayLeft.addChild(bone9);
		bone9.cubeList.add(new ModelBox(bone9, 228, 188, 32.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));

		bone10 = new ModelRenderer(this);
		bone10.setRotationPoint(0.0F, 0.0F, 0.0F);
		arrayLeft.addChild(bone10);
		bone10.cubeList.add(new ModelBox(bone10, 228, 188, 36.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F, false));
	}
	public void setTurretAndGunRotation (float yaw, float pitch) {
		setRotationAngle(turret, 0, yaw, 0);
		setRotationAngle(gun, 0, 0, pitch);
	}

	public void setArraysCharge (float charge) {

		for (int i = 0; i < arrayLeft.childModels.size(); i++) {

			ModelRenderer rendLeft = arrayLeft.childModels.get(i);
			ModelRenderer rendRight = arrayRight.childModels.get(i);

			float offset = 0.1f - Math.min(0.1f, Math.max(0f, charge * 100 - 50 - i * 4.5f) * 0.1f);

			rendLeft.offsetZ = offset;
			rendRight.offsetZ = offset;
		}

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		base.render(f5);
		turret.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
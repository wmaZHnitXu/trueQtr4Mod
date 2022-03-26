package com.anet.qtr4tdm.common.entities.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


public class cannonturret extends ModelBase {
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
	private final ModelRenderer cube_r8;

	public cannonturret() {
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
		cube_r2.cubeList.add(new ModelBox(cube_r2, 184, 69, -25.0F, -24.0F, -3.0F, 13, 4, 6, 0.0F, false));
		cube_r2.cubeList.add(new ModelBox(cube_r2, 97, 157, -29.0F, -20.0F, -11.0F, 29, 11, 22, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
		turret.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.0F, -0.2182F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 186, 131, -1.0F, -12.0F, -15.0F, 20, 8, 12, 0.0F, false));
		cube_r3.cubeList.add(new ModelBox(cube_r3, 182, 29, -1.0F, -12.0F, 3.0F, 20, 8, 12, 0.0F, false));

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
		cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 203, 10.0F, -16.0F, -3.0F, 52, 4, 1, 0.0F, false));
		cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 248, 10.0F, -17.0F, -2.0F, 52, 1, 4, 0.0F, false));
		cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 90, 13.0F, -16.0F, -2.0F, 3, 4, 4, 0.0F, false));
		cube_r7.cubeList.add(new ModelBox(cube_r7, 142, 236, 10.0F, -12.0F, -2.0F, 52, 1, 4, 0.0F, false));
		cube_r7.cubeList.add(new ModelBox(cube_r7, 150, 250, 10.0F, -16.0F, 2.0F, 52, 4, 1, 0.0F, false));

		kazennik = new ModelRenderer(this);
		kazennik.setRotationPoint(7.9286F, -1.9418F, 0.0F);
		gun.addChild(kazennik);
		

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(-8.0F, 16.0F, 0.0F);
		kazennik.addChild(cube_r8);
		setRotationAngle(cube_r8, 0.0F, 0.0F, 0.2182F);
		cube_r8.cubeList.add(new ModelBox(cube_r8, 178, 88, -17.0F, -22.0F, -4.0F, 25, 8, 8, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		base.render(f5);
		turret.render(f5);
	}

	public void setTurretAndGunRotation (float yaw, float pitch) {
		setRotationAngle(turret, 0, yaw, 0);
		setRotationAngle(gun, 0, 0, pitch);
	}

	public void setGunKnockBack (float knockBack) {
		for (ModelRenderer rend : gun.childModels) {
			rend.offsetX = -knockBack;
		}
		setRotationAngle(kazennik, 0, 0, knockBack);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
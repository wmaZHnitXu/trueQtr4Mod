package com.anet.qtr4tdm.common.entities.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

// Made with Blockbench 4.1.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


public class laserturret extends ModelBase {
	private final ModelRenderer base;
	private final ModelRenderer turret;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer gun;

	public laserturret() {
		textureWidth = 256;
		textureHeight = 256;

		base = new ModelRenderer(this);
		base.setRotationPoint(0.0F, 24.0F, 0.0F);
		base.cubeList.add(new ModelBox(base, 0, 0, -17.0F, -2.0F, -17.0F, 34, 2, 34, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 90, 75, -12.0F, -2.0F, -24.0F, 24, 2, 7, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 90, 66, -12.0F, -2.0F, 17.0F, 24, 2, 7, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 0, 87, -24.0F, -2.0F, -12.0F, 7, 2, 24, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 52, 63, 17.0F, -2.0F, -12.0F, 7, 2, 24, 0.0F, false));

		turret = new ModelRenderer(this);
		turret.setRotationPoint(-0.0714F, 17.0582F, 0.0F);
		setRotationAngle(turret, 0.0F, 0.0F, 0.0F);
		turret.cubeList.add(new ModelBox(turret, 0, 36, -9.9286F, 3.9418F, -13.0F, 20, 1, 26, 0.0F, false));
		turret.cubeList.add(new ModelBox(turret, 80, 89, -0.9286F, 1.9418F, -9.0F, 9, 2, 18, 0.0F, false));
		turret.cubeList.add(new ModelBox(turret, 108, 109, -1.9286F, 2.9418F, -9.0F, 1, 1, 18, 0.0F, false));
		turret.cubeList.add(new ModelBox(turret, 0, 113, -5.9286F, -10.0582F, -13.0F, 11, 14, 4, 0.0F, false));
		turret.cubeList.add(new ModelBox(turret, 0, 0, -5.9286F, -10.0582F, 9.0F, 11, 14, 4, 0.0F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(-2.9286F, 6.9418F, 3.0F);
		turret.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.1745F, 0.0F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 103, 0, -7.0F, -15.0F, 9.0F, 20, 15, 4, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(-2.9286F, 6.9418F, -3.0F);
		turret.addChild(cube_r2);
		setRotationAngle(cube_r2, -0.1745F, 0.0F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 59, 109, -7.0F, -15.0F, -13.0F, 20, 15, 4, 0.0F, false));

		gun = new ModelRenderer(this);
		gun.setRotationPoint(0.0714F, -8.0582F, 0.0F);
		turret.addChild(gun);
		gun.cubeList.add(new ModelBox(gun, 0, 63, -9.0F, -2.0F, -9.0F, 20, 6, 18, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 38, 89, -12.0F, -7.0F, -7.0F, 18, 5, 12, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 0, 18, 6.0F, -7.0F, -4.0F, 3, 5, 9, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 68, 36, -11.0F, -11.0F, -8.0F, 20, 4, 17, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 90, 57, -11.0F, -14.0F, 1.0F, 25, 3, 6, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 102, 19, -5.0F, -7.0F, 5.0F, 14, 5, 5, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 0, 55, -5.0F, -6.0F, -9.0F, 10, 4, 2, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 0, 36, 9.0F, -11.0F, -3.0F, 4, 10, 9, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 24, 24, 13.0F, -7.0F, 2.0F, 4, 6, 1, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 0, 63, 13.0F, -2.0F, -3.0F, 4, 1, 5, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 0, 90, 13.0F, -6.0F, -2.0F, 3, 4, 4, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 15, 18, 13.0F, -7.0F, -3.0F, 4, 1, 5, 0.0F, false));
		gun.cubeList.add(new ModelBox(gun, 58, 63, 13.0F, -6.0F, -3.0F, 4, 4, 1, 0.0F, false));
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
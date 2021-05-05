package com.anet.qtr4tdm.common.visual;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports

@SideOnly(Side.CLIENT)
public class radar1lvlModel extends ModelBase {
	private final ModelRenderer bb_main;
	private final ModelRenderer base_r1;
	private final ModelRenderer base_r2;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;

	public radar1lvlModel() {
		textureWidth = 64;
		textureHeight = 64;

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -8.0F, -2.0F, -8.0F, 16, 2, 16, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 18, -7.0F, -41.0F, 5.0F, 2, 40, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 18, 5.0F, -41.0F, 5.0F, 2, 40, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 18, -7.0F, -41.0F, -7.0F, 2, 40, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 18, 5.0F, -41.0F, -7.0F, 2, 40, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -7.0F, -42.0F, -7.0F, 14, 1, 14, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 18, -6.0F, -82.0F, 5.0F, 1, 40, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 18, 5.0F, -82.0F, 5.0F, 1, 40, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 18, 5.0F, -82.0F, -6.0F, 1, 40, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 18, -6.0F, -82.0F, -6.0F, 1, 40, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -6.0F, -88.0F, -6.0F, 12, 6, 12, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 43, 31, -2.0F, -104.0F, -2.0F, 4, 16, 4, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 13, 18, 4.5F, -48.0F, -7.5F, 3, 12, 3, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 13, 18, 4.5F, -48.0F, 4.5F, 3, 12, 3, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 13, 18, -7.5F, -48.0F, -7.5F, 3, 12, 3, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 13, 18, -7.5F, -48.0F, 4.5F, 3, 12, 3, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 34, -1.0F, -124.0F, -1.0F, 2, 20, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 26, 39, -1.0F, -132.0F, -1.0F, 2, 8, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 14, 5, -5.0F, -1.0F, 8.0F, 10, 1, 7, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 13, 1, -5.0F, -1.0F, -15.0F, 10, 1, 7, 0.0F, false));

		base_r1 = new ModelRenderer(this);
		base_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		bb_main.addChild(base_r1);
		setRotationAngle(base_r1, 0.0F, -1.5708F, 0.0F);
		base_r1.cubeList.add(new ModelBox(base_r1, 13, 7, -5.0F, -1.0F, -15.0F, 10, 1, 7, 0.0F, false));

		base_r2 = new ModelRenderer(this);
		base_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
		bb_main.addChild(base_r2);
		setRotationAngle(base_r2, 0.0F, 1.5708F, 0.0F);
		base_r2.cubeList.add(new ModelBox(base_r2, 13, 5, -5.0F, -1.0F, -15.0F, 10, 1, 7, 0.0F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		bb_main.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.2618F, 1.5708F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 18, 4.75F, -81.0F, 16.0F, 1, 41, 1, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
		bb_main.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.2618F, 3.1416F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 18, -6.25F, -81.0F, 16.0F, 1, 41, 1, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
		bb_main.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.2618F, -1.5708F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 18, 4.75F, -81.0F, 16.0F, 1, 41, 1, 0.0F, false));
		cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 18, 5.5F, -41.0F, 5.0F, 1, 41, 2, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(0.0F, 0.0F, 0.0F);
		bb_main.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.2618F, 0.0F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 18, 4.75F, -81.0F, 16.0F, 1, 41, 1, 0.0F, false));
		cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 18, 5.5F, -41.0F, 5.0F, 1, 41, 2, 0.0F, false));

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(0.0F, 0.0F, 0.0F);
		bb_main.addChild(cube_r5);
		setRotationAngle(cube_r5, -0.2618F, -1.5708F, 0.0F);
		cube_r5.cubeList.add(new ModelBox(cube_r5, 0, 18, -6.5F, -41.0F, -7.0F, 1, 41, 2, 0.0F, false));

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(0.0F, 0.0F, 0.0F);
		bb_main.addChild(cube_r6);
		setRotationAngle(cube_r6, -0.2618F, 0.0F, 0.0F);
		cube_r6.cubeList.add(new ModelBox(cube_r6, 0, 18, -6.5F, -41.0F, -7.0F, 1, 41, 2, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bb_main.render(f5);
	}

	public void renderAll () {
		bb_main.render(0.0625F);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
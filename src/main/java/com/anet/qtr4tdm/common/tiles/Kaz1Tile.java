package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.blocks.Kaz1Block;
import com.anet.qtr4tdm.common.blocks.Kaz2Block;
import com.anet.qtr4tdm.common.entities.KazAmmoEntity;
import com.anet.qtr4tdm.common.items.KAZAmmoItem;
import com.anet.qtr4tdm.common.supers.TEDefenceInvEnrg;
import com.anet.qtr4tdm.init.BlocksInit;
import com.anet.qtr4tdm.uebki.Sounds;
import com.anet.qtr4tdm.uebki.gui.KAZGuiMisc.kazContainer;
import com.flansmod.common.guns.BulletType;
import com.flansmod.common.guns.EntityBullet;

import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.MinecraftForge;
import scala.reflect.internal.Trees.Return;

public class Kaz1Tile extends TEDefenceInvEnrg {

    public boolean connected;
    public boolean powered;
    private int maxAmmo = 3;
    private int range = 20;
    public List<ItemStack> ammo;

    private int level = 1;
    private int interval = 10;
    private double energy = 0;
    private double maxEnergy = 100;
    private double usage = 20;
    private int ticksexisted;
    public kazContainer container;
    public Entity target;
    private int cooldown = 0;
    public int highlightTime = 0;
    private int ammometatype;

    protected static ArrayList<EntityBullet> lockedOn;

    //TEMPTEMP
    private int currentammo;

    public Kaz1Tile () {
        if (lockedOn == null) lockedOn = new ArrayList<EntityBullet>();
    }

    public Kaz1Tile(int level) {
        this();
        LevelInit(level);
    }

    private void LevelInit (int level) {
        this.level = level;
        switch (level) {
            case 1:
                range = 20;
                maxAmmo = 3;
                interval = 10;
                usage = 20;
                maxEnergy = 100;
                ammometatype = 0;
            break;
            case 2:
                range = 20;
                maxAmmo = 5;
                interval = 7;
                usage = 50;
                maxEnergy = 200;
                ammometatype = 1;
            break;
        } 
    }

    @Override
    public void onLoad() {
        if (ammo == null) {
            ammo = new ArrayList<ItemStack>();
            ammo.add(0, ItemStack.EMPTY);
        }
        else {
            int lvl = 1;
            if (world.getBlockState(pos).getBlock() instanceof Kaz2Block) lvl = 2;
            LevelInit(lvl);
        }
        currentammo = world.getBlockState(pos).getValue(Kaz1Block.AMMO);
        DisconnectFromBase();
        InWorldBasesManager.GetBaseConnection(this);
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
    }

    @Override
    public void invalidate() {
        //MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.invalidate();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("ammocount", ammo.get(0).getCount());
        compound.setInteger("ammometa", ammo.get(0).getItemDamage());
        compound.setInteger("level", level);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        LevelInit(compound.getInteger("level"));
        super.readFromNBT(compound);
    }

    @Override
    public void SetTargetsFromBase(ArrayList<Entity> targets) {

    }

    public void Interaction (EntityPlayer player) {
        if (!world.isRemote)
            player.openGui(TdmMod.instance, TdmMod.GUI_KAZ, world, pos.getX(), pos.getY(), pos.getZ());
    }

    public void Destruction () {
        DisconnectFromBase();
        InventoryHelper.dropInventoryItems(world, pos, this);
    }

    public int getAmmoCount () {
        return ammo.get(0).getCount();
    }

    @Override
    public void Refresh() {
        
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            connected = base != null;
            if (ticksexisted % interval == 0 && cooldown == 0) {
                if (energy > 0 && getAmmoCount() > 0) {
                    energy = Math.max(energy-usage, 0);
                    SearchForTargets();
                }
            }
            if (cooldown == 1) {
                IBlockState state = world.getBlockState(pos);
                world.setBlockState(pos, state, 2);
            }
            if (cooldown > 0) cooldown--;
            powered = energy > 0;
            doAmmoUpdate();
        }
        else {
            if (highlightTime>0) highlightTime--;
        }
        ticksexisted++;
    }

    public void SearchForTargets () {
        AxisAlignedBB bb = new AxisAlignedBB(pos.add(range, 1, range), pos.add(-range, range, -range));
        List<EntityBullet> bullets = world.getEntitiesWithinAABB(EntityBullet.class, bb);
        for (EntityBullet bullet : bullets) {
            BulletType type = bullet.shot.getBulletType();
            if ((type.explodeOnImpact || type.emp) && !lockedOn.contains(bullet) && !bullet.isDead) {
                target = bullet;
                Launch();
                lockedOn.add(bullet);
                return;
            }
        }
    }

    public void Launch () {
        KazAmmoEntity charge = new KazAmmoEntity(world);
        charge.setPositionAndRotation(pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d, world.getBlockState(pos).getValue(Kaz1Block.FACING).getHorizontalAngle(), 0);
        charge.SetTarget(target);
        world.spawnEntity(charge);
        world.playSound(null, pos, Sounds.active_protection_shot, SoundCategory.BLOCKS, 4f, 1f);
        ammo.get(0).setCount(ammo.get(0).getCount()-1);
        cooldown = 20;
    }

    public static void RefreshLocked () {
        for (int i = 0; i < lockedOn.size(); i++) {
            Entity e = lockedOn.get(i);
            if (e.isDead) {
                lockedOn.remove(i);
                i--;
            }
        }
    }

    @Override
    protected void doAmmoUpdate () {
        int _ammo = ammo.get(0).getCount();
        if (_ammo != currentammo) {
                Kaz1Block.SetAmmo(world, pos, _ammo);
                currentammo = _ammo;
                markDirty();
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return ammo.get(0).isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return ammo.get(0);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(ammo, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(ammo, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.ammo.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
        markDirty(); 
    }

    @Override
    public int getInventoryStackLimit() {
        return maxAmmo;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack != null && stack.getItem() instanceof KAZAmmoItem && stack.getItemDamage() == ammometatype;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: return connected ? 1 : 0;
            case 1: return powered ? 1 : 0;
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: connected = value==1; break;
            case 1: powered = value==1; break;
        }
    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    public void clear() {
        ammo.set(0, ItemStack.EMPTY);
        markDirty();
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasCustomName() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side != EnumFacing.UP) {
            return new int[]{0};
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (direction != EnumFacing.UP && itemStackIn != null && isItemValidForSlot(0, itemStackIn) && index == 0 && (ammo.get(0).getCount() < maxAmmo)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing side) {
        if (side != EnumFacing.UP) return true;
        return false;
    }

    @Override
    public Item GetAmmoType() {
        return BlocksInit.KAZAMMO;
    }

    @Override
    public int getPoints() {
        return 45;
    }

    @Override
    public int getRange() {
        return 25;
    }

    @Override
    public int getConsumptionPerTick() {
        return 0;
    }
}

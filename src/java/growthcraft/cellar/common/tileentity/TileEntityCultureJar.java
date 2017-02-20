/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.cellar.common.tileentity;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.common.fluids.CellarTank;
import growthcraft.cellar.common.inventory.ContainerCultureJar;
import growthcraft.cellar.common.tileentity.component.TileHeatingComponent;
import growthcraft.cellar.common.tileentity.device.CultureGenerator;
import growthcraft.cellar.common.tileentity.device.YeastGenerator;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.device.DeviceProgressive;
import growthcraft.core.common.tileentity.event.TileEventHandler;
import growthcraft.core.common.tileentity.feature.ITileHeatedDevice;
import growthcraft.core.common.tileentity.feature.ITileProgressiveDevice;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.io.IOException;

public class TileEntityCultureJar extends TileEntityCellarDevice implements ITileHeatedDevice, ITileProgressiveDevice {
    private static final int[] accessibleSlots = new int[]{0};
    private TileHeatingComponent heatComponent;
    private CultureGenerator cultureGen;
    private YeastGenerator yeastGen;
    private int jarDeviceState;

    public TileEntityCultureJar() {
        super();
        this.heatComponent = new TileHeatingComponent(this, 0.0f);
        this.cultureGen = new CultureGenerator(this, heatComponent, 0, 0);
        this.yeastGen = new YeastGenerator(this, 0, 0);
        this.yeastGen.setTimeMax(GrowthCraftCellar.getConfig().cultureJarTimeMax);
        this.yeastGen.setConsumption(GrowthCraftCellar.getConfig().cultureJarConsumption);
    }

    public boolean isHeated() {
        return cultureGen.isHeated();
    }

    public float getHeatMultiplier() {
        return cultureGen.getHeatMultiplier();
    }

    public boolean isCulturing() {
        return jarDeviceState == 1;
    }

    private DeviceProgressive getActiveDevice() {
        if (cultureGen.isHeated()) {
            return cultureGen;
        }
        return yeastGen;
    }

    private DeviceProgressive getActiveClientDevice() {
        if (jarDeviceState == 1) {
            return cultureGen;
        }
        return yeastGen;
    }

    @Override
    protected FluidTank[] createTanks() {
        final int maxTankCap = GrowthCraftCellar.getConfig().cultureJarMaxCap;
        return new FluidTank[]{new CellarTank(maxTankCap, this)};
    }

    @Override
    public GrcInternalInventory createInventory() {
        return new GrcInternalInventory(this, 1);
    }

    @Override
    public String getDefaultInventoryName() {
        return "container.grc.CultureJar";
    }

    @Override
    public String getGuiID() {
        return "grccellar:culture_jar";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerCultureJar(playerInventory, this);
    }

    @Override
    protected void markFluidDirty() {
        // Ferment Jars need to update their rendering state when a fluid
        // changes, most of the other cellar blocks are unaffected by this
        markForUpdate();
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return accessibleSlots;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, int side) {
        return index == 0;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, int side) {
        return false;
    }

    @Override
    protected int doFill(EnumFacing from, FluidStack resource, boolean shouldFill) {
        return fillFluidTank(0, resource, shouldFill);
    }

    @Override
    protected FluidStack doDrain(EnumFacing from, int maxDrain, boolean shouldDrain) {
        return drainFluidTank(0, maxDrain, shouldDrain);
    }

    @Override
    protected FluidStack doDrain(EnumFacing from, FluidStack resource, boolean shouldDrain) {
        if (resource == null || !resource.isFluidEqual(getFluidTank(0).getFluid())) {
            return null;
        }
        return doDrain(from, resource.amount, shouldDrain);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!worldObj.isRemote) {
            heatComponent.update();
            final int lastState = jarDeviceState;
            final DeviceProgressive prog = getActiveDevice();
            if (prog == cultureGen) {
                this.jarDeviceState = 1;
                yeastGen.resetTime();
            } else {
                this.jarDeviceState = 0;
                cultureGen.resetTime();
            }
            getActiveDevice().update();
            if (jarDeviceState != lastState) {
                markDirtyAndUpdate();
            }
        }
    }

    @Override
    public void receiveGUINetworkData(int id, int v) {
        super.receiveGUINetworkData(id, v);
        switch (CultureJarDataId.getByOrdinal(id)) {
            case YEAST_GEN_TIME:
                yeastGen.setTime(v);
                break;
            case YEAST_GEN_TIME_MAX:
                yeastGen.setTimeMax(v);
                break;
            case CULTURE_GEN_TIME:
                cultureGen.setTime(v);
                break;
            case CULTURE_GEN_TIME_MAX:
                cultureGen.setTimeMax(v);
                break;
            case HEAT_AMOUNT:
                heatComponent.setHeatMultiplier((float) v / (float) 0x7FFF);
                break;
            default:
                // should warn about invalid Data ID
                break;
        }
    }

    @Override
    public void sendGUINetworkData(Container container, ICrafting iCrafting) {
        super.sendGUINetworkData(container, iCrafting);
        iCrafting.sendProgressBarUpdate(container, CultureJarDataId.YEAST_GEN_TIME.ordinal(), yeastGen.getTime());
        iCrafting.sendProgressBarUpdate(container, CultureJarDataId.YEAST_GEN_TIME_MAX.ordinal(), yeastGen.getTimeMax());
        iCrafting.sendProgressBarUpdate(container, CultureJarDataId.CULTURE_GEN_TIME.ordinal(), cultureGen.getTime());
        iCrafting.sendProgressBarUpdate(container, CultureJarDataId.CULTURE_GEN_TIME_MAX.ordinal(), cultureGen.getTimeMax());
        iCrafting.sendProgressBarUpdate(container, CultureJarDataId.HEAT_AMOUNT.ordinal(), (int) (heatComponent.getHeatMultiplier() * 0x7FFF));
    }

    @TileEventHandler(event = TileEventHandler.EventType.NBT_READ)
    public void readFromNBT_CultureJar(NBTTagCompound nbt) {
        yeastGen.readFromNBT(nbt, "yeastgen");
        cultureGen.readFromNBT(nbt, "culture_gen");
        heatComponent.readFromNBT(nbt, "heat_component");
    }

    @TileEventHandler(event = TileEventHandler.EventType.NBT_WRITE)
    public void writeToNBT_CultureJar(NBTTagCompound nbt) {
        yeastGen.writeToNBT(nbt, "yeastgen");
        cultureGen.writeToNBT(nbt, "culture_gen");
        heatComponent.writeToNBT(nbt, "heat_component");
    }

    @TileEventHandler(event = TileEventHandler.EventType.NETWORK_READ)
    public boolean readFromStream_YeastGen(ByteBuf stream) throws IOException {
        this.jarDeviceState = stream.readInt();
        yeastGen.readFromStream(stream);
        cultureGen.readFromStream(stream);
        heatComponent.readFromStream(stream);
        return false;
    }

    @TileEventHandler(event = TileEventHandler.EventType.NETWORK_WRITE)
    public boolean writeToStream_YeastGen(ByteBuf stream) throws IOException {
        stream.writeInt(jarDeviceState);
        yeastGen.writeToStream(stream);
        cultureGen.writeToStream(stream);
        heatComponent.writeToStream(stream);
        return false;
    }

    @Override
    public int getHeatScaled(int scale) {
        return (int) (scale * MathHelper.clamp_float(getHeatMultiplier(), 0f, 1f));
    }

    @Override
    public float getDeviceProgress() {
        return getActiveClientDevice().getProgress();
    }

    @Override
    public int getDeviceProgressScaled(int scale) {
        return getActiveClientDevice().getProgressScaled(scale);
    }

    public enum CultureJarDataId {
        YEAST_GEN_TIME,
        YEAST_GEN_TIME_MAX,
        CULTURE_GEN_TIME,
        CULTURE_GEN_TIME_MAX,
        HEAT_AMOUNT,
        UNKNOWN;

        public static final CultureJarDataId[] VALID = new CultureJarDataId[]
                {
                        YEAST_GEN_TIME,
                        YEAST_GEN_TIME_MAX,
                        CULTURE_GEN_TIME,
                        CULTURE_GEN_TIME_MAX,
                        HEAT_AMOUNT
                };

        public static CultureJarDataId getByOrdinal(int ord) {
            if (ord >= 0 && ord < VALID.length) return VALID[ord];
            return UNKNOWN;
        }
    }
}

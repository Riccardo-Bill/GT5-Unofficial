package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTESolarTower;

public class MTESolarTowerGui extends MTEMultiBlockBaseGui<MTESolarTower> {

    public MTESolarTowerGui(MTESolarTower multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("solarTowerHeat", new IntSyncValue(multiblock::getHeatLevel));
        syncManager.syncValue("solarTowerReflectors", new IntSyncValue(multiblock.mSolarHeaters::size));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {

        IntSyncValue heatSyncer = syncManager.findSyncHandler("solarTowerHeat", IntSyncValue.class);
        IntSyncValue reflectorsSyncer = syncManager.findSyncHandler("solarTowerReflectors", IntSyncValue.class);

        String towerHeat = IGregTechDeviceInformation.encode(
            "gt.infoData.solar_tower.heat",
            formatNumber(heatSyncer.getValue()),
            formatNumber(multiblock.getHeatCap()));
        String towerReflectors = IGregTechDeviceInformation
            .encode("gt.infoData.solar_tower.reflectors", String.valueOf(reflectorsSyncer.getValue()));

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(() -> EnumChatFormatting.GRAY + IGregTechDeviceInformation.decode(towerHeat))
                    .asWidget()
                    .fullWidth()
                    .marginBottom(2)
                    .setEnabledIf(w -> baseMetaTileEntity.isActive()))
            .child(
                IKey.dynamic(() -> EnumChatFormatting.GRAY + IGregTechDeviceInformation.decode(towerReflectors))
                    .asWidget()
                    .fullWidth()
                    .marginBottom(2)
                    .setEnabledIf(w -> baseMetaTileEntity.isActive()));
    }
}

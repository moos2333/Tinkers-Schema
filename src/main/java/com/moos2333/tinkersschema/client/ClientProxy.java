package com.moos2333.tinkersschema.client;

import com.moos2333.tinkersschema.common.CommonProxy;
import com.moos2333.tinkersschema.config.ToolConfig;
import com.moos2333.tinkersschema.tools.DynamicTool;
import com.moos2333.tinkersschema.tools.DynamicToolRegister;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.tools.ToolCore;

import java.util.List;

public class ClientProxy extends CommonProxy {

    private static final int[][] DEFAULT_SLOTS = {
            {12, 62}, {48, 26}, {30, 44}, {48, 62}, {12, 26}, {30, 26}
    };

    @Override
    public void initToolGuis() {
        List<ToolCore> tools = DynamicToolRegister.getDynamicTools();
        if (tools.isEmpty()) return;
        for (ToolCore tool : tools) {
            if (!(tool instanceof DynamicTool)) continue;
            ToolConfig config = ((DynamicTool) tool).getConfig();
            List<String> slots = config.getGuiSlots();
            int count = config.getParts().size();
            ToolBuildGuiInfo info = new ToolBuildGuiInfo(tool);
            for (int i = 0; i < count; i++) {
                int[] pos = null;
                if (slots != null && i < slots.size()) {
                    pos = SlotResolver.resolve(slots.get(i));
                }
                if (pos == null) {
                    pos = DEFAULT_SLOTS[i % DEFAULT_SLOTS.length];
                }
                info.addSlotPosition(pos[0], pos[1]);
            }
            TinkerRegistryClient.addToolBuilding(info);
        }
    }
}
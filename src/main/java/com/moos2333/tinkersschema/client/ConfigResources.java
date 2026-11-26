package com.moos2333.tinkersschema.client;

import com.moos2333.tinkersschema.Tags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public final class ConfigResources implements ISelectiveResourceReloadListener {

    private final IResourcePack pack;
    private final boolean lang;

    private ConfigResources(File dir, boolean lang) {
        this.pack = new FolderResourcePack(dir);
        this.lang = lang;
    }

    public static void mount(File configDir) {
        File dir = new File(configDir, Tags.MOD_ID + "/resources");
        File assets = new File(dir, "assets");
        if (!assets.isDirectory()) return;
        IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
        if (!(manager instanceof SimpleReloadableResourceManager)) return;
        ((SimpleReloadableResourceManager) manager).registerReloadListener(new ConfigResources(dir, hasLang(assets)));
    }

    private static boolean hasLang(File assets) {
        File[] namespaces = assets.listFiles(File::isDirectory);
        if (namespaces == null) return false;
        for (File namespace : namespaces) {
            if (new File(namespace, "lang").isDirectory()) return true;
        }
        return false;
    }

    @Override
    public void onResourceManagerReload(IResourceManager manager, Predicate<IResourceType> types) {
        boolean loaded = !manager.getResourceDomains().isEmpty();
        ((SimpleReloadableResourceManager) manager).reloadResourcePack(pack);
        if (loaded && lang && types.test(VanillaResourceType.LANGUAGES)) {
            Minecraft.getMinecraft().getLanguageManager().onResourceManagerReload(manager);
        }
    }
}

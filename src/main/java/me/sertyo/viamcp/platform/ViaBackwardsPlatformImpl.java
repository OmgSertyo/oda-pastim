package me.sertyo.viamcp.platform;

import com.viaversion.viabackwards.api.ViaBackwardsPlatform;
import me.sertyo.viamcp.ViaLoadingBase;

import java.io.File;
import java.util.logging.Logger;

public class ViaBackwardsPlatformImpl implements ViaBackwardsPlatform {

    private final File directory;

    public ViaBackwardsPlatformImpl(File directory) {
        this.directory = directory;
        init(this.directory);
    }

    @Override
    public Logger getLogger() {
        return ViaLoadingBase.LOGGER;
    }

    @Override
    public boolean isOutdated() {
        return false;
    }

    @Override
    public void disable() {
    }

    @Override
    public File getDataFolder() {
        return this.directory;
    }
}
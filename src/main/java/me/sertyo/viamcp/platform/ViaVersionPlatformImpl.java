package me.sertyo.viamcp.platform;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.UnsupportedSoftware;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.gson.JsonObject;
import me.sertyo.viamcp.ViaLoadingBase;
import me.sertyo.viamcp.platform.viaversion.ViaAPIWrapper;
import me.sertyo.viamcp.platform.viaversion.ViaConfig;
import me.sertyo.viamcp.util.ViaTask;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ViaVersionPlatformImpl implements ViaPlatform<UUID> {

    private final ViaAPI<UUID> api = new ViaAPIWrapper();
    private final Logger logger;
    private final ViaConfig config;

    public ViaVersionPlatformImpl(Logger logger) {
        this.logger = logger;
        config = new ViaConfig(new File(ViaLoadingBase.getInstance().getRunDirectory(), "viaversion.yml"));
    }

    public static List<ProtocolVersion> createVersionList() {
        List<ProtocolVersion> versions = new ArrayList<>(ProtocolVersion.getProtocols()).stream().filter(protocolVersion -> protocolVersion != ProtocolVersion.unknown && ProtocolVersion.getProtocols().indexOf(protocolVersion) >= 7).collect(Collectors.toList());
        Collections.reverse(versions);
        return versions;
    }

    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        return new ViaCommandSender[0];
    }

    @Override
    public void sendMessage(UUID uuid, String msg) {
        if (uuid == null) {
            getLogger().info(msg);
        } else {
            getLogger().info("[" + uuid + "] " + msg);
        }
    }

    @Override
    public boolean kickPlayer(UUID uuid, String s) {
        return false;
    }

    @Override
    public boolean disconnect(UserConnection connection, String message) {
        return ViaPlatform.super.disconnect(connection, message);
    }

    @Override
    public ViaTask runAsync(Runnable runnable) {
        return new ViaTask(Via.getManager().getScheduler().execute(runnable));
    }

    @Override
    public ViaTask runRepeatingAsync(Runnable runnable, long ticks) {
        return new ViaTask(Via.getManager().getScheduler().scheduleRepeating(runnable, 0L, ticks * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public ViaTask runSync(Runnable runnable) {
        return runAsync(runnable);
    }

    @Override
    public ViaTask runSync(Runnable runnable, long ticks) {
        return new ViaTask(Via.getManager().getScheduler().schedule(runnable, ticks * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public ViaTask runRepeatingSync(Runnable runnable, long ticks) {
        return this.runRepeatingAsync(runnable, ticks);
    }

    @Override
    public boolean isProxy() {
        return true;
    }

    @Override
    public void onReload() {}

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public ViaVersionConfig getConf() {
        return config;
    }

    @Override
    public ConfigurationProvider getConfigurationProvider() {
        return config;
    }

    @Override
    public ViaAPI<UUID> getApi() {
        return api;
    }

    @Override
    public File getDataFolder() {
        return ViaLoadingBase.getInstance().getRunDirectory();
    }

    @Override
    public String getPluginVersion() {
        return "4.7.0";
    }

    @Override
    public String getPlatformName() {
        return "ViaVersion";
    }

    @Override
    public String getPlatformVersion() {
        return "${vialoadingbase_version}";
    }

    @Override
    public boolean isPluginEnabled() {
        return true;
    }

    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }

    @Override
    public Collection<UnsupportedSoftware> getUnsupportedSoftwareClasses() {
        return ViaPlatform.super.getUnsupportedSoftwareClasses();
    }

    @Override
    public boolean hasPlugin(String s) {
        return false;
    }

    @Override
    public JsonObject getDump() {
        if (ViaLoadingBase.getInstance().getDumpSupplier() == null) return new JsonObject();

        return ViaLoadingBase.getInstance().getDumpSupplier().get();
    }
}
package me.sertyo.viamcp;

import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.protocol.ProtocolManagerImpl;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import me.sertyo.viamcp.model.ComparableProtocolVersion;
import me.sertyo.viamcp.model.Platform;
import me.sertyo.viamcp.platform.ViaBackwardsPlatformImpl;
import me.sertyo.viamcp.platform.ViaVersionPlatformImpl;
import me.sertyo.viamcp.platform.viaversion.ViaInjector;
import me.sertyo.viamcp.platform.viaversion.ViaProviders;
import me.sertyo.viamcp.util.JLoggerToLog4j;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class ViaLoadingBase {

    public static Logger LOGGER = new JLoggerToLog4j(LogManager.getLogger("ViaVersion"));
    public static Platform PSEUDO_VIA_VERSION = new Platform("ViaVersion", () -> true, () -> {}, protocolVersions -> protocolVersions.addAll(ViaVersionPlatformImpl.createVersionList()));
    public static Platform PLATFORM_VIA_BACKWARDS = new Platform("ViaBackwards", () -> ViaLoadingBase.inClassPath("com.viaversion.viabackwards.api.ViaBackwardsPlatform"), () -> new ViaBackwardsPlatformImpl(Via.getManager().getPlatform().getDataFolder()));
    public static Map<ProtocolVersion, ComparableProtocolVersion> PROTOCOLS = new LinkedHashMap<>();
    @Getter
    private static ViaLoadingBase instance;
    private final LinkedList<Platform> platforms;
    @Getter
    private final File runDirectory;
    private final int nativeVersion;
    private final BooleanSupplier forceNativeVersionCondition;
    @Getter
    private final Supplier<JsonObject> dumpSupplier;
    @Getter
    private final Consumer<com.viaversion.viaversion.api.platform.providers.ViaProviders> providers;
    private final Consumer<ViaManagerImpl.ViaManagerBuilder> managerBuilderConsumer;
    private final Consumer<ComparableProtocolVersion> onProtocolReload;
    private ComparableProtocolVersion nativeProtocolVersion;
    private ComparableProtocolVersion targetProtocolVersion;

    public ViaLoadingBase(LinkedList<Platform> platforms, File runDirectory, int nativeVersion, BooleanSupplier forceNativeVersionCondition, Supplier<JsonObject> dumpSupplier, Consumer<com.viaversion.viaversion.api.platform.providers.ViaProviders> providers, Consumer<ViaManagerImpl.ViaManagerBuilder> managerBuilderConsumer, Consumer<ComparableProtocolVersion> onProtocolReload) {
        this.platforms = platforms;
        this.runDirectory = new File(runDirectory, "viaversion");
        this.nativeVersion = nativeVersion;
        this.forceNativeVersionCondition = forceNativeVersionCondition;
        this.dumpSupplier = dumpSupplier;
        this.providers = providers;
        this.managerBuilderConsumer = managerBuilderConsumer;
        this.onProtocolReload = onProtocolReload;
        instance = this;
        initPlatform();
    }

    public ComparableProtocolVersion getTargetVersion() {
        if (forceNativeVersionCondition != null && forceNativeVersionCondition.getAsBoolean()) {
            return nativeProtocolVersion;
        }

        return targetProtocolVersion;
    }

    public void reload(ProtocolVersion protocolVersion) {
        reload(ViaLoadingBase.fromProtocolVersion(protocolVersion));
    }

    public void reload(ComparableProtocolVersion protocolVersion) {
        targetProtocolVersion = protocolVersion;

        if (onProtocolReload != null) {
            onProtocolReload.accept(targetProtocolVersion);
        }
    }

    public void initPlatform() {
        for (Platform platform : platforms) {
            platform.createProtocolPath();
        }
        for (ProtocolVersion preProtocol : Platform.TEMP_INPUT_PROTOCOLS) {
            PROTOCOLS.put(preProtocol, new ComparableProtocolVersion(preProtocol.getVersion(), preProtocol.getName(), Platform.TEMP_INPUT_PROTOCOLS.indexOf(preProtocol)));
        }
        targetProtocolVersion = nativeProtocolVersion = ViaLoadingBase.fromProtocolVersion(ProtocolVersion.getProtocol(nativeVersion));
        ViaVersionPlatformImpl viaVersionPlatform = new ViaVersionPlatformImpl(LOGGER);
        ViaManagerImpl.ViaManagerBuilder builder = ViaManagerImpl.builder().platform(viaVersionPlatform).loader(new ViaProviders()).injector(new ViaInjector());
        if (managerBuilderConsumer != null) {
            managerBuilderConsumer.accept(builder);
        }
        Via.init(builder.build());
        ViaManagerImpl manager = (ViaManagerImpl) Via.getManager();
        manager.addEnableListener(() -> {
            for (Platform platform : platforms) {
                platform.build(LOGGER);
            }
        });
        manager.init();
        manager.onServerLoaded();
        manager.getProtocolManager().setMaxProtocolPathSize(Integer.MAX_VALUE);
        manager.getProtocolManager().setMaxPathDeltaIncrease(-1);
        ((ProtocolManagerImpl) manager.getProtocolManager()).refreshVersions();
    }

    public static boolean inClassPath(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static ComparableProtocolVersion fromProtocolVersion(ProtocolVersion protocolVersion) {
        return PROTOCOLS.get(protocolVersion);
    }

    public static class ViaLoadingBaseBuilder {

        private final LinkedList<Platform> platforms = new LinkedList<>();
        private File runDirectory;
        private Integer nativeVersion;
        private BooleanSupplier forceNativeVersionCondition;
        private Supplier<JsonObject> dumpSupplier;
        private Consumer<com.viaversion.viaversion.api.platform.providers.ViaProviders> providers;
        private Consumer<ViaManagerImpl.ViaManagerBuilder> managerBuilderConsumer;
        private Consumer<ComparableProtocolVersion> onProtocolReload;

        public ViaLoadingBaseBuilder() {
            platforms.add(PSEUDO_VIA_VERSION);
            platforms.add(PLATFORM_VIA_BACKWARDS);
        }

        public static ViaLoadingBaseBuilder create() {
            return new ViaLoadingBaseBuilder();
        }

        public ViaLoadingBaseBuilder platform(Platform platform) {
            platforms.add(platform);
            return this;
        }

        public ViaLoadingBaseBuilder platform(Platform platform, int position) {
            platforms.add(position, platform);
            return this;
        }

        public ViaLoadingBaseBuilder runDirectory(File runDirectory) {
            this.runDirectory = runDirectory;
            return this;
        }

        public ViaLoadingBaseBuilder nativeVersion(int nativeVersion) {
            this.nativeVersion = nativeVersion;
            return this;
        }

        public ViaLoadingBaseBuilder forceNativeVersionCondition(BooleanSupplier forceNativeVersionCondition) {
            this.forceNativeVersionCondition = forceNativeVersionCondition;
            return this;
        }

        public ViaLoadingBaseBuilder dumpSupplier(Supplier<JsonObject> dumpSupplier) {
            this.dumpSupplier = dumpSupplier;
            return this;
        }

        public ViaLoadingBaseBuilder providers(Consumer<com.viaversion.viaversion.api.platform.providers.ViaProviders> providers) {
            this.providers = providers;
            return this;
        }

        public ViaLoadingBaseBuilder managerBuilderConsumer(Consumer<ViaManagerImpl.ViaManagerBuilder> managerBuilderConsumer) {
            this.managerBuilderConsumer = managerBuilderConsumer;
            return this;
        }

        public ViaLoadingBaseBuilder onProtocolReload(Consumer<ComparableProtocolVersion> onProtocolReload) {
            this.onProtocolReload = onProtocolReload;
            return this;
        }

        public void build() {
            if (ViaLoadingBase.getInstance() != null) {
                return;
            }
            if (runDirectory == null || nativeVersion == null) {
                return;
            }
            new ViaLoadingBase(platforms, runDirectory, nativeVersion, forceNativeVersionCondition, dumpSupplier, providers, managerBuilderConsumer, onProtocolReload);
        }
    }
}
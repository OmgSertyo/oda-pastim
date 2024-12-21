package me.sertyo.viamcp.model;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Platform {

    public static int COUNT = 0;
    public static List<ProtocolVersion> TEMP_INPUT_PROTOCOLS = new ArrayList<>();
    @Getter
    private final String name;
    private final BooleanSupplier load;
    private final Runnable executor;
    private final Consumer<List<ProtocolVersion>> versionCallback;

    public Platform(String name, BooleanSupplier load, Runnable executor) {
        this(name, load, executor, null);
    }

    public Platform(String name, BooleanSupplier load, Runnable executor, Consumer<List<ProtocolVersion>> versionCallback) {
        this.name = name;
        this.load = load;
        this.executor = executor;
        this.versionCallback = versionCallback;
    }

    public void createProtocolPath() {
        if (versionCallback != null) versionCallback.accept(TEMP_INPUT_PROTOCOLS);
    }

    public void build(Logger logger) {
        if (load.getAsBoolean()) {
            try {
                executor.run();
                logger.info("Loaded Platform " + name);
                ++COUNT;
            } catch (Throwable t) {
                logger.severe("An error occurred while loading Platform " + name + ":");
                t.printStackTrace();
            }

            return;
        }

        logger.severe("Platform " + name + " is not present");
    }
}
package me.sertyo.viamcp;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import me.sertyo.api.ApiConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViaMCP {

    public List<ProtocolVersion> PROTOCOLS;
    public VersionSelectScreen viaScreen;
    public int NATIVE_VERSION = 754;

    public ViaMCP() {
        List<ProtocolVersion> protocolList = ProtocolVersion.getProtocols().stream().filter(pv -> pv.getVersion() == 47 || pv.getVersion() >= 107).sorted((f, s) -> Integer.compare(s.getVersion(), f.getVersion())).toList();
        PROTOCOLS = new ArrayList<>(protocolList.size() + 1);
        PROTOCOLS.addAll(protocolList);
        ViaLoadingBase.ViaLoadingBaseBuilder.create().runDirectory(new File(ApiConstants.MAIN_DIRECTORY.getAbsolutePath())).nativeVersion(NATIVE_VERSION).build();
        viaScreen = new VersionSelectScreen(Minecraft.getInstance().fontRenderer, 5, 5, 100, 20, ITextComponent.getTextComponentOrEmpty("1.16.5"));
    }
}
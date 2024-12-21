package me.sertyo.viamcp.model;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import lombok.Getter;
import me.sertyo.viamcp.ViaLoadingBase;

@Getter
public class ComparableProtocolVersion extends ProtocolVersion {

    private final int index;

    public ComparableProtocolVersion(int version, String name, int index) {
        super(version, name);
        this.index = index;
    }

    public boolean isOlderThan(ProtocolVersion other) {
        return getIndex() > ViaLoadingBase.fromProtocolVersion(other).getIndex();
    }

    public boolean isOlderThanOrEqualTo(ProtocolVersion other) {
        return getIndex() >= ViaLoadingBase.fromProtocolVersion(other).getIndex();
    }

    public boolean isNewerThan(ProtocolVersion other) {
        return getIndex() < ViaLoadingBase.fromProtocolVersion(other).getIndex();
    }

    public boolean isNewerThanOrEqualTo(ProtocolVersion other) {
        return getIndex() <= ViaLoadingBase.fromProtocolVersion(other).getIndex();
    }

    public boolean isEqualTo(ProtocolVersion other) {
        return getIndex() == ViaLoadingBase.fromProtocolVersion(other).getIndex();
    }
}
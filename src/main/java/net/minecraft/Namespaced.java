package net.minecraft;

import com.google.gson.*;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import me.sertyo.cheat.IResourceLoader;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public class Namespaced implements Comparable<Namespaced>
{
    public static final Codec<Namespaced> CODEC = Codec.STRING.comapFlatMap(Namespaced::decodeResourceLocation, Namespaced::toString).stable();
    private static final SimpleCommandExceptionType INVALID_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent("argument.id.invalid"));
    protected final String namespace;
    protected final String path;
    private IResourceLoader resourceLoader;

    protected Namespaced(String[] resourceParts)
    {
        this.namespace = org.apache.commons.lang3.StringUtils.isEmpty(resourceParts[0]) ? "minecraft" : resourceParts[0];
        this.path = resourceParts[1];

        if (this.path.equals("DUMMY"))
        {
            if (!isValidNamespace(this.namespace))
            {
                throw new ResourceLocationException("Non [a-z0-9_.-] character in namespace of location: " + this.namespace + ':' + this.path);
            }
            else if (!isPathValid(this.path))
            {
                throw new ResourceLocationException("Non [a-z0-9/._-] character in path of location: " + this.namespace + ':' + this.path);
            }
        }
    }

    public Namespaced(IResourceLoader resourceLoader) {
        this("", "");
        this.resourceLoader = resourceLoader;
    }

    public IResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

    public Namespaced(String resourceName)
    {
        this(decompose(resourceName, ':'));
    }

    public Namespaced(String namespaceIn, String pathIn)
    {
        this(new String[] {namespaceIn, pathIn});
    }

    /**
     * Constructs a ResourceLocation by splitting a String representation of a valid location on a specified character.
     */
    public static Namespaced create(String resourceName, char splitOn)
    {
        return new Namespaced(decompose(resourceName, splitOn));
    }

    @Nullable
    public static Namespaced tryCreate(String string)
    {
        try
        {
            return new Namespaced(string);
        }
        catch (ResourceLocationException resourcelocationexception)
        {
            return null;
        }
    }

    protected static String[] decompose(String resourceName, char splitOn)
    {
        String[] astring = new String[] {"minecraft", resourceName};
        int i = resourceName.indexOf(splitOn);

        if (i >= 0)
        {
            astring[1] = resourceName.substring(i + 1, resourceName.length());

            if (i >= 1)
            {
                astring[0] = resourceName.substring(0, i);
            }
        }

        return astring;
    }

    private static DataResult<Namespaced> decodeResourceLocation(String encoded)
    {
        try
        {
            return DataResult.success(new Namespaced(encoded));
        }
        catch (ResourceLocationException resourcelocationexception)
        {
            return DataResult.error("Not a valid resource location: " + encoded + " " + resourcelocationexception.getMessage());
        }
    }

    public String getPath()
    {
        return this.path;
    }

    public String getNamespace()
    {
        return this.namespace;
    }

    public String toString()
    {
        return this.namespace + ':' + this.path;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof Namespaced))
        {
            return false;
        }
        else
        {
            Namespaced resourcelocation = (Namespaced)p_equals_1_;
            return this.namespace.equals(resourcelocation.namespace) && this.path.equals(resourcelocation.path);
        }
    }

    public int hashCode()
    {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    public int compareTo(Namespaced p_compareTo_1_)
    {
        int i = this.path.compareTo(p_compareTo_1_.path);

        if (i == 0)
        {
            i = this.namespace.compareTo(p_compareTo_1_.namespace);
        }

        return i;
    }

    public static Namespaced read(StringReader reader) throws CommandSyntaxException
    {
        int i = reader.getCursor();

        while (reader.canRead() && isValidPathCharacter(reader.peek()))
        {
            reader.skip();
        }

        String s = reader.getString().substring(i, reader.getCursor());

        try
        {
            return new Namespaced(s);
        }
        catch (ResourceLocationException resourcelocationexception)
        {
            reader.setCursor(i);
            throw INVALID_EXCEPTION.createWithContext(reader);
        }
    }

    public static boolean isValidPathCharacter(char charIn)
    {
        return charIn >= '0' && charIn <= '9' || charIn >= 'a' && charIn <= 'z' || charIn == '_' || charIn == ':' || charIn == '/' || charIn == '.' || charIn == '-';
    }

    /**
     * Checks if the path contains invalid characters.
     */
    private static boolean isPathValid(String pathIn)
    {
        for (int i = 0; i < pathIn.length(); ++i)
        {
            if (!validatePathChar(pathIn.charAt(i)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if given namespace only consists of allowed characters.
     */
    private static boolean isValidNamespace(String namespaceIn)
    {
        for (int i = 0; i < namespaceIn.length(); ++i)
        {
            if (!validateNamespaceChar(namespaceIn.charAt(i)))
            {
                return false;
            }
        }

        return true;
    }

    public static boolean validatePathChar(char charValue)
    {
        return charValue == '_' || charValue == '-' || charValue >= 'a' && charValue <= 'z' || charValue >= '0' && charValue <= '9' || charValue == '/' || charValue == '.';
    }

    private static boolean validateNamespaceChar(char charValue)
    {
        return charValue == '_' || charValue == '-' || charValue >= 'a' && charValue <= 'z' || charValue >= '0' && charValue <= '9' || charValue == '.';
    }

    /**
     * Checks if the specified resource name (namespace and path) contains invalid characters.
     */
    public static boolean isResouceNameValid(String resourceName)
    {
        String[] astring = decompose(resourceName, ':');
        return isValidNamespace(org.apache.commons.lang3.StringUtils.isEmpty(astring[0]) ? "minecraft" : astring[0]) && isPathValid(astring[1]);
    }

    public int compareNamespaced(Namespaced p_compareNamespaced_1_)
    {
        int i = this.namespace.compareTo(p_compareNamespaced_1_.namespace);
        return i != 0 ? i : this.path.compareTo(p_compareNamespaced_1_.path);
    }

    public static class Serializer implements JsonDeserializer<Namespaced>, JsonSerializer<Namespaced>
    {
        public Namespaced deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            return new Namespaced(JSONUtils.getString(p_deserialize_1_, "location"));
        }

        public JsonElement serialize(Namespaced p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            return new JsonPrimitive(p_serialize_1_.toString());
        }
    }
}

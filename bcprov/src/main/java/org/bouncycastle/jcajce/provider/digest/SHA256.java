package org.bouncycastle.jcajce.provider.digest;

import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.bouncycastle.jcajce.provider.symmetric.util.PBESecretKeyFactory;

public class SHA256
{
    private SHA256()
    {

    }

    static public class Digest
        extends BCMessageDigest
        implements Cloneable
    {
        public Digest()
        {
            super(SHA256Digest.newInstance());
        }

        public Object clone()
            throws CloneNotSupportedException
        {
            Digest d = (Digest)super.clone();
            d.digest = SHA256Digest.newInstance(digest);

            return d;
        }
    }

    public static class HashMac
        extends BaseMac
    {
        public HashMac()
        {
            super(new HMac(SHA256Digest.newInstance()));
        }
    }

    // BEGIN Android-removed: Unsupported algorithms
    /*
    /**
     * PBEWithHmacSHA
     *
    public static class PBEWithMacKeyFactory
        extends PBESecretKeyFactory
    {
        public PBEWithMacKeyFactory()
        {
            super("PBEwithHmacSHA256", null, false, PKCS12, SHA256, 256, 0);
        }
    }
    */
    // END Android-removed: Unsupported algorithms

    /**
     * HMACSHA256
     */
    public static class KeyGenerator
        extends BaseKeyGenerator
    {
        public KeyGenerator()
        {
            super("HMACSHA256", 256, new CipherKeyGenerator());
        }
    }

    public static class Mappings
        extends DigestAlgorithmProvider
    {
        private static final String PREFIX = SHA256.class.getName();

        public Mappings()
        {
        }

        public void configure(ConfigurableProvider provider)
        {
            // BEGIN Android-removed: Unsupported algorithms
            /*
            provider.addAlgorithm("MessageDigest.SHA-256", PREFIX + "$Digest");
            provider.addAlgorithm("Alg.Alias.MessageDigest.SHA256", "SHA-256");
            provider.addAlgorithm("Alg.Alias.MessageDigest." + NISTObjectIdentifiers.id_sha256, "SHA-256");

            provider.addAlgorithm("SecretKeyFactory.PBEWITHHMACSHA256", PREFIX + "$PBEWithMacKeyFactory");
            provider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHHMACSHA-256", "PBEWITHHMACSHA256");
            provider.addAlgorithm("Alg.Alias.SecretKeyFactory." + NISTObjectIdentifiers.id_sha256, "PBEWITHHMACSHA256");

            provider.addAlgorithm("Mac.PBEWITHHMACSHA256", PREFIX + "$HashMac");

            addHMACAlgorithm(provider, "SHA256", PREFIX + "$HashMac",  PREFIX + "$KeyGenerator");
            addHMACAlias(provider, "SHA256", PKCSObjectIdentifiers.id_hmacWithSHA256);
            addHMACAlias(provider, "SHA256", NISTObjectIdentifiers.id_sha256);
            */
            // END Android-removed: Unsupported algorithms
            // Android-added: Private implementation needed to support PBKDF2 with PKCS#12
            provider.addPrivateAlgorithm("Mac", NISTObjectIdentifiers.id_sha256, PREFIX + "$HashMac");
        }
    }
}

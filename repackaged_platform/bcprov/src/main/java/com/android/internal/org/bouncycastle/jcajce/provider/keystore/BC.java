/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.internal.org.bouncycastle.jcajce.provider.keystore;

import com.android.internal.org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import com.android.internal.org.bouncycastle.jcajce.provider.util.AsymmetricAlgorithmProvider;
import com.android.internal.org.bouncycastle.util.Properties;

/**
 * @hide This class is not part of the Android public SDK API
 */
public class BC
{
    private static final String PREFIX = "com.android.internal.org.bouncycastle.jcajce.provider.keystore" + ".bc.";

    /**
     * @hide This class is not part of the Android public SDK API
     */
    public static class Mappings
        extends AsymmetricAlgorithmProvider
    {
        public Mappings()
        {
        }

        public void configure(ConfigurableProvider provider)
        {
            provider.addAlgorithm("KeyStore.BKS", PREFIX + "BcKeyStoreSpi$Std");
            // Android-removed: Unsupported algorithms
            // provider.addAlgorithm("KeyStore.BKS-V1", PREFIX + "BcKeyStoreSpi$Version1");
            provider.addAlgorithm("KeyStore.BouncyCastle", PREFIX + "BcKeyStoreSpi$BouncyCastleStore");
            provider.addAlgorithm("Alg.Alias.KeyStore.UBER", "BouncyCastle");
            provider.addAlgorithm("Alg.Alias.KeyStore.BOUNCYCASTLE", "BouncyCastle");
            provider.addAlgorithm("Alg.Alias.KeyStore.bouncycastle", "BouncyCastle");
        }
    }
}

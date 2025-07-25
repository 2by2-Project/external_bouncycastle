/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.internal.org.bouncycastle.jcajce.provider.asymmetric;

import com.android.internal.org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import com.android.internal.org.bouncycastle.jcajce.provider.asymmetric.dsa.DSAUtil;
import com.android.internal.org.bouncycastle.jcajce.provider.asymmetric.dsa.KeyFactorySpi;
import com.android.internal.org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import com.android.internal.org.bouncycastle.jcajce.provider.util.AsymmetricAlgorithmProvider;
import com.android.internal.org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;

/**
 * @hide This class is not part of the Android public SDK API
 */
public class DSA
{
    private static final String PREFIX = "com.android.internal.org.bouncycastle.jcajce.provider.asymmetric" + ".dsa.";

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
            provider.addAlgorithm("AlgorithmParameters.DSA", PREFIX + "AlgorithmParametersSpi");

            provider.addAlgorithm("AlgorithmParameterGenerator.DSA", PREFIX + "AlgorithmParameterGeneratorSpi");

            provider.addAlgorithm("KeyPairGenerator.DSA", PREFIX + "KeyPairGeneratorSpi");
            provider.addAlgorithm("KeyFactory.DSA", PREFIX + "KeyFactorySpi");

            // BEGIN Android-changed: Change primary ID from DSA to SHA1withDSA
            // provider.addAlgorithm("Signature.DSA", PREFIX + "DSASigner$stdDSA");
            provider.addAlgorithm("Signature.SHA1withDSA", PREFIX + "DSASigner$stdDSA");
            provider.addAlgorithm("Alg.Alias.Signature.DSA", "SHA1withDSA");
            // END Android-changed: Change primary ID from DSA to SHA1withDSA
            provider.addAlgorithm("Signature.NONEWITHDSA", PREFIX + "DSASigner$noneDSA");

            provider.addAlgorithm("Alg.Alias.Signature.RAWDSA", "NONEWITHDSA");

            // BEGIN Android-removed: Unsupported algorithms
            /*
            provider.addAlgorithm("Signature.DETDSA", PREFIX + "DSASigner$detDSA");
            provider.addAlgorithm("Signature.SHA1WITHDETDSA", PREFIX + "DSASigner$detDSA");
            provider.addAlgorithm("Signature.SHA224WITHDETDSA", PREFIX + "DSASigner$detDSA224");
            provider.addAlgorithm("Signature.SHA256WITHDETDSA", PREFIX + "DSASigner$detDSA256");
            provider.addAlgorithm("Signature.SHA384WITHDETDSA", PREFIX + "DSASigner$detDSA384");
            provider.addAlgorithm("Signature.SHA512WITHDETDSA", PREFIX + "DSASigner$detDSA512");

            provider.addAlgorithm("Signature.DDSA", PREFIX + "DSASigner$detDSA");
            provider.addAlgorithm("Signature.SHA1WITHDDSA", PREFIX + "DSASigner$detDSA");
            provider.addAlgorithm("Signature.SHA224WITHDDSA", PREFIX + "DSASigner$detDSA224");
            provider.addAlgorithm("Signature.SHA256WITHDDSA", PREFIX + "DSASigner$detDSA256");
            provider.addAlgorithm("Signature.SHA384WITHDDSA", PREFIX + "DSASigner$detDSA384");
            provider.addAlgorithm("Signature.SHA512WITHDDSA", PREFIX + "DSASigner$detDSA512");
            provider.addAlgorithm("Signature.SHA3-224WITHDDSA", PREFIX + "DSASigner$detDSASha3_224");
            provider.addAlgorithm("Signature.SHA3-256WITHDDSA", PREFIX + "DSASigner$detDSASha3_256");
            provider.addAlgorithm("Signature.SHA3-384WITHDDSA", PREFIX + "DSASigner$detDSASha3_384");
            provider.addAlgorithm("Signature.SHA3-512WITHDDSA", PREFIX + "DSASigner$detDSASha3_512");
            */
            // END Android-removed: Unsupported algorithms

            addSignatureAlgorithm(provider, "SHA224", "DSA", PREFIX + "DSASigner$dsa224", NISTObjectIdentifiers.dsa_with_sha224);
            addSignatureAlgorithm(provider, "SHA256", "DSA", PREFIX + "DSASigner$dsa256", NISTObjectIdentifiers.dsa_with_sha256);
            // BEGIN Android-removed: Unsupported algorithms
            /*
            addSignatureAlgorithm(provider, "SHA384", "DSA", PREFIX + "DSASigner$dsa384", NISTObjectIdentifiers.dsa_with_sha384);
            addSignatureAlgorithm(provider, "SHA512", "DSA", PREFIX + "DSASigner$dsa512", NISTObjectIdentifiers.dsa_with_sha512);

            addSignatureAlgorithm(provider, "SHA3-224", "DSA", PREFIX + "DSASigner$dsaSha3_224", NISTObjectIdentifiers.id_dsa_with_sha3_224);
            addSignatureAlgorithm(provider, "SHA3-256", "DSA", PREFIX + "DSASigner$dsaSha3_256", NISTObjectIdentifiers.id_dsa_with_sha3_256);
            addSignatureAlgorithm(provider, "SHA3-384", "DSA", PREFIX + "DSASigner$dsaSha3_384", NISTObjectIdentifiers.id_dsa_with_sha3_384);
            addSignatureAlgorithm(provider, "SHA3-512", "DSA", PREFIX + "DSASigner$dsaSha3_512", NISTObjectIdentifiers.id_dsa_with_sha3_512);
            */
            // END Android-removed: Unsupported algorithms

            // BEGIN Android-changed: Change primary ID from DSA to SHA1withDSA
            provider.addAlgorithm("Alg.Alias.Signature.SHA/DSA", "SHA1withDSA");
            provider.addAlgorithm("Alg.Alias.Signature.SHA1withDSA", "SHA1withDSA");
            provider.addAlgorithm("Alg.Alias.Signature.SHA1WITHDSA", "SHA1withDSA");
            provider.addAlgorithm("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.10040.4.1", "SHA1withDSA");
            provider.addAlgorithm("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.10040.4.3", "SHA1withDSA");
            provider.addAlgorithm("Alg.Alias.Signature.DSAwithSHA1", "SHA1withDSA");
            provider.addAlgorithm("Alg.Alias.Signature.DSAWITHSHA1", "SHA1withDSA");
            provider.addAlgorithm("Alg.Alias.Signature.SHA1WithDSA", "SHA1withDSA");
            provider.addAlgorithm("Alg.Alias.Signature.DSAWithSHA1", "SHA1withDSA");
            // END Android-changed: Change primary ID from DSA to SHA1withDSA

            // addSignatureAlgorithm(provider, "RIPEMD160", "DSA", PREFIX + "DSASigner$dsaRMD160");

            AsymmetricKeyInfoConverter keyFact = new KeyFactorySpi();

            for (int i = 0; i != DSAUtil.dsaOids.length; i++)
            {
                // BEGIN Android-changed: Change primary ID from DSA to SHA1withDSA
                provider.addAlgorithm("Alg.Alias.Signature." + DSAUtil.dsaOids[i], "SHA1withDSA");
                // END Android-changed: Change primary ID from DSA to SHA1withDSA

                registerOid(provider, DSAUtil.dsaOids[i], "DSA", keyFact);
                registerOidAlgorithmParameterGenerator(provider, DSAUtil.dsaOids[i], "DSA");
            }
        }
    }
}

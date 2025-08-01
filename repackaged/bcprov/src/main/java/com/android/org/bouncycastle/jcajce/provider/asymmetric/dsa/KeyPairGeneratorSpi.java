/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.org.bouncycastle.jcajce.provider.asymmetric.dsa;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.util.Hashtable;

import com.android.org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import com.android.org.bouncycastle.crypto.CryptoServicesRegistrar;
import com.android.org.bouncycastle.crypto.digests.SHA256Digest;
import com.android.org.bouncycastle.crypto.generators.DSAKeyPairGenerator;
import com.android.org.bouncycastle.crypto.generators.DSAParametersGenerator;
import com.android.org.bouncycastle.crypto.params.DSAKeyGenerationParameters;
import com.android.org.bouncycastle.crypto.params.DSAParameterGenerationParameters;
import com.android.org.bouncycastle.crypto.params.DSAParameters;
import com.android.org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import com.android.org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import com.android.org.bouncycastle.jcajce.provider.asymmetric.util.PrimeCertaintyCalculator;
import com.android.org.bouncycastle.jce.provider.BouncyCastleProvider;
import com.android.org.bouncycastle.util.Integers;
import com.android.org.bouncycastle.util.Properties;

/**
 * @hide This class is not part of the Android public SDK API
 */
public class KeyPairGeneratorSpi
    extends java.security.KeyPairGenerator
{
    private static Hashtable params = new Hashtable();
    private static Object    lock = new Object();

    DSAKeyGenerationParameters param;
    DSAKeyPairGenerator engine = new DSAKeyPairGenerator();
    // Android-changed: Change default strength to 1024
    // In 1.57, the default strength was changed to 2048.  We keep it at 1024 for app
    // compatibility, particularly because the default digest (SHA-1) doesn't have
    // a sufficiently long digest to work with 2048-bit keys.
    int strength = 1024;

    SecureRandom random = CryptoServicesRegistrar.getSecureRandom();
    boolean initialised = false;

    public KeyPairGeneratorSpi()
    {
        super("DSA");
    }

    public void initialize(
        int strength,
        SecureRandom random)
    {
        if (strength < 512 || strength > 4096 || ((strength < 1024) && strength % 64 != 0) || (strength >= 1024 && strength % 1024 != 0))
        {
            throw new InvalidParameterException("strength must be from 512 - 4096 and a multiple of 1024 above 1024");
        }

        // Android-added: Treat null SecureRandom as default
        if (random == null) {
            random = new SecureRandom();
        }

        DSAParameterSpec spec = BouncyCastleProvider.CONFIGURATION.getDSADefaultParameters(strength);

        if (spec != null)
        {
            param = new DSAKeyGenerationParameters(random, new DSAParameters(spec.getP(), spec.getQ(), spec.getG()));

            engine.init(param);
            this.initialised = true;
        }
        else
        {
            this.strength = strength;
            this.random = random;
            this.initialised = false;
        }
    }

    public void initialize(
        AlgorithmParameterSpec params,
        SecureRandom random)
        throws InvalidAlgorithmParameterException
    {
        if (!(params instanceof DSAParameterSpec))
        {
            throw new InvalidAlgorithmParameterException("parameter object not a DSAParameterSpec");
        }
        DSAParameterSpec dsaParams = (DSAParameterSpec)params;

        // Android-added: Treat null SecureRandom as default
        if (random == null) {
            random = new SecureRandom();
        }

        param = new DSAKeyGenerationParameters(random, new DSAParameters(dsaParams.getP(), dsaParams.getQ(), dsaParams.getG()));

        engine.init(param);
        initialised = true;
    }

    public KeyPair generateKeyPair()
    {
        if (!initialised)
        {
            Integer paramStrength = Integers.valueOf(strength);

            if (params.containsKey(paramStrength))
            {
                param = (DSAKeyGenerationParameters)params.get(paramStrength);
            }
            else
            {
                synchronized (lock)
                {
                    // we do the check again in case we were blocked by a generator for
                    // our key size.
                    if (params.containsKey(paramStrength))
                    {
                        param = (DSAKeyGenerationParameters)params.get(paramStrength);
                    }
                    else
                    {
                        DSAParametersGenerator pGen;
                        DSAParameterGenerationParameters dsaParams;

                        int certainty = PrimeCertaintyCalculator.getDefaultCertainty(strength);

                        // Typical combination of keysize and size of q.
                        //     keysize = 1024, q's size = 160
                        //     keysize = 2048, q's size = 224
                        //     keysize = 2048, q's size = 256
                        //     keysize = 3072, q's size = 256
                        // For simplicity if keysize is greater than 1024 then we choose q's size to be 256.
                        // For legacy keysize that is less than 1024-bit, we just use the 186-2 style parameters
                        if (strength == 1024)
                        {
                            pGen = new DSAParametersGenerator();
                            if (Properties.isOverrideSet("com.android.org.bouncycastle.dsa.FIPS186-2for1024bits"))
                            {
                                pGen.init(strength, certainty, random);
                            }
                            else
                            {
                                dsaParams = new DSAParameterGenerationParameters(1024, 160, certainty, random);
                                pGen.init(dsaParams);
                            }
                        }
                        else if (strength > 1024)
                        {
                            dsaParams = new DSAParameterGenerationParameters(strength, 256, certainty, random);
                            pGen = new DSAParametersGenerator(SHA256Digest.newInstance());
                            pGen.init(dsaParams);
                        }
                        else
                        {
                            pGen = new DSAParametersGenerator();
                            pGen.init(strength, certainty, random);
                        }
                        param = new DSAKeyGenerationParameters(random, pGen.generateParameters());

                        params.put(paramStrength, param);
                    }
                }
            }

            engine.init(param);
            initialised = true;
        }

        AsymmetricCipherKeyPair pair = engine.generateKeyPair();
        DSAPublicKeyParameters pub = (DSAPublicKeyParameters)pair.getPublic();
        DSAPrivateKeyParameters priv = (DSAPrivateKeyParameters)pair.getPrivate();

        return new KeyPair(new BCDSAPublicKey(pub), new BCDSAPrivateKey(priv));
    }
}

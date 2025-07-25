/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.MacSpi;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;

import com.android.org.bouncycastle.crypto.CipherParameters;
import com.android.org.bouncycastle.crypto.Mac;
import com.android.org.bouncycastle.crypto.macs.HMac;
import com.android.org.bouncycastle.crypto.params.AEADParameters;
import com.android.org.bouncycastle.crypto.params.KeyParameter;
import com.android.org.bouncycastle.crypto.params.ParametersWithIV;
// Android-removed: Unsupported algorithms
// import org.bouncycastle.crypto.params.RC2Parameters;
// import org.bouncycastle.crypto.params.SkeinParameters;
import com.android.org.bouncycastle.jcajce.PKCS12Key;
import com.android.org.bouncycastle.jcajce.spec.AEADParameterSpec;
// Android-removed: Unsupported algorithms
// import org.bouncycastle.jcajce.spec.SkeinParameterSpec;

/**
 * @hide This class is not part of the Android public SDK API
 */
public class BaseMac
    extends MacSpi implements PBE
{
    private Mac macEngine;

    private int scheme = PKCS12;
    private int pbeHash = SHA1;
    private int keySize = 160;

    protected BaseMac(
        Mac macEngine)
    {
        this.macEngine = macEngine;
    }

    protected BaseMac(
        Mac macEngine,
        int scheme,
        int pbeHash,
        int keySize)
    {
        this.macEngine = macEngine;
        this.scheme = scheme;
        this.pbeHash = pbeHash;
        this.keySize = keySize;
    }

    protected void engineInit(
        Key                     key,
        final AlgorithmParameterSpec  params)
        throws InvalidKeyException, InvalidAlgorithmParameterException
    {
        CipherParameters        param;

        if (key == null)
        {
            throw new InvalidKeyException("key is null");
        }

        if (key instanceof PKCS12Key)
        {
            SecretKey k;
            PBEParameterSpec pbeSpec;

            try
            {
                k = (SecretKey)key;
            }
            catch (Exception e)
            {
                throw new InvalidKeyException("PKCS12 requires a SecretKey/PBEKey");
            }

            try
            {
                pbeSpec = (PBEParameterSpec)params;
            }
            catch (Exception e)
            {
                throw new InvalidAlgorithmParameterException("PKCS12 requires a PBEParameterSpec");
            }

            if (k instanceof PBEKey && pbeSpec == null)
            {
                pbeSpec = new PBEParameterSpec(((PBEKey)k).getSalt(), ((PBEKey)k).getIterationCount());
            }

            int digest = SHA1;
            int keySize = 160;
            // BEGIN Android-removed: Unsupported algorithms
            // if (macEngine.getAlgorithmName().startsWith("GOST"))
            // {
            //     digest = GOST3411;
            //     keySize = 256;
            // }
            // END Android-removed: Unsupported algorithms
            // Android-changed: Adjust for missing if
            // else if (macEngine instanceof HMac)
            if (macEngine instanceof HMac)
            {
                if (!macEngine.getAlgorithmName().startsWith("SHA-1"))
                {
                    if (macEngine.getAlgorithmName().startsWith("SHA-224"))
                    {
                        digest = SHA224;
                        keySize = 224;
                    }
                    else if (macEngine.getAlgorithmName().startsWith("SHA-256"))
                    {
                        digest = SHA256;
                        keySize = 256;
                    }
                    else if (macEngine.getAlgorithmName().startsWith("SHA-384"))
                    {
                        digest = SHA384;
                        keySize = 384;
                    }
                    else if (macEngine.getAlgorithmName().startsWith("SHA-512"))
                    {
                        digest = SHA512;
                        keySize = 512;
                    }
                    // BEGIN Android-removed: Unsupported algorithms
                    // else if (macEngine.getAlgorithmName().startsWith("RIPEMD160"))
                    // {
                    //     digest = RIPEMD160;
                    //     keySize = 160;
                    // }
                    // END Android-removed: Unsupported algorithms
                    else
                    {
                        throw new InvalidAlgorithmParameterException("no PKCS12 mapping for HMAC: " + macEngine.getAlgorithmName());
                    }
                }
            }
            // TODO: add correct handling for other digests
            param = PBE.Util.makePBEMacParameters(k, PKCS12, digest, keySize, pbeSpec);
        }
        else if (key instanceof BCPBEKey)
        {
            BCPBEKey k = (BCPBEKey)key;

            if (k.getParam() != null)
            {
                param = k.getParam();
            }
            else if (params instanceof PBEParameterSpec)
            {
                param = PBE.Util.makePBEMacParameters(k, params);
            }
            else
            {
                throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
            }
        }
        else
        {
            if (params instanceof PBEParameterSpec)
            {
                throw new InvalidAlgorithmParameterException("inappropriate parameter type: " + params.getClass().getName());
            }
            param = new KeyParameter(key.getEncoded());
        }

        final KeyParameter keyParam;
        if (param instanceof ParametersWithIV)
        {
            keyParam = (KeyParameter)((ParametersWithIV)param).getParameters();
        }
        else
        {
            keyParam = (KeyParameter)param;
        }

        if (params instanceof AEADParameterSpec)
        {
            AEADParameterSpec aeadSpec = (AEADParameterSpec)params;

            param = new AEADParameters(keyParam, aeadSpec.getMacSizeInBits(), aeadSpec.getNonce(), aeadSpec.getAssociatedData());
        }
        else if (params instanceof IvParameterSpec)
        {
            param = new ParametersWithIV(keyParam, ((IvParameterSpec)params).getIV());
        }
        // BEGIN Android-removed: Unsupported algorithms
        /*
        else if (params instanceof RC2ParameterSpec)
        {
            param = new ParametersWithIV(new RC2Parameters(keyParam.getKey(), ((RC2ParameterSpec)params).getEffectiveKeyBits()), ((RC2ParameterSpec)params).getIV());
        }
        else if (params instanceof SkeinParameterSpec)
        {
            param = new SkeinParameters.Builder(copyMap(((SkeinParameterSpec)params).getParameters())).setKey(keyParam.getKey()).build();
        }
        */
        // END Android-removed: Unsupported algorithms
        else if (params == null)
        {
            param = new KeyParameter(key.getEncoded());
        }
        else if (GcmSpecUtil.isGcmSpec(params))
        {
            param = GcmSpecUtil.extractAeadParameters(keyParam, params);
        }
        else if (!(params instanceof PBEParameterSpec))
        {
            throw new InvalidAlgorithmParameterException("unknown parameter type: " + params.getClass().getName());
        }

        try
        {
            macEngine.init(param);
        }
        catch (Exception e)
        {
            throw new InvalidAlgorithmParameterException("cannot initialize MAC: " + e.getMessage());
        }
    }

    protected int engineGetMacLength() 
    {
        return macEngine.getMacSize();
    }

    protected void engineReset() 
    {
        macEngine.reset();
    }

    protected void engineUpdate(
        byte    input) 
    {
        macEngine.update(input);
    }

    protected void engineUpdate(
        byte[]  input,
        int     offset,
        int     len) 
    {
        macEngine.update(input, offset, len);
    }

    protected byte[] engineDoFinal() 
    {
        byte[]  out = new byte[engineGetMacLength()];

        macEngine.doFinal(out, 0);

        return out;
    }

    private static Hashtable copyMap(Map paramsMap)
    {
        Hashtable newTable = new Hashtable();

        Iterator keys = paramsMap.keySet().iterator();
        while (keys.hasNext())
        {
            Object key = keys.next();
            newTable.put(key, paramsMap.get(key));
        }

        return newTable;
    }
}

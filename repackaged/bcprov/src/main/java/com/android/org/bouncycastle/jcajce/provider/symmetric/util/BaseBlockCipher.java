/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.org.bouncycastle.jcajce.provider.symmetric.util;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.IvParameterSpec;
// BEGIN Android-added: Various key-handling modifications
import javax.crypto.spec.PBEKeySpec;
// END Android-added: Various key-handling modifications
import javax.crypto.spec.PBEParameterSpec;
// Android-removed: Unsupported algorithms
// import javax.crypto.spec.RC2ParameterSpec;
// import javax.crypto.spec.RC5ParameterSpec;

import com.android.org.bouncycastle.asn1.DEROctetString;
import com.android.org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import com.android.org.bouncycastle.crypto.BlockCipher;
import com.android.org.bouncycastle.crypto.BufferedBlockCipher;
import com.android.org.bouncycastle.crypto.CipherParameters;
import com.android.org.bouncycastle.crypto.CryptoServicesRegistrar;
import com.android.org.bouncycastle.crypto.DataLengthException;
import com.android.org.bouncycastle.crypto.DefaultBufferedBlockCipher;
import com.android.org.bouncycastle.crypto.InvalidCipherTextException;
import com.android.org.bouncycastle.crypto.OutputLengthException;
// Android-removed: Unsupported algorithms
// import org.bouncycastle.crypto.engines.DSTU7624Engine;
// import org.bouncycastle.crypto.fpe.FPEEngine;
// import org.bouncycastle.crypto.fpe.FPEFF1Engine;
// import org.bouncycastle.crypto.fpe.FPEFF3_1Engine;
import com.android.org.bouncycastle.crypto.modes.AEADBlockCipher;
import com.android.org.bouncycastle.crypto.modes.AEADCipher;
import com.android.org.bouncycastle.crypto.modes.CBCBlockCipher;
import com.android.org.bouncycastle.crypto.modes.CCMBlockCipher;
import com.android.org.bouncycastle.crypto.modes.CFBBlockCipher;
import com.android.org.bouncycastle.crypto.modes.CTSBlockCipher;
// Android-removed: Unsupported algorithms
// import org.bouncycastle.crypto.modes.EAXBlockCipher;
// import org.bouncycastle.crypto.modes.GCFBBlockCipher;
import com.android.org.bouncycastle.crypto.modes.GCMBlockCipher;
// Android-removed: Unsupported algorithms
// import org.bouncycastle.crypto.modes.GCMSIVBlockCipher;
// import org.bouncycastle.crypto.modes.GOFBBlockCipher;
// import org.bouncycastle.crypto.modes.KCCMBlockCipher;
// import org.bouncycastle.crypto.modes.KCTRBlockCipher;
// import org.bouncycastle.crypto.modes.KGCMBlockCipher;
// import org.bouncycastle.crypto.modes.OCBBlockCipher;
import com.android.org.bouncycastle.crypto.modes.OFBBlockCipher;
// Android-removed: Unsupported algorithms
// import org.bouncycastle.crypto.modes.OpenPGPCFBBlockCipher;
// import org.bouncycastle.crypto.modes.PGPCFBBlockCipher;
import com.android.org.bouncycastle.crypto.paddings.PKCS7Padding;
// import org.bouncycastle.crypto.params.FPEParameters;
import com.android.org.bouncycastle.crypto.modes.SICBlockCipher;
import com.android.org.bouncycastle.crypto.paddings.BlockCipherPadding;
import com.android.org.bouncycastle.crypto.paddings.ISO10126d2Padding;
import com.android.org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import com.android.org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import com.android.org.bouncycastle.crypto.paddings.TBCPadding;
import com.android.org.bouncycastle.crypto.paddings.X923Padding;
import com.android.org.bouncycastle.crypto.paddings.ZeroBytePadding;
import com.android.org.bouncycastle.crypto.params.AEADParameters;
import com.android.org.bouncycastle.crypto.params.KeyParameter;
import com.android.org.bouncycastle.crypto.params.ParametersWithIV;
import com.android.org.bouncycastle.crypto.params.ParametersWithRandom;
// Android-removed: Unsupported algorithms
// import org.bouncycastle.crypto.params.ParametersWithSBox;
import com.android.org.bouncycastle.crypto.params.RC2Parameters;
// Android-removed: Unsupported algorithms
// import org.bouncycastle.crypto.params.RC5Parameters;
// import org.bouncycastle.jcajce.PBKDF1Key;
// import org.bouncycastle.jcajce.PBKDF1KeyWithParameters;
import com.android.org.bouncycastle.jcajce.PKCS12Key;
import com.android.org.bouncycastle.jcajce.PKCS12KeyWithParameters;
import com.android.org.bouncycastle.jcajce.spec.AEADParameterSpec;
// Android-removed: Unsupported algorithms
// import org.bouncycastle.jcajce.spec.GOST28147ParameterSpec;
// import org.bouncycastle.jcajce.spec.RepeatedSecretKeySpec;
import com.android.org.bouncycastle.internal.asn1.cms.GCMParameters;
// import org.bouncycastle.jcajce.spec.FPEParameterSpec;
import com.android.org.bouncycastle.util.Arrays;
import com.android.org.bouncycastle.util.Strings;

/**
 * @hide This class is not part of the Android public SDK API
 */
public class BaseBlockCipher
    extends BaseWrapCipher
    implements PBE
{
    private static final int BUF_SIZE = 512;

    //
    // specs we can handle.
    //
    private Class[] availableSpecs =
        {
            // Android-removed: Unsupported alhorithms
            // RC2ParameterSpec.class,
            // RC5ParameterSpec.class,
            GcmSpecUtil.gcmSpecClass,
            // Android-removed: unsupported algorithms
            // GOST28147ParameterSpec.class,
            IvParameterSpec.class,
            PBEParameterSpec.class
        };

    private BlockCipher baseEngine;
    private BlockCipherProvider engineProvider;
    private GenericBlockCipher cipher;
    private ParametersWithIV ivParam;
    private AEADParameters aeadParams;

    private int keySizeInBits;
    private int scheme = -1;
    private int digest;

    private int ivLength = 0;

    private boolean padded;
    private boolean fixedIv = true;
    private PBEParameterSpec pbeSpec = null;
    private String pbeAlgorithm = null;

    private String modeName = null;

    protected BaseBlockCipher(
        BlockCipher engine)
    {
        baseEngine = engine;

        cipher = new BufferedGenericBlockCipher(engine);
    }

    protected BaseBlockCipher(
        BlockCipher engine,
        int scheme,
        int digest,
        int keySizeInBits,
        int ivLength)
    {
        baseEngine = engine;

        this.scheme = scheme;
        this.digest = digest;
        this.keySizeInBits = keySizeInBits;
        this.ivLength = ivLength;

        cipher = new BufferedGenericBlockCipher(engine);
    }

    protected BaseBlockCipher(
        BlockCipherProvider provider)
    {
        baseEngine = provider.get();
        engineProvider = provider;

        cipher = new BufferedGenericBlockCipher(provider.get());
    }

    protected BaseBlockCipher(
        AEADBlockCipher engine)
    {
        this.baseEngine = engine.getUnderlyingCipher();
        if (engine.getAlgorithmName().indexOf("GCM") >= 0)
        {
            this.ivLength = 12;
        }
        else
        {
            this.ivLength = baseEngine.getBlockSize();
        }
        this.cipher = new AEADGenericBlockCipher(engine);
    }

    protected BaseBlockCipher(
        AEADCipher engine,
        boolean fixedIv,
        int ivLength)
    {
        this.baseEngine = null;
        this.fixedIv = fixedIv;
        this.ivLength = ivLength;
        this.cipher = new AEADGenericBlockCipher(engine);
    }

    protected BaseBlockCipher(
        AEADBlockCipher engine,
        boolean fixedIv,
        int ivLength)
    {
        this.baseEngine = engine.getUnderlyingCipher();
        this.fixedIv = fixedIv;
        this.ivLength = ivLength;
        this.cipher = new AEADGenericBlockCipher(engine);
    }

    protected BaseBlockCipher(
        com.android.org.bouncycastle.crypto.BlockCipher engine,
        int ivLength)
    {
        this(engine, true, ivLength);
    }

    protected BaseBlockCipher(
        com.android.org.bouncycastle.crypto.BlockCipher engine,
        boolean fixedIv,
        int ivLength)
    {
        baseEngine = engine;

        this.fixedIv = fixedIv;
        this.cipher = new BufferedGenericBlockCipher(engine);
        this.ivLength = ivLength / 8;
    }

    protected BaseBlockCipher(
        BufferedBlockCipher engine,
        int ivLength)
    {
        this(engine, true, ivLength);
    }

    protected BaseBlockCipher(
        BufferedBlockCipher engine,
        boolean fixedIv,
        int ivLength)
    {
        baseEngine = engine.getUnderlyingCipher();

        this.cipher = new BufferedGenericBlockCipher(engine);
        this.fixedIv = fixedIv;
        this.ivLength = ivLength / 8;
    }

    protected int engineGetBlockSize()
    {
        if (baseEngine == null)
        {
            return -1;
        }
        return baseEngine.getBlockSize();
    }

    protected byte[] engineGetIV()
    {
        if (aeadParams != null)
        {
            return aeadParams.getNonce();
        }

        return (ivParam != null) ? ivParam.getIV() : null;
    }

    protected int engineGetKeySize(
        Key key)
    {
        return key.getEncoded().length * 8;
    }

    protected int engineGetOutputSize(
        int inputLen)
    {
        return cipher.getOutputSize(inputLen);
    }

    protected AlgorithmParameters engineGetParameters()
    {
        if (engineParams == null)
        {
            if (pbeSpec != null)
            {
                try
                {
                    engineParams = createParametersInstance(pbeAlgorithm);
                    engineParams.init(pbeSpec);
                }
                catch (Exception e)
                {
                    return null;
                }
            }
            else if (aeadParams != null)
            {
                // CHACHA20-Poly1305
                if (baseEngine == null)
                {
                    try
                    {
                        engineParams = createParametersInstance(PKCSObjectIdentifiers.id_alg_AEADChaCha20Poly1305.getId());
                        engineParams.init(new DEROctetString(aeadParams.getNonce()).getEncoded());
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e.toString());
                    }
                }
                else
                {
                    try
                    {
                        engineParams = createParametersInstance("GCM");
                        engineParams.init(new GCMParameters(aeadParams.getNonce(), aeadParams.getMacSize() / 8).getEncoded());
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e.toString());
                    }
                }
            }
            else if (ivParam != null)
            {
                String name = cipher.getUnderlyingCipher().getAlgorithmName();

                if (name.indexOf('/') >= 0)
                {
                    name = name.substring(0, name.indexOf('/'));
                }

                try
                {
                    engineParams = createParametersInstance(name);
                    engineParams.init(new IvParameterSpec(ivParam.getIV()));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e.toString());
                }
            }
        }

        return engineParams;
    }

    protected void engineSetMode(
        String mode)
        throws NoSuchAlgorithmException
    {
        if (baseEngine == null)
        {
            throw new NoSuchAlgorithmException("no mode supported for this algorithm");
        }
        modeName = Strings.toUpperCase(mode);

        if (modeName.equals("ECB"))
        {
            ivLength = 0;
            cipher = new BufferedGenericBlockCipher(baseEngine);
        }
        else if (modeName.equals("CBC"))
        {
            ivLength = baseEngine.getBlockSize();
            cipher = new BufferedGenericBlockCipher(
                CBCBlockCipher.newInstance(baseEngine));
        }
        else if (modeName.startsWith("OFB"))
        {
            ivLength = baseEngine.getBlockSize();
            if (modeName.length() != 3)
            {
                int wordSize = Integer.parseInt(modeName.substring(3));

                cipher = new BufferedGenericBlockCipher(
                    new OFBBlockCipher(baseEngine, wordSize));
            }
            else
            {
                cipher = new BufferedGenericBlockCipher(
                    new OFBBlockCipher(baseEngine, 8 * baseEngine.getBlockSize()));
            }
        }
        else if (modeName.startsWith("CFB"))
        {
            ivLength = baseEngine.getBlockSize();
            if (modeName.length() != 3)
            {
                int wordSize = Integer.parseInt(modeName.substring(3));

                cipher = new BufferedGenericBlockCipher(
                    CFBBlockCipher.newInstance(baseEngine, wordSize));
            }
            else
            {
                cipher = new BufferedGenericBlockCipher(
                    CFBBlockCipher.newInstance(baseEngine, 8 * baseEngine.getBlockSize()));
            }
        }
        // BEGIN Android-removed: Unsupported modes
        /*
        else if (modeName.startsWith("PGPCFB"))
        {
            boolean inlineIV = modeName.equals("PGPCFBWITHIV");

            if (!inlineIV && modeName.length() != 6)
            {
                throw new NoSuchAlgorithmException("no mode support for " + modeName);
            }
            
            ivLength = baseEngine.getBlockSize();
            cipher = new BufferedGenericBlockCipher(
                new PGPCFBBlockCipher(baseEngine, inlineIV));
        }
        else if (modeName.equals("OPENPGPCFB"))
        {
            ivLength = 0;
            cipher = new BufferedGenericBlockCipher(
                new OpenPGPCFBBlockCipher(baseEngine));
        }
        else if (modeName.equals("FF1"))
        {
            ivLength = 0;
            cipher = new BufferedFPEBlockCipher(
                new FPEFF1Engine(baseEngine));
        }
        else if (modeName.equals("FF3-1"))
        {
            ivLength = 0;
            cipher = new BufferedFPEBlockCipher(
                new FPEFF3_1Engine(baseEngine));
        }
        else if (modeName.equals("SIC"))
        {
            ivLength = baseEngine.getBlockSize();
            if (ivLength < 16)
            {
                throw new IllegalArgumentException("Warning: SIC-Mode can become a twotime-pad if the blocksize of the cipher is too small. Use a cipher with a block size of at least 128 bits (e.g. AES)");
            }
            fixedIv = false;
            cipher = new BufferedGenericBlockCipher(new DefaultBufferedBlockCipher(
                SICBlockCipher.newInstance(baseEngine)));
        }
        */
        // END Android-removed: Unsupported modes
        else if (modeName.equals("CTR"))
        {
            ivLength = baseEngine.getBlockSize();
            fixedIv = false;
            // BEGIN Android-removed: Unsupported algorithms
            /*
            if (baseEngine instanceof DSTU7624Engine)
            {
                cipher = new BufferedGenericBlockCipher(new DefaultBufferedBlockCipher(
                    new KCTRBlockCipher(baseEngine)));
            }
            else
            {
            */
                cipher = new BufferedGenericBlockCipher(new DefaultBufferedBlockCipher(
                    SICBlockCipher.newInstance(baseEngine)));
            /*
            }
            */
        }
        // BEGIN Android-removed: Unsupported modes
        /*
        else if (modeName.equals("GOFB"))
        {
            ivLength = baseEngine.getBlockSize();
            cipher = new BufferedGenericBlockCipher(new DefaultBufferedBlockCipher(
                new GOFBBlockCipher(baseEngine)));
        }
        else if (modeName.equals("GCFB"))
        {
            ivLength = baseEngine.getBlockSize();
            cipher = new BufferedGenericBlockCipher(new DefaultBufferedBlockCipher(
                new GCFBBlockCipher(baseEngine)));
        }
        */
        // END Android-removed: Unsupported modes
        else if (modeName.equals("CTS"))
        {
            ivLength = baseEngine.getBlockSize();
            cipher = new BufferedGenericBlockCipher(new CTSBlockCipher(CBCBlockCipher.newInstance(baseEngine)));
        }
        else if (modeName.equals("CCM"))
        {
            ivLength = 12; // CCM nonce 7..13 bytes
            // BEGIN Android-removed: Unsupported algorithms
            /*
            if (baseEngine instanceof DSTU7624Engine)
            {
                cipher = new AEADGenericBlockCipher(new KCCMBlockCipher(baseEngine));
            }
            else
            {
            */
                cipher = new AEADGenericBlockCipher(CCMBlockCipher.newInstance(baseEngine));
            /*
            }
            */
        }
        // BEGIN Android-removed: Unsupported modes
        /*
        else if (modeName.equals("OCB"))
        {
            if (engineProvider != null)
            {
                /*
                 * RFC 7253 4.2. Nonce is a string of no more than 120 bits
                 *
                ivLength = 15;
                cipher = new AEADGenericBlockCipher(new OCBBlockCipher(baseEngine, engineProvider.get()));
            }
            else
            {
                throw new NoSuchAlgorithmException("can't support mode " + mode);
            }
        }
        else if (modeName.equals("EAX"))
        {
            ivLength = baseEngine.getBlockSize();
            cipher = new AEADGenericBlockCipher(new EAXBlockCipher(baseEngine));
        }
        */
        // END Android-removed: Unsupported modes
        else if (modeName.equals("GCM"))
        {
            ivLength = baseEngine.getBlockSize();
            // BEGIN Android-removed: Unsupported algorithms
            /*
            if (baseEngine instanceof DSTU7624Engine)
            {
                ivLength = baseEngine.getBlockSize();
                cipher = new AEADGenericBlockCipher(new KGCMBlockCipher(baseEngine));
            }
            else
            {
            */
                cipher = new AEADGenericBlockCipher(GCMBlockCipher.newInstance(baseEngine));
            /*
            }
            */
        }
        else
        {
            throw new NoSuchAlgorithmException("can't support mode " + mode);
        }
    }

    protected void engineSetPadding(
        String padding)
        throws NoSuchPaddingException
    {
        if (baseEngine == null)
        {
            throw new NoSuchPaddingException("no padding supported for this algorithm");
        }

        String paddingName = Strings.toUpperCase(padding);

        if (paddingName.equals("NOPADDING"))
        {
            if (cipher.wrapOnNoPadding())
            {
                cipher = new BufferedGenericBlockCipher(new DefaultBufferedBlockCipher(cipher.getUnderlyingCipher()));
            }
        }
        else if (paddingName.equals("WITHCTS") || paddingName.equals("CTSPADDING") || paddingName.equals("CS3PADDING"))
        {
            cipher = new BufferedGenericBlockCipher(new CTSBlockCipher(cipher.getUnderlyingCipher()));
        }
        else
        {
            padded = true;

            if (isAEADModeName(modeName))
            {
                throw new NoSuchPaddingException("Only NoPadding can be used with AEAD modes.");
            }
            else if (paddingName.equals("PKCS5PADDING") || paddingName.equals("PKCS7PADDING"))
            {
                cipher = new BufferedGenericBlockCipher(cipher.getUnderlyingCipher());
            }
            else if (paddingName.equals("ZEROBYTEPADDING"))
            {
                cipher = new BufferedGenericBlockCipher(cipher.getUnderlyingCipher(), new ZeroBytePadding());
            }
            else if (paddingName.equals("ISO10126PADDING") || paddingName.equals("ISO10126-2PADDING"))
            {
                cipher = new BufferedGenericBlockCipher(cipher.getUnderlyingCipher(), new ISO10126d2Padding());
            }
            else if (paddingName.equals("X9.23PADDING") || paddingName.equals("X923PADDING"))
            {
                cipher = new BufferedGenericBlockCipher(cipher.getUnderlyingCipher(), new X923Padding());
            }
            else if (paddingName.equals("ISO7816-4PADDING") || paddingName.equals("ISO9797-1PADDING"))
            {
                cipher = new BufferedGenericBlockCipher(cipher.getUnderlyingCipher(), new ISO7816d4Padding());
            }
            else if (paddingName.equals("TBCPADDING"))
            {
                cipher = new BufferedGenericBlockCipher(cipher.getUnderlyingCipher(), new TBCPadding());
            }
            else
            {
                throw new NoSuchPaddingException("Padding " + padding + " unknown.");
            }
        }
    }

    // BEGIN Android-added: Handling missing IVs
    private boolean isBCPBEKeyWithoutIV(Key key) {
        return (key instanceof BCPBEKey) && !(((BCPBEKey)key).getParam() instanceof ParametersWithIV);
    }
    // END Android-added: Handling missing IVs

    protected void engineInit(
        int opmode,
        Key key,
        final AlgorithmParameterSpec params,
        SecureRandom random)
        throws InvalidKeyException, InvalidAlgorithmParameterException
    {
        CipherParameters param;

        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.engineParams = null;
        this.aeadParams = null;

        //
        // basic key check
        //
        if (!(key instanceof SecretKey))
        {
            throw new InvalidKeyException("Key for algorithm " + ((key != null) ? key.getAlgorithm() : null) + " not suitable for symmetric enryption.");
        }

        //
        // for RC5-64 we must have some default parameters
        //
        if (params == null && (baseEngine != null && baseEngine.getAlgorithmName().startsWith("RC5-64")))
        {
            throw new InvalidAlgorithmParameterException("RC5 requires an RC5ParametersSpec to be passed in.");
        }

        //
        // a note on iv's - if ivLength is zero the IV gets ignored (we don't use it).
        //
        // BEGIN Android-changed: Don't use PKCS12 with missing IV.
        // If the key is a BCPBE one without an IV, ignore the fact that the scheme is PKCS12.
        // if (scheme == PKCS12 || key instanceof PKCS12Key)
        if ((scheme == PKCS12 || key instanceof PKCS12Key) && !isBCPBEKeyWithoutIV(key))
        // END Android-changed: Don't use PKCS12 with missing IV.
        {
            SecretKey k;
            try
            {
                k = (SecretKey)key;
            }
            catch (Exception e)
            {
                throw new InvalidKeyException("PKCS12 requires a SecretKey/PBEKey");
            }

            if (params instanceof PBEParameterSpec)
            {
                pbeSpec = (PBEParameterSpec)params;
            }

            if (k instanceof PBEKey && pbeSpec == null)
            {
                PBEKey pbeKey = (PBEKey)k;
                if (pbeKey.getSalt() == null)
                {
                    throw new InvalidAlgorithmParameterException("PBEKey requires parameters to specify salt");
                }
                pbeSpec = new PBEParameterSpec(pbeKey.getSalt(), pbeKey.getIterationCount());
            }

            if (pbeSpec == null && !(k instanceof PBEKey))
            {
                throw new InvalidKeyException("Algorithm requires a PBE key");
            }

            if (key instanceof BCPBEKey)
            {
                // PKCS#12 sets an IV, if we get a key that doesn't have ParametersWithIV we need to reject it. If the
                // key has no parameters it means it's an old-school JCE PBE Key - we use getEncoded() on it.
                CipherParameters pbeKeyParam = ((BCPBEKey)key).getParam();
                if (pbeKeyParam instanceof ParametersWithIV)
                {
                    param = pbeKeyParam;
                }
                else if (pbeKeyParam == null)
                {
                    // BEGIN Android-changed: Unreachable code
                    // See above for the Android change that makes this code unreachable.
                    // param = PBE.Util.makePBEParameters(k.getEncoded(), PKCS12, digest, keySizeInBits, ivLength * 8, pbeSpec, cipher.getAlgorithmName());
                    throw new AssertionError("Unreachable code");
                    // END Android-changed: Unreachable code
                }
                else
                {
                    throw new InvalidKeyException("Algorithm requires a PBE key suitable for PKCS12");
                }
            }
            else
            {
                param = PBE.Util.makePBEParameters(k.getEncoded(), PKCS12, digest, keySizeInBits, ivLength * 8, pbeSpec, cipher.getAlgorithmName());
            }
            if (param instanceof ParametersWithIV)
            {
                ivParam = (ParametersWithIV)param;
            }
        }
        // BEGIN Android-removed: Unsupported algorithms
        /*
        else if (key instanceof PBKDF1Key)
        {
            PBKDF1Key k = (PBKDF1Key)key;

            if (params instanceof PBEParameterSpec)
            {
                pbeSpec = (PBEParameterSpec)params;
            }
            if (k instanceof PBKDF1KeyWithParameters && pbeSpec == null)
            {
                pbeSpec = new PBEParameterSpec(((PBKDF1KeyWithParameters)k).getSalt(), ((PBKDF1KeyWithParameters)k).getIterationCount());
            }

            param = PBE.Util.makePBEParameters(k.getEncoded(), PKCS5S1, digest, keySizeInBits, ivLength * 8, pbeSpec, cipher.getAlgorithmName());
            if (param instanceof ParametersWithIV)
            {
                ivParam = (ParametersWithIV)param;
            }
        }
        */
        // END Android-removed: Unsupported algorithms
        else if (key instanceof BCPBEKey)
        {
            BCPBEKey k = (BCPBEKey)key;

            if (k.getOID() != null)
            {
                pbeAlgorithm = k.getOID().getId();
            }
            else
            {
                pbeAlgorithm = k.getAlgorithm();
            }

            if (k.getParam() != null)
            {
                param = adjustParameters(params, k.getParam());
            }
            else if (params instanceof PBEParameterSpec)
            {
                pbeSpec = (PBEParameterSpec)params;
                // BEGIN Android-added: Allow PBE keys with only passwords.
                // At this point, k.getParam() == null, so the key hasn't been generated.  If
                // the parameters have non-default values, recreate the BCPBEKey from algorithm
                // parameters as to generate the key.
                if ((pbeSpec.getSalt().length != 0) && (pbeSpec.getIterationCount() > 0)) {
                    k = new BCPBEKey(k.getAlgorithm(), k.getOID(), k.getType(), k.getDigest(),
                            k.getKeySize(), k.getIvSize(),
                            new PBEKeySpec(
                                    k.getPassword(), pbeSpec.getSalt(), pbeSpec.getIterationCount(),
                                    k.getKeySize()),
                            null /* CipherParameters */);
                }
                // END Android-added: Allow PBE keys with only passwords.
                param = PBE.Util.makePBEParameters(k, params, cipher.getUnderlyingCipher().getAlgorithmName());
            }
            else
            {
                throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
            }

            if (param instanceof ParametersWithIV)
            {
                ivParam = (ParametersWithIV)param;
            }
        }
        else if (key instanceof PBEKey)
        {
            PBEKey k = (PBEKey)key;
            pbeSpec = (PBEParameterSpec)params;
            if (k instanceof PKCS12KeyWithParameters && pbeSpec == null)
            {
                pbeSpec = new PBEParameterSpec(k.getSalt(), k.getIterationCount());
            }

            param = PBE.Util.makePBEParameters(k.getEncoded(), scheme, digest, keySizeInBits, ivLength * 8, pbeSpec, cipher.getAlgorithmName());
            if (param instanceof ParametersWithIV)
            {
                ivParam = (ParametersWithIV)param;
            }
        }
        // BEGIN Android-changed: Unsupported algorithm
        // else if (!(key instanceof RepeatedSecretKeySpec))
        else
        // END Android-changed: Unsupported algorithms
        {
            if (scheme == PKCS5S1 || scheme == PKCS5S1_UTF8 || scheme == PKCS5S2 || scheme == PKCS5S2_UTF8)
            {
                throw new InvalidKeyException("Algorithm requires a PBE key");
            }
            param = new KeyParameter(key.getEncoded());
        }
        // BEGIN Android-removed: Unreachable
        // else
        // {
        //    param = null;
        // }
        // END Android-removed: Unreachable

        if (params instanceof AEADParameterSpec)
        {
            if (!isAEADModeName(modeName) && !(cipher instanceof AEADGenericBlockCipher))
            {
                throw new InvalidAlgorithmParameterException("AEADParameterSpec can only be used with AEAD modes.");
            }

            AEADParameterSpec aeadSpec = (AEADParameterSpec)params;

            KeyParameter keyParam;
            if (param instanceof ParametersWithIV)
            {
                keyParam = (KeyParameter)((ParametersWithIV)param).getParameters();
            }
            else
            {
                keyParam = (KeyParameter)param;
            }
            param = aeadParams = new AEADParameters(keyParam, aeadSpec.getMacSizeInBits(), aeadSpec.getNonce(), aeadSpec.getAssociatedData());
        }
        else if (params instanceof IvParameterSpec)
        {
            if (ivLength != 0)
            {
                IvParameterSpec p = (IvParameterSpec)params;

                if (p.getIV().length != ivLength && !(cipher instanceof AEADGenericBlockCipher) && fixedIv)
                {
                    throw new InvalidAlgorithmParameterException("IV must be " + ivLength + " bytes long.");
                }

                if (param instanceof ParametersWithIV)
                {
                    param = new ParametersWithIV(((ParametersWithIV)param).getParameters(), p.getIV());
                }
                else
                {
                    param = new ParametersWithIV(param, p.getIV());
                }
                ivParam = (ParametersWithIV)param;
            }
            else
            {
                if (modeName != null && modeName.equals("ECB"))
                {
                    throw new InvalidAlgorithmParameterException("ECB mode does not use an IV");
                }
            }
        }
        // BEGIN Android-removed: Unsupported algorithms
        /*
        else if (params instanceof GOST28147ParameterSpec)
        {
            GOST28147ParameterSpec gost28147Param = (GOST28147ParameterSpec)params;

            param = new ParametersWithSBox(
                new KeyParameter(key.getEncoded()), ((GOST28147ParameterSpec)params).getSBox());

            if (gost28147Param.getIV() != null && ivLength != 0)
            {
                if (param instanceof ParametersWithIV)
                {
                    param = new ParametersWithIV(((ParametersWithIV)param).getParameters(), gost28147Param.getIV());
                }
                else
                {
                    param = new ParametersWithIV(param, gost28147Param.getIV());
                }
                ivParam = (ParametersWithIV)param;
            }
        }
        else if (params instanceof RC2ParameterSpec)
        {
            RC2ParameterSpec rc2Param = (RC2ParameterSpec)params;

            param = new RC2Parameters(key.getEncoded(), ((RC2ParameterSpec)params).getEffectiveKeyBits());

            if (rc2Param.getIV() != null && ivLength != 0)
            {
                if (param instanceof ParametersWithIV)
                {
                    param = new ParametersWithIV(((ParametersWithIV)param).getParameters(), rc2Param.getIV());
                }
                else
                {
                    param = new ParametersWithIV(param, rc2Param.getIV());
                }
                ivParam = (ParametersWithIV)param;
            }
        }
        else if (params instanceof RC5ParameterSpec)
        {
            RC5ParameterSpec rc5Param = (RC5ParameterSpec)params;

            param = new RC5Parameters(key.getEncoded(), ((RC5ParameterSpec)params).getRounds());
            if (baseEngine.getAlgorithmName().startsWith("RC5"))
            {
                if (baseEngine.getAlgorithmName().equals("RC5-32"))
                {
                    if (rc5Param.getWordSize() != 32)
                    {
                        throw new InvalidAlgorithmParameterException("RC5 already set up for a word size of 32 not " + rc5Param.getWordSize() + ".");
                    }
                }
                else if (baseEngine.getAlgorithmName().equals("RC5-64"))
                {
                    if (rc5Param.getWordSize() != 64)
                    {
                        throw new InvalidAlgorithmParameterException("RC5 already set up for a word size of 64 not " + rc5Param.getWordSize() + ".");
                    }
                }
            }
            else
            {
                throw new InvalidAlgorithmParameterException("RC5 parameters passed to a cipher that is not RC5.");
            }
            if ((rc5Param.getIV() != null) && (ivLength != 0))
            {
                if (param instanceof ParametersWithIV)
                {
                    param = new ParametersWithIV(((ParametersWithIV)param).getParameters(), rc5Param.getIV());
                }
                else
                {
                    param = new ParametersWithIV(param, rc5Param.getIV());
                }
                ivParam = (ParametersWithIV)param;
            }
        }
        else if (params instanceof FPEParameterSpec)
        {
            FPEParameterSpec spec = (FPEParameterSpec)params;

            param = new FPEParameters((KeyParameter)param, spec.getRadixConverter(), spec.getTweak(), spec.isUsingInverseFunction());
        }
        */
        // END Android-removed: Unsupported algorithms
        else if (GcmSpecUtil.isGcmSpec(params))
        {
            if (!isAEADModeName(modeName) && !(cipher instanceof AEADGenericBlockCipher))
            {
                throw new InvalidAlgorithmParameterException("GCMParameterSpec can only be used with AEAD modes.");
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

            param = aeadParams = GcmSpecUtil.extractAeadParameters(keyParam, params);
        }
        else if (params != null && !(params instanceof PBEParameterSpec))
        {
            throw new InvalidAlgorithmParameterException("unknown parameter type.");
        }

        if ((ivLength != 0) && !(param instanceof ParametersWithIV) && !(param instanceof AEADParameters))
        {
            SecureRandom ivRandom = random;

            if (ivRandom == null)
            {
                ivRandom = CryptoServicesRegistrar.getSecureRandom();
            }

            if ((opmode == Cipher.ENCRYPT_MODE) || (opmode == Cipher.WRAP_MODE))
            {
                byte[] iv = new byte[ivLength];

                // BEGIN Android-changed: Reject PBE keys with no IV
                // These keys were accepted in BC 1.52 (and treated as having an IV of 0) but
                // rejected outright in BC 1.54 (even if an IV was passed in params).
                // See b/27995180 for historical details.
                if (!isBCPBEKeyWithoutIV(key)) {
                    ivRandom.nextBytes(iv);
                } else {
                    throw new InvalidAlgorithmParameterException("No IV set when using PBE key");
                }
                // END Android-changed: Reject PBE keys with no IV
                param = new ParametersWithIV(param, iv);
                ivParam = (ParametersWithIV)param;
            }
            else if (cipher.getUnderlyingCipher().getAlgorithmName().indexOf("PGPCFB") < 0)
            {
                // BEGIN Android-changed: Reject PBE keys with no IV
                // These keys were accepted in BC 1.52 (and treated as having an IV of 0) but
                // rejected outright in BC 1.54 (even if an IV was passed in params).
                throw new InvalidAlgorithmParameterException("No IV set when using PBE key");
                // END Android-changed: Reject PBE keys with no IV
            }
        }


        if (random != null && padded)
        {
            param = new ParametersWithRandom(param, random);
        }

        try
        {
            switch (opmode)
            {
            case Cipher.ENCRYPT_MODE:
            case Cipher.WRAP_MODE:
                cipher.init(true, param);
                break;
            case Cipher.DECRYPT_MODE:
            case Cipher.UNWRAP_MODE:
                cipher.init(false, param);
                break;
            default:
                throw new InvalidParameterException("unknown opmode " + opmode + " passed");
            }

            if (cipher instanceof AEADGenericBlockCipher && aeadParams == null)
            {
                AEADCipher aeadCipher = ((AEADGenericBlockCipher)cipher).cipher;

                aeadParams = new AEADParameters((KeyParameter)ivParam.getParameters(), aeadCipher.getMac().length * 8, ivParam.getIV());
            }
        }
        // BEGIN Android-removed: keeping pre 1.68 behaviour
        /*
        catch (IllegalArgumentException e)
        {
            throw new InvalidAlgorithmParameterException(e.getMessage(), e);
        }
        */
        // END Android-removed: keeping pre 1.68 behaviour
        catch (Exception e)
        {
            throw new InvalidKeyOrParametersException(e.getMessage(), e);
        }
    }

    private CipherParameters adjustParameters(AlgorithmParameterSpec params, CipherParameters param)
    {
        CipherParameters key;

        if (param instanceof ParametersWithIV)
        {
            key = ((ParametersWithIV)param).getParameters();
            if (params instanceof IvParameterSpec)
            {
                IvParameterSpec iv = (IvParameterSpec)params;

                ivParam = new ParametersWithIV(key, iv.getIV());
                param = ivParam;
            }
            // BEGIN Android-removed: Unsupported algorithms
            /*
            else if (params instanceof GOST28147ParameterSpec)
            {
                // need to pick up IV and SBox.
                GOST28147ParameterSpec gost28147Param = (GOST28147ParameterSpec)params;

                param = new ParametersWithSBox(param, gost28147Param.getSBox());

                if (gost28147Param.getIV() != null && ivLength != 0)
                {
                    ivParam = new ParametersWithIV(key, gost28147Param.getIV());
                    param = ivParam;
                }
            }
            */
            // END Android-removed: Unsupported algorithms
        }
        else
        {
            if (params instanceof IvParameterSpec)
            {
                IvParameterSpec iv = (IvParameterSpec)params;

                ivParam = new ParametersWithIV(param, iv.getIV());
                param = ivParam;
            }
            // BEGIN Android-removed: Unsupported algorithms
            /*
            else if (params instanceof GOST28147ParameterSpec)
            {
                // need to pick up IV and SBox.
                GOST28147ParameterSpec gost28147Param = (GOST28147ParameterSpec)params;

                param = new ParametersWithSBox(param, gost28147Param.getSBox());

                if (gost28147Param.getIV() != null && ivLength != 0)
                {
                    param = new ParametersWithIV(param, gost28147Param.getIV());
                }
            }
            */
            // END Android-removed: Unsupported algorithms
        }
        return param;
    }

    protected void engineInit(
        int opmode,
        Key key,
        AlgorithmParameters params,
        SecureRandom random)
        throws InvalidKeyException, InvalidAlgorithmParameterException
    {
        AlgorithmParameterSpec paramSpec = null;

        if (params != null)
        {
            paramSpec = SpecUtil.extractSpec(params, availableSpecs);

            if (paramSpec == null)
            {
                throw new InvalidAlgorithmParameterException("can't handle parameter " + params.toString());
            }
        }

        engineInit(opmode, key, paramSpec, random);

        engineParams = params;
    }

    protected void engineInit(
        int opmode,
        Key key,
        SecureRandom random)
        throws InvalidKeyException
    {
        try
        {
            engineInit(opmode, key, (AlgorithmParameterSpec)null, random);
        }
        catch (InvalidAlgorithmParameterException e)
        {
            throw new InvalidKeyException(e.getMessage());
        }
    }

    protected void engineUpdateAAD(byte[] input, int offset, int length)
    {
        cipher.updateAAD(input, offset, length);
    }

    protected void engineUpdateAAD(ByteBuffer src)
    {
        int remaining = src.remaining();
        if (remaining < 1)
        {
            // No data to update
        }
        else if (src.hasArray())
        {
            engineUpdateAAD(src.array(), src.arrayOffset() + src.position(), remaining);
            src.position(src.limit());
        }
        else if (remaining <= BUF_SIZE)
        {
            byte[] data = new byte[remaining];
            src.get(data);
            engineUpdateAAD(data, 0, data.length);
            Arrays.fill(data, (byte)0);
        }
        else
        {
            byte[] data = new byte[BUF_SIZE];
            do
            {
                int length = Math.min(data.length, remaining);
                src.get(data, 0, length);
                engineUpdateAAD(data, 0, length);
                remaining -= length;
            }
            while (remaining > 0);
            Arrays.fill(data, (byte)0);
        }
    }

    protected byte[] engineUpdate(
        byte[] input,
        int inputOffset,
        int inputLen)
    {
        int length = cipher.getUpdateOutputSize(inputLen);

        if (length > 0)
        {
            byte[] out = new byte[length];

            int len = cipher.processBytes(input, inputOffset, inputLen, out, 0);

            if (len == 0)
            {
                return null;
            }
            else if (len != out.length)
            {
                byte[] tmp = new byte[len];

                System.arraycopy(out, 0, tmp, 0, len);

                return tmp;
            }

            return out;
        }

        cipher.processBytes(input, inputOffset, inputLen, null, 0);

        return null;
    }

    protected int engineUpdate(
        byte[] input,
        int inputOffset,
        int inputLen,
        byte[] output,
        int outputOffset)
        throws ShortBufferException
    {
        if (outputOffset + cipher.getUpdateOutputSize(inputLen) > output.length)
        {
            throw new ShortBufferException("output buffer too short for input.");
        }

        try
        {
            return cipher.processBytes(input, inputOffset, inputLen, output, outputOffset);
        }
        catch (DataLengthException e)
        {
            // should never occur
            throw new IllegalStateException(e.toString());
        }
    }

    protected byte[] engineDoFinal(
        byte[] input,
        int inputOffset,
        int inputLen)
        throws IllegalBlockSizeException, BadPaddingException
    {
        int len = 0;
        byte[] tmp = new byte[engineGetOutputSize(inputLen)];

        if (inputLen != 0)
        {
            len = cipher.processBytes(input, inputOffset, inputLen, tmp, 0);
        }

        try
        {
            len += cipher.doFinal(tmp, len);
        }
        catch (DataLengthException e)
        {
            throw new IllegalBlockSizeException(e.getMessage());
        }

        if (len == tmp.length)
        {
            return tmp;
        }

        if (len > tmp.length)
        {
            throw new IllegalBlockSizeException("internal buffer overflow");
        }

        byte[] out = new byte[len];

        System.arraycopy(tmp, 0, out, 0, len);

        return out;
    }

    protected int engineDoFinal(
        byte[] input,
        int inputOffset,
        int inputLen,
        byte[] output,
        int outputOffset)
        throws IllegalBlockSizeException, BadPaddingException, ShortBufferException
    {
        int len = 0;

        if (outputOffset + engineGetOutputSize(inputLen) > output.length)
        {
            throw new ShortBufferException("output buffer too short for input.");
        }

        try
        {
            if (inputLen != 0)
            {
                len = cipher.processBytes(input, inputOffset, inputLen, output, outputOffset);
            }

            return (len + cipher.doFinal(output, outputOffset + len));
        }
        catch (OutputLengthException e)
        {
            throw new IllegalBlockSizeException(e.getMessage());
        }
        catch (DataLengthException e)
        {
            throw new IllegalBlockSizeException(e.getMessage());
        }
    }

    private boolean isAEADModeName(
        String modeName)
    {
        // Android-changed: Unsupported modes
        // return "CCM".equals(modeName) || "EAX".equals(modeName) || "GCM".equals(modeName) || "OCB".equals(modeName);
        return "CCM".equals(modeName) || "GCM".equals(modeName);
    }

    /*
     * The ciphers that inherit from us.
     */

    static private interface GenericBlockCipher
    {
        public void init(boolean forEncryption, CipherParameters params)
            throws IllegalArgumentException;

        public boolean wrapOnNoPadding();

        public String getAlgorithmName();

        public com.android.org.bouncycastle.crypto.BlockCipher getUnderlyingCipher();

        public int getOutputSize(int len);

        public int getUpdateOutputSize(int len);

        public void updateAAD(byte[] input, int offset, int length);

        public int processByte(byte in, byte[] out, int outOff)
            throws DataLengthException;

        public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff)
            throws DataLengthException;

        public int doFinal(byte[] out, int outOff)
            throws IllegalStateException,
            BadPaddingException;
    }

    private static class BufferedGenericBlockCipher
        implements GenericBlockCipher
    {
        private BufferedBlockCipher cipher;

        BufferedGenericBlockCipher(BufferedBlockCipher cipher)
        {
            this.cipher = cipher;
        }

        BufferedGenericBlockCipher(com.android.org.bouncycastle.crypto.BlockCipher cipher)
        {
            this(cipher, new PKCS7Padding());
        }

        BufferedGenericBlockCipher(com.android.org.bouncycastle.crypto.BlockCipher cipher, BlockCipherPadding padding)
        {
            this.cipher = new PaddedBufferedBlockCipher(cipher, padding);
        }

        public void init(boolean forEncryption, CipherParameters params)
            throws IllegalArgumentException
        {
            cipher.init(forEncryption, params);
        }

        public boolean wrapOnNoPadding()
        {
            return !(cipher instanceof CTSBlockCipher);
        }

        public String getAlgorithmName()
        {
            return cipher.getUnderlyingCipher().getAlgorithmName();
        }

        public com.android.org.bouncycastle.crypto.BlockCipher getUnderlyingCipher()
        {
            return cipher.getUnderlyingCipher();
        }

        public int getOutputSize(int len)
        {
            return cipher.getOutputSize(len);
        }

        public int getUpdateOutputSize(int len)
        {
            return cipher.getUpdateOutputSize(len);
        }

        public void updateAAD(byte[] input, int offset, int length)
        {
            throw new UnsupportedOperationException("AAD is not supported in the current mode.");
        }

        public int processByte(byte in, byte[] out, int outOff)
            throws DataLengthException
        {
            return cipher.processByte(in, out, outOff);
        }

        public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff)
            throws DataLengthException
        {
            return cipher.processBytes(in, inOff, len, out, outOff);
        }

        public int doFinal(byte[] out, int outOff)
            throws IllegalStateException, BadPaddingException
        {
            try
            {
                return cipher.doFinal(out, outOff);
            }
            catch (InvalidCipherTextException e)
            {
                throw new BadPaddingException(e.getMessage());
            }
        }
    }

    // BEGIN Android-removed: unsupported
    // private static class BufferedFPEBlockCipher
    //     implements GenericBlockCipher
    // {
    //     private FPEEngine cipher;
    //     private ErasableOutputStream eOut = new ErasableOutputStream();

    //     BufferedFPEBlockCipher(FPEEngine cipher)
    //     {
    //         this.cipher = cipher;
    //     }

    //     public void init(boolean forEncryption, CipherParameters params)
    //         throws IllegalArgumentException
    //     {
    //         cipher.init(forEncryption, params);
    //     }

    //     public boolean wrapOnNoPadding()
    //     {
    //         return false;
    //     }

    //     public String getAlgorithmName()
    //     {
    //         return cipher.getAlgorithmName();
    //     }

    //     public org.bouncycastle.crypto.BlockCipher getUnderlyingCipher()
    //     {
    //         throw new IllegalStateException("not applicable for FPE");
    //     }

    //     public int getOutputSize(int len)
    //     {
    //         return eOut.size() + len;
    //     }

    //     public int getUpdateOutputSize(int len)
    //     {
    //         return 0;
    //     }

    //     public void updateAAD(byte[] input, int offset, int length)
    //     {
    //         throw new UnsupportedOperationException("AAD is not supported in the current mode.");
    //     }

    //     public int processByte(byte in, byte[] out, int outOff)
    //         throws DataLengthException
    //     {
    //         eOut.write(in);

    //         return 0;
    //     }

    //     public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff)
    //         throws DataLengthException
    //     {
    //         eOut.write(in, inOff, len);

    //         return 0;
    //     }

    //     public int doFinal(byte[] out, int outOff)
    //         throws IllegalStateException, BadPaddingException
    //     {
    //         try
    //         {
    //             return cipher.processBlock(eOut.getBuf(), 0, eOut.size(), out, outOff);
    //         }
    //         finally
    //         {
    //             eOut.erase();
    //         }
    //     }
    // }
    // END Android-removed: unsupported

    private static class AEADGenericBlockCipher
        implements GenericBlockCipher
    {
        private static final Constructor aeadBadTagConstructor;

        static
        {
            Class aeadBadTagClass = ClassUtil.loadClass(BaseBlockCipher.class, "javax.crypto.AEADBadTagException");
            if (aeadBadTagClass != null)
            {
                aeadBadTagConstructor = findExceptionConstructor(aeadBadTagClass);
            }
            else
            {
                aeadBadTagConstructor = null;
            }
        }

        private static Constructor findExceptionConstructor(Class clazz)
        {
            try
            {
                return clazz.getConstructor(new Class[]{String.class});
            }
            catch (Exception e)
            {
                return null;
            }
        }

        private AEADCipher cipher;

        AEADGenericBlockCipher(AEADCipher cipher)
        {
            this.cipher = cipher;
        }

        public void init(boolean forEncryption, CipherParameters params)
            throws IllegalArgumentException
        {
            cipher.init(forEncryption, params);
        }

        public String getAlgorithmName()
        {
            if (cipher instanceof AEADBlockCipher)
            {
                return ((AEADBlockCipher)cipher).getUnderlyingCipher().getAlgorithmName();
            }

            return cipher.getAlgorithmName();
        }

        public boolean wrapOnNoPadding()
        {
            return false;
        }

        public com.android.org.bouncycastle.crypto.BlockCipher getUnderlyingCipher()
        {
            if (cipher instanceof AEADBlockCipher)
            {
                return ((AEADBlockCipher)cipher).getUnderlyingCipher();
            }

            return null;
        }

        public int getOutputSize(int len)
        {
            return cipher.getOutputSize(len);
        }

        public int getUpdateOutputSize(int len)
        {
            return cipher.getUpdateOutputSize(len);
        }

        public void updateAAD(byte[] input, int offset, int length)
        {
            cipher.processAADBytes(input, offset, length);
        }

        public int processByte(byte in, byte[] out, int outOff)
            throws DataLengthException
        {
            return cipher.processByte(in, out, outOff);
        }

        public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff)
            throws DataLengthException
        {
            return cipher.processBytes(in, inOff, len, out, outOff);
        }

        public int doFinal(byte[] out, int outOff)
            throws IllegalStateException, BadPaddingException
        {
            try
            {
                return cipher.doFinal(out, outOff);
            }
            catch (InvalidCipherTextException e)
            {
                if (aeadBadTagConstructor != null)
                {
                    BadPaddingException aeadBadTag = null;
                    try
                    {
                        aeadBadTag = (BadPaddingException)aeadBadTagConstructor
                            .newInstance(new Object[]{e.getMessage()});
                    }
                    catch (Exception i)
                    {
                        // Shouldn't happen, but fall through to BadPaddingException
                    }
                    if (aeadBadTag != null)
                    {
                        throw aeadBadTag;
                    }
                }
                throw new BadPaddingException(e.getMessage());
            }
        }
    }
}

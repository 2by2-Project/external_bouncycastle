/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.internal.org.bouncycastle.crypto.generators;

import com.android.internal.org.bouncycastle.crypto.CipherParameters;
import com.android.internal.org.bouncycastle.crypto.Digest;
import com.android.internal.org.bouncycastle.crypto.PBEParametersGenerator;
import com.android.internal.org.bouncycastle.crypto.params.KeyParameter;
import com.android.internal.org.bouncycastle.crypto.params.ParametersWithIV;
// Android-changed: Use Android digests
// import org.bouncycastle.crypto.util.DigestFactory;
import com.android.internal.org.bouncycastle.crypto.digests.AndroidDigestFactory;

/**
 * Generator for PBE derived keys and ivs as usd by OpenSSL.
 * <p>
 * Originally this scheme was a simple extension of PKCS 5 V2.0 Scheme 1 using MD5 with an
 * iteration count of 1. The default digest was changed to SHA-256 with OpenSSL 1.1.0. This
 * implementation still defaults to MD5, but the digest can now be set.
 * <p>
 * @hide This class is not part of the Android public SDK API
 */
public class OpenSSLPBEParametersGenerator
    extends PBEParametersGenerator
{
    // Android-changed: Use Android digests
    // private Digest  digest = DigestFactory.createMD5();
    private Digest  digest = AndroidDigestFactory.getMD5();

    /**
     * Construct a OpenSSL Parameters generator.
     */
    public OpenSSLPBEParametersGenerator()
    {
    }

    /**
     * Initialise - note the iteration count for this algorithm is fixed at 1.
     * 
     * @param password password to use.
     * @param salt salt to use.
     */
    public void init(
       byte[] password,
       byte[] salt)
    {
        super.init(password, salt, 1);
    }
    
    /**
     * the derived key function, the ith hash of the password and the salt.
     */
    private byte[] generateDerivedKey(
        int bytesNeeded)
    {
        byte[]  buf = new byte[digest.getDigestSize()];
        byte[]  key = new byte[bytesNeeded];
        int     offset = 0;
        
        for (;;)
        {
            digest.update(password, 0, password.length);
            digest.update(salt, 0, salt.length);

            digest.doFinal(buf, 0);
            
            int len = (bytesNeeded > buf.length) ? buf.length : bytesNeeded;
            System.arraycopy(buf, 0, key, offset, len);
            offset += len;

            // check if we need any more
            bytesNeeded -= len;
            if (bytesNeeded == 0)
            {
                break;
            }

            // do another round
            digest.reset();
            digest.update(buf, 0, buf.length);
        }
        
        return key;
    }

    /**
     * Generate a key parameter derived from the password, salt, and iteration
     * count we are currently initialised with.
     *
     * @param keySize the size of the key we want (in bits)
     * @return a KeyParameter object.
     * @exception IllegalArgumentException if the key length larger than the base hash size.
     */
    public CipherParameters generateDerivedParameters(
        int keySize)
    {
        keySize = keySize / 8;

        byte[]  dKey = generateDerivedKey(keySize);

        return new KeyParameter(dKey, 0, keySize);
    }

    /**
     * Generate a key with initialisation vector parameter derived from
     * the password, salt, and iteration count we are currently initialised
     * with.
     *
     * @param keySize the size of the key we want (in bits)
     * @param ivSize the size of the iv we want (in bits)
     * @return a ParametersWithIV object.
     * @exception IllegalArgumentException if keySize + ivSize is larger than the base hash size.
     */
    public CipherParameters generateDerivedParameters(
        int     keySize,
        int     ivSize)
    {
        keySize = keySize / 8;
        ivSize = ivSize / 8;

        byte[]  dKey = generateDerivedKey(keySize + ivSize);

        return new ParametersWithIV(new KeyParameter(dKey, 0, keySize), dKey, keySize, ivSize);
    }

    /**
     * Generate a key parameter for use with a MAC derived from the password,
     * salt, and iteration count we are currently initialised with.
     *
     * @param keySize the size of the key we want (in bits)
     * @return a KeyParameter object.
     * @exception IllegalArgumentException if the key length larger than the base hash size.
     */
    public CipherParameters generateDerivedMacParameters(
        int keySize)
    {
        return generateDerivedParameters(keySize);
    }
}

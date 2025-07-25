/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.org.bouncycastle.crypto.digests;

import com.android.org.bouncycastle.crypto.CryptoServiceProperties;
import com.android.org.bouncycastle.crypto.CryptoServicePurpose;
import com.android.org.bouncycastle.crypto.CryptoServicesRegistrar;
import com.android.org.bouncycastle.util.Memoable;
import com.android.org.bouncycastle.util.Pack;


/**
 * FIPS 180-2 implementation of SHA-512.
 *
 * <pre>
 *         block  word  digest
 * SHA-1   512    32    160
 * SHA-256 512    32    256
 * SHA-384 1024   64    384
 * SHA-512 1024   64    512
 * </pre>
 * @hide This class is not part of the Android public SDK API
 */
public class SHA512Digest
    extends LongDigest
{
    private static final int    DIGEST_LENGTH = 64;

    /**
     * Standard constructor
     */
    public SHA512Digest()
    {
        this(CryptoServicePurpose.ANY);
    }

    /**
     * Standard constructor, with purpose
     */
    public SHA512Digest(CryptoServicePurpose purpose)
    {
        super(purpose);

        CryptoServicesRegistrar.checkConstraints(cryptoServiceProperties());

        reset();
    }

    /**
     * Copy constructor.  This will copy the state of the provided
     * message digest.
     */
    public SHA512Digest(SHA512Digest t)
    {
        super(t);

        CryptoServicesRegistrar.checkConstraints(cryptoServiceProperties());
    }

    /**
     * State constructor - create a digest initialised with the state of a previous one.
     *
     * @param encodedState the encoded state from the originating digest.
     */
    public SHA512Digest(byte[] encodedState)
    {
        super(CryptoServicePurpose.values()[encodedState[encodedState.length - 1]]);

        restoreState(encodedState);

        CryptoServicesRegistrar.checkConstraints(cryptoServiceProperties());
    }

    public String getAlgorithmName()
    {
        return "SHA-512";
    }

    public int getDigestSize()
    {
        return DIGEST_LENGTH;
    }

    public int doFinal(
        byte[]  out,
        int     outOff)
    {
        finish();

        Pack.longToBigEndian(H1, out, outOff);
        Pack.longToBigEndian(H2, out, outOff + 8);
        Pack.longToBigEndian(H3, out, outOff + 16);
        Pack.longToBigEndian(H4, out, outOff + 24);
        Pack.longToBigEndian(H5, out, outOff + 32);
        Pack.longToBigEndian(H6, out, outOff + 40);
        Pack.longToBigEndian(H7, out, outOff + 48);
        Pack.longToBigEndian(H8, out, outOff + 56);

        reset();

        return DIGEST_LENGTH;
    }

    /**
     * reset the chaining variables
     */
    public void reset()
    {
        super.reset();

        /* SHA-512 initial hash value
         * The first 64 bits of the fractional parts of the square roots
         * of the first eight prime numbers
         */
        H1 = 0x6a09e667f3bcc908L;
        H2 = 0xbb67ae8584caa73bL;
        H3 = 0x3c6ef372fe94f82bL;
        H4 = 0xa54ff53a5f1d36f1L;
        H5 = 0x510e527fade682d1L;
        H6 = 0x9b05688c2b3e6c1fL;
        H7 = 0x1f83d9abfb41bd6bL;
        H8 = 0x5be0cd19137e2179L;
    }

    public Memoable copy()
    {
        return new SHA512Digest(this);
    }

    public void reset(Memoable other)
    {
        SHA512Digest d = (SHA512Digest)other;

        copyIn(d);
    }

    public byte[] getEncodedState()
    {
        byte[] encoded = new byte[getEncodedStateSize() + 1];
        super.populateState(encoded);

        encoded[encoded.length - 1] = (byte)purpose.ordinal();

        return encoded;
    }

    protected CryptoServiceProperties cryptoServiceProperties()
    {
        return Utils.getDefaultProperties(this, 256, purpose);
    }
}


/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.org.bouncycastle.crypto.engines;

import java.math.BigInteger;

import com.android.org.bouncycastle.crypto.CipherParameters;
import com.android.org.bouncycastle.crypto.CryptoServicePurpose;
import com.android.org.bouncycastle.crypto.CryptoServicesRegistrar;
import com.android.org.bouncycastle.crypto.DataLengthException;
import com.android.org.bouncycastle.crypto.constraints.ConstraintUtils;
import com.android.org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import com.android.org.bouncycastle.crypto.params.ParametersWithRandom;
import com.android.org.bouncycastle.crypto.params.RSAKeyParameters;
import com.android.org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import com.android.org.bouncycastle.util.Arrays;

/**
 * this does your basic RSA algorithm.
 */
class RSACoreEngine
{
    private RSAKeyParameters key;
    private boolean forEncryption;

    /**
     * initialise the RSA engine.
     *
     * @param forEncryption true if we are encrypting, false otherwise.
     * @param param         the necessary RSA key parameters.
     */
    public void init(
        boolean forEncryption,
        CipherParameters param)
    {
        if (param instanceof ParametersWithRandom)
        {
            ParametersWithRandom rParam = (ParametersWithRandom)param;

            key = (RSAKeyParameters)rParam.getParameters();
        }
        else
        {
            key = (RSAKeyParameters)param;
        }

        this.forEncryption = forEncryption;

        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties("RSA", ConstraintUtils.bitsOfSecurityFor(key.getModulus()), key, getPurpose(key.isPrivate(), forEncryption)));
    }

    /**
     * Return the maximum size for an input block to this engine.
     * For RSA this is always one byte less than the key size on
     * encryption, and the same length as the key size on decryption.
     *
     * @return maximum size for an input block.
     */
    public int getInputBlockSize()
    {
        int bitSize = key.getModulus().bitLength();

        if (forEncryption)
        {
            return (bitSize + 7) / 8 - 1;
        }
        else
        {
            return (bitSize + 7) / 8;
        }
    }

    /**
     * Return the maximum size for an output block to this engine.
     * For RSA this is always one byte less than the key size on
     * decryption, and the same length as the key size on encryption.
     *
     * @return maximum size for an output block.
     */
    public int getOutputBlockSize()
    {
        int bitSize = key.getModulus().bitLength();

        if (forEncryption)
        {
            return (bitSize + 7) / 8;
        }
        else
        {
            return (bitSize + 7) / 8 - 1;
        }
    }

    public BigInteger convertInput(
        byte[] in,
        int inOff,
        int inLen)
    {
        if (inLen > (getInputBlockSize() + 1))
        {
            throw new DataLengthException("input too large for RSA cipher.");
        }
        else if (inLen == (getInputBlockSize() + 1) && !forEncryption)
        {
            throw new DataLengthException("input too large for RSA cipher.");
        }

        byte[] block;

        if (inOff != 0 || inLen != in.length)
        {
            block = new byte[inLen];

            System.arraycopy(in, inOff, block, 0, inLen);
        }
        else
        {
            block = in;
        }

        BigInteger res = new BigInteger(1, block);
        if (res.compareTo(key.getModulus()) >= 0)
        {
            throw new DataLengthException("input too large for RSA cipher.");
        }

        return res;
    }

    public byte[] convertOutput(
        BigInteger result)
    {
        byte[] output = result.toByteArray();

        if (forEncryption)
        {
            if (output[0] == 0 && output.length > getOutputBlockSize())        // have ended up with an extra zero byte, copy down.
            {
                byte[] tmp = new byte[output.length - 1];

                System.arraycopy(output, 1, tmp, 0, tmp.length);

                return tmp;
            }

            if (output.length < getOutputBlockSize())     // have ended up with less bytes than normal, lengthen
            {
                byte[] tmp = new byte[getOutputBlockSize()];

                System.arraycopy(output, 0, tmp, tmp.length - output.length, output.length);

                return tmp;
            }

            return output;
        }
        else
        {
            byte[] rv;
            if (output[0] == 0)        // have ended up with an extra zero byte, copy down.
            {
                rv = new byte[output.length - 1];

                System.arraycopy(output, 1, rv, 0, rv.length);
            }
            else        // maintain decryption time
            {
                rv = new byte[output.length];

                System.arraycopy(output, 0, rv, 0, rv.length);
            }

            Arrays.fill(output, (byte)0);

            return rv;
        }
    }

    public BigInteger processBlock(BigInteger input)
    {
        if (key instanceof RSAPrivateCrtKeyParameters)
        {
            //
            // we have the extra factors, use the Chinese Remainder Theorem - the author
            // wishes to express his thanks to Dirk Bonekaemper at rtsffm.com for
            // advice regarding the expression of this.
            //
            RSAPrivateCrtKeyParameters crtKey = (RSAPrivateCrtKeyParameters)key;

            BigInteger e = crtKey.getPublicExponent();
            if (e != null)   // can't apply fault-attack countermeasure without public exponent
            {
                BigInteger p = crtKey.getP();
                BigInteger q = crtKey.getQ();
                BigInteger dP = crtKey.getDP();
                BigInteger dQ = crtKey.getDQ();
                BigInteger qInv = crtKey.getQInv();

                BigInteger mP, mQ, h, m;

                // mP = ((input mod p) ^ dP)) mod p
                mP = (input.remainder(p)).modPow(dP, p);

                // mQ = ((input mod q) ^ dQ)) mod q
                mQ = (input.remainder(q)).modPow(dQ, q);

                // h = qInv * (mP - mQ) mod p
                h = mP.subtract(mQ);
                h = h.multiply(qInv);
                h = h.mod(p);               // mod (in Java) returns the positive residual

                // m = h * q + mQ
                m = h.multiply(q).add(mQ);

                // defence against Arjen Lenstra’s CRT attack
                BigInteger check = m.modPow(e, crtKey.getModulus()); 
                if (!check.equals(input))
                {
                    throw new IllegalStateException("RSA engine faulty decryption/signing detected");
                }

                return m;
            }
        }

        return input.modPow(key.getExponent(), key.getModulus());
    }

    private CryptoServicePurpose getPurpose(boolean isPrivate, boolean forEncryption)
    {
        boolean isSigning = isPrivate && forEncryption;
        boolean isEncryption = !isPrivate && forEncryption;
        boolean isVerifying = !isPrivate && !forEncryption;

        if (isSigning)
        {
            return CryptoServicePurpose.SIGNING;
        }
        if (isEncryption)
        {
            return CryptoServicePurpose.ENCRYPTION;
        }
        if (isVerifying)
        {
            return CryptoServicePurpose.VERIFYING;
        }

        return CryptoServicePurpose.DECRYPTION;
    }
}

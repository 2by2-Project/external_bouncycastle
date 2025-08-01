/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.org.bouncycastle.crypto.agreement;

import java.math.BigInteger;

import com.android.org.bouncycastle.crypto.BasicAgreement;
import com.android.org.bouncycastle.crypto.CipherParameters;
import com.android.org.bouncycastle.crypto.CryptoServicesRegistrar;
import com.android.org.bouncycastle.crypto.params.ECDomainParameters;
import com.android.org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import com.android.org.bouncycastle.crypto.params.ECPublicKeyParameters;
import com.android.org.bouncycastle.math.ec.ECAlgorithms;
import com.android.org.bouncycastle.math.ec.ECConstants;
import com.android.org.bouncycastle.math.ec.ECPoint;

/**
 * P1363 7.2.1 ECSVDP-DH
 *
 * ECSVDP-DH is Elliptic Curve Secret Value Derivation Primitive,
 * Diffie-Hellman version. It is based on the work of [DH76], [Mil86],
 * and [Kob87]. This primitive derives a shared secret value from one
 * party's private key and another party's public key, where both have
 * the same set of EC domain parameters. If two parties correctly
 * execute this primitive, they will produce the same output. This
 * primitive can be invoked by a scheme to derive a shared secret key;
 * specifically, it may be used with the schemes ECKAS-DH1 and
 * DL/ECKAS-DH2. It assumes that the input keys are valid (see also
 * Section 7.2.2).
 * @hide This class is not part of the Android public SDK API
 */
public class ECDHBasicAgreement
    implements BasicAgreement
{
    private ECPrivateKeyParameters key;

    public void init(
        CipherParameters key)
    {
        this.key = (ECPrivateKeyParameters)key;

        CryptoServicesRegistrar.checkConstraints(Utils.getDefaultProperties("ECDH", this.key));
    }

    public int getFieldSize()
    {
        return (key.getParameters().getCurve().getFieldSize() + 7) / 8;
    }

    public BigInteger calculateAgreement(
        CipherParameters pubKey)
    {
        ECPublicKeyParameters pub = (ECPublicKeyParameters)pubKey;
        ECDomainParameters params = key.getParameters();
        if (!params.equals(pub.getParameters()))
        {
            throw new IllegalStateException("ECDH public key has wrong domain parameters");
        }

        BigInteger d = key.getD();

        // Always perform calculations on the exact curve specified by our private key's parameters
        ECPoint Q = ECAlgorithms.cleanPoint(params.getCurve(), pub.getQ());
        if (Q.isInfinity())
        {
            throw new IllegalStateException("Infinity is not a valid public key for ECDH");
        }

        BigInteger h = params.getH();
        if (!h.equals(ECConstants.ONE))
        {
            d = params.getHInv().multiply(d).mod(params.getN());
            Q = ECAlgorithms.referenceMultiply(Q, h);
        }

        ECPoint P = Q.multiply(d).normalize();
        if (P.isInfinity())
        {
            throw new IllegalStateException("Infinity is not a valid agreement value for ECDH");
        }

        return P.getAffineXCoord().toBigInteger();
    }
}

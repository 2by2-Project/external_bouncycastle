/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.internal.org.bouncycastle.asn1.x509;

import com.android.internal.org.bouncycastle.asn1.ASN1BitString;
import com.android.internal.org.bouncycastle.asn1.ASN1Integer;
import com.android.internal.org.bouncycastle.asn1.ASN1Object;
import com.android.internal.org.bouncycastle.asn1.ASN1Primitive;
import com.android.internal.org.bouncycastle.asn1.ASN1Sequence;
import com.android.internal.org.bouncycastle.asn1.ASN1TaggedObject;
import com.android.internal.org.bouncycastle.asn1.x500.X500Name;

/**
 * an X509Certificate structure.
 * <pre>
 *  Certificate ::= SEQUENCE {
 *      tbsCertificate          TBSCertificate,
 *      signatureAlgorithm      AlgorithmIdentifier,
 *      signature               BIT STRING
 *  }
 * </pre>
 * @hide This class is not part of the Android public SDK API
 */
public class Certificate
    extends ASN1Object
{
    ASN1Sequence  seq;
    TBSCertificate tbsCert;
    AlgorithmIdentifier     sigAlgId;
    ASN1BitString            sig;

    public static Certificate getInstance(
        ASN1TaggedObject obj,
        boolean          explicit)
    {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static Certificate getInstance(
        Object  obj)
    {
        if (obj instanceof Certificate)
        {
            return (Certificate)obj;
        }
        else if (obj != null)
        {
            return new Certificate(ASN1Sequence.getInstance(obj));
        }

        return null;
    }

    private Certificate(
        ASN1Sequence seq)
    {
        this.seq = seq;

        //
        // correct x509 certficate
        //
        if (seq.size() == 3)
        {
            tbsCert = TBSCertificate.getInstance(seq.getObjectAt(0));
            sigAlgId = AlgorithmIdentifier.getInstance(seq.getObjectAt(1));

            sig = ASN1BitString.getInstance(seq.getObjectAt(2));
        }
        else
        {
            throw new IllegalArgumentException("sequence wrong size for a certificate");
        }
    }

    public TBSCertificate getTBSCertificate()
    {
        return tbsCert;
    }

    public ASN1Integer getVersion()
    {
        return tbsCert.getVersion();
    }

    public int getVersionNumber()
    {
        return tbsCert.getVersionNumber();
    }

    public ASN1Integer getSerialNumber()
    {
        return tbsCert.getSerialNumber();
    }

    public X500Name getIssuer()
    {
        return tbsCert.getIssuer();
    }

    public Time getStartDate()
    {
        return tbsCert.getStartDate();
    }

    public Time getEndDate()
    {
        return tbsCert.getEndDate();
    }

    public X500Name getSubject()
    {
        return tbsCert.getSubject();
    }

    public SubjectPublicKeyInfo getSubjectPublicKeyInfo()
    {
        return tbsCert.getSubjectPublicKeyInfo();
    }

    public AlgorithmIdentifier getSignatureAlgorithm()
    {
        return sigAlgId;
    }

    public ASN1BitString getSignature()
    {
        return sig;
    }

    public ASN1Primitive toASN1Primitive()
    {
        return seq;
    }
}

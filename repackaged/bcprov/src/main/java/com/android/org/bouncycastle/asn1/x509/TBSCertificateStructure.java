/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.org.bouncycastle.asn1.x509;

import com.android.org.bouncycastle.asn1.ASN1BitString;
import com.android.org.bouncycastle.asn1.ASN1Integer;
import com.android.org.bouncycastle.asn1.ASN1Object;
import com.android.org.bouncycastle.asn1.ASN1Primitive;
import com.android.org.bouncycastle.asn1.ASN1Sequence;
import com.android.org.bouncycastle.asn1.ASN1TaggedObject;
import com.android.org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import com.android.org.bouncycastle.asn1.x500.X500Name;

/**
 * The TBSCertificate object.
 * <pre>
 * TBSCertificate ::= SEQUENCE {
 *      version          [ 0 ]  Version DEFAULT v1(0),
 *      serialNumber            CertificateSerialNumber,
 *      signature               AlgorithmIdentifier,
 *      issuer                  Name,
 *      validity                Validity,
 *      subject                 Name,
 *      subjectPublicKeyInfo    SubjectPublicKeyInfo,
 *      issuerUniqueID    [ 1 ] IMPLICIT UniqueIdentifier OPTIONAL,
 *      subjectUniqueID   [ 2 ] IMPLICIT UniqueIdentifier OPTIONAL,
 *      extensions        [ 3 ] Extensions OPTIONAL
 *      }
 * </pre>
 * <p>
 * Note: issuerUniqueID and subjectUniqueID are both deprecated by the IETF. This class
 * will parse them, but you really shouldn't be creating new ones.
 * @deprecated use TBSCertificate
 * @hide This class is not part of the Android public SDK API
 */
public class TBSCertificateStructure
    extends ASN1Object
    implements X509ObjectIdentifiers, PKCSObjectIdentifiers
{
    ASN1Sequence            seq;

    ASN1Integer             version;
    ASN1Integer             serialNumber;
    AlgorithmIdentifier     signature;
    X500Name                issuer;
    Time                    startDate, endDate;
    X500Name                subject;
    SubjectPublicKeyInfo    subjectPublicKeyInfo;
    ASN1BitString           issuerUniqueId;
    ASN1BitString           subjectUniqueId;
    X509Extensions          extensions;

    public static TBSCertificateStructure getInstance(
        ASN1TaggedObject obj,
        boolean          explicit)
    {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static TBSCertificateStructure getInstance(
        Object  obj)
    {
        if (obj instanceof TBSCertificateStructure)
        {
            return (TBSCertificateStructure)obj;
        }
        else if (obj != null)
        {
            return new TBSCertificateStructure(ASN1Sequence.getInstance(obj));
        }

        return null;
    }

    public TBSCertificateStructure(
        ASN1Sequence  seq)
    {
        int         seqStart = 0;

        this.seq = seq;

        //
        // some certficates don't include a version number - we assume v1
        //
        if (seq.getObjectAt(0) instanceof ASN1TaggedObject)
        {
            version = ASN1Integer.getInstance((ASN1TaggedObject)seq.getObjectAt(0), true);
        }
        else
        {
            seqStart = -1;          // field 0 is missing!
            version = new ASN1Integer(0);
        }

        serialNumber = ASN1Integer.getInstance(seq.getObjectAt(seqStart + 1));

        signature = AlgorithmIdentifier.getInstance(seq.getObjectAt(seqStart + 2));
        issuer = X500Name.getInstance(seq.getObjectAt(seqStart + 3));

        //
        // before and after dates
        //
        ASN1Sequence  dates = (ASN1Sequence)seq.getObjectAt(seqStart + 4);

        startDate = Time.getInstance(dates.getObjectAt(0));
        endDate = Time.getInstance(dates.getObjectAt(1));

        subject = X500Name.getInstance(seq.getObjectAt(seqStart + 5));

        //
        // public key info.
        //
        subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(seq.getObjectAt(seqStart + 6));

        for (int extras = seq.size() - (seqStart + 6) - 1; extras > 0; extras--)
        {
            ASN1TaggedObject extra = ASN1TaggedObject.getInstance(seq.getObjectAt(seqStart + 6 + extras));

            switch (extra.getTagNo())
            {
            case 1:
                issuerUniqueId = ASN1BitString.getInstance(extra, false);
                break;
            case 2:
                subjectUniqueId = ASN1BitString.getInstance(extra, false);
                break;
            case 3:
                extensions = X509Extensions.getInstance(extra);
            }
        }
    }

    public int getVersion()
    {
        return version.intValueExact() + 1;
    }

    public ASN1Integer getVersionNumber()
    {
        return version;
    }

    public ASN1Integer getSerialNumber()
    {
        return serialNumber;
    }

    public AlgorithmIdentifier getSignature()
    {
        return signature;
    }

    public X500Name getIssuer()
    {
        return issuer;
    }

    public Time getStartDate()
    {
        return startDate;
    }

    public Time getEndDate()
    {
        return endDate;
    }

    public X500Name getSubject()
    {
        return subject;
    }

    public SubjectPublicKeyInfo getSubjectPublicKeyInfo()
    {
        return subjectPublicKeyInfo;
    }

    public ASN1BitString getIssuerUniqueId()
    {
        return issuerUniqueId;
    }

    public ASN1BitString getSubjectUniqueId()
    {
        return subjectUniqueId;
    }

    public X509Extensions getExtensions()
    {
        return extensions;
    }

    public ASN1Primitive toASN1Primitive()
    {
        return seq;
    }
}

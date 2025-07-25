/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.org.bouncycastle.asn1.pkcs;

import com.android.org.bouncycastle.asn1.ASN1Encodable;
import com.android.org.bouncycastle.asn1.ASN1EncodableVector;
import com.android.org.bouncycastle.asn1.ASN1Integer;
import com.android.org.bouncycastle.asn1.ASN1Object;
import com.android.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.android.org.bouncycastle.asn1.ASN1OctetString;
import com.android.org.bouncycastle.asn1.ASN1Primitive;
import com.android.org.bouncycastle.asn1.ASN1Sequence;
import com.android.org.bouncycastle.asn1.ASN1TaggedObject;
import com.android.org.bouncycastle.asn1.BERSequence;
import com.android.org.bouncycastle.asn1.BERTaggedObject;
import com.android.org.bouncycastle.asn1.x509.AlgorithmIdentifier;

/**
 * The EncryptedData object.
 * <pre>
 *      EncryptedData ::= SEQUENCE {
 *           version Version,
 *           encryptedContentInfo EncryptedContentInfo
 *      }
 *
 *
 *      EncryptedContentInfo ::= SEQUENCE {
 *          contentType ContentType,
 *          contentEncryptionAlgorithm  ContentEncryptionAlgorithmIdentifier,
 *          encryptedContent [0] IMPLICIT EncryptedContent OPTIONAL
 *    }
 *
 *    EncryptedContent ::= OCTET STRING
 * </pre>
 * @hide This class is not part of the Android public SDK API
 */
public class EncryptedData
    extends ASN1Object
{
    ASN1Sequence                data;

    public static EncryptedData getInstance(
         Object  obj)
    {
         if (obj instanceof EncryptedData)
         {
             return (EncryptedData)obj;
         }

         if (obj != null)
         {
             return new EncryptedData(ASN1Sequence.getInstance(obj));
         }

         return null;
    }
     
    private EncryptedData(
        ASN1Sequence seq)
    {
        ASN1Integer version = (ASN1Integer)seq.getObjectAt(0);
        if (!version.hasValue(0))
        {
            throw new IllegalArgumentException("sequence not version 0");
        }

        this.data = ASN1Sequence.getInstance(seq.getObjectAt(1));
    }

    public EncryptedData(
        ASN1ObjectIdentifier contentType,
        AlgorithmIdentifier     encryptionAlgorithm,
        ASN1Encodable content)
    {
        ASN1EncodableVector v = new ASN1EncodableVector(3);

        v.add(contentType);
        v.add(encryptionAlgorithm.toASN1Primitive());
        v.add(new BERTaggedObject(false, 0, content));

        data = new BERSequence(v);
    }
        
    public ASN1ObjectIdentifier getContentType()
    {
        return ASN1ObjectIdentifier.getInstance(data.getObjectAt(0));
    }

    public AlgorithmIdentifier getEncryptionAlgorithm()
    {
        return AlgorithmIdentifier.getInstance(data.getObjectAt(1));
    }

    public ASN1OctetString getContent()
    {
        if (data.size() == 3)
        {
            ASN1TaggedObject o = ASN1TaggedObject.getInstance(data.getObjectAt(2));

            return ASN1OctetString.getInstance(o, false);
        }

        return null;
    }

    public ASN1Primitive toASN1Primitive()
    {
        ASN1EncodableVector v = new ASN1EncodableVector(2);

        v.add(new ASN1Integer(0));
        v.add(data);

        return new BERSequence(v);
    }
}

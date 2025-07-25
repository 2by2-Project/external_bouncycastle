/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.internal.org.bouncycastle.cert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import com.android.internal.org.bouncycastle.asn1.ASN1EncodableVector;
import com.android.internal.org.bouncycastle.asn1.ASN1Encoding;
import com.android.internal.org.bouncycastle.asn1.ASN1InputStream;
import com.android.internal.org.bouncycastle.asn1.ASN1Integer;
import com.android.internal.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.android.internal.org.bouncycastle.asn1.ASN1Primitive;
import com.android.internal.org.bouncycastle.asn1.ASN1Sequence;
import com.android.internal.org.bouncycastle.asn1.DERSequence;
import com.android.internal.org.bouncycastle.asn1.x500.X500Name;
import com.android.internal.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.android.internal.org.bouncycastle.asn1.x509.AltSignatureAlgorithm;
import com.android.internal.org.bouncycastle.asn1.x509.AltSignatureValue;
import com.android.internal.org.bouncycastle.asn1.x509.CertificateList;
import com.android.internal.org.bouncycastle.asn1.x509.Extension;
import com.android.internal.org.bouncycastle.asn1.x509.Extensions;
import com.android.internal.org.bouncycastle.asn1.x509.GeneralName;
import com.android.internal.org.bouncycastle.asn1.x509.GeneralNames;
import com.android.internal.org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import com.android.internal.org.bouncycastle.asn1.x509.TBSCertList;
import com.android.internal.org.bouncycastle.asn1.x509.Time;
import com.android.internal.org.bouncycastle.operator.ContentVerifier;
import com.android.internal.org.bouncycastle.operator.ContentVerifierProvider;
import com.android.internal.org.bouncycastle.util.Encodable;

/**
 * Holding class for an X.509 CRL structure.
 * @hide This class is not part of the Android public SDK API
 */
public class X509CRLHolder
    implements Encodable, Serializable
{
    private static final long serialVersionUID = 20170722001L;

    private transient CertificateList x509CRL;
    private transient boolean isIndirect;
    private transient Extensions extensions;
    private transient GeneralNames issuerName;

    private static CertificateList parseStream(InputStream stream)
        throws IOException
    {
        try
        {
            ASN1Primitive obj = new ASN1InputStream(stream, true).readObject();
            if (obj == null)
            {
                throw new IOException("no content found");
            }
            return CertificateList.getInstance(obj);
        }
        catch (ClassCastException e)
        {
            throw new CertIOException("malformed data: " + e.getMessage(), e);
        }
        catch (IllegalArgumentException e)
        {
            throw new CertIOException("malformed data: " + e.getMessage(), e);
        }
    }

    private static boolean isIndirectCRL(Extensions extensions)
    {
        if (extensions == null)
        {
            return false;
        }

        Extension ext = extensions.getExtension(Extension.issuingDistributionPoint);

        return ext != null && IssuingDistributionPoint.getInstance(ext.getParsedValue()).isIndirectCRL();
    }

    /**
     * Create a X509CRLHolder from the passed in bytes.
     *
     * @param crlEncoding BER/DER encoding of the CRL
     * @throws IOException in the event of corrupted data, or an incorrect structure.
     */
    public X509CRLHolder(byte[] crlEncoding)
        throws IOException
    {
        this(parseStream(new ByteArrayInputStream(crlEncoding)));
    }

    /**
     * Create a X509CRLHolder from the passed in InputStream.
     *
     * @param crlStream BER/DER encoded InputStream of the CRL
     * @throws IOException in the event of corrupted data, or an incorrect structure.
     */
    public X509CRLHolder(InputStream crlStream)
        throws IOException
    {
        this(parseStream(crlStream));
    }

    /**
     * Create a X509CRLHolder from the passed in ASN.1 structure.
     *
     * @param x509CRL an ASN.1 CertificateList structure.
     */
    public X509CRLHolder(CertificateList x509CRL)
    {
        init(x509CRL);
    }

    private void init(CertificateList x509CRL)
    {
        this.x509CRL = x509CRL;
        this.extensions = x509CRL.getTBSCertList().getExtensions();
        this.isIndirect = isIndirectCRL(extensions);
        this.issuerName = new GeneralNames(new GeneralName(x509CRL.getIssuer()));
    }

    /**
     * Return the ASN.1 encoding of this holder's CRL.
     *
     * @return a DER encoded byte array.
     * @throws IOException if an encoding cannot be generated.
     */
    public byte[] getEncoded()
        throws IOException
    {
        return x509CRL.getEncoded();
    }

    /**
     * Return the issuer of this holder's CRL.
     *
     * @return the CRL issuer.
     */
    public X500Name getIssuer()
    {
        return X500Name.getInstance(x509CRL.getIssuer());
    }

    public Date getThisUpdate()
    {
        return x509CRL.getThisUpdate().getDate();
    }

    public Date getNextUpdate()
    {
        Time update = x509CRL.getNextUpdate();

        if (update != null)
        {
            return update.getDate();
        }

        return null;
    }
    public X509CRLEntryHolder getRevokedCertificate(BigInteger serialNumber)
    {
        GeneralNames currentCA = issuerName;
        for (Enumeration en = x509CRL.getRevokedCertificateEnumeration(); en.hasMoreElements(); )
        {
            TBSCertList.CRLEntry entry = (TBSCertList.CRLEntry)en.nextElement();

            if (entry.getUserCertificate().hasValue(serialNumber))
            {
                return new X509CRLEntryHolder(entry, isIndirect, currentCA);
            }

            if (isIndirect && entry.hasExtensions())
            {
                Extension currentCaName = entry.getExtensions().getExtension(Extension.certificateIssuer);

                if (currentCaName != null)
                {
                    currentCA = GeneralNames.getInstance(currentCaName.getParsedValue());
                }
            }
        }

        return null;
    }

    /**
     * Return a collection of X509CRLEntryHolder objects, giving the details of the
     * revoked certificates that appear on this CRL.
     *
     * @return the revoked certificates as a collection of X509CRLEntryHolder objects.
     */
    public Collection getRevokedCertificates()
    {
        TBSCertList.CRLEntry[] entries = x509CRL.getRevokedCertificates();
        List l = new ArrayList(entries.length);
        GeneralNames currentCA = issuerName;

        for (Enumeration en = x509CRL.getRevokedCertificateEnumeration(); en.hasMoreElements(); )
        {
            TBSCertList.CRLEntry entry = (TBSCertList.CRLEntry)en.nextElement();
            X509CRLEntryHolder crlEntry = new X509CRLEntryHolder(entry, isIndirect, currentCA);

            l.add(crlEntry);

            currentCA = crlEntry.getCertificateIssuer();
        }

        return l;
    }

    /**
     * Return whether or not the holder's CRL contains extensions.
     *
     * @return true if extension are present, false otherwise.
     */
    public boolean hasExtensions()
    {
        return extensions != null;
    }

    /**
     * Look up the extension associated with the passed in OID.
     *
     * @param oid the OID of the extension of interest.
     * @return the extension if present, null otherwise.
     */
    public Extension getExtension(ASN1ObjectIdentifier oid)
    {
        if (extensions != null)
        {
            return extensions.getExtension(oid);
        }

        return null;
    }

    /**
     * Return the extensions block associated with this CRL if there is one.
     *
     * @return the extensions block, null otherwise.
     */
    public Extensions getExtensions()
    {
        return extensions;
    }

    /**
     * Returns a list of ASN1ObjectIdentifier objects representing the OIDs of the
     * extensions contained in this holder's CRL.
     *
     * @return a list of extension OIDs.
     */
    public List getExtensionOIDs()
    {
        return CertUtils.getExtensionOIDs(extensions);
    }

    /**
     * Returns a set of ASN1ObjectIdentifier objects representing the OIDs of the
     * critical extensions contained in this holder's CRL.
     *
     * @return a set of critical extension OIDs.
     */
    public Set getCriticalExtensionOIDs()
    {
        return CertUtils.getCriticalExtensionOIDs(extensions);
    }

    /**
     * Returns a set of ASN1ObjectIdentifier objects representing the OIDs of the
     * non-critical extensions contained in this holder's CRL.
     *
     * @return a set of non-critical extension OIDs.
     */
    public Set getNonCriticalExtensionOIDs()
    {
        return CertUtils.getNonCriticalExtensionOIDs(extensions);
    }

    /**
     * Return the underlying ASN.1 structure for the CRL in this holder.
     *
     * @return a CertificateList object.
     */
    public CertificateList toASN1Structure()
    {
        return x509CRL;
    }

    /**
     * Validate the signature on the CRL.
     *
     * @param verifierProvider a ContentVerifierProvider that can generate a verifier for the signature.
     * @return true if the signature is valid, false otherwise.
     * @throws CertException if the signature cannot be processed or is inappropriate.
     */
    public boolean isSignatureValid(ContentVerifierProvider verifierProvider)
        throws CertException
    {
        TBSCertList tbsCRL = x509CRL.getTBSCertList();

        if (!CertUtils.isAlgIdEqual(tbsCRL.getSignature(), x509CRL.getSignatureAlgorithm()))
        {
            throw new CertException("signature invalid - algorithm identifier mismatch");
        }

        ContentVerifier verifier;

        try
        {
            verifier = verifierProvider.get((tbsCRL.getSignature()));

            OutputStream sOut = verifier.getOutputStream();
            tbsCRL.encodeTo(sOut, ASN1Encoding.DER);
            sOut.close();
        }
        catch (Exception e)
        {
            throw new CertException("unable to process signature: " + e.getMessage(), e);
        }

        return verifier.verify(x509CRL.getSignature().getOctets());
    }

    public boolean isAlternativeSignatureValid(ContentVerifierProvider verifierProvider)
        throws CertException
    {
        TBSCertList tbsCrList = x509CRL.getTBSCertList();
        AltSignatureAlgorithm altSigAlg = AltSignatureAlgorithm.fromExtensions(tbsCrList.getExtensions());
        AltSignatureValue altSigValue = AltSignatureValue.fromExtensions(tbsCrList.getExtensions());

        ContentVerifier verifier;

        try
        {
            verifier = verifierProvider.get(AlgorithmIdentifier.getInstance(altSigAlg.toASN1Primitive()));

            OutputStream sOut = verifier.getOutputStream();

            ASN1Sequence tbsSeq = ASN1Sequence.getInstance(tbsCrList.toASN1Primitive());
            ASN1EncodableVector v = new ASN1EncodableVector();

            int start = 1;    //  want to skip signature field
            if (tbsSeq.getObjectAt(0) instanceof ASN1Integer)
            {
                v.add(tbsSeq.getObjectAt(0));
                start++;
            }

            for (int i = start; i != tbsSeq.size() - 1; i++)
            {
                v.add(tbsSeq.getObjectAt(i));
            }
            
            v.add(CertUtils.trimExtensions(0, tbsCrList.getExtensions()));

            new DERSequence(v).encodeTo(sOut, ASN1Encoding.DER);

            sOut.close();
        }
        catch (Exception e)
        {
            throw new CertException("unable to process signature: " + e.getMessage(), e);
        }

        return verifier.verify(altSigValue.getSignature().getOctets());
    }


    public boolean equals(
        Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (!(o instanceof X509CRLHolder))
        {
            return false;
        }

        X509CRLHolder other = (X509CRLHolder)o;

        return this.x509CRL.equals(other.x509CRL);
    }

    public int hashCode()
    {
        return this.x509CRL.hashCode();
    }

    private void readObject(
        ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        init(CertificateList.getInstance(in.readObject()));
    }

    private void writeObject(
        ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();

        out.writeObject(this.getEncoded());
    }
}

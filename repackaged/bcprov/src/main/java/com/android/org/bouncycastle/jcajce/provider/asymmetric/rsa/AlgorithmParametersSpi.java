/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.org.bouncycastle.jcajce.provider.asymmetric.rsa;

import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import com.android.org.bouncycastle.asn1.ASN1Encoding;
import com.android.org.bouncycastle.asn1.ASN1Integer;
import com.android.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.android.org.bouncycastle.asn1.ASN1OctetString;
import com.android.org.bouncycastle.asn1.DERNull;
import com.android.org.bouncycastle.asn1.DEROctetString;
import com.android.org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import com.android.org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import com.android.org.bouncycastle.asn1.pkcs.RSAESOAEPparams;
import com.android.org.bouncycastle.asn1.pkcs.RSASSAPSSparams;
import com.android.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.android.org.bouncycastle.jcajce.provider.util.DigestFactory;
import com.android.org.bouncycastle.jcajce.util.MessageDigestUtils;

/**
 * @hide This class is not part of the Android public SDK API
 */
public abstract class AlgorithmParametersSpi
    extends java.security.AlgorithmParametersSpi
{
    protected boolean isASN1FormatString(String format)
    {
        return format == null || format.equals("ASN.1");
    }

    protected AlgorithmParameterSpec engineGetParameterSpec(
        Class paramSpec)
        throws InvalidParameterSpecException
    {
        if (paramSpec == null)
        {
            throw new NullPointerException("argument to getParameterSpec must not be null");
        }

        return localEngineGetParameterSpec(paramSpec);
    }

    protected abstract AlgorithmParameterSpec localEngineGetParameterSpec(Class paramSpec)
        throws InvalidParameterSpecException;

    /**
     * @hide This class is not part of the Android public SDK API
     */
    public static class OAEP
        extends AlgorithmParametersSpi
    {
        OAEPParameterSpec currentSpec;
    
        /**
         * Return the PKCS#1 ASN.1 structure RSAES-OAEP-params.
         */
        protected byte[] engineGetEncoded() 
        {
            AlgorithmIdentifier hashAlgorithm = new AlgorithmIdentifier(
                                                            DigestFactory.getOID(currentSpec.getDigestAlgorithm()),
                                                            DERNull.INSTANCE);
            MGF1ParameterSpec mgfSpec = (MGF1ParameterSpec)currentSpec.getMGFParameters();
            AlgorithmIdentifier maskGenAlgorithm = new AlgorithmIdentifier(
                                                            PKCSObjectIdentifiers.id_mgf1,
                                                            new AlgorithmIdentifier(DigestFactory.getOID(mgfSpec.getDigestAlgorithm()), DERNull.INSTANCE));
            PSource.PSpecified      pSource = (PSource.PSpecified)currentSpec.getPSource();
            AlgorithmIdentifier pSourceAlgorithm = new AlgorithmIdentifier(
                                                            PKCSObjectIdentifiers.id_pSpecified, new DEROctetString(pSource.getValue()));
            RSAESOAEPparams oaepP = new RSAESOAEPparams(hashAlgorithm, maskGenAlgorithm, pSourceAlgorithm);
    
            try
            {
                return oaepP.getEncoded(ASN1Encoding.DER);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Error encoding OAEPParameters");
            }
        }
    
        protected byte[] engineGetEncoded(
            String format)
        {
            if (isASN1FormatString(format) || format.equalsIgnoreCase("X.509"))
            {
                return engineGetEncoded();
            }
    
            return null;
        }
    
        protected AlgorithmParameterSpec localEngineGetParameterSpec(
            Class paramSpec)
            throws InvalidParameterSpecException
        {
            if (paramSpec == OAEPParameterSpec.class || paramSpec == AlgorithmParameterSpec.class)
            {
                return currentSpec;
            }

            throw new InvalidParameterSpecException("unknown parameter spec passed to OAEP parameters object.");
        }
    
        protected void engineInit(
            AlgorithmParameterSpec paramSpec)
            throws InvalidParameterSpecException
        {
            if (!(paramSpec instanceof OAEPParameterSpec))
            {
                throw new InvalidParameterSpecException("OAEPParameterSpec required to initialise an OAEP algorithm parameters object");
            }
    
            this.currentSpec = (OAEPParameterSpec)paramSpec;
        }
    
        protected void engineInit(
            byte[] params) 
            throws IOException
        {
            try
            {
                RSAESOAEPparams oaepP = RSAESOAEPparams.getInstance(params);

                if (!oaepP.getMaskGenAlgorithm().getAlgorithm().equals(PKCSObjectIdentifiers.id_mgf1))
                {
                    throw new IOException("unknown mask generation function: " + oaepP.getMaskGenAlgorithm().getAlgorithm());
                }

                currentSpec = new OAEPParameterSpec(
                                       MessageDigestUtils.getDigestName(oaepP.getHashAlgorithm().getAlgorithm()),
                                       OAEPParameterSpec.DEFAULT.getMGFAlgorithm(),
                                       new MGF1ParameterSpec(MessageDigestUtils.getDigestName(AlgorithmIdentifier.getInstance(oaepP.getMaskGenAlgorithm().getParameters()).getAlgorithm())),
                                       new PSource.PSpecified(ASN1OctetString.getInstance(oaepP.getPSourceAlgorithm().getParameters()).getOctets()));
            }
            catch (ClassCastException e)
            {
                throw new IOException("Not a valid OAEP Parameter encoding.");
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                throw new IOException("Not a valid OAEP Parameter encoding.");
            }
        }

        protected void engineInit(
            byte[] params,
            String format)
            throws IOException
        {
            if (format.equalsIgnoreCase("X.509")
                    || format.equalsIgnoreCase("ASN.1"))
            {
                engineInit(params);
            }
            else
            {
                throw new IOException("Unknown parameter format " + format);
            }
        }
    
        protected String engineToString()
        {
            return "OAEP Parameters";
        }
    }
    
    /**
     * @hide This class is not part of the Android public SDK API
     */
    public static class PSS
        extends AlgorithmParametersSpi
    {  
        PSSParameterSpec currentSpec;
    
        /**
         * Return the PKCS#1 ASN.1 structure RSASSA-PSS-params.
         */
        protected byte[] engineGetEncoded() 
            throws IOException
        {
            PSSParameterSpec pssSpec = currentSpec;
            ASN1ObjectIdentifier digOid = DigestFactory.getOID(pssSpec.getDigestAlgorithm());
            AlgorithmIdentifier hashAlgorithm;
            // RFC 8072
            if (NISTObjectIdentifiers.id_shake128.equals(digOid) || NISTObjectIdentifiers.id_shake256.equals(digOid))
            {
                hashAlgorithm = new AlgorithmIdentifier(digOid);
            }
            else
            {
                hashAlgorithm = new AlgorithmIdentifier(digOid, DERNull.INSTANCE);
            }
            
            MGF1ParameterSpec mgfSpec = (MGF1ParameterSpec)pssSpec.getMGFParameters();
            if (mgfSpec != null)
            {
                AlgorithmIdentifier maskGenAlgorithm = new AlgorithmIdentifier(
                    PKCSObjectIdentifiers.id_mgf1,
                    new AlgorithmIdentifier(DigestFactory.getOID(mgfSpec.getDigestAlgorithm()), DERNull.INSTANCE));
                RSASSAPSSparams pssP = new RSASSAPSSparams(hashAlgorithm, maskGenAlgorithm, new ASN1Integer(pssSpec.getSaltLength()), new ASN1Integer(pssSpec.getTrailerField()));

                return pssP.getEncoded("DER");
            }
            else
            {
                AlgorithmIdentifier maskGenAlgorithm = new AlgorithmIdentifier(
                    pssSpec.getMGFAlgorithm().equals("SHAKE128") ? NISTObjectIdentifiers.id_shake128 : NISTObjectIdentifiers.id_shake256);
                RSASSAPSSparams pssP = new RSASSAPSSparams(hashAlgorithm, maskGenAlgorithm, new ASN1Integer(pssSpec.getSaltLength()), new ASN1Integer(pssSpec.getTrailerField()));

                return pssP.getEncoded("DER");
            }
        }
    
        protected byte[] engineGetEncoded(
            String format)
            throws IOException
        {
            if (format.equalsIgnoreCase("X.509")
                    || format.equalsIgnoreCase("ASN.1"))
            {
                return engineGetEncoded();
            }
    
            return null;
        }
    
        protected AlgorithmParameterSpec localEngineGetParameterSpec(
            Class paramSpec)
            throws InvalidParameterSpecException
        {
            if (paramSpec == PSSParameterSpec.class || paramSpec == AlgorithmParameterSpec.class)
            {
                return currentSpec;
            }

            throw new InvalidParameterSpecException("unknown parameter spec passed to PSS parameters object.");
        }
    
        protected void engineInit(
            AlgorithmParameterSpec paramSpec)
            throws InvalidParameterSpecException
        {
            if (!(paramSpec instanceof PSSParameterSpec))
            {
                throw new InvalidParameterSpecException("PSSParameterSpec required to initialise an PSS algorithm parameters object");
            }
    
            this.currentSpec = (PSSParameterSpec)paramSpec;
        }
    
        protected void engineInit(
            byte[] params) 
            throws IOException
        {
            try
            {
                RSASSAPSSparams pssP = RSASSAPSSparams.getInstance(params);

                ASN1ObjectIdentifier mgfOid = pssP.getMaskGenAlgorithm().getAlgorithm();
                if (mgfOid.equals(PKCSObjectIdentifiers.id_mgf1))
                {
                    currentSpec = new PSSParameterSpec(
                        MessageDigestUtils.getDigestName(pssP.getHashAlgorithm().getAlgorithm()),
                        PSSParameterSpec.DEFAULT.getMGFAlgorithm(),
                        new MGF1ParameterSpec(MessageDigestUtils.getDigestName(AlgorithmIdentifier.getInstance(pssP.getMaskGenAlgorithm().getParameters()).getAlgorithm())),
                        pssP.getSaltLength().intValue(),
                        pssP.getTrailerField().intValue());
                }
                else if (mgfOid.equals(NISTObjectIdentifiers.id_shake128) || mgfOid.equals(NISTObjectIdentifiers.id_shake256))
                {
                    currentSpec = new PSSParameterSpec(
                        MessageDigestUtils.getDigestName(pssP.getHashAlgorithm().getAlgorithm()),
                        mgfOid.equals(NISTObjectIdentifiers.id_shake128) ? "SHAKE128" : "SHAKE256",
                        null,
                        pssP.getSaltLength().intValue(),
                        pssP.getTrailerField().intValue());
                }
                else
                {
                    throw new IOException("unknown mask generation function: " + pssP.getMaskGenAlgorithm().getAlgorithm());
                }
            }
            catch (ClassCastException e)
            {
                throw new IOException("Not a valid PSS Parameter encoding.");
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                throw new IOException("Not a valid PSS Parameter encoding.");
            }
        }
    
        protected void engineInit(
            byte[] params,
            String format)
            throws IOException
        {
            if (isASN1FormatString(format) || format.equalsIgnoreCase("X.509"))
            {
                engineInit(params);
            }
            else
            {
                throw new IOException("Unknown parameter format " + format);
            }
        }
    
        protected String engineToString()
        {
            return "PSS Parameters";
        }
    }
}

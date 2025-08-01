/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.internal.org.bouncycastle.jcajce.provider.config;

import java.util.Map;

import com.android.internal.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.android.internal.org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;

/**
 * Implemented by the BC provider. This allows setting of hidden parameters,
 * such as the ImplicitCA parameters from X.962, if used.
 * @hide This class is not part of the Android public SDK API
 */
public interface ConfigurableProvider
{
    /**
     * Elliptic Curve CA parameters - thread local version
     */
    static final String THREAD_LOCAL_EC_IMPLICITLY_CA = "threadLocalEcImplicitlyCa";

    /**
     * Elliptic Curve CA parameters - VM wide version
     */
    static final String EC_IMPLICITLY_CA = "ecImplicitlyCa";

    /**
     * Diffie-Hellman Default Parameters - thread local version
     */
    static final String THREAD_LOCAL_DH_DEFAULT_PARAMS = "threadLocalDhDefaultParams";

    /**
     * Diffie-Hellman Default Parameters - VM wide version
     */
    static final String DH_DEFAULT_PARAMS = "DhDefaultParams";

    /**
     * A set of OBJECT IDENTIFIERs representing acceptable named curves for imported keys.
     */
    static final String ACCEPTABLE_EC_CURVES = "acceptableEcCurves";

    /**
     * A set of OBJECT IDENTIFIERs to EC Curves providing local curve name mapping.
     */
    static final String ADDITIONAL_EC_PARAMETERS = "additionalEcParameters";

    void setParameter(String parameterName, Object parameter);

    void addAlgorithm(String key, String value);

    void addAlgorithm(String key, String value, Map<String, String> attributes);

    void addAlgorithm(String type, ASN1ObjectIdentifier oid, String className);

    void addAlgorithm(String type, ASN1ObjectIdentifier oid, String className, Map<String, String> attributes);

    boolean hasAlgorithm(String type, String name);

    void addKeyInfoConverter(ASN1ObjectIdentifier oid, AsymmetricKeyInfoConverter keyInfoConverter);

    AsymmetricKeyInfoConverter getKeyInfoConverter(ASN1ObjectIdentifier oid);

    void addAttributes(String key, Map<String, String> attributeMap);

    // BEGIN Android-added: Allow algorithms to be added privately.
    // See BouncyCastleProvider for details.
    void addPrivateAlgorithm(String key, String value);

    void addPrivateAlgorithm(String type, ASN1ObjectIdentifier oid, String className);
    // END Android-added: Allow algorithms to be added privately.
}

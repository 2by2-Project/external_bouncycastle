/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.internal.org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;

import com.android.internal.org.bouncycastle.util.io.Streams;

/**
 * A parser for indefinite-length OCTET STRINGs.
 * 
 * @deprecated Check for 'ASN1OctetStringParser' instead 
 * @hide This class is not part of the Android public SDK API
 */
public class BEROctetStringParser
    implements ASN1OctetStringParser
{
    private ASN1StreamParser _parser;

    BEROctetStringParser(
        ASN1StreamParser parser)
    {
        _parser = parser;
    }

    /**
     * Return an InputStream representing the contents of the OCTET STRING.
     *
     * @return an InputStream with its source as the OCTET STRING content.
     */
    public InputStream getOctetStream()
    {
        return new ConstructedOctetStream(_parser);
    }

    /**
     * Return an in-memory, encodable, representation of the OCTET STRING.
     *
     * @return a BEROctetString.
     * @throws IOException if there is an issue loading the data.
     */
    public ASN1Primitive getLoadedObject()
        throws IOException
    {
        return parse(_parser);
    }

    /**
     * Return an BEROctetString representing this parser and its contents.
     *
     * @return an BEROctetString
     */
    public ASN1Primitive toASN1Primitive()
    {
        try
        {
            return getLoadedObject();
        }
        catch (IOException e)
        {
            throw new ASN1ParsingException("IOException converting stream to byte array: " + e.getMessage(), e);
        }
    }

    static BEROctetString parse(ASN1StreamParser sp) throws IOException
    {
        return new BEROctetString(Streams.readAll(new ConstructedOctetStream(sp)));
    }
}

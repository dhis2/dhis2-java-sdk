package org.hisp.dhis.integration.sdk.internal.operation;

import okhttp3.Request;
import org.hisp.dhis.integration.sdk.api.Dhis2Response;
import org.hisp.dhis.integration.sdk.api.operation.ResourceOperation;
import org.hisp.dhis.integration.sdk.internal.converter.JacksonConverterFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractResourceOperationTestCase
{
    @Test
    public void testTransferGivenStringResource()
    {

        CountDownLatch countDownLatch = new CountDownLatch( 1 );
        ResourceOperation resourceOperation = new AbstractResourceOperation( "http://example", "", null,
            new JacksonConverterFactory() )
        {
            @Override
            protected Dhis2Response doResourceTransfer( byte[] resourceAsBytes,
                Request.Builder requestBuilder )
            {
                assertEquals( "{\"foo\":\"acme\"}", new String( resourceAsBytes ) );
                countDownLatch.countDown();
                return null;
            }
        }.withResource( "{\"foo\":\"acme\"}" );
        resourceOperation.transfer();
        assertEquals( 0, countDownLatch.getCount() );
    }

    @Test
    public void testTransferGivenNonStringResource()
    {

        CountDownLatch countDownLatch = new CountDownLatch( 1 );
        ResourceOperation resourceOperation = new AbstractResourceOperation( "http://example", "", null,
            new JacksonConverterFactory() )
        {
            @Override
            protected Dhis2Response doResourceTransfer( byte[] resourceAsBytes,
                Request.Builder requestBuilder )
            {
                assertEquals( "{\"foo\":\"acme\"}", new String( resourceAsBytes ) );
                countDownLatch.countDown();
                return null;
            }
        }.withResource( Map.of( "foo", "acme" ) );
        resourceOperation.transfer();
        assertEquals( 0, countDownLatch.getCount() );
    }
}

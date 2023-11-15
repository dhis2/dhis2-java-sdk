/*
 * Copyright (c) 2004-2022, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.integration.sdk.internal.operation;

import okhttp3.HttpUrl;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.hisp.dhis.integration.sdk.api.RemoteDhis2ClientException;
import org.hisp.dhis.integration.sdk.internal.converter.JacksonConverterFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractOperationTestCase
{
    @Test
    public void testDoTransferThrowsExceptionWhenResponseIsNotSuccessful()
        throws
        IOException
    {
        Response responseMock = mock( Response.class );
        ResponseBody responseBodyMock = mock( ResponseBody.class );

        when( responseBodyMock.string() ).thenReturn( "body" );
        when( responseMock.isSuccessful() ).thenReturn( false );
        when( responseMock.toString() ).thenReturn( "toString" );
        when( responseMock.code() ).thenReturn( 1 );
        when( responseMock.body() ).thenReturn( responseBodyMock );

        AbstractOperation<?> resourceOperation = new AbstractOperation<Object>( "http://example", "", null,
            new JacksonConverterFactory() )
        {
            @Override
            protected Object doTransfer( HttpUrl httpUrl )
            {
                return onHttpResponse( () -> responseMock );
            }

        };

        RemoteDhis2ClientException remoteDhis2ClientException = assertThrows( RemoteDhis2ClientException.class, () -> {
            resourceOperation.doTransfer( null );
        } );

        assertEquals( 1, remoteDhis2ClientException.getHttpStatusCode() );
        assertEquals( "toString", remoteDhis2ClientException.getMessage() );
        assertEquals( "body", remoteDhis2ClientException.getBody() );
    }
}

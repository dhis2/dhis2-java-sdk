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

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.hisp.dhis.integration.sdk.api.operation.GetOperation;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultGetOperationTestCase
{
    @Test
    public void testTransferGivenFields()
        throws
        IOException
    {
        OkHttpClient okHttpClientMock = mock( OkHttpClient.class );
        Call callMock = mock( Call.class );
        Response responseMock = mock( Response.class );

        ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass( Request.class );
        when( callMock.execute() ).thenReturn( responseMock );
        when( okHttpClientMock.newCall( requestArgumentCaptor.capture() ) ).thenReturn( callMock );
        when( responseMock.isSuccessful() ).thenReturn( true );

        GetOperation getOperation = new DefaultGetOperation( "https://play.dhis2.org/2.38.0/api", "", okHttpClientMock,
            null ).withFields( "id" );
        getOperation.transfer();
        assertEquals( "fields=id", requestArgumentCaptor.getValue().url().url().getQuery() );
    }

    @Test
    public void testTransferGivenOrRootJunction()
        throws
        IOException
    {
        OkHttpClient okHttpClientMock = mock( OkHttpClient.class );
        Call callMock = mock( Call.class );
        Response responseMock = mock( Response.class );

        ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass( Request.class );
        when( callMock.execute() ).thenReturn( responseMock );
        when( okHttpClientMock.newCall( requestArgumentCaptor.capture() ) ).thenReturn( callMock );
        when( responseMock.isSuccessful() ).thenReturn( true );

        GetOperation getOperation = new DefaultGetOperation( "https://play.dhis2.org/2.38.0/api", "", okHttpClientMock,
            null ).withOrRootJunction();
        getOperation.transfer();
        assertEquals( "rootJunction=OR", requestArgumentCaptor.getValue().url().url().getQuery() );
    }

    @Test
    public void testTransferGivenAndRootJunction()
        throws
        IOException
    {
        OkHttpClient okHttpClientMock = mock( OkHttpClient.class );
        Call callMock = mock( Call.class );
        Response responseMock = mock( Response.class );

        ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass( Request.class );
        when( callMock.execute() ).thenReturn( responseMock );
        when( okHttpClientMock.newCall( requestArgumentCaptor.capture() ) ).thenReturn( callMock );
        when( responseMock.isSuccessful() ).thenReturn( true );

        GetOperation getOperation = new DefaultGetOperation( "https://play.dhis2.org/2.38.0/api", "", okHttpClientMock,
            null ).withAndRootJunction();
        getOperation.transfer();
        assertEquals( "rootJunction=AND", requestArgumentCaptor.getValue().url().url().getQuery() );
    }
}

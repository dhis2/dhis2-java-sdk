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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.hisp.dhis.integration.sdk.api.Dhis2Response;
import org.hisp.dhis.integration.sdk.api.converter.ConverterFactory;
import org.hisp.dhis.integration.sdk.api.operation.ResourceOperation;

public abstract class AbstractResourceOperation extends AbstractOperation implements ResourceOperation
{
    protected Object resource;

    public AbstractResourceOperation( String baseApiUrl, String path, OkHttpClient httpClient,
        ConverterFactory converterFactory, String... pathParams )
    {
        super( baseApiUrl, path, httpClient, converterFactory, pathParams );
    }

    @Override
    public Dhis2Response doTransfer( HttpUrl httpUrl ) {
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
        Request.Builder requestBuilder = new Request.Builder().url( httpUrlBuilder.build() )
            .addHeader( "Content-Type", "application/json" );
        final byte[] bytes;
        if ( resource != null )
        {
            if ( resource instanceof String )
            {
                bytes = ((String) resource).getBytes();
            }
            else
            {
                bytes = converterFactory.createRequestConverter( requestBuilder.build() ).convert( resource )
                    .getBytes();
            }
        }
        else
        {
            bytes = new byte[] {};
        }

        return doResourceTransfer(bytes, requestBuilder);
    }

    protected abstract Dhis2Response doResourceTransfer( byte[] resourceAsBytes, Request.Builder requestBuilder );

    @Override
    public ResourceOperation withResource( Object resource )
    {
        this.resource = resource;
        return this;
    }
}

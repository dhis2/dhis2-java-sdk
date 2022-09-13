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
package org.hisp.dhis.integration.sdk;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import org.hisp.dhis.integration.sdk.api.Dhis2Client;
import org.hisp.dhis.integration.sdk.api.converter.ConverterFactory;
import org.hisp.dhis.integration.sdk.api.operation.DeleteOperation;
import org.hisp.dhis.integration.sdk.api.operation.GetOperation;
import org.hisp.dhis.integration.sdk.api.operation.PatchOperation;
import org.hisp.dhis.integration.sdk.api.operation.PostOperation;
import org.hisp.dhis.integration.sdk.api.operation.PutOperation;
import org.hisp.dhis.integration.sdk.api.security.SecurityContext;
import org.hisp.dhis.integration.sdk.internal.operation.DefaultDeleteOperation;
import org.hisp.dhis.integration.sdk.internal.operation.DefaultGetOperation;
import org.hisp.dhis.integration.sdk.internal.operation.DefaultPostOperation;
import org.hisp.dhis.integration.sdk.internal.operation.DefaultPutOperation;

public class DefaultDhis2Client implements Dhis2Client
{
    private final OkHttpClient httpClient;

    private final String apiUrl;

    private final ConverterFactory converterFactory;

    DefaultDhis2Client( String apiUrl, SecurityContext securityContext, ConverterFactory converterFactory,
        int maxIdleConnections, long keepAliveDuration )
    {
        this.apiUrl = apiUrl;
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
            .connectionPool( new ConnectionPool( maxIdleConnections, keepAliveDuration, TimeUnit.MILLISECONDS ) );
        httpClient = httpClientBuilder.addInterceptor(
            chain -> chain.proceed( securityContext.apply( chain.request() ) ) ).build();
        this.converterFactory = converterFactory;
    }

    @Override
    public PostOperation post( String path, String... pathParams )
    {
        return new DefaultPostOperation( apiUrl, path, httpClient, converterFactory, pathParams );
    }

    @Override
    public PutOperation put( String path, String... pathParams )
    {
        return new DefaultPutOperation( apiUrl, path, httpClient, converterFactory, pathParams );
    }

    @Override
    public GetOperation get( String path, String... pathParams )
    {
        return new DefaultGetOperation( apiUrl, path, httpClient, converterFactory, pathParams );
    }

    @Override
    public PatchOperation patch( String path, String... pathParams )
    {
        throw new UnsupportedOperationException();
        // return new DefaultPatchOperation( apiUrl + path, httpClient,
        // converterFactory );
    }

    @Override
    public DeleteOperation delete( String path, String... pathParams )
    {
        return new DefaultDeleteOperation( apiUrl, path, httpClient, converterFactory, pathParams );
    }

    @Override
    public OkHttpClient getHttpClient()
    {
        return httpClient;
    }
}

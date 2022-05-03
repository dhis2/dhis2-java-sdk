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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import org.hisp.dhis.integration.sdk.api.Dhis2Response;
import org.hisp.dhis.integration.sdk.api.converter.ConverterFactory;
import org.hisp.dhis.integration.sdk.api.operation.GetOperation;
import org.hisp.dhis.integration.sdk.api.operation.PagingCollectOperation;
import org.hisp.dhis.integration.sdk.api.operation.SimpleCollectOperation;
import org.hisp.dhis.integration.sdk.internal.DefaultDhis2Response;
import org.hisp.dhis.integration.sdk.internal.operation.page.DefaultPagingCollectOperation;

public class DefaultGetOperation extends AbstractOperation implements GetOperation
{
    protected final List<String> fields = new ArrayList<>();

    protected String filter;

    public DefaultGetOperation( String baseApiUrl, String path, OkHttpClient httpClient,
        ConverterFactory converterFactory, String... pathParams )
    {
        super( baseApiUrl, path, httpClient, converterFactory, pathParams );
    }

    @Override
    public Dhis2Response doTransfer( HttpUrl httpUrl )
    {
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
        if ( !fields.isEmpty() )
        {
            httpUrlBuilder.addQueryParameter( "fields", String.join( ",", fields ) );
        }
        if ( filter != null )
        {
            httpUrlBuilder.addQueryParameter( "filter", filter );
        }

        okhttp3.Response response = onHttpResponse(
            () -> httpClient.newCall( new Request.Builder().url( httpUrlBuilder.build() ).get().build() ).execute() );
        return new DefaultDhis2Response( response, converterFactory );
    }

    @Override
    public PagingCollectOperation withPaging()
    {
        return new DefaultPagingCollectOperation( converterFactory, this, httpClient );
    }

    @Override
    public SimpleCollectOperation withoutPaging()
    {
        return new DefaultSimpleCollectOperation( converterFactory, this );
    }

    @Override
    public GetOperation withFields( String... names )
    {
        fields.addAll( Arrays.asList( names ) );
        return this;
    }

    @Override
    public GetOperation withFields( String names )
    {
        fields.add( names );
        return this;
    }

    @Override
    public GetOperation withFilter( String filter )
    {
        this.filter = filter;
        return this;
    }
}

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import org.hisp.dhis.integration.sdk.api.Dhis2ClientException;
import org.hisp.dhis.integration.sdk.api.Dhis2Response;
import org.hisp.dhis.integration.sdk.api.converter.ConverterFactory;
import org.hisp.dhis.integration.sdk.api.operation.ParameterizedOperation;

public abstract class AbstractOperation implements ParameterizedOperation
{
    protected final String url;

    protected final OkHttpClient httpClient;

    protected final ConverterFactory converterFactory;

    protected final Map<String, List<String>> queryParams = new HashMap<>();

    private final String[] pathParams;

    public AbstractOperation( String baseApiUrl, String path, OkHttpClient httpClient,
        ConverterFactory converterFactory, String... pathParams )
    {
        this.url = (baseApiUrl.endsWith( "/" ) ? baseApiUrl : baseApiUrl + "/")
            + (path != null && path.startsWith( "/" ) ? path.substring( 1 ) : path);
        this.httpClient = httpClient;
        this.converterFactory = converterFactory;
        this.pathParams = pathParams;
    }

    @Override
    public Dhis2Response transfer()
    {
        HttpUrl httpUrl = HttpUrl.parse( url );
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
        int pathParamIndex = 0;
        for ( int i = 0; i < httpUrl.pathSegments().size(); i++ )
        {
            String pathSegment = httpUrl.pathSegments().get( i );
            if ( pathSegment.startsWith( "{" ) && pathSegment.endsWith( "}" ) )
            {
                httpUrlBuilder.setPathSegment( i, pathParams[pathParamIndex] );
                pathParamIndex++;
            }
        }

        for ( Map.Entry<String, List<String>> queryParam : queryParams.entrySet() )
        {
            for ( String queryValue : queryParam.getValue() )
            {
                httpUrlBuilder.addQueryParameter( queryParam.getKey(), queryValue );

            }
        }

        return doTransfer( httpUrlBuilder.build() );
    }

    public abstract Dhis2Response doTransfer( HttpUrl httpUrl );

    @Override
    public ParameterizedOperation withParameters( Map<String, String> parameters )
    {
        return this;
    }

    @Override
    public ParameterizedOperation withParameter( String name, String value )
    {
        List<String> values = queryParams.computeIfAbsent( name, k -> new ArrayList<>() );
        values.add( value );

        return this;
    }

    protected Response onHttpResponse( Callable<Response> callable )
    {
        Response response = null;
        try
        {
            response = callable.call();
            if ( !response.isSuccessful() )
            {
                response.close();
                throw new Dhis2ClientException( response.toString() );
            }
        }
        catch ( Exception e )
        {
            if ( response != null )
            {
                response.close();
            }
            throw new Dhis2ClientException( e );
        }

        return response;
    }
}

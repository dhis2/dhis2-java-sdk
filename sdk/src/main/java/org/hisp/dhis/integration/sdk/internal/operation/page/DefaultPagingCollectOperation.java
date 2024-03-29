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
package org.hisp.dhis.integration.sdk.internal.operation.page;

import okhttp3.OkHttpClient;

import org.hisp.dhis.integration.sdk.api.Dhis2Response;
import org.hisp.dhis.integration.sdk.api.converter.ConverterFactory;
import org.hisp.dhis.integration.sdk.api.operation.PagingCollectOperation;
import org.hisp.dhis.integration.sdk.api.operation.ParameterizedOperation;
import org.hisp.dhis.integration.sdk.internal.LazyIterableDhis2Response;
import org.hisp.dhis.integration.sdk.internal.operation.AbstractOperation;

import java.util.Map;

public class DefaultPagingCollectOperation extends AbstractOperation<LazyIterableDhis2Response>
    implements PagingCollectOperation
{
    private final ParameterizedOperation<Dhis2Response> parameterizedOperation;

    public DefaultPagingCollectOperation( String baseApiUrl, String path, OkHttpClient httpClient,
        ConverterFactory converterFactory, ParameterizedOperation<Dhis2Response> parameterizedOperation,
        String... pathParams )
    {
        super( baseApiUrl, path, httpClient, converterFactory, pathParams );
        this.parameterizedOperation = parameterizedOperation;
    }

    @Override
    public LazyIterableDhis2Response transfer()
    {
        final Dhis2Response dhis2Response = parameterizedOperation.transfer();
        return new LazyIterableDhis2Response( dhis2Response, converterFactory, httpClient );
    }

    @Override
    public ParameterizedOperation<LazyIterableDhis2Response> withParameters( Map<String, String> parameters )
    {
        parameterizedOperation.withParameters( parameters );
        return this;
    }

    @Override
    public ParameterizedOperation<LazyIterableDhis2Response> withParameter( String name, String value )
    {
        parameterizedOperation.withParameter( name, value );
        return this;
    }
}

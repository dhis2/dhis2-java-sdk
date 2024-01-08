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
package org.hisp.dhis.integration.sdk.internal;

import java.io.InputStream;

import okhttp3.Response;

import org.hisp.dhis.integration.sdk.api.Dhis2Response;
import org.hisp.dhis.integration.sdk.api.converter.ConverterFactory;

public class DefaultDhis2Response implements Dhis2Response
{
    protected final Response response;

    protected final ConverterFactory converterFactory;

    public DefaultDhis2Response( Response response, ConverterFactory converterFactory )
    {
        this.response = response;
        this.converterFactory = converterFactory;
    }

    @Override
    public <T> T returnAs( Class<T> responseType )
    {
        return converterFactory.createResponseConverter( responseType ).convert( response.body() );
    }

    @Override
    public InputStream read()
    {
        return response.body().byteStream();
    }

    @Override
    public String getUrl()
    {
        return response.request().url().toString();
    }

    public Response getResponse()
    {
        return response;
    }

    public ConverterFactory getConverterFactory()
    {
        return converterFactory;
    }

    @Override
    public void close()
    {
        response.close();
    }
}

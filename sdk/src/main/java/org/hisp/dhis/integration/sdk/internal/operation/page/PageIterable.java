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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import org.hisp.dhis.api.model.Page;
import org.hisp.dhis.integration.sdk.api.Dhis2ClientException;
import org.hisp.dhis.integration.sdk.api.Dhis2Response;
import org.hisp.dhis.integration.sdk.api.converter.ConverterFactory;
import org.hisp.dhis.integration.sdk.internal.DefaultDhis2Response;

public class PageIterable<T> implements Iterable<T>
{
    private Dhis2Response dhis2Response;

    private Page currentPage;

    private Iterator<T> currentIterator;

    private final ConverterFactory converterFactory;

    private final String collectionName;

    private final Class<T> responseType;

    private final OkHttpClient httpClient;

    public PageIterable( String collectionName, ConverterFactory converterFactory,
        OkHttpClient httpClient, Class<T> responseType, Page firstPage )
    {
        List<T> collection = (List<T>) converterFactory.createResponseConverter( responseType )
            .convert( (List<Map<String, Object>>) firstPage.getAdditionalProperties().get( collectionName ) );
        this.currentIterator = collection.iterator();

        this.currentPage = firstPage;
        this.collectionName = collectionName;
        this.converterFactory = converterFactory;
        this.httpClient = httpClient;
        this.responseType = responseType;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            @Override
            public boolean hasNext()
            {
                if ( currentIterator.hasNext() )
                {
                    return true;
                }
                else if ( currentPage.getPager().getPage() >= currentPage.getPager().getPageCount() )
                {
                    return false;
                }
                else
                {
                    currentIterator = fetchPage();
                    return currentIterator.hasNext();
                }
            }

            @Override
            public T next()
            {
                if ( !currentIterator.hasNext() )
                {
                    currentIterator = fetchPage();
                }
                return currentIterator.next();
            }

            private Iterator<T> fetchPage()
            {
                try
                {
                    dhis2Response = new DefaultDhis2Response( httpClient.newCall(
                        new Request.Builder().url( currentPage.getPager().getNextPage() ).build() ).execute(),
                        converterFactory );
                }
                catch ( IOException e )
                {
                    throw new Dhis2ClientException( e );
                }

                currentPage = dhis2Response.returnAs( Page.class );
                List<T> collection = (List<T>) converterFactory.createResponseConverter( responseType )
                    .convert(
                        (List<Map<String, Object>>) currentPage.getAdditionalProperties().get( collectionName ) );
                return collection.iterator();
            }

        };
    }
}

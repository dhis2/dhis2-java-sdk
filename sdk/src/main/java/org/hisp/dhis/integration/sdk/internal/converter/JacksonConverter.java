/*
 * Copyright (c) 2004-2023, University of Oslo
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
package org.hisp.dhis.integration.sdk.internal.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hisp.dhis.integration.sdk.api.Dhis2ClientException;
import org.hisp.dhis.integration.sdk.api.converter.Converter;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class JacksonConverter implements Converter
{
    private final ObjectMapper objectMapper;

    public JacksonConverter( ObjectMapper objectMapper )
    {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert( Object from )
    {
        try
        {
            return objectMapper.writeValueAsString( from );
        }
        catch ( JsonProcessingException e )
        {
            throw new Dhis2ClientException( e );
        }
    }

    @Override
    public <T> T convert( Object from, Class<T> toType )
    {
        return objectMapper.convertValue( from, toType );
    }

    @Override
    public <T> T convert( Reader source, Class<T> toType )
    {
        try
        {
            return objectMapper.readValue( source, toType );
        }
        catch ( IOException e )
        {
            throw new Dhis2ClientException( e );
        }
    }

    @Override
    public <T> T convert( List<?> from, Class<T> toCollectionType, Class<?> toElementType )
    {
        return objectMapper.convertValue( from,
            objectMapper.getTypeFactory().constructCollectionLikeType( toCollectionType, toElementType ) );
    }

    public ObjectMapper getObjectMapper()
    {
        return objectMapper;
    }
}

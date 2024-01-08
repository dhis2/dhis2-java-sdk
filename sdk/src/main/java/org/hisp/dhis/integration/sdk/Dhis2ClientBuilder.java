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

import org.hisp.dhis.integration.sdk.api.Dhis2Client;
import org.hisp.dhis.integration.sdk.api.converter.ConverterFactory;
import org.hisp.dhis.integration.sdk.api.security.SecurityContext;
import org.hisp.dhis.integration.sdk.internal.converter.JacksonConverterFactory;
import org.hisp.dhis.integration.sdk.internal.security.BasicCredentialsSecurityContext;
import org.hisp.dhis.integration.sdk.internal.security.PersonalAccessTokenSecurityContext;

import java.util.concurrent.TimeUnit;

public class Dhis2ClientBuilder
{
    private final SecurityContext securityContext;

    private final String baseApiUrl;

    private int maxIdleConnections = 5;

    private long keepAliveDurationMs = 300000;

    private long callTimeoutMs = 0;

    private long readTimeoutMs = 10000;

    private long writeTimeoutMs = 10000;

    private long connectTimeoutMs = 10000;

    private ConverterFactory converterFactory = new JacksonConverterFactory();

    public static Dhis2ClientBuilder newClient( String baseApiUrl, String username, String password )
    {
        return new Dhis2ClientBuilder( baseApiUrl, new BasicCredentialsSecurityContext( username, password ) );
    }

    public static Dhis2ClientBuilder newClient( String baseApiUrl, String personalAccessToken )
    {
        return new Dhis2ClientBuilder( baseApiUrl, new PersonalAccessTokenSecurityContext( personalAccessToken ));
    }

    public static Dhis2ClientBuilder newClient( String baseApiUrl, SecurityContext securityContext )
    {
        return new Dhis2ClientBuilder( baseApiUrl, securityContext);
    }

    private Dhis2ClientBuilder( String baseApiUrl, SecurityContext securityContext )
    {
        this.baseApiUrl = baseApiUrl.trim();
        this.securityContext = securityContext;
    }

    public Dhis2ClientBuilder withMaxIdleConnections( int maxIdleConnections )
    {
        this.maxIdleConnections = maxIdleConnections;
        return this;
    }

    public Dhis2ClientBuilder withKeepAliveDuration( long keepAliveDuration, TimeUnit timeUnit )
    {
        this.keepAliveDurationMs = timeUnit.toMillis( keepAliveDuration );
        return this;
    }

    public Dhis2ClientBuilder withCallTimeout( long callTimeout, TimeUnit timeUnit )
    {
        this.callTimeoutMs = timeUnit.toMillis( callTimeout );
        return this;
    }

    public Dhis2ClientBuilder withReadTimeout( long readTimeout, TimeUnit timeUnit )
    {
        this.readTimeoutMs = timeUnit.toMillis( readTimeout );
        return this;
    }

    public Dhis2ClientBuilder withWriteTimeout( long writeTimeout, TimeUnit timeUnit )
    {
        this.writeTimeoutMs = timeUnit.toMillis( writeTimeout );
        return this;
    }

    public Dhis2ClientBuilder withConnectTimeout( long connectTimeout, TimeUnit timeUnit )
    {
        this.connectTimeoutMs = timeUnit.toMillis( connectTimeout );
        return this;
    }

    public Dhis2ClientBuilder withConverterFactory( ConverterFactory converterFactory )
    {
        this.converterFactory = converterFactory;
        return this;
    }

    public Dhis2Client build()
    {
        StringBuilder apiPathStringBuilder = new StringBuilder();
        apiPathStringBuilder.append( baseApiUrl );
        if ( !baseApiUrl.endsWith( "/" ) )
        {
            apiPathStringBuilder.append( "/" );
        }
        return new DefaultDhis2Client( apiPathStringBuilder.toString(), securityContext, converterFactory,
            maxIdleConnections, keepAliveDurationMs, callTimeoutMs, readTimeoutMs, writeTimeoutMs, connectTimeoutMs );
    }
}

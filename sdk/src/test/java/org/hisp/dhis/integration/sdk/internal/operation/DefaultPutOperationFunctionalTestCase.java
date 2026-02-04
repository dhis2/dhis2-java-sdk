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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hisp.dhis.api.model.v42_4.OrganisationUnit;
import org.hisp.dhis.integration.sdk.AbstractTestCase;
import org.hisp.dhis.integration.sdk.Environment;
import org.hisp.dhis.integration.sdk.api.Dhis2Response;
import org.hisp.dhis.integration.sdk.internal.DefaultDhis2Response;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

public class DefaultPutOperationFunctionalTestCase extends AbstractTestCase
{
    @Test
    public void testTransfer()
    {
        OrganisationUnit organisationUnit = dhis2Client.get( "organisationUnits/{id}", Environment.ORG_UNIT_ID )
            .transfer().returnAs( OrganisationUnit.class );
        organisationUnit.setAddress( "Alice in Wonderland" );

        Dhis2Response dhis2Response = new DefaultPutOperation( RestAssured.baseURI + "/api",
            "organisationUnits/{orgUnitId}",
            dhis2Client.getHttpClient(),
            converterFactory, Environment.ORG_UNIT_ID ).withResource( organisationUnit ).transfer();

        assertTrue( ((DefaultDhis2Response) dhis2Response).getResponse().isSuccessful() );
    }
}

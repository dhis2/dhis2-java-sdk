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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.hisp.dhis.api.model.v2_38_1.OrganisationUnit;
import org.hisp.dhis.integration.sdk.AbstractTestCase;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

public class SimpleCollectOperationTestCase extends AbstractTestCase
{
    @Test
    public void testTransfer()
    {
        Iterable<OrganisationUnit> organisationUnitIterable = new DefaultSimpleCollectOperation(
            RestAssured.baseURI + "/api", "me",
            dhis2Client.getHttpClient(), converterFactory,
            new DefaultGetOperation( RestAssured.baseURI + "/api/", "organisationUnits", dhis2Client.getHttpClient(),
                converterFactory )
        ).transfer().returnAs( OrganisationUnit.class, "organisationUnits" );

        List<OrganisationUnit> organisationAsUnits = StreamSupport
            .stream( organisationUnitIterable.spliterator(), false ).collect( Collectors.toList() );

        assertEquals( 1, organisationAsUnits.size() );
    }
}

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

import org.hisp.dhis.api.model.v40_2_2.AttributeInfo;
import org.hisp.dhis.api.model.v40_2_2.Body;
import org.hisp.dhis.api.model.v40_2_2.EnrollmentInfo;
import org.hisp.dhis.api.model.v40_2_2.ReservedValue;
import org.hisp.dhis.api.model.v40_2_2.TrackedEntityInfo;
import org.hisp.dhis.api.model.v40_2_2.TrackerImportReport;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.fail;

public class ReadMeExampleTestCase
{
    @Test
    public void testCreateATrackedEntityInstance()
    {
        String uniqueSystemIdentifier = Environment.getDhis2Client()
            .get( "trackedEntityAttributes/HlKXyR5qr2e/generate" ).transfer()
            .returnAs( ReservedValue.class )
            .getValue().get();

        TrackerImportReport trackerImportReport = Environment.getDhis2Client().post( "tracker" )
            .withResource( new Body().withTrackedEntities( Arrays.asList( new TrackedEntityInfo()
                .withOrgUnit( Environment.ORG_UNIT_ID )
                .withTrackedEntityType( "MCPQUTHX1Ze" )
                .withEnrollments( Arrays.asList( new EnrollmentInfo()
                    .withOrgUnit( Environment.ORG_UNIT_ID )
                    .withProgram( "w0qPtIW0JYu" )
                    .withEnrolledAt( new Date() )
                    .withOccurredAt( new Date() )
                    .withAttributes( Arrays.asList(
                        new AttributeInfo().withAttribute( "HlKXyR5qr2e" ).withValue( uniqueSystemIdentifier ),
                        new AttributeInfo().withAttribute( "oindugucx72" ).withValue( "Male" ),
                        new AttributeInfo().withAttribute( "NI0QRzJvQ0k" ).withValue( "2023-01-01" ) ) ) ) ) ) ) )
            .withParameter( "async", "false" )
            .transfer()
            .returnAs( TrackerImportReport.class );

        if ( !trackerImportReport.getStatus().equals( TrackerImportReport.StatusRef.OK ) )
        {
            fail();
        }
    }
}

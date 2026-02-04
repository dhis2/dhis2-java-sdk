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

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.api.model.v42_4.ApiToken;
import org.hisp.dhis.api.model.v42_4.Body;
import org.hisp.dhis.api.model.v42_4.OptionSet;
import org.hisp.dhis.api.model.v42_4.OrganisationUnit;
import org.hisp.dhis.api.model.v42_4.OrganisationUnitLevel;
import org.hisp.dhis.api.model.v42_4.ProgramParams;
import org.hisp.dhis.api.model.v42_4.ProgramStageParams;
import org.hisp.dhis.api.model.v42_4.ProgramTrackedEntityAttributeParams;
import org.hisp.dhis.api.model.v42_4.ReservedValue;
import org.hisp.dhis.api.model.v42_4.SourceRequest;
import org.hisp.dhis.api.model.v42_4.TrackedEntityAttribute;
import org.hisp.dhis.api.model.v42_4.TrackedEntityAttributeParams;
import org.hisp.dhis.api.model.v42_4.TrackedEntityType;
import org.hisp.dhis.api.model.v42_4.TrackedEntityTypeRef;
import org.hisp.dhis.api.model.v42_4.TrackerAttribute;
import org.hisp.dhis.api.model.v42_4.TrackerEnrollment;
import org.hisp.dhis.api.model.v42_4.TrackerImportReport;
import org.hisp.dhis.api.model.v42_4.TrackerTrackedEntity;
import org.hisp.dhis.api.model.v42_4.WebMessage;
import org.hisp.dhis.integration.sdk.api.Dhis2Client;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

public final class Environment
{
    public static final String ORG_UNIT_ID;

    private static final Network NETWORK = Network.newNetwork();

    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

    private static final GenericContainer<?> DHIS2_CONTAINER;

    private static final Dhis2Client DHIS2_CLIENT;

    static
    {
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse( "postgis/postgis:12-3.2-alpine" ).asCompatibleSubstituteFor( "postgres" ) )
                .withDatabaseName( "dhis2" )
                .withNetworkAliases( "db" )
                .withUsername( "dhis" )
                .withPassword( "dhis" ).withNetwork( NETWORK );

        POSTGRESQL_CONTAINER.start();

        DHIS2_CONTAINER = new GenericContainer<>(
            "dhis2/core:2.40.0" )
                .dependsOn( POSTGRESQL_CONTAINER )
                .withClasspathResourceMapping( "dhis.conf", "/opt/dhis2/dhis.conf", BindMode.READ_WRITE )
                .withNetwork( NETWORK ).withExposedPorts( 8080 )
                .waitingFor(
                    new HttpWaitStrategy().forStatusCode( 200 ).withStartupTimeout( Duration.ofSeconds( 120 ) ) )
                .withEnv( "WAIT_FOR_DB_CONTAINER", "db" + ":" + 5432 + " -t 0" );

        DHIS2_CONTAINER.start();

        String dhis2BaseApiUrl = "http://" + Environment.getDhis2Container().getHost() + ":"
            + Environment.getDhis2Container()
                .getFirstMappedPort()
            + "/api";

        Dhis2Client basicCredentialsDhis2Client = Dhis2ClientBuilder.newClient( dhis2BaseApiUrl, "admin", "district" )
            .build();

        List<String> allowedMethods = new ArrayList<>();
        allowedMethods.add( "GET" );
        allowedMethods.add( "POST" );
        allowedMethods.add( "DELETE" );
        allowedMethods.add( "PUT" );
        allowedMethods.add( "PATCH" );

        Map<String, Object> attribute = new HashMap<>();
        attribute.put( "allowedMethods", allowedMethods );
        attribute.put( "type", "MethodAllowedList" );

        List<Object> attributes = new ArrayList<>();
        attributes.add( attribute );

        WebMessage webMessage = basicCredentialsDhis2Client.post( "apiToken" )
            .withResource( new ApiToken().withAttributes( attributes )
                // FIXME: expire is set dynamically because 2.37 has expire type
                // erroneously declared as an integer instead of long
                .withAdditionalProperty( "expire", new Date().getTime() + 1000000 ) )
            .transfer().returnAs( WebMessage.class );
        String pat = (String) ((Map) webMessage.getResponse().get()).get( "key" );

        DHIS2_CLIENT = Dhis2ClientBuilder.newClient( dhis2BaseApiUrl, pat ).build();

        ORG_UNIT_ID = createOrgUnit();
        createOrgUnitLevel();
        createTrackerProgram();
        addOrgUnitToTrackerProgram( ORG_UNIT_ID );
        addOrgUnitToUser( ORG_UNIT_ID );
    }

    private Environment()
    {

    }

    private static void createTrackerProgram()
    {
        try
        {
            DHIS2_CLIENT.post( "trackedEntityTypes" )
                .withResource( new TrackedEntityType().withId( "MCPQUTHX1Ze" ).withName( "Person" )
                    .withCode( "GEN_LIB_TRK_PERSON" ) )
                .transfer().close();

            DHIS2_CLIENT.post( "trackedEntityAttributes" )
                .withResource(
                    new TrackedEntityAttribute().withId( "HlKXyR5qr2e" ).withValueType(
                        OptionSet.ValueType.TEXT ).withShortName( "Patient UID" )
                        .withName( "Patient UID" ).withUnique( true ).withFormName( "Patient UID" )
                        .withGenerated( true )
                        .withPattern( "RANDOM(XXX######)" )
                        .withCode( "IDS_AFI_PATIENT_UID" )
                        .withAggregationType( SourceRequest.AggregationType.COUNT ) )
                .transfer().close();

            DHIS2_CLIENT.post( "trackedEntityAttributes" )
                .withResource(
                    new TrackedEntityAttribute().withId( "oindugucx72" ).withValueType(
                        OptionSet.ValueType.TEXT ).withShortName( "Sex" ).withName( "GEN - Sex" )
                        .withFormName( "Sex" )
                        .withCode( "PATINFO_SEX" )
                        .withAggregationType( SourceRequest.AggregationType.NONE ) )
                .transfer().close();

            DHIS2_CLIENT.post( "trackedEntityAttributes" )
                .withResource(
                    new TrackedEntityAttribute().withId( "NI0QRzJvQ0k" ).withValueType(
                        OptionSet.ValueType.DATE ).withShortName( "Date of birth" )
                        .withName( "GEN - Date of birth" ).withFormName( "Date of birth" )
                        .withCode( "PATINFO_DOB" )
                        .withAggregationType( SourceRequest.AggregationType.COUNT ) )
                .transfer().close();

            DHIS2_CLIENT.post( "programs" )
                .withResource( new ProgramParams().withId( "w0qPtIW0JYu" ).withName( "AFI - Acute Febrile Illness" )
                    .withProgramTrackedEntityAttributes( Arrays.asList(
                        new ProgramTrackedEntityAttributeParams().withTrackedEntityAttribute(
                            new TrackedEntityAttributeParams().withId( "HlKXyR5qr2e" ) ),
                        new ProgramTrackedEntityAttributeParams().withTrackedEntityAttribute(
                            new TrackedEntityAttributeParams().withId( "oindugucx72" ) ),
                        new ProgramTrackedEntityAttributeParams().withTrackedEntityAttribute(
                            new TrackedEntityAttributeParams().withId( "NI0QRzJvQ0k" ) ) ) )
                    .withShortName( "AFI" )
                    .withCode( "IDS_AFI" ).withProgramType( ProgramParams.ProgramType.WITH_REGISTRATION )
                    .withTrackedEntityType( new TrackedEntityTypeRef().withId( "MCPQUTHX1Ze" ) ) )
                .transfer()
                .close();

            DHIS2_CLIENT.post( "programStages" )
                .withResource(
                    new ProgramStageParams().withName( "Case Report" )
                        .withProgram( new ProgramParams().withId( "w0qPtIW0JYu" ) ) )
                .transfer().close();
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static String createOrgUnit()
    {
        OrganisationUnit organisationUnit = new OrganisationUnit().withName( "Acme" ).withShortName( "Acme" )
            .withCode( "ACME" )
            .withOpeningDate( new Date() );

        return (String) ((Map) DHIS2_CLIENT.post( "organisationUnits" ).withResource( organisationUnit )
            .transfer()
            .returnAs( WebMessage.class ).getResponse().get()).get( "uid" );
    }

    public static List<String> createTestOrgUnits( int numberOfOrgUnits )
    {
        List<String> orgUnitIds = new ArrayList<>();
        for ( int i = 0; i < numberOfOrgUnits; i++ )
        {
            OrganisationUnit organisationUnit = new OrganisationUnit().withName( "Acme " + i )
                .withShortName( "Acme " + i )
                .withOpeningDate( new Date() );
            orgUnitIds.add( (String) ((Map) DHIS2_CLIENT.post( "organisationUnits" )
                .withResource( organisationUnit )
                .transfer()
                .returnAs( WebMessage.class ).getResponse().get()).get( "uid" ) );
        }
        return orgUnitIds;
    }

    public static void deleteTestOrgUnits( List<String> orgUnitIds )
    {
        for ( String orgUnitId : orgUnitIds )
        {
            DHIS2_CLIENT.delete( "organisationUnits/{orgUnitId}", orgUnitId ).transfer();
        }
    }

    private static void createOrgUnitLevel()
    {
        List<OrganisationUnitLevel> organisationUnitLevels = new ArrayList<>();
        organisationUnitLevels.add( new OrganisationUnitLevel().withName( "Level 1" ).withLevel( 1 ) );
        DHIS2_CLIENT.post( "filledOrganisationUnitLevels" )
            .withResource( Collections.singletonMap( "organisationUnitLevels",
                organisationUnitLevels ) )
            .transfer();
    }

    private static void addOrgUnitToUser( String orgUnitId )
    {
        DHIS2_CLIENT.post( "users/M5zQapPyTZI/organisationUnits/{organisationUnitId}", orgUnitId ).transfer();
    }

    private static void addOrgUnitToTrackerProgram( String orgUnitId )
    {
        DHIS2_CLIENT.post( "programs/w0qPtIW0JYu/organisationUnits/{orgUnitId}", orgUnitId )
            .transfer();
    }

    public static List<String> createTrackedEntitiesWithEnrollment( int numberOfTrackedEntities )
    {
        List<String> trackedEntities = new ArrayList<>();
        for ( int i = 0; i < numberOfTrackedEntities; i++ )
        {
            List<TrackerAttribute> attributes = new ArrayList<>();

            String uniqueSystemIdentifier = Environment.getDhis2Client()
                .get( "trackedEntityAttributes/HlKXyR5qr2e/generate" ).transfer()
                .returnAs( ReservedValue.class )
                .getValue().get();

            attributes.add(
                new TrackerAttribute().withAttribute( "HlKXyR5qr2e" ).withValue( uniqueSystemIdentifier ) );
            attributes.add( new TrackerAttribute().withAttribute( "oindugucx72" )
                .withValue( "Male" ) );
            attributes.add( new TrackerAttribute().withAttribute( "NI0QRzJvQ0k" )
                .withValue( "2023-01-01" ) );

            List<TrackerEnrollment> enrollment = new ArrayList<>();
            enrollment.add( new TrackerEnrollment()
                .withOrgUnit( ORG_UNIT_ID )
                .withProgram( "w0qPtIW0JYu" )
                .withEnrolledAt( new Date() )
                .withOccurredAt( new Date() )
                .withAttributes( attributes ) );

            TrackerTrackedEntity trackedEntity = new TrackerTrackedEntity().withOrgUnit( ORG_UNIT_ID )
                .withTrackedEntityType( "MCPQUTHX1Ze" )
                .withEnrollments( enrollment );

            String trackedEntityId = DHIS2_CLIENT.post( "tracker" )
                .withResource( new Body().withTrackedEntities( Collections.singletonList( trackedEntity ) ) )
                .withParameter( "async", "false" )
                .transfer()
                .returnAs( TrackerImportReport.class ).getBundleReport().get().getTypeReportMap().get()
                .getAdditionalProperties().get( "TRACKED_ENTITY" ).getObjectReports().get().get( 0 )
                .getUid().get();
            trackedEntities.add( trackedEntityId );

        }
        return trackedEntities;
    }

    public static void deleteTestTrackedEntities( List<String> trackedEntityIds )
        throws IOException
    {
        for ( String trackedEntity : trackedEntityIds )
        {
            DHIS2_CLIENT.delete( "trackedEntityInstances/{trackedEntityId}", trackedEntity )
                .transfer()
                .close();
        }
    }

    public static GenericContainer<?> getDhis2Container()
    {
        return DHIS2_CONTAINER;
    }

    public static Dhis2Client getDhis2Client()
    {
        return DHIS2_CLIENT;
    }
}

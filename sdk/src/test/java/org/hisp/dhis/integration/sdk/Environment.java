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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.hisp.dhis.api.model.v2_38_1.ApiToken;
import org.hisp.dhis.api.model.v2_38_1.Attribute__2;
import org.hisp.dhis.api.model.v2_38_1.Attributes;
import org.hisp.dhis.api.model.v2_38_1.DescriptiveWebMessage;
import org.hisp.dhis.api.model.v2_38_1.Enrollment;
import org.hisp.dhis.api.model.v2_38_1.Enrollment__2;
import org.hisp.dhis.api.model.v2_38_1.OrganisationUnit;
import org.hisp.dhis.api.model.v2_38_1.OrganisationUnitLevel;
import org.hisp.dhis.api.model.v2_38_1.TrackedEntity;
import org.hisp.dhis.api.model.v2_38_1.TrackerImportReport;
import org.hisp.dhis.api.model.v2_38_1.WebMessage;
import org.hisp.dhis.integration.sdk.api.Dhis2Client;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
            "dhis2/core:2.38.1-tomcat-8.5-jdk11-openjdk-slim" )
            .dependsOn( POSTGRESQL_CONTAINER )
            .withClasspathResourceMapping( "dhis.conf", "/DHIS2_home/dhis.conf", BindMode.READ_WRITE )
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
        String pat = (String) ((Map<String, Object>) webMessage.getResponse().get()).get( "key" );

        DHIS2_CLIENT = Dhis2ClientBuilder.newClient( dhis2BaseApiUrl, pat ).build();

        ORG_UNIT_ID = createOrgUnit();
        createOrgUnitLevel();
        String orgUnitLevelId = null;
        for ( OrganisationUnitLevel organisationUnitLevel : DHIS2_CLIENT.get( "organisationUnitLevels" )
            .withFields( "id" )
            .withoutPaging().transfer().returnAs( OrganisationUnitLevel.class, "organisationUnitLevels" ) )
        {
            orgUnitLevelId = organisationUnitLevel.getId().get();
        }
        importMetaData( Objects.requireNonNull( orgUnitLevelId ) );
        addOrgUnitToTrackerProgram( ORG_UNIT_ID );
        addOrgUnitToUser( ORG_UNIT_ID );
    }

    private Environment()
    {

    }

    private static String createOrgUnit()
    {
        OrganisationUnit organisationUnit = new OrganisationUnit().withName( "Acme" ).withShortName( "Acme" )
            .withCode( "ACME" )
            .withOpeningDate( new Date() );

        return (String) ((Map<String, Object>) DHIS2_CLIENT.post( "organisationUnits" ).withResource( organisationUnit )
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
            orgUnitIds.add( (String) ((Map<String, Object>) DHIS2_CLIENT.post( "organisationUnits" )
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

    public static List<String> createDhis2TrackedEntitiesWithEnrollment( int numberOfTrackedEntities )
        throws
        IOException
    {
        List<String> trackedEntities = new ArrayList<>();
        for ( int i = 0; i < numberOfTrackedEntities; i++ )
        {
            List<Attribute__2> attributes = new ArrayList<>();
            attributes.add(
                new Attribute__2().withAttribute( "HlKXyR5qr2e" ).withValue( String.format( "ID-%s", i ) ) );
            attributes.add( new Attribute__2().withAttribute( "oindugucx72" )
                .withValue( "Male" ) );
            attributes.add( new Attribute__2().withAttribute( "NI0QRzJvQ0k" )
                .withValue( "2023-01-01" ) );

            List<Enrollment__2> enrollment = new ArrayList<>();
            enrollment.add( new Enrollment__2()
                .withOrgUnit( ORG_UNIT_ID )
                .withProgram( "w0qPtIW0JYu" )
                .withEnrolledAt( "2023-01-01" )
                .withOccurredAt( "2023-01-01" )
                .withStatus( Enrollment__2.EnrollmentStatus.ACTIVE )
                .withAttributes( attributes ) );

            TrackedEntity trackedEntity = new TrackedEntity()
                .withOrgUnit( ORG_UNIT_ID )
                .withTrackedEntityType( "MCPQUTHX1Ze" )
                .withEnrollments( enrollment );

            String trackedEntityId = DHIS2_CLIENT.post( "tracker" )
                .withResource( Collections.singletonMap( "trackedEntities", Collections.singletonList( trackedEntity ) ) )
                .withParameter( "async", "false" )
                .transfer()
                .returnAs( TrackerImportReport.class ).getBundleReport().get().getTypeReportMap().get()
                .getAdditionalProperties().get( "TRACKED_ENTITY" ).getObjectReports().get().get( 0 )
                .getUid().get();
            trackedEntities.add( trackedEntityId );

        }
        return trackedEntities;
    }

    public static void deleteDhis2TrackedEntities( List<String> trackedEntities )
        throws
        IOException
    {
        for ( String trackedEntity : trackedEntities )
        {
            DHIS2_CLIENT.delete( "trackedEntityInstances/{trackedEntityId}", trackedEntity )
                .transfer()
                .close();
        }
    }

    private static void importMetaData( String orgUnitLevelId )
    {
        String metaData = null;
        try ( InputStream inputStream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream( "IDS_AFI_COMPLETE_1.0.0_DHIS2.38.json" );
            BufferedReader reader = new BufferedReader(
                new InputStreamReader( inputStream, Charset.defaultCharset() ) ) )
        {
            String content = reader.lines().collect( Collectors.joining( "\n" ) );
            metaData = content.replaceAll( "<OU_LEVEL_DISTRICT_UID>", orgUnitLevelId );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        WebMessage webMessage = DHIS2_CLIENT.post( "metadata" )
            .withResource( metaData )
            .withParameter( "atomicMode", "NONE" ).transfer().returnAs( WebMessage.class );
        assertEquals( DescriptiveWebMessage.Status.OK, webMessage.getStatus().get() );
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

# DHIS2 Java SDK

![Maven Central](https://img.shields.io/maven-central/v/org.hisp.dhis.integration.sdk/dhis2-java-sdk)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/org.hisp.dhis.integration.sdk/dhis2-java-sdk?server=https%3A%2F%2Foss.sonatype.org)
![Build Status](https://github.com/dhis2/dhis2-java-sdk/workflows/CI/badge.svg)

DHIS2 Java SDK is a _lightweight_ library that hides the nuts and bolts of DHIS2 Web API interactions behind a fluent Java API and type-safe resource models. It is powered by [OkHttp](https://square.github.io/okhttp/) and compatible with Android.

## Minimum Requirements

- Java 8

### Android

- Android 5.0 (API Level 21)

## Binaries

### Maven Release Distribution

#### Java API
```xml
<project>
    ...
    <dependencies>
        <dependency>
            <groupId>org.hisp.dhis.integration.sdk</groupId>
            <artifactId>jackson-resource-model</artifactId>
            <classifier>[v42.4|v41.5|v40.2.2|v40.0|v2.39.1|v2.38.7|v2.37.7|v2.36.11|v2.35.13]</classifier>
            <version>3.0.1</version>
        </dependency>
        <dependency>
            <groupId>org.hisp.dhis.integration.sdk</groupId>
            <artifactId>dhis2-java-sdk</artifactId>
            <version>3.0.1</version>
        </dependency>
        ...
    </dependencies>
</project>
```

#### Android API
```xml
<project>
    ...
    <dependencies>
        <dependency>
            <groupId>org.hisp.dhis.integration.sdk</groupId>
            <artifactId>android-jackson-resource-model</artifactId>
            <classifier>[v42.4|v41.5|v40.2.2|v40.0|v2.39.1|v2.38.7|v2.37.7|v2.36.11|v2.35.13]</classifier>
            <version>3.0.1</version>
        </dependency>
        <dependency>
            <groupId>org.hisp.dhis.integration.sdk</groupId>
            <artifactId>dhis2-java-sdk</artifactId>
            <version>3.0.1</version>
        </dependency>
        ...
    </dependencies>
</project>
```

### Maven Snapshot Distribution

```xml
<project>
    ...
    <dependencies>
        ...
        <dependency>
            <groupId>org.hisp.dhis.integration.sdk</groupId>
            <artifactId>dhis2-java-sdk</artifactId>
            <version>3.0.2-SNAPSHOT</version>
        </dependency>
        ...
    </dependencies>
    
    <repositories>
        <repository>
            <id>oss.sonatype.org</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
        ...
    </repositories>
</project>
```

## Basic Usage Examples

Create a client that authenticates with a personal access token:

```java
import org.hisp.dhis.integration.sdk.Dhis2ClientBuilder;
import org.hisp.dhis.integration.sdk.api.Dhis2Client;
...
...
    
Dhis2Client dhis2Client = Dhis2ClientBuilder.newClient( "https://play.dhis2.org/40.2.2/api", "d2pat_apheulkR1x7ac8vr9vcxrFkXlgeRiFc94200032556" ).build();
```

Create a client that authenticates with basic credentials:

```java
import org.hisp.dhis.integration.sdk.Dhis2ClientBuilder;
import org.hisp.dhis.integration.sdk.api.Dhis2Client;
...

Dhis2Client dhis2Client = Dhis2ClientBuilder.newClient( "https://play.dhis2.org/40.2.2/api", "admin", "district" ).build();
```

Fetch an organisation unit:

```java
import org.hisp.dhis.api.model.v40_2_2.OrganisationUnit;
...
    
OrganisationUnit organisationUnit = dhis2Client.get( "organisationUnits/{id}", "fdc6uOvgoji" ).transfer()
            .returnAs( OrganisationUnit.class );
```

Fetch all organisation units:

```java
import org.hisp.dhis.api.model.v40_2_2.OrganisationUnit;
...
    
Iterable<OrganisationUnit> organisationUnits = dhis2Client.get( "organisationUnits" )
        .withoutPaging().transfer().returnAs( OrganisationUnit.class, "organisationUnits" );

for ( OrganisationUnit organisationUnit : organisationUnits )
{
    ...
}
```

Fetch organisation units over multiple pages:

```java
import org.hisp.dhis.api.model.v40_2_2.OrganisationUnit;
...
    
Iterable<OrganisationUnit> organisationUnits = dhis2Client.get( "organisationUnits" )
        .withPaging().transfer().returnAs( OrganisationUnit.class, "organisationUnits" );

for ( OrganisationUnit organisationUnit : organisationUnits )
{
    ...
}
```

Fetch all organisation units IDs over multiple pages:

```java
import org.hisp.dhis.api.model.v40_2_2.OrganisationUnit;
...
    
Iterable<OrganisationUnit> organisationUnits = dhis2Client.get( "organisationUnits" )
            .withField( "id" )
            .withPaging().transfer().returnAs( OrganisationUnit.class, "organisationUnits" );

for ( OrganisationUnit organisationUnit : organisationUnits )
{
    ...
}
```

Fetch organisation units belonging to the third level of the organisation unit hierarchy over multiple pages:

```java
import org.hisp.dhis.api.model.v40_2_2.OrganisationUnit;
...
    
Iterable<OrganisationUnit> organisationUnits = dhis2Client.get( "organisationUnits" )
            .withFilter( "level:eq:3" )
            .withPaging().transfer().returnAs( OrganisationUnit.class, "organisationUnits" );

for ( OrganisationUnit organisationUnit : organisationUnits )
{
    ...
}
```

Create a Tracked Entity Instance:

```java
import org.hisp.dhis.api.model.v40_2_2.AttributeInfo;
import org.hisp.dhis.api.model.v40_2_2.Body;
import org.hisp.dhis.api.model.v40_2_2.EnrollmentInfo;
import org.hisp.dhis.api.model.v40_2_2.ReservedValue;
import org.hisp.dhis.api.model.v40_2_2.TrackedEntityInfo;
import org.hisp.dhis.api.model.v40_2_2.TrackerImportReport;
...

String uniqueSystemIdentifier = dhis2Client
    .get( "trackedEntityAttributes/HlKXyR5qr2e/generate" ).transfer()
    .returnAs( ReservedValue.class )
    .getValue().get();

TrackerImportReport trackerImportReport = dhis2Client.post( "tracker" )
    .withResource( new Body().withTrackedEntities( Arrays.asList( new TrackedEntityInfo()
        .withOrgUnit( orgUnitId )
        .withTrackedEntityType( "MCPQUTHX1Ze" )
        .withEnrollments( Arrays.asList( new EnrollmentInfo()
            .withOrgUnit( orgUnitId )
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
    ...
}
```

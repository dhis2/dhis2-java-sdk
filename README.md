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
            <classifier>[v2.39.1|v2.38.1|v2.37.7|v2.36.11|v2.35.13]</classifier>
            <version>2.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.hisp.dhis.integration.sdk</groupId>
            <artifactId>dhis2-java-sdk</artifactId>
            <version>2.0.0</version>
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
            <classifier>[v2.39.1|v2.38.1|v2.37.7|v2.36.11|v2.35.13]</classifier>
            <version>2.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.hisp.dhis.integration.sdk</groupId>
            <artifactId>dhis2-java-sdk</artifactId>
            <version>2.0.0</version>
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
            <version>2.0.1-SNAPSHOT</version>
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
    
Dhis2Client dhis2Client = Dhis2ClientBuilder.newClient( "https://play.dhis2.org/2.37.7/api", "d2pat_apheulkR1x7ac8vr9vcxrFkXlgeRiFc94200032556" ).build()
```

Create a client that authenticates with basic credentials:

```java
import org.hisp.dhis.integration.sdk.Dhis2ClientBuilder;
import org.hisp.dhis.integration.sdk.api.Dhis2Client;
...

Dhis2Client dhis2Client = Dhis2ClientBuilder.newClient( "https://play.dhis2.org/2.37.7/api", "admin", "district" ).build()
```

Fetch an organisation unit:

```java
import org.hisp.dhis.api.model.v2_37_7.OrganisationUnit;
...
    
OrganisationUnit organisationUnit = dhis2Client.get( "organisationUnits/{id}", "fdc6uOvgoji" ).transfer()
            .returnAs( OrganisationUnit.class );
```

Fetch all organisation units:

```java
import org.hisp.dhis.api.model.v2_37_7.OrganisationUnit;
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
import org.hisp.dhis.api.model.v2_37_7.OrganisationUnit;
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
import org.hisp.dhis.api.model.v2_37_7.OrganisationUnit;
...
    
Iterable<OrganisationUnit> organisationUnits = dhis2Client.get( "organisationUnits" )
            .withFields( "id" )
            .withPaging().transfer().returnAs( OrganisationUnit.class, "organisationUnits" );

for ( OrganisationUnit organisationUnit : organisationUnits )
{
    ...
}
```

Fetch organisation units belonging to the third level of the organisation unit hierarchy over multiple pages:

```java
import org.hisp.dhis.api.model.v2_37_7.OrganisationUnit;
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
import org.hisp.dhis.api.model.v2_37_7.AggregationType;
import org.hisp.dhis.api.model.v2_37_7.AnalyticsPeriodBoundary;
import org.hisp.dhis.api.model.v2_37_7.Attribute;
import org.hisp.dhis.api.model.v2_37_7.AttributeValue;
import org.hisp.dhis.api.model.v2_37_7.Attribute__1;
import org.hisp.dhis.api.model.v2_37_7.CategoryCombo;
import org.hisp.dhis.api.model.v2_37_7.DataElement;
import org.hisp.dhis.api.model.v2_37_7.DataValue__2;
import org.hisp.dhis.api.model.v2_37_7.DescriptiveWebMessage;
import org.hisp.dhis.api.model.v2_37_7.Enrollment;
import org.hisp.dhis.api.model.v2_37_7.Event;
import org.hisp.dhis.api.model.v2_37_7.EventChart;
import org.hisp.dhis.api.model.v2_37_7.ImportSummaries;
import org.hisp.dhis.api.model.v2_37_7.OptionSet;
import org.hisp.dhis.api.model.v2_37_7.OrganisationUnit;
import org.hisp.dhis.api.model.v2_37_7.OrganisationUnitLevel;
import org.hisp.dhis.api.model.v2_37_7.Program;
import org.hisp.dhis.api.model.v2_37_7.ProgramIndicator;
import org.hisp.dhis.api.model.v2_37_7.TrackedEntityAttributeValue;
import org.hisp.dhis.api.model.v2_37_7.TrackedEntityInstance;
import org.hisp.dhis.api.model.v2_37_7.WebMessage;
...

TrackedEntityInstance tei = new TrackedEntityInstance().withAttributes(
    List.of( new Attribute__1().withAttribute( "KSr2yTdu1AI" ).withValue( uniqueSystemIdentifier ),
        new Attribute__1().withAttribute( "NI0QRzJvQ0k" ).withValue( "2022-01-18" ),
        new Attribute__1().withAttribute( "ftFBu8mHZ4H" ).withValue( "John" ),
        new Attribute__1().withAttribute( "EpbquVl5OD6" ).withValue( "Doe" ) ) )
    .withEnrollments( List.of(
        new Enrollment().withEnrollmentDate( new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2022-01-19" ) )
            .withProgram( "SSLpOM0r1U7" ).withOrgUnit( orgUnitId )
            .withStatus( Event.EnrollmentStatus.ACTIVE )
            .withEvents( List.of(
                new Event().withStatus( EventChart.EventStatus.ACTIVE ).withDueDate( "2022-01-19" )
                    .withEventDate( "2022-01-19" ).withProgramStage( "RcbCl5ww8XY" )
                    .withProgram( "SSLpOM0r1U7" ).withOrgUnit( orgUnitId ).withDataValues( List.of(
                        new DataValue__2().withDataElement( "ABhkInP0wGY" ).withValue( "HOME" )
                            .withProvidedElsewhere( false ) ) ),
                new Event().withStatus( EventChart.EventStatus.SCHEDULE ).withDueDate( "2022-01-19" )
                    .withProgramStage( "s53RFfXA75f" ).withProgram( "SSLpOM0r1U7" )
                    .withOrgUnit( orgUnitId ) ) ) ) )
    .withOrgUnit( orgUnitId )
    .withTrackedEntityType( "MCPQUTHX1Ze" );

WebMessage webMessage = dhis2Client.post( "trackedEntityInstances" )
    .withResource( tei ).transfer().returnAs(
        WebMessage.class );

if ( !webMessage.getStatus().get().equals( DescriptiveWebMessage.Status.OK ) )
{
    ...
}
```

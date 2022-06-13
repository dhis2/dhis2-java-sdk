package org.hisp.dhis.integration.sdk.api;

import okhttp3.OkHttpClient;
import org.hisp.dhis.integration.sdk.api.operation.DeleteOperation;
import org.hisp.dhis.integration.sdk.api.operation.GetOperation;
import org.hisp.dhis.integration.sdk.api.operation.PatchOperation;
import org.hisp.dhis.integration.sdk.api.operation.PostOperation;
import org.hisp.dhis.integration.sdk.api.operation.PutOperation;

public interface Dhis2Client
{
    PostOperation post( String path, String... pathParams );

    PutOperation put( String path, String... pathParams );
    GetOperation get( String path, String... pathParams );

    PatchOperation patch( String path, String... pathParams );

    DeleteOperation delete( String path, String... pathParams );

    OkHttpClient getHttpClient();
}

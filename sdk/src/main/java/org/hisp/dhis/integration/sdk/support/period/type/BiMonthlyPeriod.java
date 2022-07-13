package org.hisp.dhis.integration.sdk.support.period.type;

import java.util.Calendar;

public class BiMonthlyPeriod extends SixMonthlyPeriod
{
    public BiMonthlyPeriod( Calendar calendar )
    {
        super( calendar );
        idAdditionalString = "B";
        durationInMonths = 2;
        startMonth = Calendar.JANUARY;
    }

    @Override
    protected String formatTime()
    {
        int periodNumber = calendar.get( Calendar.MONTH ) / durationInMonths + 1;
        return idFormatter.format( calendar.getTime() ) + String.format( "%02d", periodNumber ) + idAdditionalString;
    }
}

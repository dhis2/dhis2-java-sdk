package org.hisp.dhis.integration.sdk.support.period.type;

import java.util.Calendar;

public class DailyPeriod extends AbstractPeriod
{
    public DailyPeriod( Calendar calendar )
    {
        super( calendar, "yyyyMMdd" );
    }

    @Override
    protected void moveToStartOfCurrentPeriod()
    {
    }

    @Override
    protected void moveToStartOfCurrentYear()
    {
        calendar.set( Calendar.DAY_OF_YEAR, 1 );
    }

    @Override
    protected void movePeriods( int number )
    {
        calendar.add( Calendar.DATE, number );
    }
}

package org.hisp.dhis.integration.sdk.support.period;

import java.util.Calendar;

class MonthlyPeriod extends AbstractPeriod
{
    MonthlyPeriod( Calendar calendar )
    {
        super( calendar, "yyyyMM" );
    }

    @Override
    protected void moveToStartOfCurrentPeriod()
    {
        calendar.set( Calendar.DAY_OF_MONTH, 1 );
    }

    @Override
    protected void moveToStartOfCurrentYear()
    {
        calendar.set( Calendar.DAY_OF_YEAR, 1 );
    }

    @Override
    protected void movePeriods( int number )
    {
        calendar.add( Calendar.MONTH, number );
    }
}
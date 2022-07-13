package org.hisp.dhis.integration.sdk.support.period.type;

import java.util.Calendar;

public class YearlyPeriod extends AbstractPeriod
{
    protected final int firstMonth;

    protected final String suffix;

    public YearlyPeriod( Calendar calendar, int firstMonth, String suffix )
    {
        super( calendar, "yyyy" );
        this.firstMonth = firstMonth;
        this.suffix = suffix;
    }

    @Override
    protected void moveToStartOfCurrentPeriod()
    {
        calendar.set( Calendar.DATE, 1 );
        if ( calendar.get( Calendar.MONTH ) < firstMonth )
        {
            calendar.add( Calendar.YEAR, -1 );
        }
        calendar.set( Calendar.MONTH, firstMonth );
    }

    @Override
    protected void moveToStartOfCurrentYear()
    {
        calendar.set( Calendar.DATE, 1 );
        calendar.set( Calendar.MONTH, firstMonth );
    }

    @Override
    protected String formatTime()
    {
        return idFormatter.format( calendar.getTime() ) + suffix;
    }

    @Override
    protected void movePeriods( int number )
    {
        calendar.add( Calendar.YEAR, number );
    }
}

package org.hisp.dhis.integration.sdk.support.period.type;

import java.util.Calendar;

public class SixMonthlyPeriod extends AbstractPeriod
{
    protected int durationInMonths;

    protected String idAdditionalString;

    protected int startMonth;

    public SixMonthlyPeriod( Calendar calendar )
    {
        super( calendar, "yyyy" );
        idAdditionalString = "S";
        durationInMonths = 6;
        startMonth = Calendar.JANUARY;
    }

    @Override
    protected void moveToStartOfCurrentPeriod()
    {
        calendar.set( Calendar.DATE, 1 );
        int currentMonth = calendar.get( Calendar.MONTH );
        int monthsFromStart = (currentMonth - startMonth + 12) % durationInMonths;
        int currentPeriodStartMonth = (currentMonth - monthsFromStart + 12) % 12;
        if ( currentMonth - monthsFromStart < 0 )
        {
            calendar.add( Calendar.YEAR, -1 );
        }
        calendar.set( Calendar.MONTH, currentPeriodStartMonth );
    }

    @Override
    protected void moveToStartOfCurrentYear()
    {
        calendar.set( Calendar.DATE, 1 );
        if ( startMonth >= 6 )
        {
            calendar.add( Calendar.YEAR, -1 );
        }
        calendar.set( Calendar.MONTH, startMonth );
    }

    @Override
    protected void movePeriods( int number )
    {
        calendar.add( Calendar.MONTH, durationInMonths * number );
    }

    @Override
    protected String formatTime()
    {
        Calendar calendarCopy = (Calendar) calendar.clone();
        if ( calendarCopy.get( Calendar.MONTH ) < startMonth )
        {
            calendarCopy.add( Calendar.YEAR, -1 );
        }
        int periodNumber = ((calendarCopy.get( Calendar.MONTH ) - startMonth + 12) % 12) / durationInMonths + 1;
        String year = idFormatter.format( calendarCopy.getTime() );

        return year + idAdditionalString + periodNumber;
    }
}

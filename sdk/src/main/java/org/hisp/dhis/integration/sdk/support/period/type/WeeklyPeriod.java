package org.hisp.dhis.integration.sdk.support.period.type;

import java.util.Calendar;
import java.util.Date;

public class WeeklyPeriod extends AbstractPeriod
{
    private final int weekStartDay;

    private final String suffix;

    public WeeklyPeriod( Calendar calendar, int weekStartDay, String suffix )
    {
        super( calendar, "yyyy" );
        this.weekStartDay = weekStartDay;
        this.suffix = suffix;
    }

    @Override
    protected void moveToStartOfCurrentPeriod()
    {
        calendar.getTime();
        calendar.setFirstDayOfWeek( weekStartDay );
        calendar.setMinimalDaysInFirstWeek( 4 );
        setDayOfWeek( calendar, weekStartDay );
    }

    @Override
    protected void moveToStartOfCurrentYear()
    {
        moveToStartOfCurrentPeriod();
        calendar.set( Calendar.WEEK_OF_YEAR, 1 );
    }

    @Override
    protected void movePeriods( int number )
    {
        calendar.add( Calendar.WEEK_OF_YEAR, number );
    }

    @Override
    protected String formatTime()
    {
        Calendar cal = (Calendar) calendar.clone();
        setDayOfWeek( cal, weekStartDay + 3 );
        Date fourthWeekDay = cal.getTime();
        String year = idFormatter.format( fourthWeekDay );
        Integer weekOfYear = cal.get( Calendar.WEEK_OF_YEAR );
        return year + suffix + weekOfYear;
    }
}
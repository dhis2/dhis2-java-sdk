package org.hisp.dhis.integration.sdk.support.period.type;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class AbstractPeriod
{
    private static final int WEEK_DAYS = 7;

    private final Calendar initialCalendar;

    protected Calendar calendar;

    final SimpleDateFormat idFormatter;

    AbstractPeriod( Calendar calendar, String dateFormatStr )
    {
        this.initialCalendar = (Calendar) calendar.clone();
        this.calendar = (Calendar) calendar.clone();
        this.idFormatter = new SimpleDateFormat( dateFormatStr, Locale.US );
    }

    public synchronized final String createPeriod( Date date, int periodOffset )
    {
        this.calendar = (Calendar) initialCalendar.clone();

        moveToStartOfThePeriodOfADayWithOffset( date, periodOffset );

        String periodId = formatTime();
        this.movePeriods( 1 );
        calendar.add( Calendar.MILLISECOND, -1 );

        moveToStartOfThePeriodOfADayWithOffset( date, periodOffset );

        return periodId;
    }

    private void moveToStartOfThePeriodOfADayWithOffset( Date date, int periodOffset )
    {
        this.calendar.setTime( date );
        setCalendarToStartTimeOfADay( calendar );
        moveToStartOfCurrentPeriod();
        this.movePeriods( periodOffset );
    }

    static void setCalendarToStartTimeOfADay( Calendar calendar )
    {
        calendar.set( Calendar.HOUR_OF_DAY, 0 );
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );
    }

    protected abstract void moveToStartOfCurrentPeriod();

    protected abstract void movePeriods( int number );

    protected String formatTime()
    {
        return idFormatter.format( calendar.getTime() );
    }

    protected abstract void moveToStartOfCurrentYear();

    protected void setDayOfWeek( Calendar calendar, int targetDayOfWeek )
    {
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        int currentDay = calendar.get( Calendar.DAY_OF_WEEK );
        int diff = (targetDayOfWeek - firstDayOfWeek + WEEK_DAYS) % WEEK_DAYS -
            (currentDay - firstDayOfWeek + WEEK_DAYS) % WEEK_DAYS;

        calendar.add( Calendar.DATE, diff );
    }
}

package org.hisp.dhis.integration.sdk.support.period;

import java.util.Calendar;
import java.util.Date;

public final class PeriodBuilder
{
    private PeriodBuilder() {

    }

    public static String weekOf( Date date )
    {
        return new WeeklyPeriod( Calendar.getInstance(), Calendar.MONDAY, "W" ).createPeriod( date, 0 );
    }

    public static String monthOf( Date date )
    {
        return new MonthlyPeriod( Calendar.getInstance() ).createPeriod( date, 0 );
    }

    public static String yearOf( Date date )
    {
        return new YearlyPeriod( Calendar.getInstance(), Calendar.JANUARY, "" ).createPeriod( date, 0 );
    }
}

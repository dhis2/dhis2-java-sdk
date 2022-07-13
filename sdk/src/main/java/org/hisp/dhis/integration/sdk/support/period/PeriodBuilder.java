package org.hisp.dhis.integration.sdk.support.period;

import org.hisp.dhis.integration.sdk.support.period.type.BiMonthlyPeriod;
import org.hisp.dhis.integration.sdk.support.period.type.DailyPeriod;
import org.hisp.dhis.integration.sdk.support.period.type.FinancialYearlyNovPeriod;
import org.hisp.dhis.integration.sdk.support.period.type.MonthlyPeriod;
import org.hisp.dhis.integration.sdk.support.period.type.SixMonthlyPeriod;
import org.hisp.dhis.integration.sdk.support.period.type.WeeklyPeriod;
import org.hisp.dhis.integration.sdk.support.period.type.YearlyPeriod;

import java.util.Calendar;
import java.util.Date;

public final class PeriodBuilder
{
    private PeriodBuilder()
    {

    }

    public static String dayOf( Date date )
    {
        return new DailyPeriod( Calendar.getInstance() ).createPeriod( date, 0 );
    }

    public static String dayOf( Date date, int offset )
    {
        return new DailyPeriod( Calendar.getInstance() ).createPeriod( date, offset );
    }

    public static String weekOf( Date date )
    {
        return new WeeklyPeriod( Calendar.getInstance(), Calendar.MONDAY, "W" ).createPeriod( date, 0 );
    }

    public static String weekOf( Date date, int offset )
    {
        return new WeeklyPeriod( Calendar.getInstance(), Calendar.MONDAY, "W" ).createPeriod( date, offset );
    }

    public static String monthOf( Date date )
    {
        return new MonthlyPeriod( Calendar.getInstance() ).createPeriod( date, 0 );
    }

    public static String monthOf( Date date, int offset )
    {
        return new MonthlyPeriod( Calendar.getInstance() ).createPeriod( date, offset );
    }

    public static String biMonthOf( Date date )
    {
        return new BiMonthlyPeriod( Calendar.getInstance() ).createPeriod( date, 0 );
    }

    public static String biMonthOf( Date date, int offset )
    {
        return new BiMonthlyPeriod( Calendar.getInstance() ).createPeriod( date, offset );
    }

    public static String sixMonthOf( Date date )
    {
        return new SixMonthlyPeriod( Calendar.getInstance() ).createPeriod( date, 0 );
    }

    public static String sixMonthOf( Date date, int offset )
    {
        return new SixMonthlyPeriod( Calendar.getInstance() ).createPeriod( date, offset );
    }

    public static String financialYearStartingNovOf( Date date )
    {
        return new FinancialYearlyNovPeriod( Calendar.getInstance(), Calendar.NOVEMBER, "Nov" ).createPeriod( date, 0 );
    }

    public static String financialYearStartingNovOf( Date date, int offset )
    {
        return new FinancialYearlyNovPeriod( Calendar.getInstance(), Calendar.NOVEMBER, "Nov" ).createPeriod( date, offset );
    }

    public static String yearOf( Date date )
    {
        return new YearlyPeriod( Calendar.getInstance(), Calendar.JANUARY, "" ).createPeriod( date, 0 );
    }

    public static String yearOf( Date date, int offset )
    {
        return new YearlyPeriod( Calendar.getInstance(), Calendar.JANUARY, "" ).createPeriod( date, offset );
    }
}

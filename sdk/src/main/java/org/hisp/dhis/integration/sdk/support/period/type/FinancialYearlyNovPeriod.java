package org.hisp.dhis.integration.sdk.support.period.type;

import java.util.Calendar;
import java.util.Date;

public class FinancialYearlyNovPeriod extends YearlyPeriod
{
    public FinancialYearlyNovPeriod( Calendar calendar, int firstMonth, String suffix )
    {
        super( calendar, firstMonth, suffix );
    }

    @Override
    protected String formatTime()
    {
        calendar.add( Calendar.YEAR, +1 );
        Date date = calendar.getTime();
        calendar.add( Calendar.YEAR, -1 );
        return idFormatter.format( date ) + suffix;
    }
}

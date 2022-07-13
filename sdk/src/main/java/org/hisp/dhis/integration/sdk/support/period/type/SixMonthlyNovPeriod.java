package org.hisp.dhis.integration.sdk.support.period.type;

import java.util.Calendar;
import java.util.Date;

public class SixMonthlyNovPeriod extends SixMonthlyPeriod
{
    public SixMonthlyNovPeriod( Calendar calendar )
    {
        super( calendar );
        idAdditionalString = "NovS";
        durationInMonths = 6;
        startMonth = Calendar.NOVEMBER;
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

        calendarCopy.add( Calendar.YEAR, +1 );
        Date date = calendarCopy.getTime();
        calendarCopy.add( Calendar.YEAR, -1 );
        String year = idFormatter.format( date );

        return year + idAdditionalString + periodNumber;
    }
}

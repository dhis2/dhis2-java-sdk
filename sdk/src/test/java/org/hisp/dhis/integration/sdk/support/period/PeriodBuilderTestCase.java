package org.hisp.dhis.integration.sdk.support.period;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeriodBuilderTestCase
{
    @Test
    public void testWeekOf()
    {
        assertEquals( "2022W28", PeriodBuilder.weekOf( new Date( 1657626227255L ) ) );
    }

    @Test
    public void testMonthOf()
    {
        assertEquals( "202207", PeriodBuilder.monthOf( new Date( 1657626227255L ) ) );
    }

    @Test
    public void testYearOf()
    {
        assertEquals( "2022", PeriodBuilder.yearOf( new Date( 1657626227255L ) ) );
    }
}
